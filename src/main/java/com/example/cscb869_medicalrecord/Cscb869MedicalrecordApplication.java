package com.example.cscb869_medicalrecord;

import com.example.cscb869_medicalrecord.entity.User;
import com.example.cscb869_medicalrecord.enums.Role;
import com.example.cscb869_medicalrecord.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Cscb869MedicalrecordApplication {

	public static void main(String[] args) {
		SpringApplication.run(Cscb869MedicalrecordApplication.class, args);
	}

	@Bean
	CommandLineRunner seed(UserRepository repo, PasswordEncoder encoder) {
		return args -> {
			if (!repo.existsByUsername("admin")) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword(encoder.encode("admin123"));
				admin.setRole(Role.ADMIN);
				repo.save(admin);
			}
		};
	}

}
