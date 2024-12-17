package com.app_bank.app_bank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(

		info = @Info(
				title = "Banque Application Api",
				description = "Banque d'une application de transaction bancaire",
				version = "v1.0",
				contact = @Contact(
						name = "Teguera Aboubacar",
						email = "aboubacarteguera21@gmail.com",
						url = "https://github.com/BegginnerCoder21/app_bank"
				),
				license = @License(
						name = "BegginnerCoder21",
						url = "https://github.com/BegginnerCoder21"
				)

		),
		externalDocs = @ExternalDocumentation(
				description = "BegginnerCoder21 Banque Application Api Documentation",
				url = "https://github.com/BegginnerCoder21/app_bank"
		)
)
public class AppBankApplication {

	public static void main(String[] args) {

		SpringApplication.run(AppBankApplication.class, args);
	}

}
