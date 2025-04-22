package org.esprit.gui.back;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.esprit.models.Event;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class EventDialogController implements Initializable {
    public enum Mode {
        ADD, EDIT
    }

    @FXML private TextField eventNameField;
    @FXML private TextField descriptionField;
    @FXML private TextField locationField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> statusComboBox;

    private Mode mode;
    private Event event;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusComboBox.getItems().addAll("SCHEDULED", "CONFIRMED", "CANCELLED");
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setEvent(Event event) {
        this.event = event;
        if (event != null) {
            eventNameField.setText(event.getEventName());
            descriptionField.setText(event.getEventDescription());
            locationField.setText(event.getLocation());
            datePicker.setValue(event.getEventDate().toLocalDate());
            statusComboBox.setValue(event.getStatus());
        }
    }

    public Event getEvent() {
        if (mode == Mode.EDIT && event == null) {
            throw new IllegalStateException("Event is null in EDIT mode");
        }

        Event result = (mode == Mode.ADD) ? new Event() : event;
        result.setEventName(eventNameField.getText());
        result.setEventDescription(descriptionField.getText());
        result.setLocation(locationField.getText());
        result.setEventDate(LocalDateTime.of(datePicker.getValue(), LocalDateTime.now().toLocalTime()));
        result.setStatus(statusComboBox.getValue());

        if (mode == Mode.ADD) {
            result.setOrganizerId(1); // Default organizer ID
        }

        return result;
    }
}