package com.agarwal.panel;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import com.agarwal.config.ConnectionClass;
import com.agarwal.config.ProjectConstants;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.Font;

import org.jdatepicker.DateModel;

import java.util.Properties;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JFormattedTextField.AbstractFormatter;

import org.jdatepicker.util.JDatePickerUtil;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class StockEntryPanel extends JPanel {
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_4;

	/**
	 * Create the application.
	 */
	public StockEntryPanel() {

		JLabel lblNewLabel = new JLabel("Stock Entry");
		lblNewLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 14));

		JLabel lblScripName = new JLabel("Stock Buy/Sell");

		JLabel lblQuantity = new JLabel("Quantity");

		textField_1 = new JTextField();
		textField_1.setColumns(10);

		JLabel lblPurchaseValue = new JLabel("Price/Unit");

		textField_2 = new JTextField();
		textField_2.setColumns(10);

		JLabel lblNetTotal = new JLabel("Net Total");

		textField_4 = new JTextField();
		textField_4.setColumns(10);

		JLabel lblDate = new JLabel("Date");

		textField_5 = new JTextField();
		textField_5.setColumns(10);
		
		JLabel lblContractNoteNo = new JLabel("Stock Name");
		
		UtilDateModel model = new UtilDateModel();

		//frame.add(datePicker);

		JButton btnSave = new JButton("Save");
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textField_1 != null
						&& textField_2 != null
						&& textField_4 != null) {
					Connection con = null;
					PreparedStatement pstmt = null;
					try {
						con = ConnectionClass.getDataSource().getConnection();
						pstmt = con
								.prepareStatement("INSERT INTO STOCK_ENTRY(SCRIP_NAME,QUANTITY,PRICE_UNIT,BROKERAGE_UNIT,NET_TOTAL) VALUES (?,?,?,?,?);");
						//pstmt.setString(1, textField.getText());
						pstmt.setInt(2,
								Integer.parseInt(textField_1.getText().trim()));
						pstmt.setInt(3,
								Integer.parseInt(textField_2.getText().trim()));
						//pstmt.setInt(4,
							//	Integer.parseInt(textField_3.getText().trim()));
						pstmt.setInt(5,
								Integer.parseInt(textField_4.getText().trim()));
						pstmt.execute();
					} catch (SQLException e1) {
						e1.printStackTrace();
					} finally {
						try {
							pstmt.close();
							con.close();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

					}
				}
			}
		});
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"BUY", "SELL"}));
		comboBox.setMaximumRowCount(2);
		
		JComboBox<String> stockNameComboBox = getStockList();
		
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(191)
							.addComponent(lblNewLabel))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(112)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblScripName)
								.addComponent(lblQuantity)
								.addComponent(lblDate)
								.addComponent(lblContractNoteNo)
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
									.addComponent(lblNetTotal)
									.addComponent(lblPurchaseValue)))
							.addGap(50)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(stockNameComboBox, 0, 86, Short.MAX_VALUE)
								.addComponent(textField_4)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
									.addComponent(textField_5)
									.addComponent(textField_2)
									.addComponent(textField_1)
									.addComponent(comboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(191)
							.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)))
					.addGap(185))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(33)
					.addComponent(lblNewLabel)
					.addGap(15)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDate)
						.addComponent(textField_5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblContractNoteNo)
						.addComponent(stockNameComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(19)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblScripName)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblQuantity)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPurchaseValue)
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNetTotal)
						.addComponent(textField_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(72)
					.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(60, Short.MAX_VALUE))
		);
		setLayout(groupLayout);

	}

	Image image = (new ImageIcon(ProjectConstants.backgroundImage)).getImage();
	private JTextField textField_5;


	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null)
			g.drawImage(image, (this.getWidth() / 2)
					- (image.getWidth(this) / 2), (this.getHeight() / 2)
					- (image.getHeight(this) / 2), image.getWidth(this),
					image.getHeight(this), this);

	}
	
	public static JComboBox<String> getStockList(){
		JComboBox tempCombobox = new JComboBox();
		ResultSet rs = null;
		try(Connection con = ConnectionClass.getDataSource().getConnection();
			Statement stmt = con.createStatement();) {
			rs = stmt.executeQuery("SELECT STOCK_ID,STOCK_NAME FROM STOCKS;");
			while (rs.next()) {
				tempCombobox.addItem(new ComboItem(rs.getString(2), rs.getInt(1)));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return tempCombobox;
	}
	
	static class ComboItem
	{
	    private String key;
	    private int value;

	    public ComboItem(String key, int value)
	    {
	        this.key = key;
	        this.value = value;
	    }

	    @Override
	    public String toString()
	    {
	        return key;
	    }

	    public String getKey()
	    {
	        return key;
	    }

	    public int getValue()
	    {
	        return value;
	    }
	}
}
