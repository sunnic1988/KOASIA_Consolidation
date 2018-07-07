package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.PreparedStatement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import koasiaco.DBUtil;
import koasiaco.JsonTransfer;

/**
 * Servlet implementation class SmartDeleteRowServlet
 * 配置功能中，删除按钮的实现
 */
@WebServlet("/SmartDeleteRowServlet")
public class SmartDeleteRowServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static String jsonRrequest = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SmartDeleteRowServlet() {
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
		// TODO Auto-generated method stub

		StringBuilder jsonString = new StringBuilder();
		BufferedReader reader = request.getReader();
		String dbName = null;
		String sql_select = null;
		String sql = null;
		int intIndex = 0;

		while ((jsonRrequest = reader.readLine()) != null) {

			jsonString.append(jsonRrequest).append('\n');
			System.out.println("接收到用于删除数据库的JSON:" + jsonRrequest);

			// 处理复杂的Json格式, 先根据String创建JSON对象
			JSONObject obj = JSONObject.fromObject(jsonRrequest);

			// 取出JSON对象里的数组,这里数组名定义的是Rows,同时取到DATA中的数据库名
			JSONArray array = obj.getJSONArray("Rows");
			JSONObject objdata = obj.getJSONObject("Data");
			dbName = (String) objdata.get("dbName");

			JSONObject rows = null;
			try {

				Connection conn = DBUtil.getConnection();
				// 遍历Rows得到对应信息
				for (int i = 0; i < array.size(); i++) {

					rows = array.getJSONObject(i);

					// 通过字符串转化删除的条件，可以改成直接取得Json对象
					sql = rows.toString();

					intIndex = sql.indexOf(",");

					sql = sql.substring(intIndex + 1, sql.length() - 1);

					intIndex = sql.indexOf("__id");

					sql = sql.substring(0, intIndex - 1);

					sql = JsonTransfer.startDel(sql);

					System.out.println("sql del condition：" + sql);

					sql_select = "DELETE FROM " + dbName + " WHERE " + sql;

					PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sql_select);
					
					// 执行sql查询保存在rs里
					stmt.executeUpdate();

					// System.out.println("删除成功dbName是" + dbName + "的第" + (i + 1) + "条数据，共" + array.size() + "条数据");
				}
				System.out.println("Del finished, dbName:" + dbName);

				conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Del failed");
				e.printStackTrace();
			}
		}
	}

}
