package br.com.gs1.gs1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Gs1Application {

	public static void main(String[] args) {
		SpringApplication.run(Gs1Application.class, args);
	}

}
