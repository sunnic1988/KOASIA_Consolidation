package koasiaco;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonSelectData {
	public static JSONArray start(String sql_select, String dbName) throws SQLException {
		System.out.println("mysql select:" + sql_select);

		Connection conn = DBUtil.getConnection();
		Statement stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery(sql_select);

		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();

		JSONObject jsonObj = new JSONObject();

		JSONArray jsonArray = new JSONArray();
		for (int n = 0; rs.next(); n++) {
			jsonObj.put("__status", "null");
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnLabel(i);
				String value = rs.getString(columnName);
				jsonObj.put(columnName, value);
			}
			jsonArray.add(n, jsonObj);
		}
		rs.close();
		conn.close();

		return jsonArray;
	}
}