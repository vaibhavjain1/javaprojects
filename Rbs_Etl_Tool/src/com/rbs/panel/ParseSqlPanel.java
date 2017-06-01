package com.rbs.panel;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Font;
import java.awt.TextArea;
import javax.swing.JTextArea;

public class ParseSqlPanel extends JPanel{
	private JTextField txtbackupdir;
	private static JTextArea textArea;
	public ParseSqlPanel() {
		
		JLabel lblWelcomeToRbs = new JLabel("Welcome to RBS ETL tool");
		lblWelcomeToRbs.setFont(new Font("Tahoma", Font.PLAIN, 32));
		
		JLabel lblSqlDirectory = new JLabel("SQL Directory");
		
		txtbackupdir = new JTextField();
		txtbackupdir.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedExportDir = SelectDirectory(
						"Select export directory", txtbackupdir);
				if (selectedExportDir != null)
					txtbackupdir.setText(selectedExportDir);
			}
		});
		
		
		JButton btnNewButton = new JButton("Parse SQL Files");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){

				try {
					//There is lot of data which doesn't contain full info like id 254, 408, 477 etc. (like missing year in these records)
					// I am trying to take care as many as corrupt data records
					String currentFolderPath = txtbackupdir.getText();
					PrintWriter pr = new PrintWriter(new FileWriter(new File(currentFolderPath + "\\result.csv")));
					BufferedReader br = null;
					File currFolder = new File(currentFolderPath);
					boolean isFirstFile = true;
					if (currFolder.isDirectory()) {
						File[] fileList = currFolder.listFiles();
						//Reading all the .sql files present in specified folder and creating one final csv file.
						for (File file : fileList) {
							if (file.getAbsolutePath().endsWith(".sql")) {
								br = new BufferedReader(new FileReader(file));
								String str;
								if(isFirstFile){
									while ((str = br.readLine()) != null) {
										if(str.startsWith("INSERT INTO")){
											String headerString = str.substring(str.indexOf("(")+1, str.indexOf(")"));
											headerString = headerString.replaceAll("`", "");
											pr.write(headerString+"\n");
											break;
										}
									}
									isFirstFile = false;
								}
								
								while ((str = br.readLine()) != null) {
									if (str.startsWith("(")) {
										String finalStr = "";
										char[] tempCharArray = str.toCharArray();
										finalStr = str.substring(1, str.indexOf(",")) + ", ";
										for (int i = 0; i < tempCharArray.length; i++) {
											if (tempCharArray[i] == '\'') {
												int nextindex = str.indexOf('\'', i + 1);
												String currString = null;
												if (nextindex != -1) {
													currString = str.substring(i + 1, nextindex);
													currString = currString.replaceAll(", ", " ");
													i = nextindex + 1;
													finalStr += currString + ", ";
												}
											}
										}
										finalStr = finalStr.replaceAll(", ", "_");
										finalStr = finalStr.replaceAll(",", "");
										finalStr = finalStr.replaceAll("_", ", ");
										pr.write(finalStr);
										pr.write("\n");
									}
								}
							}
						}
					}
					pr.close();
					textArea.setText("Congratulations CSV file is generated.\n");
					textArea.append(currentFolderPath + "\result.csv");
				} catch (Exception e1) {
					textArea.setText("Exception while generating file");
				} 
			
			}
		});
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(29)
					.addComponent(lblSqlDirectory, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtbackupdir, GroupLayout.PREFERRED_SIZE, 264, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(45, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(55, Short.MAX_VALUE)
					.addComponent(lblWelcomeToRbs, GroupLayout.PREFERRED_SIZE, 368, GroupLayout.PREFERRED_SIZE)
					.addGap(43))
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(178)
					.addComponent(btnBrowse)
					.addContainerGap(209, Short.MAX_VALUE))
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(39)
					.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 374, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(49, Short.MAX_VALUE))
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(115)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(121, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblWelcomeToRbs, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addGap(42)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtbackupdir, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSqlDirectory, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(btnBrowse, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(42, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
	}
	
	public static String SelectDirectory(String title,Component parent)
	{
		final JFileChooser fileSelector = new JFileChooser();
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
}
