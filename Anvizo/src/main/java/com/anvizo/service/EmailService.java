package com.anvizo.service;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.anvizo.factory.EmailFactory;

@Service
public class EmailService {

	private final EmailFactory emailer;

	public EmailService(final EmailFactory inEmailer) {
		emailer = inEmailer;
	}
	
	public void sendContactUsEmail() {
		 try {
			MimeMessage message = emailer.createMessage();
			message.setSubject("Subject");
            message.setText("body");
            message.addRecipient(RecipientType.TO,new InternetAddress("abvaibhav@gmail.com"));
            emailer.sendMail(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
