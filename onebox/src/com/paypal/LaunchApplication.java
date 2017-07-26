package com.paypal;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.paypal.ReportGeneration.CorrIdEntry;
import com.paypal.ReportGeneration.TabularData;

public class LaunchApplication {

	public static volatile boolean isInProgress = false;

	public static void main(String[] args) {
		/*try {
			main1(Resources.ONE_BOX_START_DATETIME, Resources.ONE_BOX_END_DATETIME, Resources.ONE_BOX_MACHINE,
					Resources.SEND_TO_MAIL, Resources.ONE_BOX_JIRA_NUMBER, Resources.ONE_BOX_BUILD_ID,
					Resources.COMPARE_PREVIOUS_DATE, Resources.GENERATE_FULL_LOG);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Ky");
		}*/
		try {
			Connection connection = null;
			Statement statement = null;
			ResultSet resultSetFetchAllData = null;
			DataSource instance = DataSource.getInstance();
			connection = instance.getConnection();
			statement = connection.createStatement();
			statement.setQueryTimeout(300);
			String fetchAllDataQuery = "";
			resultSetFetchAllData = statement.executeQuery(fetchAllDataQuery);
			
			if (resultSetFetchAllData == null) {
				System.out.println("No Data found");
				
			}else{
				ResultSetMetaData rsmd = resultSetFetchAllData.getMetaData();
				int columnsNumber = rsmd.getColumnCount();
				for (int i = 1; i <= columnsNumber; i++) {
			        if (i > 1) System.out.print(",  ");
			        String columnValue = resultSetFetchAllData.getString(i);
			        System.out.print(columnValue + " " + rsmd.getColumnName(i));
			    }
			    System.out.println("");
			}
		} catch (IOException | SQLException | PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main1(String one_box_start_datetime, String one_box_end_datetime, String one_box_machine,
			String send_to_mail, String one_box_jira_number, String one_box_build_id, String compare_previous_date, boolean generate_full_log)
					throws Exception {
		isInProgress = true;
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSetFetchAllData = null;
		DataSource instance = DataSource.getInstance();
		ReportGeneration writeReport = new ReportGeneration();
		SendMailWithReport mailObj = new SendMailWithReport();
		CalUtilities calUtil = new CalUtilities();
		LaunchApplication currObj = new LaunchApplication();

		calUtil.printOneBoxSpecificData(writeReport, one_box_start_datetime, one_box_end_datetime, one_box_machine,
				one_box_jira_number, one_box_build_id);
		try {
			connection = instance.getConnection();
			statement = connection.createStatement();
			statement.setQueryTimeout(300);
			String fetchAllDataQuery = "SELECT * FROM cal_moneyplanningserv where CAL_MINUTE <= '"
					+ one_box_end_datetime + "' and CAL_MINUTE >= '" + one_box_start_datetime + "' AND mach_id in ('"
					+ one_box_machine + "')";
			resultSetFetchAllData = statement.executeQuery(fetchAllDataQuery);
			if (resultSetFetchAllData == null) {
				writeReport.println("No Records found. Please recheck one box details.");
				throw new Exception();
			} else {
				List<CalData> calLogDataList = calUtil.mapCalLogToResultSet(resultSetFetchAllData);

				currObj.analyzeOneBoxTransactions(calLogDataList, writeReport, generate_full_log);

				if (!compare_previous_date.equalsIgnoreCase("")) {
					currObj.analyzePreviousDay(statement, writeReport, one_box_start_datetime, one_box_end_datetime,
							one_box_machine, compare_previous_date);
				}
			}

			// Send Mail
			if (writeReport != null)
				writeReport.closeResources();
			if (Resources.SEND_MAIL) {
				mailObj.sendMail(send_to_mail, writeReport.getFileName());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (resultSetFetchAllData != null) try {resultSetFetchAllData.close();} catch (SQLException e) {e.printStackTrace();}
			if (statement != null) try {statement.close();} catch (SQLException e) {e.printStackTrace();}
			if (connection != null)	try {connection.close();} catch (SQLException e) {e.printStackTrace();}
			if (writeReport != null) writeReport.closeResources();
			isInProgress = false;
		}
		Runtime.getRuntime().addShutdownHook(new CloseResource(instance));
	}
	
	public void analyzePreviousDay(Statement statement, ReportGeneration writeReport, String one_box_start_datetime, String one_box_end_datetime, String one_box_machine, String compare_previous_date) throws Exception{
		if(compare_previous_date.equalsIgnoreCase("") || compare_previous_date==null){
			return;
		}
		ResultSet resultSetFetchNewDeclinesSet1 = null;
		ResultSet resultSetFetchNewFailures = null;
		ResultSet resultSetpreviousDayTransactionAnalysis = null;
		try {
			
			CorrIdEntry newFailureEntries = new CorrIdEntry();
			String fetchNewFailures = "select corr_id, description, count(*) C from cal_moneyplanningserv where CAL_MINUTE <= '"
					+ one_box_end_datetime + "' and CAL_MINUTE >= '" + one_box_start_datetime
					+ "' and STATE='FAILED' and MACH_ID='" + one_box_machine
					+ "' group by description having description not in (select distinct description from cal_moneyplanningserv where CAL_MINUTE <= '"
					+ compare_previous_date + " 23:59' and CAL_MINUTE >= '"
					+ compare_previous_date + " 00:00' and STATE='FAILED') order by C desc;";
			resultSetFetchNewFailures = statement.executeQuery(fetchNewFailures);
			newFailureEntries.setHeading("New Failures");
			while(resultSetFetchNewFailures.next()){
				newFailureEntries.setCorrIDescription(resultSetFetchNewFailures.getString(1),resultSetFetchNewFailures.getString(2)+" "+resultSetFetchNewFailures.getInt(3));
			}
			writeReport.printCorrIdEntry(newFailureEntries);
			
			TabularData prevDayData = new TabularData();
			String fetchPreviousDayTransactionsCount = "SELECT state, COUNT(*) FROM cal_moneyplanningserv force index (cal_minute) WHERE CAL_MINUTE BETWEEN '"
					+ compare_previous_date + " " + one_box_start_datetime.split(" ")[1] + "' AND '"
					+ compare_previous_date + " " + one_box_end_datetime.split(" ")[1] + "' AND MACH_ID='"
					+ one_box_machine + "' GROUP BY state;";
			System.out.println(fetchPreviousDayTransactionsCount);
			resultSetpreviousDayTransactionAnalysis = statement.executeQuery(fetchPreviousDayTransactionsCount);
			prevDayData.setHeading("Transactions analysis for Date: " + compare_previous_date + " at same time.");
			prevDayData.setPariHeading("Transaction Type", "Count");
			while(resultSetpreviousDayTransactionAnalysis.next()){
				prevDayData.setTabularData(resultSetpreviousDayTransactionAnalysis.getString(1),resultSetpreviousDayTransactionAnalysis.getString(2));
			}
			writeReport.printTabularData(prevDayData);
			
			/*CorrIdEntry newDeclineEntries = new CorrIdEntry();
			newDeclineEntries.setHeading("New Declines");
			String fetchNewDeclines = "select corr_id, description, count(*) C from cal_moneyplanningserv where CAL_MINUTE <= '"
					+ one_box_end_datetime + "' and CAL_MINUTE >= '" + one_box_start_datetime
					+ "' and STATE='DECLINED' and MACH_ID='" + one_box_machine
					+ "' group by description having description not in (select distinct description from cal_moneyplanningserv where CAL_MINUTE <= '"
					+ compare_previous_date + " 03:59' and CAL_MINUTE >= '"
					+ compare_previous_date + " 00:00' and STATE='DECLINED') order by C desc;";
			resultSetFetchNewDeclinesSet1 = statement.executeQuery(fetchNewDeclines);
			while(resultSetFetchNewDeclinesSet1.next()){
				newDeclineEntries.setCorrIDescription(resultSetFetchNewDeclinesSet1.getString(1),resultSetFetchNewDeclinesSet1.getString(2)+" "+resultSetFetchNewDeclinesSet1.getInt(3));
			}
			writeReport.printCorrIdEntry(newDeclineEntries);
			
			select distinct description from cal_moneyplanningserv where CAL_MINUTE <= '" + compare_previous_date + " 05:59' and CAL_MINUTE >= '" + compare_previous_date + " 00:00' and STATE='DECLINED'
			select distinct description from cal_moneyplanningserv where CAL_MINUTE <= '" + compare_previous_date + " 11:59' and CAL_MINUTE >= '" + compare_previous_date + " 06:00' and STATE='DECLINED'
			select distinct description from cal_moneyplanningserv where CAL_MINUTE <= '" + compare_previous_date + " 17:59' and CAL_MINUTE >= '" + compare_previous_date + " 12:00' and STATE='DECLINED'
			select distinct description from cal_moneyplanningserv where CAL_MINUTE <= '" + compare_previous_date + " 23:59' and CAL_MINUTE >= '" + compare_previous_date + " 18:00' and STATE='DECLINED'
			*/

		} catch (Exception e) {
			throw e;
		} finally {
			if (resultSetFetchNewFailures != null) try {resultSetFetchNewFailures.close();} catch (SQLException e) {e.printStackTrace();}
			if (resultSetpreviousDayTransactionAnalysis != null) try {resultSetpreviousDayTransactionAnalysis.close();} catch (SQLException e) {e.printStackTrace();}
			if (resultSetFetchNewDeclinesSet1 != null) try {resultSetFetchNewDeclinesSet1.close();} catch (SQLException e) {e.printStackTrace();}
		}
	}

	public void analyzeOneBoxTransactions(List<CalData> calLogDataList, ReportGeneration writeReport, boolean generate_full_log) {
		// Write all Corr Id in report
		if (generate_full_log) {
			CorrIdEntry allEntry = new CorrIdEntry();
			allEntry.setHeading("All Corr Id's");
			for (CalData calLogData : calLogDataList) {
				allEntry.setCorrIDescription(calLogData.CORR_ID, calLogData.toString());
			}
			writeReport.printCorrIdEntry(allEntry);
		}
		
		// Transactions count group wise
		Map<String, Integer> stateMap = new HashMap<>();
		for (CalData calLogData : calLogDataList) {
			Integer count;
			if ((count = stateMap.get(calLogData.STATE)) != null) {
				stateMap.put(calLogData.STATE, count + 1);
			} else {
				stateMap.put(calLogData.STATE, 1);
			}
		}
		
		TabularData transData = new TabularData();
		transData.setHeading("Total Transactions: " + calLogDataList.size());
		transData.setPariHeading("Transaction Type", "Count");
		Set<Map.Entry<String, Integer>> calSet = stateMap.entrySet();
		for (Map.Entry<String, Integer> entry : calSet) {
			transData.setTabularData(entry.getKey(),String.valueOf(entry.getValue()));
		}
		writeReport.printTabularData(transData);

		// Failed Transactions
		CorrIdEntry failedEntries = new CorrIdEntry();
		failedEntries.setHeading("Failed Transactions");
		for (CalData calLogData : calLogDataList) {
			if (calLogData.STATE.equalsIgnoreCase("FAILED")) {
				failedEntries.setCorrIDescription(calLogData.CORR_ID,calLogData.DESCRIPTION.substring(calLogData.DESCRIPTION.indexOf(",")+1));
			}
		}
		writeReport.printCorrIdEntry(failedEntries);

		// Declined Transactions
		CorrIdEntry declinedEntries = new CorrIdEntry();
		declinedEntries.setHeading("Declined Transactions");
		Map<String,Integer> declineReasonMap = new HashMap<>();
		for (CalData calLogData : calLogDataList) {
			if (calLogData.STATE.equalsIgnoreCase("DECLINED")) {
				Integer declineReasonCount;
				if((declineReasonCount=declineReasonMap.get(calLogData.DESCRIPTION))!=null){
					declineReasonMap.put(calLogData.DESCRIPTION,declineReasonCount+1);
				}else{
					declineReasonMap.put(calLogData.DESCRIPTION,1);
				}
				declinedEntries.setCorrIDescription(calLogData.CORR_ID,calLogData.DESCRIPTION.substring(calLogData.DESCRIPTION.indexOf(",")+1));
			}
		}

		TabularData uniqueDeclineCodeData = new TabularData();
		uniqueDeclineCodeData.setHeading("Unique Decline Reasons");
		uniqueDeclineCodeData.setPariHeading("Decline Code", "Count");
		Set<Map.Entry<String, Integer>> declineReasonSet = declineReasonMap.entrySet();
		for (Map.Entry<String, Integer> entry : declineReasonSet) {
			uniqueDeclineCodeData.setTabularData(entry.getKey().substring(entry.getKey().indexOf(",")+1),String.valueOf(entry.getValue()));
		}
		
		writeReport.printTabularData(uniqueDeclineCodeData);
		writeReport.printCorrIdEntry(declinedEntries);
	}

}
