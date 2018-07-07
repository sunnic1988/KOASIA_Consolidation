package koasiaco;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import net.sf.json.JSONArray;

public class main {
	public static void main(String[] args) throws Exception {
	}

	public static void excelUpload(String path, String tablename) throws Exception {
		InputStream in = new FileInputStream(new File(path));
		SmartInsertExcel.start(in, path, tablename);
		in.close();
	}

	public static void excelDownload(String dbName) throws IOException, SQLException {
		SmartExportExcel.start(dbName);
	}

	public static JSONArray jsonselectData(String sql_select, String dbName) throws IOException, SQLException {
		return JsonSelectData.start(sql_select, dbName);
	}

	public static Object[][] selectData(String sql_select) throws IOException, SQLException {
		return SelectData.start(sql_select);
	}

	public static void deleteData(String dbName) throws IOException, SQLException {
		DeleteData.start(dbName);
	}
}