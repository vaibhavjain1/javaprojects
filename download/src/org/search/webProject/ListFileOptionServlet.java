package org.search.webProject;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/ListFileOptionServlet")
public class ListFileOptionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public static String data_location ="";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListFileOptionServlet() {
        super();
    } 
    

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String HTML = "";
		File folder = new File(getServletContext().getInitParameter("data-location")+request.getParameter("type"));
		File[] listOfFiles = folder.listFiles();
		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isDirectory()) {
		        HTML+="<tr onmouseover=\"this.bgColor='#ffeeff';\" onmouseout=\"this.bgColor='#ffffff';\" bgcolor=\"#ffffff\">";
		        HTML+="\n";
		        HTML+="<td style=\"width: 48px; text-align: left;\"><img alt=\"icon\" style=\"vertical-align: middle;\" src=\"images/folder.png\"></td>";
		        HTML+="\n";
		        HTML+="<td><a href=\"ListFileOptionServlet?type="+request.getParameter("type")+"/"+listOfFiles[i].getName()+"\">"+listOfFiles[i].getName()+"</a></td>";
		        HTML+="\n";
		        HTML+="</tr>"; 
		        HTML+="\n";
		      } else if (listOfFiles[i].isFile()) {
		    	  String extension = listOfFiles[i].getName().substring(listOfFiles[i].getName().lastIndexOf(".")+1);  
		    	  HTML+="<tr onmouseover=\"this.bgColor='#ffeeff';\" onmouseout=\"this.bgColor='#ffffff';\" bgcolor=\"#ffffff\">";
		    	  HTML+="\n";
		    	  if(extension.equalsIgnoreCase("mp4")||extension.equalsIgnoreCase("webm")||extension.equalsIgnoreCase("mkv")||extension.equalsIgnoreCase("flv")||extension.equalsIgnoreCase("avi")||extension.equalsIgnoreCase("wmv")) {
		    		  HTML+="<td style=\"width: 48px; text-align: left;\"><img alt=\"icon\" style=\"vertical-align: middle;\" src=\"images/vlc.png\"></td>";
			    	  HTML+="\n";
		    	  } else {
		    		  HTML+="<td style=\"width: 48px; text-align: left;\"><img alt=\"icon\" style=\"vertical-align: middle;\" src=\"images/rar.png\"></td>";
		    		  HTML+="\n";
		    	  }
		    	  HTML+="<td><a href=\"DownloadServlet?file="+request.getParameter("type")+"/"+listOfFiles[i].getName()+"\">"+listOfFiles[i].getName()+"</a></td>";
		    	  HTML+="\n";
		    	  HTML+="<td>"+listOfFiles[i].length()/1024/1024+" MB&nbsp;&nbsp;&nbsp;&nbsp;"+"</td>";
		    	  if(extension.equalsIgnoreCase("mp4")||extension.equalsIgnoreCase("webm")||extension.equalsIgnoreCase("mkv")||extension.equalsIgnoreCase("flv")||extension.equalsIgnoreCase("avi")||extension.equalsIgnoreCase("wmv")) {
		    		  HTML+="<td style=\"width: 48px; text-align: left;\"><a href=\"PlayVideoServlet?file="+request.getParameter("type")+"/"+listOfFiles[i].getName()+"\" ><img alt=\"icon\" style=\"vertical-align: middle;\" src=\"images/PlayVideo.png\"></td>";
			    	  HTML+="\n";
		    	  }
		    	  if(request.getParameter("type").equalsIgnoreCase("/uploadedfiles")){
		        	HTML+="<td style=\"width: 48px; text-align: left;\"><a href=\"DeleteServlet?file="+request.getParameter("type")+"/"+listOfFiles[i].getName()+"\" ><img alt=\"icon\" style=\"vertical-align: middle;\" src=\"images/Delete.png\"></a></td>";
		        	HTML+="\n";
		        }
		    	  HTML+="</tr>";
		    	  HTML+="\n";
		      }
		    }
		    request.setAttribute("listservlet.message", HTML);
		    getServletContext().getRequestDispatcher("/ListFile.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("My do post got called.");
	}

}
