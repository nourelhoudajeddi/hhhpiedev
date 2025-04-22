package org.esprit.main;

import org.esprit.models.Event;
import org.esprit.models.Sponsor;
import org.esprit.models.Attendee;
import org.esprit.services.EventService;
import org.esprit.services.SponsorService;
import org.esprit.services.AttendeeService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TestCrudSec {

    private static final int EXISTING_ORGANIZER_ID = 1;

    public static void main(String[] args) {
        try {
            EventService eventService = new EventService();

            // Verify the organizer exists first
            if (!organizerExists(eventService, EXISTING_ORGANIZER_ID)) {
                System.err.println("Organizer with ID " + EXISTING_ORGANIZER_ID + " doesn't exist!");
                return;
            }

            // 1. Create event with specific date/time
            LocalDate eventDate = LocalDate.of(2024, 12, 15);
            LocalTime eventTime = LocalTime.of(14, 30); // 2:30 PM
            Event newEvent = createEvent(eventService, eventDate, eventTime);

            // 2. Test other operations
            testEventOperations(eventService, newEvent.getId());

        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean organizerExists(EventService service, int organizerId) throws Exception {
        // This assumes you have a way to check if user exists
        // You might need to implement this in your UserService
        return true; // Replace with actual check
    }

    private static Event createEvent(EventService service, LocalDate date, LocalTime time)
            throws Exception {
        System.out.println("Creating event...");
        LocalDateTime eventDateTime = LocalDateTime.of(date, time);

        Event newEvent = new Event(
                EXISTING_ORGANIZER_ID, // Make sure this is set correctly
                "JavaFX Maste55rclass",
                "Adva5nced JavaFX techniques",
                eventDateTime,
                "Confer5ence Room A",
                "SCHEDULED"
        );

        service.add(newEvent);
        System.out.println("Created: " + newEvent);
        return newEvent;
    }

    private static void testEventOperations(EventService service, int eventId)
            throws Exception {
        // Read
        Event fetched = service.getById(eventId);
        System.out.println("\nFetched event: " + fetched);

        // Update with new date/time
        LocalDateTime newDateTime = fetched.getEventDate()
                .plusDays(1)
                .withHour(10)
                .withMinute(0);

        Event updatedEvent = new Event(
                fetched.getId(),
                EXISTING_ORGANIZER_ID, // Make sure organizer ID is set here too
                "UPDATED: " + fetched.getEventName(),
                fetched.getEventDescription(),
                newDateTime,
                "Conference Room B",
                "CONFIRMED"
        );

        service.update(updatedEvent);
        System.out.println("Updated to: " + updatedEvent);

        // Delete
        service.delete(updatedEvent);
        System.out.println("Event deleted");
    }
    private static void testAttendeeOperations(AttendeeService service, Event event)
            throws Exception {
        System.out.println("\n=== Testing Attendee Operations ===");

        // 1. Create attendee
        Attendee newAttendee = createAttendee(service, event);

        // 2. Test other operations
        testAttendeeActions(service, newAttendee.getId(), event);
    }

    private static Attendee createAttendee(AttendeeService service, Event event)
            throws Exception {
        System.out.println("Creating attendee...");

        Attendee newAttendee = new Attendee();
        newAttendee.setEvent(event);
        newAttendee.setName("John Smith");
        newAttendee.setPhoneNumber("123-456-7890");
        newAttendee.setEmail("john.smith@example.com");

        service.add(newAttendee);
        System.out.println("Created attendee: ID=" + newAttendee.getId() + ", Name=" + newAttendee.getName());
        return newAttendee;
    }

    private static void testAttendeeActions(AttendeeService service, int attendeeId, Event event)
            throws Exception {
        // Read
        Attendee fetched = service.getById(attendeeId);
        System.out.println("\nFetched attendee: " + fetched.getName());

        // Update
        Attendee updatedAttendee = new Attendee();
        updatedAttendee.setId(attendeeId);
        updatedAttendee.setEvent(event);
        updatedAttendee.setName("UPDATED: " + fetched.getName());
        updatedAttendee.setPhoneNumber("987-654-3210");
        updatedAttendee.setEmail(fetched.getEmail());

        service.update(updatedAttendee);
        System.out.println("Updated to: " + updatedAttendee.getName());

        // Verify update
        Attendee verifiedUpdate = service.getById(attendeeId);
        System.out.println("Verified update: " + verifiedUpdate.getName());
    }

}