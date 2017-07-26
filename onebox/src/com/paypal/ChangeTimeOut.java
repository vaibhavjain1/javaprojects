package com.paypal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

public class ChangeTimeOut {

	public static void main(String[] args) throws Exception{
		FileInputStream inputFile = new FileInputStream(new File("C:\\Planning_Repo\\CodeFormatter_TimoutIncrease\\app_IncreaseTimeout.properties"));
		Properties propInput = new Properties();
		propInput.load(inputFile);
		Set<Object> myset = propInput.keySet();
		
		String fileName = "C:\\Planning_Repo\\CodeFormatter_TimoutIncrease\\Updated_App_"+new Date().toString().replaceAll(":", "-").replaceAll(" ", "_")+".properties";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pr = new PrintWriter(new File(fileName));
		String line = null;
		while(!(line=br.readLine()).equalsIgnoreCase("%")){
			if(!line.contains("=")){
				pr.println(line);
			}else{
				String key = line.substring(0, line.indexOf("="));
				if(myset.contains(key)){
					pr.println(key+"="+propInput.getProperty(key));
				}else{
					pr.println(line);
				}
			}		
		}
		pr.flush();
		System.out.println(fileName);
	}
}
