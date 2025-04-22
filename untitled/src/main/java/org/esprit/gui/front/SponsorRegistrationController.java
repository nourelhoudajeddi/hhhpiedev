package org.esprit.gui.front;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.esprit.models.Sponsor;
import org.esprit.models.Event;
import org.esprit.services.SponsorService;

import java.io.IOException;

public class SponsorRegistrationController {
    private final Event event;
    private final SponsorService sponsorService;
    private final Stage dialogStage;

    @FXML private Label eventLabel;
    @FXML private TextField nameField;
    @FXML private TextField montantField;
    @FXML private TextField emailField;

    public SponsorRegistrationController(Event event) {
        this.event = event;
        this.sponsorService = new SponsorService();
        this.dialogStage = new Stage();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/SponsorRegistrationDialog.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 400, 300);

            // Add stylesheets
            scene.getStylesheets().addAll(
                    getClass().getResource("/styles/style.css").toExternalForm(),
                    getClass().getResource("/styles/front-office.css").toExternalForm()
            );

            dialogStage.setScene(scene);
            dialogStage.setTitle("Register as Sponsor");
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
                Sponsor sponsor = new Sponsor();
                sponsor.setEvent(event);
                sponsor.setSponsorName(nameField.getText());
                sponsor.setMontant(Integer.parseInt(montantField.getText()));
                sponsor.setEmail(emailField.getText());

                sponsorService.add(sponsor);
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Successfully registered as sponsor for " + event.getEventName());
                dialogStage.close();
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Please enter a valid number for sponsorship amount");
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
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter your organization name");
            return false;
        }
        if (montantField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter the sponsorship amount");
            return false;
        }
        try {
            Integer.parseInt(montantField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter a valid number for sponsorship amount");
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