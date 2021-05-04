package fxControl;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.DbOperations;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class SignUpForm implements Initializable {
    public TextField userNewName;
    public TextField userNewSurname;
    public TextField userNewLogin;
    public TextField userNewEmail;
    public PasswordField userNewPass;
    public PasswordField userNewPassRe;
    public ComboBox courseBox;
    public TextField userNewNr;
    public CheckBox empSignup;
    private PreparedStatement statement;
    private Connection connection;

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
            courseBox.getItems().addAll(options);
        }
        DbOperations.disconnectFromDb(connection, statement);

    }

    public void returnToLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/login.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) userNewName.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void createUser(ActionEvent actionEvent) {
        if (courseBox.getValue() != null) {
            if (userNewPass.getText().equals(userNewPassRe.getText())) {
                connection = DbOperations.connectToDb();
                PreparedStatement preparedStatement = null;
                java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                try {
                    if(empSignup.isSelected()) {
                        preparedStatement = connection.prepareStatement(" INSERT INTO users values (default, ?, ?, ?, ?, ?, ?, default, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                        preparedStatement.setString(1, userNewLogin.getText());
                        preparedStatement.setString(2, userNewPass.getText());
                        preparedStatement.setString(3, "");
                        preparedStatement.setString(4, userNewName.getText());
                        preparedStatement.setString(5, userNewSurname.getText());
                        preparedStatement.setDate(6, date);
                        preparedStatement.setDate(7, date);
                        preparedStatement.setInt(8, Integer.parseInt(courseBox.getValue()
                                .toString().split("\\]")[0].replace("[", "")));
                        preparedStatement.setBoolean(9, empSignup.isSelected());
                        preparedStatement.executeUpdate();

                        ResultSet tableKeys = preparedStatement.getGeneratedKeys();
                        tableKeys.next();
                    } else {
                        preparedStatement = connection.prepareStatement(" INSERT INTO users values (default, ?, ?, ?, ?, ?, ?, default, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                        preparedStatement.setString(1, userNewLogin.getText());
                        preparedStatement.setString(2, userNewPass.getText());
                        preparedStatement.setString(3, userNewEmail.getText());
                        preparedStatement.setString(4, "");
                        preparedStatement.setString(5, userNewName.getText());
                        preparedStatement.setString(6, userNewSurname.getText());
                        preparedStatement.setDate(7, date);
                        preparedStatement.setDate(8, date);
                        preparedStatement.setInt(9, Integer.parseInt(courseBox.getValue()
                                .toString().split("\\]")[0].replace("[", "")));
                        preparedStatement.setBoolean(10, empSignup.isSelected());
                        preparedStatement.executeUpdate();

                        ResultSet tableKeys = preparedStatement.getGeneratedKeys();
                        tableKeys.next();
                    }
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }

                userNewLogin.setText("");
                userNewPass.setText("");
                userNewPassRe.setText("");
                userNewName.setText("");
                userNewSurname.setText("");
                userNewEmail.setText("");

                DbOperations.disconnectFromDb(connection, statement);
            } else {
                alertMessage("Please make sure your passwords match.");
            }
        } else {
            alertMessage("Learning platform not chosen!");
            return;
        }
    }

    private void alertMessage(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }

    public void empIsChecked(ActionEvent actionEvent) {
        if(empSignup.isSelected()) {
            userNewNr.setDisable(false);
        } else {
            userNewNr.setDisable(true);
        }
    }
}
