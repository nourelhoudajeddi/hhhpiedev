package org.esprit.gui.back;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.esprit.models.Sponsor;

public class SponsorDialogController {
    @FXML private TextField sponsorNameField;
    @FXML private TextField amountField;
    @FXML private TextField emailField;
    
    private Sponsor sponsor;

    @FXML
    private void initialize() {
        // Add numeric validation for amount field
        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                amountField.setText(oldValue);
            }
        });
    }

    public void setSponsor(Sponsor sponsor) {
        this.sponsor = sponsor;
        sponsorNameField.setText(sponsor.getSponsorName());
        amountField.setText(String.valueOf(sponsor.getMontant()));
        emailField.setText(sponsor.getEmail());
    }

    public Sponsor getSponsor() {
        if (sponsor == null) {
            sponsor = new Sponsor();
        }
        sponsor.setSponsorName(sponsorNameField.getText());        try {
            double amount = Double.parseDouble(amountField.getText());
            sponsor.setMontant((int) Math.round(amount));
        } catch (NumberFormatException e) {
            sponsor.setMontant(0);
        }
        sponsor.setEmail(emailField.getText());
        return sponsor;
    }

    public boolean validateFields() {
        boolean isValid = true;
        String errorMessage = "";

        // Validate sponsor name
        if (sponsorNameField.getText().trim().isEmpty()) {
            errorMessage += "Sponsor name is required.\n";
            isValid = false;
        }

        // Validate amount
        if (amountField.getText().trim().isEmpty()) {
            errorMessage += "Amount is required.\n";
            isValid = false;
        } else {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    errorMessage += "Amount must be a positive number.\n";
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                errorMessage += "Amount must be a valid number.\n";
                isValid = false;
            }
        }

        // Validate email
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            errorMessage += "Email is required.\n";
            isValid = false;
        } else if (!isValidEmail(email)) {
            errorMessage += "Please enter a valid email address.\n";
            isValid = false;
        }

        // Show error message if validation failed
        if (!isValid) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        }

        return isValid;
    }

    private boolean isValidEmail(String email) {
        // Basic email validation using regex
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

}
