package servlet;

import com.jspsmart.upload.SmartUpload;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import koasiaco.DBUtil;
import koasiaco.SmartExportExcel;

/**
 * download to IRT excel的实现
 */

public class AjaxExportIRTServelt extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static String exportFileName;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//根据配置获取导出路径
		ClassLoader classLoader = DBUtil.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream("config/props/db.properties");
		Properties props = new Properties();
		props.load(is);
		exportFileName = props.getProperty("exportFileName");

		SmartUpload su = new SmartUpload();
		su.initialize(getServletConfig(), request, response);
		su.setContentDisposition(null);
		//获取前台提交的内容
		String Companies = request.getParameter("Company");
		String year = request.getParameter("Year");
		String period = request.getParameter("Period");
		String currency = request.getParameter("Currency");

		System.out.println("Start download： Companies:" + Companies + " Year:" + year + " Period:" + period + " Currency:" + currency);

		try {
			SmartExportExcel.reportStart(year, Companies, currency, period);
			System.out.println("********Export Success********");
		} catch (SQLException e1) {
			System.out.println("********Export with Errors********");
			e1.printStackTrace();
		}
		try {
			su.downloadFile(exportFileName);
			System.out.println("********Download Success********");
		} catch (Exception e) {
			System.out.println("********Download with Errors********");
			e.printStackTrace();
		}
	}
}