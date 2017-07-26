package ajaxInterface;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.paypal.LaunchApplication;

/**
 * Servlet implementation class UiServlet
 */
@WebServlet("/UiServlet")
public class UiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UiServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(request.getParameter("jiraid"));
		System.out.println(request.getParameter("buildid"));
		System.out.println(request.getParameter("startTime"));
		System.out.println(request.getParameter("endTime"));
		System.out.println(request.getParameter("oneboxmachine"));
		System.out.println(request.getParameter("email"));
		System.out.println(request.getParameter("prevdate"));
		System.out.println(request.getParameter("fulllog"));
		response.setContentType("application/json");
		try {
			if(LaunchApplication.isInProgress){
		        PrintWriter out = response.getWriter();
		        out.println("{");
		        out.println("\"Status\": \"300\",");
		        out.println("\"Message\": \"One box report generation already in progress.\"");
		        out.println("}");
		        out.close();
			}
			else{
				LaunchApplication.main1(request.getParameter("startTime"), request.getParameter("endTime"), request.getParameter("oneboxmachine"), request.getParameter("email"), request.getParameter("jiraid"), request.getParameter("buildid"), request.getParameter("prevdate"), Boolean.valueOf(request.getParameter("fulllog")));
				 PrintWriter out = response.getWriter();
			        out.println("{");
			        out.println("\"Status\": \"200\",");
			        out.println("\"Message\": \"One box report generation completed successfully.\"");
			        out.println("}");
			        out.close();
			}
		} catch (Exception e) {
			 PrintWriter out = response.getWriter();
		        out.println("{");
		        out.println("\"Status\": \"400\",");
		        out.println("\"Message\": \"Error: "+e.getMessage().split("\n")[0]+"\"");
		        out.println("}");
		        out.close();
		}
	
	}

}
