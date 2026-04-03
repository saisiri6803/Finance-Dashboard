package com.finance.config;

import com.finance.model.*;
import com.finance.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    @Bean
    CommandLineRunner seedData() {
        return args -> {
            // Seed users
            User admin = userRepo.save(User.builder()
                    .name("Alice Admin").email("admin@finance.com")
                    .password(encoder.encode("admin123"))
                    .role(Role.ADMIN).active(true).build());

            User analyst = userRepo.save(User.builder()
                    .name("Bob Analyst").email("analyst@finance.com")
                    .password(encoder.encode("analyst123"))
                    .role(Role.ANALYST).active(true).build());

            userRepo.save(User.builder()
                    .name("Carol Viewer").email("viewer@finance.com")
                    .password(encoder.encode("viewer123"))
                    .role(Role.VIEWER).active(true).build());
        };
    }
}