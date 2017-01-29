package org.search.webProject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DownloadServlet
 */
@WebServlet("/DownloadServlet")
public class DownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public DownloadServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("APPLICATION/OCTET-STREAM");   
		response.setHeader("Content-Disposition","attachment; filename=\"" + request.getParameter("file").substring(request.getParameter("file").lastIndexOf("/")+1) + "\"");
		try {
			OutputStream out = response.getOutputStream();
			FileInputStream in = new FileInputStream(getServletContext().getInitParameter("data-location")+request.getParameter("file"));
			byte[] buffer = new byte[4096];
			int length;
			while ((length = in.read(buffer)) > 0){
			    out.write(buffer, 0, length);
			}
			in.close();
			out.flush();
		} catch (Exception e) {
			System.out.println("Exception: "+e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
