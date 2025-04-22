package org.esprit.services;

import org.esprit.models.Event;
import org.esprit.models.Sponsor;
import org.esprit.utils.DataBaseLink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SponsorService implements IService<Sponsor>{
    private Connection cnx;

    public SponsorService() {
        // Initialize your database connection
        cnx = DataBaseLink.getInstance().getConnection();
    }

    @Override
    public void add(Sponsor sponsor) throws Exception {
        String query = "INSERT INTO sponsor (event_id, sponsor_name, montant, email) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, sponsor.getEvent().getId());
            pst.setString(2, sponsor.getSponsorName());
            pst.setInt(3, sponsor.getMontant());
            pst.setString(4, sponsor.getEmail());

            pst.executeUpdate();

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    sponsor.setId(generatedKeys.getInt(1));
                }
            }
        }
    }
    @Override
    public void update(Sponsor sponsor) throws Exception {
        String query = "UPDATE sponsor SET event_id=?, sponsor_name=?, montant=?, email=? WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, sponsor.getEvent().getId());
            pst.setString(2, sponsor.getSponsorName());
            pst.setInt(3, sponsor.getMontant());
            pst.setString(4, sponsor.getEmail());
            pst.setInt(5, sponsor.getId());

            pst.executeUpdate();
        }
    }

    @Override
    public void delete(Sponsor sponsor) throws Exception {
        String query = "DELETE FROM sponsor WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, sponsor.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public List<Sponsor> getAll() throws Exception {
        List<Sponsor> sponsors = new ArrayList<>();
        String query = "SELECT * FROM sponsor";
        EventService eventService = new EventService(); // Add this line
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Sponsor sponsor = new Sponsor();
                sponsor.setId(rs.getInt("id"));

                // Fetch the full event object
                Event event = eventService.getById(rs.getInt("event_id")); // Updated
                sponsor.setEvent(event);

                sponsor.setSponsorName(rs.getString("sponsor_name"));
                sponsor.setMontant(rs.getInt("montant"));
                sponsor.setEmail(rs.getString("email"));

                sponsors.add(sponsor);
            }
        }
        return sponsors;
    }

    // Additional helper method
    public Sponsor getById(int id) throws Exception {
        String query = "SELECT * FROM sponsor WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Sponsor sponsor = new Sponsor();
                    sponsor.setId(rs.getInt("id"));

                    // You'll need to fetch the event from EventService
                    // Event event = eventService.getById(rs.getInt("event_id"));
                    // sponsor.setEvent(event);

                    // Or temporarily store just the event ID
                    Event event = new Event();
                    event.setId(rs.getInt("event_id"));
                    sponsor.setEvent(event);

                    sponsor.setSponsorName(rs.getString("sponsor_name"));
                    sponsor.setMontant(rs.getInt("montant"));
                    sponsor.setEmail(rs.getString("email"));

                    return sponsor;
                }
            }
        }
        return null;
    }

    public List<Sponsor> getByEventId(int eventId) throws Exception {
        List<Sponsor> sponsors = new ArrayList<>();
        String query = "SELECT * FROM sponsor WHERE event_id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, eventId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Sponsor sponsor = new Sponsor();
                    sponsor.setId(rs.getInt("id"));

                    Event event = new Event();
                    event.setId(eventId);
                    sponsor.setEvent(event);

                    sponsor.setSponsorName(rs.getString("sponsor_name"));
                    sponsor.setMontant(rs.getInt("montant"));
                    sponsor.setEmail(rs.getString("email"));

                    sponsors.add(sponsor);
                }
            }
        }
        return sponsors;
    }
}
