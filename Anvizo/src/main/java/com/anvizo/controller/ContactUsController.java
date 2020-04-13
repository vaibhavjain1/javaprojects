package com.anvizo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.anvizo.model.ContactUs;
import com.anvizo.service.EmailService;
import com.anvizo.util.Constants;

@RestController
public class ContactUsController {


	@Autowired
	private EmailService emailService;
	
	@PostMapping(path = Constants.CONTACT_US_CONTROLLER, consumes = MediaType.APPLICATION_JSON_VALUE)
	ContactUs postObject(@RequestBody ContactUs email) {
		emailService.sendContactUsEmail();
		return email;
	}
}
