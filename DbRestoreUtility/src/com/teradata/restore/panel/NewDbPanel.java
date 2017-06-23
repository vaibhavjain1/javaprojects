package com.teradata.restore.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.teradata.config.ProjectConstants;
import com.teradata.config.RunBatchPrograms;

@SuppressWarnings("serial")
public class NewDbPanel extends JPanel {
	private JTextField txtDbIp;
	private JTextField txtNewEntity;
	private JTextField txtnewDbNoChildSpace;
	private JTextArea infoTextArea;
	private JComboBox<String> parentCombo;
	private JLabel newLabel;
	private JLabel lblSpace;
	private JLabel lblMb;
	private JRadioButton nonTopRadio;
	private JRadioButton topRadio;
	private JRadioButton newMDMuserRadio;
	private JRadioButton newDatabaseRadio; 
	private JButton createButton;
	private JButton clearButton;
	private JButton logButton;
	private List<String> DbNamesList = new ArrayList<String>();
	private String Fetching = "Fetching DB Names...";
	private Connection con;

	/**
	 * Create the panel.
	 */
	public NewDbPanel() {
		
		JLabel lblDatabaseDetails = new JLabel("Database Details");
		lblDatabaseDetails.setForeground(Color.BLUE);
		
		JLabel lblDatabaseIp = new JLabel("DB IP :");
		
		JLabel lblParentDatabase = new JLabel("Create :");
		
		JLabel lblCreate = new JLabel("Parent :");
		
		newMDMuserRadio = new JRadioButton("New MDM User");
		
		newDatabaseRadio = new JRadioButton("New Database");
		
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(newMDMuserRadio);
		radioGroup.add(newDatabaseRadio);
		
		newMDMuserRadio.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(newMDMuserRadio.isSelected()){
					newLabel.setText("UserName :");
					nonTopRadio.setVisible(true);
					topRadio.setVisible(true);
					lblSpace.setVisible(false);
					txtnewDbNoChildSpace.setVisible(false);
					lblMb.setVisible(false);
				}
			}
		});
		
		newDatabaseRadio.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(newDatabaseRadio.isSelected()){
					newLabel.setText("DB Name :");
					nonTopRadio.setVisible(false);
					topRadio.setVisible(false);
					lblSpace.setVisible(true);
					txtnewDbNoChildSpace.setVisible(true);
					lblMb.setVisible(true);
				}
			}
		});
		
		txtDbIp = new JTextField();
		txtDbIp.setColumns(10);
		
		txtDbIp.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				populateParentDropDown();
			}
			@Override
			public void focusGained(FocusEvent e) {	
				infoTextArea.setText("");
				parentCombo.removeAllItems();
				createButton.setEnabled(false);
			}
			
		});
		
		parentCombo = new JComboBox<String>();
		
		newLabel = new JLabel("New Entity:");
		
		txtNewEntity = new JTextField();
		txtNewEntity.setColumns(10);
		
		nonTopRadio = new JRadioButton("Non-Topology");
		nonTopRadio.setSelected(true);
		
		topRadio = new JRadioButton("Topology");
		nonTopRadio.setVisible(false);
		topRadio.setVisible(false);
		
		ButtonGroup radioTopologyGroup1 = new ButtonGroup();
		radioTopologyGroup1.add(nonTopRadio);
		radioTopologyGroup1.add(topRadio);
		
		createButton = new JButton("Create");
		
		clearButton = new JButton("Clear");
		
		logButton = new JButton("Show Log");
		
		infoTextArea = new JTextArea();
		infoTextArea.setEditable(false);
		infoTextArea.setColumns(10);
		
		createButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (txtDbIp.getText().trim().equals(""))
					JOptionPane.showMessageDialog(null, "Please enter DB IP");
				else if(!newMDMuserRadio.isSelected() && !newDatabaseRadio.isSelected())
					JOptionPane.showMessageDialog(null, "Please Select New User or New Db");
				else if(txtNewEntity.getText().trim().equals(""))
					JOptionPane.showMessageDialog(null, "Please enter new "+newLabel.getText());
				else if(txtNewEntity.getText().trim().length()<3)
					JOptionPane.showMessageDialog(null, "New "+newLabel.getText()+" must have length greater then 2");
				else if(txtNewEntity.getText().matches(".*?['\\-—¿?,.;:/ ].*"))
					JOptionPane.showMessageDialog(null,	"BackUp file Cannot contain special characters except _");
				else if(txtNewEntity.getText().trim().equalsIgnoreCase("dbc"))
					JOptionPane.showMessageDialog(null, "Are You Nuts ??");
				else if(newDatabaseRadio.isSelected() && (txtnewDbNoChildSpace.getText().trim().equals("")
					|| Double.parseDouble(txtnewDbNoChildSpace.getText()) < 50 || Double.parseDouble(txtnewDbNoChildSpace.getText()) > 5000))
					JOptionPane.showMessageDialog(null, "For New Db Min 50MB & Max 5000MB space is allowed");
				else if(checkUniqueDbName()){
					if(createDbScript()){
						createDb();
					}
				}
			}
		});
		
		logButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				openLogFile();
			}
		});
		
		clearButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				txtDbIp.setText("");
				txtNewEntity.setText("");
				parentCombo.removeAllItems();
				infoTextArea.setText("");
			}
		});
		
		JLabel lblNewLabel = new JLabel("              ");
		
		lblSpace = new JLabel("Space:");
		lblSpace.setVisible(false);
		
		txtnewDbNoChildSpace = new JTextField();
		txtnewDbNoChildSpace.setToolTipText("Please assign value between 50 - 5000");
		txtnewDbNoChildSpace.setText("50");
		txtnewDbNoChildSpace.setColumns(10);
		txtnewDbNoChildSpace.setVisible(false);
		
		lblMb = new JLabel("MB");
		lblMb.setVisible(false);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(148)
							.addComponent(lblDatabaseDetails))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addGroup(groupLayout.createSequentialGroup()
								.addGap(107)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addGroup(groupLayout.createSequentialGroup()
										.addGap(64)
										.addComponent(logButton)
										.addGap(26)
										.addComponent(clearButton))
									.addGroup(groupLayout.createSequentialGroup()
										.addGap(18)
										.addComponent(nonTopRadio)
										.addGap(10)
										.addComponent(topRadio)
										.addGap(6)
										.addComponent(lblSpace)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(txtnewDbNoChildSpace, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
										.addGap(2)
										.addComponent(lblMb)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
							.addGroup(groupLayout.createSequentialGroup()
								.addGap(72)
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
									.addComponent(lblDatabaseIp)
									.addComponent(createButton)
									.addComponent(newLabel)
									.addComponent(lblCreate)
									.addComponent(lblParentDatabase))
								.addGap(18)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addGroup(groupLayout.createSequentialGroup()
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
											.addComponent(txtDbIp)
											.addComponent(newMDMuserRadio, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(newDatabaseRadio))
									.addComponent(txtNewEntity, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
									.addComponent(parentCombo, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)))))
					.addGap(46))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(56)
					.addComponent(infoTextArea, GroupLayout.PREFERRED_SIZE, 342, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(116, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblDatabaseDetails)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDatabaseIp)
						.addComponent(txtDbIp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(newMDMuserRadio)
						.addComponent(newDatabaseRadio)
						.addComponent(lblParentDatabase))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCreate)
						.addComponent(parentCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(27)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(newLabel)
						.addComponent(txtNewEntity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(topRadio)
						.addComponent(nonTopRadio)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSpace)
						.addComponent(txtnewDbNoChildSpace, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblMb))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(createButton)
						.addComponent(clearButton)
						.addComponent(logButton))
					.addGap(18)
					.addComponent(infoTextArea, GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
					.addContainerGap())
		);
		setLayout(groupLayout);

	}
	
	public boolean checkUniqueDbName(){
		String DbName = txtNewEntity.getText();
		for (String str : DbNamesList) {
			if(str.equalsIgnoreCase(DbName.trim())){
				JOptionPane.showMessageDialog(null, "Already Exists");
				infoTextArea.append("\n"+DbName+" Already Exists.Select different one.");
				return false;
			}
		}
		return true;
	}
	
	public void populateParentDropDown(){
		if(!txtDbIp.getText().trim().equals("")){
			parentCombo.removeAllItems();
			parentCombo.addItem(Fetching);
		Thread task = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Class.forName("com.teradata.jdbc.TeraDriver");
					String connectionstring = "jdbc:teradata://"+ txtDbIp.getText() + "/TMODE=TERA";
					con = DriverManager.getConnection(connectionstring,"dbc", "dbc");
					infoTextArea.setText("");
					infoTextArea.setText("Connected to DB: "+txtDbIp.getText());
				} catch (Exception e1) {
					infoTextArea.setText("");
					infoTextArea.setText("DB IP is not Valid");
					con=null;
				}
				if(con!=null){
					try {
						Statement stmt = con.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT DatabaseName FROM DBC.Databases ORDER BY DatabaseName");
						parentCombo.removeItem(Fetching);
						while (rs.next()) {
							if(rs.getString(1)!=null)
							DbNamesList.add(rs.getString(1).trim());
							parentCombo.addItem(rs.getString(1).trim());
						}
						createButton.setEnabled(true);
					} catch (SQLException e) {
						infoTextArea.append("\nError While Fetching ParentDB Names");
					}
				}
			}
		});
		task.start();
		}
	}
	
	public boolean createDbScript(){
		String str,outtext;
		StringBuffer outbuff = new StringBuffer();
		FileWriter writer = null;
		BufferedReader template = null;
		infoTextArea.append("\nChecking Available Space.");
		try {
			if(newDatabaseRadio.isSelected()){
				infoTextArea.append("\nFor New DB Minimum 50MB & Maximum 5000MB is allowed.");
				if(checkAvailableSpace(Double.parseDouble(txtnewDbNoChildSpace.getText())*1024*1024,524288000L,524288000))
					template = new BufferedReader(new FileReader(System.getProperty("user.dir")+ProjectConstants.NoChildDBTemplate));
				else
					return false;
			}
			else if(nonTopRadio.isSelected()){
				infoTextArea.append("\nFor New Non Topology 500MB space is required.");
				if(checkAvailableSpace(524288000,2147483648L,524288000))
					template = new BufferedReader(new FileReader(System.getProperty("user.dir")+ProjectConstants.nonTopologyDbTemplate));
				else
					return false;
			}
			else if(topRadio.isSelected()){
				infoTextArea.append("\nFor New Topology 800MB space is required.");
				if(checkAvailableSpace(838860800,2147483648L,524288000))
					template = new BufferedReader(new FileReader(System.getProperty("user.dir")+ProjectConstants.TopologyDbTemplate));
				else
					return false;
			}
			infoTextArea.append("\nCreating Scripts");
			while((str = template.readLine())!=null)
	    	{
				if(str.contains("{DB_IP}"))
	    		{
	    			str=str.replace("{DB_IP}", txtDbIp.getText().toUpperCase());
	    		}
				if(str.contains("{PARENTDB}"))
	    		{
	    			str=str.replace("{PARENTDB}", parentCombo.getSelectedItem().toString());
	    		}
	    		if(str.contains("{NEWDB}"))
	    		{
	    			str=str.replace("{NEWDB}", txtNewEntity.getText().toUpperCase());
	    		}
	    		if(str.contains("{NEWDBNOCHILDSPACE}"))
	    		{
	    			str=str.replace("{NEWDBNOCHILDSPACE}", String.valueOf(Double.parseDouble(txtnewDbNoChildSpace.getText())*1024*1024));
	    		}
	    		outbuff.append(str + "\r\n");
	    	}
			writer = new FileWriter(System.getProperty("user.dir")+ProjectConstants.createDbscript);
			outtext=outbuff.toString();
			writer.write(outtext);
			writer.close();
		} catch (Exception e) {
			infoTextArea.append("\nError while Creating Scripts");
			return false;
		} 
		return true;
	}
	
	public boolean checkAvailableSpace(double requiredPermSpace,long requiredSpoolSpace,int requiredTempSpace){
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT SUM(MaxPerm - CurrentPerm) from dbc.diskspace where DatabaseName = '"+parentCombo.getSelectedItem().toString()+"'");
			if(rs.next()){
			 	if(rs.getInt(1) <= requiredPermSpace){
			 		JOptionPane.showMessageDialog(null, "Not Enough Perm Space available. Please assign: "+((requiredPermSpace-rs.getInt(1))/1024/1024+1)+" MB Perm space to parent or choose new parent.");
			 		return false;
			 	}
			}
			rs = stmt.executeQuery("SELECT SpoolSpace, TempSpace from DBC.databases WHERE DatabaseName = '"+parentCombo.getSelectedItem().toString()+"'");
			if(rs.next()){
			 	if(rs.getLong(1) <= requiredSpoolSpace){
			 		JOptionPane.showMessageDialog(null, "Not Enough Spool Space available. Please assign: "+((requiredSpoolSpace-rs.getLong(1))/1024/1024+1)+" MB Spool space to parent or choose new parent.");
			 		return false;
			 	}
			 	if(rs.getInt(2) <= requiredTempSpace){
			 		JOptionPane.showMessageDialog(null, "Not Enough Temp Space available. Please assign: "+((requiredTempSpace-rs.getInt(2))/1024/1024+1)+" MB Temp space to parent or choose new parent.");
			 		return false;
			 	}
			}
		} catch (SQLException e) {
			infoTextArea.append("\nError while checking available space");
			return false;
		} finally{
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public void openLogFile() {
		if (txtDbIp != null) {
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("C:\\Windows\\notepad.exe " + System.getProperty("user.dir")+ProjectConstants.CreateDblogfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			JOptionPane
					.showMessageDialog(null, "Sorry, Log file not available");
	}
	
	public void createDb(){
		infoTextArea.append("\nStarting Process.");
		String batFileParams = ProjectConstants.createDbbatfile;
		String additionalGrants = null;
		String grantDbs[] = null;
		int issuccessful;
		issuccessful = RunBatchPrograms.execBatFile(
				System.getProperty("user.dir") + batFileParams, null);
		if(issuccessful==0){
			infoTextArea.append("\nProcess Successful.");
			if(newMDMuserRadio.isSelected())
				JOptionPane.showMessageDialog(null, "User "+txtNewEntity.getText()+" successfully created.");
			else if(newDatabaseRadio.isSelected()){
				additionalGrants = JOptionPane.showInputDialog(null, "DB "+txtNewEntity.getText()+" Successfully created.\nDo you want to provide access of this DB?\nProvide Name of DB seperated By comma(,)", "DB Created", JOptionPane.INFORMATION_MESSAGE);
				if(additionalGrants!=null){
					grantDbs = additionalGrants.split(",");
					try {
						Statement stmt = con.createStatement();
						for (String string : grantDbs) {
							if(!string.trim().equalsIgnoreCase("")){
								infoTextArea.append("\nProviding Access to following Entities\n");
								try {
									stmt.execute("GRANT ALL ON "+txtNewEntity.getText()+" TO "+string);
									infoTextArea.append(string+" ");
								} catch (Exception e) {
									if(!string.trim().equalsIgnoreCase("")){
										infoTextArea.append("error: "+string+" ");
									}
								}
							}
						}
					} catch (SQLException e) {
						infoTextArea.append("\nError while giving grants.");
					} 	
				}
			}
		}
		else{
			infoTextArea.append("\nProcess Failed.");
			JOptionPane.showMessageDialog(null, "Error while creating DB. Please check logs");
		}
		if(con!=null)
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
}
