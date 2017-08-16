package InvoiceUI;

import java.text.DecimalFormat;

public class ProjectConstants {

	public static final String ApplicationName = null;
	public static String frameIcon = "resources/favicon.png";
	
	/* Heading */
	public static String taxInvoiceHeading = "Tax Invoice";
	public static String forRecipientHeading = "(ORIGINAL FOR RECIPIENT)";
	public static String forTransporterHeading = "(DUPLICATE FOR TRANSPORTER)";
	public static String forSupplierHeading = "(TRIPLICATE FOR SUPPLIER)";
	public static String sellerName = "Naresh Kirana Company";
	public static String sellerAddressLine1 = "19A GADODIA MARKET";
	public static String sellerAddressLine2 = "KHARI BAOLI";
	public static String sellerAddressLine3 = "GSTIN/UIN: 07AAJPK2146C1Z3";
	public static String sellerEmail = "E-Mail : NKCG1986@GMAIL.COM";
	public static String buyerAddressHeading = "Buyer";
	public static String invoiceNoHeading = "Invoice No.";
	public static String datedHeading = "Dated";
	public static String deliveryNoteHeading = "Delivery Note";
	public static String despatchDocumentNo = "Despatch Document No.";
	public static String deliveryNoteDateHeading = "Delivery Note Date";
	public static String despatchedThroughHeading = "Despatched through";
	public static String destinationHeading = "Destination";
	public static String backgroundImage = "resources/slide_48.jpg";
	public static String generatedPdfFolder = "resources/";
	public static String nkcSupply = "NKC/SUPPLY/";
	public static int maxItems = 4;
	public static DecimalFormat dFormat = new DecimalFormat(".##");
	
	/* Table Headings */
	public static String siHeading = "SI";
	public static String NumHeading = "No.";
	public static String descriptionOfGoodsHeading = "Description of Goods";
	public static String hsnSacHeading = "HSN/SAC";
	public static String gstRateHeading = "GST Rate";
	public static String quantityHeading = "Quantity";
	public static String rateHeading = "Rate";
	public static String perHeading = "per";
	public static String amountHeading = "Amount";

	public static String bardana = "BARDANA";
	public static String majdoori = "MAJDOORI";
	public static String outputIGST = "OUTPUT IGST";
	public static String outputCGST = "OUTPUT CGST";
	public static String outputSGST = "OUTPUT SGST";
	public static String roundOff = "ROUND OFF";
	public static String total = "Total";
	public static String amountChargableInwords = "Amount Chargeable (in words)";
	public static String eoe = "E. & O.E";
	
	public static String taxableHeading = "Taxable";
	public static String valueHeading = "Value";
	public static String integratedTax = "Integrated Tax";
	public static String centralTax = "Central Tax";
	public static String stateTax = "State Tax";
	public static String rate = "Rate";
	public static String amount = "Amount";
	
	// Footer
	public static String taxAmountInwords = "Tax Amount (in words) :";
	public static String companyPan = "Company's PAN :";
	public static String companyPanNumber = "AAJPK2146C";
	public static String decleration = "Declaration";
	public static String declerationLine1 = "1. All disputes shall be settled in Delhi Jurisdiction only.";
	public static String declerationLine2 = "2. Certified that the particulars given above are true and";
	public static String declerationLine3 = "correct.";
	public static String companyBankDetails = "Company's Bank Details";
	public static String bankName = "Bank Name : United Bank of India";
	public static String bankAcNo = "A/c No. : 0414050013049";
	public static String branchIfscCode = "Branch & IFS Code : Khari Baoli & UTBI0KHB712";
	public static String forNareshComp = "for Naresh Kirana Company";
	public static String authSign = "Authorised Signatory";
	public static String compInvoice = "This is a Computer Generated Invoice";
	
	public static String productName = "GST Bill Generator";
	public static String productVersion = "1.0";
}
