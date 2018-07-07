package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jspsmart.upload.SmartUpload;
import java.util.Properties;
import koasiaco.DBUtil;
import java.io.InputStream;
import java.sql.SQLException;

import koasiaco.main;

/**
 * Servlet implementation class SmartDownloadServlet
 * 前台下载文件，只支持excel下载，根据选择的全部数据库表名导出
 */
@WebServlet("/SmartDownloadServlet.do")
public class SmartDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static String exportFileName;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SmartDownloadServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 通过配置文件设置导出文件保存路径
		String result = "Download OK";
		ClassLoader classLoader = DBUtil.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream("config/props/db.properties");
		Properties props = new Properties();
		props.load(is);
		exportFileName = props.getProperty("exportFileName");

		SmartUpload su = new SmartUpload();

		su.initialize(getServletConfig(), request, response);

		su.setContentDisposition(null);

		// 前台form提交过来的表名
		String dbName = request.getParameter("ExpSelection");

		System.out.println("Download dbName:" + dbName);

		try {
			main.excelDownload(dbName);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			su.downloadFile(exportFileName);
		} catch (Exception e) {
			result = "Download failed";
			System.out.println("********"+result+"********");
			e.printStackTrace();
		}
	}

}
