package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles database access for the "users" table.
 * @author David Long
 */
public class UserDAO {

    /**
     * The select query for everything in "users" table.
     */
    private static final String selectAllQuery= "SELECT * FROM users";

    /**
     * This method retrieves all user information currently in the database and instantiates a new user object for every
     * record that is returned from the query. All user objects are added to an observable list, the list is then
     * returned.
     * @return the list of users
     * @throws SQLException handles SQL errors
     */
    public static ObservableList<User> getAllUserObjects () throws SQLException {

        // List of User objects
        ObservableList<User> users = FXCollections.observableArrayList();

        PreparedStatement ps = JDBC.connection.prepareStatement(selectAllQuery);
        ResultSet rs = ps.executeQuery();

        // Create a User object for every record returned and add it to the list
        while (rs.next()) {
            User user = new User(rs.getInt("User_ID"), rs.getString("User_Name"));

            users.add(user);
        }
        return users;
    }

    /**
     * This method takes in the Id of a user and compares it to all the user object Ids's in the database. If the Id
     * matches a user object's Id, that user object is returned.
     * @param userId the user id for comparison
     * @return the matching user object
     * @throws SQLException handles SQL errors
     */
    public static User getUserObject(int userId) throws SQLException {

        // Instantiate User object
        User userObject = null;
        for(User user : getAllUserObjects()) {
            if (user.getId() == userId) {  // If user is found
                userObject = user;
                break;
            }
        }
        return userObject;
    }
}