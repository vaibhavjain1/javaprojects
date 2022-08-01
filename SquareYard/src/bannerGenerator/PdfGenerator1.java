package bannerGenerator;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.Date;

import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.svg.converter.SvgConverter;

public class PdfGenerator1 {

	public static void main(String[] args) throws Exception {
		String pdfFilename = ProjectConstants.generatedPdfFolder + "squareYard" + new Date() + ".pdf";
		pdfFilename = pdfFilename.replaceAll(" ", "_").replaceAll(":", "-");
		new PdfGenerator1().testSVG(pdfFilename);
		try {
			Desktop.getDesktop().open(new File(pdfFilename));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void testSVG(String fileName) throws Exception {
		//Initialize PDF writer
		PdfWriter writer = new PdfWriter(fileName);

		//Initialize PDF document
		PdfDocument pdf = new PdfDocument(writer);

		// Initialize document
		Document document = new Document(pdf, new PageSize(8640, 3456));
        
        document.add(new Paragraph("new pikachu"));                      

        
        
        PdfCanvas canvas = new PdfCanvas(pdf.getPage(1));
        canvas.rectangle(0, 0, 5758, 3456);
        canvas.setFillColor(new DeviceCmyk(0,0,0,100));
        canvas.fill();
        
        URL svgUrl = new File(ProjectConstants.iconsFolder + "Asset_1.svg").toURI().toURL();
        Image image = SvgConverter.convertToImage(svgUrl.openStream(), pdf).setFixedPosition(500, 1500);
        document.add(image);

        //Close document
        document.close();
	}
}

