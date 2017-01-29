package org.search.webProject;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

/**
 * Servlet implementation class PlayVideoServlet
 */
@WebServlet("/PlayVideoServlet")
public class PlayVideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PlayVideoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		File tempVideoFolder = new File(getServletContext().getInitParameter("temporary-video-location"));
		FileUtils.deleteDirectory(tempVideoFolder);
		tempVideoFolder.mkdir();
		File videoFile = new File(getServletContext().getInitParameter("data-location")+request.getParameter("file"));
		FileUtils.copyFileToDirectory(videoFile, tempVideoFolder);
		String VideoHTML = "";
		VideoHTML+="<br><br>";
		VideoHTML+="<video width=\"640\" height=\"360\" controls><source src=\"TempVideo\\"+request.getParameter("file").substring(request.getParameter("file").lastIndexOf("/")+1)+"\" type=\"video/mp4\" /></video>";
		VideoHTML+="<br>";
		VideoHTML+="<a href=\"index.jsp\" ><img alt=\"icon\" style=\"vertical-align: middle;\" src=\"images/Exit.png\"></a></td>";
		request.setAttribute("Video.path", VideoHTML);
		getServletContext().getRequestDispatcher("/PlayVideo.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
