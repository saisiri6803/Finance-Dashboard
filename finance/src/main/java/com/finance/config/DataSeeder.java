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
    private final FinancialRecordRepository recordRepo;
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

            // Seed financial records
            LocalDate now = LocalDate.now();
            recordRepo.save(FinancialRecord.builder()
                    .amount(new BigDecimal("5000.00")).type(RecordType.INCOME)
                    .category("Salary").date(now.minusDays(5))
                    .notes("Monthly salary").createdBy(admin).build());

            recordRepo.save(FinancialRecord.builder()
                    .amount(new BigDecimal("1200.00")).type(RecordType.EXPENSE)
                    .category("Rent").date(now.minusDays(4))
                    .notes("Monthly rent").createdBy(admin).build());

            recordRepo.save(FinancialRecord.builder()
                    .amount(new BigDecimal("350.00")).type(RecordType.EXPENSE)
                    .category("Utilities").date(now.minusDays(3))
                    .notes("Electricity + water").createdBy(admin).build());

            recordRepo.save(FinancialRecord.builder()
                    .amount(new BigDecimal("800.00")).type(RecordType.INCOME)
                    .category("Freelance").date(now.minusDays(2))
                    .notes("Consulting work").createdBy(analyst).build());

            recordRepo.save(FinancialRecord.builder()
                    .amount(new BigDecimal("200.00")).type(RecordType.EXPENSE)
                    .category("Groceries").date(now.minusDays(1))
                    .notes("Weekly groceries").createdBy(admin).build());

            log.info("✅ Seed data loaded. Login: admin@finance.com / admin123");
        };
    }
}