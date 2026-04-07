package com.finance.config;

import com.finance.entity.FinancialRecord;
import com.finance.entity.User;
import com.finance.enums.RecordType;
import com.finance.enums.Role;
import com.finance.repository.FinancialRecordRepository;
import com.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FinancialRecordRepository recordRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            System.out.println("Seeding test data...");
            seedUsers();
            seedFinancialRecords();
            System.out.println("Test data seeded successfully!");
        }
    }

    private void seedUsers() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@finance.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        User analyst = new User();
        analyst.setUsername("analyst");
        analyst.setEmail("analyst@finance.com");
        analyst.setPassword(passwordEncoder.encode("analyst123"));
        analyst.setRole(Role.ANALYST);
        userRepository.save(analyst);

        User viewer = new User();
        viewer.setUsername("viewer");
        viewer.setEmail("viewer@finance.com");
        viewer.setPassword(passwordEncoder.encode("viewer123"));
        viewer.setRole(Role.VIEWER);
        userRepository.save(viewer);

        System.out.println("Created test users: admin, analyst, viewer");
    }

    private void seedFinancialRecords() {
        User admin = userRepository.findByUsername("admin").orElseThrow();

        LocalDate today = LocalDate.now();

        List<FinancialRecord> records = Arrays.asList(
                createRecord(new BigDecimal("5000.00"), RecordType.INCOME, "Salary",
                        "Monthly salary", today.minusDays(30), admin),
                createRecord(new BigDecimal("5000.00"), RecordType.INCOME, "Salary",
                        "Monthly salary", today.minusDays(60), admin),
                createRecord(new BigDecimal("1500.00"), RecordType.INCOME, "Freelance",
                        "Web development project", today.minusDays(15), admin),
                createRecord(new BigDecimal("200.00"), RecordType.INCOME, "Investment",
                        "Stock dividends", today.minusDays(10), admin),
                createRecord(new BigDecimal("500.00"), RecordType.INCOME, "Bonus",
                        "Performance bonus", today.minusDays(5), admin),

                createRecord(new BigDecimal("1200.00"), RecordType.EXPENSE, "Rent",
                        "Monthly rent", today.minusDays(28), admin),
                createRecord(new BigDecimal("1200.00"), RecordType.EXPENSE, "Rent",
                        "Monthly rent", today.minusDays(58), admin),
                createRecord(new BigDecimal("400.00"), RecordType.EXPENSE, "Utilities",
                        "Electricity and water", today.minusDays(25), admin),
                createRecord(new BigDecimal("300.00"), RecordType.EXPENSE, "Groceries",
                        "Weekly groceries", today.minusDays(20), admin),
                createRecord(new BigDecimal("150.00"), RecordType.EXPENSE, "Groceries",
                        "Weekly groceries", today.minusDays(13), admin),
                createRecord(new BigDecimal("200.00"), RecordType.EXPENSE, "Transportation",
                        "Fuel and parking", today.minusDays(18), admin),
                createRecord(new BigDecimal("100.00"), RecordType.EXPENSE, "Entertainment",
                        "Movie and dinner", today.minusDays(7), admin),
                createRecord(new BigDecimal("80.00"), RecordType.EXPENSE, "Health",
                        "Gym membership", today.minusDays(3), admin),
                createRecord(new BigDecimal("250.00"), RecordType.EXPENSE, "Shopping",
                        "Clothes", today.minusDays(2), admin),
                createRecord(new BigDecimal("50.00"), RecordType.EXPENSE, "Entertainment",
                        "Streaming subscriptions", today.minusDays(1), admin));

        recordRepository.saveAll(records);
        System.out.println("Created " + records.size() + " sample financial records");
    }

    private FinancialRecord createRecord(BigDecimal amount, RecordType type,
            String category, String description,
            LocalDate date, User createdBy) {
        FinancialRecord record = new FinancialRecord();
        record.setAmount(amount);
        record.setType(type);
        record.setCategory(category);
        record.setDescription(description);
        record.setDate(date);
        record.setCreatedBy(createdBy);
        return record;
    }
}
