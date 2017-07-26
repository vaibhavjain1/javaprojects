package com.paypal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.paypal.ReportGeneration.TabularData.PairValue;

public class ReportGeneration {
	
	static class CorrIdEntry{
		String heading;
		Map<String,String> corrMap;
		public CorrIdEntry() {
			corrMap = new HashMap<>();
		}
		
		public void setHeading(String heading){
			this.heading = heading;
		}
		
		public void setCorrIDescription(String corrId, String description){
			corrMap.put(corrId, description);
		}
	}
	
	static class TabularData{
		class PairValue{
			String key;
			String value;
			public PairValue(String key, String value) {
				this.key = key;
				this.value = value;
			}
		}
		
		String heading;
		String keyHeading;
		String valueHeading;
		List<PairValue> pairValueList;
		public TabularData() {
			pairValueList = new ArrayList<>();
		}
		
		public void setPariHeading(String keyHeading, String valueHeading){
			this.keyHeading = keyHeading;
			this.valueHeading = valueHeading;
		}
		
		public void setHeading(String heading){
			this.heading = heading;
		}
		
		public void setTabularData(String key, String value){
			pairValueList.add(new PairValue(key, value));
		}
	}

	public PrintWriter pr;
	public String fileName;
	public boolean isPrintWriterClosed;
	
	public String getFileName(){
		return this.fileName;
	}
	
	public ReportGeneration() {
		fileName = Resources.ONE_BOX_REPORT_FILE_PREFIX+new Date().toString().replaceAll(":", "-").replaceAll(" ", "_")+".html";
		if(System.getProperty("os.name").startsWith("Linux") || System.getProperty("os.name").startsWith("LINUX"))
			fileName = Resources.ONE_BOX_LINUX_REPORT_FOLDER_PATH+fileName;
		else
			fileName = Resources.ONE_BOX_WINDOWS_REPORT_FOLDER_PATH+fileName;
		try {
			System.out.println("-----------------------One Box Testing Report---------------------------");
			pr = new PrintWriter(new File(fileName));
			isPrintWriterClosed = false;
			pr.println("<html>");
			pr.println("<head>");
			pr.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>Data</title>");
			pr.println("</head>");
			pr.println("<body>");
			pr.println("<table border=1>");
			pr.println("<tr>");
			pr.println("<th colspan=\"2\" style=\"padding-left:25em; padding-right:25em\">One Box Testing Report</th>");
			pr.println("</tr>");
			pr.println("</table><br>");
			pr.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void println(String str){
		System.out.println(str);
		pr.println(str+"<br>");
		pr.flush();
	}

	public void printCorrIdEntry(CorrIdEntry corrEntry) {
		if (corrEntry != null) {
			System.out.println("\n---------------------" + corrEntry.heading + "---------------------");
			pr.println("<br>");
			pr.println("<table border=1>");
			pr.println("<tr>");
			pr.println("<th colspan=\"2\">" + corrEntry.heading + "</th>");
			pr.println("</tr>");
			pr.println("<tr>");
			pr.println("<td bgcolor=silver class='medium'>Corr Id</td>");
			pr.println("<td bgcolor=silver class='medium'>Description</td>");
			pr.println("</tr>");
			if (corrEntry.corrMap.size() == 0) {
				pr.println("<tr>");
				pr.println("<td colspan=\"2\" class='normal' valign='top'><B> --- No Records Found --- <B></td>");
				System.out.println("--- No Records Found ---");
				pr.println("</tr>");
			} else {
				Set<Map.Entry<String, String>> corrKeySet = corrEntry.corrMap.entrySet();
				for (Map.Entry<String, String> entry : corrKeySet) {
					System.out.println(entry.getKey() + ": " + entry.getValue());
					pr.println("<tr>");
					pr.println(
							"<td class='normal' valign='top'> <a href=\"https://engineering.paypalcorp.com/cal/cgi/idsearch_manager.py?id_type=corr_id&id_value="
									+ entry.getKey() + "&fetchlog=0&submit=Search\" target=\"_blank\">" + entry.getKey()+ "</a></td>");
					pr.println("<td class='normal' valign='top'>" + entry.getValue() + "</td>");
					pr.println("</tr>");
				}
			}
			pr.println("</table>");
			pr.println("<br>");
			pr.flush();
		}
	}
	
	public void printTabularData(TabularData tabularData){
		if(tabularData!=null){
			System.out.println("\n---------------------"+tabularData.heading+"---------------------");
			System.out.println(tabularData.keyHeading+"  "+tabularData.valueHeading);
			pr.println("<br>");
			pr.println("<table border=1>");
			pr.println("<tr>");
			pr.println("<th colspan=\"2\">"+tabularData.heading+"</th>");
			pr.println("</tr>");
			pr.println("<tr>");
			pr.println("<td bgcolor=silver class='medium'>"+tabularData.keyHeading+"</td>");
			pr.println("<td bgcolor=silver class='medium'>"+tabularData.valueHeading+"</td>");
			pr.println("</tr>");
			Iterator<PairValue> itr = tabularData.pairValueList.iterator();
			if(tabularData.pairValueList.size()==0){
				pr.println("<tr>");
				pr.println("<td colspan=\"2\" class='normal' valign='top'><B> --- No Records Found --- <B></td>");
				System.out.println("--- No Records Found ---");
				pr.println("</tr>");
			}
			while (itr.hasNext()) {
				PairValue tempPair = itr.next();
				System.out.println(tempPair.key+": "+tempPair.value);
				pr.println("<tr>");
				pr.println("<td class='normal' valign='top'>"+tempPair.key+"</td>");
				pr.println("<td class='normal' valign='top'>"+tempPair.value+"</td>");
				pr.println("</tr>");
			}
			pr.println("</table>");
			pr.println("<br>");
			pr.flush();
		}
	}
	
	public void closeResources(){
		if(pr!=null && !isPrintWriterClosed){
			System.out.println("File saved at location: "+fileName);
			pr.println("</body>");
			pr.print("</html>");
			pr.close();
			isPrintWriterClosed = true;
		}
	}

}
