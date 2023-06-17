package controller;

import DAO.AppointmentDAO;
import DAO.JDBC;
import Interfaces.AppointmentInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Appointment;
import utlities.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class is used to manage the flow of data and view of the application's Login GUI form.
 * @author David Long
 */
public class LoginController implements Initializable {

    // Elements of GUI form
    @FXML
    private Button loginBtn;
    @FXML
    private Button loginCancelBtn;
    @FXML
    private AnchorPane loginFormAll;
    @FXML
    private Label loginLbl;
    @FXML
    private PasswordField loginPassFld;
    @FXML
    private Label loginPassLbl;
    @FXML
    private Label loginTimeZoneLbl;
    @FXML
    private TextField loginUserFld;
    @FXML
    private Label loginUserLbl;

    /**
     * Object used to pass the value of the current user's Id
     */
    public static Object currentUserId = null;

    /**
     * This method loads the current form's GUI elements and obtains the current default locale accordingly.
     * @param url the location of the controller's .fxml file
     * @param rb_languages the locale-specific resources for the controller's objects
     */
    @Override
    public void initialize (URL url, ResourceBundle rb_languages) {

        // Load form with the properties file of the corresponding default locale's language
        ResourceBundle.getBundle("languages.loginRB", Locale.getDefault());

        // Set displayed timezone
        loginTimeZoneLbl.setText(TimeUtils.getTimezone());
    }

    /**
     * Lambda expression used for checking existing upcoming appointments, specifically for a user, that start within 15
     * minutes or less from the time the expression is called. It first instantiates a boolean variable "upcomingAppt"
     * to false, which is used for keeping track of whether the user has any upcoming appointments. It then checks (by
     * the user's Id that was taken in as an argument) for the user's existing appointments. If the user has any
     * appointments, the list is iterated over to check for an appointment starting time within the 15-minute window from
     * the current time. If one is found, the details of that appointment are placed into the dialog box alert method to
     * inform the user of an upcoming appointment and the "upcomingAppt" variable is set to true. If the variable is
     * false (the list of appointments is empty or no appointments were within 15 minutes), it calls the dialog box
     * alert method to inform the user of no upcoming appointments. A lambda expression was chosen here for clarity and
     * conciseness.
     */
    AppointmentInterface upcoming = (userId) -> {

        // Variable for keeping track of upcoming appointments
        boolean upcomingAppt = false;

        // If user has appointments
        if (!AppointmentDAO.getUserAppts(userId).isEmpty()) {
            for (Appointment appt : AppointmentDAO.getUserAppts(userId)) {
                if (appt.getStart().isAfter(LocalDateTime.now()) &&          // If appointment is after current time AND
                        appt.getStart().isBefore(LocalDateTime.now().plusMinutes(15)) ) {   // Earlier than 15 minutes
                    AlertUtils.upcomingApptAlert(appt); // Dialog box for upcoming appointment info
                    upcomingAppt = true;
                }
            }
        }
        // If no appointments or none are within 15 minutes
        if (!upcomingAppt) {
            AlertUtils.noUpcomingApptAlert(); // Dialog box for no upcoming appointments
        }
    };

    /**
     * Upon validation for non-blank text-fields, this method checks the user's login credentials with "getUserId"
     * method for existence in the database. Upon credentials validation, it assigns the user's Id to a variable and
     * the attempt is logged. It then calls the "toMainForm" method and checks for the user's upcoming appointments with
     * use of a lambda expression. If the user credentials are invalid, the unsuccessful attempt is logged and an alert
     * is displayed to the user.
     * @param actionEvent "Login" button click
     * @throws SQLException handles SQL errors
     * @throws IOException thrown by FXMLLoader.load() if the .fxml file URL is not input correctly
     */
    @FXML
    void onActionLogin(ActionEvent actionEvent) throws SQLException, IOException {

        // Check for empty text fields
        if (ValidationUtils.loginIsEmpty(loginUserFld, AlertUtils.rb_languages.getString("Username"))) {
            return;
        }
        if (ValidationUtils.loginIsEmpty(loginPassFld, AlertUtils.rb_languages.getString("Password"))) {
            return;
        }

        // Assign the user's ID object to the static variable
        currentUserId = ValidationUtils.getUserId(loginUserFld.getText(), loginPassFld.getText());

        // Login credentials check
        if (currentUserId == null) { // Invalid credentials
            LoggingUtils.failToFile(loginUserFld.getText()); // Log unsuccessful attempt
            AlertUtils.popCredentialsAlert(); // Invalid credentials dialog box
        }
        else {
            System.out.println("User with ID: " + currentUserId + " validated");
            LoggingUtils.successToFile(loginUserFld.getText()); // Log successful attempt
            SceneUtils.toMainForm(loginBtn);

            // Lambda expression call to check for user's upcoming appointments
            try {
                upcoming.apptCheck(LoginController.currentUserId);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This method throws a confirmation dialog box and, if confirmed, closes database connection and exits the
     * application
     * @param actionEvent "Exit" button click
     */
    @FXML
    void onActionLoginExit(ActionEvent actionEvent) {

        if (AlertUtils.loginExitAlert() == true) {
            JDBC.closeConnection(); // Close the connection
            System.exit(0);
        }
    }
}