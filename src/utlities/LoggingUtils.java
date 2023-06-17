package utlities;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * This class contains methods to handle writing logs of user login attempts to a file.
 * @author David Long
 */
public class LoggingUtils {

    /**
     * The login attempt logging file.
     */
    private static final String logFile = "login_activity.txt";

    /**
     * The DateTimeFormatter object to be used for formatting the timestamp written to the logging file.
     */
    private static final DateTimeFormatter tsFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * This method takes in a user's name and appends a string to the "login_activity.txt" file. The string states that
     * the user's login attempt was successful, along with the date and time in UTC.
     * @param user the user attempting log-in
     * @throws IOException thrown if there is an issue writing to the file
     */
    public static void successToFile(String user) throws IOException {

        try {
            // FileWriter object for appending to logging file
            FileWriter appndWriter = new FileWriter(logFile, true);

            // PrintWriter object for printing to the logging file
            PrintWriter pwWrite = new PrintWriter(appndWriter);

            // Write successful login to file
            pwWrite.println("User: " +"\"" + user +"\"" + " log-in was successful at "
                            + tsFormat.format(LocalDateTime.now(ZoneId.of("UTC"))) + "UTC");

            System.out.println("User: " +"\"" + user +"\"" + " successful log-in attempt logged to file"); // For debugging

            // Close writer
            pwWrite.close();

        }
        catch (IOException e) {
            System.out.println("Error attempting to write to login_activity file. See message: " + e.getMessage());
        }
    }

    /**
     * This method takes in a user's name and appends a string to the "login_activity.txt" file. The string states that
     * the user's login attempt was unsuccessful, along with the date and time in UTC.
     * @param user the user attempting log-in
     * @throws IOException thrown if there is an issue writing to the file
     */
    public static void failToFile(String user) throws IOException {

        try {
            // FileWriter object for appending to logging file
            FileWriter appndWriter = new FileWriter(logFile, true);

            // PrintWriter object for printing to the logging file
            PrintWriter pwWrite = new PrintWriter(appndWriter);

            // Write unsuccessful login to file
            pwWrite.println("User: " +"\"" + user +"\"" + " attempted unsuccessful log-in at "
                    + tsFormat.format(LocalDateTime.now(ZoneId.of("UTC"))) + "UTC");

            System.out.println("User: " +"\"" + user +"\"" + " unsuccessful log-in attempt logged to file"); // For debugging

            // Close writer
            pwWrite.close();

        }
        catch (IOException e) {
            System.out.println("Error attempting to write to login_activity file. See message: " + e.getMessage());
        }
    }
}