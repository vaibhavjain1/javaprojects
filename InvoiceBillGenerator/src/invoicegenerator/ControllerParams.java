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
		this.DescriptionOfGoods = descriptionOfGoods;
		this.hsn_sac = "0908";
		this.gstRate = gstRate;
		this.quantity = quantity;
		this.rate = rate;
		this.unit = "kg";
		this.Amount = amount;
	}

	@Override
	public String toString() {
		return "Good [siNo=" + siNo + ", DescriptionOfGoods=" + DescriptionOfGoods + ", hsn_sac=" + hsn_sac
				+ ", gstRate=" + gstRate + ", quantity=" + quantity + ", rate=" + rate + ", unit=" + unit + ", Amount="
				+ Amount + "]";
	}
	
}

public class ControllerParams{
	
	public class BillHeaderInputParams {
		
		public String invoiceNumber;
		public String date;
		public String deliveryNote;
		public String despatchDocumentNo;
		public String deliveryNoteDate;
		public String despatchedThrough;
		public String destination;
		public String buyerName;
		public String buyerAddressLine1;
		public String buyerAddressLine2;
		public String buyerAddressLine3;
		public String buyerAddressLine4;
		public String buyerAddressLine5;
		
		public void setInvoiceNumber(String string) {
			try {
				this.invoiceNumber = "NKC/SUPLY/"+string;
			} catch (Exception e) {
				logger.error("Error while setting invoice number:"+string);
			}
		}
		
	}

	
}

