package invoicegenerator;

import static utilities.InvoiceUtil.logger;

import java.util.ArrayList;
import java.util.List;

public class ControllerParams{
	
	public BillHeaderInputParams BillHeaderInputParamsObj;
	public AllGoods AllGoodsObj;
	
	public class BillHeaderInputParams {
		public String invoiceNumber;
		public String date;
		public String deliveryNote;
		public String despatchDocumentNo;
		public String deliveryNoteDate;
		public String despatchedThrough;
		public String destination;
		public String buyerDetails;	
	}
	
	public class Good{
		int siNo;
		String DescriptionOfGoods;
		String hsnSac;
		double gstRate;
		double quantity;
		double rate;
		String unit;
		double Amount;
		
		public Good(int siNo, String descriptionOfGoods, String hsnSac, double gstRate, double quantity, double rate, double amount) {
			this.siNo = siNo;
			this.DescriptionOfGoods = descriptionOfGoods;
			this.hsnSac = hsnSac;
			this.gstRate = gstRate;
			this.quantity = quantity;
			this.rate = rate;
			this.unit = "kg";
			this.Amount = amount;
		}

		@Override
		public String toString() {
			return "Good [siNo=" + siNo + ", DescriptionOfGoods=" + DescriptionOfGoods + ", hsn_sac=" + hsnSac
					+ ", gstRate=" + gstRate + ", quantity=" + quantity + ", rate=" + rate + ", unit=" + unit + ", Amount="
					+ Amount + "]";
		}
		
	}
	
	public class AllGoods{
		int totalCount;
		double totalQuantity;
		double bardana;
		double majdoori;
		double igst;
		double cgst;
		double sgst;
		double roundOff;
		double totalAmountInNumber;
		double totalAmountWithGST;
		List<Good> goodsList;
		
		public AllGoods(double totalQuantity, double bardana, double majdoori, double igst, double cgst,
				double sgst, double roundOff, double totalAmountInNumber, double totalAmountWithGST) {
			this.totalQuantity = totalQuantity;
			this.bardana = bardana;
			this.majdoori = majdoori;
			this.igst = igst;
			this.cgst = cgst;
			this.sgst = sgst;
			this.roundOff = roundOff;
			this.totalAmountInNumber = totalAmountInNumber;
			this.totalAmountWithGST = totalAmountWithGST;
			goodsList = new ArrayList<>();
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
}

