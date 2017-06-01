package com.rbs.panel;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.TextArea;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.awt.event.ActionEvent;

public class AnalyticsOnFile extends JPanel{
	private JTextField txtbackupdir;
	private static JTextArea textArea;
	private static JTextArea textArea_1;
	private static JTextArea textArea_2;
	private static Map<String,Integer> citywiseDonorMap;
	public AnalyticsOnFile() {
		
		JLabel lblAnalysticsOnCsv = new JLabel("Analystics on CSV File");
		lblAnalysticsOnCsv.setFont(new Font("Tahoma", Font.PLAIN, 25));
		
		JLabel lblCsvFileLocation = new JLabel("CSV File location");
		
		txtbackupdir = new JTextField();
		txtbackupdir.setColumns(10);
		
		JButton button = new JButton("Browse");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedExportDir = SelectDirectory(
						"Select csv file", txtbackupdir);
				if (selectedExportDir != null)
					txtbackupdir.setText(selectedExportDir);
			}
		});
		
		JLabel lblAverageDonationAmount = new JLabel("average donation amount");
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		JLabel lblByRecurringDonors = new JLabel(" by recurring donors");
		
		JLabel lblNumberOfDonors = new JLabel("number of donors donating");
		
		JLabel lblNewLabel = new JLabel("in more than one years");
		
		textArea_1 = new JTextArea();
		textArea_1.setEditable(false);
		
		JLabel lblTotalAmountOf = new JLabel("total amount of donation by NJ state");
		
		textArea_2 = new JTextArea();
		textArea_2.setEditable(false);
		
		JButton btnCalculate = new JButton("Calculate");
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					BufferedReader br = new BufferedReader(new FileReader(txtbackupdir.getText()));
					String splitBy = ",";
					String str = null;
					Double totalDonationByNJState = 0d;
					Map<String,String> donorvsyearMap = new HashMap<>();
					Set<String> donorDontatingInMorethenoneyear = new HashSet<>();
					int noOfRecurringdonors = 0;
					Double donationAmountByRecurringDonors = 0d;
					citywiseDonorMap = new HashMap<>();
					while ((str = br.readLine()) != null) {
						String[] currRecord = str.split(splitBy);
						String id = currRecord[0].trim();
						String donor_id = currRecord[1].trim();
						String last_name = currRecord[2].trim();
						String first_name = currRecord[3].trim();	
						String year = currRecord[4].trim();
						String city = currRecord[5].trim();
						String state = currRecord[6].trim().trim();
						String postal_code = currRecord[7].trim();
						String gift_amount = currRecord[8].trim();
						
						// Finding total donation by NJ state
						if(state.equalsIgnoreCase("NJ")){
							try {
								totalDonationByNJState += Double.parseDouble(gift_amount);
							} catch (Exception e1) {
								// Ignore it
							}
						}
						// Finding number of donors donating in more then one year
						// Also finding average donationation by recurring donors
						
						if(donorvsyearMap.containsKey(donor_id)){
							noOfRecurringdonors++;
							donationAmountByRecurringDonors+=Double.parseDouble(gift_amount);
							String yearofDontation = donorvsyearMap.get(donor_id);
							if(yearofDontation.equalsIgnoreCase(year))
								donorDontatingInMorethenoneyear.add(donor_id);
						} else{
							donorvsyearMap.put(donor_id, year);
						}
						
						// Calculating city wise donation
						if(citywiseDonorMap.containsKey(city)){
							citywiseDonorMap.put(city, citywiseDonorMap.get(city)+1);
						}else{
							citywiseDonorMap.put(city, 1);
						}
						
					}
					textArea.setText("Total Donation By NJ state = "+totalDonationByNJState);
					textArea.setLineWrap(true);
					textArea_1.setText("Total number of donors donating in more then one year = "+ donorDontatingInMorethenoneyear.size()+ " Donor id's are:"+donorDontatingInMorethenoneyear);
					textArea_1.setLineWrap(true);
					textArea_2.setText("Average donation amount by recurring donors: "+donationAmountByRecurringDonors+ " where no of Recurring donors are :"+ noOfRecurringdonors +" and total donation amount of recurring donors is : "+ donationAmountByRecurringDonors);
					textArea_2.setLineWrap(true);
					System.out.println(citywiseDonorMap);
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
				
			}
		});
		btnCalculate.setFont(new Font("Tahoma", Font.PLAIN, 21));
		
		JButton btnNewButton = new JButton("Draw Chart");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				Set<Map.Entry<String, Integer>> dataSetEntry = citywiseDonorMap.entrySet();
				int value = 0;
				for (Entry<String, Integer> entry : dataSetEntry) {
					if(value==6)
						break;
					dataset.setValue(entry.getValue(), "Value1", entry.getKey());
					value++;
				}
	  			JFreeChart chart = ChartFactory.createBarChart("City Wise Donor - For demo showing only 5 cities", "City name", "No of donors", dataset, PlotOrientation.VERTICAL, false, true, false);
	  			CategoryPlot p = chart.getCategoryPlot();
	  			p.setRangeGridlinePaint(Color.black);
	  			ChartFrame frame = new ChartFrame("Bar chart for student", chart);
	  			frame.setVisible(true);
	  			frame.setSize(800,700);
			}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(175)
							.addComponent(lblAnalysticsOnCsv, GroupLayout.PREFERRED_SIZE, 264, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(88)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(112)
									.addComponent(button, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblCsvFileLocation, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
									.addGap(5)
									.addComponent(txtbackupdir, GroupLayout.PREFERRED_SIZE, 264, GroupLayout.PREFERRED_SIZE))))
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
							.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
								.addGap(135)
								.addComponent(btnCalculate, GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE))
							.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
								.addGap(23)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addComponent(lblByRecurringDonors)
									.addComponent(lblAverageDonationAmount, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)
									.addComponent(lblNumberOfDonors)
									.addComponent(lblNewLabel)
									.addComponent(lblTotalAmountOf))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
									.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
									.addComponent(textArea_1)
									.addComponent(textArea_2)))))
					.addContainerGap(72, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblAnalysticsOnCsv, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addGap(26)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(4)
							.addComponent(lblCsvFileLocation, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
						.addComponent(txtbackupdir, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(button)
					.addGap(41)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblAverageDonationAmount, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblByRecurringDonors))
						.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE))
					.addGap(40)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblNumberOfDonors)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblNewLabel))
						.addComponent(textArea_1, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(63)
							.addComponent(lblTotalAmountOf))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(37)
							.addComponent(textArea_2, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnCalculate, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
					.addGap(42))
		);
		setLayout(groupLayout);
	}

	public static String SelectDirectory(String title,Component parent)
	{
		final JFileChooser fileSelector = new JFileChooser();
		fileSelector.setDialogTitle(title);
		fileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fileSelector.showOpenDialog(parent);
		String folderPath = null;
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File folder = fileSelector.getSelectedFile();
			folderPath = folder.getAbsolutePath();
		}
		return folderPath;
	}
}
