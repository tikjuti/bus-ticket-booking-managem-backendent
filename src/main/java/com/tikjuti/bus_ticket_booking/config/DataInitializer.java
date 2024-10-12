//package com.tikjuti.bus_ticket_booking.config;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//
//@Component
//public class DataInitializer implements CommandLineRunner {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public DataInitializer(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public void initializeStoredProcedures() {
//        String proceduresDirectory = "src/main/resources/StoredProcedures";
//
//        try {
//            List<Path> sqlFiles = Files.list(Paths.get(proceduresDirectory))
//                    .filter(path -> path.toString().endsWith(".sql"))
//                    .toList(); // Lấy danh sách các file SQL
//
//            for (Path sqlFile : sqlFiles) {
//                String sql = loadSqlFromFile(sqlFile); // Tải SQL từ file
//
//                // Lấy tên procedure từ tên file
//                String procedureName = sqlFile.getFileName().toString().replace(".sql", "");
//
//                // Kiểm tra nếu procedure đã tồn tại
//                String checkProcedureSql = "SHOW PROCEDURE STATUS WHERE Name = '" + procedureName + "'";
//                Integer procedureCount = jdbcTemplate.queryForObject(checkProcedureSql, Integer.class);
//
//                // Nếu procedure không tồn tại, tạo mới
//                if (procedureCount == null || procedureCount == 0) {
//                    jdbcTemplate.execute(sql); // Thực thi SQL tạo procedure
//                    System.out.println("Procedure '" + procedureName + "' created successfully.");
//                } else {
//                    System.out.println("Procedure '" + procedureName + "' already exists.");
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Phương thức để tải SQL từ file
//    private String loadSqlFromFile(Path sqlFilePath) throws IOException {
//        return Files.readString(sqlFilePath); // Đọc toàn bộ nội dung file
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        initializeStoredProcedures();
//    }
//}
