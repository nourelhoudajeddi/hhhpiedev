package org.esprit.services;

import org.esprit.models.Attendee;
import org.esprit.models.Event;
import org.esprit.utils.DataBaseLink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendeeService implements IService<Attendee>{

    private Connection cnx;

    public AttendeeService() {
        // Initialize your database connection
        cnx = DataBaseLink.getInstance().getConnection();
    }
    @Override
    public void add(Attendee attendee) throws Exception {
        String query = "INSERT INTO attendee (event_id, name, phone_number, email) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, attendee.getEvent().getId());
            pst.setString(2, attendee.getName());
            pst.setString(3, attendee.getPhoneNumber());
            pst.setString(4, attendee.getEmail());

            pst.executeUpdate();

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    attendee.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Attendee attendee) throws Exception {
        String query = "UPDATE attendee SET event_id=?, name=?, phone_number=?, email=? WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, attendee.getEvent().getId());
            pst.setString(2, attendee.getName());
            pst.setString(3, attendee.getPhoneNumber());
            pst.setString(4, attendee.getEmail());
            pst.setInt(5, attendee.getId());

            pst.executeUpdate();
        }
    }

    @Override
    public void delete(Attendee attendee) throws Exception {
        String query = "DELETE FROM attendee WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, attendee.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public List<Attendee> getAll() throws Exception {
        List<Attendee> attendees = new ArrayList<>();
        String query = "SELECT * FROM attendee";
        EventService eventService = new EventService(); // Add this line
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Attendee attendee = new Attendee();
                attendee.setId(rs.getInt("id"));

                // Fetch the full event object
                Event event = eventService.getById(rs.getInt("event_id")); // Updated
                attendee.setEvent(event);

                attendee.setName(rs.getString("name"));
                attendee.setPhoneNumber(rs.getString("phone_number"));
                attendee.setEmail(rs.getString("email"));

                attendees.add(attendee);
            }
        }
        return attendees;
    }

    // Additional helper method
    public Attendee getById(int id) throws Exception {
        String query = "SELECT * FROM attendee WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Attendee attendee = new Attendee();
                    attendee.setId(rs.getInt("id"));

                    // You'll need to fetch the event from EventService
                    // Event event = eventService.getById(rs.getInt("event_id"));
                    // attendee.setEvent(event);

                    // Or temporarily store just the event ID
                    Event event = new Event();
                    event.setId(rs.getInt("event_id"));
                    attendee.setEvent(event);

                    attendee.setName(rs.getString("name"));
                    attendee.setPhoneNumber(rs.getString("phone_number"));
                    attendee.setEmail(rs.getString("email"));

                    return attendee;
                }
            }
        }
        return null;
    }

    public List<Attendee> getByEventId(int eventId) throws Exception {
        List<Attendee> attendees = new ArrayList<>();
        String query = "SELECT * FROM attendee WHERE event_id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, eventId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Attendee attendee = new Attendee();
                    attendee.setId(rs.getInt("id"));

                    Event event = new Event();
                    event.setId(eventId);
                    attendee.setEvent(event);

                    attendee.setName(rs.getString("name"));
                    attendee.setPhoneNumber(rs.getString("phone_number"));
                    attendee.setEmail(rs.getString("email"));

                    attendees.add(attendee);
                }
            }
        }
        return attendees;
    }
}
