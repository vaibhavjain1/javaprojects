package com.teradata.restore.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;

import com.teradata.config.ApplicationUtil;
import com.teradata.config.ProjectConstants;
import com.teradata.config.RunBatchPrograms;

public class DbDeletePanel extends JPanel {
	private static JTextField txtdbip;
	private static JTextField txtdbusername;
	private static JPasswordField txtpassword;
	private JButton btnDeleteDb;
	private JTextArea textArea;
	private JProgressBar progressBar;
	private JLabel progressBarlabel;
	private Thread task;
	private int issuccessful;
	private JCheckBox fastdeletecheck;
	private long startTime;
	private long endTime;

	/**
	 * Create the panel.
	 */
	public DbDeletePanel() {

		JLabel label = new JLabel("Database Login Details");
		label.setForeground(Color.BLUE);

		JLabel label_1 = new JLabel("Database IP :");

		txtdbip = new JTextField();
		txtdbip.setColumns(10);

		JLabel label_2 = new JLabel("DB Login User :");

		txtdbusername = new JTextField();
		txtdbusername.setColumns(10);

		txtpassword = new JPasswordField();
		txtpassword.setEchoChar('*');
		txtpassword.setColumns(10);

		JLabel label_3 = new JLabel("DB Login Password :");
		fastdeletecheck = new JCheckBox("Fast Delete");
		fastdeletecheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					progressBarlabel
							.setText(ProjectConstants.dbDirectdeletewarning);
				} else {
					progressBarlabel.setText(" ");
				}
			}
		});
		btnDeleteDb = new JButton("Delete DB");
		btnDeleteDb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (txtdbip.getText().trim().equals("")
						|| txtdbusername.getText().trim().equals("")
						|| txtpassword.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please fill all required fields first");
				} else if (txtdbusername.getText().trim().equals("dbc")
						|| txtdbusername.getText().trim().equals("DBC")) {
					JOptionPane.showMessageDialog(null, "Are You Nuts ??");
				} else {
					checkConnection();
					clearFiles();
					createLoginSqlFile();
					startBarProgress();

					task = new Thread(new Runnable() {

						public void run() {
							if (fastdeletecheck.isSelected()) {

								fastdelete();
								stopBarProgress();
							} else {
								deleteDb();
								deleteResidual();
								stopBarProgress();
							}
						}
					});
					task.start();
				}
			}
		});

		textArea = new JTextArea();
		textArea.setEditable(false);

		progressBar = new JProgressBar();

		progressBarlabel = new JLabel(".");

		JButton btnShowLog = new JButton("Show Log");
		btnShowLog.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				openLogFile();
			}

		});

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(31)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(label_3, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED))
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
										.addGap(67)))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)))
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(txtdbusername, GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
								.addComponent(txtpassword, GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
								.addComponent(txtdbip, GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
								.addComponent(fastdeletecheck)))
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(progressBar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(progressBarlabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(textArea, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 283, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED, 3, Short.MAX_VALUE)))
					.addGap(133))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(75)
					.addComponent(btnDeleteDb, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnShowLog, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(167, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(84)
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(227, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(29)
					.addComponent(label)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(label_1)
						.addComponent(txtdbip, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(11)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtdbusername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label_2))
					.addGap(16)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_3)
						.addComponent(txtpassword, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(fastdeletecheck)
					.addGap(11)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnDeleteDb)
						.addComponent(btnShowLog))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(progressBarlabel)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		setLayout(groupLayout);

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

	private void clearFiles() {
		textArea.append("\n" + "Clearing Data");
		String batFileParams = ProjectConstants.clearBatFile;
		RunBatchPrograms.execBatFile(System.getProperty("user.dir")
				+ batFileParams, null);
	}

	private void deleteResidual() {
		// TODO Auto-generated method stub
		try {
			Connection con = null;
			Class.forName("com.teradata.jdbc.TeraDriver");
			String connectionstring = "jdbc:teradata://" + txtdbip.getText()
					+ "/TMODE=TERA";
			con = DriverManager.getConnection(connectionstring,
					txtdbusername.getText(), txtpassword.getText());
			if (con != null) {
				textArea.append("\n" + "Cleaning Residual tables & Jars");
				PreparedStatement stmt = con.prepareStatement("delete user "
						+ txtdbusername.getText() + ";");
				stmt.execute();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void enableControls() {
		txtdbip.setEditable(true);
		btnDeleteDb.setEnabled(true);
		txtdbusername.setEditable(true);
		txtpassword.setEditable(true);
	}

	private void disableControls() {
		txtdbip.setEditable(false);
		btnDeleteDb.setEnabled(false);
		txtdbusername.setEditable(false);
		txtpassword.setEditable(false);
	}

	private void checkConnection() {
		Connection con = null;
		while (con == null) {
			try {
				Class.forName("com.teradata.jdbc.TeraDriver");
				String connectionstring = "jdbc:teradata://"
						+ txtdbip.getText() + "/TMODE=TERA";
				con = DriverManager.getConnection(connectionstring,
						txtdbusername.getText(), txtpassword.getText());
				/*
				 * String str="jdbc:teradata://153.65.183.79/TMODE=TERA"; con =
				 * DriverManager.getConnection(str, "AL186021_01",
				 * "AL186021_01");
				 */
				if (con != null)
					textArea.setText("Connection Successful");
				else
					JOptionPane.showMessageDialog(null,
							"Invalid DB Credentials");

				String noofpresenttables = null;
				PreparedStatement stmt = con
						.prepareStatement("SELECT count(*) from dbc.tablesv where databaseName LIKE '"
								+ txtdbusername.getText()
								+ "%' and CreatorName ='"
								+ txtdbusername.getText() + "'");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					noofpresenttables = rs.getString(1);
				}
				if (noofpresenttables.equals("0")) {
					JOptionPane.showMessageDialog(null, "DB is already empty");
					con = null;
				}
				if (con != null) {
					if (JOptionPane.showConfirmDialog(null,
							"Are you sure you want to delete db : "
									+ txtdbusername.getText(), "WARNING",
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						// yes option
					} else {
						// no option
						con = null;
					}
				}

			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Invalid DB Credentials");
				throw new IllegalArgumentException(
						"connection Requirements are missing", e);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Invalid DB Credentials");
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

	public static void openLogFile() {
		File file = new File(System.getProperty("user.dir") + ProjectConstants.Droplogfile);
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
					+ ProjectConstants.Droplogfile);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Sorry, File not present");
			e.printStackTrace();
		}
	}

	public void startBarProgress() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				disableControls();
				textArea.append("\n" + "Deleting database");
				progressBarlabel.setText("Deleting DB");
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
				endTime = System.currentTimeMillis();
				enableControls();
				textArea.append("\n" + "Deletion of database Completed");
				progressBar.setIndeterminate(false);
				progressBar.setString("100 %");
				progressBar.setValue(100);
				progressBarlabel.setText("Completed.");
				if(issuccessful == 0|| !fastdeletecheck.isSelected())
					JOptionPane.showMessageDialog(null, "          DB Deleted\n"+ApplicationUtil.timeTaken(startTime, endTime));
				else
					JOptionPane.showMessageDialog(null, "Problem in DB Deletion.\nPlease delete Objects(e.g JOIN Indexes/Ext.Sp's first.");
			}
		});
	}

	public void deleteDb() {
		String batFileParams = ProjectConstants.dbdeleteBatFile;
		issuccessful = RunBatchPrograms.execBatFile(System.getProperty("user.dir")
				+ batFileParams, null);
	}

	public void fastdelete() {
		String batFileParams = ProjectConstants.dbdeleteDirectBatFile;
		issuccessful = RunBatchPrograms.execBatFile(System.getProperty("user.dir")
				+ batFileParams, null);
	}

	public static void main(String[] args) {

	}

	public static void createLoginSqlFile() {
		try {
			File file = new File(System.getProperty("user.dir")
					+ ProjectConstants.logininfofile);
			FileWriter ff = new FileWriter(file); // here true is for appending
			PrintWriter pw = new PrintWriter(ff);
			// pw.write(".logon "+txtdbip.getText()+"/"+txtusername.getText()+","+txtpassword.getText()+";"+"\n\n"+" ARCHIVE DATA TABLES "+DBstoarchive+",ABORT, RELEASE LOCK, FILE ="+txtbackupfile.getText()+";"+"\n\n"+" LOGOFF;"+"\n\n"+" .QUIT;");
			pw.write(".logon " + txtdbip.getText() + "/"
					+ txtdbusername.getText() + "," + txtpassword.getText());
			pw.close();
			ff.close();
			System.out.println("filewritten successfully");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
