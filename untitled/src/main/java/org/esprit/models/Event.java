package org.esprit.models;

import java.time.LocalDateTime;
import javafx.beans.property.*;

public class Event {
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final IntegerProperty organizer = new SimpleIntegerProperty(this, "organizer");
    private final StringProperty eventName = new SimpleStringProperty(this, "eventName");
    private final StringProperty eventDescription = new SimpleStringProperty(this, "eventDescription");
    private final ObjectProperty<LocalDateTime> eventDate = new SimpleObjectProperty<>(this, "eventDate");
    private final StringProperty location = new SimpleStringProperty(this, "location");
    private final StringProperty status = new SimpleStringProperty(this, "status");

    // Default constructor
    public Event() {
    }

    // Constructor without id
    public Event(int organizer, String eventName, String eventDescription,
                 LocalDateTime eventDate, String location, String status) {
        setOrganizerId(organizer);
        setEventName(eventName);
        setEventDescription(eventDescription);
        setEventDate(eventDate);
        setLocation(location);
        setStatus(status);
    }

    // Full constructor
    public Event(int id, int organizer, String eventName, String eventDescription,
                 LocalDateTime eventDate, String location, String status) {
        setId(id);
        setOrganizerId(organizer);
        setEventName(eventName);
        setEventDescription(eventDescription);
        setEventDate(eventDate);
        setLocation(location);
        setStatus(status);
    }

    // Property getters
    public IntegerProperty idProperty() {
        return id;
    }

    public IntegerProperty organizerProperty() {
        return organizer;
    }

    public StringProperty eventNameProperty() {
        return eventName;
    }

    public StringProperty eventDescriptionProperty() {
        return eventDescription;
    }

    public ObjectProperty<LocalDateTime> eventDateProperty() {
        return eventDate;
    }

    public StringProperty locationProperty() {
        return location;
    }

    public StringProperty statusProperty() {
        return status;
    }

    // Getters and Setters
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getOrganizerId() {
        return organizer.get();
    }

    public void setOrganizerId(int organizer) {
        this.organizer.set(organizer);
    }

    public String getEventName() {
        return eventName.get();
    }

    public void setEventName(String eventName) {
        this.eventName.set(eventName);
    }

    public String getEventDescription() {
        return eventDescription.get();
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription.set(eventDescription);
    }

    public LocalDateTime getEventDate() {
        return eventDate.get();
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate.set(eventDate);
    }

    public String getLocation() {
        return location.get();
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + getId() +
                ", organizer=" + getOrganizerId() +
                ", eventName='" + getEventName() + '\'' +
                ", eventDescription='" + getEventDescription() + '\'' +
                ", eventDate=" + getEventDate() +
                ", location='" + getLocation() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }
}