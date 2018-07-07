package servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import koasiaco.DBUtil;
import koasiaco.main;
import com.jspsmart.upload.SmartUpload;
import java.util.Properties;

/**
 * Servlet implementation class SmartUploadServlet
 * 前台上传文件，只支持excel上传，sheet名对应mysql表名，字段名和mysql列名一致
 */
@WebServlet("/SmartUploadServlet.do")
public class SmartUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static String filePath;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SmartUploadServlet() {
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
		
		// 通过配置文件设置上传文件保存路径
		ClassLoader classLoader = DBUtil.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream("config/props/db.properties");
		Properties props = new Properties();
		props.load(is);
		filePath = props.getProperty("filePath");
		// 路径内文件可删除
		File file = new File(filePath);

		// 创建文件夹
		if (!file.exists()) {
			file.mkdir();
		}

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();

		String result = "<h1 style =\"text-align: center\">Upload Succeful!</h1>";
		String uploadPath = "<p style=\"text-align: center\"><a href=\"Source/Pages/public/uploadfile.jsp\">Go Back!</a></p>";

		try {
			SmartUpload su = new SmartUpload();
			// 初始化对象
			su.initialize(this.getServletConfig(), request, response);
			// 设置文件大小
			su.setMaxFileSize(1024 * 1024 * 100);
			// 设置上传所有文件大小
			su.setTotalMaxFileSize(1024 * 1024 * 100);
			// 设置可上传文件的类型
			su.setAllowedFilesList("xls,xlsx");
			// 设置可上传文件的类型

			// 上传文件
			su.upload();

			// 获取JSP上的表单信息必须在upload()之后
			String dbName = su.getRequest().getParameter("isDB_Table");
			dbName = "";
			System.out.println("Get dbName from Customer:" + dbName);

			// 获取文件名
			String fileName = su.getFiles().getFile(0).getFileName();
			fileName = new String(fileName.getBytes("GBK"), "utf-8");

			// 上传文件计数
			int count = su.save(filePath);

			// 获得完整服务器导入mysql的路径及文件名
			String dbFilename = filePath + fileName;

			System.out.println("Uploading DbName：" + dbName);
			System.out.println("Upload Succeful," + count + " files, " + dbFilename);

			main.excelUpload(dbFilename, dbName);

			writer.println(result + "<br>" + uploadPath);
			writer.flush();
			writer.close();

		} catch (Exception e) {
			result = "<h1 style =\"text-align: center\">Upload with Errors!</h1>" + "<h3 style =\"text-align: left\">"
					+ e + "</h3>" + "<br>" + uploadPath;
			writer.println(result);
			writer.flush();
			writer.close();
			e.printStackTrace();
		}

	}
}
