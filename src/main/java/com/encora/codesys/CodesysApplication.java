package com.encora.codesys;

import com.encora.codesys.taskmanager.entity.Users;
import com.encora.codesys.taskmanager.repository.UserRepository;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
public class CodesysApplication {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	//@PostConstruct
	public void createUser(){
		userRepository.saveAll(Arrays.asList(
			new Users("admin",passwordEncoder.encode("admin"),"admin@encora.com"),
			new Users("test",passwordEncoder.encode("test"),"test@encora.com")
		));
	}

	public static void main(String[] args) {
		SpringApplication.run(CodesysApplication.class, args);
	}

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI().addSecurityItem(new SecurityRequirement().
						addList("Bearer Authentication"))
				.components(new Components().addSecuritySchemes
						("Bearer Authentication", createAPIKeyScheme()))
				.info(new Info().title("REST API")
						.description("Some custom description of API.")
						.version("1.0").contact(new Contact().name("Ghansham Zinge")
								.email( "www.taskmanager.com").url("zinge.sham@gmail.com"))
						.license(new License().name("License of API")
								.url("API license URL")));
	}

	private SecurityScheme createAPIKeyScheme() {
		return new SecurityScheme().type(SecurityScheme.Type.HTTP)
				.bearerFormat("JWT")
				.scheme("bearer");
	}
}
