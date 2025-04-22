package org.esprit.models;

import javafx.beans.property.*;

public class Sponsor {
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final ObjectProperty<Event> event = new SimpleObjectProperty<>(this, "event");
    private final StringProperty sponsorName = new SimpleStringProperty(this, "sponsorName");
    private final IntegerProperty montant = new SimpleIntegerProperty(this, "montant");
    private final StringProperty email = new SimpleStringProperty(this, "email");

    // Default constructor
    public Sponsor() {
    }    // Constructor without id
    public Sponsor(Event event, String sponsorName, int montant, String email) {
        setEvent(event);
        setSponsorName(sponsorName);
        setMontant(montant);
        setEmail(email);
    }

    // Full constructor
    public Sponsor(int id, Event event, String sponsorName, int montant, String email) {
        setId(id);
        setEvent(event);
        setSponsorName(sponsorName);
        setMontant(montant);
        setEmail(email);
    }

    // Property getters
    public IntegerProperty idProperty() {
        return id;
    }

    public ObjectProperty<Event> eventProperty() {
        return event;
    }

    public StringProperty sponsorNameProperty() {
        return sponsorName;
    }

    public IntegerProperty montantProperty() {
        return montant;
    }

    public StringProperty emailProperty() {
        return email;
    }

    // Getters and Setters
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

    public String getSponsorName() {
        return sponsorName.get();
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName.set(sponsorName);
    }

    public int getMontant() {
        return montant.get();
    }

    public void setMontant(int montant) {
        this.montant.set(montant);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    @Override
    public String toString() {
        return "Sponsor{" +
                "id=" + getId() +
                ", event=" + (getEvent() != null ? getEvent().getId() : "null") +
                ", sponsorName='" + getSponsorName() + '\'' +
                ", montant=" + getMontant() +
                ", email='" + getEmail() + '\'' +
                '}';
    }
}