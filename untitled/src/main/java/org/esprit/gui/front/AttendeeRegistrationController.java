package org.esprit.gui.front;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.esprit.models.Attendee;
import org.esprit.models.Event;
import org.esprit.services.AttendeeService;

import java.io.IOException;

public class AttendeeRegistrationController {
    private final Event event;
    private final AttendeeService attendeeService;
    private final Stage dialogStage;

    @FXML private Label eventLabel;
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;

    public AttendeeRegistrationController(Event event) {
        this.event = event;
        this.attendeeService = new AttendeeService();
        this.dialogStage = new Stage();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/AttendeeRegistrationDialog.fxml"));
            // This is the critical line - set the controller BEFORE loading
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 400, 300);

            // Add stylesheets
            scene.getStylesheets().addAll(
                    getClass().getResource("/styles/style.css").toExternalForm(),
                    getClass().getResource("/styles/front-office.css").toExternalForm()
            );

            dialogStage.setScene(scene);
            dialogStage.setTitle("Register as Attendee");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML", e);
        }
    }

    @FXML
    private void initialize() {
        eventLabel.setText("Event: " + event.getEventName());
    }

    @FXML
    private void handleRegistration() {
        if (validateFields()) {
            try {
                Attendee attendee = new Attendee();
                attendee.setEvent(event);
                attendee.setName(nameField.getText());
                attendee.setPhoneNumber(phoneField.getText());
                attendee.setEmail(emailField.getText());

                attendeeService.add(attendee);
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Successfully registered for " + event.getEventName());
                dialogStage.close();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Registration failed: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void show() {
        dialogStage.show();
    }

    public void showAndWait() {
        dialogStage.showAndWait();
    }

    private boolean validateFields() {
        if (nameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter your name");
            return false;
        }
        if (phoneField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter your phone number");
            return false;
        }
        if (!phoneField.getText().matches("\\d{8}")) { // Ensures exactly 8 digits
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Phone number must be exactly 8 digits");
            return false;
        }
        if (emailField.getText().trim().isEmpty() || !emailField.getText().contains("@")) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter a valid email address");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}