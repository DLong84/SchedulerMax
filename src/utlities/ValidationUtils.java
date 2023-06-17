package utlities;

import DAO.AppointmentDAO;
import DAO.JDBC;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Appointment;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * This class contains methods to handle validations for the application's GUI.
 * @author David Long
 */
public class ValidationUtils {

    /**
     * This method takes in a TextField and its label from the Login form and checks to see if the TextField is empty.
     * If the field is empty, the "loginBlankAlert" method is called, then the method returns true. Otherwise, it
     * returns false.
     * @param fieldId the fxId of the TextField being checked
     * @param labelTxt the TextField's label
     * @return "true" if the TextField is empty, otherwise "false"
     */
    public static boolean loginIsEmpty(TextField fieldId, String labelTxt) {

        String field = fieldId.getText(); // Get user input

        if (field.isBlank()) {
            AlertUtils.loginBlankAlert(labelTxt);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * This method takes in a PasswordField and its label from the Login form and checks to see if the TextField is
     * empty. If the field is empty, the "loginBlankAlert" method is called, then the method returns true. Otherwise, it
     * returns false.
     * @param fieldId the fxId of the PasswordField being checked
     * @param labelTxt the PasswordField's label
     * @return "true" if the PasswordField is empty, otherwise "false"
     */
    public static boolean loginIsEmpty(PasswordField fieldId, String labelTxt) {

        String field = fieldId.getText(); // Get user input

        if (field.isBlank()) {
            AlertUtils.loginBlankAlert(labelTxt);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * This method takes in a TextField and its label and checks to see if the TextField is empty. If the field is empty,
     * the "blankFldAlert" method is called, then the method returns true. Otherwise, it returns false.
     * @param fieldId the fxId of the TextField being checked
     * @param labelTxt the TextField's label
     * @return "true" if the TextField is empty, otherwise "false"
     */
    public static boolean fldIsEmpty(TextField fieldId, String labelTxt) {

        String field = fieldId.getText(); // Get user input

        if (field.isBlank()) {
            AlertUtils.blankFldAlert(labelTxt);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * This method takes in a ComboBox and its label and checks to see if the ComboBox has a selection made. If the
     * selection is null, the "noSelectionAlert" method is called and the method returns true. Otherwise, it returns false.
     * @param boxId the fxId of the ComboBox being checked
     * @param labelTxt the ComboBox's label
     * @return "true" if the ComboBox selection is null, otherwise "false"
     */
    public static boolean boxNotSelected(ComboBox boxId, String labelTxt) {

        Object field = boxId.getValue(); // Get user selection

        if (field == null) {
            AlertUtils.noSelectionAlert(labelTxt);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * This method takes in a DatePicker and its label and checks to see if the DatePicker has a selection made. If the
     * selection is null, the "noSelectionAlert" method is called and the method returns true. Otherwise, it returns false.
     * @param pickerId the fxId of the DatePicker being checked
     * @param labelTxt the DatePicker's label
     * @return "true" if the DatePicker selection is null, otherwise "false"
     */
    public static boolean dateNotSelected(DatePicker pickerId, String labelTxt) {

        Object field = pickerId.getValue(); // Get user selection

        if (field == null) {
            AlertUtils.noSelectionAlert(labelTxt);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * This method takes in a username and password, then executes a query which returns a matching ID object from the
     * USERS table. If there is no user with same username and password, it returns a null object.
     * @param userName The user's username
     * @param passWord The user's password
     * @return If it exists, the user's ID, otherwise null.
     * @throws SQLException handles SQL errors
     */
    public static Object getUserId (String userName, String passWord) throws SQLException {

        // Select query for matching username and password to user's Id
        String query = "SELECT User_ID FROM USERS WHERE User_Name=? AND Password=?";

        PreparedStatement ps = JDBC.connection.prepareStatement(query);
        ps.setString(1, userName);
        ps.setString(2, passWord);

        ResultSet result = ps.executeQuery();

        Object userId = null;

        if (result.next()) { // If such user exists
            userId = result.getObject(1);
        }
        return userId;
    }

    /**
     * This method takes in a customer and checks the list of all appointments in the database to see if there are any
     * appointments with the customer's ID. If there is a matching appointment, the method returns true. Otherwise, it
     * returns false.
     * @param selectedCustomer the customer to be checked for matching appointments
     * @return "true" if there is an appointment containing a customer ID matching the customer's ID, otherwise "false
     * @throws SQLException handles SQL errors
     */
    public static boolean customerHasAppts(Customer selectedCustomer) throws SQLException {

        for (Appointment appt : AppointmentDAO.getAllAppts()) {
            if (selectedCustomer.getId() == appt.getCustomerId()) {  // If appointment is found
                return true;
            }
        }
        return false;
    }

    /**
     * This method compares the appointment starting time to the appointment ending time for validation. If the appointment
     * ending time is before or equal to the appointment starting time, the "apptTimesAlert" method is called and
     * the method returns true. Otherwise, it returns false.
     * @param apptStart the appointment starting time
     * @param apptEnd the appointment ending time
     * @return "true" if end time is before or equal to start time, otherwise "false"
     */
    public static boolean endIsBeforeStart(LocalTime apptStart, LocalTime apptEnd) {

        if (apptEnd.isBefore(apptStart) || apptEnd.equals(apptStart)) {
            AlertUtils.apptTimesAlert(); // Alert dialog box
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * This method compares the appointment starting time to the start of business hours and the appointment ending time
     * to the end of business hours. If the proposed start is before 8am or the proposed end is after 10pm, the
     * "businessHoursAlert" method is called and the method returns true. Otherwise, it returns false.
     * @param apptStart the appointment starting time
     * @param apptEnd the appointment ending time
     * @return "true" if start is before 8am or end is after 10pm, otherwise "false"
     */
    public static boolean outsideBussHours(LocalTime apptStart, LocalTime apptEnd) {

        if (apptStart.isBefore(LocalTime.of(8, 00)) || apptEnd.isAfter(LocalTime.of(22, 00))) {
            AlertUtils.businessHoursAlert(); // Alert dialog box
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * This method takes in the customer Id from the proposed appointment and compares that customer's existing
     * appointment times for overlap with the proposed appointment start/end time period by means of 3 possible scenarios.
     * If any of the 3 possible overlap scenarios is true, the "overlapAlert" method is called and the method returns
     * true. Otherwise, it returns false.
     * @param apptCustomerId the proposed appointment's customer Id
     * @param start the proposed appointment's starting time
     * @param end the proposed appointment's ending time
     * @return "true" if the proposed appointment time period overlaps any of the scenarios, otherwise "false"
     * @throws SQLException handles SQL errors
     */
    public static boolean custApptOverlaps(int apptCustomerId, LocalDateTime start, LocalDateTime end)
            throws SQLException {

        for (Appointment appt : AppointmentDAO.getAllAppts()) {
            if (appt.getCustomerId() == apptCustomerId) {   // Compare appointment times
                if (((start.isEqual(appt.getStart()) || start.isAfter(appt.getStart())) && start.isBefore(appt.getEnd())) ||
                        (end.isEqual(appt.getEnd()) || end.isBefore(appt.getEnd()) && (end.isAfter(appt.getStart()))) ||
                        ((end.isAfter(appt.getEnd()) || end.isEqual(appt.getEnd())) && (start.isBefore(appt.getStart())
                        || start.isEqual(appt.getStart())))) {
                    AlertUtils.overlapAlert(); // Alert dialog box
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method takes in the Id from the appointment being updated and customer Id from the proposed appointment. The
     * appointment being updated is ignored for overlap with itself, then the customer's existing appointment times are
     * compared with the proposed appointment start/end time period for overlap by means of 3 possible scenarios. If any
     * of the 3 possible overlap scenarios is true, the "overlapAlert" method is called and the method returns true.
     * Otherwise, it returns false.
     * @param apptId the Id of the appointment being updated
     * @param apptCustomerId the proposed appointment's customer Id
     * @param start the proposed appointment's starting time
     * @param end the proposed appointment's ending time
     * @return "true" if the proposed appointment time period overlaps any of the scenarios, otherwise "false"
     * @throws SQLException handles SQL errors
     */
    public static boolean custApptOverlaps(int apptId, int apptCustomerId, LocalDateTime start, LocalDateTime end)
            throws SQLException {

        for (Appointment appt : AppointmentDAO.getAllAppts()) {
            if (appt.getId() == apptId) {  // If appointment already exists, skip it
                continue;
            }
            if (appt.getCustomerId() == apptCustomerId) {   // Compare appointment times
                if (((start.isEqual(appt.getStart()) || start.isAfter(appt.getStart())) && start.isBefore(appt.getEnd())) ||
                        (end.isEqual(appt.getEnd()) || end.isBefore(appt.getEnd()) && (end.isAfter(appt.getStart()))) ||
                        ((end.isAfter(appt.getEnd()) || end.isEqual(appt.getEnd())) && (start.isBefore(appt.getStart())
                                || start.isEqual(appt.getStart())))) {
                    AlertUtils.overlapAlert(); // Alert dialog box
                    return true;
                }
            }
        }
        return false;
    }
}