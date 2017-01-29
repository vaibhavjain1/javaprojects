package org.search.webProject;

import java.io.File;

import javax.servlet.ServletContext;

public class ListFolder {

	public static String getFolders(ServletContext context){
		String HTML="";
		File folder = new File(context.getInitParameter("data-location"));
		File[] fList = folder.listFiles();
		for (File file : fList) {
	        if (file.isDirectory() && !file.getName().equalsIgnoreCase("uploadedfiles") && !file.isHidden()) {
	            HTML+="<li><div class=\"image\"><a href=\"ListFileOptionServlet?type=/"+file.getName()+"\"> <img src=\"images/BlueButton.png\" alt=\""+file.getName()+"\"><div><h3>"+file.getName()+"<br></h3></div></a></div></li>";
	        }
	    }
		return HTML;
	}
}
