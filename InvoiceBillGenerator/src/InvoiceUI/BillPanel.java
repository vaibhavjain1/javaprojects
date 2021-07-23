package InvoiceUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
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
import javax.swing.text.JTextComponent;

import invoicegenerator.ControllerParams;
import invoicegenerator.ControllerParams.AllGoods;
import invoicegenerator.ControllerParams.BillHeaderInputParams;
import invoicegenerator.ControllerParams.Good;
import invoicegenerator.InvoicePrinter;

class BreakUpTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private String breakUpTableColumnNames[] = { ProjectConstants.hsnSacHeading, ProjectConstants.taxableHeading+ProjectConstants.valueHeading,
			"C.T. " + ProjectConstants.rate, "C.T. " + ProjectConstants.amount, "S.T. " + ProjectConstants.rate, "S.T. " + ProjectConstants.amount
			, "I.T. " + ProjectConstants.rate, "I.T. " + ProjectConstants.amount};
	
	private Object[][] totalData = {
			{ "0908", new Double(0), new Double(2.50), new Double(0), new Double(2.50), new Double(0), new Double(5.0), new Double(0)},
			{ ProjectConstants.total, new Double(0), "", new Double(0), "", new Double(0), "", new Double(0)} 
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
	private JTextField despatchDocumentNoTextField;
	private JTextField deliveryNoteDateTextField;
	private JTextField despatchedThroughTextField;
	private JTextField destinationTextField;
	private JScrollPane billTableScrollPane;
	private JTable billTable;
	private JTable breakUpTable;
	private JScrollPane totalTableScrollPane;
	private JEditorPane companyDetailsTextArea;
	private JEditorPane panDeclerationTextArea;
	private JCheckBox icsGstCheckBox;
	public static boolean iGSTEnabled = false;
	
	class BillTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;

		private String[] columnNames = { ProjectConstants.siHeading+ProjectConstants.NumHeading, ProjectConstants.descriptionOfGoodsHeading,
				ProjectConstants.hsnSacHeading, ProjectConstants.gstRateHeading+" (%)", ProjectConstants.quantityHeading+" (kg)",
				ProjectConstants.rateHeading, ProjectConstants.perHeading, ProjectConstants.amountHeading };

		public Object[][] billData = {
				{ "1", "", "0908", new Double(5.0), new Double(0), new Double(0), "kg", new Double(0.0) },
				{ "2", "", "0908", new Double(5.0), new Double(0), new Double(0), "kg", new Double(0) },
				{ "3", "", "0908", new Double(5.0), new Double(0), new Double(0), "kg", new Double(0) },
				{ "4", "", "0908", new Double(5.0), new Double(0), new Double(0), "kg", new Double(0) },
				{ null, ProjectConstants.bardana, null, null, null, null, null, new Double(0) },
				{ null, ProjectConstants.majdoori, null, null, null, null, null, new Double(0) },
				{ null, ProjectConstants.outputCGST, null, null, null, null, null, new Double(0) },
				{ null, ProjectConstants.outputSGST, null, null, null, null, null, new Double(0) },
				{ null, ProjectConstants.outputIGST, null, null, null, null, null, new Double(0) },
				{ null, ProjectConstants.roundOff, null, null, null, null, null, new Double(0) },
				{ null, ProjectConstants.total, null, null, new Double(0), null, null, new Double(0) }, 
		};

	    public int getColumnCount() {
	      return columnNames.length;
	    }
	    
	    @SuppressWarnings({ "unchecked", "rawtypes" })
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
			if (col < 1 || col==6){
				return false;
			}
			if(row > 3 && col < 7){
				return false;
			}
			if(row > 5 && row != 9 && col == 7){
				return false;
			}
			else {
				return true;
			}
		}
		
		public void setValueAt(Object value, int row, int col) {
			if(value instanceof Double){
				value = ProjectConstants.dFormat.format(value);
			}
			billData[row][col] = value;
			fireTableCellUpdated(row, col);
			// if quantity || quantity is updated
			if (col == 4 || col == 5 || row == 4 || row == 5 || row == 6 || row == 7 || row == 8 || row ==9) {
				//update individual amount
				for(int i = 0; i < 4 ; i++){
					billData[i][7] = Double.parseDouble(billData[i][4].toString()) * Double.parseDouble(billData[i][5].toString());
					fireTableCellUpdated(row, 7);
				}
				
				//update total quantity
				billData[10][4] = 0.0;
				for (int i = 0; i < 4; i++) {
					billData[10][4] = Double.parseDouble(billData[10][4].toString()) + Double.parseDouble(billData[i][4].toString());
				}
				fireTableCellUpdated(9, 4);
				
				//update IGST, CGST and SGST
				double sum = 0.0;
				for (int i = 0; i < 6; i++) {
					sum += Double.parseDouble(billData[i][7].toString());
				}
				sum = Double.parseDouble(ProjectConstants.dFormat.format(sum));
				if(iGSTEnabled){
					billData[8][7] = 0.0;
					billData[8][7] = Double.parseDouble(ProjectConstants.dFormat.format((sum * 5.0)/100));
					fireTableCellUpdated(8, 7);
				}else{
					billData[6][7] = 0.0;
					billData[7][7] = 0.0;
					
					billData[6][7] = Double.parseDouble(ProjectConstants.dFormat.format((sum * 2.5)/100));
					billData[7][7] = Double.parseDouble(ProjectConstants.dFormat.format((sum * 2.5)/100));
					fireTableCellUpdated(6, 7);
					fireTableCellUpdated(7, 7);
				}
				
				//update total amount
				double totalSum = 0.0;
				for (int i = 0; i < 10; i++) {
					totalSum += Double.parseDouble(billData[i][7].toString());
				}
				billData[10][7] = ProjectConstants.dFormat.format(totalSum);;
				fireTableCellUpdated(10, 7);
				
				TableModel breakUpTableModel  = breakUpTable.getModel();
				breakUpTableModel.setValueAt(sum, 0, 1);
				breakUpTableModel.setValueAt(sum, 1, 1);
				breakUpTableModel.setValueAt(billData[6][7], 0, 3);
				breakUpTableModel.setValueAt(billData[6][7], 1, 3);
				breakUpTableModel.setValueAt(billData[7][7], 0, 5);
				breakUpTableModel.setValueAt(billData[7][7], 1, 5);
				breakUpTableModel.setValueAt(billData[8][7], 0, 7);
				breakUpTableModel.setValueAt(billData[8][7], 1, 7);
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
        table.getColumnModel().getColumn(6).setPreferredWidth(20);
        table.getColumnModel().getColumn(7).setPreferredWidth(30);
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
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        
        table.setRowHeight(24);
        table.setSelectionBackground(Color.green);
        table.setCellEditor(null);
    }
	
	public class SelectAllCellEditor extends DefaultCellEditor
	{
		private static final long serialVersionUID = -5892354312576646394L;

		public SelectAllCellEditor(final JTextField textField ) {
	        super( textField );
	        textField.addFocusListener( new FocusAdapter()
	        {
	            public void focusGained( final FocusEvent e )
	            {
	                textField.selectAll();
	            }
	        } );
	    }
	}
	
	private void clearAll() {
	}
	
	public BillPanel() {
		
		JLabel lblNewLabel = new JLabel(ProjectConstants.taxInvoiceHeading);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		sellerAddressTextArea = new JEditorPane("text/html", "");
		sellerAddressTextArea.setEditable(false);
		String sellerAddress = "&nbsp;<b>"+ProjectConstants.sellerName+"</b>" + "<br>&nbsp;"+ProjectConstants.sellerAddressLine1 + 
							   "&nbsp;"+ProjectConstants.sellerAddressLine2 + "<br>&nbsp;"+ProjectConstants.sellerAddressLine3 +
							   "<br>&nbsp;"+ProjectConstants.sellerEmail;
		sellerAddressTextArea.setText(sellerAddress);
		sellerAddressTextArea.setBorder(BorderFactory.createLineBorder(null, 1));
		
		buyerAddressTextArea = new JTextArea();
		buyerAddressTextArea.setLineWrap(true);
		buyerAddressTextArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(null, 1), ProjectConstants.buyerAddressHeading, 0, 0));
		buyerAddressTextArea.setFont(new Font("Verdana", Font.PLAIN, 13));
		
		invoiceNumTextField = new JTextField();
		invoiceNumTextField.setColumns(10);
		invoiceNumTextField.setText(ProjectConstants.nkcSupply);
		invoiceNumTextField.setHorizontalAlignment(JTextField.CENTER);
		invoiceNumTextField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(null, 1), ProjectConstants.invoiceNoHeading, 0, 0));
		invoiceNumTextField.setFont(new Font("Verdana", Font.PLAIN, 16));
		invoiceNumTextField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (invoiceNumTextField.getText().length() >= 16 )
		            e.consume(); 
		    }  
		});
		
		datedTextField = new JTextField();
		datedTextField.setColumns(10);
		datedTextField.setFont(new Font("Verdana", Font.PLAIN, 16));
		DateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
		datedTextField.setHorizontalAlignment(JTextField.CENTER);
		datedTextField.setText(dateFormatter.format(new Date()));
		datedTextField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(null, 1), ProjectConstants.datedHeading, 0, 0));
		
		deliverNoteTextField = new JTextField();
		deliverNoteTextField.setColumns(10);
		deliverNoteTextField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(null, 1), ProjectConstants.deliveryNoteHeading, 0, 0));
		
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

		billTable = new JTable(new BillTableModel()){
			private static final long serialVersionUID = 1L;

			@Override // Always selectAll()
		    public boolean editCellAt(int row, int column, EventObject e) {
		        boolean result = super.editCellAt(row, column, e);
		        final Component editor = getEditorComponent();
		        if (editor == null || !(editor instanceof JTextComponent)) {
		            return result;
		        }
		        if (e instanceof MouseEvent) {
		            EventQueue.invokeLater(() -> {
		                ((JTextComponent) editor).selectAll();
		            });
		        } else {
		            ((JTextComponent) editor).selectAll();
		        }
		        return result;
		    }
		};
		billTableAlignCellAndRow(billTable);
		billTableScrollPane = new JScrollPane(billTable);
		
		breakUpTable = new JTable(new BreakUpTableModel());
		breakUpTableAlignCellAndRow(breakUpTable);
		totalTableScrollPane = new JScrollPane(breakUpTable);
		
		companyDetailsTextArea = new JEditorPane("text/html", "");
		companyDetailsTextArea.setEditable(false);
		String compDetails = "&nbsp;"+ProjectConstants.companyBankDetails + "<br>&nbsp;"+ProjectConstants.bankName + 
				   "<br>&nbsp;"+ProjectConstants.bankAcNo + "<br>&nbsp;"+ProjectConstants.branchIfscCode;
		companyDetailsTextArea.setText(compDetails);
		companyDetailsTextArea.setBorder(BorderFactory.createLineBorder(null, 1));
		
		panDeclerationTextArea = new JEditorPane("text/html", "");
		String pandecleration = "&nbsp;"+ProjectConstants.companyPan+ProjectConstants.companyPanNumber + "<br>&nbsp;<U>"+ProjectConstants.decleration + 
				   "<br></U>&nbsp;"+ProjectConstants.declerationLine1 + "<br>&nbsp;"+ProjectConstants.declerationLine2+ "&nbsp;"+ProjectConstants.declerationLine3;
		panDeclerationTextArea.setText(pandecleration);
		panDeclerationTextArea.setEditable(false);
		panDeclerationTextArea.setBorder(BorderFactory.createLineBorder(null, 1));
		
		generatePdfButton = new JButton("PDF");
		generatePdfButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Set the header of bill
				ControllerParams paramObj = new ControllerParams();
				BillHeaderInputParams billHeaderObj = paramObj.new BillHeaderInputParams();
				billHeaderObj.buyerDetails = buyerAddressTextArea.getText();
				billHeaderObj.date = datedTextField.getText();
				billHeaderObj.invoiceNumber = invoiceNumTextField.getText();
				billHeaderObj.deliveryNote = deliverNoteTextField.getText();
				billHeaderObj.despatchDocumentNo = despatchDocumentNoTextField.getText();
				billHeaderObj.deliveryNoteDate = deliveryNoteDateTextField.getText();
				billHeaderObj.despatchedThrough = despatchedThroughTextField.getText();
				billHeaderObj.destination = destinationTextField.getText();
				paramObj.BillHeaderInputParamsObj = billHeaderObj;

				TableModel billTableModel = billTable.getModel();
				double totalQuantity = Double.parseDouble(billTableModel.getValueAt(10, 4).toString());
				double bardana = Double.parseDouble(billTableModel.getValueAt(4, 7).toString());
				double majdoori = Double.parseDouble(billTableModel.getValueAt(5, 7).toString());
				double cgst = Double.parseDouble(billTableModel.getValueAt(6, 7).toString());
				double sgst = Double.parseDouble(billTableModel.getValueAt(7, 7).toString());
				double igst = Double.parseDouble(billTableModel.getValueAt(8, 7).toString());
				double roundOff = Double.parseDouble(billTableModel.getValueAt(9, 7).toString());
				double totalAmountWithGST = Double.parseDouble(billTableModel.getValueAt(10, 7).toString());
				AllGoods allGoods = paramObj.new AllGoods(totalQuantity, bardana, majdoori, igst, cgst, sgst, roundOff, 0,	totalAmountWithGST);
				for (int i = 0; i < ProjectConstants.maxItems; i++) {
					String itemDesc = billTableModel.getValueAt(i, 1).toString();
					String hsnSac = billTableModel.getValueAt(i, 2).toString();
					double gstRate = Double.parseDouble(billTableModel.getValueAt(i, 3).toString());
					double quanity = Double.parseDouble(billTableModel.getValueAt(i, 4).toString());
					double rate = Double.parseDouble(billTableModel.getValueAt(i, 5).toString());
					double amount = Double.parseDouble(billTableModel.getValueAt(i, 7).toString());
					if (itemDesc != null && itemDesc.trim() != "") {
						Good good = paramObj.new Good(i + 1, itemDesc, hsnSac, gstRate, quanity, rate, amount);
						allGoods.addGoodInList(good);
					}
				}
				paramObj.AllGoodsObj = allGoods;

				new InvoicePrinter().GeneratePdf(paramObj);

			}
		});
		
		resetButtin = new JButton("Reset");
		resetButtin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAll();
			}
		});
		
		icsGstCheckBox = new JCheckBox("IGST");
		icsGstCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		icsGstCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(icsGstCheckBox.isSelected()){
					iGSTEnabled = true;
					TableModel billTableModel = billTable.getModel();
					billTableModel.setValueAt(0.0, 6, 7);
					billTableModel.setValueAt(0.0, 7, 7);
				}
				else{
					iGSTEnabled = false;
					TableModel billTableModel = billTable.getModel();
					billTableModel.setValueAt(0.0, 8, 7);
				}
			}
		});
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(buyerAddressTextArea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(sellerAddressTextArea, GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(despatchedThroughTextField, GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
						.addComponent(invoiceNumTextField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
						.addComponent(deliverNoteTextField, 263, 263, Short.MAX_VALUE)
						.addComponent(despatchDocumentNoTextField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(deliveryNoteDateTextField, Alignment.LEADING)
								.addComponent(datedTextField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(13)
							.addComponent(destinationTextField, GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(icsGstCheckBox)))
					.addGap(41))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(347)
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(396, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(billTableScrollPane, GroupLayout.DEFAULT_SIZE, 834, Short.MAX_VALUE)
					.addGap(20))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(totalTableScrollPane, GroupLayout.DEFAULT_SIZE, 834, Short.MAX_VALUE)
					.addGap(20))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panDeclerationTextArea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(companyDetailsTextArea, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(generatePdfButton)
						.addComponent(resetButtin))
					.addContainerGap(52, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(invoiceNumTextField, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
								.addComponent(datedTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(deliverNoteTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(icsGstCheckBox))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(despatchDocumentNoTextField, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
								.addComponent(deliveryNoteDateTextField, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(despatchedThroughTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(destinationTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(sellerAddressTextArea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(buyerAddressTextArea, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(billTableScrollPane, GroupLayout.PREFERRED_SIZE, 290, GroupLayout.PREFERRED_SIZE)
					.addGap(1)
					.addComponent(totalTableScrollPane, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(generatePdfButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(resetButtin))
						.addComponent(companyDetailsTextArea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(panDeclerationTextArea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(197, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
		
	}
	
	Image image = (new ImageIcon(ProjectConstants.backgroundImage)).getImage();
	private JButton generatePdfButton;
	private JButton resetButtin;

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null)
			g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);

	}
}
