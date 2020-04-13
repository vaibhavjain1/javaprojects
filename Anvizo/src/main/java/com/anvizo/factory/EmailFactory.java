package com.anvizo.factory;

import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Data
public class EmailFactory {

	private Map<String, String> transportConfiguration;
	private String auth;
	private String username;
	private String password;
	private String defaultFromAddress;
	
	public EmailFactory(Map<String, String> inTransportConfiguration, String auth, String username, String password,
			String defaultFromAddress) {
		transportConfiguration = inTransportConfiguration;
		this.auth = auth;
		this.username = username;
		this.password = password;
		this.defaultFromAddress = defaultFromAddress;
	}
	
	public MimeMessage createMessage() throws MessagingException
	{
		Properties mailProps = new Properties();
		for (Map.Entry<String, String> cfg : transportConfiguration.entrySet())
        {
            mailProps.put(cfg.getKey(), cfg.getValue());
            
        }
		Session mailSession = Session.getDefaultInstance(mailProps);
		MimeMessage msg = new MimeMessage(mailSession);
        msg.setSentDate(new Date());
        msg.setFrom(new InternetAddress(defaultFromAddress));
		return msg;
	}

	public void sendMail(MimeMessage message) throws MessagingException {
		  Transport.send(message);
	}
}
