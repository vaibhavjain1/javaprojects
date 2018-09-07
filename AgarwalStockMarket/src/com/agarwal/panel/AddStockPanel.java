package com.agarwal.panel;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;

import com.agarwal.config.ConnectionClass;
import com.agarwal.config.ProjectConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddStockPanel extends JPanel{

	private JFrame frame;
	private JTextField textField;

	

	/**
	 * Create the application.
	 */
	public AddStockPanel() {
		
		JLabel lblAddStock = new JLabel("Add Stock");
		lblAddStock.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
		
		JLabel lblStockName = new JLabel("Stock Name");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel lblStockDescription = new JLabel("Stock Description");
		
		JTextArea textArea = new JTextArea();
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textField!=null){
					ResultSet rs = null;
					try(Connection con = ConnectionClass.getDataSource().getConnection();
						Statement stmt = con.createStatement();) {
						rs = stmt.executeQuery("INSERT INTO STOCKS(STOCK_NAME) VALUES ('"+textField.getText()+"');");
					} catch (SQLException e1) {
						e1.printStackTrace();
					} finally {
						try {
							rs.close();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
					
			}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(168)
							.addComponent(lblAddStock, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblStockName)
								.addComponent(lblStockDescription))
							.addGap(43)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(textField)
								.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE))))
					.addContainerGap(90, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(145)
					.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(194, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblAddStock, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
					.addGap(38)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblStockName))
					.addGap(39)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblStockDescription))
					.addGap(35)
					.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(45, Short.MAX_VALUE))
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
}
