package com.bitespeed.backendTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BackendTaskApplication {

	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(BackendTaskApplication.class, args);
//		ResponseService responseServices = ctx.getBean(ResponseService.class);
//		System.out.println(responseServices.getContactInfo("madhav aman srivastava aditya khushi","1222"));
	}

}
