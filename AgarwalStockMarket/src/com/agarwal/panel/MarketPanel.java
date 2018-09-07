package com.agarwal.panel;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

public class MarketPanel {

	private JFrame frame;
	private StockEntryPanel stockEntryPanel;
	private ReportPanel reportPanel;
	private AddStockPanel addStockPanel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MarketPanel window = new MarketPanel();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MarketPanel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 562, 479);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane);
		
		stockEntryPanel = new StockEntryPanel();
		tabbedPane.addTab("Stock Entry", null, stockEntryPanel, null);
		
		reportPanel = new ReportPanel();
		tabbedPane.addTab("Print Report", null, reportPanel, null);
		
		addStockPanel = new AddStockPanel();
		tabbedPane.addTab("Add Stock", null, addStockPanel, null);
		
	}

}
