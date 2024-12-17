package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class SportController {

    private final DataSource dataSource;

    @Autowired

    public SportController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/sport")
    public String listSport(Model model) {
        StringBuilder sportResults = new StringBuilder();
        String sql = "SELECT * FROM sport;";

        try (var connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                sportResults.append("There are no records in the sport table.");
            } else {
                while (rs.next()) {
                    int sport_id = rs.getInt("sport_id");
                    String sport_name = rs.getString("sport_name");

                    sportResults.append(String.format("ID: %d, Name: %s<br/>",
                            sport_id, sport_name));
                }
            }
        } catch (SQLException e) {
            sportResults.append("Error while getting list of sports: ").append(e.getMessage());
        }

        model.addAttribute("sportResults", sportResults.toString());
        return "sport";
    }

    @GetMapping("/sport-create")
    public String showCreateSportForm() {
        return "sport-create";
    }

    @PostMapping("/sport-create")
    public String createSport(@RequestParam("sport_name") String name,
                              Model model) {
        String sql = "INSERT INTO sport (sport_name) VALUES (?)";

        try (var connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);

            pstmt.executeUpdate();
            model.addAttribute("message", "New entry created successfully!");
        } catch (SQLException e) {
            model.addAttribute("message", "Error while creating new entry: " + e.getMessage());
        }

        return "redirect:/sport";
    }

    @GetMapping("/sport-delete")
    public String getSportDeleteForm() {
        return "sport-delete";
    }


    @PostMapping("/sport-delete")
    public String deleteSport(@RequestParam("sport_id") int id, Model model) {
        String sql = "DELETE FROM sport WHERE sport_id = ?";

        try (var connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                model.addAttribute("message", "Entry deleted successfully!");
            } else {
                model.addAttribute("message", "Entry not found or already deleted.");
            }
        } catch (SQLException e) {
            model.addAttribute("message", "Error while deleting entry: " + e.getMessage());
        }

        return "redirect:/sport";
    }

    @GetMapping("/sport-edit")
    public String getUpdateSportForm() {
        return "sport-edit";
    }

    @PostMapping("/sport-edit")
    public String updateSport(@RequestParam("sport_id") int id,
                              @RequestParam("sport_name") String name,
                              Model model) {
        String sql = "UPDATE sport SET sport_name = ? WHERE sport_id = ?";

        try (var connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, id);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                model.addAttribute("message", "Record updated successfully!");
            } else {
                model.addAttribute("message", "Record not found.");
            }
        } catch (SQLException e) {
            model.addAttribute("message", "Error while updating record: " + e.getMessage());
        }

        return "redirect:/sport";
    }
}
