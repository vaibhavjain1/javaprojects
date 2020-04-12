package com.anvizo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anvizo.util.Constants;

@RestController
public class DefaultController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return Constants.ANVIZO + " Service is up";
	}
}
