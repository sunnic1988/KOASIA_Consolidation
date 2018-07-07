package reports;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import koasiaco.DBUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class basicInfo {
	
	//准备PL structure数据（CNY and Euro）
	public static void basic_plstucture(String Companies, String Fiscal_Year, String Posting_period)
			throws SQLException {
		JSONObject rows = null;
		PreparedStatement prep = null;
		JSONObject jsonPLObj = new JSONObject();
		String[] mainCurrency = { "CNY", "EUR" };

		String sql_save = " INSERT INTO report_irt_basic_info_plstructure (`Year`,`Period`,`CoCd`, `Account`,`Cost Ctr`,`Order`,`WBS Element`,`Sales Doc`,`In company code currency`,`Local Crcy`,`Req CC Order`,`Req CC WBS`,`Final CC`,`P&L Structure`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sql_del = "delete from report_irt_basic_info_plstructure Where (`CoCd` = " + Companies
				+ ") AND `Year` = " + Fiscal_Year + " AND `Period` = " + Posting_period;

		String sql_selectPL = "Select * from basic_info_plstructure Where (`CoCd` = " + Companies + ") AND `Year` = "
				+ Fiscal_Year + " AND `Period` = " + Posting_period;

		Connection conn = DBUtil.getConnection();

		//删除结果表中的内容report_irt_basic_info_plstructure
		conn.prepareStatement(sql_del).executeUpdate();
		Statement stmt = conn.createStatement();
		prep = conn.prepareStatement(sql_save);

		//查询出相关内容，保存在jsonArray中进行计算
		conn.setAutoCommit(false);
		for (int j = 0; j < mainCurrency.length; j++) {
			JSONArray jsonPLArray = new JSONArray();

			ResultSet rs = stmt.executeQuery(sql_selectPL);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int n = 0; rs.next(); n++) {
				for (int i = 1; i <= columnCount; i++) {
					String columnName = metaData.getColumnLabel(i);
					String value = rs.getString(columnName);
					jsonPLObj.put(columnName, value);
				}
				jsonPLArray.add(n, jsonPLObj);
			}
			
			for (int n = 0; n < jsonPLArray.size(); n++) {
				rows = jsonPLArray.getJSONObject(n);
				String Year = rows.getString("Year");
				String Period = rows.getString("Period");
				String CoCd = rows.getString("CoCd");
				String Account = rows.getString("Account");
				String Cost_Ctr = rows.getString("Cost Ctr");
				String Order = rows.getString("Order");
				String WBS_Element = rows.getString("WBS Element");
				String Sales_Doc = rows.getString("Sales Doc");
				double In_company_code_currency = rows.getDouble("In company code currency");
				String Local_Crcy = rows.getString("Local Crcy");
				String Req_CC_Order = rows.getString("Req CC Order");
				String Req_CC_WBS = rows.getString("Req CC WBS");
				String Final_CC = rows.getString("Final CC");
				String PL_Structure = rows.getString("P&L Structure");
				// String ExRType = rows.getString("ExRType");
				int RatioFrom = rows.getInt("RatioFrom");
				String FromCurrency = rows.getString("FromCurrency");
				double DirQuot = rows.getDouble("DirQuot");
				if (!Local_Crcy.equals(mainCurrency[j])) {
					if (mainCurrency[j].equals(FromCurrency)) {
						In_company_code_currency = In_company_code_currency / DirQuot / RatioFrom;
					}
				} else {
					prep.setString(1, Year);
					prep.setString(2, Period);
					prep.setString(3, CoCd);
					prep.setString(4, Account);
					prep.setString(5, Cost_Ctr);
					prep.setString(6, Order);
					prep.setString(7, WBS_Element);
					prep.setString(8, Sales_Doc);
					prep.setDouble(9, In_company_code_currency);
					prep.setString(10, mainCurrency[j]);
					prep.setString(11, Req_CC_Order);
					prep.setString(12, Req_CC_WBS);
					prep.setString(13, Final_CC);
					prep.setString(14, PL_Structure);
					prep.addBatch();
				}
			}
			prep.executeBatch();
			rs.close();
		}
		System.out.println("PL_stucture update process finished");
		conn.commit();
		prep.close();
		conn.close();
	}

	//PL structure调整计算，因为operating costs账号再SQVI中导的是当月明细，和F.01的对应账号通过累计余额计算的发生比较，有差异的调整进other manufacture costs
	public static void basic_plstuctureAdj(String Companies, String Fiscal_Year, String Posting_period)
			throws SQLException {
		JSONObject rows = null;
		PreparedStatement prep = null;
		JSONObject jsonObj = new JSONObject();

		String sql_save = " INSERT INTO report_irt_basic_info_plstructure (`Year`,`Period`,`CoCd`, `Account`,`Cost Ctr`,`Order`,`WBS Element`,`Sales Doc`,`In company code currency`,`Local Crcy`,`Req CC Order`,`Req CC WBS`,`Final CC`,`P&L Structure`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sql_select = "SELECT\r\nbasic_info_plstructrue_adjustment.`Year`,\r\nbasic_info_plstructrue_adjustment.CoCd,\r\nbasic_info_plstructrue_adjustment.Period,\r\nSUM(basic_info_plstructrue_adjustment.`Diff LC`) AS `Diff LC` ,\r\nbasic_info_plstructrue_adjustment.Currency,\r\nsys_exchage_rate.ExRType,\r\nsys_exchage_rate.RatioFrom,\r\nsys_exchage_rate.FromCurrency,\r\nsys_exchage_rate.DirQuot,\r\nsys_exchage_rate.ToCurrency\r\nFROM\r\nbasic_info_plstructrue_adjustment\r\nINNER JOIN sys_exchage_rate ON sys_exchage_rate.`Year` = basic_info_plstructrue_adjustment.`Year` AND sys_exchage_rate.Period = basic_info_plstructrue_adjustment.Period AND sys_exchage_rate.ToCurrency = basic_info_plstructrue_adjustment.Currency\r\nWHERE\r\nsys_exchage_rate.ExRType = 'PDK' AND\r\nbasic_info_plstructrue_adjustment.`Year` = "
				+ Fiscal_Year + " AND\r\nbasic_info_plstructrue_adjustment.Period = " + Posting_period
				+ " AND\r\n(basic_info_plstructrue_adjustment.CoCd = " + Companies
				+ ")GROUP BY\r\nbasic_info_plstructrue_adjustment.`Year`,\r\nbasic_info_plstructrue_adjustment.Period,\r\nbasic_info_plstructrue_adjustment.CoCd,\r\nbasic_info_plstructrue_adjustment.Currency";

		Connection conn = DBUtil.getConnection();

		Statement stmt = conn.createStatement();
		prep = conn.prepareStatement(sql_save);

		conn.setAutoCommit(false);

		JSONArray jsonArray = new JSONArray();

		ResultSet rs = stmt.executeQuery(sql_select);
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int n = 0; rs.next(); n++) {
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnLabel(i);
				String value = rs.getString(columnName);
				jsonObj.put(columnName, value);
			}
			jsonArray.add(n, jsonObj);
		}
		for (int n = 0; n < jsonArray.size(); n++) {
			rows = jsonArray.getJSONObject(n);
			String Year = rows.getString("Year");
			String Period = rows.getString("Period");
			String CoCd = rows.getString("CoCd");
			// String ExRType = rows.getString("ExRType");
			int RatioFrom = rows.getInt("RatioFrom");
			// String FromCurrency = rows.getString("FromCurrency");
			double DirQuot = rows.getDouble("DirQuot");
			double diff_LC = rows.getDouble("Diff LC");
			String ToCurrency = rows.getString("ToCurrency");
			if (diff_LC != 0.0D) {
				prep.setString(1, Year);
				prep.setString(2, Period);
				prep.setString(3, CoCd);
				prep.setString(4, "ADJ PL");
				prep.setString(5, "");
				prep.setString(6, "");
				prep.setString(7, "");
				prep.setString(8, "");
				prep.setDouble(9, -diff_LC);
				prep.setString(10, ToCurrency);
				prep.setString(11, "");
				prep.setString(12, "");
				prep.setString(13, "");
				prep.setString(14, "006 Other Manufacturing Cost Indirect");
				prep.addBatch();
				// Euro 调整
				prep.setString(1, Year);
				prep.setString(2, Period);
				prep.setString(3, CoCd);
				prep.setString(4, "ADJ PL");
				prep.setString(5, "");
				prep.setString(6, "");
				prep.setString(7, "");
				prep.setString(8, "");
				prep.setDouble(9, -diff_LC / DirQuot / RatioFrom);
				prep.setString(10, "EUR");
				prep.setString(11, "");
				prep.setString(12, "");
				prep.setString(13, "");
				prep.setString(14, "006 Other Manufacturing Cost Indirect");
				prep.addBatch();
			}
		}
		prep.executeBatch();
		rs.close();
		System.out.println("PL_stucture adjusted process finished");
		conn.commit();
		prep.close();
		conn.close();
	}

	//计算出down payment和 折旧，然后保存在report_irt_basic_info_investment
	public static void basic_downpayment_dep(String Companies, String Fiscal_Year, String Posting_period)
			throws SQLException {
		JSONObject rows = null;
		PreparedStatement prep = null;
		JSONObject jsonObj = new JSONObject();

		String sql_save = " INSERT INTO report_irt_basic_info_investment (`CoCd`,`Year`,`Period`, `IRT_Structure`,`Asset`,`SubNumber`,`Asset Class`,`currectAcquisition`,`Crcy`,`Class`,`Category`,`Description`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

		String sql_select = "SELECT basic_info_down_and_dep.`Company Code`, basic_info_down_and_dep.`Fiscal Year`, basic_info_down_and_dep.`Posting period`, basic_info_down_and_dep.`IRT Structure`, basic_info_down_and_dep.`Balance LC`, basic_info_down_and_dep.Currency FROM basic_info_down_and_dep WHERE (basic_info_down_and_dep.`Company Code` = "
				+ Companies + ") AND basic_info_down_and_dep.`Fiscal Year` = " + Fiscal_Year
				+ " AND basic_info_down_and_dep.`Posting period` = " + sqlcondition(Posting_period);

		Connection conn = DBUtil.getConnection();
		Statement stmt = conn.createStatement();
		prep = conn.prepareStatement(sql_save);

		conn.setAutoCommit(false);

		JSONArray jsonArray = new JSONArray();

		ResultSet rs = stmt.executeQuery(sql_select);
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int n = 0; rs.next(); n++) {
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnLabel(i);
				String value = rs.getString(columnName);
				jsonObj.put(columnName, value);
			}
			jsonArray.add(n, jsonObj);
		}
		
		for (int n = 0; n < jsonArray.size(); n++) {
			rows = jsonArray.getJSONObject(n);
			String Cocd = rows.getString("Company Code");
			String Year = rows.getString("Fiscal Year");
			String Period = rows.getString("Posting period");
			String IRT_Structure = rows.getString("IRT Structure");
			double Balance_LC = rows.getDouble("Balance LC");
			String Crcy = rows.getString("Currency");
			if (IRT_Structure.equals("Other Fixed Assets")) {
				prep.setString(1, Cocd);
				prep.setString(2, Year);
				prep.setString(3, Period);
				prep.setString(4, "Invest Other Fixed Assets");
				prep.setString(5, "Down00");
				prep.setString(6, "");
				prep.setString(7, "");
				prep.setDouble(8, Balance_LC);
				prep.setString(9, Crcy);
				prep.setString(10, "");
				prep.setString(11, "");
				prep.setString(12, "Down payment");
				prep.addBatch();
			} else if (IRT_Structure.equals("Other Income / Expenses")) {
				prep.setString(1, Cocd);
				prep.setString(2, Year);
				prep.setString(3, Period);
				prep.setString(4, "TOTAL DEPRECIATION");
				prep.setString(5, "Dep000");
				prep.setString(6, "");
				prep.setString(7, "");
				prep.setDouble(8, Balance_LC);
				prep.setString(9, Crcy);
				prep.setString(10, "");
				prep.setString(11, "");
				prep.setString(12, "TOTAL DEPRECIATION");
				prep.addBatch();
			}
		}
		prep.executeBatch();
		rs.close();
		System.out.println("Down payment and DEPRECIATION was finished");
		conn.commit();
		prep.close();
	}

	//计算投资数据保存在 report_irt_basic_info_investment
	public static void basic_investment(String Companies, String Fiscal_Year, String Posting_period)
			throws SQLException {
		JSONObject rows = null;
		PreparedStatement prep = null;
		JSONObject jsonObj = new JSONObject();
		String currentPeriod = Posting_period;
		String lastPeriod = String.valueOf(Integer.parseInt(Posting_period) - 1);

		String sql_save = " INSERT INTO report_irt_basic_info_investment (`CoCd`,`Year`,`Period`, `IRT_Structure`,`Asset`,`SubNumber`,`Asset Class`,`currectAcquisition`,`Crcy`,`Class`,`Category`,`Description`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		String sql_del = "delete from report_irt_basic_info_investment Where (`CoCd` = " + Companies + ") AND `Year` = "
				+ Fiscal_Year + " AND `Period` = " + Posting_period;

		String sql_select = "SELECT report_irt_investment.CoCd, report_irt_investment.`Year`, sys_class_investment.IRT_Structure, report_irt_investment.Asset, report_irt_investment.SubNumber, report_irt_investment.`Asset Class`, sum(if((report_irt_investment.Period = "
				+ sqlcondition(currentPeriod)
				+ "),report_irt_investment.Acquisition,0)) - sum(if((report_irt_investment.Period = "
				+ sqlcondition(lastPeriod)
				+ "),report_irt_investment.Acquisition,0)) as currectAcquisition, report_irt_investment.Crcy, report_irt_investment.Class, report_irt_investment.Category, report_irt_investment.Description, sys_exchage_rate.RatioFrom, sys_exchage_rate.FromCurrency, sum(if((report_irt_investment.Period = "
				+ sqlcondition(currentPeriod)
				+ "),sys_exchage_rate.DirQuot,0)) AS currectDirQuot, sys_exchage_rate.ToCurrency FROM report_irt_investment INNER JOIN sys_class_investment ON sys_class_investment.`Asset Class` = report_irt_investment.`Asset Class` AND sys_class_investment.Category = report_irt_investment.Category AND sys_class_investment.Class = report_irt_investment.Class INNER JOIN sys_exchage_rate ON sys_exchage_rate.`Year` = report_irt_investment.`Year` AND sys_exchage_rate.Period = report_irt_investment.Period WHERE (report_irt_investment.Period = "
				+ sqlcondition(currentPeriod) + " OR report_irt_investment.Period = " + sqlcondition(lastPeriod)
				+ ") AND (report_irt_investment.`Asset Class` <> '5070' and report_irt_investment.`Asset Class` <> '5075' and report_irt_investment.`Asset Class` <> '5085') AND report_irt_investment.`Year` = "
				+ Fiscal_Year + " AND (report_irt_investment.CoCd = " + Companies
				+ ") AND sys_exchage_rate.ExRType = 'SK' GROUP BY report_irt_investment.Asset, report_irt_investment.SubNumber, report_irt_investment.`Asset Class`, report_irt_investment.CoCd, report_irt_investment.Crcy, report_irt_investment.Class, report_irt_investment.Category, report_irt_investment.`Year`, sys_class_investment.IRT_Structure";

		// System.out.println(sql_select);

		Connection conn = DBUtil.getConnection();

		// 删除 report_irt_basic_info_investment内容
		conn.prepareStatement(sql_del).executeUpdate();

		Statement stmt = conn.createStatement();
		prep = conn.prepareStatement(sql_save);

		conn.setAutoCommit(false);

		JSONArray jsonArray = new JSONArray();

		ResultSet rs = stmt.executeQuery(sql_select);
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int n = 0; rs.next(); n++) {
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnLabel(i);
				String value = rs.getString(columnName);
				jsonObj.put(columnName, value);
			}
			jsonArray.add(n, jsonObj);
		}
		
		for (int n = 0; n < jsonArray.size(); n++) {
			rows = jsonArray.getJSONObject(n);
			String Cocd = rows.getString("CoCd");
			String Year = rows.getString("Year");
			String Period = Posting_period;
			String IRT_Structure = rows.getString("IRT_Structure");
			String Asset = rows.getString("Asset");
			String SubNumber = rows.getString("SubNumber");
			String Asset_Class = rows.getString("Asset Class");
			double currectAcquisition = rows.getDouble("currectAcquisition");
			String Crcy = rows.getString("Crcy");
			String Class = rows.getString("Class");
			String Category = rows.getString("Category");
			String Description = rows.getString("Description");
			double currectDirQuot = rows.getDouble("currectDirQuot");
			int RatioFrom = rows.getInt("RatioFrom");
			if (currectAcquisition != 0.0D) {
				prep.setString(1, Cocd);
				prep.setString(2, Year);
				prep.setString(3, Period);
				prep.setString(4, IRT_Structure);
				prep.setString(5, Asset);
				prep.setString(6, SubNumber);
				prep.setString(7, Asset_Class);
				prep.setDouble(8, currectAcquisition);
				prep.setString(9, Crcy);
				prep.setString(10, Class);
				prep.setString(11, Category);
				prep.setString(12, Description);
				prep.addBatch();

				prep.setString(1, Cocd);
				prep.setString(2, Year);
				prep.setString(3, Period);
				prep.setString(4, IRT_Structure);
				prep.setString(5, Asset);
				prep.setString(6, SubNumber);
				prep.setString(7, Asset_Class);
				prep.setDouble(8, currectAcquisition / currectDirQuot / RatioFrom);
				prep.setString(9, "EUR");
				prep.setString(10, Class);
				prep.setString(11, Category);
				prep.setString(12, Description);
				prep.addBatch();
			}
		}
		prep.executeBatch();
		rs.close();
		System.out.println("Investment update was finished");
		conn.commit();
		prep.close();
	}

	//计算各类账号的基础数据
	public static void basicPrepare(String Companies, String Fiscal_Year, String Posting_period) throws SQLException {
		basicCalculate(Companies, Fiscal_Year, Posting_period);

		JSONObject rows = null;
		PreparedStatement prep = null;

		JSONObject jsonPLObj = new JSONObject();
		JSONObject jsonBSObj = new JSONObject();
		JSONObject jsonBSdiffObj = new JSONObject();
		JSONObject jsonPLdiffObj = new JSONObject();

		String[] mainCurrency = { "CNY", "EUR" };

		String sql_save = " INSERT INTO report_irt_basic_info (`Company Code`,`Account Number`,`FS Item`, `Fiscal Year`,`Posting period`,`IRT Structure`,`BS Indicate`,`Asset Indicate`,`GL Description`,`Debit amount LC`,`Credit amount LC`,`Accumulated balance LC`,`Balance LC`,`Currency`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		String sql_del = "delete from report_irt_basic_info Where (`Company Code` = " + Companies
				+ ") AND `Fiscal Year` = " + Fiscal_Year + " AND `Posting period` = " + Posting_period;
		String sql_bsdiff = "Select * from basic_info_diff_bs Where (`Company Code` = " + Companies
				+ ") AND `Fiscal Year` = " + Fiscal_Year + " AND `Posting period` = " + Posting_period;
		String sql_pldiff = "Select * from basic_info_diff_pl Where (`Company Code` = " + Companies
				+ ") AND `Fiscal Year` = " + Fiscal_Year + " AND `Posting period` = " + Posting_period;

		String sql_selectPL = "Select * from basic_info where (`ExRType` = 'PDK' AND `BS Indicate` = '' AND `Fiscal Year` = "
				+ sqlcondition(Fiscal_Year) + "AND `Posting period` = " + sqlcondition(Posting_period)
				+ " AND (`Company Code` = " + Companies
				+ ")) OR (`ExRType` = 'PDK' AND `IRT Structure` = 'Profit/Loss' AND `Fiscal Year` = "
				+ sqlcondition(Fiscal_Year) + "AND `Posting period` = " + sqlcondition(Posting_period)
				+ " AND (`Company Code` = " + Companies + "))";
		String sql_selectBS = "Select * from basic_info where `ExRType` = 'SK' AND `BS Indicate` = 'X' AND `IRT Structure` <> 'Profit/Loss' AND `Fiscal Year` = "
				+ sqlcondition(Fiscal_Year) + "AND `Posting period` = " + sqlcondition(Posting_period)
				+ " AND (`Company Code` = " + Companies + ")";

		Connection conn = DBUtil.getConnection();

		//删除 report_irt_basic_info
		conn.prepareStatement(sql_del).executeUpdate();

		Statement stmt = conn.createStatement();
		prep = conn.prepareStatement(sql_save);

		conn.setAutoCommit(false);
		for (int j = 0; j < mainCurrency.length; j++) {
			JSONArray jsonPLArray = new JSONArray();
			JSONArray jsonBSArray = new JSONArray();
			JSONArray jsonBSdiffArray = new JSONArray();
			JSONArray jsonPLdiffArray = new JSONArray();

			ResultSet rs = stmt.executeQuery(sql_selectPL);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int n = 0; rs.next(); n++) {
				for (int i = 1; i <= columnCount; i++) {
					String columnName = metaData.getColumnLabel(i);
					String value = rs.getString(columnName);
					jsonPLObj.put(columnName, value);
				}
				jsonPLArray.add(n, jsonPLObj);
			}
			for (int n = 0; n < jsonPLArray.size(); n++) {
				rows = jsonPLArray.getJSONObject(n);
				String companyCode = rows.getString("Company Code");
				String accountNumber = rows.getString("Account Number");
				String fsItem = rows.getString("Financial Statement Item");
				String fiscalYear = rows.getString("Fiscal Year");
				String postingPeriod = rows.getString("Posting period");
				String irtStructure = rows.getString("IRT Structure");
				String bsIndicate = rows.getString("BS Indicate");
				String assetIndicate = rows.getString("Asset Indicate");
				String glDescription = rows.getString("GL Description");
				int ratioFrom = rows.getInt("RatioFrom");
				String fromCurrency = rows.getString("FromCurrency");
				double dirQuot = rows.getDouble("DirQuot");
				double debitAmountLC = rows.getDouble("Debit amount LC");
				double creditAmountLC = rows.getDouble("Credit amount LC");
				double accumulatedBalanceLC = rows.getDouble("Accumulated balance LC");
				double balanceLC = rows.getDouble("Balance LC");
				String currency = rows.getString("Currency");
				if (!currency.equals(mainCurrency[j])) {
					if (mainCurrency[j].equals(fromCurrency)) {
						debitAmountLC = debitAmountLC / dirQuot / ratioFrom;
						creditAmountLC = creditAmountLC / dirQuot / ratioFrom;
						accumulatedBalanceLC = accumulatedBalanceLC / dirQuot / ratioFrom;
						balanceLC = balanceLC / dirQuot / ratioFrom;
					}
				} else {
					prep.setString(1, companyCode);
					prep.setString(2, accountNumber);
					prep.setString(3, fsItem);
					prep.setString(4, fiscalYear);
					prep.setString(5, postingPeriod);
					prep.setString(6, irtStructure);
					prep.setString(7, bsIndicate);
					prep.setString(8, assetIndicate);
					prep.setString(9, glDescription);
					prep.setDouble(10, debitAmountLC);
					prep.setDouble(11, creditAmountLC);
					prep.setDouble(12, accumulatedBalanceLC);
					prep.setDouble(13, balanceLC);
					prep.setString(14, mainCurrency[j]);

					prep.addBatch();
				}
			}
			rs = stmt.executeQuery(sql_selectBS);
			metaData = rs.getMetaData();
			columnCount = metaData.getColumnCount();
			for (int n = 0; rs.next(); n++) {
				for (int i = 1; i <= columnCount; i++) {
					String columnName = metaData.getColumnLabel(i);
					String value = rs.getString(columnName);
					jsonBSObj.put(columnName, value);
				}
				jsonBSArray.add(n, jsonBSObj);
			}
			for (int n = 0; n < jsonBSArray.size(); n++) {
				rows = jsonBSArray.getJSONObject(n);
				String companyCode = rows.getString("Company Code");
				String accountNumber = rows.getString("Account Number");
				String fsItem = rows.getString("Financial Statement Item");
				String fiscalYear = rows.getString("Fiscal Year");
				String postingPeriod = rows.getString("Posting period");
				String irtStructure = rows.getString("IRT Structure");
				String bsIndicate = rows.getString("BS Indicate");
				String assetIndicate = rows.getString("Asset Indicate");
				String glDescription = rows.getString("GL Description");
				int ratioFrom = rows.getInt("RatioFrom");
				String fromCurrency = rows.getString("FromCurrency");
				double dirQuot = rows.getDouble("DirQuot");
				double debitAmountLC = rows.getDouble("Debit amount LC");
				double creditAmountLC = rows.getDouble("Credit amount LC");
				double accumulatedBalanceLC = rows.getDouble("Accumulated balance LC");
				double balanceLC = rows.getDouble("Balance LC");
				String currency = rows.getString("Currency");
				if (!currency.equals(mainCurrency[j])) {
					if (mainCurrency[j].equals(fromCurrency)) {
						debitAmountLC = debitAmountLC / dirQuot / ratioFrom;
						creditAmountLC = creditAmountLC / dirQuot / ratioFrom;
						accumulatedBalanceLC = accumulatedBalanceLC / dirQuot / ratioFrom;
						balanceLC = balanceLC / dirQuot / ratioFrom;
					}
				} else {
					prep.setString(1, companyCode);
					prep.setString(2, accountNumber);
					prep.setString(3, fsItem);
					prep.setString(4, fiscalYear);
					prep.setString(5, postingPeriod);
					prep.setString(6, irtStructure);
					prep.setString(7, bsIndicate);
					prep.setString(8, assetIndicate);
					prep.setString(9, glDescription);
					prep.setDouble(10, debitAmountLC);
					prep.setDouble(11, creditAmountLC);
					prep.setDouble(12, accumulatedBalanceLC);
					prep.setDouble(13, balanceLC);
					prep.setString(14, mainCurrency[j]);

					prep.addBatch();
				}
			}
			//计算balance sheet差异
			rs = stmt.executeQuery(sql_bsdiff);
			metaData = rs.getMetaData();
			columnCount = metaData.getColumnCount();
			for (int n = 0; rs.next(); n++) {
				for (int i = 1; i <= columnCount; i++) {
					String columnName = metaData.getColumnLabel(i);
					String value = rs.getString(columnName);
					jsonBSdiffObj.put(columnName, value);
				}
				jsonBSdiffArray.add(n, jsonBSdiffObj);
			}
			for (int n = 0; n < jsonBSdiffArray.size(); n++) {
				rows = jsonBSdiffArray.getJSONObject(n);
				String companyCode = rows.getString("Company Code");
				String fiscalYear = rows.getString("Fiscal Year");
				String postingPeriod = rows.getString("Posting period");
				int ratioFrom = rows.getInt("RatioFrom");
				String fromCurrency = rows.getString("FromCurrency");
				double sk = rows.getDouble("SK");
				double pdk = rows.getDouble("PDK");
				double debitAmountLC = rows.getDouble("SUM of Debit amount LC");
				double creditAmountLC = rows.getDouble("SUM of Credit amount LC");
				double accumulatedBalanceLC = rows.getDouble("SUM of Accumulated balance LC");
				double balanceLC = rows.getDouble("SUM of Balance LC");
				String currency = rows.getString("Currency");
				if (!currency.equals(mainCurrency[j])) {
					if (mainCurrency[j].equals(fromCurrency)) {
						debitAmountLC = (debitAmountLC / sk - debitAmountLC / pdk) / ratioFrom;
						creditAmountLC = (creditAmountLC / sk - creditAmountLC / pdk) / ratioFrom;
						accumulatedBalanceLC = (accumulatedBalanceLC / sk - accumulatedBalanceLC / pdk) / ratioFrom;
						balanceLC = (balanceLC / sk - balanceLC / pdk) / ratioFrom;

						prep.setString(1, companyCode);
						prep.setString(2, "DUMMY001");
						prep.setString(3, "IRT EXADJ");
						prep.setString(4, fiscalYear);
						prep.setString(5, postingPeriod);
						prep.setString(6, "Capital Reserve");
						prep.setString(7, "X");
						prep.setString(8, "");
						prep.setString(9, "Exchange Rate diff between SK and PDK in Profit/Loss");
						prep.setDouble(10, 0.0D);
						prep.setDouble(11, 0.0D);
						prep.setDouble(12, accumulatedBalanceLC);
						prep.setDouble(13, 0.0D);
						prep.setString(14, mainCurrency[j]);

						prep.addBatch();
						System.out.println("?????BSDIFF Profit/Loss????" + mainCurrency[j] + (n + 1) + "???");
					}
				}
			}
			//计算profit and loss差异
			rs = stmt.executeQuery(sql_pldiff);
			metaData = rs.getMetaData();
			columnCount = metaData.getColumnCount();
			for (int n = 0; rs.next(); n++) {
				for (int i = 1; i <= columnCount; i++) {
					String columnName = metaData.getColumnLabel(i);
					String value = rs.getString(columnName);
					jsonPLdiffObj.put(columnName, value);
				}
				jsonPLdiffArray.add(n, jsonPLdiffObj);
			}
			for (int n = 0; n < jsonPLdiffArray.size(); n++) {
				rows = jsonPLdiffArray.getJSONObject(n);
				String companyCode = rows.getString("Company Code");
				String fiscalYear = rows.getString("Fiscal Year");
				String postingPeriod = rows.getString("Posting period");
				int ratioFrom = rows.getInt("RatioFrom");
				String fromCurrency = rows.getString("FromCurrency");
				double sk = rows.getDouble("SK");
				double pdk = rows.getDouble("PDK");
				double debitAmountLC = rows.getDouble("SUM of Debit amount LC");
				double creditAmountLC = rows.getDouble("SUM of Credit amount LC");
				double accumulatedBalanceLC = rows.getDouble("SUM of Accumulated balance LC");
				double balanceLC = rows.getDouble("SUM of Balance LC");
				String currency = rows.getString("Currency");
				if (!currency.equals(mainCurrency[j])) {
					if (mainCurrency[j].equals(fromCurrency)) {
						debitAmountLC = (debitAmountLC / sk - debitAmountLC / pdk) / ratioFrom;
						creditAmountLC = (creditAmountLC / sk - creditAmountLC / pdk) / ratioFrom;
						accumulatedBalanceLC = (accumulatedBalanceLC / sk - accumulatedBalanceLC / pdk) / ratioFrom;
						balanceLC = (balanceLC / sk - balanceLC / pdk) / ratioFrom;

						prep.setString(1, companyCode);
						prep.setString(2, "DUMMY002");
						prep.setString(3, "IRT EXADJ");
						prep.setString(4, fiscalYear);
						prep.setString(5, postingPeriod);
						prep.setString(6, "Capital Reserve");
						prep.setString(7, "X");
						prep.setString(8, "");
						prep.setString(9, "Exchange Rate diff between SK and PDK in Profit/Loss");
						prep.setDouble(10, 0.0D);
						prep.setDouble(11, 0.0D);
						prep.setDouble(12, accumulatedBalanceLC);
						prep.setDouble(13, 0.0D);
						prep.setString(14, mainCurrency[j]);

						prep.addBatch();
						System.out.println("?????BSDIFF Income before Tax????" + mainCurrency[j] + (n + 1) + "???");
					}
				}
			}
			prep.executeBatch();
			rs.close();
		}
		System.out.println("basic data process was finished");
		conn.commit();

		prep.close();
		conn.close();
	}

	//balance sheet中profit and loss中的EURO调整
	public static void EuroAdjust(String Companies, String Fiscal_Year, String Posting_period) throws SQLException {
		JSONObject rows = null;
		PreparedStatement prep = null;

		String currentPeriod = Posting_period;
		//String lastPeriod = String.valueOf(Integer.parseInt(Posting_period) - 1);
		String sql_save = " INSERT INTO report_irt_basic_info (`Company Code`,`Account Number`,`FS Item`, `Fiscal Year`,`Posting period`,`IRT Structure`,`BS Indicate`,`Asset Indicate`,`GL Description`,`Debit amount LC`,`Credit amount LC`,`Accumulated balance LC`,`Balance LC`,`Currency`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

		String[] periods = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13" };

		String periodsCondition = "";
		for (int i = 0; i < Integer.parseInt(Posting_period); i++) {
			if (i < Integer.parseInt(Posting_period) - 1) {
				periodsCondition = periodsCondition + "'" + periods[i] + "' OR `Posting period` = ";
			} else {
				periodsCondition = periodsCondition + "'" + periods[i] + "'";
			}
		}
		String sql_select = "SELECT\r\nreport_irt_basic_info.`Company Code`,\r\nreport_irt_basic_info.`Fiscal Year`,\r\nSum(IF((report_irt_basic_info.`Posting period` = "
				+ sqlcondition(currentPeriod)
				+ "),report_irt_basic_info.`Accumulated balance LC`,0)) - Sum(report_irt_basic_info.`Balance LC`) - (Sum(IF((report_irt_basic_info.`Posting period` = '1'),report_irt_basic_info.`Accumulated balance LC`,0))-Sum(IF((report_irt_basic_info.`Posting period` = '1'),report_irt_basic_info.`Balance LC`,0))) AS `Balance Euro Adjust`,\r\nreport_irt_basic_info.Currency\r\nFROM\r\nreport_irt_basic_info\r\nWHERE\r\n(report_irt_basic_info.`BS Indicate` = '' OR\r\nreport_irt_basic_info.`IRT Structure` = 'Profit/Loss') AND\r\n (report_irt_basic_info.`Fiscal Year` = "
				+ Fiscal_Year + ") AND\r\n (report_irt_basic_info.`Company Code` = " + Companies
				+ ") AND\r\n(report_irt_basic_info.`Posting period` = " + periodsCondition
				+ ") AND\r\nreport_irt_basic_info.Currency = 'EUR' AND\r\nreport_irt_basic_info.`Account Number` <> 'DUMMY003'\r\nGROUP BY\r\nreport_irt_basic_info.`Company Code`,\r\nreport_irt_basic_info.`Fiscal Year`";

		//System.out.println(sql_select);

		Connection conn = DBUtil.getConnection();

		Statement stmt = conn.createStatement();
		prep = conn.prepareStatement(sql_save);

		conn.setAutoCommit(false);

		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		ResultSet rs = stmt.executeQuery(sql_select);
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int n = 0; rs.next(); n++) {
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnLabel(i);
				String value = rs.getString(columnName);
				jsonObj.put(columnName, value);
			}
			jsonArray.add(n, jsonObj);
		}
		for (int n = 0; n < jsonArray.size(); n++) {
			rows = jsonArray.getJSONObject(n);
			String companyCode = rows.getString("Company Code");
			String fiscalYear = rows.getString("Fiscal Year");
			String postingPeriod = Posting_period;
			double balanceLC = rows.getDouble("Balance Euro Adjust");
			//String currency = rows.getString("Currency");

			prep.setString(1, companyCode);
			prep.setString(2, "DUMMY003");
			prep.setString(3, "IRT EXADJ");
			prep.setString(4, fiscalYear);
			prep.setString(5, postingPeriod);
			prep.setString(6, "Capital Reserve");
			prep.setString(7, "X");
			prep.setString(8, "");
			prep.setString(9, "Exchange Rate diff between SK and PDK in Profit/Loss");
			prep.setDouble(10, 0.0D);
			prep.setDouble(11, 0.0D);
			prep.setDouble(12, balanceLC);
			prep.setDouble(13, 0.0D);
			prep.setString(14, "EUR");

			prep.addBatch();

			prep.setString(1, companyCode);
			prep.setString(2, "DUMMY003");
			prep.setString(3, "IRT EXADJ");
			prep.setString(4, fiscalYear);
			prep.setString(5, postingPeriod);
			prep.setString(6, "Profit/Loss");
			prep.setString(7, "X");
			prep.setString(8, "");
			prep.setString(9, "Exchange Rate diff between SK and PDK in Profit/Loss");
			prep.setDouble(10, 0.0D);
			prep.setDouble(11, 0.0D);
			prep.setDouble(12, -balanceLC);
			prep.setDouble(13, 0.0D);
			prep.setString(14, "EUR");

			prep.addBatch();
		}
		prep.executeBatch();
		rs.close();
		System.out.println("EURO for profit in balance was finished");
		conn.commit();
		prep.close();
		conn.close();
	}

	//计算中间表，通过累计余额相减得到发生额
	public static void basicCalculate(String Companies, String Fiscal_Year, String Posting_period) throws SQLException {
		JSONObject rows = null;
		PreparedStatement prep = null;

		String currentPeriod = Posting_period;
		String lastPeriod = String.valueOf(Integer.parseInt(Posting_period) - 1);

		String sql_save = " INSERT INTO basic_info (`Company Code`,`Account Number`,`Financial Statement Item`, `Fiscal Year`,`Posting period`,`IRT Structure`,`BS Indicate`,`Asset Indicate`,`GL Description`,`RatioFrom`,`FromCurrency`,`DirQuot`,`Debit amount LC`,`Credit amount LC`,`Accumulated balance LC`,`Balance LC`,`Currency`,`ExRType`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

		String sql_select = "";
		//如果是period 1不用特别计算
		if (Posting_period.equals("1")) {
			sql_select = "SELECT\r\n\t`report_irt_structure`.`Company Code` AS `Company Code`,\r\n\t`report_irt_structure`.`Account Number` AS `Account Number`,\r\n\t`report_irt_structure`.`Financial Statement Item` AS `Financial Statement Item`,\r\n\t`report_irt_structure`.`Fiscal Year` AS `Fiscal Year`,\r\n\t`report_irt_structure`.`Posting period` AS `Posting period`,\r\n\t`sys_gl_masterdata`.`IRT Structure` AS `IRT Structure`,\r\n\t`sys_gl_masterdata`.`BS Indicate` AS `BS Indicate`,\r\n\t`sys_gl_masterdata`.`Asset Indicate` AS `Asset Indicate`,\r\n\t`sys_gl_masterdata`.`GL Description` AS `GL Description`,\r\n\t`sys_exchage_rate`.`RatioFrom` AS `RatioFrom`,\r\n\t`sys_exchage_rate`.`FromCurrency` AS `FromCurrency`,\r\n\t`sys_exchage_rate`.`DirQuot` AS `DirQuot`,\r\n\t`report_irt_gl_balance`.`      Debit rept.period` AS `Debit amount LC`,\r\n\t`report_irt_gl_balance`.`     Credit report per.` AS `Credit amount LC`,\r\n\t`report_irt_gl_balance`.`    Accumulated balance` AS `Accumulated balance LC`,\r\n\t(\r\n\t\t`report_irt_gl_balance`.`      Debit rept.period` - `report_irt_gl_balance`.`     Credit report per.`\r\n\t) AS `Balance LC`,\r\n\t`report_irt_structure`.`Currency` AS `Currency`,\r\n\t`sys_exchage_rate`.`ExRType` AS `ExRType`\r\nFROM\r\n\t(\r\n\t\t(\r\n\t\t\t(\r\n\t\t\t\t`report_irt_structure`\r\n\t\t\t\tJOIN `sys_gl_masterdata` ON (\r\n\t\t\t\t\t(\r\n\t\t\t\t\t\t(\r\n\t\t\t\t\t\t\t`sys_gl_masterdata`.`G/L Account` = `report_irt_structure`.`Account Number`\r\n\t\t\t\t\t\t)\r\n\t\t\t\t\t\tAND (\r\n\t\t\t\t\t\t\t`sys_gl_masterdata`.`FS Item` = `report_irt_structure`.`Financial Statement Item`\r\n\t\t\t\t\t\t)\r\n\t\t\t\t\t)\r\n\t\t\t\t)\r\n\t\t\t)\r\n\t\t\tJOIN `sys_exchage_rate` ON (\r\n\t\t\t\t(\r\n\t\t\t\t\t(\r\n\t\t\t\t\t\t`sys_exchage_rate`.`Year` = `report_irt_structure`.`Fiscal Year`\r\n\t\t\t\t\t)\r\n\t\t\t\t\tAND (\r\n\t\t\t\t\t\t`sys_exchage_rate`.`Period` = `report_irt_structure`.`Posting period`\r\n\t\t\t\t\t)\r\n\t\t\t\t\tAND (\r\n\t\t\t\t\t\t`sys_exchage_rate`.`ToCurrency` = `report_irt_structure`.`Currency`\r\n\t\t\t\t\t)\r\n\t\t\t\t)\r\n\t\t\t)\r\n\t\t)\r\n\t\tJOIN `report_irt_gl_balance` ON (\r\n\t\t\t(\r\n\t\t\t\t(\r\n\t\t\t\t\t`report_irt_gl_balance`.`CoCd` = `report_irt_structure`.`Company Code`\r\n\t\t\t\t)\r\n\t\t\t\tAND (\r\n\t\t\t\t\t`report_irt_gl_balance`.`G/L acct` = `report_irt_structure`.`Account Number`\r\n\t\t\t\t)\r\n\t\t\t\tAND (\r\n\t\t\t\t\t`report_irt_gl_balance`.`Crcy` = `report_irt_structure`.`Currency`\r\n\t\t\t\t)\r\n\t\t\t\tAND (\r\n\t\t\t\t\t`report_irt_gl_balance`.`Year` = `report_irt_structure`.`Fiscal Year`\r\n\t\t\t\t)\r\n\t\t\t\tAND (\r\n\t\t\t\t\t`report_irt_gl_balance`.`Period` = `report_irt_structure`.`Posting period`\r\n\t\t\t\t)\r\n\t\t\t)\r\n\t\t)\r\n\t)\r\nWHERE\r\n(sys_gl_masterdata.`IRT Structure` <> 'KOCHI_DUMM') AND\r\n(report_irt_structure.`Company Code` = "
					+ Companies + ") AND\r\nreport_irt_structure.`Fiscal Year` = " + Fiscal_Year
					+ " AND\r\nreport_irt_structure.`Posting period` = " + sqlcondition(currentPeriod);
		} else {
			sql_select = "SELECT\r\n\treport_irt_structure.`Company Code` AS `Company Code`,\r\n\treport_irt_structure.`Account Number` AS `Account Number`,\r\n\tMax(IF((report_irt_structure.`Posting period` = "
					+ sqlcondition(currentPeriod)
					+ "),report_irt_structure.`Financial Statement Item`,'')) AS `Financial Statement Item`,\r\n\treport_irt_structure.`Fiscal Year`,\r\n\tMax(IF((report_irt_structure.`Posting period` = "
					+ sqlcondition(currentPeriod)
					+ "),sys_gl_masterdata.`IRT Structure`,'')) AS `IRT Structure`,\r\n\tMax(IF((report_irt_structure.`Posting period` = "
					+ sqlcondition(currentPeriod)
					+ "),sys_gl_masterdata.`BS Indicate`,'')) AS `BS Indicate`,\r\n\tMax(IF((report_irt_structure.`Posting period` = "
					+ sqlcondition(currentPeriod)
					+ "),sys_gl_masterdata.`Asset Indicate`,'')) AS `Asset Indicate`,\r\n\tsys_gl_masterdata.`GL Description` AS `GL Description`,\r\n\tsys_exchage_rate.RatioFrom AS RatioFrom,\r\n\tsys_exchage_rate.FromCurrency AS FromCurrency,\r\n\tSUM(IF((report_irt_structure.`Posting period` = "
					+ sqlcondition(currentPeriod)
					+ "),sys_exchage_rate.DirQuot,0)) AS DirQuot,\r\n\tSUM(IF((report_irt_structure.`Posting period` = "
					+ sqlcondition(currentPeriod)
					+ "),report_irt_gl_balance.`      Debit rept.period`,0)) AS `Debit amount LC`,\r\n\tSUM(IF((report_irt_structure.`Posting period` = "
					+ sqlcondition(currentPeriod)
					+ "),report_irt_gl_balance.`     Credit report per.`,0)) AS `Credit amount LC`,\r\n\tSUM(IF((report_irt_structure.`Posting period` = "
					+ sqlcondition(currentPeriod)
					+ "),report_irt_gl_balance.`    Accumulated balance`,0)) AS `Accumulated balance LC`,\r\n\tsum(\r\n\r\n\t\tIF (\r\n\t\t\t(\r\n\t\t\t\treport_irt_structure.`Posting period` = "
					+ sqlcondition(currentPeriod)
					+ "\t\t\t),\r\n\t\t\t`report_irt_gl_balance`.`    Accumulated balance`,\r\n\t\t\t0\r\n\t\t) -\r\n\t\tIF (\r\n\t\t\t(\r\n\t\t\t\treport_irt_structure.`Posting period` = "
					+ sqlcondition(lastPeriod)
					+ "\t\t\t),\r\n\t\t\t`report_irt_gl_balance`.`    Accumulated balance`,\r\n\t\t\t0\r\n\t\t)\r\n\t) AS `Balance LC`,\r\n\treport_irt_structure.Currency AS Currency,\r\n\tsys_exchage_rate.ExRType AS ExRType\r\nFROM\r\n\t(\r\n\t\t(\r\n\t\t\t(\r\n\t\t\t\t`report_irt_structure`\r\n\t\t\t\tJOIN `sys_gl_masterdata` ON (\r\n\t\t\t\t\t(\r\n\t\t\t\t\t\t(\r\n\t\t\t\t\t\t\t`sys_gl_masterdata`.`G/L Account` = `report_irt_structure`.`Account Number`\r\n\t\t\t\t\t\t)\r\n\t\t\t\t\t\tAND (\r\n\t\t\t\t\t\t\t`sys_gl_masterdata`.`FS Item` = `report_irt_structure`.`Financial Statement Item`\r\n\t\t\t\t\t\t)\r\n\t\t\t\t\t)\r\n\t\t\t\t)\r\n\t\t\t)\r\n\t\t\tJOIN `sys_exchage_rate` ON (\r\n\t\t\t\t(\r\n\t\t\t\t\t(\r\n\t\t\t\t\t\t`sys_exchage_rate`.`Year` = `report_irt_structure`.`Fiscal Year`\r\n\t\t\t\t\t)\r\n\t\t\t\t\tAND (\r\n\t\t\t\t\t\t`sys_exchage_rate`.`Period` = `report_irt_structure`.`Posting period`\r\n\t\t\t\t\t)\r\n\t\t\t\t\tAND (\r\n\t\t\t\t\t\t`sys_exchage_rate`.`ToCurrency` = `report_irt_structure`.`Currency`\r\n\t\t\t\t\t)\r\n\t\t\t\t)\r\n\t\t\t)\r\n\t\t)\r\n\t\tJOIN `report_irt_gl_balance` ON (\r\n\t\t\t(\r\n\t\t\t\t(\r\n\t\t\t\t\t`report_irt_gl_balance`.`CoCd` = `report_irt_structure`.`Company Code`\r\n\t\t\t\t)\r\n\t\t\t\tAND (\r\n\t\t\t\t\t`report_irt_gl_balance`.`G/L acct` = `report_irt_structure`.`Account Number`\r\n\t\t\t\t)\r\n\t\t\t\tAND (\r\n\t\t\t\t\t`report_irt_gl_balance`.`Crcy` = `report_irt_structure`.`Currency`\r\n\t\t\t\t)\r\n\t\t\t\tAND (\r\n\t\t\t\t\t`report_irt_gl_balance`.`Year` = `report_irt_structure`.`Fiscal Year`\r\n\t\t\t\t)\r\n\t\t\t\tAND (\r\n\t\t\t\t\t`report_irt_gl_balance`.`Period` = `report_irt_structure`.`Posting period`\r\n\t\t\t\t)\r\n\t\t\t)\r\n\t\t)\r\n\t)\r\nWHERE\r\n\t(\r\n\t\tsys_gl_masterdata.`IRT Structure` <> 'KOCHI_DUMM'\r\n\t)\r\nAND (\r\n\treport_irt_structure.`Posting period` = "
					+ sqlcondition(currentPeriod) + "\tOR report_irt_structure.`Posting period` = "
					+ sqlcondition(lastPeriod) + ")\r\nAND (report_irt_structure.`Company Code` = " + Companies
					+ ")AND `Fiscal Year` = " + Fiscal_Year
					+ " GROUP BY\r\n\treport_irt_structure.`Fiscal Year`,\r\n\treport_irt_structure.`Account Number`,\r\n\treport_irt_structure.`Company Code`,\r\n\treport_irt_structure.Currency,\r\n\tsys_exchage_rate.ExRType";
		}
		String sql_del = "delete from basic_info Where (`Company Code` = " + Companies + ") AND `Fiscal Year` = "
				+ Fiscal_Year + " AND `Posting period` = " + Posting_period;

		//System.out.println(sql_select);

		Connection conn = DBUtil.getConnection();

		//删除basic_info
		conn.prepareStatement(sql_del).executeUpdate();

		Statement stmt = conn.createStatement();
		prep = conn.prepareStatement(sql_save);

		conn.setAutoCommit(false);

		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		ResultSet rs = stmt.executeQuery(sql_select);
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int n = 0; rs.next(); n++) {
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnLabel(i);
				String value = rs.getString(columnName);
				jsonObj.put(columnName, value);
			}
			jsonArray.add(n, jsonObj);
		}
		for (int n = 0; n < jsonArray.size(); n++) {
			rows = jsonArray.getJSONObject(n);
			String companyCode = rows.getString("Company Code");
			String accountNumber = rows.getString("Account Number");
			String fsItem = rows.getString("Financial Statement Item");
			String fiscalYear = rows.getString("Fiscal Year");
			String postingPeriod = Posting_period;
			String irtStructure = rows.getString("IRT Structure");
			String bsIndicate = rows.getString("BS Indicate");
			String assetIndicate = rows.getString("Asset Indicate");
			String glDescription = rows.getString("GL Description");
			int ratioFrom = rows.getInt("RatioFrom");
			String fromCurrency = rows.getString("FromCurrency");
			double dirQuot = rows.getDouble("DirQuot");
			double debitAmountLC = rows.getDouble("Debit amount LC");
			double creditAmountLC = rows.getDouble("Credit amount LC");
			double accumulatedBalanceLC = rows.getDouble("Accumulated balance LC");
			double balanceLC = rows.getDouble("Balance LC");
			String currency = rows.getString("Currency");
			String exrType = rows.getString("ExRType");
			if (dirQuot != 0.0D) {
				prep.setString(1, companyCode);
				prep.setString(2, accountNumber);
				prep.setString(3, fsItem);
				prep.setString(4, fiscalYear);
				prep.setString(5, postingPeriod);
				prep.setString(6, irtStructure);
				prep.setString(7, bsIndicate);
				prep.setString(8, assetIndicate);
				prep.setString(9, glDescription);
				prep.setInt(10, ratioFrom);
				prep.setString(11, fromCurrency);
				prep.setDouble(12, dirQuot);
				prep.setDouble(13, debitAmountLC);
				prep.setDouble(14, creditAmountLC);
				prep.setDouble(15, accumulatedBalanceLC);
				prep.setDouble(16, balanceLC);
				prep.setString(17, currency);
				prep.setString(18, exrType);

				prep.addBatch();
			}
		}
		prep.executeBatch();
		rs.close();
		System.out.println("Account balance calculate was finished");
		conn.commit();
		prep.close();
		conn.close();
	}

	public static String sqlcondition(String str) {
		str = "'" + str + "'";
		return str;
	}
}