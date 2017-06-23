package org.search.webProject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

//import com.oreilly.servlet.MultipartRequest;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private boolean isMultipart;
	private int maxFileSize = 1200000 * 1024;
	private int maxMemSize = 4 * 1024;
	private File file ;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filePath = getServletContext().getInitParameter("data-location")+"/uploadedfiles/";
		new File(filePath).mkdir();
			
		isMultipart = ServletFileUpload.isMultipartContent(request);
	      response.setContentType("text/html");
	      if( !isMultipart ){
	    	  request.setAttribute("Servlet.message", "error while uploading");
			  getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
	      }
	      
	      DiskFileItemFactory factory = new DiskFileItemFactory();
	      // maximum size that will be stored in memory
	      factory.setSizeThreshold(maxMemSize);
	      // Location to save data that is larger than maxMemSize.
	      factory.setRepository(new File(getServletContext().getInitParameter("data-location")+"/uploadedfiles"));

	      // Create a new file upload handler
	      ServletFileUpload upload = new ServletFileUpload(factory);
	      // maximum file size to be uploaded.
	      upload.setSizeMax( maxFileSize );

	      try{ 
	      // Parse the request to get file items.
	      @SuppressWarnings("rawtypes")
	      List fileItems = upload.parseRequest(request);
		
	      // Process the uploaded file items
	      @SuppressWarnings("rawtypes")
	      Iterator i = fileItems.iterator();

	      while ( i.hasNext () ) 
	      {
	         FileItem fi = (FileItem)i.next();
	         if ( !fi.isFormField () )	
	         {
	            // Get the uploaded file parameters
	            String fileName = fi.getName();
	            // Write the file
	            if( fileName.lastIndexOf("\\") >= 0 ){
	            	if(new File(filePath + fileName.substring( fileName.lastIndexOf("\\"))).exists())
	            		file = new File( filePath + renameFileIfExist(fileName.substring( fileName.lastIndexOf("\\"))) + new Date()) ;
	            	else
	            		file = new File( filePath + fileName.substring( fileName.lastIndexOf("\\"))) ;
	            }else{
	            	if(new File(filePath + fileName.substring( fileName.lastIndexOf("\\")+1)).exists())
	            		file = new File( filePath + renameFileIfExist(fileName.substring( fileName.lastIndexOf("\\")+1)) + new Date()) ;
	            	else
	            		file = new File( filePath + fileName.substring(fileName.lastIndexOf("\\")+1)) ;
	            }
	            fi.write( file ) ;
	            request.setAttribute("Servlet.message", file.getName() +" successfully uploaded");
	         }
	      }
			getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
	      }catch (Exception e) {
	    	  request.setAttribute("Servlet.message", "error while uploading");
			  getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
	      }
	}
	
	public static String renameFileIfExist(String fileName){
		SimpleDateFormat bb = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss");
		return fileName.substring(0, fileName.lastIndexOf("."))+"_"+bb.format(new Date())+fileName.substring(fileName.lastIndexOf("."));
	}

}
