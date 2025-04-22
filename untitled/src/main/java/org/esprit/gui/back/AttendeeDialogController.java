package org.esprit.gui.back;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.esprit.models.Attendee;

public class AttendeeDialogController {
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;

    private Attendee attendee;

    @FXML
    private void initialize() {
        // Initialization code if needed
    }

    public void setAttendee(Attendee attendee) {
        this.attendee = attendee;
        if (attendee != null) {
            nameField.setText(attendee.getName());
            phoneField.setText(attendee.getPhoneNumber());
            emailField.setText(attendee.getEmail());
        }
    }

    /**
     * Updates the attendee object with field values.
     * This does NOT perform validation.
     */
    public Attendee getAttendee() {
        if (attendee == null) {
            attendee = new Attendee();
        }
        attendee.setName(nameField.getText());
        attendee.setPhoneNumber(phoneField.getText());
        attendee.setEmail(emailField.getText());
        return attendee;
    }

    /**
     * Validates input fields and shows warnings if validation fails.
     * @return true if all fields are valid, false otherwise
     */
    public boolean validateFields() {
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