package InvoiceUI;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

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
	
	private String billTableColumnNames[] = { ProjectConstants.siNoHeading, ProjectConstants.descriptionOfGoodsHeading,
			ProjectConstants.hsnSacHeading, ProjectConstants.gstRateHeading+" (%)", ProjectConstants.quantityHeading+" (kg)",
			ProjectConstants.rateHeading, ProjectConstants.perHeading, ProjectConstants.amountHeading };
	
	private final DefaultTableModel totalTableDataModel = new DefaultTableModel(){
		public int getColumnCount() { 
            return 3; 
        }

        public int getRowCount() { 
            return 5;
        }
	};
	
	private final DefaultTableModel billTableDataModel = new DefaultTableModel(billTableColumnNames, 6){

        public int getColumnCount() { 
            return 8; 
        }

        public int getRowCount() { 
            return 10;
        }

        public Object getValueAt(int row, int col) { 
        	// For sino column auto increment number
        	if(col==0 && row < 4)
        		return new Integer(row+1);
        	// For per column
        	else if(col==6 && row < 4)
        		return "kg";
        	// For hsn sac
        	else if(col==2 && row < 4)
        		return "0908";
        	// For hsn sac
        	else if(col==3 && row < 4)
        		return "5";
        	// for bardana
        	else if(col==1 && row ==5)
        		return ProjectConstants.baradana;
        	// for bardana
        	else if(col==1 && row ==6)
        		return ProjectConstants.baradana;
        	else
        		return null;
        }
        
        public boolean isCellEditable(int row, int col) { 
            return true; 
        }

	};
	
	public static void alignCellAndRow(JTable table, int alignment)
    {
		alignment = SwingConstants.CENTER;
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(alignment);

        TableModel tableModel = table.getModel();

        for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++)
        {
            table.getColumnModel().getColumn(columnIndex).setCellRenderer(rightRenderer);
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

		JTable billTable = new JTable(billTableDataModel);
		alignCellAndRow(billTable, SwingConstants.CENTER);
		billTableScrollPane = new JScrollPane(billTable);
		
		JTable totalTable = new JTable(totalTableDataModel);
		totalTableScrollPane = new JScrollPane(totalTable);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(351)
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(35)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(totalTableScrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 746, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(buyerAddressTextArea)
										.addComponent(sellerAddressTextArea, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(notInuseTextArea, GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
										.addGroup(groupLayout.createSequentialGroup()
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
													.addGroup(groupLayout.createSequentialGroup()
														.addComponent(despatchedThroughTextField, GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
														.addPreferredGap(ComponentPlacement.UNRELATED))
													.addComponent(despatchDocumentNoTextField, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
												.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
													.addComponent(deliverNoteTextField)
													.addComponent(invoiceNumTextField, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)))
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
													.addGroup(groupLayout.createSequentialGroup()
														.addGap(56)
														.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
															.addComponent(notInUseTextField, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE)
															.addComponent(datedTextField, GroupLayout.PREFERRED_SIZE, 188, GroupLayout.PREFERRED_SIZE)))
													.addComponent(destinationTextField, GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE))
												.addComponent(deliveryNoteDateTextField, GroupLayout.PREFERRED_SIZE, 238, GroupLayout.PREFERRED_SIZE)))))
								.addComponent(billTableScrollPane, GroupLayout.DEFAULT_SIZE, 746, Short.MAX_VALUE))))
					.addGap(50))
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
								.addComponent(invoiceNumTextField, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(29)
									.addComponent(notInUseTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(deliverNoteTextField, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(despatchDocumentNoTextField, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
								.addComponent(deliveryNoteDateTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(destinationTextField, GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
								.addComponent(despatchedThroughTextField, GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(notInuseTextArea, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(billTableScrollPane, GroupLayout.PREFERRED_SIZE, 318, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
					.addComponent(totalTableScrollPane, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		setLayout(groupLayout);
		
	}
	
	Image image = (new ImageIcon(ProjectConstants.backgroundImage)).getImage();
	private JScrollPane totalTableScrollPane;

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null)
			g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);

	}
}
