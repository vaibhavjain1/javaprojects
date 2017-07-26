package invoicegenerator;

import static invoicegenerator.Utilities.logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class AllGoods{
	int totalCount;
	double totalQuantity;
	double bardana;
	double majdoori;
	double igst;
	double cgst;
	double sgst;
	double totalAmountInNumber;
	double totalAmountWithGST;
	String totalAmountWithGSTInWords;
	List<Good> goodsList;
	
	public AllGoods(int totalCount, double totalQuantity, double bardana, double majdoori, double igst, double cgst,
			double sgst, double totalAmountInNumber, double totalAmountWithGST, String totalAmountWithGSTInWords,
			List<Good> goodsList) {
		this.totalCount = totalCount;
		this.totalQuantity = totalQuantity;
		this.bardana = bardana;
		this.majdoori = majdoori;
		this.igst = igst;
		this.cgst = cgst;
		this.sgst = sgst;
		this.totalAmountInNumber = totalAmountInNumber;
		this.totalAmountWithGST = totalAmountWithGST;
		this.totalAmountWithGSTInWords = totalAmountWithGSTInWords;
		this.goodsList = goodsList;
	}
	
	public void addGoodInList(Good good){
		try {
			goodsList.add(good);
			totalCount++;
		} catch (Exception e) {
			logger.error("Error while adding good in list:"+good.toString());
		}
	}
	
}

class Good{
	int siNo;
	String DescriptionOfGoods;
	String hsn_sac;
	double gstRate;
	double quantity;
	double rate;
	String unit;
	double Amount;
	
	public Good(int siNo, String descriptionOfGoods, double gstRate, double quantity, double rate, double amount) {
		this.siNo = siNo;
		DescriptionOfGoods = descriptionOfGoods;
		this.hsn_sac = "0908";
		this.gstRate = gstRate;
		this.quantity = quantity;
		this.rate = rate;
		this.unit = "kg";
		Amount = amount;
	}

	@Override
	public String toString() {
		return "Good [siNo=" + siNo + ", DescriptionOfGoods=" + DescriptionOfGoods + ", hsn_sac=" + hsn_sac
				+ ", gstRate=" + gstRate + ", quantity=" + quantity + ", rate=" + rate + ", unit=" + unit + ", Amount="
				+ Amount + "]";
	}
	
}

class InputParams {
	
	String invoiceNumber;
	Date date;
	String deliveryNote;
	String despatchDocumentNo;
	String deliveryNoteDate;
	String despatchedThrough;
	String destination;
	String buyerName;
	String buyerAddressLine1;
	String buyerAddressLine2;
	String buyerAddressLine3;
	String buyerAddressLine4;
	String buyerAddressLine5;
	
	public void setInvoiceNumber(int invoiceNumber) {
		try {
			this.invoiceNumber = "NKC/SUPLY/"+String.valueOf(invoiceNumber);
		} catch (Exception e) {
			logger.error("Error while setting invoice number:"+invoiceNumber);
		}
	}
	
	public void setDate(String currDate){
		try {
			DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			this.date = formatter.parse(currDate);
		} catch (ParseException e) {
			logger.error("Error while setting Date:"+currDate);
		}
	}
	
}
