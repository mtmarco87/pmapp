package com.ricardo.pmapp;

import com.ricardo.pmapp.configurations.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ AppConfig.class })
public class PmAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(PmAppApplication.class, args);
	}
}
