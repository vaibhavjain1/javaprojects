package com.paypal;

import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMailWithReport {

	public static void main(String args[]) throws Exception {
		/*SendMailWithReport obj = new SendMailWithReport();
		obj.sendMail(Resources.SEND_TO_MAIL, "");
		*/
		
		Runtime runtime = Runtime.getRuntime();
		String[] cmd = { "sh", "sendMailForLinux.sh", "Trying something", "/x/home07/vaibhjain/One_box_testing_reports/One_Box Mon_Jun_12_10-23-00_PDT_2017.html", "Some subject", Resources.SEND_TO_MAIL, Resources.SEND_CC_MAIL };
		Process proc = runtime.exec(cmd);
		int returnValue = proc.waitFor();
		if(returnValue!=0)
			throw new Exception("Error while sending mail with error code:"+returnValue);
	}

	public void sendMail(String send_to_mail, String fileSource) throws Exception {
		// Recipient's email ID needs to be mentioned.
		String to = send_to_mail;

		// Sender's email ID needs to be mentioned
		String from = Resources.SEND_MAIL_USER_NAME;

		String subject = "One box testing report";

		String body = "Please find attached one box testing report and Do not reply to this mail";
		if (System.getProperty("os.name").startsWith("Linux") || System.getProperty("os.name").startsWith("LINUX")) {
			Runtime runtime = Runtime.getRuntime();
			/*
			 * 0 - sh file to send mail.
			 * 1 - Send to
			 * 2 - mail body
			 * 3 - Report file
			 * 4 - CC to
			 * 5 - Subject
			 */
			String[] cmd = { Resources.ONE_BOX_LINUX_REPORT_FOLDER_PATH+"sendMailForLinux.sh",send_to_mail,"One Box Report. Please do not reply to this mail", fileSource, "vaibhjain@paypal.com", "One_Box_Report"};
			Process proc = runtime.exec(cmd);
			int returnValue = proc.waitFor();
			if(returnValue!=0)
				throw new Exception("Error while sending mail with error code:"+returnValue);
		} else {

			final String username = Resources.SEND_MAIL_USER_NAME;
			final String password = Resources.SEND_MAIL_PASSWORD;

			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "25");

			// Get the Session object.
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// Set CC: header filed of header
			if (!Resources.SEND_CC_MAIL.equalsIgnoreCase(""))
				message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(Resources.SEND_CC_MAIL));

			// Set Subject: header field
			message.setSubject(subject);

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message
			messageBodyPart.setText(body);

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			String filename = fileSource.substring(fileSource.lastIndexOf("/"));
			DataSource source = new FileDataSource(fileSource);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			message.setContent(multipart);

			// Send message
			Transport.send(message);
		}

		System.out.println("Sent mail successfully to " + send_to_mail + "....");
	}
}
