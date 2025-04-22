package org.esprit.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private void openBackOffice(javafx.event.ActionEvent event) {
        try {
            // Close dashboard window
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Open back office window
            Parent root = FXMLLoader.load(getClass().getResource("/back/EventManagementApp.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Event Management System - Back Office");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open back office: " + e.getMessage());
        }
    }

    @FXML
    private void openFrontOffice(javafx.event.ActionEvent event) {
        try {
            // Close dashboard window
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Open front office window
            Parent root = FXMLLoader.load(getClass().getResource("/front/FrontOfficeWindow.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 700, 500)); // Set a smaller window size
            stage.setTitle("Event Management System - Front Office");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open front office: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}