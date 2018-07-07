package koasiaco;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import net.sf.json.JSONObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import reports.RunIRTbyPeriod;

public class SmartExportExcel {
	static String expfile;

	//导出各种mysql表格，只要传递表名这个参数
	public static void start(String dbName) throws IOException, SQLException {
		ClassLoader classLoader = DBUtil.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream("config/props/db.properties");
		Properties props = new Properties();
		props.load(is);
		expfile = props.getProperty("exportFileName");

		String[] arr = dbName.split(",");

		Workbook workbook = new XSSFWorkbook();

		Connection conn = DBUtil.getConnection();

		Statement stmt = conn.createStatement();
		for (int z = 0; z < arr.length; z++) {
			String dbstr = arr[z].toString();

			String sql_select = " select * from " + dbstr;

			ResultSet rs = stmt.executeQuery(sql_select);

			ResultSetMetaData colName = rs.getMetaData();
			int colCount = colName.getColumnCount();

			Sheet sheet = workbook.createSheet(dbstr);

			Row row = sheet.createRow(0);

			Cell cell = null;

			//标题样式
			CellStyle cellStyle1 = workbook.createCellStyle();
			XSSFFont font1 = (XSSFFont) workbook.createFont();
			font1.setFontName("Calibri");
			font1.setBoldweight((short) 700);
			font1.setColor(IndexedColors.WHITE.getIndex());
			cellStyle1.setFillPattern((short) 1);
			cellStyle1.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
			cellStyle1.setAlignment((short) 2);

			cellStyle1.setFont(font1);

			//内容样式
			CellStyle cellStyle2 = workbook.createCellStyle();
			XSSFFont font2 = (XSSFFont) workbook.createFont();
			font2.setFontName("Calibri");
			font2.setBoldweight((short) 400);
			cellStyle2.setFont(font2);
			
			for (int n = 0; n < colCount; n++) {
				cell = row.createCell((short) n);
				cell.setCellValue(colName.getColumnName(n + 1));
				cell.setCellStyle(cellStyle1);
			}
			int i = 1;
			while (rs.next()) {
				row = sheet.createRow(i);
				for (int j = 1; j <= colCount; j++) {
					cell = row.createCell((short) j - 1);
					switch (colName.getColumnType(j)) {
					case 4:
						cell.setCellStyle(cellStyle2);
						cell.setCellValue(rs.getInt(j));
						break;
					case 8:
						cell.setCellStyle(cellStyle2);
						cell.setCellValue(rs.getDouble(j));
						break;
					default:
						cell.setCellStyle(cellStyle2);
						cell.setCellValue(rs.getString(j));
					}
				}
				i++;
			}
			for (int x = 0; x < colCount; x++) {
				sheet.autoSizeColumn((short) x);
			}
		}
		FileOutputStream FOut = new FileOutputStream(expfile);
		workbook.write(FOut);
		FOut.flush();
		FOut.close();
		stmt.close();
		conn.close();
		System.out.println("export to:" + expfile);
	}

	//导出IRT格式样式
	public static void reportStart(String Year, String CompanyCode, String Currency, String Period)
    throws IOException, SQLException
  {
    ClassLoader classLoader = DBUtil.class.getClassLoader();
    InputStream is = classLoader.getResourceAsStream("config/props/db.properties");
    Properties props = new Properties();
    props.load(is);
    expfile = props.getProperty("exportFileName");
    Workbook workbook = new XSSFWorkbook();
    
    Connection conn = DBUtil.getConnection();
    
    Statement stmt = conn.createStatement();
    
    //创建sheet for profit and loss
    
    Sheet sheet_PL = workbook.createSheet("Profit and Loss");
    int col_Num = 16;
    
    //标题样式和内容
    Row row = sheet_PL.createRow(0);
    Cell cell = null;
    cell = row.createCell(0);
    cell.setCellValue("Profit and Loss");
    String[] Border = { "Top", "Bottom", "Right", "Left" };
    CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "BOLD", "CENTER", "", Border, "");
    cell.setCellStyle(cellStyle);
    cell = row.createCell(16);
    cell.setCellStyle(cellStyle);
    
    row = sheet_PL.createRow(1);
    String[] row_Title = { "", "Description", "Accouts", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Adj", "Total" };
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "BOLD", "CENTER", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue(row_Title[i]);
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "BOLD", "CENTER", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue(row_Title[i]);
        cell.setCellStyle(cellStyle);
      }
    }
    
    //profit and loss 的内容
    int row_Num_Start = 2;
    int row_Num_REVENUES = reportSQL(workbook, stmt, Year, CompanyCode, "REVENUES", Currency, row_Num_Start, sheet_PL, "-");
    
    row_Num_Start = row_Num_REVENUES;
    int row_Num_CIS = reportSQL(workbook, stmt, Year, CompanyCode, "Change in Stockvalue", Currency, row_Num_Start, sheet_PL, "-");
    
    row_Num_Start = row_Num_CIS;
    int row_Num_OCC = reportSQL(workbook, stmt, Year, CompanyCode, "Other Capitalized Costs", Currency, row_Num_Start, sheet_PL, "-");
    
    int row_Num_BV = row_Num_OCC;
    row = sheet_PL.createRow((short)row_Num_BV);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_REVENUES + "+" + getExcelColumnLabel(i) + row_Num_CIS + "+" + 
          getExcelColumnLabel(i) + row_Num_OCC);
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Total Business Volume");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_REVENUES + "+" + getExcelColumnLabel(i) + row_Num_CIS + "+" + 
          getExcelColumnLabel(i) + row_Num_OCC);
        cell.setCellStyle(cellStyle);
      }
    }
    
    row_Num_Start = row_Num_BV + 1;
    int row_Num_DM = reportSQL(workbook, stmt, Year, CompanyCode, "Direct Material", Currency, row_Num_Start, sheet_PL, "+");
    
    int row_Num_DM_Rate = row_Num_DM;
    row = sheet_PL.createRow((short)row_Num_DM_Rate);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(
          getExcelColumnLabel(i) + row_Num_DM + "/" + getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("in % of Total Business Volume");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(
          getExcelColumnLabel(i) + row_Num_DM + "/" + getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
    }
    
    row_Num_Start = row_Num_DM + 1;
    int row_Num_MH = reportPLSQL(workbook, stmt, Year, CompanyCode, "Material Handling", Currency, row_Num_Start, sheet_PL);
    
    row_Num_Start = row_Num_MH;
    int row_Num_DL = reportPLSQL(workbook, stmt, Year, CompanyCode, "Direct Labor", Currency, row_Num_Start, sheet_PL);
    
    row_Num_Start = row_Num_DL;
    int row_Num_IL = reportPLSQL(workbook, stmt, Year, CompanyCode, "Indirect Labor", Currency, row_Num_Start, sheet_PL);
    
    row_Num_Start = row_Num_IL;
    int row_Num_Dep_M = reportPLSQL(workbook, stmt, Year, CompanyCode, "Depn of Machinery", Currency, row_Num_Start, sheet_PL);
    
    row_Num_Start = row_Num_Dep_M;
    int row_Num_OMC = reportPLSQL(workbook, stmt, Year, CompanyCode, "Other Manufacturing Cost", Currency, row_Num_Start, sheet_PL);
    
    int row_Num_MC = row_Num_OMC;
    row = sheet_PL.createRow((short)row_Num_MC);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_DM + "+" + getExcelColumnLabel(i) + row_Num_MH + "+" + 
          getExcelColumnLabel(i) + row_Num_DL + "+" + getExcelColumnLabel(i) + row_Num_IL + "+" + 
          getExcelColumnLabel(i) + row_Num_Dep_M + "+" + getExcelColumnLabel(i) + row_Num_OMC);
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Manufacturing Costs");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_DM + "+" + getExcelColumnLabel(i) + row_Num_MH + "+" + 
          getExcelColumnLabel(i) + row_Num_DL + "+" + getExcelColumnLabel(i) + row_Num_IL + "+" + 
          getExcelColumnLabel(i) + row_Num_Dep_M + "+" + getExcelColumnLabel(i) + row_Num_OMC);
        cell.setCellStyle(cellStyle);
      }
    }
    
    int row_Num_GM = row_Num_MC + 1;
    row = sheet_PL.createRow((short)row_Num_GM);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(
          getExcelColumnLabel(i) + (row_Num_BV + 1) + "-" + getExcelColumnLabel(i) + (row_Num_MC + 1));
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Gross Margin");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(
          getExcelColumnLabel(i) + (row_Num_BV + 1) + "-" + getExcelColumnLabel(i) + (row_Num_MC + 1));
        cell.setCellStyle(cellStyle);
      }
    }
    
    int row_Num_GM_Rate = row_Num_GM + 1;
    row = sheet_PL.createRow((short)row_Num_GM_Rate);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(
          getExcelColumnLabel(i) + (row_Num_GM + 1) + "/" + getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("in % of Total Business Volume");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(
          getExcelColumnLabel(i) + (row_Num_GM + 1) + "/" + getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
    }
    
    row_Num_Start = row_Num_GM_Rate + 1;
    int row_Num_E = reportPLSQL(workbook, stmt, Year, CompanyCode, "Engineering", Currency, row_Num_Start, sheet_PL);
    
    row_Num_Start = row_Num_E;
    int row_Num_A = reportPLSQL(workbook, stmt, Year, CompanyCode, "Administration", Currency, row_Num_Start, sheet_PL);
    
    row_Num_Start = row_Num_A;
    int row_Num_S = reportPLSQL(workbook, stmt, Year, CompanyCode, "Sales", Currency, row_Num_Start, sheet_PL);
    
    int row_Num_OC = row_Num_S;
    row = sheet_PL.createRow((short)row_Num_OC);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_E + "+" + getExcelColumnLabel(i) + row_Num_A + "+" + 
          getExcelColumnLabel(i) + row_Num_S);
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Overhead Costs");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_E + "+" + getExcelColumnLabel(i) + row_Num_A + "+" + 
          getExcelColumnLabel(i) + row_Num_S);
        cell.setCellStyle(cellStyle);
      }
    }
    
    int row_Num_OI = row_Num_OC + 1;
    row = sheet_PL.createRow((short)row_Num_OI);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(
          getExcelColumnLabel(i) + (row_Num_GM + 1) + "-" + getExcelColumnLabel(i) + row_Num_OI);
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Operating Income");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(
          getExcelColumnLabel(i) + (row_Num_GM + 1) + "-" + getExcelColumnLabel(i) + row_Num_OI);
        cell.setCellStyle(cellStyle);
      }
    }
    
    int row_Num_OI_Rate = row_Num_OI + 1;
    row = sheet_PL.createRow((short)row_Num_OI_Rate);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(
          getExcelColumnLabel(i) + (row_Num_OI + 1) + "/" + getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("in % of Total Business Volume");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(
          getExcelColumnLabel(i) + (row_Num_OI + 1) + "/" + getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
    }
    
    row_Num_Start = row_Num_OI_Rate + 1;
    int row_Num_Monetary = reportSQL(workbook, stmt, Year, CompanyCode, "Monetary Corrections", Currency, row_Num_Start, sheet_PL, "-");
    
    row_Num_Start = row_Num_Monetary;
    int row_Num_Exchange = reportSQL(workbook, stmt, Year, CompanyCode, "Exchange Gains/Losses", Currency, row_Num_Start, sheet_PL, "-");
    
    row_Num_Start = row_Num_Exchange;
    int row_Num_Interest = reportSQL(workbook, stmt, Year, CompanyCode, "Interest Income / Expenses", Currency, row_Num_Start, sheet_PL, "-");
    
    row_Num_Start = row_Num_Interest;
    int row_Num_Other_Income = reportSQL(workbook, stmt, Year, CompanyCode, "Other Income / Expenses", Currency, row_Num_Start, sheet_PL, "-");
    
    int row_Num_NOI = row_Num_Other_Income;
    row = sheet_PL.createRow((short)row_Num_NOI);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Monetary + "+" + getExcelColumnLabel(i) + row_Num_Exchange + "+" + 
          getExcelColumnLabel(i) + row_Num_Interest + "+" + 
          getExcelColumnLabel(i) + row_Num_Other_Income);
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Non Operating Income");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Monetary + "+" + getExcelColumnLabel(i) + row_Num_Exchange + "+" + 
          getExcelColumnLabel(i) + row_Num_Interest + "+" + 
          getExcelColumnLabel(i) + row_Num_Other_Income);
        cell.setCellStyle(cellStyle);
      }
    }
    
    int row_Num_NOI_Rate = row_Num_NOI + 1;
    row = sheet_PL.createRow((short)row_Num_NOI_Rate);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(
          getExcelColumnLabel(i) + (row_Num_NOI + 1) + "/" + getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("in % of Total Business Volume");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(
          getExcelColumnLabel(i) + (row_Num_NOI + 1) + "/" + getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
    }
    
    int row_Num_IBT = row_Num_NOI_Rate + 1;
    row = sheet_PL.createRow((short)row_Num_IBT);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(
          getExcelColumnLabel(i) + (row_Num_NOI + 1) + "+" + getExcelColumnLabel(i) + (row_Num_OI + 1));
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Income before Taxes");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(
          getExcelColumnLabel(i) + (row_Num_NOI + 1) + "+" + getExcelColumnLabel(i) + (row_Num_OI + 1));
        cell.setCellStyle(cellStyle);
      }
    }
    
    int row_Num_IBT_Rate = row_Num_IBT + 1;
    row = sheet_PL.createRow((short)row_Num_IBT_Rate);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(
          getExcelColumnLabel(i) + (row_Num_IBT + 1) + "/" + getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("in % of Total Business Volume");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(
          getExcelColumnLabel(i) + (row_Num_IBT + 1) + "/" + getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
    }
    sheet_PL.createFreezePane(3, 2);
    for (int i = 0; i < col_Num; i++) {
      if (i <= 2) {
        sheet_PL.autoSizeColumn(i);
      } else {
        sheet_PL.setColumnWidth(0, 1280);
      }
    }
    sheet_PL.setDisplayGridlines(false);
    
    sheet_PL.addMergedRegion(new CellRangeAddress(0, 0, 0, 16));
    
    
    
    
    //创建 operating costs
    Sheet sheet_OC = workbook.createSheet("Operating Costs");
    
    row = sheet_OC.createRow(0);
    cell = row.createCell(0);
    cell.setCellValue("Operating Costs");
    String[] BorderOC = { "Top", "Bottom", "Right", "Left" };
    cellStyle = reportSetStyle(workbook, "Calibri", "BOLD", "CENTER", "", BorderOC, "");
    cell.setCellStyle(cellStyle);
    cell = row.createCell(16);
    cell.setCellStyle(cellStyle);
    
    row = sheet_OC.createRow(1);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "BOLD", "CENTER", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue(row_Title[i]);
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "BOLD", "CENTER", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue(row_Title[i]);
        cell.setCellStyle(cellStyle);
      }
    }
    
    row_Num_Start = 2;
    int row_Num_Productive_Wages = reportSQL(workbook, stmt, Year, CompanyCode, "Productive Wages", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Productive_Wages;
    int row_Num_Unproductive_Wages = reportSQL(workbook, stmt, Year, CompanyCode, "Unproductive Wages", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Unproductive_Wages;
    int row_Num_Overtime_Premium = reportSQL(workbook, stmt, Year, CompanyCode, "Overtime Premium", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Overtime_Premium;
    int row_Num_Other_Fringe_Benefits_Wages = reportSQL(workbook, stmt, Year, CompanyCode, "Other Fringe Benefits Wages", Currency, row_Num_Start, sheet_OC, "+");
    
    int row_Num_Total_Wages = row_Num_Other_Fringe_Benefits_Wages;
    row = sheet_OC.createRow((short)row_Num_Total_Wages);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Productive_Wages + "+" + getExcelColumnLabel(i) + row_Num_Unproductive_Wages + "+" + 
          getExcelColumnLabel(i) + row_Num_Overtime_Premium + "+" + 
          getExcelColumnLabel(i) + row_Num_Other_Fringe_Benefits_Wages);
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Total Wages");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Productive_Wages + "+" + getExcelColumnLabel(i) + row_Num_Unproductive_Wages + "+" + 
          getExcelColumnLabel(i) + row_Num_Overtime_Premium + "+" + 
          getExcelColumnLabel(i) + row_Num_Other_Fringe_Benefits_Wages);
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_Total_Wages_Rate = row_Num_Total_Wages + 1;
    row = sheet_OC.createRow((short)row_Num_Total_Wages_Rate);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Total_Wages + 1) + "/'Profit and Loss'!" + 
          getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("in % of Total Business Volume");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Total_Wages + 1) + "/'Profit and Loss'!" + 
          getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
    }
    row_Num_Start = row_Num_Total_Wages_Rate + 1;
    int row_Num_Salaries = reportSQL(workbook, stmt, Year, CompanyCode, "Salaries", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Salaries;
    int row_Num_Overtime = reportSQL(workbook, stmt, Year, CompanyCode, "Overtime", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Overtime;
    int row_Num_Other_Fringe_Benefits = reportSQL(workbook, stmt, Year, CompanyCode, "Other Fringe Benefits", Currency, row_Num_Start, sheet_OC, "+");
    
    int row_Num_Total_Salaries = row_Num_Other_Fringe_Benefits;
    row = sheet_OC.createRow((short)row_Num_Total_Salaries);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Salaries + "+" + getExcelColumnLabel(i) + row_Num_Overtime + "+" + 
          getExcelColumnLabel(i) + row_Num_Other_Fringe_Benefits);
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Total Salaries");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Salaries + "+" + getExcelColumnLabel(i) + row_Num_Overtime + "+" + 
          getExcelColumnLabel(i) + row_Num_Other_Fringe_Benefits);
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_Total_Salaries_Rate = row_Num_Total_Salaries + 1;
    row = sheet_OC.createRow((short)row_Num_Total_Salaries_Rate);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Total_Salaries + 1) + "/'Profit and Loss'!" + 
          getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("in % of Total Business Volume");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Total_Salaries + 1) + "/'Profit and Loss'!" + 
          getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_Total_Personnel_Costs = row_Num_Total_Salaries_Rate + 1;
    row = sheet_OC.createRow((short)row_Num_Total_Personnel_Costs);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Total_Wages + 1) + "+" + getExcelColumnLabel(i) + (row_Num_Total_Salaries + 1));
        
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Total Personnel Costs");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Total_Wages + 1) + "+" + getExcelColumnLabel(i) + (row_Num_Total_Salaries + 1));
        
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_Total_Personnel_Costs_Rate = row_Num_Total_Personnel_Costs + 1;
    row = sheet_OC.createRow((short)row_Num_Total_Personnel_Costs_Rate);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Total_Personnel_Costs + 1) + "/'Profit and Loss'!" + 
          getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("in % of Total Business Volume");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Total_Personnel_Costs + 1) + "/'Profit and Loss'!" + 
          getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
    }
    row_Num_Start = row_Num_Total_Personnel_Costs_Rate + 1;
    int row_Num_Consumables = reportSQL(workbook, stmt, Year, CompanyCode, "Consumables", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Consumables;
    int row_Num_Energy = reportSQL(workbook, stmt, Year, CompanyCode, "Energy", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Energy;
    int row_Num_Repairwork = reportSQL(workbook, stmt, Year, CompanyCode, "Repairwork (external)", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Repairwork;
    int row_Num_Leasing = reportSQL(workbook, stmt, Year, CompanyCode, "Leasing/Depreciation (Build.)", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Leasing;
    int row_Num_Other_Leasing_Costs = reportSQL(workbook, stmt, Year, CompanyCode, "Other Leasing Costs", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Other_Leasing_Costs;
    int row_Num_Other_Depreciation = reportSQL(workbook, stmt, Year, CompanyCode, "Other Depreciation", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Other_Depreciation;
    int row_Num_Engineering_Cost_Depreciation = reportSQL(workbook, stmt, Year, CompanyCode, "Engineering Cost Depreciation", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Engineering_Cost_Depreciation;
    int row_Num_Interest_Costs = reportSQL(workbook, stmt, Year, CompanyCode, "Interest Costs", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Interest_Costs;
    int row_Num_Taxes = reportSQL(workbook, stmt, Year, CompanyCode, "Taxes/Duties", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Taxes;
    int row_Num_Insurances = reportSQL(workbook, stmt, Year, CompanyCode, "Insurances", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Insurances;
    int row_Num_Travel = reportSQL(workbook, stmt, Year, CompanyCode, "Travel and Subsistance", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Travel;
    int row_Num_Telephone = reportSQL(workbook, stmt, Year, CompanyCode, "Telephone/Telex/Telefax/Mail", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Telephone;
    int row_Num_Legal = reportSQL(workbook, stmt, Year, CompanyCode, "Legal/Audit", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Legal;
    int row_Num_Airfreight = reportSQL(workbook, stmt, Year, CompanyCode, "Airfreight", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Airfreight;
    int row_Num_Surface_Freight = reportSQL(workbook, stmt, Year, CompanyCode, "Surface Freight", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Surface_Freight;
    int row_Num_Canteen = reportSQL(workbook, stmt, Year, CompanyCode, "Canteen", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Canteen;
    int row_Num_Royalties = reportSQL(workbook, stmt, Year, CompanyCode, "Royalties", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_Royalties;
    int row_Num_GCSP = reportSQL(workbook, stmt, Year, CompanyCode, "GCSP", Currency, row_Num_Start, sheet_OC, "+");
    
    row_Num_Start = row_Num_GCSP;
    int row_Num_Other_Costs = reportSQL(workbook, stmt, Year, CompanyCode, "Other Costs", Currency, row_Num_Start, sheet_OC, "+");
    
    int row_Num_Total_Other_Costs = row_Num_Other_Costs;
    row = sheet_OC.createRow((short)row_Num_Total_Other_Costs);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Consumables + "+" + getExcelColumnLabel(i) + row_Num_Energy + "+" + 
          getExcelColumnLabel(i) + row_Num_Repairwork + "+" + 
          getExcelColumnLabel(i) + row_Num_Leasing + "+" + getExcelColumnLabel(i) + row_Num_Other_Leasing_Costs + "+" + 
          getExcelColumnLabel(i) + row_Num_Other_Depreciation + "+" + 
          getExcelColumnLabel(i) + row_Num_Engineering_Cost_Depreciation + "+" + 
          getExcelColumnLabel(i) + row_Num_Interest_Costs + "+" + getExcelColumnLabel(i) + row_Num_Taxes + "+" + 
          getExcelColumnLabel(i) + row_Num_Insurances + "+" + 
          getExcelColumnLabel(i) + row_Num_Travel + "+" + getExcelColumnLabel(i) + row_Num_Telephone + "+" + 
          getExcelColumnLabel(i) + row_Num_Legal + "+" + getExcelColumnLabel(i) + row_Num_Airfreight + "+" + 
          getExcelColumnLabel(i) + row_Num_Surface_Freight + "+" + 
          getExcelColumnLabel(i) + row_Num_Canteen + "+" + getExcelColumnLabel(i) + row_Num_Royalties + "+" + 
          getExcelColumnLabel(i) + row_Num_GCSP + "+" + 
          getExcelColumnLabel(i) + row_Num_Other_Costs);
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Total Other Costs");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Consumables + "+" + getExcelColumnLabel(i) + row_Num_Energy + "+" + 
          getExcelColumnLabel(i) + row_Num_Repairwork + "+" + 
          getExcelColumnLabel(i) + row_Num_Leasing + "+" + getExcelColumnLabel(i) + row_Num_Other_Leasing_Costs + "+" + 
          getExcelColumnLabel(i) + row_Num_Other_Depreciation + "+" + 
          getExcelColumnLabel(i) + row_Num_Engineering_Cost_Depreciation + "+" + 
          getExcelColumnLabel(i) + row_Num_Interest_Costs + "+" + getExcelColumnLabel(i) + row_Num_Taxes + "+" + 
          getExcelColumnLabel(i) + row_Num_Insurances + "+" + 
          getExcelColumnLabel(i) + row_Num_Travel + "+" + getExcelColumnLabel(i) + row_Num_Telephone + "+" + 
          getExcelColumnLabel(i) + row_Num_Legal + "+" + getExcelColumnLabel(i) + row_Num_Airfreight + "+" + 
          getExcelColumnLabel(i) + row_Num_Surface_Freight + "+" + 
          getExcelColumnLabel(i) + row_Num_Canteen + "+" + getExcelColumnLabel(i) + row_Num_Royalties + "+" + 
          getExcelColumnLabel(i) + row_Num_GCSP + "+" + 
          getExcelColumnLabel(i) + row_Num_Other_Costs);
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_Total_Other_Costs_Rate = row_Num_Total_Other_Costs + 1;
    row = sheet_OC.createRow((short)row_Num_Total_Other_Costs_Rate);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Total_Other_Costs + 1) + "/'Profit and Loss'!" + 
          getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("in % of Total Business Volume");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Total_Other_Costs + 1) + "/'Profit and Loss'!" + 
          getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_Total_Operating_Costs = row_Num_Total_Other_Costs_Rate + 1;
    row = sheet_OC.createRow((short)row_Num_Total_Operating_Costs);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Total_Personnel_Costs + 1) + "+" + 
          getExcelColumnLabel(i) + (row_Num_Total_Other_Costs + 1));
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Total Operating Costs");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Total_Personnel_Costs + 1) + "+" + 
          getExcelColumnLabel(i) + (row_Num_Total_Other_Costs + 1));
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_Total_Operating_Costs_Rate = row_Num_Total_Operating_Costs + 1;
    row = sheet_OC.createRow((short)row_Num_Total_Operating_Costs_Rate);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Total_Operating_Costs_Rate + "/'Profit and Loss'!" + 
          getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("in % of Total Business Volume");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "0.0%", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Total_Operating_Costs_Rate + "/'Profit and Loss'!" + 
          getExcelColumnLabel(i) + (row_Num_BV + 1));
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_PL_Check = row_Num_Total_Operating_Costs_Rate + 1;
    row = sheet_OC.createRow((short)row_Num_PL_Check);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Total_Operating_Costs + 1) + "-'Profit and Loss'!" + 
          getExcelColumnLabel(i) + row_Num_MH + "-'Profit and Loss'!" + 
          getExcelColumnLabel(i) + row_Num_DL + "-'Profit and Loss'!" + getExcelColumnLabel(i) + row_Num_IL + "-'Profit and Loss'!" + 
          getExcelColumnLabel(i) + row_Num_Dep_M + "-'Profit and Loss'!" + 
          getExcelColumnLabel(i) + row_Num_OMC + "-'Profit and Loss'!" + 
          getExcelColumnLabel(i) + row_Num_E + "-'Profit and Loss'!" + getExcelColumnLabel(i) + row_Num_A + "-'Profit and Loss'!" + 
          getExcelColumnLabel(i) + row_Num_S);
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("PL_Check");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Total_Operating_Costs + 1) + "-'Profit and Loss'!" + 
          getExcelColumnLabel(i) + row_Num_MH + "-'Profit and Loss'!" + 
          getExcelColumnLabel(i) + row_Num_DL + "-'Profit and Loss'!" + getExcelColumnLabel(i) + row_Num_IL + "-'Profit and Loss'!" + 
          getExcelColumnLabel(i) + row_Num_Dep_M + "-'Profit and Loss'!" + 
          getExcelColumnLabel(i) + row_Num_OMC + "-'Profit and Loss'!" + 
          getExcelColumnLabel(i) + row_Num_E + "-'Profit and Loss'!" + getExcelColumnLabel(i) + row_Num_A + "-'Profit and Loss'!" + 
          getExcelColumnLabel(i) + row_Num_S);
        cell.setCellStyle(cellStyle);
      }
    }
    sheet_OC.createFreezePane(3, 2);
    for (int i = 0; i < col_Num; i++) {
      if (i <= 2) {
        sheet_OC.autoSizeColumn(i);
      } else {
        sheet_OC.setColumnWidth(0, 1280);
      }
    }
    sheet_OC.setDisplayGridlines(false);
    
    sheet_OC.addMergedRegion(new CellRangeAddress(0, 0, 0, 16));
    
    Sheet sheet_BS = workbook.createSheet("Balance Sheet");
    String[] row_TitleBS = { "", "Description", "Accouts", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Adj", "Final" };
    
    row = sheet_BS.createRow(0);
    cell = row.createCell(0);
    cell.setCellValue("Balance Sheet");
    String[] BorderBS = { "Top", "Bottom", "Right", "Left" };
    cellStyle = reportSetStyle(workbook, "Calibri", "BOLD", "CENTER", "", BorderBS, "");
    cell.setCellStyle(cellStyle);
    cell = row.createCell(16);
    cell.setCellStyle(cellStyle);
    
    row = sheet_BS.createRow(1);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "BOLD", "CENTER", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue(row_TitleBS[i]);
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "BOLD", "CENTER", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue(row_TitleBS[i]);
        cell.setCellStyle(cellStyle);
      }
    }
    row_Num_Start = 2;
    int row_Num_Land = reportBSSQL(workbook, stmt, Year, CompanyCode, "Land/Building", Currency, row_Num_Start, sheet_BS, "+", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_Land;
    int row_Num_Machinery = reportBSSQL(workbook, stmt, Year, CompanyCode, "Machinery/Equipment", Currency, row_Num_Start, sheet_BS, "+", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_Machinery;
    int row_Num_Tools = reportBSSQL(workbook, stmt, Year, CompanyCode, "Tools/Devices", Currency, row_Num_Start, sheet_BS, "+", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_Tools;
    int row_Num_Other_Fixed_Assets = reportBSSQL(workbook, stmt, Year, CompanyCode, "Other Fixed Assets", Currency, row_Num_Start, sheet_BS, "+", Period, row_Num_IBT + 1);
    
    int row_Num_Fixed_Assets = row_Num_Other_Fixed_Assets;
    row = sheet_BS.createRow((short)row_Num_Fixed_Assets);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Land + "+" + getExcelColumnLabel(i) + row_Num_Machinery + "+" + 
          getExcelColumnLabel(i) + row_Num_Tools + "+" + 
          getExcelColumnLabel(i) + row_Num_Other_Fixed_Assets);
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Fixed Assets");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Land + "+" + getExcelColumnLabel(i) + row_Num_Machinery + "+" + 
          getExcelColumnLabel(i) + row_Num_Tools + "+" + 
          getExcelColumnLabel(i) + row_Num_Other_Fixed_Assets);
        cell.setCellStyle(cellStyle);
      }
    }
    row_Num_Start = row_Num_Fixed_Assets + 1;
    int row_Num_Rawmaterial = reportBSSQL(workbook, stmt, Year, CompanyCode, "Rawmaterial/Parts", Currency, row_Num_Start, sheet_BS, "+", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_Rawmaterial;
    int row_Num_WiP = reportBSSQL(workbook, stmt, Year, CompanyCode, "WiP/FG", Currency, row_Num_Start, sheet_BS, "+", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_WiP;
    int row_Num_Engineering = reportBSSQL(workbook, stmt, Year, CompanyCode, "Engineering Services", Currency, row_Num_Start, sheet_BS, "+", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_Engineering;
    int row_Num_Devices = reportBSSQL(workbook, stmt, Year, CompanyCode, "Tools/Devices/Samples", Currency, row_Num_Start, sheet_BS, "+", Period, row_Num_IBT + 1);
    
    int row_Num_Inventory = row_Num_Devices;
    row = sheet_BS.createRow((short)row_Num_Inventory);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Rawmaterial + "+" + getExcelColumnLabel(i) + row_Num_WiP + "+" + 
          getExcelColumnLabel(i) + row_Num_Engineering + "+" + 
          getExcelColumnLabel(i) + row_Num_Devices);
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Inventory");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Rawmaterial + "+" + getExcelColumnLabel(i) + row_Num_WiP + "+" + 
          getExcelColumnLabel(i) + row_Num_Engineering + "+" + 
          getExcelColumnLabel(i) + row_Num_Devices);
        cell.setCellStyle(cellStyle);
      }
    }
    row_Num_Start = row_Num_Inventory + 1;
    int row_Num_AR = reportBSSQL(workbook, stmt, Year, CompanyCode, "Accounts Receivable", Currency, row_Num_Start, sheet_BS, "+", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_AR;
    int row_Num_GR = reportBSSQL(workbook, stmt, Year, CompanyCode, "Group Receivables", Currency, row_Num_Start, sheet_BS, "+", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_GR;
    int row_Num_CR = reportBSSQL(workbook, stmt, Year, CompanyCode, "Clearing Receivables", Currency, row_Num_Start, sheet_BS, "+", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_CR;
    int row_Num_Bank = reportBSSQL(workbook, stmt, Year, CompanyCode, "Bank Balances and Cash", Currency, row_Num_Start, sheet_BS, "+", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_Bank;
    int row_Num_Other_Current_Assets = reportBSSQL(workbook, stmt, Year, CompanyCode, "Other Current Assets", Currency, row_Num_Start, sheet_BS, "+", Period, row_Num_IBT + 1);
    
    int row_Num_Current_Assets = row_Num_Other_Current_Assets;
    row = sheet_BS.createRow((short)row_Num_Current_Assets);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_AR + "+" + getExcelColumnLabel(i) + row_Num_GR + "+" + 
          getExcelColumnLabel(i) + row_Num_CR + "+" + getExcelColumnLabel(i) + row_Num_Bank + "+" + 
          getExcelColumnLabel(i) + row_Num_Other_Current_Assets);
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Current Assets");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_AR + "+" + getExcelColumnLabel(i) + row_Num_GR + "+" + 
          getExcelColumnLabel(i) + row_Num_CR + "+" + getExcelColumnLabel(i) + row_Num_Bank + "+" + 
          getExcelColumnLabel(i) + row_Num_Other_Current_Assets);
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_Total_Assets = row_Num_Current_Assets + 1;
    row = sheet_BS.createRow((short)row_Num_Total_Assets);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Fixed_Assets + 1) + "+" + getExcelColumnLabel(i) + (row_Num_Inventory + 1) + "+" + 
          getExcelColumnLabel(i) + (row_Num_Current_Assets + 1));
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Total Assets");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Fixed_Assets + 1) + "+" + getExcelColumnLabel(i) + (row_Num_Inventory + 1) + "+" + 
          getExcelColumnLabel(i) + (row_Num_Current_Assets + 1));
        cell.setCellStyle(cellStyle);
      }
    }
    row_Num_Start = row_Num_Total_Assets + 1;
    int row_Num_Share_Capital = reportBSSQL(workbook, stmt, Year, CompanyCode, "Share Capital", Currency, row_Num_Start, sheet_BS, "-", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_Share_Capital;
    int row_Num_Capital_Reserve = reportBSSQL(workbook, stmt, Year, CompanyCode, "Capital Reserve", Currency, row_Num_Start, sheet_BS, "-", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_Capital_Reserve;
    int row_Num_previous_years = reportBSSQL(workbook, stmt, Year, CompanyCode, "Profit/Loss previous years", Currency, row_Num_Start, sheet_BS, "-", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_previous_years;
    int row_Num_Profit = reportBSSQL(workbook, stmt, Year, CompanyCode, "Profit/Loss", Currency, row_Num_Start, sheet_BS, "-", Period, row_Num_IBT + 1);
    
    int row_Num_Equity_Capital = row_Num_Profit;
    row = sheet_BS.createRow((short)row_Num_Equity_Capital);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Share_Capital + "+" + getExcelColumnLabel(i) + row_Num_Capital_Reserve + "+" + 
          getExcelColumnLabel(i) + row_Num_previous_years + "+" + 
          getExcelColumnLabel(i) + row_Num_Profit);
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Equity Capital");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Share_Capital + "+" + getExcelColumnLabel(i) + row_Num_Capital_Reserve + "+" + 
          getExcelColumnLabel(i) + row_Num_previous_years + "+" + 
          getExcelColumnLabel(i) + row_Num_Profit);
        cell.setCellStyle(cellStyle);
      }
    }
    row_Num_Start = row_Num_Equity_Capital + 1;
    int row_Num_Provisions_for_Retirements = reportBSSQL(workbook, stmt, Year, CompanyCode, "Provisions for Retirements", Currency, row_Num_Start, sheet_BS, "-", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_Provisions_for_Retirements;
    int row_Num_Longterm_Bank_Loans = reportBSSQL(workbook, stmt, Year, CompanyCode, "Longterm Bank Loans", Currency, row_Num_Start, sheet_BS, "-", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_Longterm_Bank_Loans;
    int row_Num_LK_Loan = reportBSSQL(workbook, stmt, Year, CompanyCode, "LK-Loan", Currency, row_Num_Start, sheet_BS, "-", Period, row_Num_IBT + 1);
    
    int row_Num_Longterm_Liabilities = row_Num_LK_Loan;
    row = sheet_BS.createRow((short)row_Num_Longterm_Liabilities);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(
          getExcelColumnLabel(i) + row_Num_Provisions_for_Retirements + "+" + getExcelColumnLabel(i) + row_Num_Longterm_Bank_Loans + "+" + 
          getExcelColumnLabel(i) + row_Num_LK_Loan);
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Longterm Liabilities");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(
          getExcelColumnLabel(i) + row_Num_Provisions_for_Retirements + "+" + getExcelColumnLabel(i) + row_Num_Longterm_Bank_Loans + "+" + 
          getExcelColumnLabel(i) + row_Num_LK_Loan);
        cell.setCellStyle(cellStyle);
      }
    }
    row_Num_Start = row_Num_Longterm_Liabilities + 1;
    int row_Num_AP = reportBSSQL(workbook, stmt, Year, CompanyCode, "Accounts Payables", Currency, row_Num_Start, sheet_BS, "-", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_AP;
    int row_Num_GP = reportBSSQL(workbook, stmt, Year, CompanyCode, "Group Payables", Currency, row_Num_Start, sheet_BS, "-", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_GP;
    int row_Num_CP = reportBSSQL(workbook, stmt, Year, CompanyCode, "Clearing Payables", Currency, row_Num_Start, sheet_BS, "-", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_CP;
    int row_Num_Shortterm_Bank_Loans = reportBSSQL(workbook, stmt, Year, CompanyCode, "Shortterm Bank Loans", Currency, row_Num_Start, sheet_BS, "-", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_Shortterm_Bank_Loans;
    int row_Num_Shortterm_Provisions = reportBSSQL(workbook, stmt, Year, CompanyCode, "Shortterm Provisions", Currency, row_Num_Start, sheet_BS, "-", Period, row_Num_IBT + 1);
    
    row_Num_Start = row_Num_Shortterm_Provisions;
    int row_Num_Other_Shortterm_Liabilities = reportBSSQL(workbook, stmt, Year, CompanyCode, "Other Shortterm Liabilities", Currency, row_Num_Start, sheet_BS, "-", Period, row_Num_IBT + 1);
    
    int row_Num_Shortterm_Liabilities = row_Num_Other_Shortterm_Liabilities;
    row = sheet_BS.createRow((short)row_Num_Shortterm_Liabilities);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_AP + "+" + getExcelColumnLabel(i) + row_Num_GP + "+" + 
          getExcelColumnLabel(i) + row_Num_CP + "+" + getExcelColumnLabel(i) + row_Num_Shortterm_Bank_Loans + "+" + 
          getExcelColumnLabel(i) + row_Num_Shortterm_Provisions + "+" + 
          getExcelColumnLabel(i) + row_Num_Other_Shortterm_Liabilities);
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Shortterm Liabilities");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_AP + "+" + getExcelColumnLabel(i) + row_Num_GP + "+" + 
          getExcelColumnLabel(i) + row_Num_CP + "+" + getExcelColumnLabel(i) + row_Num_Shortterm_Bank_Loans + "+" + 
          getExcelColumnLabel(i) + row_Num_Shortterm_Provisions + "+" + 
          getExcelColumnLabel(i) + row_Num_Other_Shortterm_Liabilities);
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_Total_Equity = row_Num_Shortterm_Liabilities + 1;
    row = sheet_BS.createRow((short)row_Num_Total_Equity);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Equity_Capital + 1) + "+" + getExcelColumnLabel(i) + (row_Num_Longterm_Liabilities + 1) + "+" + 
          getExcelColumnLabel(i) + (row_Num_Shortterm_Liabilities + 1));
        
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("Total Equity and Liabilities");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Equity_Capital + 1) + "+" + getExcelColumnLabel(i) + (row_Num_Longterm_Liabilities + 1) + "+" + 
          getExcelColumnLabel(i) + (row_Num_Shortterm_Liabilities + 1));
        
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_BS_Check = row_Num_Total_Equity + 1;
    row = sheet_BS.createRow((short)row_Num_BS_Check);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Total_Equity + 1) + "-" + getExcelColumnLabel(i) + (row_Num_Total_Assets + 1));
        
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("BS_Check");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Total_Equity + 1) + "-" + getExcelColumnLabel(i) + (row_Num_Total_Assets + 1));
        
        cell.setCellStyle(cellStyle);
      }
    }
    row_Num_Start = row_Num_BS_Check + 2;
    int row_Num_Invest_L = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Invest Land/Building", Currency, row_Num_Start, sheet_BS);
    
    row_Num_Start = row_Num_Invest_L;
    int row_Num_Invest_M = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Invest Machinery/Equipment", Currency, row_Num_Start, sheet_BS);
    
    row_Num_Start = row_Num_Invest_M;
    int row_Num_Invest_T = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Invest Tools/Devices", Currency, row_Num_Start, sheet_BS);
    
    row_Num_Start = row_Num_Invest_T;
    int row_Num_Invest_E = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Invest Engineering Services", Currency, row_Num_Start, sheet_BS);
    
    row_Num_Start = row_Num_Invest_E;
    int row_Num_Invest_O = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Invest Other Fixed Assets", Currency, row_Num_Start, sheet_BS);
    
    row_Num_Start = row_Num_Invest_O;
    int row_Num_Invest_L_IC = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Invest Land/Building IC", Currency, row_Num_Start, sheet_BS);
    
    row_Num_Start = row_Num_Invest_L_IC;
    int row_Num_Invest_M_IC = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Invest Machinery/Equipment IC", Currency, row_Num_Start, sheet_BS);
    
    row_Num_Start = row_Num_Invest_M_IC;
    int row_Num_Invest_T_IC = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Invest Tools/Devices IC", Currency, row_Num_Start, sheet_BS);
    
    row_Num_Start = row_Num_Invest_T_IC;
    int row_Num_Invest_E_IC = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Invest Engineering Services IC", Currency, row_Num_Start, sheet_BS);
    
    row_Num_Start = row_Num_Invest_E_IC;
    int row_Num_Invest_O_IC = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Invest Other Fixed Assets IC", Currency, row_Num_Start, sheet_BS);
    
    int row_Num_Invest_L_3rd = row_Num_Invest_O_IC;
    row = sheet_BS.createRow((short)row_Num_Invest_L_3rd);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Invest_L + "-" + getExcelColumnLabel(i) + row_Num_Invest_L_IC);
        
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("Invest Land/Building 3rd");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Invest_L + "-" + getExcelColumnLabel(i) + row_Num_Invest_L_IC);
        
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_Invest_M_3rd = row_Num_Invest_L_3rd + 1;
    row = sheet_BS.createRow((short)row_Num_Invest_M_3rd);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Invest_M + "-" + getExcelColumnLabel(i) + row_Num_Invest_M_IC);
        
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("Invest Machinery/Equipment 3rd");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Invest_M + "-" + getExcelColumnLabel(i) + row_Num_Invest_M_IC);
        
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_Invest_T_3rd = row_Num_Invest_M_3rd + 1;
    row = sheet_BS.createRow((short)row_Num_Invest_T_3rd);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Invest_T + "-" + getExcelColumnLabel(i) + row_Num_Invest_T_IC);
        
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("Invest Tools/Devices 3rd");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Invest_T + "-" + getExcelColumnLabel(i) + row_Num_Invest_T_IC);
        
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_Invest_E_3rd = row_Num_Invest_T_3rd + 1;
    row = sheet_BS.createRow((short)row_Num_Invest_E_3rd);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Invest_E + "-" + getExcelColumnLabel(i) + row_Num_Invest_E_IC);
        
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("Invest Engineering Services 3rd");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Invest_E + "-" + getExcelColumnLabel(i) + row_Num_Invest_E_IC);
        
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_Invest_O_3rd = row_Num_Invest_E_3rd + 1;
    row = sheet_BS.createRow((short)row_Num_Invest_O_3rd);

    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Invest_O + "-" + getExcelColumnLabel(i) + row_Num_Invest_O_IC);
        
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("Invest Other Fixed Assets 3rd");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Invest_O + "-" + getExcelColumnLabel(i) + row_Num_Invest_O_IC);
        
        cell.setCellStyle(cellStyle);
      }
    }
    row_Num_Start = row_Num_Invest_O_3rd + 1;
    int row_Num_RInvest_L = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Retirement Land/Building", Currency, row_Num_Start, sheet_BS);
    
    row_Num_Start = row_Num_RInvest_L;
    int row_Num_RInvest_M = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Retirement Machinery/Equipment", Currency, row_Num_Start, sheet_BS);
    
    row_Num_Start = row_Num_RInvest_M;
    int row_Num_RInvest_T = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Retirement Tools/Devices", Currency, row_Num_Start, sheet_BS);
    
    row_Num_Start = row_Num_RInvest_T;
    int row_Num_RInvest_E = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Retirement Engineering Services", Currency, row_Num_Start, sheet_BS);
    
    row_Num_Start = row_Num_RInvest_E;
    int row_Num_RInvest_O = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Retirement Other Fixed Assets", Currency, row_Num_Start, sheet_BS);
    
    row_Num_Start = row_Num_RInvest_O;
    int row_Num_RInvest_L_IC = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Retirement Land/Building IC", Currency, row_Num_Start, sheet_BS);
    
    row_Num_Start = row_Num_RInvest_L_IC;
    int row_Num_RInvest_M_IC = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Retirement Machinery/Equipment IC", Currency, row_Num_Start, sheet_BS);
    
    row_Num_Start = row_Num_RInvest_M_IC;
    int row_Num_RInvest_T_IC = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Retirement Tools/Devices IC", Currency, row_Num_Start, sheet_BS);
    
    row_Num_Start = row_Num_RInvest_T_IC;
    int row_Num_RInvest_E_IC = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Retirement Engineering Services IC", Currency, row_Num_Start, sheet_BS);
    
    row_Num_Start = row_Num_RInvest_E_IC;
    int row_Num_RInvest_O_IC = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "Retirement Other Fixed Assets IC", Currency, row_Num_Start, sheet_BS);
    
    int row_Num_RInvest_L_3rd = row_Num_RInvest_O_IC;
    row = sheet_BS.createRow((short)row_Num_RInvest_L_3rd);

    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_RInvest_L + "-" + getExcelColumnLabel(i) + row_Num_RInvest_L_IC);
        
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("Retirement Land/Building 3rd");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_RInvest_L + "-" + getExcelColumnLabel(i) + row_Num_RInvest_L_IC);
        
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_RInvest_M_3rd = row_Num_RInvest_L_3rd + 1;
    row = sheet_BS.createRow((short)row_Num_RInvest_M_3rd);

    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_RInvest_M + "-" + getExcelColumnLabel(i) + row_Num_RInvest_M_IC);
        
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("Retirement Machinery/Equipment 3rd");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_RInvest_M + "-" + getExcelColumnLabel(i) + row_Num_RInvest_M_IC);
        
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_RInvest_T_3rd = row_Num_RInvest_M_3rd + 1;
    row = sheet_BS.createRow((short)row_Num_RInvest_T_3rd);

    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_RInvest_T + "-" + getExcelColumnLabel(i) + row_Num_RInvest_T_IC);
        
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("Retirement Tools/Devices 3rd");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_RInvest_T + "-" + getExcelColumnLabel(i) + row_Num_RInvest_T_IC);
        
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_RInvest_E_3rd = row_Num_RInvest_T_3rd + 1;
    row = sheet_BS.createRow((short)row_Num_RInvest_E_3rd);

    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_RInvest_E + "-" + getExcelColumnLabel(i) + row_Num_RInvest_E_IC);
        
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("Retirement Engineering Services 3rd");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_RInvest_E + "-" + getExcelColumnLabel(i) + row_Num_RInvest_E_IC);
        
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_RInvest_O_3rd = row_Num_RInvest_E_3rd + 1;
    row = sheet_BS.createRow((short)row_Num_RInvest_O_3rd);
 
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_RInvest_O + "-" + getExcelColumnLabel(i) + row_Num_RInvest_O_IC);
        
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("Retirement Other Fixed Assets 3rd");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_RInvest_O + "-" + getExcelColumnLabel(i) + row_Num_RInvest_O_IC);
        
        cell.setCellStyle(cellStyle);
      }
    }
    row = sheet_BS.createRow((short)row_Num_Invest_L - 2);
    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "BOLD", "CENTER", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "BOLD", "CENTER", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_CAPITAL_EXPENDITURES = row_Num_RInvest_O_3rd + 1;
    row = sheet_BS.createRow((short)row_Num_CAPITAL_EXPENDITURES);

    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top", "Bottom", "Right", "Left" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Invest_L + "+" + getExcelColumnLabel(i) + row_Num_Invest_M + "+" + 
          getExcelColumnLabel(i) + row_Num_Invest_T + "+" + 
          getExcelColumnLabel(i) + row_Num_Invest_E + "+" + getExcelColumnLabel(i) + row_Num_Invest_O + "+" + 
          getExcelColumnLabel(i) + row_Num_RInvest_L + "+" + 
          getExcelColumnLabel(i) + row_Num_RInvest_M + "+" + getExcelColumnLabel(i) + row_Num_RInvest_T + "+" + 
          getExcelColumnLabel(i) + row_Num_RInvest_E + "+" + 
          getExcelColumnLabel(i) + row_Num_RInvest_O);
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("CAPITAL EXPENDITURES");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top", "Bottom" };
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "YELLOW");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + row_Num_Invest_L + "+" + getExcelColumnLabel(i) + row_Num_Invest_M + "+" + 
          getExcelColumnLabel(i) + row_Num_Invest_T + "+" + 
          getExcelColumnLabel(i) + row_Num_Invest_E + "+" + getExcelColumnLabel(i) + row_Num_Invest_O + "+" + 
          getExcelColumnLabel(i) + row_Num_RInvest_L + "+" + 
          getExcelColumnLabel(i) + row_Num_RInvest_M + "+" + getExcelColumnLabel(i) + row_Num_RInvest_T + "+" + 
          getExcelColumnLabel(i) + row_Num_RInvest_E + "+" + 
          getExcelColumnLabel(i) + row_Num_RInvest_O);
        cell.setCellStyle(cellStyle);
      }
    }
    row_Num_Start = row_Num_CAPITAL_EXPENDITURES + 1;
    int row_Num_TOTAL_DEPRECIATION = reportInvestmentSQL(workbook, stmt, Year, CompanyCode, "TOTAL DEPRECIATION", Currency, row_Num_Start, sheet_BS);
    
    row = sheet_BS.createRow((short)row_Num_TOTAL_DEPRECIATION);

    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "BOLD", "CENTER", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = { "Top" };
        cellStyle = reportSetStyle(workbook, "Calibri", "BOLD", "CENTER", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_Inventory_N = row_Num_TOTAL_DEPRECIATION + 1;
    row = sheet_BS.createRow((short)row_Num_Inventory_N);

    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Inventory + 1) + "*0.05/12");
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("INVENTORY");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Inventory + 1) + "*0.05/12");
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_Other_N = row_Num_Inventory_N + 1;
    row = sheet_BS.createRow((short)row_Num_Other_N);

    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula("(" + getExcelColumnLabel(i) + (row_Num_Current_Assets + 1) + "-" + 
          getExcelColumnLabel(i) + row_Num_Bank + "-" + 
          getExcelColumnLabel(i) + (row_Num_Shortterm_Liabilities + 1) + ")*0.05/12");
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("Other Asset and Liabilities");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula("(" + getExcelColumnLabel(i) + (row_Num_Current_Assets + 1) + "-" + 
          getExcelColumnLabel(i) + row_Num_Bank + "-" + 
          getExcelColumnLabel(i) + (row_Num_Shortterm_Liabilities + 1) + ")*0.05/12");
        cell.setCellStyle(cellStyle);
      }
    }
    int row_Num_T = row_Num_Other_N + 1;
    row = sheet_BS.createRow((short)row_Num_T);

    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Inventory_N + 1) + "+" + 
          getExcelColumnLabel(i) + (row_Num_Other_N + 1));
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("TOTAL NOMINAL INTEREST");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_Inventory_N + 1) + "+" + 
          getExcelColumnLabel(i) + (row_Num_Other_N + 1));
        cell.setCellStyle(cellStyle);
      }
    }
    row_Num_Start = row_Num_T + 2;
    int row_Num_Asset_Balance = reportAssetBalanceSQL(workbook, stmt, Year, CompanyCode, "Assets Balance", Currency, row_Num_Start, sheet_BS);
    
    int row_Num_F_Check = row_Num_Asset_Balance;
    row = sheet_BS.createRow((short)row_Num_F_Check);

    for (int i = 0; i <= col_Num; i++) {
      if (i == col_Num)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_CAPITAL_EXPENDITURES + 1) + "-" + 
          getExcelColumnLabel(i) + row_Num_TOTAL_DEPRECIATION + "-" + getExcelColumnLabel(i) + row_Num_Asset_Balance);
        
        cell.setCellStyle(cellStyle);
      }
      else if (i == 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("Check Investment");
        cell.setCellStyle(cellStyle);
      }
      else if (i < 2)
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border2, "");
        cell = row.createCell(i);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
      }
      else
      {
        String[] Border2 = new String[0];
        cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
        cell = row.createCell(i);
        cell.setCellFormula(getExcelColumnLabel(i) + (row_Num_CAPITAL_EXPENDITURES + 1) + "-" + 
          getExcelColumnLabel(i) + row_Num_TOTAL_DEPRECIATION + "-" + getExcelColumnLabel(i) + row_Num_Asset_Balance);
        
        cell.setCellStyle(cellStyle);
      }
    }
    sheet_BS.createFreezePane(3, 2);
    for (int i = 0; i < col_Num; i++) {
      if (i <= 2) {
        sheet_BS.autoSizeColumn(i);
      } else {
        sheet_BS.setColumnWidth(0, 1280);
      }
    }
    sheet_BS.setDisplayGridlines(false);
    
    sheet_BS.addMergedRegion(new CellRangeAddress(0, 0, 0, 16));
    
    System.out.println("END");
    
    FileOutputStream FOut = new FileOutputStream(expfile);
    workbook.write(FOut);
    FOut.flush();
    FOut.close();
    stmt.close();
    conn.close();
  }

	//自定义样式的函数
	public static CellStyle reportSetStyle(Workbook wb, String FontName, String Boldweight, String Alignment, String DataFormat, String[] Border, String BackgroundColor)
  {
    CellStyle cellStyle = wb.createCellStyle();
    XSSFFont font = (XSSFFont)wb.createFont();
    XSSFDataFormat format = (XSSFDataFormat)wb.createDataFormat();
    switch (FontName)
    {
    case "Calibri": 
      font.setFontName("Calibri");
    }
    if (Boldweight.equals("BOLD")) {
      font.setBoldweight((short)700);
    }
    switch (Alignment)
    {
    case "CENTER": 
      cellStyle.setAlignment((short)2);
      break;
    case "LEFT": 
      cellStyle.setAlignment((short)1);
      break;
    case "RIGHT": 
      cellStyle.setAlignment((short)3);
    }
    cellStyle.setDataFormat(format.getFormat(DataFormat));
    for (int i = 0; i < Border.length; i++) {
      switch (Border[i])
      {
      case "Top": 
        cellStyle.setBorderTop((short)2);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        break;
      case "Bottom": 
        cellStyle.setBorderBottom((short)2);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        break;
      case "Right": 
        cellStyle.setBorderRight((short)2);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        break;
      case "Left": 
        cellStyle.setBorderLeft((short)2);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
      }
    }
    cellStyle.setFont(font);
    switch (BackgroundColor)
    {
    case "YELLOW": 
      cellStyle.setFillForegroundColor((short)13);
      cellStyle.setFillPattern((short)1);
    }
    return cellStyle;
  }

	//查询投资的函数
	public static int reportInvestmentSQL(Workbook workbook, Statement stmt, String Year, String CompanyCode,
			String IRT_Structure, String Currency, int row_Num_Start, Sheet sheet) throws SQLException {
		int row_Num = row_Num_Start;
		int col_Num = 16;

		String sql_select = "SELECT\r\nbasic_info_investment_by_period.`IRT_Structure`,\r\nbasic_info_investment_by_period.Jan,\r\nbasic_info_investment_by_period.Feb,\r\nbasic_info_investment_by_period.Mar,\r\nbasic_info_investment_by_period.Apr,\r\nbasic_info_investment_by_period.May,\r\nbasic_info_investment_by_period.Jun,\r\nbasic_info_investment_by_period.Jul,\r\nbasic_info_investment_by_period.Aug,\r\nbasic_info_investment_by_period.Sep,\r\nbasic_info_investment_by_period.Oct,\r\nbasic_info_investment_by_period.Nov,\r\nbasic_info_investment_by_period.`Dec`,\r\nbasic_info_investment_by_period.Adj\r\nFROM\r\nbasic_info_investment_by_period\r\nWHERE\r\nbasic_info_investment_by_period.`Year` = "
				+ Year + " AND\r\nbasic_info_investment_by_period.CoCd = " + CompanyCode
				+ " AND\r\nbasic_info_investment_by_period.`Crcy` = '" + Currency
				+ "' AND\r\nbasic_info_investment_by_period.`IRT_Structure` = '" + IRT_Structure + "'";

		ResultSet rs = stmt.executeQuery(sql_select);
		Row row = sheet.createRow((short) row_Num);
		Cell cell = null;
		while (rs.next()) {
			row = sheet.createRow((short) row_Num);
			for (int i = 2; i <= col_Num; i++) {
				if (i == col_Num) {
					cell = row.createCell(i);
					String[] Border = { "Right", "Left" };
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
					cell.setCellFormula("SUM(D" + (row_Num + 1) + ":P" + (row_Num + 1) + ")");
					cell.setCellStyle(cellStyle);
				} else if (i <= 2) {
					String[] Border = new String[0];
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border, "");
					cell = row.createCell(i);
					cell.setCellValue(rs.getString(i - 1));
					cell.setCellStyle(cellStyle);
				} else {
					String[] Border = new String[0];
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
					cell = row.createCell(i);
					cell.setCellValue(rs.getDouble(i - 1) / 1000.0D);
					cell.setCellStyle(cellStyle);
				}
			}
			row_Num += 1;
		}
		if (row_Num == row_Num_Start) {
			for (int i = 2; i <= col_Num; i++) {
				if (i == col_Num) {
					cell = row.createCell(i);
					cell.setCellFormula("SUM(D" + (row_Num + 1) + ":P" + (row_Num + 1) + ")");
					String[] Border = { "Right", "Left" };
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
					cell.setCellStyle(cellStyle);
				} else if (i <= 2) {
					cell = row.createCell(i);
					String[] Border = new String[0];
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border, "");
					cell.setCellValue(IRT_Structure);
					cell.setCellStyle(cellStyle);
				} else {
					cell = row.createCell(i);
					String[] Border = new String[0];
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
					cell.setCellValue(0.0D);
					cell.setCellStyle(cellStyle);
				}
			}
			row_Num += 1;
		}
		return row_Num;
	}

	//查询fixed asset发生额
	public static int reportAssetBalanceSQL(Workbook workbook, Statement stmt, String Year, String CompanyCode,
			String IRT_Structure, String Currency, int row_Num_Start, Sheet sheet) throws SQLException {
		int row_Num = row_Num_Start;
		int col_Num = 16;

		String sql_select = "\tSELECT\r\n  'Assets Balance' as `IRT Structure`,\r\n\tSUM(IF((basic_info_investment_check.`Posting period`= '1'),basic_info_investment_check.`Balance LC`,0))AS `Jan`,\r\n\tSUM(IF((basic_info_investment_check.`Posting period`= '2'),basic_info_investment_check.`Balance LC`,0))AS `Feb`,\r\n\tSUM(IF((basic_info_investment_check.`Posting period`= '3'),basic_info_investment_check.`Balance LC`,0))AS `Mar`,\r\n\tSUM(IF((basic_info_investment_check.`Posting period`= '4'),basic_info_investment_check.`Balance LC`,0))AS `Apr`,\r\n\tSUM(IF((basic_info_investment_check.`Posting period`= '5'),basic_info_investment_check.`Balance LC`,0))AS `May`,\r\n\tSUM(IF((basic_info_investment_check.`Posting period`= '6'),basic_info_investment_check.`Balance LC`,0))AS `Jun`,\r\n\tSUM(IF((basic_info_investment_check.`Posting period`= '7'),basic_info_investment_check.`Balance LC`,0))AS `Jul`,\r\n\tSUM(IF((basic_info_investment_check.`Posting period`= '8'),basic_info_investment_check.`Balance LC`,0))AS `Aug`,\r\n\tSUM(IF((basic_info_investment_check.`Posting period`= '9'),basic_info_investment_check.`Balance LC`,0))AS `Sep`,\r\n\tSUM(IF((basic_info_investment_check.`Posting period`= '10'),basic_info_investment_check.`Balance LC`,0))AS `Oct`,\r\n\tSUM(IF((basic_info_investment_check.`Posting period`= '11'),basic_info_investment_check.`Balance LC`,0))AS `Nov`,\r\n\tSUM(IF((basic_info_investment_check.`Posting period`= '12'),basic_info_investment_check.`Balance LC`,0))AS `Dec`,\r\n\tSUM(IF((basic_info_investment_check.`Posting period`= '13'),basic_info_investment_check.`Balance LC`,0))AS `Adj`\r\n\tFROM\r\n\tbasic_info_investment_check\r\n\tWHERE\r\n\tbasic_info_investment_check.`Company Code` = "
				+ CompanyCode + " AND\r\n\tbasic_info_investment_check.`Fiscal Year` = " + Year
				+ " AND\r\n\tbasic_info_investment_check.Currency = '" + Currency
				+ "'\tGROUP BY\r\n\tbasic_info_investment_check.`Company Code`,\r\n\tbasic_info_investment_check.`Fiscal Year`,\r\n\tbasic_info_investment_check.Currency";

		//System.out.println(sql_select);

		ResultSet rs = stmt.executeQuery(sql_select);
		Row row = sheet.createRow((short) row_Num);
		Cell cell = null;
		while (rs.next()) {
			row = sheet.createRow((short) row_Num);
			for (int i = 2; i <= col_Num; i++) {
				if (i == col_Num) {
					cell = row.createCell(i);
					String[] Border = new String[0];
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
					cell.setCellFormula("SUM(D" + (row_Num + 1) + ":P" + (row_Num + 1) + ")");
					cell.setCellStyle(cellStyle);
				} else if (i <= 2) {
					String[] Border = new String[0];
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border, "");
					cell = row.createCell(i);
					cell.setCellValue(rs.getString(i - 1));
					cell.setCellStyle(cellStyle);
				} else {
					String[] Border = new String[0];
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
					cell = row.createCell(i);
					cell.setCellValue(rs.getDouble(i - 1) / 1000.0D);
					cell.setCellStyle(cellStyle);
				}
			}
			row_Num += 1;
		}
		if (row_Num == row_Num_Start) {
			for (int i = 2; i <= col_Num; i++) {
				if (i == col_Num) {
					cell = row.createCell(i);
					cell.setCellFormula("SUM(D" + (row_Num + 1) + ":P" + (row_Num + 1) + ")");
					String[] Border = new String[0];
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
					cell.setCellStyle(cellStyle);
				} else if (i <= 2) {
					cell = row.createCell(i);
					String[] Border = new String[0];
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border, "");
					cell.setCellValue(IRT_Structure);
					cell.setCellStyle(cellStyle);
				} else {
					cell = row.createCell(i);
					String[] Border = new String[0];
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
					cell.setCellValue(0.0D);
					cell.setCellStyle(cellStyle);
				}
			}
			row_Num += 1;
		}
		return row_Num;
	}

	//查询PL structure
	public static int reportPLSQL(Workbook workbook, Statement stmt, String Year, String CompanyCode,
			String IRT_Structure, String Currency, int row_Num_Start, Sheet sheet) throws SQLException {
		int row_Num = row_Num_Start;
		int col_Num = 16;

		String sql_select = "SELECT\r\nbasic_info_plstructure_by_period.`IRT Structure`,\r\nbasic_info_plstructure_by_period.Jan,\r\nbasic_info_plstructure_by_period.Feb,\r\nbasic_info_plstructure_by_period.Mar,\r\nbasic_info_plstructure_by_period.Apr,\r\nbasic_info_plstructure_by_period.May,\r\nbasic_info_plstructure_by_period.Jun,\r\nbasic_info_plstructure_by_period.Jul,\r\nbasic_info_plstructure_by_period.Aug,\r\nbasic_info_plstructure_by_period.Sep,\r\nbasic_info_plstructure_by_period.Oct,\r\nbasic_info_plstructure_by_period.Nov,\r\nbasic_info_plstructure_by_period.`Dec`,\r\nbasic_info_plstructure_by_period.Adj\r\nFROM\r\nbasic_info_plstructure_by_period\r\nWHERE\r\nbasic_info_plstructure_by_period.`Year` = "
				+ Year + " AND\r\nbasic_info_plstructure_by_period.CoCd = " + CompanyCode
				+ " AND\r\nbasic_info_plstructure_by_period.`Local Crcy` = '" + Currency
				+ "' AND\r\nbasic_info_plstructure_by_period.`IRT Structure` = '" + IRT_Structure + "'";

		ResultSet rs = stmt.executeQuery(sql_select);
		Row row = sheet.createRow((short) row_Num);
		Cell cell = null;
		while (rs.next()) {
			row = sheet.createRow((short) row_Num);
			for (int i = 2; i <= col_Num; i++) {
				if (i == col_Num) {
					cell = row.createCell(i);
					String[] Border = { "Right", "Left" };
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
					cell.setCellFormula("SUM(D" + (row_Num + 1) + ":P" + (row_Num + 1) + ")");
					cell.setCellStyle(cellStyle);
				} else if (i <= 2) {
					String[] Border = new String[0];
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border, "");
					cell = row.createCell(i);
					cell.setCellValue(rs.getString(i - 1));
					cell.setCellStyle(cellStyle);
				} else {
					String[] Border = new String[0];
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
					cell = row.createCell(i);
					cell.setCellValue(rs.getDouble(i - 1) / 1000.0D);
					cell.setCellStyle(cellStyle);
				}
			}
			row_Num += 1;
		}
		if (row_Num == row_Num_Start) {
			for (int i = 2; i <= col_Num; i++) {
				if (i == col_Num) {
					cell = row.createCell(i);
					cell.setCellFormula("SUM(D" + (row_Num + 1) + ":P" + (row_Num + 1) + ")");
					String[] Border = { "Right", "Left" };
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
					cell.setCellStyle(cellStyle);
				} else if (i <= 2) {
					cell = row.createCell(i);
					String[] Border = new String[0];
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border, "");
					cell.setCellValue(IRT_Structure);
					cell.setCellStyle(cellStyle);
				} else {
					cell = row.createCell(i);
					String[] Border = new String[0];
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
					cell.setCellValue(0.0D);
					cell.setCellStyle(cellStyle);
				}
			}
			row_Num += 1;
		}
		return row_Num;
	}

	//查询账号发生额
	public static int reportSQL(Workbook workbook, Statement stmt, String Year, String CompanyCode,
			String IRT_Structure, String Currency, int row_Num_Start, Sheet sheet, String sign) throws SQLException {
		int row_Num = row_Num_Start;
		int col_Num = 16;

		String sql_select = "SELECT\r\nreport_irt_basic_info.`GL Description`,\r\nreport_irt_basic_info.`Account Number`,\r\nSum(if(`Posting period` = '1',"
				+ sign + "`Balance LC`,0)) AS `Jan`,\r\nSum(if(`Posting period` = '2'," + sign
				+ "`Balance LC`,0)) AS `Feb`,\r\nSum(if(`Posting period` = '3'," + sign
				+ "`Balance LC`,0)) AS `Mar`,\r\nSum(if(`Posting period` = '4'," + sign
				+ "`Balance LC`,0)) AS `Apr`,\r\nSum(if(`Posting period` = '5'," + sign
				+ "`Balance LC`,0)) AS `May`,\r\nSum(if(`Posting period` = '6'," + sign
				+ "`Balance LC`,0)) AS `Jun`,\r\nSum(if(`Posting period` = '7'," + sign
				+ "`Balance LC`,0)) AS `Jul`,\r\nSum(if(`Posting period` = '8'," + sign
				+ "`Balance LC`,0)) AS `Aug`,\r\nSum(if(`Posting period` = '9'," + sign
				+ "`Balance LC`,0)) AS `Sep`,\r\nSum(if(`Posting period` = '10'," + sign
				+ "`Balance LC`,0)) AS `Oct`,\r\nSum(if(`Posting period` = '11'," + sign
				+ "`Balance LC`,0)) AS `Nov`,\r\nSum(if(`Posting period` = '12'," + sign
				+ "`Balance LC`,0)) AS `Dec`,\r\nSum(if(`Posting period` = '13'," + sign
				+ "`Balance LC`,0)) AS `Adj`\r\nFROM\r\nreport_irt_basic_info\r\nWHERE\r\nreport_irt_basic_info.`Company Code` = "
				+ CompanyCode + " AND\r\nreport_irt_basic_info.`Fiscal Year` = " + Year
				+ " AND\r\nreport_irt_basic_info.`IRT Structure` = '" + IRT_Structure
				+ "' AND\r\nreport_irt_basic_info.Currency = '" + Currency
				+ "'GROUP BY\r\nreport_irt_basic_info.`Account Number`";

		ResultSet rs = stmt.executeQuery(sql_select);
		Row row = sheet.createRow((short) row_Num);
		Cell cell = null;
		while (rs.next()) {
			row = sheet.createRow((short) row_Num);
			cell = row.createCell(0);
			for (int i = 1; i <= col_Num; i++) {
				if (i == col_Num) {
					cell = row.createCell(i);
					String[] Border = { "Right", "Left" };
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
					cell.setCellFormula("SUM(D" + (row_Num + 1) + ":P" + (row_Num + 1) + ")");
					cell.setCellStyle(cellStyle);
				} else if (i <= 2) {
					cell = row.createCell(i);
					String[] Border = new String[0];
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border, "");
					cell.setCellValue(rs.getString(i));
					cell.setCellStyle(cellStyle);
				} else {
					cell = row.createCell(i);
					String[] Border = new String[0];
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
					cell.setCellValue(rs.getDouble(i) / 1000.0D);
					cell.setCellStyle(cellStyle);
				}
			}
			row_Num += 1;
		}
		if (row_Num != row_Num_Start) {
			row = sheet.createRow((short) row_Num);
			cell = row.createCell(2);
			String[] Border = new String[0];
			CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border, "");
			cell.setCellValue(IRT_Structure);
			cell.setCellStyle(cellStyle);
			for (int i = 3; i <= col_Num; i++) {
				if (i == col_Num) {
					cell = row.createCell(i);
					String[] Border2 = { "Right", "Left" };
					cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
					cell.setCellFormula("SUM(D" + (row_Num + 1) + ":P" + (row_Num + 1) + ")");
					cell.setCellStyle(cellStyle);
				} else {
					cell = row.createCell(i);
					String[] Border2 = new String[0];
					cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
					cell.setCellFormula("SUM(" + getExcelColumnLabel(i) + (row_Num_Start + 1) + ":"
							+ getExcelColumnLabel(i) + row_Num + ")");
					cell.setCellStyle(cellStyle);
				}
			}
			sheet.groupRow(row_Num_Start, row_Num - 1);
			return row_Num + 1;
		}
		for (int i = 2; i <= col_Num; i++) {
			if (i == col_Num) {
				cell = row.createCell(i);
				String[] Border2 = { "Right", "Left" };
				CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
				cell.setCellFormula("SUM(D" + (row_Num + 1) + ":P" + (row_Num + 1) + ")");
				cell.setCellStyle(cellStyle);
			} else if (i <= 2) {
				cell = row.createCell(i);
				String[] Border = new String[0];
				CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border, "");
				cell.setCellValue(IRT_Structure);
				cell.setCellStyle(cellStyle);
			} else {
				cell = row.createCell(i);
				String[] Border = new String[0];
				CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
				cell.setCellValue(0.0D);
				cell.setCellStyle(cellStyle);
			}
		}
		return row_Num + 1;
	}

	//查询账号余额，其中最后一个期间保持和最新期间一致
	public static int reportBSSQL(Workbook workbook, Statement stmt, String Year, String CompanyCode,
			String IRT_Structure, String Currency, int row_Num_Start, Sheet sheet, String sign, String Period,
			int Profit_Row_Num) throws SQLException {
		int row_Num = row_Num_Start;
		int col_Num = 16;
		int intperiod = Integer.parseInt(Period) + 2;

		String sql_select = "SELECT\r\nreport_irt_basic_info.`GL Description`,\r\nreport_irt_basic_info.`Account Number`,\r\nSum(if(`Posting period` = '1',"
				+ sign + "`Accumulated balance LC`,0)) AS `Jan`,\r\nSum(if(`Posting period` = '2'," + sign
				+ "`Accumulated balance LC`,0)) AS `Feb`,\r\nSum(if(`Posting period` = '3'," + sign
				+ "`Accumulated balance LC`,0)) AS `Mar`,\r\nSum(if(`Posting period` = '4'," + sign
				+ "`Accumulated balance LC`,0)) AS `Apr`,\r\nSum(if(`Posting period` = '5'," + sign
				+ "`Accumulated balance LC`,0)) AS `May`,\r\nSum(if(`Posting period` = '6'," + sign
				+ "`Accumulated balance LC`,0)) AS `Jun`,\r\nSum(if(`Posting period` = '7'," + sign
				+ "`Accumulated balance LC`,0)) AS `Jul`,\r\nSum(if(`Posting period` = '8'," + sign
				+ "`Accumulated balance LC`,0)) AS `Aug`,\r\nSum(if(`Posting period` = '9'," + sign
				+ "`Accumulated balance LC`,0)) AS `Sep`,\r\nSum(if(`Posting period` = '10'," + sign
				+ "`Accumulated balance LC`,0)) AS `Oct`,\r\nSum(if(`Posting period` = '11'," + sign
				+ "`Accumulated balance LC`,0)) AS `Nov`,\r\nSum(if(`Posting period` = '12'," + sign
				+ "`Accumulated balance LC`,0)) AS `Dec`,\r\nSum(if(`Posting period` = '13'," + sign
				+ "`Accumulated balance LC`,0)) AS `Adj`\r\nFROM\r\nreport_irt_basic_info\r\nWHERE\r\nreport_irt_basic_info.`Company Code` = "
				+ CompanyCode + " AND\r\nreport_irt_basic_info.`Fiscal Year` = " + Year
				+ " AND\r\nreport_irt_basic_info.`IRT Structure` = '" + IRT_Structure
				+ "' AND\r\nreport_irt_basic_info.Currency = '" + Currency
				+ "'GROUP BY\r\nreport_irt_basic_info.`Account Number`";

		ResultSet rs = stmt.executeQuery(sql_select);
		Row row = sheet.createRow((short) row_Num);
		Cell cell = null;
		while (rs.next()) {
			row = sheet.createRow((short) row_Num);
			cell = row.createCell(0);
			for (int i = 1; i <= col_Num; i++) {
				if (i == col_Num) {
					cell = row.createCell(i);
					String[] Border = { "Right", "Left" };
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
					cell.setCellValue(rs.getDouble(intperiod) / 1000.0D);
					cell.setCellStyle(cellStyle);
				} else if (i <= 2) {
					cell = row.createCell(i);
					String[] Border = new String[0];
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border, "");
					cell.setCellValue(rs.getString(i));
					cell.setCellStyle(cellStyle);
				} else {
					cell = row.createCell(i);
					String[] Border = new String[0];
					CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
					cell.setCellValue(rs.getDouble(i) / 1000.0D);
					cell.setCellStyle(cellStyle);
				}
			}
			row_Num += 1;
		}
		row = sheet.createRow((short) row_Num);
		if (IRT_Structure.equals("Profit/Loss")) {
			if (Currency.equals("EUR")) {
				JSONObject jsonObj = new JSONObject();
				jsonObj = RunIRTbyPeriod.getBalance(stmt, CompanyCode,
						"'Overtime','Royalties','GCSP','Direct Material','Engineering Cost Depreciation','Interest Costs','REVENUES','Other Costs','Consumables','Productive Wages','Overtime Premium','Other Fringe Benefits Wages','Unproductive Wages','Salaries','Other Fringe Benefits','Canteen','Energy','Repairwork (external)','Taxes/Duties','Leasing/Depreciation (Build.)','Other Depreciation','Travel and Subsistance','Other Leasing Costs','Surface Freight','Airfreight','Other Capitalized Costs','Change in Stockvalue','Other Income / Expenses','Exchange Gains/Losses','Interest Income / Expenses','Legal/Audit','Insurances','Telephone/Telex/Telefax/Mail'",
						"Profit/Loss", Currency, Year, -1);

				System.out.println(jsonObj);
				for (int i = 1; i <= col_Num; i++) {
					if (i == col_Num) {
						cell = row.createCell(i);
						String[] Border = { "Right", "Left" };
						CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
						cell.setCellFormula(getExcelColumnLabel(intperiod) + String.valueOf(row_Num + 1));
						cell.setCellStyle(cellStyle);
					} else if (i <= 2) {
						cell = row.createCell(i);
						String[] Border = new String[0];
						CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border, "");
						cell.setCellValue("Profit");
						cell.setCellStyle(cellStyle);
					} else if (i <= intperiod) {
						cell = row.createCell(i);
						String[] Border = new String[0];
						CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");

						System.out.println("iiiii:" + i);
						cell.setCellValue(jsonObj.getDouble(String.valueOf(i - 2)));
						cell.setCellStyle(cellStyle);
					} else {
						cell = row.createCell(i);
						String[] Border = new String[0];
						CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");

						cell.setCellValue(0.0D);
						cell.setCellStyle(cellStyle);
					}
				}
				row_Num += 1;
			} else {
				for (int i = 1; i <= col_Num; i++) {
					if (i == col_Num) {
						cell = row.createCell(i);
						String[] Border = { "Right", "Left" };
						CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
						cell.setCellFormula(getExcelColumnLabel(intperiod) + String.valueOf(row_Num + 1));
						cell.setCellStyle(cellStyle);
					} else if (i <= 2) {
						cell = row.createCell(i);
						String[] Border = new String[0];
						CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border, "");
						cell.setCellValue("Profit");
						cell.setCellStyle(cellStyle);
					} else if (i <= intperiod) {
						cell = row.createCell(i);
						String[] Border = new String[0];
						CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");

						cell.setCellFormula("SUM('Profit and Loss'!D" + Profit_Row_Num + ":" + getExcelColumnLabel(i)
								+ Profit_Row_Num + ")");
						cell.setCellStyle(cellStyle);
					} else {
						cell = row.createCell(i);
						String[] Border = new String[0];
						CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");

						cell.setCellValue(0.0D);
						cell.setCellStyle(cellStyle);
					}
				}
				row_Num += 1;
			}
		}
		if (row_Num != row_Num_Start) {
			row = sheet.createRow((short) row_Num);
			cell = row.createCell(2);
			String[] Border = new String[0];
			CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border, "");
			cell.setCellValue(IRT_Structure);
			cell.setCellStyle(cellStyle);
			for (int i = 3; i <= col_Num; i++) {
				if (i == col_Num) {
					cell = row.createCell(i);
					String[] Border2 = { "Right", "Left" };
					cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
					cell.setCellFormula("SUM(" + getExcelColumnLabel(i) + (row_Num_Start + 1) + ":"
							+ getExcelColumnLabel(i) + row_Num + ")");
					cell.setCellStyle(cellStyle);
				} else {
					cell = row.createCell(i);
					String[] Border2 = new String[0];
					cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
					cell.setCellFormula("SUM(" + getExcelColumnLabel(i) + (row_Num_Start + 1) + ":"
							+ getExcelColumnLabel(i) + row_Num + ")");
					cell.setCellStyle(cellStyle);
				}
			}
			sheet.groupRow(row_Num_Start, row_Num - 1);
			return row_Num + 1;
		}
		for (int i = 2; i <= col_Num; i++) {
			if (i == col_Num) {
				cell = row.createCell(i);
				String[] Border2 = { "Right", "Left" };
				CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border2, "");
				cell.setCellValue(0.0D);
				cell.setCellStyle(cellStyle);
			} else if (i <= 2) {
				cell = row.createCell(i);
				String[] Border = new String[0];
				CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "LEFT", "", Border, "");
				cell.setCellValue(IRT_Structure);
				cell.setCellStyle(cellStyle);
			} else {
				cell = row.createCell(i);
				String[] Border = new String[0];
				CellStyle cellStyle = reportSetStyle(workbook, "Calibri", "", "RIGHT", "#,##0", Border, "");
				cell.setCellValue(0.0D);
				cell.setCellStyle(cellStyle);
			}
		}
		return row_Num + 1;
	}

	//根据值获取列标签
	public static String getExcelColumnLabel(int iCol) {
		String strCol = "";
		int baseCol = 65 + iCol;
		if (baseCol > 90) {
			int i2 = 0;
			if ((baseCol - 90) / 26 > 0) {
				i2 = 65 + (baseCol - 90 - 1) / 26;
			} else {
				i2 = 65;
			}
			int i1 = (baseCol - 90 - 1) % 26;
			i1 = 65 + i1;

			strCol = String.valueOf((char) i2) + String.valueOf((char) i1);
		} else {
			strCol = String.valueOf((char) baseCol);
		}
		return strCol;
	}
}
