package Interfaces;

import java.sql.SQLException;

/**
 * This is a functional interface for use with a lambda expression to check a user's upcoming appointments.
 * @author David Long
 */
public interface AppointmentInterface {

    /**
     * This is an abstract method for checking a user's appointments.
     * @param userId the Id of the user
     * @throws SQLException handles SQL errors
     */
    void apptCheck(Object userId) throws SQLException;
}