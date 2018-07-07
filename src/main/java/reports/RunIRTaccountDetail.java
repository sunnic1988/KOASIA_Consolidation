package reports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import koasiaco.DBUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class RunIRTaccountDetail {
	
	//查询账号明细的主函数功能 by company code
	public static JSONArray startBalance(String Companies, String currency, String Fiscal_Year, String Posting_period,
			String structure) throws SQLException {
		String[] companycode = Companies.split(",");
		JSONArray jsonRsArray = new JSONArray();

		Connection conn = DBUtil.getConnection();
		Statement stmt = conn.createStatement();
		Statement stmtTBV = conn.createStatement();
		switch (structure) {
		case "REVENUES":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Change in Stockvalue":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Other Capitalized Costs":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Direct Material":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Material Handling":
			jsonRsArray = getPLResult(stmt, companycode, "'001 Material Handling'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Direct Labor":
			jsonRsArray = getPLResult(stmt, companycode, "'002 Direct Labor'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Indirect Labor":
			jsonRsArray = getPLResult(stmt, companycode, "'003 Indirect Labor'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Depreciation of Machinery":
			jsonRsArray = getPLResult(stmt, companycode, "'004 Depn of Machinery''", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Other Manufacturing Costs":
			jsonRsArray = getPLResult(stmt, companycode,
					"'005 Other Manufacturing Cost Direct','006 Other Manufacturing Cost Indirect'", structure,
					currency, Fiscal_Year, Posting_period, 1);
			break;
		case "Engineering":
			jsonRsArray = getPLResult(stmt, companycode, "'007 Engineering'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Administration":
			jsonRsArray = getPLResult(stmt, companycode, "'008 Administration'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Sales":
			jsonRsArray = getPLResult(stmt, companycode, "'009 Sales'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Monetary Corrections":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Exchange Gains/Losses":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Interest Income / Expenses":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Other Income / Expenses":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Productive Wages":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Unproductive Wages":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Overtime Premium":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Other Fringe Benefits Wages":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Salaries":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Overtime":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Other Fringe Benefits":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Consumables":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Energy":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Repairwork (external)":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Leasing/Depreciation (Build.)":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Other Leasing Costs":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Other Depreciation":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Engineering Cost Depreciation":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Interest Costs":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Taxes/Duties":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Insurances":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Travel and Subsistance":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Telephone/Telex/Telefax/Mail":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Legal/Audit":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Airfreight":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Surface Freight":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Canteen":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Royalties":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "GCSP":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Other Costs":
			jsonRsArray = getResult(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Land/Building":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Machinery/Equipment":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Tools/Devices":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Other Fixed Assets":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Rawmaterial/Parts":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "WiP/FG":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Engineering Services":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Tools/Devices/Samples":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Accounts Receivable":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Group Receivables":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Clearing Receivables":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Bank Balances and Cash":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Other Current Assets":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Share Capital":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Capital Reserve":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Profit/Loss previous years":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Profit/Loss":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Provisions for Retirements":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Longterm Bank Loans":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "LK-Loan":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Accounts Payables":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Group Payables":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Clearing Payables":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Shortterm Bank Loans":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Shortterm Provisions":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Other Shortterm Liabilities":
			jsonRsArray = getBalance(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, -1);
			break;
		case "Invest Land/Building":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Invest Machinery/Equipment":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Invest Tools/Devices":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Invest Engineering Services":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Invest Other Fixed Assets":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Invest Land/Building IC":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Invest Machinery/Equipment IC":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Invest Tools/Devices IC":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Invest Engineering Services IC":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Invest Other Fixed Assets IC":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Retirement Land/Building":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Retirement Machinery/Equipment":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Retirement Tools/Devices":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Retirement Engineering Services":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Retirement Other Fixed Assets":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Retirement Land/Building IC":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Retirement Machinery/Equipment IC":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Retirement Tools/Devices IC":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Retirement Engineering Services IC":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
			break;
		case "Retirement Other Fixed Assets IC":
			jsonRsArray = getInvestment(stmt, companycode, "'" + structure + "'", structure, currency, Fiscal_Year,
					Posting_period, 1);
		}
		stmt.close();
		stmtTBV.close();
		conn.close();

		return jsonRsArray;
	}

	//获取投资明细
	public static JSONArray getInvestment(Statement stmt, String[] CompanyCode, String IRT_Structure,
			String IRT_Structure_Name, String currency, String Fiscal_Year, String Posting_period, int denominator)
			throws SQLException {
		JSONArray jsonRsArray = new JSONArray();
		DecimalFormat df = new DecimalFormat("0.00");
		if (IRT_Structure.contains(",")) {
			IRT_Structure = IRT_Structure.replaceAll(",", " OR `IRT_Structure` =  ");
		}
		String CompanyCodeCondition = " AND (";
		for (int i = 0; i < CompanyCode.length; i++) {
			CompanyCodeCondition = CompanyCodeCondition + "`CoCd` = '" + CompanyCode[i].toString() + "' OR";
		}
		CompanyCodeCondition = CompanyCodeCondition.substring(0, CompanyCodeCondition.lastIndexOf(" OR")) + ")";

		String sql = " SELECT `Asset`, `Description`,SUM(`currectAcquisition`) / 1000 AS 'currectAcquisition'  FROM report_irt_basic_info_investment  WHERE (`IRT_Structure` = "
				+ IRT_Structure + ")" + CompanyCodeCondition + " AND `Crcy` = '" + currency + "' AND `Year` = '"
				+ Fiscal_Year + "' AND `Period` = '" + Posting_period + "' GROUP BY `Asset`";

		ResultSet rsAccounts = stmt.executeQuery(sql);
		rsAccounts.last();
		int rowsCount = rsAccounts.getRow();
		int n = 0;

		Object[] accountNumbers = new Object[rowsCount];
		rsAccounts = stmt.executeQuery(sql);
		while (rsAccounts.next()) {
			accountNumbers[n] = rsAccounts.getString("Asset");
			n++;
		}
		for (int j = 0; j < accountNumbers.length + 1; j++) {
			JSONObject jsonObj = new JSONObject();
			if (j < accountNumbers.length) {
				for (int i = 0; i < CompanyCode.length + 1; i++) {
					if (i < CompanyCode.length) {
						String sql_getResult = "SELECT `Asset`, `Description`,SUM(`currectAcquisition`) / 1000 AS 'SUM of currectAcquisition'  FROM report_irt_basic_info_investment  WHERE (`IRT_Structure` = "
								+ IRT_Structure + ") AND `CoCd` = '" + CompanyCode[i].toString() + "' AND `Crcy` = '"
								+ currency + "' AND `Year` = '" + Fiscal_Year + "' AND `Period` = '" + Posting_period
								+ "' AND `Asset` = '" + accountNumbers[j] + "' GROUP BY `Crcy`, `Asset`, `Description`";

						ResultSet rs = stmt.executeQuery(sql_getResult);
						while (rs.next()) {
							jsonObj.put("Structure", IRT_Structure_Name);
							jsonObj.put("Account Number", rs.getString("Asset"));
							jsonObj.put("GL_Description", rs.getString("Description"));
							jsonObj.put(CompanyCode[i].toString(),
									df.format(rs.getDouble("SUM of currectAcquisition") / denominator));
						}
					} else {
						String sql_getResult = "SELECT `Asset`, `Description`,SUM(`currectAcquisition`) / 1000 AS 'SUM of currectAcquisition'  FROM report_irt_basic_info_investment  WHERE (`IRT_Structure` = "
								+ IRT_Structure + ")" + CompanyCodeCondition + " AND `Crcy` = '" + currency
								+ "' AND `Year` = '" + Fiscal_Year + "' AND `Period` = '" + Posting_period
								+ "' AND `Asset` = '" + accountNumbers[j] + "' GROUP BY `Crcy`, `Asset`, `Description`";

						ResultSet rs = stmt.executeQuery(sql_getResult);
						while (rs.next()) {
							jsonObj.put("Structure", IRT_Structure_Name);
							jsonObj.put("Account Number", rs.getString("Asset"));
							jsonObj.put("GL_Description", rs.getString("Description"));
							jsonObj.put("Total", df.format(rs.getDouble("SUM of currectAcquisition") / denominator));
						}
					}
				}
			} else {
				for (int i = 0; i < CompanyCode.length + 1; i++) {
					if (i < CompanyCode.length) {
						String sql_getResult = "SELECT SUM(`currectAcquisition`) / 1000 AS 'SUM of currectAcquisition'  FROM report_irt_basic_info_investment  WHERE (`IRT_Structure` = "
								+ IRT_Structure + ") AND `CoCd` = '" + CompanyCode[i].toString() + "' AND `Crcy` = '"
								+ currency + "' AND `Year` = '" + Fiscal_Year + "' AND `Period` = '" + Posting_period
								+ "' GROUP BY `Crcy`";

						ResultSet rs = stmt.executeQuery(sql_getResult);
						while (rs.next()) {
							jsonObj.put("Structure", IRT_Structure_Name);
							jsonObj.put("Account Number", IRT_Structure_Name);
							jsonObj.put("GL_Description", "");
							jsonObj.put(CompanyCode[i].toString(),
									df.format(rs.getDouble("SUM of currectAcquisition") / denominator));
						}
					} else {
						String sql_getResult = "SELECT SUM(`currectAcquisition`) / 1000 AS 'SUM of currectAcquisition'  FROM report_irt_basic_info_investment  WHERE (`IRT_Structure` = "
								+ IRT_Structure + ")" + CompanyCodeCondition + " AND `Crcy` = '" + currency
								+ "' AND `Year` = '" + Fiscal_Year + "' AND `Period` = '" + Posting_period
								+ "' GROUP BY `Crcy`";

						ResultSet rs = stmt.executeQuery(sql_getResult);
						while (rs.next()) {
							jsonObj.put("Structure", IRT_Structure_Name);
							jsonObj.put("Account Number", IRT_Structure_Name);
							jsonObj.put("GL_Description", "");
							jsonObj.put("Total", df.format(rs.getDouble("SUM of currectAcquisition") / denominator));
						}
					}
				}
			}
			jsonRsArray.add(j, jsonObj);
		}
		return jsonRsArray;
	}

	//获取PL structure结果
	public static JSONArray getPLResult(Statement stmt, String[] CompanyCode, String IRT_Structure,
			String IRT_Structure_Name, String currency, String Fiscal_Year, String Posting_period, int denominator)
			throws SQLException {
		JSONArray jsonRsArray = new JSONArray();
		DecimalFormat df = new DecimalFormat("0.00");
		if (IRT_Structure.contains(",")) {
			IRT_Structure = IRT_Structure.replaceAll(",", " OR `P&L Structure` =  ");
		}
		String CompanyCodeCondition = " AND (";
		for (int i = 0; i < CompanyCode.length; i++) {
			CompanyCodeCondition = CompanyCodeCondition + "`CoCd` = '" + CompanyCode[i].toString() + "' OR";
		}
		CompanyCodeCondition = CompanyCodeCondition.substring(0, CompanyCodeCondition.lastIndexOf(" OR")) + ")";

		String sql = " SELECT `Account`, `Final CC`,SUM(`In company code currency`) / 1000 AS 'SUM of Balance'  FROM report_irt_basic_info_plstructure  WHERE (`P&L Structure` = "
				+ IRT_Structure + ")" + CompanyCodeCondition + " AND `Local Crcy` = '" + currency + "' AND `Year` = '"
				+ Fiscal_Year + "' AND `Period` = '" + Posting_period + "' GROUP BY `Account`, `Final CC`";

		ResultSet rsAccounts = stmt.executeQuery(sql);
		rsAccounts.last();
		int rowsCount = rsAccounts.getRow();
		int n = 0;

		Object[] accountNumbers = new Object[rowsCount];
		Object[] ccNumbers = new Object[rowsCount];
		rsAccounts = stmt.executeQuery(sql);
		while (rsAccounts.next()) {
			accountNumbers[n] = rsAccounts.getString("Account");
			ccNumbers[n] = rsAccounts.getString("Final CC");
			n++;
		}
		for (int j = 0; j < accountNumbers.length + 1; j++) {
			JSONObject jsonObj = new JSONObject();
			if (j < accountNumbers.length) {
				for (int i = 0; i < CompanyCode.length + 1; i++) {
					if (i < CompanyCode.length) {
						String sql_getResult = "SELECT `Account`, `Final CC`,SUM(`In company code currency`) / 1000 AS 'SUM of Balance'  FROM report_irt_basic_info_plstructure  WHERE (`P&L Structure` = "
								+ IRT_Structure + ") AND `CoCd` = '" + CompanyCode[i].toString()
								+ "' AND `Local Crcy` = '" + currency + "' AND `Year` = '" + Fiscal_Year
								+ "' AND `Period` = '" + Posting_period + "' AND `Account` = '" + accountNumbers[j]
								+ "' AND `Final CC` = '" + ccNumbers[j]
								+ "' GROUP BY `Local Crcy`, `Account`, `Final CC`";

						ResultSet rs = stmt.executeQuery(sql_getResult);
						while (rs.next()) {
							jsonObj.put("Structure", IRT_Structure_Name);
							jsonObj.put("Account Number", rs.getString("Account"));
							jsonObj.put("GL_Description", rs.getString("Final CC"));
							jsonObj.put(CompanyCode[i].toString(),
									df.format(rs.getDouble("SUM of Balance") / denominator));
						}
					} else {
						String sql_getResult = "SELECT `Account`, `Final CC`,SUM(`In company code currency`) / 1000 AS 'SUM of Balance'  FROM report_irt_basic_info_plstructure  WHERE (`P&L Structure` = "
								+ IRT_Structure + ")" + CompanyCodeCondition + " AND `Local Crcy` = '" + currency
								+ "' AND `Year` = '" + Fiscal_Year + "' AND `Period` = '" + Posting_period
								+ "' AND `Account` = '" + accountNumbers[j] + "' AND `Final CC` = '" + ccNumbers[j]
								+ "' GROUP BY `Local Crcy`, `Account`, `Final CC`";

						ResultSet rs = stmt.executeQuery(sql_getResult);
						while (rs.next()) {
							jsonObj.put("Structure", IRT_Structure_Name);
							jsonObj.put("Account Number", rs.getString("Account"));
							jsonObj.put("GL_Description", rs.getString("Final CC"));
							jsonObj.put("Total", df.format(rs.getDouble("SUM of Balance") / denominator));
						}
					}
				}
			} else {
				for (int i = 0; i < CompanyCode.length + 1; i++) {
					if (i < CompanyCode.length) {
						String sql_getResult = "SELECT SUM(`In company code currency`) / 1000 AS 'SUM of Balance'  FROM report_irt_basic_info_plstructure  WHERE (`P&L Structure` = "
								+ IRT_Structure + ") AND `CoCd` = '" + CompanyCode[i].toString()
								+ "' AND `Local Crcy` = '" + currency + "' AND `Year` = '" + Fiscal_Year
								+ "' AND `Period` = '" + Posting_period + "' GROUP BY `Local Crcy`";

						ResultSet rs = stmt.executeQuery(sql_getResult);
						while (rs.next()) {
							jsonObj.put("Structure", IRT_Structure_Name);
							jsonObj.put("Account Number", IRT_Structure_Name);
							jsonObj.put("GL_Description", "");
							jsonObj.put(CompanyCode[i].toString(),
									df.format(rs.getDouble("SUM of Balance") / denominator));
						}
					} else {
						String sql_getResult = "SELECT SUM(`In company code currency`) / 1000 AS 'SUM of Balance'  FROM report_irt_basic_info_plstructure  WHERE (`P&L Structure` = "
								+ IRT_Structure + ")" + CompanyCodeCondition + " AND `Local Crcy` = '" + currency
								+ "' AND `Year` = '" + Fiscal_Year + "' AND `Period` = '" + Posting_period
								+ "' GROUP BY `Local Crcy`";

						ResultSet rs = stmt.executeQuery(sql_getResult);
						while (rs.next()) {
							jsonObj.put("Structure", IRT_Structure_Name);
							jsonObj.put("Account Number", IRT_Structure_Name);
							jsonObj.put("GL_Description", "");
							jsonObj.put("Total", df.format(rs.getDouble("SUM of Balance") / denominator));
						}
					}
				}
			}
			jsonRsArray.add(j, jsonObj);
		}
		return jsonRsArray;
	}

	//获取账号发生额
	public static JSONArray getResult(Statement stmt, String[] CompanyCode, String IRT_Structure,
			String IRT_Structure_Name, String currency, String Fiscal_Year, String Posting_period, int denominator)
			throws SQLException {
		JSONArray jsonRsArray = new JSONArray();
		DecimalFormat df = new DecimalFormat("0.00");
		if (IRT_Structure.contains(",")) {
			IRT_Structure = IRT_Structure.replaceAll(",", " OR `IRT Structure` =  ");
		}
		String CompanyCodeCondition = " AND (";
		for (int i = 0; i < CompanyCode.length; i++) {
			CompanyCodeCondition = CompanyCodeCondition + "`Company Code` = '" + CompanyCode[i].toString() + "' OR";
		}
		CompanyCodeCondition = CompanyCodeCondition.substring(0, CompanyCodeCondition.lastIndexOf(" OR")) + ")";

		String sql = " SELECT `Account Number`, `GL Description`,SUM(`Balance LC`) / 1000 AS 'SUM of Balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = "
				+ IRT_Structure + ")" + CompanyCodeCondition + " AND `Currency` = '" + currency
				+ "' AND `Fiscal Year` = '" + Fiscal_Year + "' AND `Posting period` = '" + Posting_period
				+ "' GROUP BY `Account Number`, `GL Description`";

		ResultSet rsAccounts = stmt.executeQuery(sql);
		rsAccounts.last();
		int rowsCount = rsAccounts.getRow();
		int n = 0;

		Object[] accountNumbers = new Object[rowsCount];
		rsAccounts = stmt.executeQuery(sql);
		while (rsAccounts.next()) {
			accountNumbers[n] = rsAccounts.getString("Account Number");
			n++;
		}
		for (int j = 0; j < accountNumbers.length + 1; j++) {
			JSONObject jsonObj = new JSONObject();
			if (j < accountNumbers.length) {
				for (int i = 0; i < CompanyCode.length + 1; i++) {
					if (i < CompanyCode.length) {
						String sql_getResult = "SELECT `Account Number`, `GL Description`,SUM(`Balance LC`) / 1000 AS 'SUM of Balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = "
								+ IRT_Structure + ") AND `Company Code` = '" + CompanyCode[i].toString()
								+ "' AND `Currency` = '" + currency + "' AND `Fiscal Year` = '" + Fiscal_Year
								+ "' AND `Posting period` = '" + Posting_period + "' AND `Account Number` = '"
								+ accountNumbers[j] + "' GROUP BY `Currency`, `Account Number`, `GL Description`";

						ResultSet rs = stmt.executeQuery(sql_getResult);
						while (rs.next()) {
							jsonObj.put("Structure", IRT_Structure_Name);
							jsonObj.put("Account Number", rs.getString("Account Number"));
							jsonObj.put("GL_Description", rs.getString("GL Description"));
							jsonObj.put(CompanyCode[i].toString(),
									df.format(rs.getDouble("SUM of Balance") / denominator));
						}
					} else {
						String sql_getResult = "SELECT `Account Number`, `GL Description`,SUM(`Balance LC`) / 1000 AS 'SUM of Balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = "
								+ IRT_Structure + ")" + CompanyCodeCondition + " AND `Currency` = '" + currency
								+ "' AND `Fiscal Year` = '" + Fiscal_Year + "' AND `Posting period` = '"
								+ Posting_period + "' AND `Account Number` = '" + accountNumbers[j]
								+ "' GROUP BY `Currency`, `Account Number`, `GL Description`";

						ResultSet rs = stmt.executeQuery(sql_getResult);
						while (rs.next()) {
							jsonObj.put("Structure", IRT_Structure_Name);
							jsonObj.put("Account Number", rs.getString("Account Number"));
							jsonObj.put("GL_Description", rs.getString("GL Description"));
							jsonObj.put("Total", df.format(rs.getDouble("SUM of Balance") / denominator));
						}
					}
				}
			} else {
				for (int i = 0; i < CompanyCode.length + 1; i++) {
					if (i < CompanyCode.length) {
						String sql_getResult = "SELECT SUM(`Balance LC`) / 1000 AS 'SUM of Balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = "
								+ IRT_Structure + ") AND `Company Code` = '" + CompanyCode[i].toString()
								+ "' AND `Currency` = '" + currency + "' AND `Fiscal Year` = '" + Fiscal_Year
								+ "' AND `Posting period` = '" + Posting_period + "' GROUP BY `Currency`";

						ResultSet rs = stmt.executeQuery(sql_getResult);
						while (rs.next()) {
							jsonObj.put("Structure", IRT_Structure_Name);
							jsonObj.put("Account Number", IRT_Structure_Name);
							jsonObj.put("GL_Description", "");
							jsonObj.put(CompanyCode[i].toString(),
									df.format(rs.getDouble("SUM of Balance") / denominator));
						}
					} else {
						String sql_getResult = "SELECT SUM(`Balance LC`) / 1000 AS 'SUM of Balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = "
								+ IRT_Structure + ")" + CompanyCodeCondition + " AND `Currency` = '" + currency
								+ "' AND `Fiscal Year` = '" + Fiscal_Year + "' AND `Posting period` = '"
								+ Posting_period + "' GROUP BY `Currency`";

						ResultSet rs = stmt.executeQuery(sql_getResult);
						while (rs.next()) {
							jsonObj.put("Structure", IRT_Structure_Name);
							jsonObj.put("Account Number", IRT_Structure_Name);
							jsonObj.put("GL_Description", "");
							jsonObj.put("Total", df.format(rs.getDouble("SUM of Balance") / denominator));
						}
					}
				}
			}
			jsonRsArray.add(j, jsonObj);
		}
		return jsonRsArray;
	}

	//获取账号余额
	public static JSONArray getBalance(Statement stmt, String[] CompanyCode, String IRT_Structure,
			String IRT_Structure_Name, String currency, String Fiscal_Year, String Posting_period, int denominator)
			throws SQLException {
		JSONArray jsonRsArray = new JSONArray();
		DecimalFormat df = new DecimalFormat("0.00");
		if (IRT_Structure.contains(",")) {
			IRT_Structure = IRT_Structure.replaceAll(",", " OR `IRT Structure` =  ");
		}
		String CompanyCodeCondition = " AND (";
		for (int i = 0; i < CompanyCode.length; i++) {
			CompanyCodeCondition = CompanyCodeCondition + "`Company Code` = '" + CompanyCode[i].toString() + "' OR";
		}
		CompanyCodeCondition = CompanyCodeCondition.substring(0, CompanyCodeCondition.lastIndexOf(" OR")) + ")";

		String sql = " SELECT `Account Number`, `GL Description`,SUM(`Accumulated balance LC`) / 1000 AS 'SUM of Accumulated Balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = "
				+ IRT_Structure + ")" + CompanyCodeCondition + " AND `Currency` = '" + currency
				+ "' AND `Fiscal Year` = '" + Fiscal_Year + "' AND `Posting period` = '" + Posting_period
				+ "' GROUP BY `Account Number`, `GL Description`";

		ResultSet rsAccounts = stmt.executeQuery(sql);
		rsAccounts.last();
		int rowsCount = rsAccounts.getRow();
		int n = 0;

		Object[] accountNumbers = new Object[rowsCount];
		rsAccounts = stmt.executeQuery(sql);
		while (rsAccounts.next()) {
			accountNumbers[n] = rsAccounts.getString("Account Number");
			n++;
		}
		int j = 0;
		for (j = 0; j < accountNumbers.length + 1; j++) {
			JSONObject jsonObj = new JSONObject();
			if (j < accountNumbers.length) {
				for (int i = 0; i < CompanyCode.length + 1; i++) {
					if (i < CompanyCode.length) {
						String sql_getResult = "SELECT `Account Number`, `GL Description`,SUM(`Accumulated balance LC`) / 1000 AS 'SUM of Accumulated Balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = "
								+ IRT_Structure + ") AND `Company Code` = '" + CompanyCode[i].toString()
								+ "' AND `Currency` = '" + currency + "' AND `Fiscal Year` = '" + Fiscal_Year
								+ "' AND `Posting period` = '" + Posting_period + "' AND `Account Number` = '"
								+ accountNumbers[j] + "' GROUP BY `Currency`, `Account Number`, `GL Description`";

						ResultSet rs = stmt.executeQuery(sql_getResult);
						while (rs.next()) {
							jsonObj.put("Structure", IRT_Structure_Name);
							jsonObj.put("Account Number", rs.getString("Account Number"));
							jsonObj.put("GL_Description", rs.getString("GL Description"));
							jsonObj.put(CompanyCode[i].toString(),
									df.format(rs.getDouble("SUM of Accumulated Balance") / denominator));
						}
					} else {
						String sql_getResult = "SELECT `Account Number`, `GL Description`,SUM(`Accumulated balance LC`) / 1000 AS 'SUM of Accumulated Balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = "
								+ IRT_Structure + ")" + CompanyCodeCondition + " AND `Currency` = '" + currency
								+ "' AND `Fiscal Year` = '" + Fiscal_Year + "' AND `Posting period` = '"
								+ Posting_period + "' AND `Account Number` = '" + accountNumbers[j]
								+ "' GROUP BY `Currency`, `Account Number`, `GL Description`";

						ResultSet rs = stmt.executeQuery(sql_getResult);
						while (rs.next()) {
							jsonObj.put("Structure", IRT_Structure_Name);
							jsonObj.put("Account Number", rs.getString("Account Number"));
							jsonObj.put("GL_Description", rs.getString("GL Description"));
							jsonObj.put("Total", df.format(rs.getDouble("SUM of Accumulated Balance") / denominator));
						}
					}
				}
			} else {
				for (int i = 0; i < CompanyCode.length + 1; i++) {
					if (i < CompanyCode.length) {
						String sql_getResult = "SELECT SUM(`Accumulated balance LC`) / 1000 AS 'SUM of Accumulated Balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = "
								+ IRT_Structure + ") AND `Company Code` = '" + CompanyCode[i].toString()
								+ "' AND `Currency` = '" + currency + "' AND `Fiscal Year` = '" + Fiscal_Year
								+ "' AND `Posting period` = '" + Posting_period + "' GROUP BY `Currency`";

						ResultSet rs = stmt.executeQuery(sql_getResult);
						while (rs.next()) {
							jsonObj.put("Structure", IRT_Structure_Name);
							jsonObj.put("Account Number", IRT_Structure_Name);
							jsonObj.put("GL_Description", "");
							jsonObj.put(CompanyCode[i].toString(),
									df.format(rs.getDouble("SUM of Accumulated Balance") / denominator));
						}
					} else {
						String sql_getResult = "SELECT SUM(`Accumulated balance LC`) / 1000 AS 'SUM of Accumulated Balance'  FROM report_irt_basic_info  WHERE (`IRT Structure` = "
								+ IRT_Structure + ")" + CompanyCodeCondition + " AND `Currency` = '" + currency
								+ "' AND `Fiscal Year` = '" + Fiscal_Year + "' AND `Posting period` = '"
								+ Posting_period + "' GROUP BY `Currency`";

						ResultSet rs = stmt.executeQuery(sql_getResult);
						while (rs.next()) {
							jsonObj.put("Structure", IRT_Structure_Name);
							jsonObj.put("Account Number", IRT_Structure_Name);
							jsonObj.put("GL_Description", "");
							jsonObj.put("Total", df.format(rs.getDouble("SUM of Accumulated Balance") / denominator));
						}
					}
				}
			}
			jsonRsArray.add(j, jsonObj);
		}
		JSONObject jsonObj = new JSONObject();
		if (IRT_Structure_Name.equals("Profit/Loss")) {
			jsonObj = RunIRT.getBalance(stmt, CompanyCode,
					"'Overtime','Royalties','GCSP','Direct Material','Engineering Cost Depreciation','Interest Costs','REVENUES','Other Costs','Consumables','Productive Wages','Overtime Premium','Other Fringe Benefits Wages','Unproductive Wages','Salaries','Other Fringe Benefits','Canteen','Energy','Repairwork (external)','Taxes/Duties','Leasing/Depreciation (Build.)','Other Depreciation','Travel and Subsistance','Other Leasing Costs','Surface Freight','Airfreight','Other Capitalized Costs','Change in Stockvalue','Other Income / Expenses','Exchange Gains/Losses','Interest Income / Expenses','Legal/Audit','Insurances','Telephone/Telex/Telefax/Mail'",
					"Profit/Loss", currency, Fiscal_Year, Posting_period, -1);
			jsonObj.put("Structure", IRT_Structure_Name);
			jsonObj.put("Account Number", "Profit");
			jsonObj.put("GL_Description", "");
			jsonRsArray.add(j, jsonObj);
		}
		return jsonRsArray;
	}
}