package utlities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.*;

/**
 * This class contains methods to handle Time/Date object manipulation for the application's GUI.
 * @author David Long
 */
public class TimeUtils {

    /**
     * This method creates a list of LocalTime objects as appointment times for loading into a ComboBox.
     * @return the list of appointment times
     */
    public static ObservableList<LocalTime> getAllApptTimes() {

        // List of LocalTime objects
        ObservableList<LocalTime> apptTimes = FXCollections.observableArrayList();

        LocalTime listStart = LocalTime.of(0, 0);
        LocalTime listEnd = LocalTime.of(23, 30);

        // Add times to list in 15 minute increments
        while(listStart.isBefore(listEnd.plusSeconds(1))) {
            apptTimes.add(listStart);
            listStart = listStart.plusMinutes(15);
        }
        return apptTimes;
    }

    /**
     * This method obtains the current system's timezone "ZoneId" and returns it as a String.
     * @return the ZoneId (String)
     */
    public static String getTimezone() {
        String timezone = ZoneId.systemDefault().getId();
        return timezone;
    }

    /**
     * This method obtains the current system's timezone "ZoneId" and returns it as a ZoneId object.
     * @return the ZoneId (object)
     */
    public static ZoneId getTimezoneIdObject() {
        ZoneId timeZoneId = ZoneId.systemDefault();
        return timeZoneId;
    }

    /**
     * This method takes in a LocalDate and a LocalTime and combines/converts them into a single LocalDateTime object.
     * @param date the LocalDate object
     * @param time the LocalTime object
     * @return the LocalDateTime object
     */
    public static LocalDateTime toDateTime(LocalDate date, LocalTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        System.out.println("LocalDateTime object: " + dateTime + " created from form"); // For debugging purposes
        return dateTime;
    }

    /**
     * This method takes in a LocalDateTime object and converts it to a LocalTime object in Eastern time.
     * @param selectedDateTime the LocalDateTime object to be converted
     * @return the LocalTime object in Eastern time
     */
    public static LocalTime toEST(LocalDateTime selectedDateTime) {
        // Set selectedDateTime to a "system" ZonedDateTime object
        ZonedDateTime systemDateTime = ZonedDateTime.of(selectedDateTime, getTimezoneIdObject());

        // Convert the "System" ZonedDateTime object to "Eastern" timezone
        ZonedDateTime estDateTime = systemDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));

        // Pull the LocalTime from the "Eastern" ZonedDateTime object and return it
        LocalTime estTime = estDateTime.toLocalTime();
        return estTime;
    }
}