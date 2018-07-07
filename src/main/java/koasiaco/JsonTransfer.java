package koasiaco;

public class JsonTransfer {
	
	//这个方法废弃，直接用Json对象操作数据
	public static String start(String sql) {
		String sql_condition = "";

		int intIndex1 = 0;
		int intIndex2 = 0;
		while (sql.indexOf(":") > 0) {
			intIndex1 = sql.indexOf(":");
			sql_condition = sql_condition + sqlcolumn(sql.substring(1, intIndex1 - 1));
			intIndex2 = sql.indexOf(",");
			sql_condition = sql_condition + " LIKE " + sqlcondition(sql.substring(intIndex1 + 2, intIndex2 - 1));
			sql = sql.substring(intIndex2 + 1, sql.length());
			sql_condition = sql_condition + " AND ";
		}
		return sql_condition.substring(0, sql_condition.length() - 5);
	}

	public static String startDel(String sql) {
		String sql_condition = "";

		int intIndex1 = 0;
		int intIndex2 = 0;
		while (sql.indexOf(":") > 0) {
			intIndex1 = sql.indexOf(":");
			sql_condition = sql_condition + sqlcolumn(sql.substring(1, intIndex1 - 1));
			intIndex2 = sql.indexOf(",");
			sql_condition = sql_condition + " = " + sqlcondition(sql.substring(intIndex1 + 2, intIndex2 - 1));
			sql = sql.substring(intIndex2 + 1, sql.length());
			sql_condition = sql_condition + " AND ";
		}
		return sql_condition.substring(0, sql_condition.length() - 5);
	}

	public static String startAdd(String sql) {
		String sql_condition = "";
		String sql_condition1 = "";
		String sql_condition2 = "";

		int intIndex1 = 0;
		int intIndex2 = 0;
		while (sql.indexOf(":") > 0) {
			intIndex1 = sql.indexOf(":");
			sql_condition1 = sql_condition1 + sqlcolumn(sql.substring(1, intIndex1 - 1)) + ",";
			sql = sql.substring(intIndex1 + 1, sql.length());

			intIndex2 = sql.indexOf(",");
			sql_condition2 = sql_condition2 + sqlcondition(sql.substring(1, intIndex2 - 1)) + ",";
			sql = sql.substring(intIndex2 + 1, sql.length());
		}
		sql_condition = "( " + sql_condition1.substring(0, sql_condition1.length() - 1) + ") VALUES ("
				+ sql_condition2.substring(0, sql_condition2.length() - 1) + " )";

		return sql_condition;
	}

	public static String sqlcolumn(String str) {
		str = "`" + str + "`";
		return str;
	}

	public static String sqlcondition(String str) {
		str = "'" + str + "'";
		return str;
	}
}
