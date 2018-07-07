package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import reports.RunIRT;

/**
 * Servlet implementation class ReportPlServlet
 * 查询profit and loss by company code的数据
 */
public class ReportPlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static String jsonRrequest = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReportPlServlet() {
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
		StringBuilder jsonString = new StringBuilder();
		BufferedReader reader = request.getReader();

		while ((jsonRrequest = reader.readLine()) != null) {
			jsonString.append(jsonRrequest).append('\n');
			System.out.println("Ajax JSON:" + jsonRrequest);

			// 创建Json对象，得到数据库名
			JSONObject json = JSONObject.fromObject(jsonRrequest);
			String companies = json.getString("CompanyCode");
			String Fiscal_Year = json.getString("FiscalYear");
			String Posting_period = json.getString("Postingperiod");
			String currency = json.getString("Currency");
			PrintWriter writer = response.getWriter();

			try {
				JSONObject obj = new JSONObject();
				JSONArray report = RunIRT.ProfitLoss(companies, currency, Fiscal_Year, Posting_period);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				// 调整成grid接收的JSON对象
				obj.put("Rows", report);
				writer.println(obj.toString());
				//writer.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
