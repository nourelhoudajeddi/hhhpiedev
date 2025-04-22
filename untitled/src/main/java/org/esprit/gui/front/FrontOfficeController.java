package org.esprit.gui.front;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.esprit.models.Event;
import org.esprit.services.EventService;

import java.io.IOException;
import java.util.List;

public class FrontOfficeController {
    private final EventService eventService = new EventService();

    @FXML private FlowPane eventCardsContainer;

    @FXML
    private void initialize() {
        loadEvents();
    }    private void loadEvents() {
        try {
            List<Event> events = eventService.getAll();
            eventCardsContainer.getChildren().clear();
            for (Event event : events) {
                EventCardController controller = EventCardController.loadEventCard(event);
                VBox root = controller.getRoot();
                root.getStylesheets().addAll(
                    getClass().getResource("/styles/style.css").toExternalForm(),
                    getClass().getResource("/styles/front-office.css").toExternalForm()
                );
                eventCardsContainer.getChildren().add(root);
            }
        } catch (Exception e) {
            // More detailed error message
            String errorMsg = "Failed to load events.\n" +
                    "Possible causes:\n" +
                    "1. EventCard.fxml is not in /front/ directory\n" +
                    "2. FXML file contains errors\n" +
                    "3. Resource path is incorrect\n" +
                    "Original error: " + e.getMessage();
            showAlert(Alert.AlertType.ERROR, "Error", errorMsg);
        }
    }    @FXML
    private void backToDashboard() {
        try {
            // Close current window
            ((Stage) eventCardsContainer.getScene().getWindow()).close();

            // Open dashboard - ensure this path is correct for your project structure
            Parent root = FXMLLoader.load(getClass().getResource("/DashboardWindow.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root, 400, 300);
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Event Management System - Dashboard");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to return to dashboard: " + e.getMessage());
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