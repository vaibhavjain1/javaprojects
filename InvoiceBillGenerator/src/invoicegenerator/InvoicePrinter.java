package invoicegenerator;

import static utilities.InvoiceUtil.logger;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfWriter;

import InvoiceUI.BillPanel;
import InvoiceUI.ProjectConstants;
import invoicegenerator.ControllerParams.AllGoods;
import invoicegenerator.ControllerParams.BillHeaderInputParams;
import invoicegenerator.ControllerParams.Good;
import utilities.NumberToWord;

public class InvoicePrinter {

	private BaseFont bfBold;
	private BaseFont bf;
	private static String copyHeading = "";

	public static void main(String[] args) {
		ControllerParams controlParam = new ControllerParams();
		BillHeaderInputParams billHeaderObj = controlParam.new BillHeaderInputParams();
		billHeaderObj.buyerDetails = "ALLIED BUSINESS COMPANY";
		billHeaderObj.date = "13-Jul-2017";
		billHeaderObj.invoiceNumber = "NKC/SUPPLY/010";
		billHeaderObj.deliveryNote = "---add delivery note---";
		billHeaderObj.despatchDocumentNo = "---add despatchDocNo---";
		billHeaderObj.deliveryNoteDate = "---add deliverynotedate---";
		billHeaderObj.despatchedThrough = "---add despatched---";
		billHeaderObj.destination = "---add destination---";
		controlParam.BillHeaderInputParamsObj = billHeaderObj;
		
		double totalQuantity = 10;
		double bardana = 50;
		double majdoori = 50;
		double cgst = 500;
		double sgst = 500;
		double igst = 500;
		double roundOff = 10;
		double totalAmountWithGST = 120000;
		AllGoods allGoods = controlParam.new AllGoods(totalQuantity, bardana, majdoori, igst, cgst, sgst, roundOff, 0,	totalAmountWithGST);
		
			String itemDesc = "Item 1";
			String hsnSac = "0908";
			double gstRate = 5;
			double quanity = 120;
			double rate = 1000;
			double amount = 50000;
				Good good = controlParam.new Good(1, itemDesc, hsnSac, gstRate, quanity, rate, amount);
				allGoods.addGoodInList(good);
		
		controlParam.AllGoodsObj = allGoods;
		
		new InvoicePrinter().GeneratePdf(controlParam);
	}

	public void GeneratePdf(ControllerParams controlParamObj){
		String pdfFilename = ProjectConstants.generatedPdfFolder+"Invoice"+new Date()+".pdf";
		String pdfFilename_1 = ProjectConstants.generatedPdfFolder+"Invoice"+new Date()+"_1.pdf";
		String pdfFilename_2 = ProjectConstants.generatedPdfFolder+"Invoice"+new Date()+"_2.pdf";
		String pdfFilename_3 = ProjectConstants.generatedPdfFolder+"Invoice"+new Date()+"_3.pdf";
		pdfFilename = pdfFilename.replaceAll(" ", "_").replaceAll(":", "-");
		pdfFilename_1 = pdfFilename_1.replaceAll(" ", "_").replaceAll(":", "-");
		pdfFilename_2 = pdfFilename_2.replaceAll(" ", "_").replaceAll(":", "-");
		pdfFilename_3 = pdfFilename_3.replaceAll(" ", "_").replaceAll(":", "-");
		
		logger.info("Creating Pdf");
		InvoicePrinter generateInvoice = new InvoicePrinter();
		copyHeading = ProjectConstants.forRecipientHeading;
		generateInvoice.createPDF(pdfFilename_1, controlParamObj);
		copyHeading = ProjectConstants.forTransporterHeading;
		generateInvoice.createPDF(pdfFilename_2, controlParamObj);
		copyHeading = ProjectConstants.forSupplierHeading;
		generateInvoice.createPDF(pdfFilename_3, controlParamObj);
		concatenatePdfs(new File(pdfFilename), pdfFilename_1, pdfFilename_2, pdfFilename_3);
		try {
			Desktop.getDesktop().open(new File(pdfFilename));
		} catch (IOException e) {
			logger.error("Error while opening Pdf in system editor. Check if adobe reader is installed. File Path:"+pdfFilename);
		}
	}
	
	public static void concatenatePdfs(File outputFile, String... listOfPdfFiles){
		Document document = null;
		try {
			document = new Document();
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			PdfCopy copy = new PdfSmartCopy(document, outputStream);
			document.open();
			for (String inFilePath : listOfPdfFiles) {
			    PdfReader reader = new PdfReader(inFilePath);
			    copy.addDocument(reader);
			    reader.close();
			}
		} catch (DocumentException | IOException e) {
			
		} finally {
			document.close();
		}
	}
	
	private void createPDF(String pdfFilename, ControllerParams controlParams){
		Document doc = new Document();
		PdfWriter docWriter = null;
		initializeFonts();

		try {
			String path = pdfFilename;
			docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));
			doc.addAuthor("Vaibhav");
			doc.addCreationDate();
			doc.addProducer();
			doc.addCreator("Vaibhav");
			doc.addTitle("Invoice");
			doc.setPageSize(PageSize.LETTER);
			doc.open();
			PdfContentByte cb = docWriter.getDirectContent();
			generateStaticContent(cb, controlParams.BillHeaderInputParamsObj);
			generateDetail(cb, controlParams.AllGoodsObj);
		} catch (Exception ex) {
			logger.error("Unknown Error while creating PDF");
		} finally {
			if (doc != null) {doc.close();}
			if (docWriter != null) {docWriter.close();}
		}
		logger.info("PDF generated successfully @ "+pdfFilename);
	}

	private void generateStaticContent(PdfContentByte cb, BillHeaderInputParams billHeaderObj) throws Exception {
		if(billHeaderObj==null){
			logger.error("Bill header data is not reaching to pdf");
			throw new Exception();
		}
		try {
			cb.setLineWidth(.5f);

			// Invoice Header box layout
			cb.rectangle(45, 673, 475, 80);
			cb.rectangle(45, 593, 475, 80);
			// Horizontal lines
			cb.moveTo(280, 728);
			cb.lineTo(520, 728);
			cb.moveTo(280, 700);
			cb.lineTo(520, 700);
			cb.moveTo(280, 646);
			cb.lineTo(520, 646);
			// vertical lines
			cb.moveTo(280, 593);
			cb.lineTo(280, 753);
			cb.moveTo(400, 645);
			cb.lineTo(400, 753);
			cb.stroke();

			// Invoice Header box Text Headings
			createContent(cb, 283, 745, ProjectConstants.invoiceNoHeading,PdfContentByte.ALIGN_LEFT);
			createHeadings(cb, 283, 730, billHeaderObj.invoiceNumber,11);
			createContent(cb, 405, 745, ProjectConstants.datedHeading,PdfContentByte.ALIGN_LEFT);
			createHeadings(cb, 405, 730, billHeaderObj.date,11);
			createContent(cb, 283, 719, ProjectConstants.deliveryNoteHeading,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 283, 704, billHeaderObj.deliveryNote,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 283, 690, ProjectConstants.despatchDocumentNo,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 283, 675, billHeaderObj.despatchDocumentNo,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 283, 665, ProjectConstants.despatchedThroughHeading,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 283, 650, billHeaderObj.despatchedThrough,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 405, 690, ProjectConstants.deliveryNoteDateHeading,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 405, 675, billHeaderObj.deliveryNoteDate,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 405, 665, ProjectConstants.destinationHeading,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 405, 650, billHeaderObj.destination,PdfContentByte.ALIGN_LEFT);
			
			createHeadings(cb, 238, 763, ProjectConstants.taxInvoiceHeading,12);
			createContent(cb, 390, 763, copyHeading,PdfContentByte.ALIGN_LEFT);
			createHeadings(cb, 49, 740, ProjectConstants.sellerName,11);
			createContent(cb, 49, 728, ProjectConstants.sellerAddressLine1,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 49, 716, ProjectConstants.sellerAddressLine2,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 49, 702, ProjectConstants.sellerAddressLine3,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 49, 690, ProjectConstants.sellerEmail,PdfContentByte.ALIGN_LEFT);
			
			createContent(cb, 49, 665, ProjectConstants.buyerAddressHeading,PdfContentByte.ALIGN_LEFT);
			String[] buyerAdd = billHeaderObj.buyerDetails.split("\n");
			for (int i = 0; i < buyerAdd.length; i++) {
				createContent(cb, 49, 650 - (10*i), buyerAdd[i],PdfContentByte.ALIGN_LEFT);
			}
		
			// Invoice Detail box layout
			cb.rectangle(45, 285, 475, 308);
			// Horizontal line
			cb.moveTo(45, 570); // For invoice heading
			cb.lineTo(520, 570); // For total box
			cb.moveTo(45, 310);
			cb.lineTo(520, 310);
			// Vertical lines
			cb.moveTo(60, 285);
			cb.lineTo(60, 593);
			cb.moveTo(225, 285);
			cb.lineTo(225, 593);
			cb.moveTo(284, 285);
			cb.lineTo(284, 593);
			cb.moveTo(330, 285);
			cb.lineTo(330, 593);
			cb.moveTo(380, 285);
			cb.lineTo(380, 593);
			cb.moveTo(430, 285);
			cb.lineTo(430, 593);
			cb.moveTo(450, 285);
			cb.lineTo(450, 593);

			// Invoice Detail box Text Headings
			createContent(cb, 47, 583, ProjectConstants.siHeading,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 47, 574, ProjectConstants.NumHeading,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 85, 583, ProjectConstants.descriptionOfGoodsHeading,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 230, 583, ProjectConstants.hsnSacHeading,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 287, 583, ProjectConstants.gstRateHeading,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 338, 583, ProjectConstants.quantityHeading,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 385, 583, ProjectConstants.rate,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 432, 583, ProjectConstants.perHeading,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 460, 583, ProjectConstants.amount,PdfContentByte.ALIGN_LEFT);
			
			createContent(cb, 172, 450, ProjectConstants.bardana,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 168, 438, ProjectConstants.majdoori,PdfContentByte.ALIGN_LEFT);
			if(BillPanel.iGSTEnabled){
				createContent(cb, 155, 426, ProjectConstants.outputIGST,PdfContentByte.ALIGN_LEFT);
			} else {
				createContent(cb, 150, 426, ProjectConstants.outputCGST,PdfContentByte.ALIGN_LEFT);
				createContent(cb, 150, 414, ProjectConstants.outputSGST,PdfContentByte.ALIGN_LEFT);
			}
			createContent(cb, 161, 402, ProjectConstants.roundOff,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 200, 293, ProjectConstants.total,PdfContentByte.ALIGN_LEFT);
			
			// Invoice footer box
			cb.rectangle(45, 70, 475, 215);
			// Break up table Horizontal line
			cb.moveTo(45, 250);
			cb.lineTo(520, 250);
			cb.moveTo(45, 225);
			cb.lineTo(520, 225);
			cb.moveTo(45, 210);
			cb.lineTo(520, 210);
			cb.moveTo(45, 195);
			cb.lineTo(520, 195);
			cb.moveTo(330, 238);
			cb.lineTo(520, 238);
			// Break up table Vertical lines
			cb.moveTo(265, 195);
			cb.lineTo(265, 250);
			cb.moveTo(330, 195);
			cb.lineTo(330, 250);
			
			
			// Signature box
			cb.moveTo(280, 120);
			cb.lineTo(520, 120);
			cb.moveTo(280, 120);
			cb.lineTo(280, 70);
			
			if(BillPanel.iGSTEnabled){
				createContent(cb, 390, 242, ProjectConstants.integratedTax,PdfContentByte.ALIGN_LEFT);
				createContent(cb, 350, 230, ProjectConstants.rate,PdfContentByte.ALIGN_LEFT);
				createContent(cb, 440, 230, ProjectConstants.amount,PdfContentByte.ALIGN_LEFT);
				createContent(cb, 350, 215, "5%",PdfContentByte.ALIGN_LEFT);
				//vertical
				cb.moveTo(420, 195);
				cb.lineTo(420, 238);
			} else {
				createContent(cb, 350, 242, ProjectConstants.centralTax,PdfContentByte.ALIGN_LEFT);
				createContent(cb, 450, 242, ProjectConstants.stateTax,PdfContentByte.ALIGN_LEFT);
				createContent(cb, 340, 230, ProjectConstants.rate,PdfContentByte.ALIGN_LEFT);
				createContent(cb, 380, 230, ProjectConstants.amount,PdfContentByte.ALIGN_LEFT);
				createContent(cb, 430, 230, ProjectConstants.rate,PdfContentByte.ALIGN_LEFT);
				createContent(cb, 480, 230, ProjectConstants.amount,PdfContentByte.ALIGN_LEFT);
				createContent(cb, 340, 215, "2.50%",PdfContentByte.ALIGN_LEFT);
				createContent(cb, 430, 215, "2.50%",PdfContentByte.ALIGN_LEFT);
				//vertical
				cb.moveTo(375, 195);
				cb.lineTo(375, 238);
				cb.moveTo(420, 195);
				cb.lineTo(420, 250);
				cb.moveTo(470, 195);
				cb.lineTo(470, 238);
			}
			
			
			createContent(cb, 48, 274, ProjectConstants.amountChargableInwords,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 480, 274, ProjectConstants.eoe,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 140, 240, ProjectConstants.hsnSacHeading,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 280, 240, ProjectConstants.taxableHeading,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 285, 230, ProjectConstants.valueHeading,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 48, 215, "0908",PdfContentByte.ALIGN_LEFT);
			createContent(cb, 240, 200, ProjectConstants.total,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 48, 180, ProjectConstants.taxAmountInwords,PdfContentByte.ALIGN_LEFT);
			
			createContent(cb, 280, 160, ProjectConstants.companyBankDetails,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 280, 148, ProjectConstants.bankName,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 280, 134, ProjectConstants.bankAcNo,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 280, 122, ProjectConstants.branchIfscCode,PdfContentByte.ALIGN_LEFT);
			createHeadings(cb, 409, 113, ProjectConstants.forNareshComp,8);
			createContent(cb, 432, 73, ProjectConstants.authSign,PdfContentByte.ALIGN_LEFT);
			
			createContent(cb, 48, 115, ProjectConstants.companyPan,PdfContentByte.ALIGN_LEFT);
			createHeadings(cb, 150, 115, ProjectConstants.companyPanNumber,9);
			createContent(cb, 48, 102, ProjectConstants.decleration,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 48, 92, ProjectConstants.declerationLine1,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 48, 82, ProjectConstants.declerationLine2,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 48, 72, ProjectConstants.declerationLine3,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 200, 57, ProjectConstants.compInvoice,PdfContentByte.ALIGN_LEFT);
			cb.stroke();
			
		} catch (Exception ex) {
			logger.error("Error while generating layout");
		}

	}

	private void generateDetail(PdfContentByte cb, AllGoods allGoodObj) {
		int y = 565;
		//DecimalFormat df = new DecimalFormat("0.00");
		if(allGoodObj.totalCount==0)
			return;
		for (Good good : allGoodObj.goodsList) {
			try {
				y = y - 15;
				createContent(cb, 55, y, String.valueOf(good.siNo), PdfContentByte.ALIGN_RIGHT);
				createContent(cb, 66, y, good.DescriptionOfGoods, PdfContentByte.ALIGN_LEFT);
				createContent(cb, 228, y, good.hsnSac, PdfContentByte.ALIGN_LEFT);
				createContent(cb, 292, y, String.valueOf(good.gstRate)+"%", PdfContentByte.ALIGN_LEFT);
				createContent(cb, 336, y, String.valueOf(good.quantity)+" KG ", PdfContentByte.ALIGN_LEFT);
				createContent(cb, 387, y, String.valueOf(good.rate), PdfContentByte.ALIGN_LEFT);
				createContent(cb, 435, y, "kg ", PdfContentByte.ALIGN_LEFT);
				createContent(cb, 465, y, String.valueOf(good.Amount), PdfContentByte.ALIGN_LEFT);
			}
			catch (Exception ex) {
				logger.error("Error while adding dynamic content");
			}
		}
		if(allGoodObj.bardana!=0.0)
		createContent(cb, 465, 450, String.valueOf(allGoodObj.bardana),PdfContentByte.ALIGN_LEFT);
		if(allGoodObj.majdoori!=0.0)
		createContent(cb, 465, 438, String.valueOf(allGoodObj.majdoori),PdfContentByte.ALIGN_LEFT);
		if(BillPanel.iGSTEnabled){
			createContent(cb, 465, 426, String.valueOf(allGoodObj.igst),PdfContentByte.ALIGN_LEFT);
			createContent(cb, 440, 215, String.valueOf(allGoodObj.igst),PdfContentByte.ALIGN_LEFT);
			createContent(cb, 440, 200, String.valueOf(allGoodObj.igst),PdfContentByte.ALIGN_LEFT);
			createContent(cb, 290, 215, String.valueOf(allGoodObj.totalAmountWithGST-allGoodObj.igst),PdfContentByte.ALIGN_LEFT);
			createContent(cb, 290, 200, String.valueOf(allGoodObj.totalAmountWithGST-allGoodObj.igst),PdfContentByte.ALIGN_LEFT);
			createContent(cb, 160, 180, NumberToWord.NumberToCurrency(allGoodObj.igst),PdfContentByte.ALIGN_LEFT);
		}else{
			createContent(cb, 465, 426, String.valueOf(allGoodObj.cgst),PdfContentByte.ALIGN_LEFT);
			createContent(cb, 465, 414, String.valueOf(allGoodObj.sgst),PdfContentByte.ALIGN_LEFT);
			createContent(cb, 380, 215, String.valueOf(allGoodObj.cgst),PdfContentByte.ALIGN_LEFT);
			createContent(cb, 380, 200, String.valueOf(allGoodObj.cgst),PdfContentByte.ALIGN_LEFT);
			createContent(cb, 480, 215, String.valueOf(allGoodObj.sgst),PdfContentByte.ALIGN_LEFT);
			createContent(cb, 480, 200, String.valueOf(allGoodObj.sgst),PdfContentByte.ALIGN_LEFT);
			createContent(cb, 280, 215, String.valueOf(allGoodObj.totalAmountWithGST-allGoodObj.cgst-allGoodObj.sgst),PdfContentByte.ALIGN_LEFT);
			createContent(cb, 280, 200, String.valueOf(allGoodObj.totalAmountWithGST-allGoodObj.cgst-allGoodObj.sgst),PdfContentByte.ALIGN_LEFT);
			createContent(cb, 160, 180, NumberToWord.NumberToCurrency(allGoodObj.cgst+allGoodObj.sgst),PdfContentByte.ALIGN_LEFT);
		}
		if(allGoodObj.roundOff!=0.0)
		createContent(cb, 465, 402, String.valueOf(allGoodObj.roundOff),PdfContentByte.ALIGN_LEFT);
		createContent(cb, 465, 293, String.valueOf(allGoodObj.totalAmountWithGST),PdfContentByte.ALIGN_LEFT);
		createContent(cb, 350, 293, String.valueOf(allGoodObj.totalQuantity),PdfContentByte.ALIGN_LEFT);
		
		createContent(cb, 48, 260, NumberToWord.NumberToCurrency(allGoodObj.totalAmountWithGST),PdfContentByte.ALIGN_LEFT);
		
	}

	private void createHeadings(PdfContentByte cb, float x, float y, String text, int size) {
		cb.beginText();
		cb.setFontAndSize(bfBold, size);
		cb.setTextMatrix(x, y);
		cb.showText(text.trim());
		cb.endText();
	}

	private void createContent(PdfContentByte cb, float x, float y, String text, int align) {
		cb.beginText();
		cb.setFontAndSize(bf, 9);
		cb.showTextAligned(align, text.trim(), x, y, 0);
		cb.endText();
	}
	
	private void createText(PdfContentByte cb, float x, float y, String text, boolean bold, boolean italics, boolean underline, int align) {
		//Font SUBFONT = new Font(Font.getFamily("TIMES_ROMAN"), 12,Font.UNDERLINE);
		//Font SUBFONT = new Font();
		Font SUBFONT = FontFactory.getFont(null,
			    BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 0.8f, Font.NORMAL, BaseColor.BLACK);
			BaseFont baseFont = SUBFONT.getBaseFont();
		try {
			BaseFont currBf = bf;
			if(bold && italics){
				
			} else if(bold) {
				
				currBf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			} else if(italics) {
				currBf = BaseFont.createFont(BaseFont.TIMES_ITALIC, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			} else if(underline) {
				currBf = BaseFont.createFont(BaseFont.TIMES_ITALIC, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			}
			
			
			cb.beginText();
			cb.setFontAndSize(baseFont, 12);
			cb.showTextAligned(align, text.trim(), x, y, 0);
			cb.endText();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}

	private void initializeFonts() {
		try {
			bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		} catch (Exception e) {
			logger.error("Error while initializing fonts");
		}
	}

}