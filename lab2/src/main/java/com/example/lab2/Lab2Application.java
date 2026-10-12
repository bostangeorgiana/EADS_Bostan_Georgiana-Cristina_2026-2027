package com.example.lab2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Lab2Application {

	public static void main(String[] args) {
		SpringApplication.run(Lab2Application.class, args);
	}

}

/*

java -jar target/lab2-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
java -jar target/lab2-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod

APP_DATA_LABEL=from-env-variable java -jar target/lab2-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
APP_DATA_LABEL=from-env-variable java -jar target/lab2-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev --app.data.label=from-command-line
 */