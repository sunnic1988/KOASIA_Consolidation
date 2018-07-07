package reports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import koasiaco.DBUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class RunIRTbyPeriod {
	//根据irt结构，和自定义的查询方法，查询出Profit and loss的数据
	public static JSONArray ProfitLoss(String Company, String currency, String Fiscal_Year) throws SQLException {
		JSONObject jsonObj = new JSONObject();
		JSONObject jsonObjTBV = new JSONObject();
		JSONObject jsonObjDM = new JSONObject();
		JSONObject jsonObjGM = new JSONObject();
		JSONObject jsonObjOP = new JSONObject();
		JSONObject jsonObjNOP = new JSONObject();
		JSONObject jsonObjIBT = new JSONObject();
		JSONArray jsonRsArray = new JSONArray();
		String companycode = Company;

		Connection conn = DBUtil.getConnection();
		Statement stmt = conn.createStatement();
		Statement stmtTBV = conn.createStatement();

		jsonObj = getResult(stmt, companycode, "'REVENUES'", "REVENUES", currency, Fiscal_Year, -1);
		jsonRsArray.add(0, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Change in Stockvalue'", "Change in Stockvalue", currency, Fiscal_Year,
				-1);
		jsonRsArray.add(1, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Other Capitalized Costs'", "Other Capitalized Costs", currency,
				Fiscal_Year, -1);
		jsonRsArray.add(2, jsonObj);

		jsonObjTBV = getResult(stmt, companycode, "'REVENUES','Change in Stockvalue','Other Capitalized Costs'",
				"Total Business Volume", currency, Fiscal_Year, -1);
		jsonRsArray.add(3, jsonObjTBV);

		jsonObjDM = getResult(stmt, companycode, "'Direct Material'", "Direct Material", currency, Fiscal_Year, 1);
		jsonRsArray.add(4, jsonObjDM);

		jsonObj = getRatioTBV(stmt, stmtTBV, companycode, "'Direct Material'", "in % of Total Business Volume",
				currency, Fiscal_Year, -1);
		jsonRsArray.add(5, jsonObj);

		jsonObj = getPLstructure(stmt, companycode, "'001 Material Handling'", "Material Handling", currency,
				Fiscal_Year, 1);
		jsonRsArray.add(6, jsonObj);

		jsonObj = getPLstructure(stmt, companycode, "'002 Direct Labor'", "Direct Labor", currency, Fiscal_Year, 1);
		jsonRsArray.add(7, jsonObj);

		jsonObj = getPLstructure(stmt, companycode, "'003 Indirect Labor'", "Indirect Labor", currency, Fiscal_Year, 1);
		jsonRsArray.add(8, jsonObj);

		jsonObj = getPLstructure(stmt, companycode, "'004 Depn of Machinery'", "Depreciation of Machinery", currency,
				Fiscal_Year, 1);
		jsonRsArray.add(9, jsonObj);

		jsonObj = getPLstructure(stmt, companycode,
				"'005 Other Manufacturing Cost Direct','006 Other Manufacturing Cost Indirect'",
				"Other Manufacturing Costs", currency, Fiscal_Year, 1);
		jsonRsArray.add(10, jsonObj);

		jsonObj = getPLstructure(stmt, companycode,
				"'001 Material Handling','002 Direct Labor','003 Indirect Labor','004 Depn of Machinery','005 Other Manufacturing Cost Direct','006 Other Manufacturing Cost Indirect'",
				"Manufacturing Costs", currency, Fiscal_Year, 1);
		jsonObj = jsonConbinePlus(jsonObj, jsonObjDM, "Manufacturing Costs");
		jsonRsArray.add(11, jsonObj);

		jsonObjGM = jsonConbine(jsonObjTBV, jsonObj, "Gross Margin");
		jsonRsArray.add(12, jsonObjGM);

		jsonObj = jsonConbineRatio(jsonObjGM, jsonObjTBV, "in % of Total Business Volume");
		jsonRsArray.add(13, jsonObj);

		jsonObj = getPLstructure(stmt, companycode, "'007 Engineering'", "Engineering", currency, Fiscal_Year, 1);
		jsonRsArray.add(14, jsonObj);

		jsonObj = getPLstructure(stmt, companycode, "'008 Administration'", "Administration", currency, Fiscal_Year, 1);
		jsonRsArray.add(15, jsonObj);

		jsonObj = getPLstructure(stmt, companycode, "'009 Sales'", "Sales", currency, Fiscal_Year, 1);
		jsonRsArray.add(16, jsonObj);

		jsonObj = getPLstructure(stmt, companycode, "'007 Engineering','008 Administration','009 Sales'",
				"Overhead Costs", currency, Fiscal_Year, 1);
		jsonRsArray.add(17, jsonObj);

		jsonObjOP = jsonConbine(jsonObjGM, jsonObj, "Operating Income");
		jsonRsArray.add(18, jsonObjOP);

		jsonObj = jsonConbineRatio(jsonObjOP, jsonObjTBV, "in % of Total Business Volume");
		jsonRsArray.add(19, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Monetary Corrections'", "Monetary Corrections", currency, Fiscal_Year,
				-1);
		jsonRsArray.add(20, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Exchange Gains/Losses'", "Exchange Gains/Losses", currency,
				Fiscal_Year, -1);
		jsonRsArray.add(21, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Interest Income / Expenses'", "Interest Income / Expenses", currency,
				Fiscal_Year, -1);
		jsonRsArray.add(22, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Other Income / Expenses'", "Other Income / Expenses", currency,
				Fiscal_Year, -1);
		jsonRsArray.add(23, jsonObj);

		jsonObjNOP = getResult(stmt, companycode,
				"'Monetary Corrections','Exchange Gains/Losses','Interest Income / Expenses','Other Income / Expenses'",
				"Non Operating Income", currency, Fiscal_Year, -1);
		jsonRsArray.add(24, jsonObjNOP);

		jsonObj = getRatioTBV(stmt, stmtTBV, companycode,
				"'Monetary Corrections','Exchange Gains/Losses','Interest Income / Expenses','Other Income / Expenses'",
				"in % of Total Business Volume", currency, Fiscal_Year, 1);
		jsonRsArray.add(25, jsonObj);

		jsonObjIBT = jsonConbinePlus(jsonObjNOP, jsonObjOP, "Income before Taxes");
		jsonRsArray.add(26, jsonObjIBT);

		jsonObj = jsonConbineRatio(jsonObjIBT, jsonObjTBV, "in % of Total Business Volume");
		jsonRsArray.add(27, jsonObj);

		stmt.close();
		stmtTBV.close();
		conn.close();

		return jsonRsArray;
	}
	//根据irt结构，和自定义的查询方法，查询operating costs
	public static JSONArray OpeartingCosts(String Company, String currency, String Fiscal_Year) throws SQLException {
		String companycode = Company;
		JSONObject jsonObj = new JSONObject();
		JSONObject jsonObjTOC = new JSONObject();
		JSONArray jsonRsArray = new JSONArray();

		Connection conn = DBUtil.getConnection();
		Statement stmt = conn.createStatement();
		Statement stmtTBV = conn.createStatement();

		jsonObj = getResult(stmt, companycode, "'Productive Wages'", "Productive Wages", currency, Fiscal_Year, 1);
		jsonRsArray.add(0, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Unproductive Wages'", "Unproductive Wages", currency, Fiscal_Year, 1);
		jsonRsArray.add(1, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Overtime Premium'", "Overtime Premium", currency, Fiscal_Year, 1);
		jsonRsArray.add(2, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Other Fringe Benefits Wages'", "Other Fringe Benefits Wages", currency,
				Fiscal_Year, 1);
		jsonRsArray.add(3, jsonObj);

		jsonObj = getResult(stmt, companycode,
				"'Productive Wages','Unproductive Wages','Overtime Premium','Other Fringe Benefits Wages'",
				"Total Wages", currency, Fiscal_Year, 1);
		jsonRsArray.add(4, jsonObj);

		jsonObj = getRatioTBV(stmt, stmtTBV, companycode,
				"'Productive Wages','Unproductive Wages','Overtime Premium','Other Fringe Benefits Wages'",
				"in % of Total Business Volume", currency, Fiscal_Year, -1);
		jsonRsArray.add(5, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Salaries'", "Salaries", currency, Fiscal_Year, 1);
		jsonRsArray.add(6, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Overtime'", "Overtime", currency, Fiscal_Year, 1);
		jsonRsArray.add(7, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Other Fringe Benefits'", "Other Fringe Benefits", currency,
				Fiscal_Year, 1);
		jsonRsArray.add(8, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Salaries','Overtime','Other Fringe Benefits'", "Total Salaries",
				currency, Fiscal_Year, 1);
		jsonRsArray.add(9, jsonObj);

		jsonObj = getRatioTBV(stmt, stmtTBV, companycode, "'Salaries','Overtime','Other Fringe Benefits'",
				"in % of Total Business Volume", currency, Fiscal_Year, -1);
		jsonRsArray.add(10, jsonObj);

		jsonObj = getResult(stmt, companycode,
				"'Productive Wages','Unproductive Wages','Overtime Premium','Other Fringe Benefits Wages','Salaries','Overtime','Other Fringe Benefits'",
				"Total Personnel Costs", currency, Fiscal_Year, 1);
		jsonRsArray.add(11, jsonObj);

		jsonObj = getRatioTBV(stmt, stmtTBV, companycode,
				"'Productive Wages','Unproductive Wages','Overtime Premium','Other Fringe Benefits Wages','Salaries','Overtime','Other Fringe Benefits'",
				"in % of Total Business Volume", currency, Fiscal_Year, -1);
		jsonRsArray.add(12, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Consumables'", "Consumables", currency, Fiscal_Year, 1);
		jsonRsArray.add(13, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Energy'", "Energy", currency, Fiscal_Year, 1);
		jsonRsArray.add(14, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Repairwork (external)'", "Repairwork (external)", currency,
				Fiscal_Year, 1);
		jsonRsArray.add(15, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Leasing/Depreciation (Build.)'", "Leasing/Depreciation (Build.)",
				currency, Fiscal_Year, 1);
		jsonRsArray.add(16, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Other Leasing Costs'", "Other Leasing Costs", currency, Fiscal_Year,
				1);
		jsonRsArray.add(17, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Other Depreciation'", "Other Depreciation", currency, Fiscal_Year, 1);
		jsonRsArray.add(18, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Engineering Cost Depreciation'", "Engineering Cost Depreciation",
				currency, Fiscal_Year, 1);
		jsonRsArray.add(19, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Interest Costs'", "Interest Costs", currency, Fiscal_Year, 1);
		jsonRsArray.add(20, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Taxes/Duties'", "Taxes/Duties", currency, Fiscal_Year, 1);
		jsonRsArray.add(21, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Insurances'", "Insurances", currency, Fiscal_Year, 1);
		jsonRsArray.add(22, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Travel and Subsistance'", "Travel and Subsistance", currency,
				Fiscal_Year, 1);
		jsonRsArray.add(23, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Telephone/Telex/Telefax/Mail'", "Telephone/Telex/Telefax/Mail",
				currency, Fiscal_Year, 1);
		jsonRsArray.add(24, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Legal/Audit'", "Legal/Audit", currency, Fiscal_Year, 1);
		jsonRsArray.add(25, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Airfreight'", "Airfreight", currency, Fiscal_Year, 1);
		jsonRsArray.add(26, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Surface Freight'", "Surface Freight", currency, Fiscal_Year, 1);
		jsonRsArray.add(27, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Canteen'", "Canteen", currency, Fiscal_Year, 1);
		jsonRsArray.add(28, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Royalties'", "Royalties", currency, Fiscal_Year, 1);
		jsonRsArray.add(29, jsonObj);

		jsonObj = getResult(stmt, companycode, "'GCSP'", "GCSP", currency, Fiscal_Year, 1);
		jsonRsArray.add(30, jsonObj);

		jsonObj = getResult(stmt, companycode, "'Other Costs'", "Other Costs", currency, Fiscal_Year, 1);
		jsonRsArray.add(31, jsonObj);

		jsonObj = getResult(stmt, companycode,
				"'Consumables','Energy','Repairwork (external)','Leasing/Depreciation (Build.)','Other Leasing Costs','Other Depreciation','Engineering Cost Depreciation','Interest Costs','Taxes/Duties','Insurances','Travel and Subsistance','Telephone/Telex/Telefax/Mail','Legal/Audit','Airfreight','Surface Freight','Canteen','Royalties','GCSP','Other Costs'",
				"Total Other Costs", currency, Fiscal_Year, 1);
		jsonRsArray.add(32, jsonObj);

		jsonObj = getRatioTBV(stmt, stmtTBV, companycode,
				"'Consumables','Energy','Repairwork (external)','Leasing/Depreciation (Build.)','Other Leasing Costs','Other Depreciation','Engineering Cost Depreciation','Interest Costs','Taxes/Duties','Insurances','Travel and Subsistance','Telephone/Telex/Telefax/Mail','Legal/Audit','Airfreight','Surface Freight','Canteen','Royalties','GCSP','Other Costs'",
				"in % of Total Business Volume", currency, Fiscal_Year, -1);
		jsonRsArray.add(33, jsonObj);

		jsonObjTOC = getResult(stmt, companycode,
				"'Productive Wages','Unproductive Wages','Overtime Premium','Other Fringe Benefits Wages','Salaries','Overtime','Other Fringe Benefits','Consumables','Energy','Repairwork (external)','Leasing/Depreciation (Build.)','Other Leasing Costs','Other Depreciation','Engineering Cost Depreciation','Interest Costs','Taxes/Duties','Insurances','Travel and Subsistance','Telephone/Telex/Telefax/Mail','Legal/Audit','Airfreight','Surface Freight','Canteen','Royalties','GCSP','Other Costs'",
				"Total Operating Costs", currency, Fiscal_Year, 1);
		jsonRsArray.add(34, jsonObjTOC);

		jsonObj = getRatioTBV(stmt, stmtTBV, companycode,
				"'Productive Wages','Unproductive Wages','Overtime Premium','Other Fringe Benefits Wages','Salaries','Overtime','Other Fringe Benefits','Consumables','Energy','Repairwork (external)','Leasing/Depreciation (Build.)','Other Leasing Costs','Other Depreciation','Engineering Cost Depreciation','Interest Costs','Taxes/Duties','Insurances','Travel and Subsistance','Telephone/Telex/Telefax/Mail','Legal/Audit','Airfreight','Surface Freight','Canteen','Royalties','GCSP','Other Costs'",
				"in % of Total Business Volume", currency, Fiscal_Year, -1);
		jsonRsArray.add(35, jsonObj);

		jsonObj = getPLstructure(stmt, companycode,
				"'001 Material Handling','002 Direct Labor','003 Indirect Labor','004 Depn of Machinery','005 Other Manufacturing Cost Direct','006 Other Manufacturing Cost Indirect','007 Engineering','008 Administration','009 Sales'",
				"PL", currency, Fiscal_Year, 1);
		jsonObj = jsonConbine(jsonObjTOC, jsonObj, "Check PL_Structure");
		jsonRsArray.add(36, jsonObj);

		stmt.close();
		stmtTBV.close();
		conn.close();

		return jsonRsArray;
	}

	//根据irt结构，和自定义的查询方法，查询balance sheet
	public static JSONArray BalanceSheet(String Company, String currency, String Fiscal_Year) throws SQLException {
		String companycode = Company;
		JSONObject jsonObj = new JSONObject();
		JSONObject jsonAS = new JSONObject();
		JSONObject jsonLL = new JSONObject();
		JSONArray jsonRsArray = new JSONArray();

		Connection conn = DBUtil.getConnection();
		Statement stmt = conn.createStatement();
		Statement stmtTBV = conn.createStatement();

		jsonObj = getBalance(stmt, companycode, "'Land/Building'", "Land/Building", currency, Fiscal_Year, 1);
		jsonRsArray.add(0, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Machinery/Equipment'", "Machinery/Equipment", currency, Fiscal_Year,
				1);
		jsonRsArray.add(1, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Tools/Devices'", "Tools/Devices", currency, Fiscal_Year, 1);
		jsonRsArray.add(2, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Other Fixed Assets'", "Other Fixed Assets", currency, Fiscal_Year, 1);
		jsonRsArray.add(3, jsonObj);

		jsonObj = getBalance(stmt, companycode,
				"'Land/Building','Machinery/Equipment','Tools/Devices','Other Fixed Assets'", "Fixed Assets", currency,
				Fiscal_Year, 1);
		jsonRsArray.add(4, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Rawmaterial/Parts'", "Rawmaterial/Parts", currency, Fiscal_Year, 1);
		jsonRsArray.add(5, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'WiP/FG'", "WiP/FG", currency, Fiscal_Year, 1);
		jsonRsArray.add(6, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Engineering Services'", "Engineering Services", currency, Fiscal_Year,
				1);
		jsonRsArray.add(7, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Tools/Devices/Samples'", "Tools/Devices/Samples", currency,
				Fiscal_Year, 1);
		jsonRsArray.add(8, jsonObj);

		jsonObj = getBalance(stmt, companycode,
				"'Rawmaterial/Parts','WiP/FG','Engineering Services','Tools/Devices/Samples'", "Inventory", currency,
				Fiscal_Year, 1);
		jsonRsArray.add(9, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Accounts Receivable'", "Accounts Receivable", currency, Fiscal_Year,
				1);
		jsonRsArray.add(10, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Group Receivables'", "Group Receivables", currency, Fiscal_Year, 1);
		jsonRsArray.add(11, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Clearing Receivables'", "Clearing Receivables", currency, Fiscal_Year,
				1);
		jsonRsArray.add(12, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Bank Balances and Cash'", "Bank Balances and Cash", currency,
				Fiscal_Year, 1);
		jsonRsArray.add(13, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Other Current Assets'", "Other Current Assets", currency, Fiscal_Year,
				1);
		jsonRsArray.add(14, jsonObj);

		jsonObj = getBalance(stmt, companycode,
				"'Accounts Receivable','Group Receivables','Clearing Receivables','Bank Balances and Cash','Other Current Assets'",
				"Current Assets", currency, Fiscal_Year, 1);
		jsonRsArray.add(15, jsonObj);

		jsonAS = getBalance(stmt, companycode,
				"'Land/Building','Machinery/Equipment','Tools/Devices','Other Fixed Assets','Rawmaterial/Parts','WiP/FG','Engineering Services','Tools/Devices/Samples','Accounts Receivable','Group Receivables','Clearing Receivables','Bank Balances and Cash','Other Current Assets'",
				"Total Assets", currency, Fiscal_Year, 1);
		jsonRsArray.add(16, jsonAS);

		jsonObj = getBalance(stmt, companycode, "'Share Capital'", "Share Capital", currency, Fiscal_Year, -1);
		jsonRsArray.add(17, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Capital Reserve'", "Capital Reserve", currency, Fiscal_Year, -1);
		jsonRsArray.add(18, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Profit/Loss previous years'", "Profit/Loss previous years", currency,
				Fiscal_Year, -1);
		jsonRsArray.add(19, jsonObj);

		jsonObj = getBalance(stmt, companycode,
				"'Overtime','Royalties','GCSP','Direct Material','Engineering Cost Depreciation','Interest Costs','REVENUES','Other Costs','Consumables','Productive Wages','Overtime Premium','Other Fringe Benefits Wages','Unproductive Wages','Salaries','Other Fringe Benefits','Canteen','Energy','Repairwork (external)','Taxes/Duties','Leasing/Depreciation (Build.)','Other Depreciation','Travel and Subsistance','Other Leasing Costs','Surface Freight','Airfreight','Other Capitalized Costs','Change in Stockvalue','Profit/Loss','Other Income / Expenses','Exchange Gains/Losses','Interest Income / Expenses','Legal/Audit','Insurances','Telephone/Telex/Telefax/Mail'",
				"Profit/Loss", currency, Fiscal_Year, -1);
		jsonRsArray.add(20, jsonObj);

		jsonObj = getBalance(stmt, companycode,
				"'Share Capital','Capital Reserve','Profit/Loss previous years','Profit/Loss','Direct Material','Engineering Cost Depreciation','Interest Costs','REVENUES','Other Costs','Consumables','Productive Wages','Overtime Premium','Other Fringe Benefits Wages','Unproductive Wages','Salaries','Overtime','Other Fringe Benefits','Canteen','Energy','Repairwork (external)','Taxes/Duties','Leasing/Depreciation (Build.)','Other Depreciation','Travel and Subsistance','Other Leasing Costs','Surface Freight','Airfreight','Other Capitalized Costs','Change in Stockvalue','Other Income / Expenses','Exchange Gains/Losses','Interest Income / Expenses','Legal/Audit','Insurances','Telephone/Telex/Telefax/Mail'",
				"Equity Capital", currency, Fiscal_Year, -1);
		jsonRsArray.add(21, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Provisions for Retirements'", "Provisions for Retirements", currency,
				Fiscal_Year, -1);
		jsonRsArray.add(22, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Longterm Bank Loans'", "Longterm Bank Loans", currency, Fiscal_Year,
				-1);
		jsonRsArray.add(23, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'LK-Loan'", "LK-Loan", currency, Fiscal_Year, -1);
		jsonRsArray.add(24, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Provisions for Retirements','Longterm Bank Loans','LK-Loan'",
				"Longterm Liabilities", currency, Fiscal_Year, -1);
		jsonRsArray.add(25, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Accounts Payables'", "Accounts Payables", currency, Fiscal_Year, -1);
		jsonRsArray.add(26, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Group Payables'", "Group Payables", currency, Fiscal_Year, -1);
		jsonRsArray.add(27, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Clearing Payables'", "Clearing Payables", currency, Fiscal_Year, -1);
		jsonRsArray.add(28, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Shortterm Bank Loans'", "Shortterm Bank Loans", currency, Fiscal_Year,
				-1);
		jsonRsArray.add(29, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Shortterm Provisions'", "Shortterm Provisions", currency, Fiscal_Year,
				-1);
		jsonRsArray.add(30, jsonObj);

		jsonObj = getBalance(stmt, companycode, "'Other Shortterm Liabilities'", "Other Shortterm Liabilities",
				currency, Fiscal_Year, -1);
		jsonRsArray.add(31, jsonObj);

		jsonObj = getBalance(stmt, companycode,
				"'Accounts Payables','Group Payables','Clearing Payables','Shortterm Bank Loans','Shortterm Provisions','Other Shortterm Liabilities'",
				"Shortterm Liabilities", currency, Fiscal_Year, -1);
		jsonRsArray.add(32, jsonObj);

		jsonLL = getBalance(stmt, companycode,
				"'Overtime','Royalties','GCSP','Share Capital','Capital Reserve','Profit/Loss previous years','Profit/Loss','Provisions for retirements','Longterm Bank Loans','LK-Loan','Accounts Payables','Group Payables','Clearing Payables','Shortterm Bank Loans','Shortterm Provisions','Other Shortterm Liabilities','Direct Material','Engineering Cost Depreciation','Interest Costs','REVENUES','Other Costs','Consumables','Productive Wages','Overtime Premium','Other Fringe Benefits Wages','Unproductive Wages','Salaries','Other Fringe Benefits','Canteen','Energy','Repairwork (external)','Taxes/Duties','Leasing/Depreciation (Build.)','Other Depreciation','Travel and Subsistance','Other Leasing Costs','Surface Freight','Airfreight','Other Capitalized Costs','Change in Stockvalue','Other Income / Expenses','Exchange Gains/Losses','Interest Income / Expenses','Legal/Audit','Insurances','Telephone/Telex/Telefax/Mail'",
				"Total Equity and Liabilities", currency, Fiscal_Year, -1);
		jsonRsArray.add(33, jsonLL);

		jsonObj = jsonConbine(jsonAS, jsonLL, "Check Balance");
		jsonRsArray.add(34, jsonObj);

		JSONObject jsonLand = new JSONObject();
		JSONObject jsonMachinery = new JSONObject();
		JSONObject jsonTools = new JSONObject();
		JSONObject jsonEngineering = new JSONObject();
		JSONObject jsonOther = new JSONObject();
		JSONObject jsonLandIC = new JSONObject();
		JSONObject jsonMachineryIC = new JSONObject();
		JSONObject jsonToolsIC = new JSONObject();
		JSONObject jsonEngineeringIC = new JSONObject();
		JSONObject jsonOtherIC = new JSONObject();

		jsonLand = getInvestment(stmt, companycode, "'Invest Land/Building'", "Invest Land/Building", currency,
				Fiscal_Year, 1);
		jsonRsArray.add(35, jsonLand);

		jsonMachinery = getInvestment(stmt, companycode, "'Invest Machinery/Equipment'", "Invest Machinery/Equipment",
				currency, Fiscal_Year, 1);
		jsonRsArray.add(36, jsonMachinery);

		jsonTools = getInvestment(stmt, companycode, "'Invest Tools/Devices'", "Invest Tools/Devices", currency,
				Fiscal_Year, 1);
		jsonRsArray.add(37, jsonTools);

		jsonEngineering = getInvestment(stmt, companycode, "'Invest Engineering Services'",
				"Invest Engineering Services", currency, Fiscal_Year, 1);
		jsonRsArray.add(38, jsonEngineering);

		jsonOther = getInvestment(stmt, companycode, "'Invest Other Fixed Assets'", "Invest Other Fixed Assets",
				currency, Fiscal_Year, 1);
		jsonRsArray.add(39, jsonOther);

		jsonLandIC = getInvestment(stmt, companycode, "'Invest Land/Building IC'", "Invest Land/Building IC", currency,
				Fiscal_Year, 1);
		jsonRsArray.add(40, jsonLandIC);

		jsonMachineryIC = getInvestment(stmt, companycode, "'Invest Machinery/Equipment IC'",
				"Invest Machinery/Equipment IC", currency, Fiscal_Year, 1);
		jsonRsArray.add(41, jsonMachineryIC);

		jsonToolsIC = getInvestment(stmt, companycode, "'Invest Tools/Devices IC'", "Invest Tools/Devices IC", currency,
				Fiscal_Year, 1);
		jsonRsArray.add(42, jsonToolsIC);

		jsonEngineeringIC = getInvestment(stmt, companycode, "'Invest Engineering Services IC'",
				"Invest Engineering Services IC", currency, Fiscal_Year, 1);
		jsonRsArray.add(43, jsonEngineeringIC);

		jsonOtherIC = getInvestment(stmt, companycode, "'Invest Other Fixed Assets IC'", "Invest Other Fixed Assets IC",
				currency, Fiscal_Year, 1);
		jsonRsArray.add(44, jsonOtherIC);

		jsonObj = jsonConbine(jsonLand, jsonLandIC, "Invest Land/Building 3rd");
		jsonRsArray.add(45, jsonObj);

		jsonObj = jsonConbine(jsonMachinery, jsonMachineryIC, "Invest Machinery/Equipment 3rd");
		jsonRsArray.add(46, jsonObj);

		jsonObj = jsonConbine(jsonTools, jsonToolsIC, "Invest Tools/Devices 3rd");
		jsonRsArray.add(47, jsonObj);

		jsonObj = jsonConbine(jsonEngineering, jsonEngineeringIC, "Invest Engineering Services 3rd");
		jsonRsArray.add(48, jsonObj);

		jsonObj = jsonConbine(jsonOther, jsonOtherIC, "Invest Other Fixed Assets 3rd");
		jsonRsArray.add(49, jsonObj);

		JSONObject jsonLandRetirement = new JSONObject();
		JSONObject jsonMachineryRetirement = new JSONObject();
		JSONObject jsonToolsRetirement = new JSONObject();
		JSONObject jsonEngineeringRetirement = new JSONObject();
		JSONObject jsonOtherRetirement = new JSONObject();
		JSONObject jsonLandICRetirement = new JSONObject();
		JSONObject jsonMachineryICRetirement = new JSONObject();
		JSONObject jsonToolsICRetirement = new JSONObject();
		JSONObject jsonEngineeringICRetirement = new JSONObject();
		JSONObject jsonOtherICRetirement = new JSONObject();

		jsonLandRetirement = getInvestment(stmt, companycode, "'Retirement Land/Building'", "Retirement Land/Building",
				currency, Fiscal_Year, 1);
		jsonRsArray.add(50, jsonLandRetirement);

		jsonMachineryRetirement = getInvestment(stmt, companycode, "'Retirement Machinery/Equipment'",
				"Retirement Machinery/Equipment", currency, Fiscal_Year, 1);
		jsonRsArray.add(51, jsonMachineryRetirement);

		jsonToolsRetirement = getInvestment(stmt, companycode, "'Retirement Tools/Devices'", "Retirement Tools/Devices",
				currency, Fiscal_Year, 1);
		jsonRsArray.add(52, jsonToolsRetirement);

		jsonEngineeringRetirement = getInvestment(stmt, companycode, "'Retirement Engineering Services'",
				"Retirement Engineering Services", currency, Fiscal_Year, 1);
		jsonRsArray.add(53, jsonEngineeringRetirement);

		jsonOtherRetirement = getInvestment(stmt, companycode, "'Retirement Other Fixed Assets'",
				"Retirement Other Fixed Assets", currency, Fiscal_Year, 1);
		jsonRsArray.add(54, jsonOtherRetirement);

		jsonLandICRetirement = getInvestment(stmt, companycode, "'Retirement Land/Building IC'",
				"Retirement Land/Building IC", currency, Fiscal_Year, 1);
		jsonRsArray.add(55, jsonLandICRetirement);

		jsonMachineryICRetirement = getInvestment(stmt, companycode, "'Retirement Machinery/Equipment IC'",
				"Retirement Machinery/Equipment IC", currency, Fiscal_Year, 1);
		jsonRsArray.add(56, jsonMachineryICRetirement);

		jsonToolsICRetirement = getInvestment(stmt, companycode, "'Retirement Tools/Devices IC'",
				"Retirement Tools/Devices IC", currency, Fiscal_Year, 1);
		jsonRsArray.add(57, jsonToolsICRetirement);

		jsonEngineeringICRetirement = getInvestment(stmt, companycode, "'Retirement Engineering Services IC'",
				"Retirement Engineering Services IC", currency, Fiscal_Year, 1);
		jsonRsArray.add(58, jsonEngineeringICRetirement);

		jsonOtherICRetirement = getInvestment(stmt, companycode, "'Retirement Other Fixed Assets IC'",
				"Retirement Other Fixed Assets IC", currency, Fiscal_Year, 1);
		jsonRsArray.add(59, jsonOtherICRetirement);

		jsonObj = jsonConbine(jsonLandRetirement, jsonLandICRetirement, "Retirement Land/Building 3rd");
		jsonRsArray.add(60, jsonObj);

		jsonObj = jsonConbine(jsonMachineryRetirement, jsonMachineryICRetirement, "Retirement Machinery/Equipment 3rd");
		jsonRsArray.add(61, jsonObj);

		jsonObj = jsonConbine(jsonToolsRetirement, jsonToolsICRetirement, "Retirement Tools/Devices 3rd");
		jsonRsArray.add(62, jsonObj);

		jsonObj = jsonConbine(jsonEngineeringRetirement, jsonEngineeringICRetirement,
				"Retirement Engineering Services 3rd");
		jsonRsArray.add(63, jsonObj);

		jsonObj = jsonConbine(jsonOtherRetirement, jsonOtherICRetirement, "Retirement Other Fixed Assets 3rd");
		jsonRsArray.add(64, jsonObj);

		JSONObject jsonCAPITAL_EXPENDITURES = new JSONObject();
		JSONObject jsonTOTAL_DEPRECIATION = new JSONObject();
		JSONObject jsonCheck_Investment = new JSONObject();

		jsonCAPITAL_EXPENDITURES = getInvestment(stmt, companycode,
				"'Invest Land/Building','Invest Machinery/Equipment','Invest Tools/Devices','Invest Engineering Services','Invest Other Fixed Assets','Retirement Land/Building','Retirement Machinery/Equipment','Retirement Tools/Devices','Retirement Engineering Services','Retirement Other Fixed Assets'",
				"CAPITAL EXPENDITURES", currency, Fiscal_Year, 1);
		jsonRsArray.add(65, jsonCAPITAL_EXPENDITURES);

		jsonTOTAL_DEPRECIATION = getInvestment(stmt, companycode, "'TOTAL DEPRECIATION'", "TOTAL DEPRECIATION",
				currency, Fiscal_Year, 1);
		jsonRsArray.add(66, jsonTOTAL_DEPRECIATION);

		jsonCheck_Investment = getAssetBalance(stmt, companycode, "", "Check Investment", currency, Fiscal_Year, 1);
		jsonObj = jsonConbine(jsonCAPITAL_EXPENDITURES, jsonTOTAL_DEPRECIATION, "Check Investment");
		jsonObj = jsonConbine(jsonObj, jsonCheck_Investment, "Check Investment");
		jsonRsArray.add(67, jsonObj);

		stmt.close();
		stmtTBV.close();
		conn.close();

		return jsonRsArray;
	}

	//按月获取irt结构的汇总金额，账号发生数
	public static JSONObject getResult(Statement stmt, String CompanyCode, String IRT_Structure,
			String IRT_Structure_Name, String currency, String Fiscal_Year, int denominatorSign) throws SQLException {
		JSONObject jsonObj = new JSONObject();
		DecimalFormat df = new DecimalFormat("0.00");
		jsonObj.put("Structure", IRT_Structure_Name);
		if (IRT_Structure.contains(",")) {
			IRT_Structure = IRT_Structure.replaceAll(",", " OR `IRT Structure` =  ");
		}
		String[] periods = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13" };

		String periodsCondition = "";
		for (int i = 0; i < periods.length; i++) {
			if (i < periods.length - 1) {
				periodsCondition = periodsCondition + "'" + periods[i] + "' OR `Posting period` = ";
			} else {
				periodsCondition = periodsCondition + "'" + periods[i] + "'";
			}
		}
		for (int i = 0; i < periods.length + 1; i++) {
			if (i < periods.length) {
				String sql_getResult = "SELECT `Company Code`, SUM(`Balance LC`) / 1000 AS 'SUM of Balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = "
						+ IRT_Structure + ") AND `Company Code` = '" + CompanyCode + "' AND `Posting period` = '"
						+ periods[i].toString() + "' AND `Currency` = '" + currency + "' AND `Fiscal Year` = '"
						+ Fiscal_Year + "' GROUP BY `Company Code`, `Currency`";

				ResultSet rs = stmt.executeQuery(sql_getResult);
				while (rs.next()) {
					jsonObj.put(periods[i].toString(), df.format(rs.getDouble("SUM of Balance") / denominatorSign));
				}
			} else {
				String sql_getResult = "SELECT SUM(`Balance LC`) / 1000 AS 'SUM of Balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = "
						+ IRT_Structure + ") AND `Company Code` = '" + CompanyCode + "' AND (`Posting period` = "
						+ periodsCondition + ") AND `Currency` = '" + currency + "' AND `Fiscal Year` = '" + Fiscal_Year
						+ "' GROUP BY `Currency`";

				ResultSet rs = stmt.executeQuery(sql_getResult);
				while (rs.next()) {
					jsonObj.put("Total", df.format(rs.getDouble("SUM of Balance") / denominatorSign));
				}
			}
		}
		return jsonObj;
	}

	//按月获取irt结构的汇总金额，账号发生数，并计算占TBV
	public static JSONObject getRatioTBV(Statement stmt, Statement stmtTBV, String CompanyCode, String IRT_Structure,
			String IRT_Structure_Name, String currency, String Fiscal_Year, int denominatorSign) throws SQLException {
		JSONObject jsonObj = new JSONObject();
		DecimalFormat df = new DecimalFormat("0.0%");
		jsonObj.put("Structure", IRT_Structure_Name);
		if (IRT_Structure.contains(",")) {
			IRT_Structure = IRT_Structure.replaceAll(",", " OR `IRT Structure` =  ");
		}
		String[] periods = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13" };

		String periodsCondition = "";
		for (int i = 0; i < periods.length; i++) {
			if (i < periods.length - 1) {
				periodsCondition = periodsCondition + "'" + periods[i] + "' OR `Posting period` = ";
			} else {
				periodsCondition = periodsCondition + "'" + periods[i] + "'";
			}
		}
		for (int i = 0; i < periods.length + 1; i++) {
			if (i < periods.length) {
				String sql_getResult = "SELECT `Company Code`, SUM(`Balance LC`) / 1000 AS 'SUM of Balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = "
						+ IRT_Structure + ") AND `Company Code` = '" + CompanyCode + "' AND `Posting period` = '"
						+ periods[i].toString() + "' AND `Currency` = '" + currency + "' AND `Fiscal Year` = '"
						+ Fiscal_Year + "' GROUP BY `Company Code`, `Currency`";

				String sql_getResultTBV = "SELECT `Company Code`, SUM(`Balance LC`) / 1000 AS 'SUM of Balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = 'REVENUES' OR `IRT Structure` = 'Change in Stockvalue' OR `IRT Structure` = 'Other Capitalized Costs') AND `Currency` = '"
						+ currency + "' AND `Company Code` = '" + CompanyCode + "' AND `Posting period` = '"
						+ periods[i].toString() + "' AND `Fiscal Year` = '" + Fiscal_Year + "' GROUP BY  `Currency`";

				ResultSet rs = stmt.executeQuery(sql_getResult);
				ResultSet rsTBV = stmtTBV.executeQuery(sql_getResultTBV);

				double molecule = 0.0D;
				double denominator = 0.0D;
				while (rs.next()) {
					molecule = rs.getDouble("SUM of Balance");
				}
				while (rsTBV.next()) {
					denominator = rsTBV.getDouble("SUM of Balance");
				}
				double percentage = molecule / denominator;
				if (denominator == 0.0D) {
					percentage = 0.0D;
				}
				jsonObj.put(periods[i].toString(), df.format(percentage / denominatorSign));
			} else {
				String sql_getResult = "SELECT SUM(`Balance LC`) / 1000 AS 'SUM of Balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = "
						+ IRT_Structure + ") AND `Company Code` = '" + CompanyCode + "' AND (`Posting period` = "
						+ periodsCondition + ") AND `Currency` = '" + currency + "' AND `Fiscal Year` = '" + Fiscal_Year
						+ "' GROUP BY `Currency`";

				String sql_getResultTBV = "SELECT  SUM(`Balance LC`) / 1000 AS 'SUM of Balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = 'REVENUES' OR `IRT Structure` = 'Change in Stockvalue' OR `IRT Structure` = 'Other Capitalized Costs') AND `Company Code` = '"
						+ CompanyCode + "' AND (`Posting period` = " + periodsCondition + ") AND `Currency` = '"
						+ currency + "' AND `Fiscal Year` = '" + Fiscal_Year + "' GROUP BY `Currency`";

				ResultSet rs = stmt.executeQuery(sql_getResult);
				ResultSet rsTBV = stmtTBV.executeQuery(sql_getResultTBV);

				double molecule = 0.0D;
				double denominator = 0.0D;
				while (rs.next()) {
					molecule = rs.getDouble("SUM of Balance");
				}
				while (rsTBV.next()) {
					denominator = rsTBV.getDouble("SUM of Balance");
				}
				double percentage = molecule / denominator;
				if (denominator == 0.0D) {
					percentage = 0.0D;
				}
				jsonObj.put("Total", df.format(percentage / denominatorSign));
			}
		}
		return jsonObj;
	}


	//查询balance sheet里的名义利息
	public static JSONObject getNOMINAL_INTEREST(Statement stmt, String CompanyCode, String IRT_Structure,
			String IRT_Structure_Name, String currency, String Fiscal_Year, int denominatorSign) throws SQLException {
		JSONObject jsonObj = new JSONObject();
		DecimalFormat df = new DecimalFormat("0.00");
		jsonObj.put("Structure", IRT_Structure_Name);
		if (IRT_Structure.contains(",")) {
			IRT_Structure = IRT_Structure.replaceAll(",", " OR `IRT Structure` =  ");
		}
		String[] periods = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13" };

		String periodsCondition = "";
		for (int i = 0; i < periods.length; i++) {
			if (i < periods.length - 1) {
				periodsCondition = periodsCondition + "'" + periods[i] + "' OR `Posting period` = ";
			} else {
				periodsCondition = periodsCondition + "'" + periods[i] + "'";
			}
		}
		for (int i = 0; i < periods.length + 1; i++) {
			if (i < periods.length) {
				String sql_getResult = "SELECT `Company Code`, SUM(`Accumulated balance LC`) / 1000 AS 'SUM of Accumulated balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = "
						+ IRT_Structure + ") AND `Company Code` = '" + CompanyCode + "' AND `Posting period` = '"
						+ periods[i].toString() + "' AND `Currency` = '" + currency + "' AND `Fiscal Year` = '"
						+ Fiscal_Year + "' GROUP BY `Company Code`, `Currency`";

				ResultSet rs = stmt.executeQuery(sql_getResult);
				while (rs.next()) {
					jsonObj.put(periods[i].toString(),
							df.format(rs.getDouble("SUM of Accumulated balance") / denominatorSign * 0.05D / 12.0D));
				}
			} else {
				String sql_getResult = "SELECT SUM(`Accumulated balance LC`) / 1000 AS 'SUM of Accumulated balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = "
						+ IRT_Structure + ") AND `Company Code` = '" + CompanyCode + "' AND (`Posting period` = "
						+ periodsCondition + ") AND `Currency` = '" + currency + "' AND `Fiscal Year` = '" + Fiscal_Year
						+ "' GROUP BY `Currency`";

				ResultSet rs = stmt.executeQuery(sql_getResult);
				while (rs.next()) {
					jsonObj.put("Total",
							df.format(rs.getDouble("SUM of Accumulated balance") / denominatorSign * 0.05D / 12.0D));
				}
			}
		}
		return jsonObj;
	}

	//获取账号余额
	public static JSONObject getBalance(Statement stmt, String CompanyCode, String IRT_Structure,
			String IRT_Structure_Name, String currency, String Fiscal_Year, int denominatorSign) throws SQLException {
		JSONObject jsonObj = new JSONObject();
		DecimalFormat df = new DecimalFormat("0.00");
		jsonObj.put("Structure", IRT_Structure_Name);
		if (IRT_Structure.contains(",")) {
			IRT_Structure = IRT_Structure.replaceAll(",", " OR `IRT Structure` =  ");
		}
		String[] periods = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13" };

		String periodsCondition = "";
		for (int i = 0; i < periods.length; i++) {
			if (i < periods.length - 1) {
				periodsCondition = periodsCondition + "'" + periods[i] + "' OR `Posting period` = ";
			} else {
				periodsCondition = periodsCondition + "'" + periods[i] + "'";
			}
		}
		for (int i = 0; i < periods.length + 1; i++) {
			if (i < periods.length) {
				String sql_getResult = "SELECT `Company Code`, SUM(`Accumulated balance LC`) / 1000 AS 'SUM of Accumulated balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = "
						+ IRT_Structure + ") AND `Company Code` = '" + CompanyCode + "' AND `Posting period` = '"
						+ periods[i].toString() + "' AND `Currency` = '" + currency + "' AND `Fiscal Year` = '"
						+ Fiscal_Year + "' GROUP BY `Company Code`, `Currency`";

				ResultSet rs = stmt.executeQuery(sql_getResult);
				while (rs.next()) {
					jsonObj.put(periods[i].toString(),
							df.format(rs.getDouble("SUM of Accumulated balance") / denominatorSign));
				}
			} else {
				String sql_getResult = "SELECT SUM(`Accumulated balance LC`) / 1000 AS 'SUM of Accumulated balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = "
						+ IRT_Structure + ") AND `Company Code` = '" + CompanyCode + "' AND (`Posting period` = "
						+ periodsCondition + ") AND `Currency` = '" + currency + "' AND `Fiscal Year` = '" + Fiscal_Year
						+ "' GROUP BY `Currency`";

				ResultSet rs = stmt.executeQuery(sql_getResult);
				while (rs.next()) {
					jsonObj.put("Total", df.format(rs.getDouble("SUM of Accumulated balance") / denominatorSign));
				}
			}
		}
		return jsonObj;
	}

	//获取profit and loss中的PL structure的内容
	public static JSONObject getPLstructure(Statement stmt, String CompanyCode, String IRT_Structure,
			String IRT_Structure_Name, String currency, String Fiscal_Year, int denominatorSign) throws SQLException {
		JSONObject jsonObj = new JSONObject();
		DecimalFormat df = new DecimalFormat("0.00");
		jsonObj.put("Structure", IRT_Structure_Name);
		if (IRT_Structure.contains(",")) {
			IRT_Structure = IRT_Structure.replaceAll(",", " OR `P&L Structure` =  ");
		}
		String[] periods = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13" };

		String periodsCondition = "";
		for (int i = 0; i < periods.length; i++) {
			if (i < periods.length - 1) {
				periodsCondition = periodsCondition + "'" + periods[i] + "' OR `Period` = ";
			} else {
				periodsCondition = periodsCondition + "'" + periods[i] + "'";
			}
		}
		for (int i = 0; i < periods.length + 1; i++) {
			if (i < periods.length) {
				String sql_getResult = "SELECT `CoCd`, SUM(`In company code currency`) / 1000 AS 'SUM of In company code currency'  FROM report_irt_basic_info_plstructure  WHERE (`P&L Structure` = "
						+ IRT_Structure + ") AND `CoCd` = '" + CompanyCode + "' AND `Period` = '"
						+ periods[i].toString() + "' AND `Local Crcy` = '" + currency + "' AND `Year` = '" + Fiscal_Year
						+ "' GROUP BY `CoCd`, `Local Crcy`";

				ResultSet rs = stmt.executeQuery(sql_getResult);
				while (rs.next()) {
					jsonObj.put(periods[i].toString(),
							df.format(rs.getDouble("SUM of In company code currency") / denominatorSign));
				}
			} else {
				String sql_getResult = "SELECT SUM(`In company code currency`) / 1000 AS 'SUM of In company code currency'  FROM report_irt_basic_info_plstructure  WHERE (`P&L Structure` = "
						+ IRT_Structure + ") AND `CoCd` = '" + CompanyCode + "' AND (`Period` = " + periodsCondition
						+ ") AND `Local Crcy` = '" + currency + "' AND `Year` = '" + Fiscal_Year
						+ "' GROUP BY `Local Crcy`";

				ResultSet rs = stmt.executeQuery(sql_getResult);
				while (rs.next()) {
					jsonObj.put("Total", df.format(rs.getDouble("SUM of In company code currency") / denominatorSign));
				}
			}
		}
		return jsonObj;
	}

	//获取投资内容
	public static JSONObject getInvestment(Statement stmt, String CompanyCode, String IRT_Structure,
			String IRT_Structure_Name, String currency, String Fiscal_Year, int denominatorSign) throws SQLException {
		JSONObject jsonObj = new JSONObject();

		DecimalFormat df = new DecimalFormat("0.00");
		jsonObj.put("Structure", IRT_Structure_Name);
		if (IRT_Structure.contains(",")) {
			IRT_Structure = IRT_Structure.replaceAll(",", " OR `IRT_Structure` =  ");
		}
		String[] periods = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13" };
		String periodsCondition = "";
		for (int i = 0; i < periods.length; i++) {
			if (i < periods.length - 1) {
				periodsCondition = periodsCondition + "'" + periods[i] + "' OR `Period` = ";
			} else {
				periodsCondition = periodsCondition + "'" + periods[i] + "'";
			}
		}
		for (int i = 0; i < periods.length + 1; i++) {
			if (i < periods.length) {
				String sql_getResult = "SELECT `CoCd`, SUM(`currectAcquisition`) / 1000 AS 'SUM of currectAcquisition'  FROM report_irt_basic_info_investment  WHERE (`IRT_Structure` = "
						+ IRT_Structure + ") AND `CoCd` = '" + CompanyCode + "' AND `Period` = '"
						+ periods[i].toString() + "' AND `Crcy` = '" + currency + "' AND `Year` = '" + Fiscal_Year
						+ "' GROUP BY `CoCd`, `Crcy`";

				ResultSet rs = stmt.executeQuery(sql_getResult);
				while (rs.next()) {
					jsonObj.put(periods[i].toString(),
							df.format(rs.getDouble("SUM of currectAcquisition") / denominatorSign));
				}
			} else {
				String sql_getResult = "SELECT SUM(`currectAcquisition`) / 1000 AS 'SUM of currectAcquisition'  FROM report_irt_basic_info_investment  WHERE (`IRT_Structure` = "
						+ IRT_Structure + ") AND `CoCd` = '" + CompanyCode + "' AND (`Period` = " + periodsCondition
						+ ") AND `Crcy` = '" + currency + "' AND `Year` = '" + Fiscal_Year + "' GROUP BY `Crcy`";

				ResultSet rs = stmt.executeQuery(sql_getResult);
				while (rs.next()) {
					jsonObj.put("Total", df.format(rs.getDouble("SUM of currectAcquisition") / denominatorSign));
				}
			}
		}
		return jsonObj;
	}

	//计算fixed asset的发生额，和投资去check
	public static JSONObject getAssetBalance(Statement stmt, String CompanyCode, String IRT_Structure,
			String IRT_Structure_Name, String currency, String Fiscal_Year, int denominatorSign) throws SQLException {
		JSONObject jsonObj = new JSONObject();

		DecimalFormat df = new DecimalFormat("0.00");
		jsonObj.put("Structure", IRT_Structure_Name);

		String[] periods = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13" };
		String periodsCondition = "";
		for (int i = 0; i < periods.length; i++) {
			if (i < periods.length - 1) {
				periodsCondition = periodsCondition + "'" + periods[i] + "' OR `Posting period` = ";
			} else {
				periodsCondition = periodsCondition + "'" + periods[i] + "'";
			}
		}
		for (int i = 0; i < periods.length + 1; i++) {
			if (i < periods.length) {
				String sql_getResult = "SELECT `Company Code`, SUM(`Balance LC`) / 1000 AS 'SUM of Balance LC'  FROM basic_info_investment_check  WHERE  `Company Code` = '"
						+ CompanyCode + "' AND `Posting period` = '" + periods[i].toString() + "' AND `Currency` = '"
						+ currency + "' AND `Fiscal Year` = '" + Fiscal_Year + "' GROUP BY `Company Code`, `Currency`";

				ResultSet rs = stmt.executeQuery(sql_getResult);
				while (rs.next()) {
					jsonObj.put(periods[i].toString(), df.format(rs.getDouble("SUM of Balance LC") / denominatorSign));
				}
			} else {
				String sql_getResult = "SELECT SUM(`Balance LC`) / 1000 AS 'SUM of Balance LC'  FROM basic_info_investment_check  WHERE  `Company Code` = '"
						+ CompanyCode + "' AND (`Posting period` = " + periodsCondition + ") AND `Currency` = '"
						+ currency + "' AND `Fiscal Year` = '" + Fiscal_Year + "' GROUP BY `Currency`";

				ResultSet rs = stmt.executeQuery(sql_getResult);
				while (rs.next()) {
					jsonObj.put("Total", df.format(rs.getDouble("SUM of Balance LC") / denominatorSign));
				}
			}
		}
		return jsonObj;
	}

	//两个Json对象的相减
	public static JSONObject jsonConbine(JSONObject json1, JSONObject json2, String IRT_Structure_Name) {
		String[] periods = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "Total" };
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("Structure", IRT_Structure_Name);
		for (int i = 0; i < periods.length; i++) {
			if ((json1.has(periods[i])) && (json2.has(periods[i]))) {
				jsonObj.put(periods[i], Double.valueOf(json1.getDouble(periods[i]) - json2.getDouble(periods[i])));
			} else if ((json1.has(periods[i])) && (!json2.has(periods[i]))) {
				jsonObj.put(periods[i], Double.valueOf(json1.getDouble(periods[i]) - 0.0D));
			} else if ((!json1.has(periods[i])) && (json2.has(periods[i]))) {
				jsonObj.put(periods[i], Double.valueOf(0.0D - json2.getDouble(periods[i])));
			}
		}
		return jsonObj;
	}

	//两个Json对象的相加
	public static JSONObject jsonConbinePlus(JSONObject json1, JSONObject json2, String IRT_Structure_Name) {
		String[] periods = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "Total" };
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("Structure", IRT_Structure_Name);
		for (int i = 0; i < periods.length; i++) {
			if ((json1.has(periods[i])) && (json2.has(periods[i]))) {
				jsonObj.put(periods[i], Double.valueOf(json1.getDouble(periods[i]) + json2.getDouble(periods[i])));
			} else if ((json1.has(periods[i])) && (!json2.has(periods[i]))) {
				jsonObj.put(periods[i], Double.valueOf(json1.getDouble(periods[i]) + 0.0D));
			} else if ((!json1.has(periods[i])) && (json2.has(periods[i]))) {
				jsonObj.put(periods[i], Double.valueOf(0.0D + json2.getDouble(periods[i])));
			}
		}
		return jsonObj;
	}

	//两个Json对象的%计算
	public static JSONObject jsonConbineRatio(JSONObject json1, JSONObject json2, String IRT_Structure_Name) {
		String[] periods = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "Total" };
		DecimalFormat df = new DecimalFormat("0.0%");
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("Structure", IRT_Structure_Name);
		for (int i = 0; i < periods.length; i++) {
			if ((json1.has(periods[i])) && (json2.has(periods[i]))) {
				jsonObj.put(periods[i], df.format(json1.getDouble(periods[i]) / json2.getDouble(periods[i])));
			}
		}
		return jsonObj;
	}
}