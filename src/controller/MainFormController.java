package controller;

import DAO.AppointmentDAO;
import DAO.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Customer;
import utlities.AlertUtils;
import utlities.SceneUtils;
import utlities.ValidationUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ResourceBundle;

/**
 * This class is used to manage the flow of data and view of the application's main GUI form.
 * @author David Long
 */
public class MainFormController  implements Initializable {

    // Elements of GUI form
    @FXML
    private Button addApptBtn;
    @FXML
    private Button addCustomerBtn;
    @FXML
    private RadioButton allApptsRBtn;
    @FXML
    private TableColumn<Appointment, String> apptContactCol;
    @FXML
    private TableColumn<Appointment, Integer> apptCustIdCol;
    @FXML
    private TableColumn<Appointment, String> apptDescripCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> apptEndDTCol;
    @FXML
    private ToggleGroup apptFilterTG;
    @FXML
    private TableColumn<Appointment, Integer> apptIdCol;
    @FXML
    private TableColumn<Appointment, String> apptLocationCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> apptStartDTCol;
    @FXML
    private TableColumn<Appointment, String> apptTitleCol;
    @FXML
    private TableColumn<Appointment, String> apptTypeCol;
    @FXML
    private TableColumn<Appointment, Integer> apptUserIdCol;
    @FXML
    private TableColumn<Customer, String> custAddressCol;
    @FXML
    private TableColumn<Customer, Integer> custIdCol;
    @FXML
    private TableColumn<Customer, String> custNameCol;
    @FXML
    private TableColumn<Customer, String> custPhoneCol;
    @FXML
    private TableColumn<Customer, String> custPostalCol;
    @FXML
    private TableColumn<Customer, String> custStateProvCol;
    @FXML
    private Button deleteAppt;
    @FXML
    private Button deleteCustomer;
    @FXML
    private Button logoutBtn;
    @FXML
    private TableView<Appointment> mainApptsTable;
    @FXML
    private TableView<Customer> mainCustomersTable;
    @FXML
    private Button mainReportsBtn;
    @FXML
    private RadioButton monthViewRBtn;
    @FXML
    private Button updateApptBtn;
    @FXML
    private Button updateCustomerBtn;
    @FXML
    private RadioButton weekViewRBtn;

    /**
     * Selected customer object from customers tableview.
     */
    public static Customer selectedCustomer;

    /**
     * Selected appointment object from appointments tableview.
     */
    public static Appointment selectedAppt;

    /**
     * This method loads the current form's GUI elements and sets the form's tableviews with customer and appointment
     * data from the database.
     * @param url The location of the controller's .fxml file
     * @param resourceBundle The locale-specific resources for the controller's objects
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set tableview with all customers currently in database
        try {
            mainCustomersTable.setItems(CustomerDAO.getAllCustomers());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        custStateProvCol.setCellValueFactory(new PropertyValueFactory<>("state"));
        custPostalCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Set tableview with all appointments currently in database
        try {
            mainApptsTable.setItems(AppointmentDAO.getAllAppts());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescripCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptContactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        apptStartDTCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        apptEndDTCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        apptCustIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        apptUserIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        System.out.println("Main Form initialized");


    }

    /**
     * This method moves the current scene to the Customer form.
     * @param event customers "Add" button click
     * @throws IOException thrown by FXMLLoader.load() if the .fxml file URL is not input correctly
     */
    @FXML
    void onActionAddCustomer(ActionEvent event) throws IOException {
        SceneUtils.toCustomerForm(addCustomerBtn);
    }

    /**
     * This method identifies the selected customer, sets the customer modification tracking variable to "true", then
     * moves the current scene to the Customer form.
     * @param event customers "Update" button click
     * @throws IOException thrown by FXMLLoader.load() if the .fxml file URL is not input correctly
     */
    @FXML
    void onActionUpdateCustomer(ActionEvent event) throws IOException {

        try {
            // Assign currently selected customer to "selectedCustomer" object
            selectedCustomer = mainCustomersTable.getSelectionModel().getSelectedItem();

            // Throw exception if no customer is selected
            if (selectedCustomer == null) {
                throw new NullPointerException();
            }

            // Set modification tracking boolean to true
            CustomerController.modifyCustomer = true;

            // Move scene to Customer form
            SceneUtils.toCustomerForm(updateCustomerBtn);
        }
        catch (NullPointerException e) {
            AlertUtils.noSelectionAlert("Customer", "update");
        }
    }

    /**
     * This method identifies the selected customer from the customers tableview and performs a check for remaining
     * appointments. If there are no appointments related to the customer, the customer is removed from the database upon
     * user confirmation.
     * @param event customers "Delete" button click
     * @throws SQLException handles SQL errors
     */
    @FXML
    void onActionDeleteCustomer(ActionEvent event) throws SQLException {

        try {
            // Assign currently selected customer to "selectedCustomer" object
            selectedCustomer = mainCustomersTable.getSelectionModel().getSelectedItem();

            // Throw exception if no customer is selected
            if (selectedCustomer == null) {
                throw new NullPointerException();
            }
            // Check for remaining appointments
            if (ValidationUtils.customerHasAppts(selectedCustomer)) {
                AlertUtils.remainingApptsAlert(selectedCustomer);
                MainFormController.selectedCustomer = null; // Reset selected customer, no longer needed
            }
            else{
                // Customer removal confirmation
                if (AlertUtils.deleteWarningYes("customer")) {
                    // Remove the customer
                    CustomerDAO.deleteCustomer(selectedCustomer);
                    MainFormController.selectedCustomer = null; // Reset selected customer, no longer needed
                }
            }
        }
        catch (NullPointerException e) {
            AlertUtils.noSelectionAlert("Customer", "delete");
        }
    }

    /**
     * This method moves the current scene to the Appointment form.
     * @param event appointments "Add" button click
     * @throws IOException thrown by FXMLLoader.load() if the .fxml file URL is not input correctly
     */
    @FXML
    void onActionAddAppt(ActionEvent event) throws IOException {
        SceneUtils.toApptForm(addApptBtn);
    }

    /**
     * This method identifies the selected appointment, sets the appointment modification tracking variable to "true",
     * then moves the current scene to the Appointment form.
     * @param event appointments "Update" button click
     * @throws IOException thrown by FXMLLoader.load() if the .fxml file URL is not input correctly
     */
    @FXML
    void onActionUpdateAppt(ActionEvent event) throws IOException {

        try {
            // Assign currently selected appointment to "selectedAppt" object
            selectedAppt = mainApptsTable.getSelectionModel().getSelectedItem();

            // Throw exception if no appointment is selected
            if (selectedAppt == null) {
                throw new NullPointerException();
            }

            // Set modification tracking boolean to true
            ApptController.modifyAppt = true;

            // Move scene to Customer form
            SceneUtils.toApptForm(updateApptBtn);
        }
        catch (NullPointerException e) {
            AlertUtils.noSelectionAlert("Appointment", "update");
        }
    }

    /**
     * This method identifies the selected appointment from the appointments tableview and, upon user confirmation,
     * removes the appointment from the database.
     * @param event appointments "Delete" button click
     * @throws SQLException handles SQL errors
     */
    @FXML
    void onActionDeleteAppt(ActionEvent event) throws SQLException {

        try {
            // Assign currently selected appointment to "selectedAppt" object
            selectedAppt = mainApptsTable.getSelectionModel().getSelectedItem();

            // Throw exception if no appointment is selected
            if(selectedAppt == null) {
                throw new NullPointerException();
            }
            else{
                // Appointment removal confirmation
                if (AlertUtils.deleteWarningYes("appointment")) {
                    // Remove the appointment
                    AppointmentDAO.deleteAppt(selectedAppt);
                }
            }
        }
        catch (NullPointerException e) {
            AlertUtils.noSelectionAlert("Appointment", "delete");
        }
        selectedAppt = null;
    }

    /**
     * This method sets the Appointments tableview with all the appointments currently in the database.
     * @param event "All Appointments" radio button selected
     */
    @FXML
    void onViewAllAppts(ActionEvent event) {

        try {
            // Set tableview with all appointments currently in database
            mainApptsTable.setItems(AppointmentDAO.getAllAppts());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method sets the Appointments tableview with all the appointments currently in the database that fall within
     * the current month.
     * @param event "Current Month" radio button selected
     * @throws SQLException handles SQL errors
     */
    @FXML
    void onViewMonthAppts(ActionEvent event) throws SQLException {

        // List of appointments for current month
        ObservableList<Appointment> monthAppts = FXCollections.observableArrayList();

        // Check for appointments within the current month & add found appointments to list
        for (Appointment appt : AppointmentDAO.getAllAppts()) {
            if (appt.getStart().toLocalDate().getYear() == LocalDate.now().getYear() &&
                    appt.getStart().toLocalDate().getMonth() == LocalDate.now().getMonth()) {
                monthAppts.add(appt);
            }
        }
        // Set tableview with list of current month's appointments
        mainApptsTable.setItems(monthAppts);
    }

    /**
     * This method sets the Appointments tableview with all the appointments currently in the database that fall within
     * the current week.
     * @param event "Current Week" radio button selected
     * @throws SQLException handles SQL errors
     */
    @FXML
    void onViewWeekAppts(ActionEvent event) throws SQLException {

        // List of appointments for current week
        ObservableList<Appointment> weekAppts = FXCollections.observableArrayList();

        // Variables for tracking current week's first and last day
        LocalDate weekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // Check for appointments within the current week & add found appointments to list
        for (Appointment appt : AppointmentDAO.getAllAppts()) {
            if (appt.getStart().toLocalDate().isEqual(weekStart) || appt.getStart().toLocalDate().isEqual(weekEnd) ||
                    (appt.getStart().toLocalDate().isAfter(weekStart) && appt.getStart().toLocalDate().isBefore(weekEnd)))
            {
                weekAppts.add(appt);
            }
        }
        // Set tableview with list of current week's appointments
        mainApptsTable.setItems(weekAppts);
    }

    /**
     * This method moves the current scene to the Reports form.
     * @param event "Reports" button click
     * @throws IOException thrown by FXMLLoader.load() if the .fxml file URL is not input correctly
     */
    @FXML
    void onActionShowReports(ActionEvent event) throws IOException {
        SceneUtils.toReportsForm(mainReportsBtn);
    }

    /**
     * Upon confirmation though use of a dialog box, this method changes the current scene to the Login GUI form and
     * sets "currentUserID" object to null.
     * @param event "Logout" button click
     * @throws IOException thrown by FXMLLoader.load() if the .fxml file URL is not input correctly
     */
    @FXML
    void onActionLogOut(ActionEvent event) throws IOException {

        if (AlertUtils.mainLogoutConfirm() == true) {

            SceneUtils.toLoginForm(logoutBtn);

            // Set static variable to null (safety measure)
            LoginController.currentUserId = null;
        }
        System.out.println(LoginController.currentUserId); // Here for debugging purposes
    }
}