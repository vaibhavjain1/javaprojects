package com.teradata.restore.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;

import com.teradata.config.ApplicationUtil;
import com.teradata.config.ProjectConstants;
import com.teradata.config.RunBatchPrograms;

public class RestorePanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static JTextField txtdbbackupdir;
	private JTextField txtdbbackupfile;
	// private static int fileavailcount;
	private static JTextArea textArea;
	private JProgressBar progressBar;
	private JLabel progressBarlabel;
	private Thread task;
	private JButton restoreButton;
	private JButton browseButton;
	private JButton resetButton;
	private JTextField txtdbip;
	private JTextField txtusername;
	private String passwordBackup;
	private static JCheckBox copyCheckBox;
	private int issuccessful;
	private static String ipBackup;
	private long startTime;
	private long endTime;
	/**
	 * Create the panel.
	 */
	public RestorePanel() {

		JLabel lblDatabaseRestoration = new JLabel("DataBase Restoration");
		lblDatabaseRestoration.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDatabaseRestoration.setForeground(Color.BLUE);

		JLabel lblSelectDbBackup = new JLabel("Select DB backup Directory :");

		txtdbbackupdir = new JTextField();
		txtdbbackupdir.setText("Select DB Backup Directory");
		txtdbbackupdir.setColumns(10);

		browseButton = new JButton("Browse");
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String selectedExportDir = ApplicationUtil.SelectDirectory(
						"Select export directory", txtdbbackupdir);
				if (selectedExportDir != null) {
					txtdbbackupdir.setText(selectedExportDir);

					Properties prop = new Properties();
					InputStream input = null;
					File file = new File(selectedExportDir
							+ ProjectConstants.savedbackupInfofile);

					try {
						input = new FileInputStream(file);
						prop.load(input);
						ipBackup = prop.getProperty("DB_IP");
						txtdbip.setText(prop.getProperty("DB_IP"));
						txtusername.setText(prop.getProperty("DB_User"));
						txtdbbackupfile.setText(prop
								.getProperty("DB_Backup_file"));
					} catch (FileNotFoundException e) {
						JOptionPane
								.showMessageDialog(null, "Invalid Directory");
						e.printStackTrace();
					} catch (IOException e) {
						JOptionPane
								.showMessageDialog(null, "Invalid Directory");
						e.printStackTrace();
					}

				}

			}
		});

		JLabel lblDbBackAvailable = new JLabel("DB Back Up file Name :");

		txtdbbackupfile = new JTextField();
		txtdbbackupfile.setEditable(false);
		txtdbbackupfile.setColumns(10);

		restoreButton = new JButton("Start Restoration");
		restoreButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		restoreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (txtdbbackupdir.getText().trim().equals("")
						|| txtdbbackupfile.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Select DB Backup Directory");
				} else if (isarcmaininstalled()) {
					clearFiles();
					checkConnection();
					startBarProgress();
					task = new Thread(new Runnable() {

						@Override
						public void run() {
							if (copyCheckBox.isSelected())
								copyDb();
							else
								restoreDb();
							stopBarProgress();
						}

					});
					task.start();
				}
			}

			private void restoreDb() {
				String batFileParams = ProjectConstants.restorebatFile;
				issuccessful = RunBatchPrograms.execBatFile(
						System.getProperty("user.dir") + batFileParams, null);
			}

			private void copyDb() {
				createLoginSqlFile();
				String batFileParams = ProjectConstants.copybatFile;
				issuccessful = RunBatchPrograms.execBatFile(System.getProperty("user.dir")
						+ batFileParams, null);
			}

			public void createLoginSqlFile() {
				try {
					File file = new File(System.getProperty("user.dir")
							+ ProjectConstants.logininfofile);
					FileWriter ff = new FileWriter(file); // here true is for
															// appending
					PrintWriter pw = new PrintWriter(ff);
					pw.write(".logon " + txtdbip.getText() + "/"
							+ txtusername.getText() + "," + passwordBackup
							+ ";");
					pw.close();
					ff.close();
					System.out.println("filewritten successfully");

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			public void startBarProgress() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						disableControls();
						if (copyCheckBox.isSelected()) {
							textArea.append("\n" + "Copying database");
							progressBarlabel
									.setText("Copying DB. It will take time. Be patient");
						} else {
							textArea.append("\n" + "Restoring database");
							progressBarlabel
									.setText("Restoring DB. It will take time. Be patient");
						}
						progressBar.setString("");
						progressBar.setVisible(true);
						progressBar.setIndeterminate(true);
						startTime = System.currentTimeMillis();
					}
				});
			}

			public void stopBarProgress() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						endTime  = System.currentTimeMillis();
						enableControls();
						textArea.append("\n"
								+ "Restoration of database Completed");
						progressBar.setIndeterminate(false);
						progressBar.setString("100 %");
						progressBar.setValue(100);
						progressBarlabel.setText("Completed.");
						if (issuccessful == 0 || issuccessful == 4)
							if (copyCheckBox.isSelected())
								JOptionPane.showMessageDialog(null, "   DB Copied\n"+ApplicationUtil.timeTaken(startTime,endTime));
							else
								JOptionPane.showMessageDialog(null,	"   DB Restored\n"+ApplicationUtil.timeTaken(startTime,endTime));
						else
							if (copyCheckBox.isSelected())
								JOptionPane.showMessageDialog(null,"      DB Copied with Warnings\n"+ApplicationUtil.timeTaken(startTime,endTime));
							else
								JOptionPane.showMessageDialog(null,	"     DB Restored with warnings\n "+ApplicationUtil.timeTaken(startTime,endTime));
					}
				});
			}

			private void enableControls() {
				txtdbbackupdir.setEditable(true);
				restoreButton.setEnabled(true);
				browseButton.setEnabled(true);
				resetButton.setEnabled(true);
				copyCheckBox.setEnabled(true);
			}

			private void disableControls() {
				txtdbbackupdir.setEditable(false);
				restoreButton.setEnabled(false);
				browseButton.setEnabled(false);
				resetButton.setEnabled(false);
				copyCheckBox.setEnabled(false);
			}

		});

		progressBar = new JProgressBar();

		textArea = new JTextArea();

		progressBarlabel = new JLabel(".");

		JLabel lblDbIpOf = new JLabel("DB IP of Backup :");

		JLabel lblDbUsernameOf = new JLabel("UserName of Backup :");

		txtdbip = new JTextField();
		txtdbip.setEditable(false);
		txtdbip.setColumns(10);

		txtusername = new JTextField();
		txtusername.setEditable(false);
		txtusername.setColumns(10);

		btnShowLog = new JButton("Show Log");
		btnShowLog.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				openLogFile();
			}

		});

		btnShowLog.setFont(new Font("Tahoma", Font.PLAIN, 14));

		resetButton = new JButton("Reset");
		resetButton.setToolTipText("Click here to reset all fields");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtdbip.setText("");
				txtusername.setText("");
				txtdbbackupfile.setText("");
				textArea.setText("");
				txtdbbackupdir.setText("Select DB Backup Directory");
			}
		});

		resetButton.setFont(new Font("Tahoma", Font.PLAIN, 14));

		copyCheckBox = new JCheckBox("Copy");
		copyCheckBox.setToolTipText("Click this checkbox if you want to restore the backup to another db");
		copyCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (copyCheckBox.isSelected()) {
					txtdbip.setEditable(true);
					restoreButton.setText("Start Copying");
				} else {
					txtdbip.setEditable(false);
					restoreButton.setText("Start Restoration");
					txtdbip.setText(ipBackup);
				}
			}
		});

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout.createSequentialGroup()
										.addContainerGap(232, Short.MAX_VALUE)
										.addComponent(browseButton).addGap(151))
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(35)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(6)
																		.addComponent(
																				restoreButton,
																				GroupLayout.PREFERRED_SIZE,
																				139,
																				GroupLayout.PREFERRED_SIZE)
																		.addGap(1)
																		.addComponent(
																				btnShowLog,
																				GroupLayout.PREFERRED_SIZE,
																				121,
																				GroupLayout.PREFERRED_SIZE)
																		.addGap(1)
																		.addComponent(
																				resetButton,
																				GroupLayout.PREFERRED_SIZE,
																				95,
																				GroupLayout.PREFERRED_SIZE))
														.addGroup(
																groupLayout
																		.createParallelGroup(
																				Alignment.LEADING,
																				false)
																		.addComponent(
																				progressBar,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addGroup(
																				groupLayout
																						.createSequentialGroup()
																						.addGroup(
																								groupLayout
																										.createParallelGroup(
																												Alignment.TRAILING)
																										.addComponent(
																												lblSelectDbBackup)
																										.addComponent(
																												lblDatabaseRestoration)
																										.addGroup(
																												groupLayout
																														.createSequentialGroup()
																														.addGroup(
																																groupLayout
																																		.createParallelGroup(
																																				Alignment.LEADING)
																																		.addComponent(
																																				lblDbUsernameOf,
																																				GroupLayout.PREFERRED_SIZE,
																																				128,
																																				GroupLayout.PREFERRED_SIZE)
																																		.addComponent(
																																				lblDbIpOf)
																																		.addComponent(
																																				lblDbBackAvailable))
																														.addGap(9)))
																						.addGap(18)
																						.addGroup(
																								groupLayout
																										.createParallelGroup(
																												Alignment.LEADING)
																										.addComponent(
																												txtdbbackupdir,
																												GroupLayout.PREFERRED_SIZE,
																												169,
																												GroupLayout.PREFERRED_SIZE)
																										.addComponent(
																												txtdbbackupfile,
																												GroupLayout.PREFERRED_SIZE,
																												GroupLayout.DEFAULT_SIZE,
																												GroupLayout.PREFERRED_SIZE)
																										.addGroup(
																												groupLayout
																														.createParallelGroup(
																																Alignment.TRAILING)
																														.addComponent(
																																txtusername,
																																Alignment.LEADING,
																																GroupLayout.PREFERRED_SIZE,
																																GroupLayout.DEFAULT_SIZE,
																																GroupLayout.PREFERRED_SIZE)
																														.addGroup(
																																Alignment.LEADING,
																																groupLayout
																																		.createSequentialGroup()
																																		.addComponent(
																																				txtdbip,
																																				GroupLayout.PREFERRED_SIZE,
																																				GroupLayout.DEFAULT_SIZE,
																																				GroupLayout.PREFERRED_SIZE)
																																		.addGap(18)
																																		.addComponent(
																																				copyCheckBox))))))
														.addComponent(
																textArea,
																GroupLayout.PREFERRED_SIZE,
																343,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																progressBarlabel,
																GroupLayout.PREFERRED_SIZE,
																255,
																GroupLayout.PREFERRED_SIZE))
										.addContainerGap(52, Short.MAX_VALUE)));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(22)
										.addComponent(lblDatabaseRestoration)
										.addGap(18)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblSelectDbBackup)
														.addComponent(
																txtdbbackupdir,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addComponent(browseButton)
										.addGap(26)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																txtdbip,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(lblDbIpOf)
														.addComponent(
																copyCheckBox))
										.addGap(18)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																lblDbUsernameOf)
														.addComponent(
																txtusername,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addGap(13)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																txtdbbackupfile,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																lblDbBackAvailable))
										.addPreferredGap(
												ComponentPlacement.RELATED,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																Alignment.TRAILING,
																groupLayout
																		.createSequentialGroup()
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								restoreButton,
																								Alignment.TRAILING,
																								GroupLayout.PREFERRED_SIZE,
																								32,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								btnShowLog,
																								Alignment.TRAILING,
																								GroupLayout.PREFERRED_SIZE,
																								32,
																								GroupLayout.PREFERRED_SIZE))
																		.addGap(8)
																		.addComponent(
																				progressBarlabel)
																		.addPreferredGap(
																				ComponentPlacement.RELATED))
														.addGroup(
																Alignment.TRAILING,
																groupLayout
																		.createSequentialGroup()
																		.addComponent(
																				resetButton,
																				GroupLayout.PREFERRED_SIZE,
																				32,
																				GroupLayout.PREFERRED_SIZE)
																		.addGap(30)))
										.addComponent(progressBar,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addComponent(textArea,
												GroupLayout.PREFERRED_SIZE,
												104, GroupLayout.PREFERRED_SIZE)
										.addContainerGap(
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		setLayout(groupLayout);

	}

	Image image = (new ImageIcon(ProjectConstants.backgroundImage)).getImage();
	private JButton btnShowLog;

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null)
			g.drawImage(image, (this.getWidth() / 2)
					- (image.getWidth(this) / 2), (this.getHeight() / 2)
					- (image.getHeight(this) / 2), image.getWidth(this),
					image.getHeight(this), this);

	}

	private void clearFiles() {
		textArea.append("\n" + "Clearing Data");
		String batFileParams = ProjectConstants.clearBatFile;
		RunBatchPrograms.execBatFile(System.getProperty("user.dir")
				+ batFileParams, null);

	}

	public static void main(String[] args) throws IOException, InterruptedException {

		long startTime1  = System.currentTimeMillis();
		Thread.sleep(71000);
		long endTime1  = System.currentTimeMillis();
		System.out.println(ApplicationUtil.timeTaken(startTime1,endTime1));

	}

	private static void copyFiles() {
		// textArea.append("\n"+"Moving files to specified directory");
		File srcdir = new File(txtdbbackupdir.getText());
		File destdir = new File(System.getProperty("user.dir")
				+ "\\arcMain\\data");
		System.out.println(txtdbbackupdir.getText());
		System.out.println(System.getProperty("user.dir") + "\\arcMain\\data");
		if (srcdir.isDirectory()) {
			File[] content = srcdir.listFiles();
			for (int i = 0; i < content.length; i++) {
				try {
					FileUtils.copyDirectory(srcdir, destdir);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println(content[i].getName());
			}
		}

	}

	private void checkConnection() {
		Connection con = null;
		while (con == null) {
			try {

				copyFiles();
				Properties prop = new Properties();
				InputStream input = null;
				File file = new File(System.getProperty("user.dir")
						+ ProjectConstants.backupInfofile);
				input = new FileInputStream(file);
				prop.load(input);
				passwordBackup = prop.getProperty("DB_Password");
				Class.forName("com.teradata.jdbc.TeraDriver");
				String connectionstring = "jdbc:teradata://"
						+ txtdbip.getText() + "/TMODE=TERA";
				con = DriverManager.getConnection(connectionstring,
						prop.getProperty("DB_User"),
						prop.getProperty("DB_Password"));
				/*
				 * String str="jdbc:teradata://153.65.183.79/TMODE=TERA"; con =
				 * DriverManager.getConnection(str, "AL186021_01",
				 * "AL186021_01");
				 */
				if (con != null) {
					textArea.setText("Connection Successful");

					// to check whether db is empty
					// Reading Parameters from DB_Info file
					String noofpresenttables = null;
					PreparedStatement stmt = con
							.prepareStatement("SELECT count(*) from dbc.tablesv where databaseName LIKE '"
									+ prop.getProperty("DB_User")
									+ "%' and CreatorName ='"
									+ prop.getProperty("DB_User") + "'");
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						noofpresenttables = rs.getString(1);
					}
					if (noofpresenttables.equals("0")) {
						if (copyCheckBox.isSelected())
							createCopyScript(txtdbip.getText(),
									prop.getProperty("DB_User"),
									prop.getProperty("DB_Password"),
									prop.getProperty("Avail_DB"),
									prop.getProperty("DB_Backup_file"));
						else
							createRestoreScript(prop.getProperty("DB_IP"),
									prop.getProperty("DB_User"),
									prop.getProperty("DB_Password"),
									prop.getProperty("Avail_DB"),
									prop.getProperty("DB_Backup_file"));
					} else {
						JOptionPane.showMessageDialog(null, "DB is not empty");
						con = null;
					}
					// To do pass dbs to restore
				} else
					JOptionPane.showMessageDialog(null,
							"Invalid DB Credentials1");
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Invalid DB Credentials2");
				throw new IllegalArgumentException(
						"connection Requirements are missing", e);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Invalid DB Credentials3");
				throw new IllegalArgumentException(
						"some problem occured in connection", e);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

		}
	}

	private static void createRestoreScript(String DB_IP, String DB_Username,
			String DB_Password, String AvailDB, String DB_Backupfile) {
		// textArea.append("\n"+"Creating Restore Script");
		try {
			File file = new File(System.getProperty("user.dir")
					+ ProjectConstants.restorescript);
			FileWriter ff = new FileWriter(file); // here true is for appending
			PrintWriter pw = new PrintWriter(ff);
			// pw.write(".logon "+txtdbip.getText()+"/"+txtusername.getText()+","+txtpassword.getText()+";"+"\n\n"+" ARCHIVE DATA TABLES "+DBstoarchive+",ABORT, RELEASE LOCK, FILE ="+txtbackupfile.getText()+";"+"\n\n"+" LOGOFF;"+"\n\n"+" .QUIT;");
			pw.write(".logon " + DB_IP + "/" + DB_Username + "," + DB_Password
					+ ";\n\nrestore DATA TABLES " + AvailDB
					+ ",ABORT, RELEASE LOCK, FILE = " + DB_Backupfile
					+ ";\n\nrevalidate references for " + AvailDB
					+ " ALL,RELEASE LOCK;\n\nrelease lock " + AvailDB
					+ " all;\n\nLOGOFF;\n\n.QUIT;");
			pw.close();
			ff.close();
			System.out.println("filewritten successfully");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void createCopyScript(String DB_IP, String DB_Username,
			String DB_Password, String AvailDB, String DB_Backupfile) {
		// textArea.append("\n"+"Creating Restore Script");
		try {
			File file = new File(System.getProperty("user.dir")
					+ ProjectConstants.copyscript);
			FileWriter ff = new FileWriter(file);
			PrintWriter pw = new PrintWriter(ff);

			String tempArray[] = AvailDB.split(",");
			String copyString = "";
			for (int i = 0; i < tempArray.length; i++)
				if (copyString != "")
					copyString += "," + tempArray[i] + " (FROM " + tempArray[i]
							+ " , NO FALLBACK)";
				else
					copyString += tempArray[i] + " (FROM " + tempArray[i]
							+ " , NO FALLBACK)";

			pw.write(".logon " + DB_IP + "/" + DB_Username + "," + DB_Password
					+ ";\n\ncopy DATA TABLES " + copyString
					+ ",ABORT, RELEASE LOCK, FILE = " + DB_Backupfile
					+ ";\n\nrevalidate references for " + AvailDB
					+ " ALL,RELEASE LOCK;\n\nrelease lock " + AvailDB
					+ " all;\n\nLOGOFF;\n\n.QUIT;");
			pw.close();
			ff.close();
			System.out.println("filewritten successfully");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void ReadDbInfoFilie() {

		Properties prop = new Properties();
		InputStream input = null;
		File file = new File(System.getProperty("user.dir")
				+ ProjectConstants.backupInfofile);
		try {
			input = new FileInputStream(file);
			prop.load(input);
			prop.getProperty("DB_IP");
			prop.getProperty("DB_User");
			prop.getProperty("DB_Password");
			prop.getProperty("Multitable");
			prop.getProperty("Avail_DB");
			prop.getProperty("DB_Backup_file");
			System.out.println(prop.getProperty("DB_IP"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

	}

	public static void test() {
		Connection con = null;
		while (con == null) {
			try {
				Class.forName("com.teradata.jdbc.TeraDriver");
				String connectionstring = "jdbc:teradata://153.65.183.79/TMODE=TERA";
				con = DriverManager.getConnection(connectionstring,
						"MDMUSER04", "MDMUSER04");
				PreparedStatement stmt = con
						.prepareStatement("SELECT count(*) from dbc.tablesv where databaseName LIKE 'MDMUSER05%'");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					System.out.println(rs.getString(1));
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

			}
		}
	}

	public static void openLogFile() {
		File file = new File(System.getProperty("user.dir") + ProjectConstants.Restorelogfile);
		if(!file.exists()){
			JOptionPane.showMessageDialog(null, "Sorry, File not present");
			return;
		}

		Runtime runtime = Runtime.getRuntime();
		try {
			// Process process =
			// runtime.exec("C:\\path\\to\\notepad.exe C:\\path\\to\\file.txt");
			Process process = runtime.exec("C:\\Windows\\notepad.exe "
					+ System.getProperty("user.dir")
					+ ProjectConstants.Restorelogfile);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Sorry, File not present");
			e.printStackTrace();
		}
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
