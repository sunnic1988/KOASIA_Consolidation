package koasiaco;

import com.mysql.jdbc.PreparedStatement;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;

public class DeleteData {
	private static final char[] List = null;

	public static void start(String dbName) throws IOException, SQLException {
		System.out.println("in mysql which db is del:" + dbName);

		Connection conn = DBUtil.getConnection();

		String[] arr = dbName.split(",");
		for (int z = 0; z < arr.length; z++) {
			String dbstr = arr[z].toString();

			String sql = "delete  from " + dbstr;

			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sql);

			stmt.executeUpdate();

			conn.close();
		}
	}

	public static void normalStartl(String sql) throws IOException, SQLException {
		Connection conn = DBUtil.getConnection();
		PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sql);
		stmt.executeUpdate();
		conn.close();
	}
}
