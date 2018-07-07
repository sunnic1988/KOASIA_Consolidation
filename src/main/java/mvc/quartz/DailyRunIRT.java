package mvc.quartz;

import files.GetFileList;
import files.UploadSAP;
import files.WindowsProcess;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import koasiaco.DBUtil;
import koasiaco.DeleteData;
import net.sf.json.JSONObject;
import reports.basicInfo;

public class DailyRunIRT {
	static String SAP_DownloadDefaultFolder;

	//定时任务，参考AjaxCreateDataServelt
	public void run() throws IOException, SQLException {
		
		long startTime = System.currentTimeMillis();
		ClassLoader classLoader = DBUtil.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream("config/props/db.properties");
		Properties props = new Properties();
		props.load(is);
		SAP_DownloadDefaultFolder = props.getProperty("SAP_DownloadDefaultFolder");
		GetFileList.delAllFile(SAP_DownloadDefaultFolder);

		String Companies = "";
		String year = "";
		String period = "";
		String Companies_report_irt_structure = "";
		String Companies_report_irt_gl_balance = "";
		String Companies_report_irt_plstructure = "";
		JSONObject jsonObj = new JSONObject();
		try {
			Connection conn = DBUtil.getConnection();
			Statement stmt = conn.createStatement();
			String sql = "Select * from sys_configuration_to_python";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				jsonObj.put(rs.getString(1), rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Companies = jsonObj.getString("Companies");
		year = jsonObj.getString("Report_Year");
		period = jsonObj.getString("Report_Month");
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

		
		String sql_del_report_irt_structure = "delete from report_irt_structure Where (`Company Code` = "
				+ Companies_report_irt_structure + ") AND `Fiscal Year` = " + year + " AND `Posting period` = "
				+ period;

		String sql_del_report_irt_gl_balance = "delete from report_irt_gl_balance Where (`CoCd` = "
				+ Companies_report_irt_gl_balance + ") AND `Year` = " + year + " AND `Period` = " + period;

		String sql_del_report_irt_plstructure = "delete from report_irt_plstructure Where (`CoCd` = "
				+ Companies_report_irt_plstructure + ") AND `Year` = " + year + " AND `Period` = " + period;
		try {
			DeleteData.normalStartl(sql_del_report_irt_structure);
			DeleteData.normalStartl(sql_del_report_irt_gl_balance);
			DeleteData.normalStartl(sql_del_report_irt_plstructure);
		} catch (SQLException e1) {
			System.out.println("del with errors");
		}
		
		String taskPatch = "D:\\PycharmProjects\\SAPexport\\venv\\Scripts\\dist\\ProcessRunning.exe";
		WindowsProcess.killTask(taskPatch);
		WindowsProcess.startProgram(taskPatch);

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
		UploadSAP upload = new UploadSAP();
		upload.SAPUploadtoMysql();

		basicInfo.basicPrepare(Companies_report_irt_structure, year, period);
		basicInfo.basic_plstucture(Companies_report_irt_plstructure, year, period);
		long endTime = System.currentTimeMillis();
		System.out.println("Task need : " + (endTime - startTime) / 1000L + "S");
	}
}