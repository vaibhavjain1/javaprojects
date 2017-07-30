package invoicegenerator;

import static invoicegenerator.Utilities.logger;

import java.io.FileOutputStream;
import java.text.DecimalFormat;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import InvoiceUI.ProjectConstants;

public class InvoicePrinter {

	private BaseFont bfBold;
	private BaseFont bf;

	public static void main(String[] args) {
		String pdfFilename = "c:/exportpdf.pdf";
		logger.info("Creating Pdf");
		InvoicePrinter generateInvoice = new InvoicePrinter();
		if (args.length < 0) {
			System.err.println("Usage: java " + generateInvoice.getClass().getName() + " c:/exportpdf.pdf");
			System.exit(1);
		}
		generateInvoice.createPDF(pdfFilename);

	}

	private void createPDF(String pdfFilename) {

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

			boolean beginPage = true;
			int y = 0;

			for (int i = 0; i < 10; i++) {
				if (beginPage) {
					beginPage = false;
					generateLayout(doc, cb);
					y = 550;
				}
				generateDetail(doc, cb, i, y);
				y = y - 15;
				if (y < 50) {

					doc.newPage();
					beginPage = true;
				}
			}

		} catch (DocumentException dex) {
			logger.error("Error While creating new PDF");
		} catch (Exception ex) {
			logger.error("Unknown Error while creating PDF");
		} finally {
			if (doc != null) {
				doc.close();
			}
			if (docWriter != null) {
				docWriter.close();
			}
		}
	}

	private void generateLayout(Document doc, PdfContentByte cb) {

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
			createContent(cb, 283, 745, "Invoice No.",PdfContentByte.ALIGN_LEFT);
			createContent(cb, 283, 719, "Delivery Note",PdfContentByte.ALIGN_LEFT);
			createContent(cb, 283, 690, "Despatch Document No.",PdfContentByte.ALIGN_LEFT);
			createContent(cb, 283, 665, "Despatched through",PdfContentByte.ALIGN_LEFT);
			createContent(cb, 405, 745, "Dated",PdfContentByte.ALIGN_LEFT);
			createHeadingsH3(cb, 405, 730, "13-Jul-2017");
			createContent(cb, 405, 690, "Delivery Note Date",PdfContentByte.ALIGN_LEFT);
			createContent(cb, 405, 665, "Destination",PdfContentByte.ALIGN_LEFT);
			// Invoice Detail box layout
			cb.rectangle(45, 285, 475, 308);
			// Horizontal line
			cb.moveTo(45, 570);
			cb.lineTo(520, 570);
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
			cb.moveTo(45, 320);
			cb.lineTo(520, 320);
			cb.stroke();

			createHeadingsH1(cb, 238, 763, ProjectConstants.taxInvoiceHeading);
			createHeadingsH3(cb, 49, 740, ProjectConstants.sellerName);
			createContent(cb, 49, 728, ProjectConstants.sellerAddressLine1,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 49, 716, ProjectConstants.sellerAddressLine2,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 49, 702, ProjectConstants.sellerAddressLine3,PdfContentByte.ALIGN_LEFT);
			createContent(cb, 49, 690, ProjectConstants.sellerEmail,PdfContentByte.ALIGN_LEFT);
			
			// Invoice Detail box Text Headings
			createContent(cb, 47, 585, "SI",PdfContentByte.ALIGN_LEFT);
			createContent(cb, 47, 575, "No",PdfContentByte.ALIGN_LEFT);
			createContent(cb, 85, 585, "Description of Goods",PdfContentByte.ALIGN_LEFT);
			createContent(cb, 230, 585, "HSN/SAC",PdfContentByte.ALIGN_LEFT);
			createContent(cb, 287, 585, "GST Rate",PdfContentByte.ALIGN_LEFT);
			createContent(cb, 340, 585, "Quantity",PdfContentByte.ALIGN_LEFT);
			createContent(cb, 385, 585, "Rate",PdfContentByte.ALIGN_LEFT);
			createContent(cb, 432, 585, "Per",PdfContentByte.ALIGN_LEFT);
			createContent(cb, 460, 585, "Amount",PdfContentByte.ALIGN_LEFT);
			
			
		} catch (Exception ex) {
			logger.error("Error while generating layout");
		}

	}

	private void generateDetail(Document doc, PdfContentByte cb, int index, int y) {
		DecimalFormat df = new DecimalFormat("0.00");

		try {

			createContent(cb, 55, y, String.valueOf(index + 1), PdfContentByte.ALIGN_RIGHT);
			createContent(cb, 66, y, "ITEM" + String.valueOf(index + 1), PdfContentByte.ALIGN_LEFT);
			createContent(cb, 228, y, "0908 ", PdfContentByte.ALIGN_LEFT);
			createContent(cb, 292, y, "5% ", PdfContentByte.ALIGN_LEFT);
			createContent(cb, 336, y, "25.00 KG ", PdfContentByte.ALIGN_LEFT);
			createContent(cb, 387, y, "830.00 ", PdfContentByte.ALIGN_LEFT);
			createContent(cb, 435, y, "kg ", PdfContentByte.ALIGN_LEFT);
			createContent(cb, 465, y, "20,750 ", PdfContentByte.ALIGN_LEFT);
			/*
			 * double price = Double.valueOf(df.format(Math.random() * 10));
			 * double extPrice = price * (index + 1); createContent(cb, 498, y,
			 * df.format(price), PdfContentByte.ALIGN_RIGHT); createContent(cb,
			 * 568, y, df.format(extPrice), PdfContentByte.ALIGN_RIGHT);
			 */
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void createHeadingsH1(PdfContentByte cb, float x, float y, String text) {
		cb.beginText();
		cb.setFontAndSize(bfBold, 12);
		cb.setTextMatrix(x, y);
		cb.showText(text.trim());
		cb.endText();
	}

	private void createHeadingsH3(PdfContentByte cb, float x, float y, String text) {
		cb.beginText();
		cb.setFontAndSize(bfBold, 11);
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

	private void initializeFonts() {
		try {
			bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		} catch (Exception e) {
			logger.error("Error while initializing fonts");
		}
	}

}