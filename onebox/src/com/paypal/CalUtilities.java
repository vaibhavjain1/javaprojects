package com.paypal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CalUtilities {

	public List<CalData> mapCalLogToResultSet(ResultSet rs) throws SQLException {
		List<CalData> calLogDataList = new ArrayList<>();
		while (rs.next()) {
			CalData calLogData = new CalData();
			calLogData.setAVAL_PLAN(rs.getString("AVAL_PLAN"));
			calLogData.setCAL_MINUTE(rs.getString("CAL_MINUTE"));
			calLogData.setCAL_STATUS(rs.getString("CAL_STATUS"));
			calLogData.setCORR_ID(rs.getString("CORR_ID"));
			calLogData.setDATA_CTR(rs.getString("DATA_CTR"));
			calLogData.setDEF_FM(rs.getString("DEF_FM"));
			calLogData.setDESCRIPTION(rs.getString("DESCRIPTION"));
			calLogData.setFC_REASON(rs.getString("FC_REASON"));
			calLogData.setFC_TYPE(rs.getString("FC_TYPE"));
			calLogData.setMACH_ID(rs.getString("MACH_ID"));
			calLogData.setPIPELINE_NAME(rs.getString("PIPELINE_NAME"));
			calLogData.setPLANS_FM(rs.getString("PLANS_FM"));
			calLogData.setPROD(rs.getString("PROD"));
			calLogData.setPROD_FMLY(rs.getString("PROD_FMLY"));
			calLogData.setREASON(rs.getString("REASON"));
			calLogData.setSTATE(rs.getString("STATE"));
			calLogData.setSUBPIPELINE_NAME(rs.getString("SUBPIPELINE_NAME"));
			calLogData.setTXN_TIME(rs.getString("TXN_TIME"));
			calLogDataList.add(calLogData);
		}
		return calLogDataList;
	}
	
	public void printOneBoxSpecificData(ReportGeneration writeReport, String one_box_start_datetime, String one_box_end_datetime, String one_box_machine, String one_box_jira_number, String one_box_build_id){
		writeReport.println("One box Start Time : " + one_box_start_datetime);
		writeReport.println("One box End Time : " + one_box_end_datetime);
		
		DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:SS z");
		df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
		String PST = df.format(new Date());
		writeReport.println("Report Generation Time : " + PST);
		writeReport.println("One Box Machine details: "+one_box_machine);
		writeReport.println("JIRA Id: "+"<a href=\"https://engineering.paypalcorp.com/jira/browse/"+one_box_jira_number+"\" target=\"_blank\">"+one_box_jira_number+"</a>");
		writeReport.println("Build Id: "+one_box_build_id);
	}
}
