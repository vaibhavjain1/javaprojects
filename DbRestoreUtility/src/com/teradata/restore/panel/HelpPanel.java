package com.teradata.restore.panel;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.teradata.config.ProjectConstants;

@SuppressWarnings("serial")
public class HelpPanel extends JPanel {
	/**
	 * Create the panel.
	 */
	public HelpPanel() {
		
		JButton btnViewReadme = new JButton("View ReadMe");
		btnViewReadme.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				/*Runtime runtime = Runtime.getRuntime();
					try {
						//Process process = runtime.exec("C:\\Windows\\notepad.exe "+System.getProperty("user.dir")+ProjectConstants.Helpfile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}*/
				
				File htmlFile = new File(System.getProperty("user.dir")+ProjectConstants.HelpfileHtm);
				try {
					Desktop.getDesktop().browse(htmlFile.toURI());
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		
		});
		
		JTextArea txtrDbRestoreVersion = new JTextArea();
		txtrDbRestoreVersion.setEditable(false);
		txtrDbRestoreVersion.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 11));
		txtrDbRestoreVersion.setText("\t"+ProjectConstants.ApplicationName+"\r\n\r\nThis utility is created only for terakora employees\r\nSo that users can archive and restore old DB and\r\nsave installation time. Users are not allowed to share\r\nit outside.\r\n\r\nThis utility is in developing mode. So please use it \r\ncarefully. \r\n\r\nPlease forward any suggestion/Bug Report/improvements\r\non Vaibhav.Jain@Teradata.com.\r\n\r\nFor Requirements , Warning & Bug fixes in this release \r\nPlease view Read Me.\r\n\r\n");
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(157)
							.addComponent(btnViewReadme))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(36)
							.addComponent(txtrDbRestoreVersion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(27, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(45)
					.addComponent(txtrDbRestoreVersion, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnViewReadme)
					.addContainerGap(101, Short.MAX_VALUE))
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
}
