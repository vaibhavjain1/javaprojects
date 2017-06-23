package com.teradata.restore.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import com.teradata.config.ApplicationUtil;
import com.teradata.config.ProjectConstants;
import com.teradata.config.RunBatchPrograms;

public class ArchivePanel extends JPanel {
	private static JTextField txtdbip;
	private static JTextField txtusername;
	private static JPasswordField txtpassword;
	private static JTextField txtbackupfile;
	private static JTextField txtbackupdir;
	private static String Dbstoarchive;
	private static JTextArea textArea;
	private static String backupdir;
	private static int issuccessful;
	private static String additionalComments;
	private JLabel progressBarlabel;
	private JProgressBar progressBar;
	private JButton archiveButton;
	private JButton resetButton;
	private JButton browseButton;
	private JButton btnCheckLog;
	private Thread task;
	private long startTime;
	private long endTime;

	public ArchivePanel() {
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

		JLabel lblNewLabel = new JLabel("Database Login Details");
		lblNewLabel.setForeground(Color.BLUE);

		JLabel lblNewLabel_1 = new JLabel("Database IP :");

		JLabel lblDatabaseLoginUser = new JLabel("DB Login User :");

		JLabel lblNewLabel_2 = new JLabel("DB Login Password :");

		txtdbip = new JTextField();
		txtdbip.setColumns(10);

		txtusername = new JTextField();
		txtusername.setColumns(10);

		txtpassword = new JPasswordField();
		txtpassword.setEchoChar('*');
		txtpassword.setColumns(10);

		JLabel lblNewLabel_3 = new JLabel("Database Back UP File Name :");
		lblNewLabel_3.setForeground(Color.BLUE);

		txtbackupfile = new JTextField();
		txtbackupfile
				.setToolTipText("Max 8 Characters. No special Chars. Atleast One alphabet is required");
		txtbackupfile.setColumns(10);

		archiveButton = new JButton("Archive");
		archiveButton.setToolTipText("Click here to Archieve Database");
		archiveButton.setEnabled(true);

		archiveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (txtdbip.getText().trim().equals("")
						|| txtusername.getText().trim().equals("")
						|| txtpassword.getText().trim().equals("")
						|| txtbackupdir.getText().trim().equals(""))
					progressBarlabel
							.setText("Please enter all required fields");
				else if (txtusername.getText().trim().equals("dbc")
						|| txtusername.getText().trim().equals("DBC")) {
					JOptionPane.showMessageDialog(null, "Are You Nuts ??");
				}

				else if (isarcmaininstalled()) {
					if (txtbackupfile.getText().length() > 8
							|| txtbackupfile.getText().length() < 2)
						JOptionPane.showMessageDialog(null,
								"Back file name should be 2-8 characters.");
					else if(txtbackupfile.getText().matches(".*?['\\-—¿?,.;: ].*"))
						JOptionPane.showMessageDialog(null,	"BackUp file Cannot contain special characters except _.");
					else {
						File exportdir = new File(txtbackupdir.getText());
						String[] filelist = exportdir.list();
						if (filelist.length != 0)
							JOptionPane.showMessageDialog(null,
									"Directory you selected is not empty");
						else {
							checkConnection();
							clearLogFiles();
							createLoginSqlFile();
							startBarProgress();
							task = new Thread(new Runnable() {

								public void run() {
									archieveDB();
									stopBarProgress();
									CreateDbInfoFile();
									moveFiles();
									clearLogFiles();
								}
							});

							task.start();
						}
					}
				}
			}
		});

		resetButton = new JButton("Reset");
		resetButton.setToolTipText("Click here to clear all fields");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtdbip.setText("");
				txtusername.setText("");
				txtpassword.setText("");
				txtbackupfile.setText("");
				textArea.setText("");
				txtbackupdir.setText("Please Choose Empty Directory");
			}
		});

		textArea = new JTextArea();
		textArea.setEditable(false);

		progressBar = new JProgressBar();

		progressBarlabel = new JLabel(".");

		JLabel lblDatabaseBackupDirectory = new JLabel(
				"Database Backup Directory :");

		txtbackupdir = new JTextField();
		txtbackupdir.setText("Please Choose Empty Directory");
		txtbackupdir.setColumns(10);

		browseButton = new JButton("Browse");
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String selectedExportDir = ApplicationUtil.SelectDirectory(
						"Select export directory", txtbackupdir);
				if (selectedExportDir != null)
					txtbackupdir.setText(selectedExportDir);
			}
		});

		btnCheckLog = new JButton("Show Log");
		btnCheckLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openLogFile();
			}
		});

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(66)
																		.addComponent(
																				lblNewLabel))
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(47)
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.TRAILING)
																						.addGroup(
																								groupLayout
																										.createSequentialGroup()
																										.addGroup(
																												groupLayout
																														.createParallelGroup(
																																Alignment.TRAILING)
																														.addComponent(
																																lblDatabaseLoginUser)
																														.addComponent(
																																lblNewLabel_1)
																														.addComponent(
																																lblNewLabel_2))
																										.addGap(49))
																						.addGroup(
																								groupLayout
																										.createSequentialGroup()
																										.addGroup(
																												groupLayout
																														.createParallelGroup(
																																Alignment.LEADING)
																														.addGroup(
																																groupLayout
																																		.createSequentialGroup()
																																		.addComponent(
																																				archiveButton)
																																		.addGap(18)
																																		.addComponent(
																																				resetButton))
																														.addGroup(
																																groupLayout
																																		.createParallelGroup(
																																				Alignment.TRAILING)
																																		.addComponent(
																																				lblDatabaseBackupDirectory)
																																		.addComponent(
																																				lblNewLabel_3)))
																										.addGap(7)))
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addGroup(
																								groupLayout
																										.createSequentialGroup()
																										.addGap(17)
																										.addGroup(
																												groupLayout
																														.createParallelGroup(
																																Alignment.LEADING)
																														.addGroup(
																																groupLayout
																																		.createParallelGroup(
																																				Alignment.TRAILING)
																																		.addGroup(
																																				groupLayout
																																						.createParallelGroup(
																																								Alignment.LEADING)
																																						.addComponent(
																																								txtusername,
																																								GroupLayout.PREFERRED_SIZE,
																																								GroupLayout.DEFAULT_SIZE,
																																								GroupLayout.PREFERRED_SIZE)
																																						.addComponent(
																																								txtpassword,
																																								GroupLayout.PREFERRED_SIZE,
																																								GroupLayout.DEFAULT_SIZE,
																																								GroupLayout.PREFERRED_SIZE)
																																						.addComponent(
																																								txtbackupfile,
																																								GroupLayout.PREFERRED_SIZE,
																																								GroupLayout.DEFAULT_SIZE,
																																								GroupLayout.PREFERRED_SIZE))
																																		.addComponent(
																																				txtdbip,
																																				GroupLayout.PREFERRED_SIZE,
																																				GroupLayout.DEFAULT_SIZE,
																																				GroupLayout.PREFERRED_SIZE))
																														.addComponent(
																																txtbackupdir,
																																GroupLayout.PREFERRED_SIZE,
																																193,
																																GroupLayout.PREFERRED_SIZE)))
																						.addGroup(
																								groupLayout
																										.createSequentialGroup()
																										.addGap(11)
																										.addComponent(
																												btnCheckLog)
																										.addGap(18)
																										.addComponent(
																												browseButton))))
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(35)
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								progressBarlabel,
																								GroupLayout.PREFERRED_SIZE,
																								208,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								progressBar,
																								GroupLayout.DEFAULT_SIZE,
																								344,
																								Short.MAX_VALUE)
																						.addComponent(
																								textArea,
																								GroupLayout.DEFAULT_SIZE,
																								377,
																								Short.MAX_VALUE))))
										.addGap(34)));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(16)
										.addComponent(lblNewLabel)
										.addGap(18)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																txtdbip,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																lblNewLabel_1))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																txtusername,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																lblDatabaseLoginUser))
										.addGap(13)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																txtpassword,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																lblNewLabel_2))
										.addGap(18)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																txtbackupfile,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																lblNewLabel_3))
										.addGap(10)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblDatabaseBackupDirectory)
														.addComponent(
																txtbackupdir,
																GroupLayout.PREFERRED_SIZE,
																20,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																archiveButton)
														.addComponent(
																resetButton)
														.addComponent(
																btnCheckLog)
														.addComponent(
																browseButton))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addComponent(progressBarlabel)
										.addGap(1)
										.addComponent(progressBar,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addComponent(textArea,
												GroupLayout.PREFERRED_SIZE,
												125, GroupLayout.PREFERRED_SIZE)
										.addContainerGap(137, Short.MAX_VALUE)));
		setLayout(groupLayout);

	}

	public void clearLogFiles() {
		textArea.append("\n" + "Clearing Data");
		String batFileParams = ProjectConstants.clearBatFile;
		RunBatchPrograms.execBatFile(System.getProperty("user.dir")
				+ batFileParams, null);
		/*
		 * consoleMsg_= RunBatchPrograms.getConsoleOut().toString();
		 * RunBatchPrograms.resetConsoleOut();
		 */
	}

	public void checkConnection() {
		Dbstoarchive = "";
		Connection con = null;
		while (con == null) {
			try {
				Class.forName("com.teradata.jdbc.TeraDriver");
				String connectionstring = "jdbc:teradata://"
						+ txtdbip.getText() + "/TMODE=TERA";
				con = DriverManager.getConnection(connectionstring,
						txtusername.getText(), txtpassword.getText());
				/*
				 * String str="jdbc:teradata://153.65.183.79/TMODE=TERA"; con =
				 * DriverManager.getConnection(str, "AL186021_01",
				 * "AL186021_01");
				 */
				if (con != null) {
					textArea.setText("Connection Successful");
					backupdir = null;
					PreparedStatement stmt = con
							.prepareStatement("select distinct(PHYSICAL_DB) FROM SYS_DB_MAP WHERE PHYSICAL_DB IS NOT NULL AND PHYSICAL_DB <> ' ';");
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						if (Dbstoarchive != "")
							Dbstoarchive += ",(" + rs.getString(1) + ")";
						else
							Dbstoarchive += "(" + rs.getString(1) + ")";

					}
					createArchiveScript(Dbstoarchive);
				} else
					JOptionPane.showMessageDialog(null,
							"Invalid DB Credentials");
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Invalid DB Credentials");
				throw new IllegalArgumentException(
						"connection Requirements are missing", e);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,
						"DB IS EMPTY.SYS_DB_MAP NOT AVAILABLE");
				throw new IllegalArgumentException(
						"some problem occured in connection", e);
			} finally {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void archieveDB() {

		String batFileParams = ProjectConstants.archivebatFile;
		issuccessful = RunBatchPrograms.execBatFile(
				System.getProperty("user.dir") + batFileParams, null);

	}

	public void startBarProgress() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				disableControls();
				textArea.append("\n" + "Archiving database");
				progressBarlabel.setText("Archiving DB");
				progressBar.setString("");
				progressBar.setVisible(true);
				progressBar.setIndeterminate(true);
				startTime = System.currentTimeMillis();
			}

		});
	}

	private void enableControls() {
		archiveButton.setEnabled(true);
		resetButton.setEnabled(true);
		browseButton.setEnabled(true);
		txtdbip.setEditable(true);
		txtusername.setEditable(true);
		txtpassword.setEditable(true);
		txtbackupfile.setEditable(true);
		txtbackupdir.setEditable(true);
		btnCheckLog.setEnabled(true);
	}

	private void disableControls() {
		archiveButton.setEnabled(false);
		resetButton.setEnabled(false);
		browseButton.setEnabled(false);
		txtdbip.setEditable(false);
		txtusername.setEditable(false);
		txtpassword.setEditable(false);
		txtbackupfile.setEditable(false);
		txtbackupdir.setEditable(false);
		btnCheckLog.setEnabled(false);
	}

	public void stopBarProgress() {
		/*SwingUtilities.invokeLater(new Runnable() {
			public void run() {*/
				endTime  = System.currentTimeMillis();
				enableControls();
				textArea.append("\n" + "Archiving database Completed");
				progressBar.setIndeterminate(false);
				progressBar.setString("100 %");
				progressBar.setValue(100);
				progressBarlabel.setText("Completed.");
				if (issuccessful == 0)
					additionalComments = JOptionPane.showInputDialog(null, "       DB Archieved\n  "+ApplicationUtil.timeTaken(startTime, endTime)+"\nAdditonal Comment if any?", "DB Archieved", JOptionPane.INFORMATION_MESSAGE);
				else
					additionalComments = JOptionPane.showInputDialog(null, "    Problem in DB Archieve\n  "+ApplicationUtil.timeTaken(startTime, endTime)+"\nAdditonal Comment if any?", "DB Archieved", JOptionPane.INFORMATION_MESSAGE);
			/*}
		});*/
	}

	Image image = (new ImageIcon(ProjectConstants.backgroundImage)).getImage();

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null)
			g.drawImage(image, (this.getWidth() / 2)
					- (image.getWidth(this) / 2), (this.getHeight() / 2)
					- (image.getHeight(this) / 2), image.getWidth(this),
					image.getHeight(this), this);

	}

	public static void main(String[] args) {
		System.out.println(isarcmaininstalled());
	}

	@SuppressWarnings("deprecation")
	public void createArchiveScript(String DBstoarchive) {
		textArea.append("\n" + "Creating Archieve Script");
		try {
			File file = new File(System.getProperty("user.dir")
					+ ProjectConstants.archivescript);
			FileWriter ff = new FileWriter(file); // here true is for appending
			PrintWriter pw = new PrintWriter(ff);
			pw.write(".logon " + txtdbip.getText() + "/"
					+ txtusername.getText() + "," + txtpassword.getText() + ";"
					+ "\n\n" + " ARCHIVE DATA TABLES " + DBstoarchive
					+ ",ABORT, RELEASE LOCK, FILE =" + txtbackupfile.getText()
					+ ";" + "\n\n" + " LOGOFF;" + "\n\n" + " .QUIT;");
			pw.close();
			ff.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void CreateDbInfoFile() {
		System.out.println("already gone");
		Properties prop = new Properties();
		OutputStream output = null;
		File file = new File(System.getProperty("user.dir")
				+ ProjectConstants.backupInfofile);

		Connection con = null;
		while (con == null) {
			try {
				output = new FileOutputStream(file);

				prop.setProperty("DB_IP", txtdbip.getText());
				prop.setProperty("DB_User", txtusername.getText());
				prop.setProperty("DB_Password", txtpassword.getText());
				prop.setProperty("Avail_DB", Dbstoarchive);
				prop.setProperty("DB_Backup_file", txtbackupfile.getText());

				Class.forName("com.teradata.jdbc.TeraDriver");
				String connectionstring = "jdbc:teradata://"
						+ txtdbip.getText() + "/TMODE=TERA";
				con = DriverManager.getConnection(connectionstring,
						txtusername.getText(), txtpassword.getText());
				/*
				 * String str="jdbc:teradata://153.65.183.79/TMODE=TERA"; con =
				 * DriverManager.getConnection(str, "AL186021_01",
				 * "AL186021_01");
				 */
				if (con != null) {
					PreparedStatement stmt = con
							.prepareStatement("sel * FROM dbc.tables where databasename='"
									+ txtusername.getText()
									+ "' and tableName='MDM_META_MATRIX'");
					ResultSet rs = stmt.executeQuery();
					if (rs.next()) {
						PreparedStatement stmt1 = con
								.prepareStatement("Select * from MDM_META_MATRIX");
						ResultSet rs1 = stmt1.executeQuery();
						if (rs1.next()) {
							prop.setProperty("MDM_Version", rs1.getString(7));
							prop.setProperty("Install_platform",
									rs1.getString(5));
						}
					} else {
						prop.setProperty("MDM_Version", "No_Info");
						prop.setProperty("Install_platform", "No_Info");
					}
					PreparedStatement stmt2 = con
							.prepareStatement("sel count(*) from sys_dbc_tables where tablename like 'mst_Reference_%'");
					ResultSet rs2 = stmt2.executeQuery();
					if (rs2.next()) {
						if (rs2.getString(1).equals("4"))
							prop.setProperty("Multitable", "No");
						else
							prop.setProperty("Multitable", "Yes");

					}

					PreparedStatement stmt3 = con
							.prepareStatement("sel InfoData from dbc.dbcinfo where InfoKey ='VERSION'");
					ResultSet rs3 = stmt3.executeQuery();
					if (rs3.next()) {
							prop.setProperty("DB_Version", rs3.getString(1));
					}

					PreparedStatement stmt4 = con
							.prepareStatement("sel count(*) from dbc.tablesv where DatabaseName ='"+txtusername.getText()+"' and TableName = 'MST_ACCOUNT_PARTY'");
					ResultSet rs4 = stmt4.executeQuery();
					if (rs4.next()) {
						if (rs4.getString(1).equals("0")){
							prop.setProperty("Is_CRDM", "No");
							PreparedStatement stmt5 = con
									.prepareStatement("select * from dbc.tablesv where databaseName = '"+txtusername.getText()+"' and tablename = 'facet_info_misc'");
							ResultSet rs5 = stmt5.executeQuery();
									if(rs5.next()){
										PreparedStatement stmt6 = con
												.prepareStatement("select facet_content from facet_info_misc");
										ResultSet rs6 = stmt6.executeQuery();
										if(rs6.next()){
											Clob tempClob = rs6.getClob("facet_content");
											String abc ="";
											BufferedReader br = new BufferedReader(tempClob.getCharacterStream());
											while((abc = br.readLine()) != null){
												if(abc.contains("<FOLDER Name=\"Custom Models\">")){
													prop.setProperty("Is_Custom", "Yes");
													break;
												}
											}
											if(prop.getProperty("Is_Custom") != "Yes")
											prop.setProperty("Is_Custom", "No");
										}
									}
						else
							prop.setProperty("Is_CRDM", "Yes");
						}
					}
					prop.setProperty("Arcmain_Version", arcmainVersion());
					if(additionalComments!=null){
						prop.setProperty("Additional_Comments", additionalComments);
					}
				} else
					JOptionPane.showMessageDialog(null,
							"Invalid DB Credentials");
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Invalid DB Credentials");
				throw new IllegalArgumentException(
						"connection Requirements are missing", e);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,
						"DB IS EMPTY.SYS_DB_MAP NOT AVAILABLE");
				throw new IllegalArgumentException(
						"some problem occured in connection", e);
			} catch (IOException e) {
				textArea.append("\n" + "Some problem creating Db_Info file");
				e.printStackTrace();
			} finally {
				try {
					prop.store(output,
							"This File Contains Important Info about DB Back files. Please don't Modify");
					con.close();
					output.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String arcmainVersion() {
		String releaseVersion = "Not available";
		try {

			Process p = Runtime.getRuntime().exec("cmd.exe /c arcmain");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = reader.readLine();
			while (line != null) {
				if (line.contains("RELEASE")) {
					int lineLength = line.length();
					releaseVersion = line.substring((line.indexOf(": ")) + 2,
							lineLength);
					break;
				}
				line = reader.readLine();
			}

		} catch (IOException e) {
			System.out.println("Some problem while getting arcmain version");
			e.printStackTrace();
		}
		return releaseVersion;
	}

	public static void createLoginSqlFile() {
		try {
			File file = new File(System.getProperty("user.dir")
					+ ProjectConstants.logininfofile);
			FileWriter ff = new FileWriter(file); // here true is for appending
			PrintWriter pw = new PrintWriter(ff);
			// pw.write(".logon "+txtdbip.getText()+"/"+txtusername.getText()+","+txtpassword.getText()+";"+"\n\n"+" ARCHIVE DATA TABLES "+DBstoarchive+",ABORT, RELEASE LOCK, FILE ="+txtbackupfile.getText()+";"+"\n\n"+" LOGOFF;"+"\n\n"+" .QUIT;");
			pw.write(".logon " + txtdbip.getText() + "/"
					+ txtusername.getText() + "," + txtpassword.getText());
			pw.close();
			ff.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void moveFiles() {
		textArea.append("\n" + "Moving files to specified directory");
		backupdir = txtbackupdir.getText();
		File dir1 = new File(System.getProperty("user.dir")
				+ ProjectConstants.defaultbackupdir);
		if (dir1.isDirectory()) {
			File[] content = dir1.listFiles();
			for (int i = 0; i < content.length; i++) {
				content[i].renameTo(new File(txtbackupdir.getText() + "\\"
						+ content[i].getName()));
				System.out.println(content[i].getName());
			}
		}
	}

	public static void openLogFile() {
		if (backupdir != null) {
			Runtime runtime = Runtime.getRuntime();
			try {
				// Process process =
				// runtime.exec("C:\\path\\to\\notepad.exe C:\\path\\to\\file.txt");
				runtime.exec("C:\\Windows\\notepad.exe " + backupdir
						+ ProjectConstants.Arcmainlogfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			JOptionPane
					.showMessageDialog(null, "Sorry, Log file not available");
	}

	public static boolean isarcmaininstalled() {
		Process checkarcmain = null;

		String[] cmd = new String[] { "cmd.exe", "/C", "arcmain", "2>&1" };
		try {
			checkarcmain = Runtime.getRuntime().exec(cmd);
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(
					checkarcmain.getInputStream()));
			while ((line = br.readLine()) != null) {
				if (line.contains("arcmain"))
					;
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Arcmain not installed properly");
		return false;

	}
}
