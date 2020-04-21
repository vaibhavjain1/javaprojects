package com.anvizo.service;

import java.util.Arrays;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.anvizo.factory.EmailFactory;
import com.anvizo.model.ContactUs;
import com.anvizo.util.Constants;

@Service
public class EmailService {

	private final EmailFactory emailer;

	private static final Logger LOG = Logger.getLogger(EmailService.class);

	public EmailService(final EmailFactory inEmailer) {
		emailer = inEmailer;
	}

	public void sendContactUsEmail(ContactUs email) {
		LOG.info("Creating contact us message to be sent");
		try {
			MimeMessage message = emailer.createMessage();
			message.setSubject(email.getSubject());
			
			message.setText(
							"Message received from: " + email.getName() + 
							"\n\nMessage:\n" + email.getMessage()+ 
							"\n\nEmail of client: " + email.getEmail());
			
			message.addRecipient(RecipientType.TO, new InternetAddress(Constants.SUPPORT_EMAIL));

			emailer.sendMail(message);
			LOG.info("Message sent to:" + Arrays.toString(message.getAllRecipients()));
		} catch (Exception e) {
			LOG.error("Error while sending contactus message", e);
		}
	}
}
