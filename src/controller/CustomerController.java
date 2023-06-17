package controller;

import DAO.CountryDAO;
import DAO.CustomerDAO;
import DAO.DivisionDAO;
import DAO.JDBC;
import Interfaces.ComboBoxInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Customer;
import model.Division;
import utlities.AlertUtils;
import utlities.SceneUtils;
import utlities.ValidationUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * This class is used to manage the flow of data and view of the application's Customer GUI form.
 * @author David Long
 */
public class CustomerController implements Initializable {

    // Elements of GUI form
    @FXML
    private TextField custAddressFld;
    @FXML
    private Button custCancelBtn;
    @FXML
    private ComboBox<String> custCountryComboBox;
    @FXML
    private TextField custIDFld;
    @FXML
    private TextField custNameFld;
    @FXML
    private TextField custPhoneFld;
    @FXML
    private TextField custPostalFld;
    @FXML
    private Button custSaveBtn;
    @FXML
    private ComboBox<String> custStateComboBox;

    /**
     * Selected country from country ComboBox.
     */
    public static String selectedCountry;

    /**
     * Boolean variable for keeping track of customer modification status.
     */
    static boolean modifyCustomer = false;

    /**
     * This method loads the form's GUI elements and sets the country ComboBox with all available countries. If a
     * customer was selected for modification, the fields and ComboBoxes are set with the selected customer data.
     * Otherwise, all form fields and selections are empty.
     * @param url The location of the controller's .fxml file
     * @param resourceBundle The locale-specific resources for the controller's objects
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Dummy event for triggering the onAction event when loading the form for modifying a customer
        ActionEvent event = new ActionEvent();

        // Get selected customer from Main form
        Customer selectedCustomer = MainFormController.selectedCustomer;

        try {
            // Set Countries available in custCountryComboBox
            try {
                custCountryComboBox.setItems(CountryDAO.getAllCountryNames());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            // Load existing customer info into form for existing customer
            if (modifyCustomer) {
                custIDFld.setText(Integer.toString(selectedCustomer.getId()));
                custNameFld.setText(selectedCustomer.getName());
                custAddressFld.setText(selectedCustomer.getAddress());
                custCountryComboBox.setValue(selectedCustomer.getCountry());
                onCountrySelection(event); // Preload division ComboBox
                custStateComboBox.setValue(DivisionDAO.getDivisionName(selectedCustomer.getDivisionId()));
                custPostalFld.setText(selectedCustomer.getPostalCode());
                custPhoneFld.setText(selectedCustomer.getPhone());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method throws a specific warning dialog box, depending on whether the user was attempting to modify an existing
     * customer or add a new customer. Upon the user selecting "yes" button in the respective dialog box, the scene is
     * then changed to the "MainForm" GUI.
     * @param event "Cancel" button click
     * @throws IOException thrown by FXMLLoader.load() if the .fxml file URL is not input correctly
     */
    @FXML
    void onActionCancelCustomer(ActionEvent event) throws IOException {

        // Existing customer warning
        if (modifyCustomer) {
            if (AlertUtils.cancelWarningYes()) {
                modifyCustomer = false; // Reset modification variable
                MainFormController.selectedCustomer = null; // Reset selected customer, no longer needed
                SceneUtils.toMainForm(custCancelBtn);
            }
        }
        // New customer warning
        else if (AlertUtils.cancelWarningYes("customer")) {
            modifyCustomer = false; // Reset modification variable
            MainFormController.selectedCustomer = null; // Reset selected customer, no longer needed
            SceneUtils.toMainForm(custCancelBtn);
        }
    }

    /**
     * This method checks the form for empty and non-selected fields, then inserts the form's user input data into the
     * corresponding SQL statement depending on whether the user is intending to create a new customer or modify
     * an existing customer. Upon successfully modifying the "customers" table, the scene is then changed to the "MainForm"
     * GUI.
     * @param event "Save" button click
     * @throws SQLException handles SQL errors
     * @throws IOException thrown by FXMLLoader.load() if the .fxml file URL is not input correctly
     */
    @FXML
    void onActionSaveCustomer(ActionEvent event) throws SQLException, IOException {

        // Form validation check for blank fields or non-selected ComboBoxes
        if (    ValidationUtils.fldIsEmpty(custNameFld, "Name") ||
                ValidationUtils.fldIsEmpty(custAddressFld, "Address") ||
                ValidationUtils.boxNotSelected(custCountryComboBox, "Country") ||
                ValidationUtils.boxNotSelected(custStateComboBox, "State/Province") ||
                ValidationUtils.fldIsEmpty(custPostalFld, "Postal code") ||
                ValidationUtils.fldIsEmpty(custPhoneFld, "Phone number"))
        {
            return;
        }
        else {
            PreparedStatement ps; // Create PreparedStatement object

            if (modifyCustomer) {
                ps = JDBC.connection.prepareStatement(CustomerDAO.modCustomerStmt); // Use existing customer statement
            }
            else {
                ps = JDBC.connection.prepareStatement(CustomerDAO.addCustomerStmt); // Use new customer statement
            }
            // Set form's values into corresponding SQL statement & execute
            ps.setString(1, custNameFld.getText());
            ps.setString(2, custAddressFld.getText());
            ps.setString(3, custPostalFld.getText());
            ps.setString(4, custPhoneFld.getText());
            ps.setInt(5, DivisionDAO.getDivisionId(custStateComboBox.getValue()));

            // Set value of Customer_ID into update statement
            if (modifyCustomer) {
                ps.setInt(6, MainFormController.selectedCustomer.getId());
            }
            // Execute SQL statement and return number of rows added as a variable
            int rowsAdded = ps.executeUpdate();
            // If successful
            if (rowsAdded == 1) {
                System.out.println("Customer: " + "\"" + custNameFld.getText() + "\"" + " add/update successful");

                SceneUtils.toMainForm(custSaveBtn);
            } else {
                System.out.println("Something went wrong!");
                return;
            }
        }
        modifyCustomer = false; // Reset modification variable
        MainFormController.selectedCustomer = null; // Reset selected customer, no longer needed
    }

    /**
     * Lambda expression used for filtering and setting Division names to be displayed in the "custStateComboBox".
     * It takes in a country's ID as a parameter, to be used as the filter. It then iterates through the list of
     * division objects, comparing the ID of the selected country to the division's country ID. When a match is found,
     * the name of the matching division is added to an Observable list. That list of division names is then set into the
     * "custStateComboBox". A lambda expression was chosen here for clarity and conciseness.
     */
    ComboBoxInterface filter = (selectedCountryId) -> {

        // List for holding division names
        ObservableList<String> divisionNames = FXCollections.observableArrayList();

        // If the division's "Country ID" matches the selected country's ID, add it to the divisionNames list
        for (Division division : DivisionDAO.getAllDivisionObjects()) {
            if (division.getCountryId() == selectedCountryId) {
                divisionNames.add(division.getDivision());
            }
        }
        custStateComboBox.setItems(divisionNames); // Set the list of filtered division names in the ComboBox
    };

    /**
     * This method changes the available selections in the "custStateComboBox" depending on which country is selected in
     * the "custCountryComboBox".
     * @param event selection made in "custCountryComboBox"
     * @throws SQLException handles SQL errors
     */
    @FXML
    void onCountrySelection(ActionEvent event) throws SQLException {

        // Set currently selected country to static variable
        selectedCountry = custCountryComboBox.getSelectionModel().getSelectedItem();

        // Get selected country's ID and assign it to a variable
        int selectedCountryId = CountryDAO.getCountryId(selectedCountry);

        // Lambda expression call to filter divisions
        filter.filterComboBox(selectedCountryId);
    }
}
