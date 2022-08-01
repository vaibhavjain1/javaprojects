import com.spire.doc.*;

public class ExtractData {

	public static void main(String[] args) {
		
		Document doc = new Document();
		
		doc.loadFromFile("C:\\Users\\vaibhav.jain\\Downloads\\Documents\\Vikash Jain.docx");
		doc.getHtmlExportOptions().setImageEmbedded(true);
		doc.saveToFile("result.html",FileFormat.Html);
		
	}

}
