package koasiaco;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SmartInsertExcel {
	public static void start(InputStream in, String path, String tablename) throws Exception {
		Workbook book = getWorkBook(in, path);
		List<Sheet> sheets = getSheets(book);
		SheetIterator(sheets, tablename);
	}

	public static Workbook getWorkBook(InputStream in, String path) throws FileNotFoundException, IOException {
		return path.endsWith(".xlsx") ? new XSSFWorkbook(in) : path.endsWith(".xls") ? new HSSFWorkbook(in) : null;
	}

	private static List<Sheet> getSheets(Workbook book) {
		int numberOfSheets = book.getNumberOfSheets();
		System.out.println("Sheets number is:" + numberOfSheets);
		List<Sheet> sheets = new ArrayList<Sheet>();
		for (int i = 0; i < numberOfSheets; i++) {
			sheets.add(book.getSheetAt(i));
		}
		return sheets;
	}

	private static void SheetIterator(List<Sheet> sheets, String tablename) throws Exception {
		Connection conn = null;

		PreparedStatement prep = null;

		int countItem = 0;

		String dbName = null;
		System.out.println("uploadServlet tablename is :" + tablename);
		
		for (int i = 0; i < sheets.size(); i++) {
			Sheet sheet = (Sheet) sheets.get(i);
			System.out.println(sheet.getLastRowNum());
			if (sheet.getLastRowNum() >= 1) {
				System.out.println("the name of sheet is" + sheet.getSheetName());
				if (isNumeric(tablename)) {
					dbName = sheet.getSheetName();
				} else {
					dbName = tablename;
				}
				System.out.println("insert table to mysql:" + dbName);

				Row fisrtRow = sheet.getRow(0);

				int filledColumns = fisrtRow.getLastCellNum();

				String columnid = "";
				String valueid = "";
				for (int y = 0; y < filledColumns; y++) {
					Cell fistLinecell = fisrtRow.getCell(y);
					String coluid = fistLinecell.getStringCellValue();
					columnid = columnid + "," + sqlcolumn(coluid);
					valueid = valueid + "?,";
				}
				String columnName = "(" + columnid.substring(1, columnid.length()) + ") VALUES("
						+ valueid.substring(0, valueid.length() - 1) + ")";

				String sql_save = " INSERT INTO " + dbName + columnName;
				System.out.println("insert mysql:" + sql_save);

				conn = DBUtil.getConnection();
				prep = conn.prepareStatement(sql_save);

				conn.setAutoCommit(false);
				
				//遍历所有 row -> cells
				Iterator<Row> iterator = sheet.iterator();
				while (iterator.hasNext()) {
					Row nextRow = (Row) iterator.next();
					if (nextRow.getRowNum() >= 1) {
						countItem += 1;
						for (int j = 1; j <= filledColumns; j++) {
							Cell cell = nextRow.getCell(j - 1);
							if (cell == null) {
								prep.setString(j, "");
							} else {
								switch (cell.getCellType()) {
								case 0:
									prep.setDouble(j, cell.getNumericCellValue());
									break;
								case 3:
									prep.setString(j, "");

									break;
								case 2:
									prep.setString(j, cell.getStringCellValue());

									break;
								case 1:
								default:
									prep.setString(j, cell.getStringCellValue());
								}
							}
						}
						prep.addBatch();
					}
				}
				prep.executeBatch();
				conn.commit();
				prep.close();
				DBUtil.release(prep, conn);
			}
		}
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static String sqlcolumn(String str) {
		str = "`" + str + "`";
		return str;
	}
}
