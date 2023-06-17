package Interfaces;

import java.sql.SQLException;

/**
 * This is a functional interface for use with a lambda expression to filter a combo box's available selections.
 * @author David Long
 */
public interface ComboBoxInterface {

    /**
     * This is an abstract method for filtering/setting a ComboBox's selections.
     * @param selectedCountryId the Id of the country to be used as the filter
     * @throws SQLException handles SQL errors
     */
    void filterComboBox (int selectedCountryId) throws SQLException;
}
