package model;

/**
 * This class represents a Contact.
 * @author David Long
 */
public class Contact {

    // Attributes
    private int id;
    private String name;
    private String email;

    /**
     * This is the Contact constructor.
     * @param id the contact's ID
     * @param name the contact's Name
     * @param email the contact's Email
     */
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Setters for Contact attributes
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getters for Contact attributes
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    /**
     * This method overrides the default toString() object method to display the Contact object by its name when setting
     * it into a ComboBox.
     * @return the contact's name
     */
    @Override
    public String toString() {
        return name;
    }
}