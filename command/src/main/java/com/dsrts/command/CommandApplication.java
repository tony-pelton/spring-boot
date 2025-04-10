package com.dsrts.command;

import net.datafaker.Faker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class CommandApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommandApplication.class, args);
	}
	@Bean
	public Random random() {
		return new Random(1L);
	}
	@Bean
	public Faker faker(Random random) {
		return new Faker(random);
	}

}
