package fxControl;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;
import utils.DbOperations;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Login implements Initializable {

    @FXML
    public Button logInBtn;
    @FXML
    public Button signUpBtn;
    @FXML
    public TextField loginField;
    @FXML
    public CheckBox empChk;
    @FXML
    public PasswordField pswField;
    @FXML
    public ComboBox<String> coursesBox;
    private Connection connection;
    private PreparedStatement statement;
    private User currentUser = null;
    private int courseIsId = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> options = new ArrayList<>();
        connection = DbOperations.connectToDb();
        if (connection == null) {
            alertMessage("Unable to connect");
            Platform.exit();
        } else {
            try {
                statement = connection.prepareStatement("SELECT * FROM course_is");
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    options.add("[" + rs.getInt(1) + "] " + rs.getString(2));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            coursesBox.getItems().addAll(options);
        }
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void validateAndLogin(ActionEvent actionEvent) throws IOException, SQLException {
        if (coursesBox.getValue() != null) {
            courseIsId = Integer.parseInt(coursesBox.getValue().toString().split("\\]")[0].replace("[", ""));
        } else {
            alertMessage("Learning platform not chosen!");
        }
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM users AS u WHERE u.login = ? AND u.psw = ? AND u.course_is = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, loginField.getText());
        statement.setString(2, pswField.getText());
        statement.setInt(3, courseIsId);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            currentUser = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBoolean(12));
        }
        DbOperations.disconnectFromDb(connection, statement);

        if (currentUser != null) {
            if ((currentUser.isAdmin() && empChk.isSelected()) || (!currentUser.isAdmin() && !empChk.isSelected())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/mainWindow.fxml"));
                Parent root = loader.load();

                MainWindow mainWindow = loader.getController();
                boolean empSelected = empChk.isSelected();
                mainWindow.setFormData(courseIsId, currentUser.getLogin(), empSelected);

                Stage stage = (Stage) loginField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                if(currentUser.isAdmin()) { alertMessage("That's employees account"); }
                else { alertMessage("You are not employee"); }
            }
        } else {
            alertMessage("Wrong login or password");
        }
    }

    private void alertMessage(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(alertMessage);
        alert.showAndWait();
    }

    public void loadSignUpForm(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/signUpForm.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    //public void checkIfOccupied(ActionEvent actionEvent) { }

    public int getCourseIsId() {
        return courseIsId;
    }
}
