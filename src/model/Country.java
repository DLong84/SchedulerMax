package model;

/**
 * This class represents a Country.
 * @author David Long
 */
public class Country {

    // Attributes
    private int id;
    private String country;

    /**
     * This is the Country constructor.
     * @param id the country's ID
     * @param country the country's Name
     */
    public Country(int id, String country) {
        this.id = id;
        this.country = country;
    }

    // Setters for Country attributes
    public void setId(int id) {
        this.id = id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // Getters for Country attributes
    public int getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }
}