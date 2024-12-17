package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SqlRequestController {

    private final DataSource dataSource;

    public SqlRequestController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/execute-sql")
    public String executeSqlQuery(@RequestParam("sqlQuery") String sqlQuery, Model model) {
        try (var connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {

            ResultSet rs = pstmt.executeQuery();
            List<Map<String, Object>> results = new ArrayList<>();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                results.add(row);
            }

            model.addAttribute("results", results);
            return "result"; // Страница с результатами
        } catch (SQLException e) {
            model.addAttribute("message", "Error while executing SQL query: " + e.getMessage());
            return "result";
        }
    }
}
