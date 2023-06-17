package model;

import java.time.LocalDateTime;
import java.time.Month;

/**
 * This class represents an Appointment.
 * @author David Long
 */
public class Appointment {

    // Attributes
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private String contact;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerId;
    private int userId;
    private int count;
    private Month month;

    /**
     * This is the Appointment constructor.
     * @param id the appointment's ID
     * @param title the appointment's title
     * @param description the appointment's description
     * @param location the appointment's location
     * @param type the appointment's type
     * @param contact the appointment's contact
     * @param start the appointment's start date and time
     * @param end the appointment's end date and time
     * @param customerId the appointment's Customer ID
     * @param userId the appointment's User ID
     */
    public Appointment(int id, String title, String description, String location, String type, String contact,
                       LocalDateTime start, LocalDateTime end, int customerId, int userId)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.contact = contact;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
    }

    /**
     * This is the Appointment constructor used for special Appointment objects displayed in a report.
     * @param month the appointment's month
     * @param type the appointment's type
     * @param count the appointment's count
     */
    public Appointment(Month month, String type, int count) {
        this.month = month;
        this.type = type;
        this.count = count;
    }

    // Setters for Appointment attributes
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    // Getters for Appointment attributes
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public String getContact() {
        return contact;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getUserId() {
        return userId;
    }

    public int getCount() {
        return count;
    }

    public Month getMonth() {
        return month;
    }
}