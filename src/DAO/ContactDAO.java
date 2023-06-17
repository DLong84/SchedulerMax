package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles database access for the "contacts" table.
 * @author David Long
 */
public class ContactDAO {

    /**
     * The select query for everything in "contacts" table.
     */
    private static final String selectAllQuery= "SELECT * FROM contacts";

    /**
     * This method retrieves all contact information currently in the database and instantiates a new contact
     * object for every record that is returned from the query. All contact objects are added to an observable list,
     * the list is then returned.
     * @return the list of contacts
     * @throws SQLException handles SQL errors
     */
    public static ObservableList<Contact> getAllContactObjects () throws SQLException {

        // List of Contact objects
        ObservableList<Contact> contacts = FXCollections.observableArrayList();

        PreparedStatement ps = JDBC.connection.prepareStatement(selectAllQuery);
        ResultSet rs = ps.executeQuery();

        // Create a Contact object for every record returned and add it to the list
        while (rs.next()) {
            Contact contact = new Contact(rs.getInt("Contact_ID"), rs.getString("Contact_Name"),
                    rs.getString("Email"));

            contacts.add(contact);
        }
        return contacts;
    }

    /**
     * This method takes in the name (String) of a contact and compares it to all the contact object names in the
     * database. If the name matches a contact object's name, that contact object is returned.
     * @param contactName the contact name for comparison
     * @return the matching contact object
     * @throws SQLException handles SQL errors
     */
    public static Contact getContactObject(String contactName) throws SQLException {

        // Instantiate Contact object
        Contact contactObject = null;
        for(Contact contact : getAllContactObjects()) {
            if (contact.getName().equals(contactName)) {  // If contact is found
                contactObject = contact;
                break;
            }
        }
        return contactObject;
    }
}