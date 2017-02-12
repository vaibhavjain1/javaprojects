package taskerSrc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetUserServlet
 */
@WebServlet("/GetUserServlet")
public class GetUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUserServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBOperations obj = new DBOperations();
		if(request.getParameter("action").equalsIgnoreCase("login")){
			String responseString = String.valueOf(obj.login(request.getParameter("txtUserName"), request.getParameter("txtPassword")));
			response.setContentType("text/plain");
			response.getWriter().write(responseString);
		} else if(request.getParameter("action").equalsIgnoreCase("addtask")){
			String responseString = String.valueOf(obj.addUserTask(request.getParameter("loggedInUser"), request.getParameter("taskname"), request.getParameter("taskdescription"), request.getParameter("startTime"), request.getParameter("endTime"),request.getParameter("taskStatus"), request.getParameter("taskComment")));
			response.setContentType("text/plain");
			response.getWriter().write(responseString);
		} else if(request.getParameter("action").equalsIgnoreCase("newuser")){
			String responseString = String.valueOf(obj.addUser(request.getParameter("txtUserName"), request.getParameter("txtPassword")));
			response.setContentType("text/plain");
			response.getWriter().write(responseString);
		}else if(request.getParameter("action").equalsIgnoreCase("getusertasklist")){
			String responseString = obj.getUserTasks(request.getParameter("loggedInUser"));
			response.setContentType("text/plain");
			response.getWriter().write(responseString);
		}else if(request.getParameter("action").equalsIgnoreCase("gettaskdetail")){
			String responseString = obj.getTaskDetails(request.getParameter("taskId"));
			response.setContentType("text/plain");
			response.getWriter().write(responseString);
		}else if(request.getParameter("action").equalsIgnoreCase("UpdateTask")){
			String responseString = obj.updateTask(request.getParameter("taskId"), request.getParameter("taskname"), request.getParameter("taskdescription"), request.getParameter("startTime"), request.getParameter("endTime"),request.getParameter("taskStatus"),request.getParameter("taskComment"));
			response.setContentType("text/plain");
			response.getWriter().write(responseString);
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("do post got called");
	}

}
