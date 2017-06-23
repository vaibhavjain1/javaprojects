package com.teradata.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JTextArea;

class StreamGobbler extends Thread
{
	private InputStream is;
	private String type;
	private JTextArea textAreaConsoleOut;
	public static StringBuffer consoleMsg = new StringBuffer();

	StreamGobbler(InputStream is, String type,JTextArea txtAreaBatConsole)
	{
		this.is = is;
		this.type = type;
		this.textAreaConsoleOut=txtAreaBatConsole;
	}

	public void run()
	{
		try
		{
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line=null;
			while ( (line = br.readLine()) != null)
			{
				printConMsg(type + ">" + line);
			}

		} catch (IOException ioe)
		{
			ioe.printStackTrace();  
		}
	}
	public void printConMsg(String msg)
	{
		if(textAreaConsoleOut != null)
			textAreaConsoleOut.append(msg+"\n");
	}

}