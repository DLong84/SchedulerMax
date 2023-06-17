package DAO;

import controller.CustomerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles database access for the "countries" table.
 * @author David Long
 */
public class CountryDAO {

    /**
     * The select query for everything in "countries" table.
     */
    private static final String selectAllQuery = "SELECT * FROM COUNTRIES";

    /**
     * This method retrieves all country information currently in the database and instantiates a new country object for
     * every record that is returned from the query. It then obtains the country's name, which is added to the returned
     * observable list.
     * @return the list of countries by name
     * @throws SQLException handles SQL errors
     */
    public static ObservableList<String> getAllCountryNames () throws SQLException {

        // List of country names
        ObservableList<String> countries = FXCollections.observableArrayList();

        PreparedStatement ps = JDBC.connection.prepareStatement(selectAllQuery);
        ResultSet rs = ps.executeQuery();

        // Create a Country object for every record returned and add its name to the list
        while (rs.next()) {
            Country country = new Country(rs.getInt("Country_Id"),
                    rs.getString("Country"));
            String countryName = country.getCountry();
            countries.add(countryName);
        }
        return countries;
    }

    /**
     * This method retrieves the ID of the currently selected country from the database, based on the selection in a
     * ComboBox.
     * @param selectedCountry selected country from the ComboBox
     * @return the country's ID
     * @throws SQLException handles SQL errors
     */
    public static int getCountryId(String selectedCountry) throws SQLException {
        selectedCountry = CustomerController.selectedCountry; // Currently selected country

        String query = "SELECT * FROM countries WHERE Country=?";

        PreparedStatement ps = JDBC.connection.prepareStatement(query);
        ps.setString(1, selectedCountry);

        ResultSet result = ps.executeQuery();

        // Instantiate the "ID" variable
        int countryId = -1;

        // If it exists, get the country's ID from the database results
        while (result.next()) {
            countryId = result.getInt("Country_ID");
        }

        return countryId;
    }
}