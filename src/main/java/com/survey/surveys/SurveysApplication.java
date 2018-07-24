package com.survey.surveys;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class SurveysApplication {

	public static void main(String[] args) {
		SpringApplication.run(SurveysApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(UserRepository userRepo){
		return args -> {
			// save a couple of customers
			userRepo.save(new User("Alasdar","hola123","alasdair@gmail.com"));
			userRepo.save(new User("Mika","holita123","micaela@gmail.com"));
			userRepo.save(new User("Adonis","cleo123","adonis@gmail.com"));

		};
	}
}

