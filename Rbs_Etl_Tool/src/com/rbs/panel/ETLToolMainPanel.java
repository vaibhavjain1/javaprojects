package com.rbs.panel;

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

/*import com.teradata.config.ProjectConstants;
import com.teradata.restore.panel.ArchivePanel;
import com.teradata.restore.panel.DbDeletePanel;
import com.teradata.restore.panel.LockReleasePanel;
import com.teradata.restore.panel.NewDbPanel;
import com.teradata.restore.panel.RestorePanel;
import com.teradata.restore.panel.HelpPanel;*/
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

public class ETLToolMainPanel {

	private JFrame frame;
	private ParseSqlPanel parseSqlPanel;
	private AnalyticsOnFile anaPanel;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				ETLToolMainPanel window = new ETLToolMainPanel();
				window.frame.setVisible(true);
			}
		});

	}

	public ETLToolMainPanel() {
		initialize();
	}

	private void initialize() {

		frame = new JFrame();
		// frame.setBounds(100, 100, 600, 600);
		frame.setBounds(100, 100, 600, 700);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane);

		parseSqlPanel = new ParseSqlPanel();
		parseSqlPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null,
				null, null));
		tabbedPane.addTab("ParseSql", null, parseSqlPanel, null);

		anaPanel = new AnalyticsOnFile();
		tabbedPane.addTab("Analytics", null, anaPanel, null);

		
		frame.setTitle("RBS ETL Tool");
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
