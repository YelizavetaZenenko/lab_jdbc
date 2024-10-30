package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Controller
public class YourController {

    private final DataSource dataSource;

    @Autowired
    public YourController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/select-table")
    public String selectTable(Model model) {
        return "selectTable";
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
        return "olimpiada"; // имя шаблона для страницы с результатами
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

    @GetMapping("/sportsman")
    public String listSportsman(Model model) {
        StringBuilder sportsmanResults = new StringBuilder();
        String sql = "SELECT * FROM sportsman;";

        try (var connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                sportsmanResults.append("There are no records in the sportsman table.");
            } else {
                while (rs.next()) {
                    int sportsman_id = rs.getInt("sportsman_id");
                    String sportsman_name = rs.getString("sportsman_name");
                    int sportsman_age = rs.getInt("sportsman_age");

                    sportsmanResults.append(String.format("ID: %d, Name: %s, Age: %d<br/>",
                            sportsman_id, sportsman_name, sportsman_age));
                }
            }
        } catch (SQLException e) {
            sportsmanResults.append("Error while getting list of sportsmans: ").append(e.getMessage());
        }

        model.addAttribute("sportsmanResults", sportsmanResults.toString());
        return "sportsman";


    }


    @GetMapping("/create-entry")
    public String createEntry(Model model) {
        model.addAttribute("title", "Create New Entry");
        return "create-entry";
    }

    @PostMapping("/create")
    public String handleCreateEntry(@RequestParam String name, @RequestParam int age) {
        return "redirect:/";
    }

    @GetMapping("/olimpiada-create")
    public String showCreateOlimpiadaForm() {
        return "olimpiada-create";
    }

    @PostMapping("/olimpiada-create")
    public String createOlimpiada(@RequestParam("olimpiada_id") int id,
                                  @RequestParam("olimpiada_year") int year,
                                  @RequestParam("olimpiada_city") String city,
                                  @RequestParam("olimpiada_view") String view,
                                  Model model) {
        String sql = "INSERT INTO olimpiada (olimpiada_id," +
                "olimpiada_year, olimpiada_city, olimpiada_view) VALUES (?, ?, ?, ?)";

        try (var connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setInt(2, year);
            pstmt.setString(3, city);
            pstmt.setString(4, view);

            pstmt.executeUpdate();
            model.addAttribute("message", "New entry created successfully!");
        } catch (SQLException e) {
            model.addAttribute("message", "Error while creating new entry: " + e.getMessage());
        }

        return "redirect:/olimpiada";
    }

    @GetMapping("/sport-create")
    public String showCreateSportForm() {
        return "sport-create";
    }

    @PostMapping("/sport-create")
    public String createSport(@RequestParam("sport_id") int id,
                                  @RequestParam("sport_name") String name,
                                  Model model) {
        String sql = "INSERT INTO sport (sport_id," +
                "sport_name) VALUES (?, ?)";

        try (var connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);

            pstmt.executeUpdate();
            model.addAttribute("message", "New entry created successfully!");
        } catch (SQLException e) {
            model.addAttribute("message", "Error while creating new entry: " + e.getMessage());
        }

        return "redirect:/sport";
    }

    @GetMapping("/sportsman-create")
    public String showCreateSportsmanForm() {
        return "sportsman-create";
    }

    @PostMapping("/sportsman-create")
    public String createSportsman(@RequestParam("sportsman_id") int id,
                                  @RequestParam("sportsman_name") String name,
                                  @RequestParam("sportsman_age") int age,
                              Model model) {
        String sql = "INSERT INTO sportsman (sportsman_id," +
                "sportsman_name, sportsman_age) VALUES (?, ?, ?)";

        try (var connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, age);

            pstmt.executeUpdate();
            model.addAttribute("message", "New entry created successfully!");
        } catch (SQLException e) {
            model.addAttribute("message", "Error while creating new entry: " + e.getMessage());
        }

        return "redirect:/sportsman";
    }

    @GetMapping("/delete-by-key")
    public String showDeleteByKeyForm() {
        return "delete-by-key";
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

    @GetMapping("/sportsman-delete")
    public String getSportsmantDeleteForm() {
        return "sportsman-delete";
    }


    @PostMapping("/sportsman-delete")
    public String deleteSportsman(@RequestParam("sportsman_id") int id, Model model) {
        String sql = "DELETE FROM sportsman WHERE sportsman_id = ?";

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

        return "redirect:/sportsman";
    }

    @GetMapping("/edit-table")
    public String getEditTableForm() {
        return "edit-table";
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

    @GetMapping("/sportsman-edit")
    public String getUpdateSportsmanForm() {
        return "sportsman-edit";
    }

    @PostMapping("/sportsman-edit")
    public String updateSportsman(@RequestParam("sportsman_id") int id,
                              @RequestParam("sportsman_name") String name,
                              @RequestParam("sportsman_age") int age,
                              Model model) {
        String sql = "UPDATE sportsman SET sportsman_name = ?, sportsman_age = ? WHERE sportsman_id = ?";
        try (var connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setInt(3, id);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                model.addAttribute("message", "Record updated successfully!");
            } else {
                model.addAttribute("message", "Record not found.");
            }
        } catch (SQLException e) {
            model.addAttribute("message", "Error while updating record: " + e.getMessage());
        }

        return "redirect:/sportsman";
    }


}

