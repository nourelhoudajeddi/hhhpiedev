package org.esprit.services;

import org.esprit.models.Event;
import org.esprit.utils.DataBaseLink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService implements IService<Event> {
    private Connection cnx;

    public EventService() {
        // Initialize your database connection
        cnx = DataBaseLink.getInstance().getConnection();
    }    @Override
    public void add(Event event) throws Exception {
        String query = "INSERT INTO event (organizer_id, event_name, event_description, event_date, event_location, event_status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, event.getOrganizerId());
            pst.setString(2, event.getEventName());
            pst.setString(3, event.getEventDescription());
            pst.setTimestamp(4, Timestamp.valueOf(event.getEventDate()));
            pst.setString(5, event.getLocation());
            pst.setString(6, event.getStatus());

            pst.executeUpdate();

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    event.setId(generatedKeys.getInt(1));
                }
            }
        }
    }    @Override
    public void update(Event event) throws Exception {
        String query = "UPDATE event SET organizer_id=?, event_name=?, event_description=?, event_date=?, " +
                "event_location=?, event_status=? WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, event.getOrganizerId());
            pst.setString(2, event.getEventName());
            pst.setString(3, event.getEventDescription());
            pst.setTimestamp(4, Timestamp.valueOf(event.getEventDate()));
            pst.setString(5, event.getLocation());
            pst.setString(6, event.getStatus());
            pst.setInt(7, event.getId());

            pst.executeUpdate();
        }
    }

    @Override
    public void delete(Event event) throws Exception {
        String query = "DELETE FROM event WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, event.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public List<Event> getAll() throws Exception {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM event";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Event event = new Event();                event.setId(rs.getInt("id"));
                // You'll need to fetch the organizer from UserService
                event.setOrganizerId(rs.getInt("organizer_id"));
                event.setEventName(rs.getString("event_name"));
                event.setEventDescription(rs.getString("event_description"));
                event.setEventDate(rs.getTimestamp("event_date").toLocalDateTime());
                event.setLocation(rs.getString("event_location"));
                event.setStatus(rs.getString("event_status"));

                events.add(event);
            }
        }
        return events;
    }

    // Additional helper methods
    public Event getById(int id) throws Exception {
        String query = "SELECT * FROM event WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Event event = new Event();
                    event.setId(rs.getInt("id"));                event.setOrganizerId(rs.getInt("organizer_id"));  // Set organizer ID
                event.setEventName(rs.getString("event_name"));
                event.setEventDescription(rs.getString("event_description"));
                event.setEventDate(rs.getTimestamp("event_date").toLocalDateTime());
                event.setLocation(rs.getString("event_location"));
                event.setStatus(rs.getString("event_status"));
                    return event;
                }
            }
        }
        return null;
    }
}