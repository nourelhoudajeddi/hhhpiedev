package org.esprit.models;

import javafx.beans.property.*;

public class Attendee {
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final ObjectProperty<Event> event = new SimpleObjectProperty<>(this, "event");
    private final StringProperty name = new SimpleStringProperty(this, "name");
    private final StringProperty phoneNumber = new SimpleStringProperty(this, "phoneNumber");
    private final StringProperty email = new SimpleStringProperty(this, "email");

    // Default constructor
    public Attendee() {}

    // Constructor without ID
    public Attendee(Event event, String name, String phoneNumber, String email) {
        setEvent(event);
        setName(name);
        setPhoneNumber(phoneNumber);
        setEmail(email);
    }

    // Full constructor
    public Attendee(int id, Event event, String name, String phoneNumber, String email) {
        setId(id);
        setEvent(event);
        setName(name);
        setPhoneNumber(phoneNumber);
        setEmail(email);
    }

    // Properties for JavaFX binding
    public IntegerProperty idProperty() {
        return id;
    }

    public ObjectProperty<Event> eventProperty() {
        return event;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public StringProperty emailProperty() {
        return email;
    }

    // Standard Getters and Setters
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public Event getEvent() {
        return event.get();
    }

    public void setEvent(Event event) {
        this.event.set(event);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    @Override
    public String toString() {
        return "Attendee{" +
                "id=" + getId() +
                ", event=" + (getEvent() != null ? getEvent().getId() : "null") +
                ", name='" + getName() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }
}
