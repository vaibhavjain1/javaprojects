package com.teradata.restore.app;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.teradata.config.ProjectConstants;
import com.teradata.restore.panel.ArchivePanel;
import com.teradata.restore.panel.DbDeletePanel;
import com.teradata.restore.panel.LockReleasePanel;
import com.teradata.restore.panel.NewDbPanel;
import com.teradata.restore.panel.RestorePanel;
import com.teradata.restore.panel.HelpPanel;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

public class DbRestoreUtility {

	private JFrame frame;
	private ArchivePanel arcPanel;
	private RestorePanel resPanel;
	private LockReleasePanel lockPanel;
	private DbDeletePanel dbDelPanel;
	private NewDbPanel	newDbPanel;
	private HelpPanel helpPanel;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				DbRestoreUtility window = new DbRestoreUtility();
				window.frame.setVisible(true);
			}
		});

	}

	public DbRestoreUtility() {
		initialize();
	}

	private void initialize() {

		frame = new JFrame();
		// frame.setBounds(100, 100, 600, 600);
		frame.setBounds(100, 100, 500, 530);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane);

		arcPanel = new ArchivePanel();
		arcPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null,
				null, null));
		tabbedPane.addTab("Archieve", null, arcPanel, null);

		resPanel = new RestorePanel();
		tabbedPane.addTab("Restore", null, resPanel, null);

		lockPanel = new LockReleasePanel();
		tabbedPane.addTab("Lock Release", null, lockPanel, null);

		dbDelPanel = new DbDeletePanel();
		tabbedPane.addTab("DB Delete", null, dbDelPanel, null);

		newDbPanel = new NewDbPanel();
		tabbedPane.addTab("Create DB", null, newDbPanel, null);
		
		helpPanel = new HelpPanel();
		tabbedPane.addTab("Help", null, helpPanel, null);

		// set image icon
		BufferedImage images = null;
		try {
			File imageFile = new File(ProjectConstants.frameIcon);
			images = ImageIO.read(imageFile);
			frame.setIconImage(images);
		} catch (IOException e) {
			System.out.println("Could not find app icon : "
					+ ProjectConstants.frameIcon);
			// e.printStackTrace();
		}
		frame.setTitle(ProjectConstants.ApplicationName);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {

				} catch (Exception ex) {

				} finally {
					System.exit(0);
				}

			}
		});
	}
}
