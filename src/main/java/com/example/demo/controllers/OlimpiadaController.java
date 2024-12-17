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
//@RequestMapping("/olimpiada")
public class OlimpiadaController {

    private final DataSource dataSource;

    @Autowired
    public OlimpiadaController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/olimpiada")
    public String listOlimpiada(Model model) {
        StringBuilder olimpiadaResults = new StringBuilder();
        String sql = "SELECT * FROM olimpiada;";

        try (var connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                olimpiadaResults.append("There are no records in the olimpiada table.");
            } else {
                while (rs.next()) {
                    int olimpiada_id = rs.getInt("olimpiada_id");
                    int olimpiada_year = rs.getInt("olimpiada_year");
                    String olimpiada_city = rs.getString("olimpiada_city");
                    String olimpiada_view = rs.getString("olimpiada_view");

                    olimpiadaResults.append(String.format("ID: %d, Year: %d, City: %s, View: %s<br/>",
                            olimpiada_id, olimpiada_year, olimpiada_city, olimpiada_view));
                }
            }
        } catch (SQLException e) {
            olimpiadaResults.append("Error while getting list of olympiads: ").append(e.getMessage());
        }

        model.addAttribute("olimpiadaResults", olimpiadaResults.toString());
        return "olimpiada";
    }

    @GetMapping("/olimpiada-create")
    public String showCreateOlimpiadaForm() {
        return "olimpiada-create";
    }

    @PostMapping("/olimpiada-create")
    public String createOlimpiada(@RequestParam("olimpiada_year") int year,
                                  @RequestParam("olimpiada_city") String city,
                                  @RequestParam("olimpiada_view") String view,
                                  Model model) {
        String sql = "INSERT INTO olimpiada (olimpiada_year, olimpiada_city, olimpiada_view) VALUES (?, ?, ?)";

        try (var connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, year);
            pstmt.setString(2, city);
            pstmt.setString(3, view);

            pstmt.executeUpdate();
            model.addAttribute("message", "New entry created successfully!");

        } catch (SQLException e) {
            model.addAttribute("message", "Error while creating new entry: " + e.getMessage());
        }

        return "redirect:/olimpiada";
    }


    @GetMapping("/olimpiada-delete")
    public String getOlimpiadaDeleteForm() {
        return "olimpiada-delete";
    }


    @PostMapping("/olimpiada-delete")
    public String deleteOlimpiada(@RequestParam("olimpiada_id") int id, Model model) {
        String sql = "DELETE FROM olimpiada WHERE olimpiada_id = ?";

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

        return "redirect:/olimpiada";
    }
    @GetMapping("/olimpiada-edit")
    public String getUpdateOlimpiadaForm() {
        return "olimpiada-edit";
    }

    @PostMapping("/olimpiada-edit")
    public String updateOlimpiada(@RequestParam("olimpiada_id") int id,
                                  @RequestParam("olimpiada_year") int year,
                                  @RequestParam("olimpiada_city") String city,
                                  @RequestParam("olimpiada_view") String view,
                                  Model model) {
        String sql = "UPDATE olimpiada SET olimpiada_year = ?, olimpiada_city = ?" +
                ", olimpiada_view = ? WHERE olimpiada_id = ?";

        try (var connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, year);
            pstmt.setString(2, city);
            pstmt.setString(3, view);
            pstmt.setInt(4, id);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                model.addAttribute("message", "Record updated successfully!");
            } else {
                model.addAttribute("message", "Record not found.");
            }
        } catch (SQLException e) {
            model.addAttribute("message", "Error while updating record: " + e.getMessage());
        }

        return "redirect:/olimpiada";
    }


}