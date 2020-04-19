package com.anvizo.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anvizo.util.Constants;

@RestController
public class DefaultController {

	@Autowired
	private ApplicationContext context;
	
	private static final Logger LOG = Logger.getLogger(DefaultController.class);
	
	//@RequestMapping(value = "/", method = RequestMethod.GET)
	@RequestMapping(value = "servicecheck", method = RequestMethod.GET)
	public String index() {
		LOG.info(Constants.ANVIZO + " Service is up");
		return Constants.ANVIZO + " Service is up";
	}
	
	@SuppressWarnings({ "unused", "resource" })
	public void getRegisteredBeans() {
		ApplicationContext context1 = new FileSystemXmlApplicationContext("classpath:/applicationContext.xml");
		try (ClassPathXmlApplicationContext context2 = new ClassPathXmlApplicationContext("applicationContext.xml")) {
			
		}
		System.out.println("Number of beans:");
        System.out.println(context.getBeanDefinitionCount());

        String[] names = context.getBeanDefinitionNames();
        for(String name : names)
        {
            System.out.println("-----------------");
            System.out.println(name);
        }
	}
}
