package servlet;

import com.mysql.jdbc.PreparedStatement;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import koasiaco.DBUtil;
import koasiaco.JsonTransfer;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 前台配置功能中，grid save按钮的功能实现
 */

@WebServlet({ "/SmartSaveRowServlet" })
public class SmartSaveRowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static String jsonRrequest = null;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		StringBuilder jsonString = new StringBuilder();
		BufferedReader reader = request.getReader();
		String dbName = null;
		String sql = null;
		String sql_select = null;
		String status = null;
		int intIndex = 0;
		
		while ((jsonRrequest = reader.readLine()) != null) {
			jsonString.append(jsonRrequest).append('\n');

			System.out.println("Received ajax JSON:" + jsonRrequest);

			JSONObject obj = JSONObject.fromObject(jsonRrequest);

			JSONArray array = obj.getJSONArray("Rows");
			JSONObject objdata = obj.getJSONObject("Data");
			dbName = (String) objdata.get("dbName");

			JSONObject rows = null;
			try {
				Connection conn = DBUtil.getConnection();
				for (int i = 0; i < array.size(); i++) {
					rows = array.getJSONObject(i);

					status = (String) rows.get("__status");
					switch (status) {
					// 判断是新增还是更新
					case "add":
						sql = rows.toString();
						intIndex = sql.indexOf("add");
						sql = sql.substring(intIndex + 5, sql.length() - 1) + ",";
						sql = JsonTransfer.startAdd(sql);
						System.out.println("addsql:" + sql);
						sql_select = "INSERT INTO " + dbName + " " + sql;
						PreparedStatement stmtadd = (PreparedStatement) conn.prepareStatement(sql_select);
						stmtadd.executeUpdate();
						System.out.println("Saved to dbName：" + dbName + "***** Content is" + sql);
						break;
					case "update":
						// 判断更新的具体表名
						switch (dbName) {
						case "sys_gl_masterdata":
							String GL_Account = sqlcondition(rows.getString("G/L Account"));
							String FS_Item = sqlcondition(rows.getString("FS Item"));
							String IRT_Structure = sqlcondition(rows.getString("IRT Structure"));
							String BS_Indicate = sqlcondition(rows.getString("BS Indicate"));
							String Asset_Indicate = sqlcondition(rows.getString("Asset Indicate"));
							String GL_Description = sqlcondition(rows.getString("GL Description"));
							sql_select = "UPDATE " + dbName + " SET `BS Indicate` = " + BS_Indicate
									+ ", `Asset Indicate` = " + Asset_Indicate + ", `GL Description` = "
									+ GL_Description + " WHERE `G/L Account` = " + GL_Account + " AND `FS Item` = "
									+ FS_Item + " AND `IRT Structure` = " + IRT_Structure;

							System.out.println("sql_select*********" + sql_select);
							break;
						case "sys_configuration_to_python":
							String item = sqlcondition(rows.getString("Item"));
							String configuration = sqlcondition(rows.getString("Configuration"));
							sql_select = "UPDATE " + dbName + " SET `Configuration` = " + configuration
									+ " WHERE `Item` = " + item;

							System.out.println("sql_select*********" + sql_select);
							break;
						case "sys_exchage_rate":
							String ExRType = sqlcondition(rows.getString("ExRType"));
							String Year = sqlcondition(rows.getString("Year"));
							String Period = sqlcondition(rows.getString("Period"));
							String FromCurrency = sqlcondition(rows.getString("FromCurrency"));
							String ToCurrency = sqlcondition(rows.getString("ToCurrency"));
							String RatioFrom = sqlcondition(rows.getString("RatioFrom"));
							String DirQuot = sqlcondition(rows.getString("DirQuot"));
							sql_select = "UPDATE " + dbName + " SET `RatioFrom` = " + RatioFrom + ", `DirQuot` = "
									+ DirQuot + " WHERE `ExRType` = " + ExRType + " AND `Year` = " + Year
									+ " AND `Period` = " + Period + " AND `FromCurrency` = " + FromCurrency
									+ " AND `ToCurrency` = " + ToCurrency;

							System.out.println("sql_select*********" + sql_select);
							break;
						default:
							System.out.println("Saved failed");
						}
						PreparedStatement stmtupdate = (PreparedStatement) conn.prepareStatement(sql_select);
						stmtupdate.executeUpdate();
						System.out.println("Saved to dbName：" + dbName + "***** Content is" + sql_select);
						break;
					default:
						System.out.println("thereof number:" + i + "*****do not need to updated" + sql);
					}
				}
				conn.close();
			} catch (SQLException e) {
				System.out.println("Saved or upated failed");
				e.printStackTrace();
			}
		}
	}

	public static String sqlcondition(String str) {
		str = "'" + str + "'";
		return str;
	}
}
