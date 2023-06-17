package controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Contact;
import model.Customer;
import utlities.SceneUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ResourceBundle;

/**
 * This class is used to manage the flow of data and view of the application's Reports GUI form.
 * @author David Long
 */
public class ReportsController implements Initializable {

    // Elements of GUI form
    @FXML
    private TableColumn<Appointment, Month> apptCntMonthCol;
    @FXML
    private TableColumn<Appointment, String> apptCntTypeCol;
    @FXML
    private TableColumn<Appointment, Integer> apptCountCol;
    @FXML
    private TableView<Appointment> apptCountTable;
    @FXML
    private TableColumn<Appointment, Integer> apptCustIdCol;
    @FXML
    private TableColumn<Appointment, String> apptDescripCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> apptEndDTCol;
    @FXML
    private TableColumn<Appointment, Integer> apptIdCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> apptStartDTCol;
    @FXML
    private TableColumn<Appointment, String> apptTitleCol;
    @FXML
    private TableView<Appointment> contactApptTable;
    @FXML
    private TableColumn<Appointment, String> contactApptTypeCol;
    @FXML
    private TableColumn<Customer, String> custCountryCol;
    @FXML
    private TableView<Customer> custLocTable;
    @FXML
    private TableColumn<Customer, String> custNameCol;
    @FXML
    private TableColumn<Customer, String> custStateProvCol;
    @FXML
    private ComboBox<Contact> rprtContactComboBox;
    @FXML
    private Button rptsBackBtn;

    /**
     * This method loads the current form's GUI elements and sets the form's tableviews with customer and appointment
     * data from the database.
     * @param url the location of the controller's .fxml file
     * @param resourceBundle the locale-specific resources for the controller's objects
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Initialize contact schedule tableview
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescripCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        contactApptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptStartDTCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        apptEndDTCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        apptCustIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));


        // Initialize and set appointment counts tableview
        apptCntMonthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        apptCntTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptCountCol.setCellValueFactory(new PropertyValueFactory<>("count"));

        try {
            apptCountTable.setItems(AppointmentDAO.getApptCount());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        // Initialize and set customer location tableview
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        custCountryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        custStateProvCol.setCellValueFactory(new PropertyValueFactory<>("state"));

        try {
            custLocTable.setItems(CustomerDAO.getAllCustomers());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Load ComboBox with contacts
        try {
            rprtContactComboBox.setItems(ContactDAO.getAllContactObjects());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Upon making a selection in the contacts ComboBox, this method sets the "contactApptTable" with the selected
     * contact's appointment information.
     * @param event selection made in "rprtContactComboBox"
     * @throws SQLException handles SQL errors
     */
    @FXML
    void onActionContactSelect(ActionEvent event) throws SQLException {

        // Get selected contact's name and assign it to a variable
        String selectedContact = rprtContactComboBox.getValue().getName();

        // List of appointments for selected contact
        ObservableList<Appointment> contactAppts = FXCollections.observableArrayList();

        // Check for appointments related to the selected contact & add found appointments to list
        for (Appointment appt : AppointmentDAO.getAllAppts()) {
            if (appt.getContact().equals(selectedContact)) {
                contactAppts.add(appt);
            }
        }
        // Set Tableview with list of selected contact's appointment information
        contactApptTable.setItems(contactAppts);
    }

    /**
     * This method moves the current scene to the Main form.
     * @param event "Back" button click
     * @throws IOException thrown by FXMLLoader.load() if the .fxml file URL is not input correctly
     */
    @FXML
    void onActionGoBack(ActionEvent event) throws IOException {
        SceneUtils.toMainForm(rptsBackBtn);
    }
}