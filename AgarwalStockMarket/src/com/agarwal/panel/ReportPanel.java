package com.agarwal.panel;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import sun.org.mozilla.javascript.internal.ObjArray;

import com.agarwal.config.ConnectionClass;
import com.agarwal.config.ProjectConstants;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportPanel extends JPanel{
	private JTable table;
	private DefaultTableModel dtml;
	public ReportPanel() {
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton btnShowReport = new JButton("Show Report");
		btnShowReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dtml.setRowCount(0);
				Statement stmt = null;
				ResultSet rs = null;
				try(Connection con = ConnectionClass.getDataSource().getConnection();) {
					stmt = con.createStatement();
					rs = stmt.executeQuery("SELECT * FROM STOCK_ENTRY");
					while (rs.next()) {
						dtml.addRow(new Object[]{rs.getString(2),rs.getInt(3),rs.getInt(4)});
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				} finally {
					try {
						stmt.close();
						rs.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
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
							.addGap(156)
							.addComponent(btnShowReport))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(22)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(26, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnShowReport)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(170, Short.MAX_VALUE))
		);
		
		table = new JTable();
					
		dtml = new DefaultTableModel(
				new Object[][] {
						{null, null, null, null, null},
						{null, null, null, null, null},
						{null, null, null, null, null},
						{null, null, null, null, null},
					},
					new String[] {
						"Scrip Name", "Quantity", "Price/Unit", "Brokerage/Unit", "Net Total"
					});
		
		table.setModel(dtml);
		
		scrollPane.setViewportView(table);
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
