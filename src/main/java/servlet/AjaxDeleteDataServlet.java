package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import koasiaco.DeleteData;

/**
 * Servlet implementation class AjaxDeleteDataServlet
 * 删除功能，准备弃用
 */
public class AjaxDeleteDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AjaxDeleteDataServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


		// 获取前台的传过来的值，ajax要自己定义
		String Companies = request.getParameter("Company");
		String year = request.getParameter("Year");
		String period = request.getParameter("Period");

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();

		String Companies_report_irt_basic_info = "";
		String Companies_report_irt_basic_info_plstructure = "";

		if (Companies.contains(",")) {
			Companies_report_irt_basic_info = Companies.replace(",", " OR `Company Code` = ");
		} else {
			Companies_report_irt_basic_info = Companies;
		}

		if (Companies.contains(",")) {
			Companies_report_irt_basic_info_plstructure = Companies.replace(",", " OR `CoCd` = ");
		} else {
			Companies_report_irt_basic_info_plstructure = Companies;
		}

		String sql_del_report_irt_basic_info = "delete from report_irt_basic_info Where (`Company Code` = "
				+ Companies_report_irt_basic_info + ") AND `Fiscal Year` = " + year + " AND `Posting period` = "
				+ period;
		String report_irt_basic_info_plstructure = "delete from report_irt_basic_info_plstructure Where (`CoCd` = "
				+ Companies_report_irt_basic_info_plstructure + ") AND `Year` = " + year + " AND `Period` = " + period;

		try {
			DeleteData.normalStartl(sql_del_report_irt_basic_info);
			DeleteData.normalStartl(report_irt_basic_info_plstructure);
			JSONObject outData = new JSONObject();
			outData.put("ajaxResult", "Delete Success");
			writer.println(outData);
			writer.flush();
			writer.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JSONObject outData = new JSONObject();
			outData.put("ajaxResult", "Delete with Errors");
			writer.println(outData);
			writer.flush();
			writer.close();
			e.printStackTrace();
		}
	}
}
