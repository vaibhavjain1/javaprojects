package com.teradata.config;
import java.awt.Component;
import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import javax.swing.JFileChooser;

public class ApplicationUtil {

	private final static JFileChooser fileSelector = new JFileChooser();

	public static String getTimeStamp()
	{
		java.util.Date date= new java.util.Date();
		Date timeStamp = new Timestamp(date.getTime());
		String currTime = timeStamp.toString();
		return currTime;
	}
	
	public static String SelectDirectory(String title,Component parent)
	{
		fileSelector.setDialogTitle(title);
		fileSelector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fileSelector.showOpenDialog(parent);
		String folderPath = null;
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File folder = fileSelector.getSelectedFile();
			folderPath = folder.getAbsolutePath();
		}
		return folderPath;
	}
	
	public static String timeTaken(long startTime,long endTime)
	{
		int sec = ((int) ((endTime - startTime)/1000))%60;
		int min = ((int) ((endTime - startTime)/1000))/60;
		Object abc = (min>0) ? min+" Minutes & "+sec+" Sec." : sec+" Sec." ;
		return "Time Taken = "+abc.toString();
	}
	
	/*public static void showMessage(String message)
	{
		JOptionPane.showMessageDialog(null,message,"DIfference Utility",JOptionPane.PLAIN_MESSAGE);
	}
	//overriden showMessage with component alignment option
	public static void showMessage(Component parent,String message)
	{
		JOptionPane.showMessageDialog(parent,message,"DIfference Utility",JOptionPane.PLAIN_MESSAGE);
	}
	public static void showErrorMessage(String message)
	{
		JOptionPane.showMessageDialog(null,message,"Error",JOptionPane.ERROR_MESSAGE);
	}
	//overriden showErrorMessage with component alignment option
	public static void showErrorMessage(Component parent,String message)
	{
		JOptionPane.showMessageDialog(parent,message,"Error",JOptionPane.ERROR_MESSAGE);
	}
	public static void showWarningMessage(String message)
	{
		JOptionPane.showMessageDialog(null,message,"Warning",JOptionPane.WARNING_MESSAGE);
	}
	public static void showWarningMessage(Component parent,String message)
	{
		JOptionPane.showMessageDialog(parent,message,"Warning",JOptionPane.WARNING_MESSAGE);
	}
	public static void loadFile(JEditorPane txtArea)
	{
		int returnVal = fileSelector.showOpenDialog(txtArea);
		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
			File file = fileSelector.getSelectedFile();
			//read the file content to scratchPadSqlTextArea
			try {
				txtArea.read( new FileReader( file.getAbsolutePath() ), null );
			} catch (IOException ex) {
				System.out.println(ApplicationUtil.getTimeStamp() + " - Problem accessing file"+file.getAbsolutePath());
			}
		} else 
		{
			System.out.println(ApplicationUtil.getTimeStamp() + " - Open command cancelled by user." + "\n");
		}
	}
	public static void saveFile(JEditorPane txtArea)
	{
		int returnVal = fileSelector.showSaveDialog(txtArea);
		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
			File file = fileSelector.getSelectedFile();
			try {
				txtArea.write(new FileWriter(file.getAbsolutePath()));
			} catch (IOException e) {
				System.out.println(ApplicationUtil.getTimeStamp() + " - Error Occurred while writing file " + file.getAbsolutePath());
				e.printStackTrace();
			}
		}
		else 
		{
			System.out.println(ApplicationUtil.getTimeStamp() + " - Save as File command cancelled by user." + "\n");
		}
	}
	

	
	public static void printConsoleMsg(String msg)
	{
		System.out.println(getTimeStamp() + " : " + msg);	
	}*/

}
