package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
public class PppApplication {

	public static void main(String[] args) {
		SpringApplication.run(PppApplication.class, args);

		String url = "jdbc:postgresql://localhost:5432/laba3";
		String username = "postgres";
		String password = "lizaliza123";

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			System.out.println("Connection to database successful!");

			UserDAO userDAO = new UserDAO(connection);

			userDAO.listOlimpiada();
			userDAO.listSport();
			userDAO.listSportsman();

		} catch (SQLException e) {
			System.err.println("Database connection error: " + e.getMessage());
		}
	}
}
