package model;

/**
 * This class represents a User.
 * @author David Long
 */
public class User {

    // Attributes
    private int id;
    private String name;

    /**
     * This is the User constructor.
     * @param id the user's ID
     * @param name the user's Name
     */
    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Setters for User attributes
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getters for User attributes
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * This method overrides the default toString() object method to display the User object by its id when setting it
     * into a ComboBox.
     * @return the contact's id
     */
    @Override
    public String toString() {
        return Integer.toString(id);
    }
}