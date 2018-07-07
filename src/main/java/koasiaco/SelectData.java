package koasiaco;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

public class SelectData {
	private static final char[] List = null;

	public static Object[][] start(String sql_select) throws IOException, SQLException {
		System.out.println("mysql select:" + sql_select);

		Connection conn = DBUtil.getConnection();
		Statement stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery(sql_select);
		ResultSetMetaData colName = rs.getMetaData();
		int colCount = colName.getColumnCount();

		int sample = 0;
		while (rs.next()) {
			sample++;
		}
		Object[][] list = new Object[sample + 1][colCount];
		for (int n = 0; n < colCount; n++) {
			list[0][n] = colName.getColumnName(n + 1);
		}
		rs = stmt.executeQuery(sql_select);

		DecimalFormat df = new DecimalFormat("#,##0.00");
		for (int i = 0; rs.next(); i++) {
			for (int j = 0; j < colCount; j++) {
				switch (colName.getColumnType(j + 1)) {
				case 4:
					list[(i + 1)][j] = Integer.valueOf(rs.getInt(j + 1));
					break;
				case 8:
					list[(i + 1)][j] = df.format(rs.getDouble(j + 1));
					break;
				default:
					list[(i + 1)][j] = rs.getString(j + 1);
				}
			}
		}
		stmt.close();
		rs.close();
		conn.close();

		return list;
	}
}
