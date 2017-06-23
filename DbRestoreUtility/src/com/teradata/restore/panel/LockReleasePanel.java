package com.teradata.restore.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;

import com.teradata.config.ProjectConstants;
import com.teradata.config.RunBatchPrograms;

public class LockReleasePanel extends JPanel {
	private JTextField txtdbip;
	private JTextField txtusername;
	private JPasswordField txtpassword;
	private JTextArea textArea;
    private Thread task;
    private JLabel progressLabel;
    private JButton btnReleaseLock;
	/**
	 * Create the panel.
	 */
	public LockReleasePanel() {
		
		JLabel label = new JLabel("Database IP :");
		
		JLabel label_1 = new JLabel("Database Login Details");
		label_1.setForeground(Color.BLUE);
		
		txtdbip = new JTextField();
		txtdbip.setColumns(10);
		
		JLabel label_2 = new JLabel("DB Login User :");
		
		txtusername = new JTextField();
		txtusername.setColumns(10);
		
		progressLabel = new JLabel(".");
		
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		JLabel lblLoginPassword = new JLabel("Login Password :");
		
		txtpassword = new JPasswordField();
		txtpassword.setEchoChar('*');
		txtpassword.setColumns(10);
		
		btnReleaseLock = new JButton("Release Lock");
		btnReleaseLock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(txtdbip.getText().trim().equals("") || txtusername.getText().trim().equals(""))
					progressLabel.setText("Please enter all required fields");
				else if(txtusername.getText().trim().equals("dbc")|| txtusername.getText().trim().equals("DBC"))
				{
					JOptionPane.showMessageDialog(null, "Are You Nuts ??");
				}
				else
				{
					checkConnection();
					startProgress();
					task = new Thread(new Runnable(){

						public void run() {
							releaseDbLock();
							stopProgress();
						}});
					task.start();
					}
			}
		});
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(52)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 253, GroupLayout.PREFERRED_SIZE)
						.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
							.addGap(73)
							.addComponent(txtdbip, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
							.addGap(73)
							.addComponent(txtusername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblLoginPassword, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
							.addGap(57)
							.addComponent(txtpassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(53)
							.addComponent(btnReleaseLock))
						.addComponent(progressLabel, GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE))
					.addGap(145))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(28)
					.addComponent(label_1)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(label))
						.addComponent(txtdbip, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(label_2))
						.addComponent(txtusername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblLoginPassword)
						.addComponent(txtpassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnReleaseLock, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
					.addGap(17)
					.addComponent(progressLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
					.addGap(22))
		);
		setLayout(groupLayout);

	}
	
	Image image = (new ImageIcon(ProjectConstants.backgroundImage)).getImage();
	

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(image != null) 
			g.drawImage(
					image, 
					(this.getWidth()/2) - (image.getWidth(this) / 2),
					(this.getHeight()/2) - (image.getHeight(this) / 2),
					image.getWidth(this),
					image.getHeight(this),
					this); 
		
	}
	
	public void releaseDbLock()
	{
		String batFileParams = ProjectConstants.releaselockbatfile;
		RunBatchPrograms.execBatFile(System.getProperty("user.dir")+batFileParams,null);
	}
	
	public void startProgress()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				btnReleaseLock.setEnabled(false);
				disableControls();
				textArea.append("\n"+"Unlocking database");
				progressLabel.setText("Unlocking DB");
			}

		});
	}
	
	private void disableControls() {
		// TODO Auto-generated method stub
		txtdbip.setEditable(false);
		txtpassword.setEditable(false);
		txtusername.setEditable(false);
		btnReleaseLock.setEnabled(false);
	}
	
	public void stopProgress()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				btnReleaseLock.setEnabled(true);
				enableControls();
				textArea.append("\n"+"Database Unlocked");
				progressLabel.setText("Completed.");
			}
		});
	}
	
	private void enableControls() {
		// TODO Auto-generated method stub
		txtdbip.setEditable(true);
		txtpassword.setEditable(true);
		txtusername.setEditable(true);
		btnReleaseLock.setEnabled(true);
	}
	public void checkConnection()
	{    
		 Connection con=null;
		 while (con == null) {
			try {
				Class.forName("com.teradata.jdbc.TeraDriver");
				String connectionstring = "jdbc:teradata://"+ txtdbip.getText() + "/TMODE=TERA";
				con = DriverManager.getConnection(connectionstring,	txtusername.getText(), txtpassword.getText());
				if (con != null)
				{   
					textArea.setText("Connection Created");
					progressLabel.setText("Completed.");
					createArchiveScript();
				}
				else
					JOptionPane.showMessageDialog(null, "Invalid DB Credentials");
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		   }
			 
		 }
	}
	
	public void createArchiveScript()
	{
		textArea.append("\n"+"Creating DB Unlock Script");
		try {
			File file=new File(System.getProperty("user.dir")+ProjectConstants.lockReleasecript);
			FileWriter ff=new FileWriter(file); //here true is for appending
			PrintWriter pw=new PrintWriter(ff);		
			pw.write(".logon "+txtdbip.getText()+"/"+"dbc"+","+"dbc"+";"+"\n\n"+"RELEASE LOCK ("+txtusername.getText()+"),OVERRIDE;"+"\n\n"+" LOGOFF;"+"\n\n"+" .QUIT;");
			pw.close();
			ff.close();
			System.out.println("filewritten successfully");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
