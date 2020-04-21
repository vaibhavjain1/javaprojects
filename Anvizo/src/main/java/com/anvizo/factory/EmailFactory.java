package com.anvizo.factory;

import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import lombok.Data;

@Data
public class EmailFactory {

	private final Map<String, String> transportConfiguration;
	private final String username;
	private final String password;

	public EmailFactory(Map<String, String> inTransportConfiguration, String inUsername, String inPassword)
	{
		transportConfiguration = inTransportConfiguration;
		username = inUsername;
		password = inPassword;
	}

	public MimeMessage createMessage() throws MessagingException {
		Properties mailProps = new Properties();
		for (Map.Entry<String, String> cfg : transportConfiguration.entrySet())
		{
			mailProps.put(cfg.getKey(), cfg.getValue());

		}
		
		String inUsername = new String(Base64.getDecoder().decode(username));
		String inpassword = new String(Base64.getDecoder().decode(password));
		
		Session mailSession = Session.getDefaultInstance(mailProps, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(inUsername, inpassword);
			}
		});

		MimeMessage msg = new MimeMessage(mailSession);
		msg.setFrom(new InternetAddress(inUsername));
		msg.setSentDate(new Date());
		return msg;
	}

	public void sendMail(MimeMessage message) throws MessagingException {
		Transport.send(message);
	}
}
