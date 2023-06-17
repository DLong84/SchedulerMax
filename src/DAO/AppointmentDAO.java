package DAO;

import controller.MainFormController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import utlities.AlertUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;

/**
 * This class handles database access for the "appointments" table.
 * @author David Long
 */
public class AppointmentDAO {

    /**
     * The list of Appointment objects instantiated from all records in the "appointments" table.
     */
    private static ObservableList<Appointment> allAppts = FXCollections.observableArrayList();

    /**
     * The list of Appointment objects instantiated from records in the "appointments" table specific to a single user.
     */
    private static ObservableList<Appointment> userAppts = FXCollections.observableArrayList();

    /**
     * The list of Appointment objects instantiated from records in the "appointments" table specific to months, type,
     * and count.
     */
    private static ObservableList<Appointment> apptCounts = FXCollections.observableArrayList();

    /**
     * The select join query for pulling relevant appointment info from "appointments" and "contacts" tables.
     */
    private static final String getAllApptsQuery =
            "SELECT appt.Appointment_ID, appt.Title, appt.Description, appt.Location, appt.Type, appt.Start, appt.End,"
            + " appt.Customer_ID, appt.User_ID, ct.Contact_Name"
            + " FROM appointments AS appt"
            + " INNER JOIN contacts AS ct ON appt.Contact_ID = ct.Contact_ID";

    /**
     * The select join query for pulling a user's relevant appointment info from "appointments" and "contacts" tables.
     */
    private static final String getUserApptsQuery =
            "SELECT appt.Appointment_ID, appt.Title, appt.Description, appt.Location, appt.Type, appt.Start, appt.End,"
                    + " appt.Customer_ID, appt.User_ID, ct.Contact_Name"
                    + " FROM appointments AS appt"
                    + " INNER JOIN contacts AS ct ON appt.Contact_ID = ct.Contact_ID"
                    + " WHERE appt.User_ID = ?";

    /**
     * The select query for pulling appointment info and counts from the "appointments" table.
     */
    private static final String getApptCountsQuery = "SELECT MONTH(start) AS month, type, COUNT(*) AS appointment_count"
                    + " FROM appointments"
                    + " GROUP BY MONTH(start), type"
                    + " ORDER BY MONTH(start), type";

    /**
     * The insert statement for adding a new appointment record to the "appointments" table.
     */
    public static final String addApptStmt =
            "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * The update statement for modifying an existing appointment record in the "appointments" table.
     */
    public static final String modApptStmt =
            "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?,"
                    + " Customer_ID = ?, User_ID = ?, Contact_ID = ?"
                    + " WHERE Appointment_ID = ?";

    /**
     * The delete statement for removing an appointment record from the "appointments" table.
     */
    private static final String deleteApptStmt = "DELETE FROM appointments WHERE Appointment_ID=?";

    /**
     * This method retrieves all relevant appointment information currently in the database and instantiates a new
     * appointment object for every record that is returned from the query. All appointment objects are added to an
     * observable list, the list is then returned.
     * @return the list of appointments
     * @throws SQLException handles SQL errors
     */
    public static ObservableList<Appointment> getAllAppts() throws SQLException {

        // Clear out list of appointments
        allAppts.clear();

        PreparedStatement ps = JDBC.connection.prepareStatement(getAllApptsQuery);
        ResultSet rs = ps.executeQuery();

        // Create an appointment object for every record returned and add it to the list
        while (rs.next()) {
            Appointment appointment  = new Appointment(rs.getInt("Appointment_ID"), rs.getString("Title"),
                    rs.getString("Description"), rs.getString("Location"), rs.getString("Type"),
                    rs.getString("Contact_Name"), rs.getTimestamp("Start").toLocalDateTime(),
                    rs.getTimestamp("End").toLocalDateTime(), rs.getInt("Customer_ID"),
                    rs.getInt("User_ID"));

            allAppts.add(appointment);
        }
        return allAppts;
    }

    /**
     * This method takes in a userId object as a parameter and retrieves all relevant appointment information that is
     * connected to that user's Id currently in the database. It instantiates a new appointment object for every record
     * that is returned from the query. All appointment objects are added to an observable list, the list is then
     * returned.
     * @param userId the user's Id
     * @return the list of appointments
     * @throws SQLException handles SQL errors
     */
    public static ObservableList<Appointment> getUserAppts(Object userId) throws SQLException {

        // Clear out list of appointments
        userAppts.clear();

        PreparedStatement ps = JDBC.connection.prepareStatement(getUserApptsQuery);
        ps.setObject(1, userId); // Set user's Id into SQL query
        ResultSet rs = ps.executeQuery();

        // Create an appointment object for every record returned and add it to the list
        while (rs.next()) {
            Appointment appointment  = new Appointment(rs.getInt("Appointment_ID"), rs.getString("Title"),
                    rs.getString("Description"), rs.getString("Location"), rs.getString("Type"),
                    rs.getString("Contact_Name"), rs.getTimestamp("Start").toLocalDateTime(),
                    rs.getTimestamp("End").toLocalDateTime(), rs.getInt("Customer_ID"),
                    rs.getInt("User_ID"));

            userAppts.add(appointment);
        }
        return userAppts;
    }

    /**
     * This method retrieves appointments by month and type currently in the database, along with their counts and
     * instantiates a new appointment object for every record that is returned from the query. All appointment objects
     * are added to an observable list, the list is then returned.
     * @return the list of appointment objects
     * @throws SQLException handles SQL errors
     */
    public static ObservableList<Appointment> getApptCount() throws SQLException {

        // Clear out list of appointment counts
        apptCounts.clear();

        PreparedStatement ps = JDBC.connection.prepareStatement(getApptCountsQuery);
        ResultSet rs = ps.executeQuery();

        // Create an appointment object for every record returned and add it to the list
        while (rs.next()) {
            Appointment appointment  = new Appointment(Month.of(rs.getInt("month")), rs.getString("type"),
                    rs.getInt("appointment_count"));

            apptCounts.add(appointment);
        }
        return apptCounts;
    }

    /**
     * This method removes the selected appointment from the "appointments" table. If successful, it calls the
     * getAllAppts() method to reload updated data into the "allAppts" ObservableList and throws a "removal" alert to
     * the GUI.
     * @param selectedAppt the appointment to be removed
     * @return "true" if customer is successfully removed, otherwise "false"
     * @throws SQLException handles SQL errors
     */
    public static boolean deleteAppt (Appointment selectedAppt) throws SQLException {

        PreparedStatement ps = JDBC.connection.prepareStatement(deleteApptStmt);
        ps.setInt(1, selectedAppt.getId());

        // Execute SQL statement and return number of rows removed as a variable
        int isDeleted = ps.executeUpdate();

        // If successful
        if (isDeleted == 1) {
            getAllAppts(); // Reload the observable list with updated table data
            AlertUtils.apptCanceledAlert(MainFormController.selectedAppt);
            System.out.println("Appointment with ID: " + selectedAppt.getId() + " deleted");
            return true;
        }
        else {
            System.out.println("Something went wrong!");
            return false;
        }
    }
}