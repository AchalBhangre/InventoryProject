package com.example.csvDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages="repository")
@ComponentScan(basePackages = {"controller","Service","repository"})
@EntityScan(basePackages = "controller")
public class CsvDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsvDemoApplication.class, args);
	}

}
