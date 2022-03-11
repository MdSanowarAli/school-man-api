package com.sano.schoolmanApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.sano.schoolmanApi"})
public class SchoolmanApp extends SpringBootServletInitializer {

	public static void main(String[] args) {
		System.out.println("Start Running...");
		SpringApplication.run(SchoolmanApp.class, args);
		System.out.println("App Running Successfully...!");
	}

}
