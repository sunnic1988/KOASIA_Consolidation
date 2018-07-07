package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;

import koasiaco.main;
import koasiaco.JsonTransfer;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class SelectConfigurationServlet
 * 前台配置界面的查询功能
 */
@WebServlet("/SelectConfigurationServlet")
public class SelectConfigurationServlet extends HttpServlet {

	static String jsonRrequest = null;
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SelectConfigurationServlet() {
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
	 * @throws IOException
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 获取前台的传过来的值，ajax要自己定义
		StringBuilder jsonString = new StringBuilder();
		BufferedReader reader = request.getReader();

		while ((jsonRrequest = reader.readLine()) != null) {
			jsonString.append(jsonRrequest).append('\n');
			System.out.println("接收到web端的JSON:" + jsonRrequest);

			// 创建Json对象，得到数据库名
			JSONObject json = JSONObject.fromObject(jsonRrequest);
			String dbName = json.getString("dbName");

			// 分解字段名和筛选内容
			String sql = jsonRrequest.substring(1, jsonRrequest.length() - 1);
			int intIndex = sql.indexOf(",");
			sql = sql.substring(intIndex + 1, sql.length()) + ",";

			System.out.println("MySql String:" + sql);

			String sql_condition = JsonTransfer.start(sql);

			sql_condition = " WHERE " + sql_condition;

			// 得到最终的查询条件
			String sql_select = "SELECT * FROM " + dbName + sql_condition;

			System.out.println("MySql String::" + sql_select);

			PrintWriter writer = response.getWriter();

			try {

				JSONObject obj = new JSONObject();
				List<?> list = main.jsonselectData(sql_select, dbName);

				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");

				// 调整成grid接收的JSON对象
				obj.put("Rows", list);
				obj.put("Total", list.size());
				System.out.println(obj.toString());
				// 返回AJAX
				writer.println(obj.toString());

			} catch (SQLException e) {
				System.out.println("Search failed！");
				e.printStackTrace();
			}
		}
	}

}
