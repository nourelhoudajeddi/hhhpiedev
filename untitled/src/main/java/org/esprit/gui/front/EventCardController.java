package org.esprit.gui.front;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.esprit.models.Event;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class EventCardController {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

    @FXML private VBox root;
    @FXML private Label nameLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label dateLabel;
    @FXML private Label locationLabel;
    @FXML private Label statusLabel;

    private Event event;

    public void setEvent(Event event) {
        this.event = event;
        updateUI();
    }

    private void updateUI() {
        if (event == null) {
            return;
        }
        nameLabel.setText(event.getEventName());
        descriptionLabel.setText(event.getEventDescription());
        dateLabel.setText("Date: " + event.getEventDate().format(DATE_FORMATTER));
        locationLabel.setText("Location: " + event.getLocation());
        statusLabel.setText("Status: " + event.getStatus());
    }

    // This method is automatically called by FXML loader
    @FXML
    private void initialize() {
        // Any initialization code can go here
        if (event != null) {
            updateUI();
        }
    }    public static EventCardController loadEventCard(Event event) {
        try {
            // Get the resource URL and verify it's not null
            var resourceUrl = EventCardController.class.getResource("/front/EventCard.fxml");
            if (resourceUrl == null) {
                throw new IOException("EventCard.fxml not found in classpath at /front/EventCard.fxml");
            }
            
            FXMLLoader loader = new FXMLLoader(resourceUrl);
            loader.setController(new EventCardController()); // Set controller instance explicitly
            VBox root = loader.load();
            EventCardController controller = loader.getController();
            controller.root = root; // Set the root explicitly
            controller.setEvent(event);
            return controller;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load EventCard.fxml: " + e.getMessage(), e);
        }
    }

    public VBox getRoot() {
        return root;
    }

    @FXML
    private void handleAttendeeRegistration() {
        new AttendeeRegistrationController(event).showAndWait();
    }

    @FXML
    private void handleSponsorRegistration() {
        new SponsorRegistrationController(event).showAndWait();
    }
}