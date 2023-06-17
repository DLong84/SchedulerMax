package utlities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * This class manages scene-change methods.
 * @author David Long
 */
public class SceneUtils {

    /**
     * This method changes the scene to the "MainForm" GUI.
     * @param clickedButton Variable used from the current scene to set the stage
     * @throws IOException thrown by FXMLLoader.load() if the .fxml file URL is not input correctly
     */
    public static void toMainForm (Button clickedButton) throws IOException {
        Parent root = FXMLLoader.load(SceneUtils.class.getResource("/view/MainForm.fxml"));
        Stage stage = (Stage) clickedButton.getScene().getWindow();
        Scene scene = new Scene(root, 1296, 823);
        stage.setTitle("SchedulerMax");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method changes the scene to the "LoginForm" GUI and loads the GUI elements with the appropriate
     * ResourceBundle properties file.
     * @param clickedButton Variable used from the current scene to set the stage
     * @throws IOException thrown by FXMLLoader.load() if the .fxml file URL is not input correctly
     */
    public static void toLoginForm (Button clickedButton) throws IOException {
        ResourceBundle rb_languages = ResourceBundle.getBundle("languages.loginRB"); // ResourceBundle object

        Stage mainStage = (Stage) clickedButton.getScene().getWindow();
        mainStage.setResizable(false);
        FXMLLoader loader = new FXMLLoader(SceneUtils.class.getResource("/view/LoginForm.fxml"), rb_languages);
        Parent root = loader.load();
        mainStage.setTitle(rb_languages.getString("loginFormHeader"));
        mainStage.setScene(new Scene(root, 600, 449));
        mainStage.show();
    }

    /**
     * This method changes the scene to the "CustomerForm" GUI.
     * @param clickedButton Variable used from the current scene to set the stage
     * @throws IOException thrown by FXMLLoader.load() if the .fxml file URL is not input correctly
     */
    public static void toCustomerForm (Button clickedButton) throws IOException {
        Parent root = FXMLLoader.load(SceneUtils.class.getResource("/view/CustomerForm.fxml"));
        Stage stage = (Stage) clickedButton.getScene().getWindow();
        Scene scene = new Scene(root, 693, 651);
        stage.setTitle("Customer Form");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method changes the scene to the "ApptForm" GUI.
     * @param clickedButton Variable used from the current scene to set the stage
     * @throws IOException thrown by FXMLLoader.load() if the .fxml file URL is not input correctly
     */
    public static void toApptForm (Button clickedButton) throws IOException {
        Parent root = FXMLLoader.load(SceneUtils.class.getResource("/view/ApptForm.fxml"));
        Stage stage = (Stage) clickedButton.getScene().getWindow();
        Scene scene = new Scene(root, 693, 833);
        stage.setTitle("Appointment Form");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method changes the scene to the "ReportsForm" GUI.
     * @param clickedButton Variable used from the current scene to set the stage
     * @throws IOException thrown by FXMLLoader.load() if the .fxml file URL is not input correctly
     */
    public static void toReportsForm (Button clickedButton) throws IOException {
        Parent root = FXMLLoader.load(SceneUtils.class.getResource("/view/ReportsForm.fxml"));
        Stage stage = (Stage) clickedButton.getScene().getWindow();
        Scene scene = new Scene(root, 1022, 660);
        stage.setTitle("Reports Form");
        stage.setScene(scene);
        stage.show();
    }
}