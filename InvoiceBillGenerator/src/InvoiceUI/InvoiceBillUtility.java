package InvoiceUI;

import static utilities.InvoiceUtil.logger;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public class InvoiceBillUtility {

	private JFrame frame;
	private BillPanel billPanel;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				InvoiceBillUtility window = new InvoiceBillUtility();
				window.frame.setVisible(true);
				logger.info("Initializing window on application startup");
			}
		});

	}

	public InvoiceBillUtility() {
		initialize();
		logger.info("Setting layout of main window completed");
	}
	
	private void initialize() {
		frame = new JFrame();

		frame = new JFrame();
		frame.setBounds(100, 100, 850, 730);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y-20);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane);

		billPanel = new BillPanel();
		billPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tabbedPane.addTab(ProjectConstants.taxInvoiceHeading, null, billPanel, null);
		
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
		frame.setTitle(ProjectConstants.productName+" "+ProjectConstants.productVersion);
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
	
	private void setLayout(){
		// set image icon
				BufferedImage images = null;
				try {
					File imageFile = new File(ProjectConstants.frameIcon);
					images = ImageIO.read(imageFile);
					frame.setIconImage(images);
				} catch (IOException e) {
					logger.error("Could not find app icon : "
							+ ProjectConstants.frameIcon);
				}
				frame.setTitle(ProjectConstants.ApplicationName);
				
	}
}
