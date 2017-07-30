package InvoiceUI;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import invoicegenerator.ControllerParams;
import invoicegenerator.ControllerParams.BillHeaderInputParams;

class BreakUpTableModel extends AbstractTableModel {

	private String breakUpTableColumnNames[] = { ProjectConstants.hsnSacHeading, ProjectConstants.taxableValue,
			"C.T. " + ProjectConstants.rate, "C.T. " + ProjectConstants.amount, "S.T. " + ProjectConstants.rate, "S.T. " + ProjectConstants.amount };
	
	private Object[][] totalData = {
			{ "0908", new Double(0), new Double(2.50), new Double(0), new Double(2.50), new Double(0)},
			{ ProjectConstants.total, new Double(0), "", new Double(0), "", new Double(0)} 
	};
	
	@Override
	public int getRowCount() {
		return totalData.length;
	}

	@Override
	public int getColumnCount() {
		return breakUpTableColumnNames.length;
	}

	public String getColumnName(int col) {
	      return breakUpTableColumnNames[col];
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		return totalData[row][col];
	}
	
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	@Override
	public void setValueAt(Object aValue, int row, int col) {
		totalData[row][col] = aValue;
		fireTableCellUpdated(row, col);
	}
}


public class BillPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private JEditorPane sellerAddressTextArea;
	private JTextArea buyerAddressTextArea;
	private JTextField invoiceNumTextField;
	private JTextField datedTextField;
	private JTextField deliverNoteTextField;
	private JTextField notInUseTextField;
	private JTextField despatchDocumentNoTextField;
	private JTextField deliveryNoteDateTextField;
	private JTextField despatchedThroughTextField;
	private JTextField destinationTextField;
	private JTextArea notInuseTextArea;
	private JScrollPane billTableScrollPane;
	private JTable billTable;
	private JTable breakUpTable;
	private JScrollPane totalTableScrollPane;
	private JEditorPane companyDetailsTextArea;
	private JEditorPane panDeclerationTextArea;
	
	class BillTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;

		private String[] columnNames = { ProjectConstants.siNoHeading, ProjectConstants.descriptionOfGoodsHeading,
				ProjectConstants.hsnSacHeading, ProjectConstants.gstRateHeading+" (%)", ProjectConstants.quantityHeading+" (kg)",
				ProjectConstants.rateHeading, ProjectConstants.perHeading, ProjectConstants.amountHeading };

		public Object[][] billData = {
				{ "1", "", "0908", new Double(5.0), new Double(0), new Double(0), "kg", new Double(0.0) },
				{ "2", "", "0908", new Double(5.0), new Double(0), new Double(0), "kg", new Double(0) },
				{ "3", "", "0908", new Double(5.0), new Double(0), new Double(0), "kg", new Double(0) },
				{ "4", "", "0908", new Double(5.0), new Double(0), new Double(0), "kg", new Double(0) },
				{ null, ProjectConstants.baradana, null, null, null, null, null, new Double(0) },
				{ null, ProjectConstants.majdoori, null, null, null, null, null, new Double(0) },
				{ null, ProjectConstants.outputCGST, null, null, null, null, null, new Double(0) },
				{ null, ProjectConstants.outputSGST, null, null, null, null, null, new Double(0) },
				{ null, ProjectConstants.total, null, null, new Double(0), null, null, new Double(0) }, 
		};

	    public int getColumnCount() {
	      return columnNames.length;
	    }
	    
	    public Class getColumnClass(int c) {
	        return getValueAt(0, c).getClass();
	    }
	    
	    public int getRowCount() {
	      return billData.length;
	    }

	    public String getColumnName(int col) {
	      return columnNames[col];
	    }

	    public Object getValueAt(int row, int col) {
	      return billData[row][col];
	    }

		public boolean isCellEditable(int row, int col) {
			if (col < 1){
				return false;
			}
			else if (row > 3){
				if(col>6)
					return true;
				else 
					return false;
			}
			else {
				return true;
			}
		}

		public void setValueAt(Object value, int row, int col) {
			billData[row][col] = value;
			fireTableCellUpdated(row, col);
			// if quantity || quantity is updated
			if (col == 4 || col == 5 || row == 4 || row == 5) {
				//update individual amount
				for(int i = 0; i < 4 ; i++){
					billData[i][7] = Double.parseDouble(billData[i][4].toString()) * Double.parseDouble(billData[i][5].toString());
					fireTableCellUpdated(row, 7);
				}
				
				//update total quantity
				billData[8][4] = 0.0;
				for (int i = 0; i < 4; i++) {
					billData[8][4] = Double.parseDouble(billData[8][4].toString()) + Double.parseDouble(billData[i][4].toString());
				}
				fireTableCellUpdated(8, 4);
				
				//update CGST and SGST
				billData[6][7] = 0.0;
				billData[7][7] = 0.0;
				double sum = 0.0;
				for (int i = 0; i < 6; i++) {
					sum += Double.parseDouble(billData[i][7].toString());
				}
				billData[6][7] = (sum * 2.5)/100;
				billData[7][7] = (sum * 2.5)/100;
				fireTableCellUpdated(6, 7);
				fireTableCellUpdated(7, 7);
				
				//update total amount
				double totalSum = 0.0;
				for (int i = 0; i < 8; i++) {
					totalSum += Double.parseDouble(billData[i][7].toString());
				}
				billData[8][7] = totalSum;
				fireTableCellUpdated(8, 7);
				
				TableModel breakUpTableModel  = breakUpTable.getModel();
				breakUpTableModel.setValueAt(sum, 0, 1);
				breakUpTableModel.setValueAt(sum, 1, 1);
				breakUpTableModel.setValueAt(billData[6][7], 0, 3);
				breakUpTableModel.setValueAt(billData[6][7], 1, 3);
				breakUpTableModel.setValueAt(billData[7][7], 0, 5);
				breakUpTableModel.setValueAt(billData[7][7], 1, 5);
			}
		}

	}
	
	public static void breakUpTableAlignCellAndRow(JTable table)
    {
		table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(30);
        table.getColumnModel().getColumn(2).setPreferredWidth(20);
        table.getColumnModel().getColumn(3).setPreferredWidth(30);
        table.getColumnModel().getColumn(4).setPreferredWidth(20);
        table.getColumnModel().getColumn(5).setPreferredWidth(30);
        
        table.setRowHeight(25);
    }
	
	public static void billTableAlignCellAndRow(JTable table)
    {
		int alignment = SwingConstants.CENTER;
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(alignment);

        TableModel tableModel = table.getModel();

        for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++)
        {
            table.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRender);
        }
        
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(40);
        table.getColumnModel().getColumn(3).setPreferredWidth(60);
        table.getColumnModel().getColumn(4).setPreferredWidth(60);
        table.getColumnModel().getColumn(5).setPreferredWidth(30);
        table.getColumnModel().getColumn(6).setPreferredWidth(15);
        table.getColumnModel().getColumn(7).setPreferredWidth(60);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        
        table.setRowHeight(35);
        
    }
	
	public BillPanel() {
		
		JLabel lblNewLabel = new JLabel(ProjectConstants.taxInvoiceHeading);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		sellerAddressTextArea = new JEditorPane("text/html", "");
		sellerAddressTextArea.setEditable(false);
		String sellerAddress = "&nbsp;<b>"+ProjectConstants.sellerName+"</b>" + "<br>&nbsp;"+ProjectConstants.sellerAddressLine1 + 
							   "<br>&nbsp;"+ProjectConstants.sellerAddressLine2 + "<br>&nbsp;"+ProjectConstants.sellerAddressLine3 +
							   "<br>&nbsp;"+ProjectConstants.sellerEmail;
		sellerAddressTextArea.setText(sellerAddress);
		sellerAddressTextArea.setBorder(BorderFactory.createLineBorder(null, 1));
		
		
		buyerAddressTextArea = new JTextArea();
		buyerAddressTextArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(null, 1), ProjectConstants.buyerAddressHeading, 0, 0));
		
		invoiceNumTextField = new JTextField();
		invoiceNumTextField.setColumns(10);
		invoiceNumTextField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(null, 1), ProjectConstants.invoiceNoHeading, 0, 0));
		
		datedTextField = new JTextField();
		datedTextField.setColumns(10);
		datedTextField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(null, 1), ProjectConstants.datedHeading, 0, 0));
		
		deliverNoteTextField = new JTextField();
		deliverNoteTextField.setColumns(10);
		deliverNoteTextField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(null, 1), ProjectConstants.deliveryNoteHeading, 0, 0));
		
		notInUseTextField = new JTextField();
		notInUseTextField.setColumns(10);
		notInUseTextField.setEditable(false);
		notInUseTextField.setBorder(BorderFactory.createLineBorder(null, 1));
		
		despatchDocumentNoTextField = new JTextField();
		despatchDocumentNoTextField.setColumns(10);
		despatchDocumentNoTextField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(null, 1), ProjectConstants.despatchDocumentNo, 0, 0));
		
		deliveryNoteDateTextField = new JTextField();
		deliveryNoteDateTextField.setColumns(10);
		deliveryNoteDateTextField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(null, 1), ProjectConstants.deliveryNoteDateHeading, 0, 0));
		
		despatchedThroughTextField = new JTextField();
		despatchedThroughTextField.setColumns(10);
		despatchedThroughTextField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(null, 1), ProjectConstants.despatchedThroughHeading, 0, 0));
		
		destinationTextField = new JTextField();
		destinationTextField.setColumns(10);
		destinationTextField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(null, 1), ProjectConstants.destinationHeading, 0, 0));
		
		notInuseTextArea = new JTextArea();
		notInuseTextArea.setEditable(false);
		notInuseTextArea.setBorder(BorderFactory.createLineBorder(null, 1));

		billTable = new JTable(new BillTableModel());
		billTableAlignCellAndRow(billTable);
		billTableScrollPane = new JScrollPane(billTable);
		
		breakUpTable = new JTable(new BreakUpTableModel());
		breakUpTableAlignCellAndRow(breakUpTable);
		totalTableScrollPane = new JScrollPane(breakUpTable);
		
		JButton btnPdf = new JButton("PDF");
		btnPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Rukk ja bhai");
				ControllerParams paramObj = new ControllerParams();
				BillHeaderInputParams obj = paramObj.new BillHeaderInputParams();
				obj.setInvoiceNumber(invoiceNumTextField.getText());
				obj.deliveryNote = deliverNoteTextField.getText();
				obj.despatchDocumentNo = despatchDocumentNoTextField.getText();
				obj.deliveryNoteDate = deliveryNoteDateTextField.getText();
				obj.despatchedThrough = despatchedThroughTextField.getText();
				obj.destination = destinationTextField.getText();
			}
		});
		
		companyDetailsTextArea = new JEditorPane("text/html", "");
		companyDetailsTextArea.setEditable(false);
		String compDetails = "&nbsp;"+ProjectConstants.companyBankDetails + "<br>&nbsp;"+ProjectConstants.bankName + 
				   "<br>&nbsp;"+ProjectConstants.bankAcNo + "<br>&nbsp;"+ProjectConstants.branchIfscCode;
		companyDetailsTextArea.setText(compDetails);
		companyDetailsTextArea.setBorder(BorderFactory.createLineBorder(null, 1));
		
		panDeclerationTextArea = new JEditorPane("text/html", "");
		String pandecleration = "&nbsp;"+ProjectConstants.companyPan + "<br>&nbsp;<U>"+ProjectConstants.decleration + 
				   "<br></U>&nbsp;"+ProjectConstants.declerationLine1 + "<br>&nbsp;"+ProjectConstants.declerationLine2;
		panDeclerationTextArea.setText(pandecleration);
		panDeclerationTextArea.setEditable(false);
		panDeclerationTextArea.setBorder(BorderFactory.createLineBorder(null, 1));
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(377)
					.addComponent(btnPdf)
					.addContainerGap(401, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(351)
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(35)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(billTableScrollPane, GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(buyerAddressTextArea)
										.addComponent(sellerAddressTextArea, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addGroup(groupLayout.createSequentialGroup()
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(despatchDocumentNoTextField, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
												.addComponent(deliverNoteTextField, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
												.addGroup(groupLayout.createSequentialGroup()
													.addComponent(despatchedThroughTextField, GroupLayout.PREFERRED_SIZE, 217, GroupLayout.PREFERRED_SIZE)
													.addPreferredGap(ComponentPlacement.RELATED))
												.addComponent(invoiceNumTextField, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
													.addComponent(destinationTextField, GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
													.addGroup(groupLayout.createSequentialGroup()
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(datedTextField, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE))
													.addComponent(deliveryNoteDateTextField, GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE))
												.addGroup(groupLayout.createSequentialGroup()
													.addGap(2)
													.addComponent(notInUseTextField, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE))))
										.addComponent(notInuseTextArea, GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)))
								.addComponent(totalTableScrollPane, GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(panDeclerationTextArea, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(companyDetailsTextArea, GroupLayout.PREFERRED_SIZE, 387, GroupLayout.PREFERRED_SIZE)))))
					.addGap(41))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addGap(24)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(sellerAddressTextArea, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(buyerAddressTextArea, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(datedTextField, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
								.addComponent(invoiceNumTextField, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(deliverNoteTextField, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
								.addComponent(notInUseTextField, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
							.addGap(9)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(despatchDocumentNoTextField, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
								.addComponent(deliveryNoteDateTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(despatchedThroughTextField, GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
								.addComponent(destinationTextField, GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(notInuseTextArea, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(billTableScrollPane, GroupLayout.PREFERRED_SIZE, 341, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(totalTableScrollPane, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(companyDetailsTextArea, GroupLayout.PREFERRED_SIZE, 63, Short.MAX_VALUE)
						.addComponent(panDeclerationTextArea, GroupLayout.PREFERRED_SIZE, 63, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnPdf)
					.addContainerGap())
		);
		setLayout(groupLayout);
		
	}
	
	Image image = (new ImageIcon(ProjectConstants.backgroundImage)).getImage();

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null)
			g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);

	}
}
