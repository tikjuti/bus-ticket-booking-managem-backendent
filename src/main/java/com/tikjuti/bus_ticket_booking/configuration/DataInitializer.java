package com.tikjuti.bus_ticket_booking.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.core.io.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ResourcePatternResolver resourcePatternResolver;

    @PostConstruct
    public void initializeData() {
        try {
            // Load all .sql files from StoredProcedures and StoredFunctions directories
            Resource[] procedureResources = resourcePatternResolver.getResources("classpath:StoredProcedures/*.sql");
            Resource[] functionResources = resourcePatternResolver.getResources("classpath:StoredFunctions/*.sql");

            // Handle Stored Procedures
            processSQLFiles(procedureResources, "PROCEDURE");

            // Handle Functions
            processSQLFiles(functionResources, "FUNCTION");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processSQLFiles(Resource[] resources, String routineType) {
        for (Resource resource : resources) {
            String routineName = extractRoutineName(resource.getFilename());

            // Check if stored procedure or function exists
            if (!doesRoutineExist(routineName, routineType)) {
                // Create stored procedure or function
                createRoutine(resource, routineType);
            }
        }
    }

    private String extractRoutineName(String fileName) {
        // Remove .sql extension to get routine name
        return fileName != null ? fileName.replace(".sql", "") : null;
    }

    private boolean doesRoutineExist(String routineName, String routineType) {
        // Example query for checking stored procedure or function existence (for MySQL)
        String sql = "SELECT COUNT(*) FROM information_schema.ROUTINES " +
                "WHERE ROUTINE_TYPE = ? AND ROUTINE_NAME = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{routineType, routineName}, Integer.class);
        return count > 0;
    }

    private void createRoutine(Resource resource, String routineType) {
        try {
            // Read SQL file content
            String sql = new BufferedReader(new InputStreamReader(Objects.requireNonNull(resource.getInputStream()), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            // Execute SQL to create the routine
            jdbcTemplate.execute(sql);

            System.out.println(routineType + " " + resource.getFilename() + " created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run(String... args) throws Exception {
        initializeData();
    }
}
