package servlet;

import files.GetFileList;
import files.UploadSAP;
import files.WindowsProcess;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import koasiaco.DBUtil;
import koasiaco.DeleteData;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import reports.basicInfo;

/**
 * 结合python，对mysql数据进行更新sap数据
 */

public class AjaxCreateDataServelt extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static String SAP_DownloadDefaultFolder;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//获取任务启动时间
		long startTime = System.currentTimeMillis();

		
		ClassLoader classLoader = DBUtil.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream("config/props/db.properties");
		Properties props = new Properties();
		props.load(is);
		SAP_DownloadDefaultFolder = props.getProperty("SAP_DownloadDefaultFolder");

		//根据配置文件，获取路径，并删除此文件中所有之前导出的数据
		GetFileList.delAllFile(SAP_DownloadDefaultFolder);

		//获取基本条件
		String Companies = request.getParameter("Company");
		String year = request.getParameter("Year");
		String period = request.getParameter("Period");
		String method = request.getParameter("Method");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();

		//因为company code 是文本，采用 or 这种查询方式，因为mysql中有两种表示公司代码的方法，1：company code 2：Cocd
		String Companies_report_irt_structure = "";
		String Companies_report_irt_gl_balance = "";
		String Companies_report_irt_plstructure = "";
		
		if (Companies.contains(",")) {
			Companies_report_irt_structure = Companies.replace(",", " OR `Company Code` = ");
		} else {
			Companies_report_irt_structure = Companies;
		}
		if (Companies.contains(",")) {
			Companies_report_irt_gl_balance = Companies.replace(",", " OR `CoCd` = ");
		} else {
			Companies_report_irt_gl_balance = Companies;
		}
		Companies_report_irt_plstructure = Companies_report_irt_gl_balance;
		
		
		try {
			//判断期间是否Open
			if (checkPeriod(year, period)) {
				String step = "";
				//得到python 自动程序运行的step
				try {
					Connection conn = DBUtil.getConnection();
					Statement stmt = conn.createStatement();
					String sqlCheckStep = "SELECT sys_configuration_to_python.Configuration FROM sys_configuration_to_python WHERE sys_configuration_to_python.Item = 'AutoProcessStep'";
					ResultSet rs = stmt.executeQuery(sqlCheckStep);
					while (rs.next()) {
						step = rs.getString(1);
					}
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				//如果step 是starting，则跳出程序，因为python程序不能同时执行
				if (step.equals("Starting")) {
					JSONObject outData = new JSONObject();
					outData.put("ajaxResult", "Starting");
					writer.println(outData);
					writer.flush();
					writer.close();
				} else {
					//判断前台参数，是否是Redownload，意思是否需要重新从SAP导出
					if (method.equals("ReDownload")) {
						try {
							Connection conn = DBUtil.getConnection();
							Statement stmt = conn.createStatement();
							//把前台传进的基本参数，写入mysql，供python程序读取
							String sql = "UPDATE sys_configuration_to_python set Configuration = '" + Companies
									+ "' where Item = 'Companies'";

							stmt.execute(sql);
							sql = "UPDATE sys_configuration_to_python set Configuration = '" + year
									+ "' where Item = 'Report_Year'";

							stmt.execute(sql);
							sql = "UPDATE sys_configuration_to_python set Configuration = '" + period
									+ "' where Item = 'Report_Month'";

							stmt.execute(sql);
							stmt.close();
							conn.close();
						} catch (SQLException e) {
							JSONObject outData = new JSONObject();
							outData.put("ajaxResult", "Connection to Mysql with Errors");
							writer.println(outData);
							writer.flush();
							writer.close();
						}
						
						
						//因为是Redownload，所以需要删除四个数据源的基础数据，根据company code，year，period
						String sql_del_report_irt_structure = "delete from report_irt_structure Where (`Company Code` = "
								+ Companies_report_irt_structure + ") AND `Fiscal Year` = " + year
								+ " AND `Posting period` = " + period;

						String sql_del_report_irt_gl_balance = "delete from report_irt_gl_balance Where (`CoCd` = "
								+ Companies_report_irt_gl_balance + ") AND `Year` = " + year + " AND `Period` = "
								+ period;

						String sql_del_report_irt_plstructure = "delete from report_irt_plstructure Where (`CoCd` = "
								+ Companies_report_irt_plstructure + ") AND `Year` = " + year + " AND `Period` = "
								+ period;

						String sql_del_report_irt_investment = "delete from report_irt_investment Where (`CoCd` = "
								+ Companies_report_irt_plstructure + ") AND `Year` = " + year + " AND `Period` = "
								+ period;
						try {
							DeleteData.normalStartl(sql_del_report_irt_structure);
							DeleteData.normalStartl(sql_del_report_irt_gl_balance);
							DeleteData.normalStartl(sql_del_report_irt_plstructure);
							DeleteData.normalStartl(sql_del_report_irt_investment);
						} catch (SQLException e1) {
							JSONObject outData = new JSONObject();
							outData.put("ajaxResult", "Delete Old Data with Errors");
							writer.println(outData);
							writer.flush();
							writer.close();
						}
						
						
						//准备启动python程序，启动前，先删除一下进程同样的进程，担心之前进程会卡死
						String taskPatch = "D:\\PycharmProjects\\SAPexport\\venv\\Scripts\\dist\\ProcessRunning.exe";
						WindowsProcess.killTask(taskPatch);
						WindowsProcess.startProgram(taskPatch);

						//python程序启动20秒后，监控python是否在运行，如果不在运行了，认为python程序结束，程序继续运行
						int SleepSecond = 1000;
						try {
							Thread.sleep(SleepSecond * 20);
							System.out.println("delay 20");
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						int n = 0;
						while (WindowsProcess.checkTaskList("ProcessRunning.exe")) {
							n += 1;
							try {
								Thread.sleep(SleepSecond * 10);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							System.out.println("checkTaskList:" + n + " Times");
						}
						
						
						//再次获取python传mysql的step
						try {
							Connection conn = DBUtil.getConnection();
							Statement stmt = conn.createStatement();
							String sqlCheckStep = "SELECT sys_configuration_to_python.Configuration FROM sys_configuration_to_python WHERE sys_configuration_to_python.Item = 'AutoProcessStep'";
							ResultSet rs = stmt.executeQuery(sqlCheckStep);
							while (rs.next()) {
								step = rs.getString(1);
							}
						} catch (SQLException e2) {
							e2.printStackTrace();
						}
						
						//判断step是否是完成
						if (step.equals("Finished")) {
							UploadSAP upload = new UploadSAP();
							//上传所有基础文件到mysql中（4张基础表）
							upload.SAPUploadtoMysql();
							try {
								//通过计算表计算结果
								basicInfo.basicPrepare(Companies_report_irt_structure, year, period);
								basicInfo.basic_plstucture(Companies_report_irt_plstructure, year, period);
								basicInfo.basic_plstuctureAdj(Companies_report_irt_plstructure, year, period);
								basicInfo.EuroAdjust(Companies_report_irt_structure, year, period);
								basicInfo.basic_investment(Companies_report_irt_plstructure, year, period);
								basicInfo.basic_downpayment_dep(Companies_report_irt_structure, year, period);
								JSONObject outData = new JSONObject();
								outData.put("ajaxResult", "Updated Success");
								writer.println(outData);
								writer.flush();
								writer.close();
							} catch (SQLException e) {
								JSONObject outData = new JSONObject();
								outData.put("ajaxResult", "Updated with Errors");
								writer.println(outData);
								writer.flush();
								writer.close();
								e.printStackTrace();
							}
							
						} else {
							JSONObject outData = new JSONObject();
							outData.put("ajaxResult", "Updated with Errors");
							writer.println(outData);
							writer.flush();
							writer.close();
						}
						
					//如果前台参数不是Redownload，则直接重新计算
					} else {
						try {
							basicInfo.basicPrepare(Companies_report_irt_structure, year, period);
							basicInfo.basic_plstucture(Companies_report_irt_plstructure, year, period);
							basicInfo.basic_plstuctureAdj(Companies_report_irt_plstructure, year, period);
							basicInfo.EuroAdjust(Companies_report_irt_structure, year, period);
							basicInfo.basic_investment(Companies_report_irt_plstructure, year, period);
							basicInfo.basic_downpayment_dep(Companies_report_irt_structure, year, period);
							JSONObject outData = new JSONObject();
							outData.put("ajaxResult", "Updated Success");
							writer.println(outData);
							writer.flush();
							writer.close();
						} catch (SQLException e) {
							JSONObject outData = new JSONObject();
							outData.put("ajaxResult", "Updated with Errors");
							writer.println(outData);
							writer.flush();
							writer.close();
							e.printStackTrace();
						}
					}
					
					//获取任务结束时间
					long endTime = System.currentTimeMillis();
					System.out.println("Data updated need : " + (endTime - startTime) / 1000L + "S");
				}
			} else {
				JSONObject outData = new JSONObject();
				outData.put("ajaxResult", "Period was block");
				writer.println(outData);
				writer.flush();
				writer.close();
			}
		} catch (SQLException e3) {
			JSONObject outData = new JSONObject();
			outData.put("ajaxResult", "Updated with Errors");
			writer.println(outData);
			writer.flush();
			writer.close();
			e3.printStackTrace();
		}
	}

	//判断期间是否open
	public boolean checkPeriod(String year, String period) throws SQLException {
		JSONObject jsonObj = new JSONObject();
		JSONObject rows = null;
		Connection conn = DBUtil.getConnection();
		Statement stmt = conn.createStatement();
		String sqlPeriodCheck = "SELECT *FROM sys_period";

		JSONArray jsonArray = new JSONArray();
		
		//查询出所有期间表中的内容	
		ResultSet rs = stmt.executeQuery(sqlPeriodCheck);
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
		
		//和前台提交的期间做比较
		for (int n = 0; n < jsonArray.size(); n++) {
			rows = jsonArray.getJSONObject(n);
			String PeriodCheck = rows.getString("Period");
			String StateCheck = rows.getString("OpenClose");
			if (StateCheck.equals("Open")) {
				String MonthCheck = PeriodCheck.substring(4, 6);
				System.out.println("MonthCheck" + MonthCheck);
				String YearCheck = PeriodCheck.substring(0, 4);
				System.out.println("YearCheck" + YearCheck);
				if ((Integer.parseInt(MonthCheck) == Integer.parseInt(period))
						&& (Integer.parseInt(YearCheck) == Integer.parseInt(year))) {
					return true;
				}
			}
		}
		return false;
	}
}