package com.example.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public void listOlimpiada() throws SQLException {
        String sql = "SELECT * FROM olimpiada";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("Table ~Olimpiada~:");
            if (!rs.isBeforeFirst()) {
                System.out.println("There are no records in the olimpiada table.");
            } else {
                while (rs.next()) {
                    int olimpiada_id = rs.getInt("olimpiada_id");
                    int olimpiada_year = rs.getInt("olimpiada_year");
                    String olimpiada_city = rs.getString("olimpiada_city");
                    String olimpiada_view = rs.getString("olimpiada_view");

                    System.out.printf("ID: %d, Year: %d, City: %s, View: %s%n",
                            olimpiada_id, olimpiada_year, olimpiada_city, olimpiada_view);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while getting list of olympiads: " + e.getMessage());
        }
    }

    public void listSport() throws SQLException {
        String sql = "SELECT * FROM sport";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("Table ~Sport~:");
            if (!rs.isBeforeFirst()) {
                System.out.println("There are no records in the sport table.");
            } else {
                while (rs.next()) {
                    int sport_id = rs.getInt("sport_id");
                    String sport_name = rs.getString("sport_name");

                    System.out.printf("ID: %d, Name:  %s%n", sport_id, sport_name);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while getting list of sport: " + e.getMessage());
        }
    }

    public void listSportsman() throws SQLException {
        String sql = "SELECT * FROM sportsman";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("Table ~Sportsman~:");
            if (!rs.isBeforeFirst()) {
                System.out.println("There are no records in the sportsman table.");
            } else {
                while (rs.next()) {
                    int sportsman_id = rs.getInt("sportsman_id");
                    String sportsman_name = rs.getString("sportsman_name");
                    int sportsman_age = rs.getInt("sportsman_age");

                    System.out.printf("ID: %d, Name:  %s,  Age: %d%n",
                            sportsman_id, sportsman_name, sportsman_age);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while getting list of sportsman: " + e.getMessage());
        }
    }


}