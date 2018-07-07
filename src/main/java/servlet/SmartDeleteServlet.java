package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import koasiaco.main;

/**
 * Servlet implementation class SmartDeleteServlet
 * 前台删除数据库内容： 多用于主数据批量维护
 */
@WebServlet("/SmartDeleteServlet")
public class SmartDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SmartDeleteServlet() {
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

		// web端form传过的表名
		String dbName = request.getParameter("DelSelection");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();

		String delResult = "<h1 style =\"text-align: center\">Delete Succeful!</h1>";
		String uploadPath = "<p style=\"text-align: center\"><a href=\"Source/Pages/public/deletefile.jsp\">Go Back!</a></p>";
		try {
			main.deleteData(dbName);
			writer.println(delResult + "<br>" + uploadPath);
			writer.flush();
			writer.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			delResult = "<h1 style =\"text-align: center\">Delete with Errors!</h1>"
					+ "<h3 style =\"text-align: left\">" + e + "</h3>" + "<br>" + uploadPath;
			writer.println(delResult);
			writer.flush();
			writer.close();
			e.printStackTrace();
		}
	}

}
