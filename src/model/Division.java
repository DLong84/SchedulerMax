package model;

/**
 * This class represents a Division.
 * @author David Long
 */
public class Division {

    // Attributes
    private int id;
    private String division;
    private int countryId;

    /**
     * This is the Division constructor.
     * @param id the division's ID
     * @param division the division's name
     * @param countryId the division's Country ID
     */
    public Division(int id, String division, int countryId) {
        this.id = id;
        this.division = division;
        this.countryId = countryId;
    }

    // Setters for Division attributes
    public void setId(int id) {
        this.id = id;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    // Getters for Division attributes
    public int getId() {
        return id;
    }

    public String getDivision() {
        return division;
    }

    public int getCountryId() {
        return countryId;
    }
}