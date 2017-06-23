package com.teradata.config;

import javax.swing.JTextArea;


public class RunBatchPrograms
{
	private static int exitVal =  1;
	@SuppressWarnings("finally")
	public static int execBatFile(String fileName,JTextArea txtAreaBatConsole)
	{
		if (fileName == null || fileName.length()<1)
		{
			System.out.println(ApplicationUtil.getTimeStamp() + " - Emtpy file name to run");
			System.exit(1);
		}

		try
		{            
			String[] cmd = new String[3];
				cmd[0] = "cmd.exe" ;
				cmd[1] = "/C" ;
				cmd[2] = fileName;
			
			Runtime rt = Runtime.getRuntime();
			System.out.println(ApplicationUtil.getTimeStamp() + " - Executing " + cmd[0] + " " + cmd[1] 
			                                                     + " " + cmd[2]);
			Process proc = rt.exec(cmd);
			// any error message?
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "E",txtAreaBatConsole);            

			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "O",txtAreaBatConsole);

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			// any error???
					exitVal = proc.waitFor();
					System.out.println(ApplicationUtil.getTimeStamp() + " - ExitValue: " + exitVal);        
		} catch (Throwable t)
		{
			t.printStackTrace();
		}
		finally
		{
			return exitVal;
		}

	}
	
}