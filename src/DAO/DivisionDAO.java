package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles database access for the "first_level_divisions" table.
 * @author David Long
 */
public class DivisionDAO {

    /**
     * The select query for everything in "first_level_divisions" table.
     */
    private static final String selectAllQuery= "SELECT * FROM first_level_divisions";

    /**
     * This method retrieves all first level division information currently in the database and instantiates a new
     * division object for every record that is returned from the query. All division objects are added to an observable
     * list, the list is then returned.
     * @return the list of divisions
     * @throws SQLException handles SQL errors
     */
    public static ObservableList<Division> getAllDivisionObjects () throws SQLException {

        // List of Division objects
        ObservableList<Division> divisions = FXCollections.observableArrayList();

        PreparedStatement ps = JDBC.connection.prepareStatement(selectAllQuery);
        ResultSet rs = ps.executeQuery();

        // Create a Division object for every record returned and add it to the list
        while (rs.next()) {
            Division division = new Division(rs.getInt("Division_ID"), rs.getString("Division"),
                    rs.getInt("Country_ID"));

            divisions.add(division);
        }
        return divisions;
    }

    /**
     * This method retrieves the ID of the currently selected division from the database, based on the selection in a
     * ComboBox.
     * @param selectedDivision selected division from the ComboBox
     * @return the division's ID
     * @throws SQLException handles SQL errors
     */
    public static int getDivisionId(String selectedDivision) throws SQLException {

        String query = "SELECT * FROM first_level_divisions WHERE Division=?";

        PreparedStatement ps = JDBC.connection.prepareStatement(query);
        ps.setString(1, selectedDivision);

        ResultSet result = ps.executeQuery();

        // Instantiate the "ID" variable
        int divisionId = -1;

        // If it exists, get the country's ID from the database results
        while (result.next()) {
            divisionId = result.getInt("Division_ID");
        }

        return divisionId;
    }

    /**
     * This method takes in the Id of a division and compares it to all the division object Id's in the database. If the
     * Id matches a division object's Id, that division name is returned.
     * @param selectedDivisionId the division Id for comparison
     * @return the matching division's name
     * @throws SQLException handles SQL errors
     */
    public static String getDivisionName(int selectedDivisionId) throws SQLException {

        // Instantiate division name variable
        String divisionName = "";

        for (Division division : getAllDivisionObjects()) {
            if (division.getId() == selectedDivisionId) { // If division is found
                divisionName = division.getDivision();
                break;
            }
        }
        return divisionName;
    }
}