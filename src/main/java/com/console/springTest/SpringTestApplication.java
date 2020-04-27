package com.console.springTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * @author Denis Glushkov
 * @version 1.0
 * Main-класс со стратом приложения.
 */
@SpringBootApplication
@ComponentScan
public class SpringTestApplication {
	public static void main(String[] args) throws IOException {
		Logger logger = Logger.getLogger(SpringTestApplication.class.getName());
		Handler handler = new FileHandler("default.log");
		logger.addHandler(handler);
		logger.info("Главный класс серверного приложения запущен.");
		SpringApplication.run(SpringTestApplication.class, args);
	}
}
