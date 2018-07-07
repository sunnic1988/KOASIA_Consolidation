package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import koasiaco.JsonSelectData;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;;

/**
 * Servlet implementation class AjaxCompanyCodeServlet
 * 获取Consolevel中对应的公司
 */
public class AjaxCompanyCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AjaxCompanyCodeServlet() {
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

		// 前台form提交过来的表名
		String Consolevel = request.getParameter("Consolevel");
		String sql_select = "Select `Company Code` FROM sys_conso_tp WHERE ConsoLevel = '" + Consolevel
				+ "' GROUP BY `Company Code`";
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();

		try {
			JSONArray companies = JsonSelectData.start(sql_select, "");

			//通过文本转，可以改进
			String ajaxResult = "";
			for (int i = 0; i < companies.size(); i++) {
				JSONObject jsonObj = companies.getJSONObject(i);
				String CompanyCode = jsonObj.getString("Company Code");
				ajaxResult = ajaxResult + "," + CompanyCode;
			}

			JSONObject outData = new JSONObject();
			ajaxResult = ajaxResult.substring(1, ajaxResult.length());
			outData.put("CompanyCode", ajaxResult);
			writer.println(outData);
			writer.flush();
			writer.close();
			System.out.println("return CompanyCode：" + ajaxResult);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
