package fxControl;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import model.Course;
import utils.DbOperations;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

public class MainWindow implements Initializable {
    @FXML
    public ListView<String> myCourses;
    @FXML
    public ListView<String> allCourses;
    @FXML
    public TableView<TableParams> myCreatedCourses;


    public TableColumn<TableParams, Integer> colId;
    public TableColumn<TableParams, String> colName;
    public TableColumn<TableParams, String> colStart;
    public TableColumn<TableParams, String> colEnd;
    public TableColumn<TableParams, Double> colPrice;
    public TableColumn<TableParams, String> deleteCol;
    public TextField addName;
    public DatePicker addStartDate;
    public DatePicker addEndDate;
    public TextField addCoursePrice;
    public Button addCourseBtn;
    public Button enrollBtn;
    public Button viewCourseInfoBtn;
    public Tab myCoursesTab;
    public Tab manageFoldersTab;
    public Tab manageAccountsTab;
    public Button unenrollBtn;
    public Tab myCreatedCoursesTab;
    public ListView foldersField;
    public ListView accountsField;
    public TextField updateEmail;
    public TextField updateNr;
    public TextField updLogin;
    public TextField updPsw;
    public Button updateAccountBtn;
    public Button deleteAccountBtn;
    public Button createAccBtn;
    public CheckBox empAcc;
    private ObservableList<TableParams> data = FXCollections.observableArrayList();

    private int courseIS;
    private String currentUser;
    private Connection connection;
    private PreparedStatement statement;
    private boolean admin = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Tableview structure and behaviour
        myCreatedCourses.setEditable(true);
        colId.setCellValueFactory(new PropertyValueFactory<TableParams, Integer>("colId"));
        colName.setCellValueFactory(new PropertyValueFactory<TableParams, String>("colName"));
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableParams, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableParams, String> t) {
                ((TableParams) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setColName(t.getNewValue());
                TableParams tp = (TableParams) t.getTableView().getItems().get(
                        t.getTablePosition().getRow());
                try {
                    DbOperations.updateDbRecord(tp.getColId(), "name", tp.getColName());
                    refresh();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        colStart.setCellValueFactory(new PropertyValueFactory<TableParams, String>("colStart"));
        colStart.setCellFactory(TextFieldTableCell.forTableColumn());
        colStart.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableParams, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableParams, String> t) {
                ((TableParams) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setColStart(t.getNewValue());
                TableParams tp = (TableParams) t.getTableView().getItems().get(
                        t.getTablePosition().getRow());
                try {
                    DbOperations.updateDbRecord(tp.getColId(), "start_date", tp.getColStart());
                    refresh();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        colEnd.setCellValueFactory(new PropertyValueFactory<TableParams, String>("colEnd"));
        colEnd.setCellFactory(TextFieldTableCell.forTableColumn());
        colEnd.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableParams, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableParams, String> t) {
                ((TableParams) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setColEnd(t.getNewValue());
                TableParams tp = (TableParams) t.getTableView().getItems().get(
                        t.getTablePosition().getRow());
                try {
                    DbOperations.updateDbRecord(tp.getColId(), "end_date", tp.getColEnd());
                    refresh();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        colPrice.setCellValueFactory(new PropertyValueFactory<TableParams, Double>("colPrice"));
        colPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colPrice.setOnEditCommit(
                t -> {
                    ((TableParams) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                            .setColPrice((Double) t.getNewValue());
                    TableParams tp =
                            (TableParams) t.getTableView().getItems().get(t.getTablePosition().getRow());
                    try {
                        DbOperations.updateDbRecord(tp.getColId(), "course_price", tp.getColPrice());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });

        Callback<TableColumn<TableParams, String>, TableCell<TableParams, String>> cellFactory
                = //
                new Callback<TableColumn<TableParams, String>, TableCell<TableParams, String>>() {
                    @Override
                    public TableCell call(final TableColumn<TableParams, String> param) {
                        final TableCell<TableParams, String> cell = new TableCell<TableParams, String>() {
                            final Button btn = new Button("Delete");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        TableParams tp = (TableParams) getTableView().getItems().get(getIndex());
                                        TableParams tp2 = (TableParams) getTableView().getItems().get(2);
                                        try {
                                            DbOperations.deleteDbRecord(tp.getColId());
                                            //allCourses.getItems().remove(tp2.toString());
                                            allCourses.getItems().removeAll(tp2.toString());
                                            refresh();
                                        } catch (SQLException throwables) {
                                            throwables.printStackTrace();
                                        }
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        deleteCol.setCellFactory(cellFactory);
    }

    private void fillAllCourses() {
        List<String> options = new ArrayList<>();
        allCourses.getItems().clear();
        connection = DbOperations.connectToDb();
        if (connection == null) {
            alertMessage("Unable to connect");
            Platform.exit();
        } else {
            try {
                String sql = "SELECT * FROM course AS c WHERE c.course_is = ?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, courseIS);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    options.add(rs.getString(2));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            allCourses.getItems().addAll(options);
        }
        DbOperations.disconnectFromDb(connection, statement);
    }

    private void fillMyCourses() {
        List<String> options = new ArrayList<>();
        String selectedCourse = allCourses.getSelectionModel().getSelectedItems().toString();
        myCourses.getItems().clear();
        connection = DbOperations.connectToDb();
        if (connection == null) {
            alertMessage("Unable to connect");
            Platform.exit();
        } else {
            try {
                String sql = "SELECT u.surname, c.name, c.start_date FROM users u, user_enroll_course ue, course c WHERE u.id = ue.user_id AND c.id = ue.course_id AND c.name = ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, selectedCourse.replace("[", "").replace("]", ""));
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    options.add(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            myCourses.getItems().addAll(options);
        }
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void setFormData(int courseIS, String loginName, boolean emp) throws SQLException {
        this.courseIS = courseIS;
        this.currentUser = loginName;
        this.admin = emp;

        if (!admin) {
            myCreatedCourses.setDisable(true);
            enrollBtn.setDisable(false);
            unenrollBtn.setDisable(false);
            myCoursesTab.setDisable(false);
            manageAccountsTab.setDisable(true);
            manageFoldersTab.setDisable(true);
            myCreatedCoursesTab.setDisable(true);
        } else {
            myCreatedCourses.setDisable(false);
            enrollBtn.setDisable(true);
            unenrollBtn.setDisable(true);
            myCoursesTab.setDisable(true);
            manageAccountsTab.setDisable(false);
            manageFoldersTab.setDisable(false);
            myCreatedCoursesTab.setDisable(false);
        }

        fillAllCourses();
        //fillMyCourses();
    }

    private void alertMessage(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }

    public void viewCourseInfo(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        String selectedCourse = allCourses.getSelectionModel().getSelectedItems().toString();
        String sql = "SELECT * FROM course AS c WHERE c.name = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, selectedCourse.replace("[", "").replace("]", ""));
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Course Information");
            alert.setHeaderText("Course: " + rs.getString(2));
            alert.setContentText("Start Date: " + rs.getString(3)
                    + "\nEnd Date: " + rs.getString(4)
                    + "\nCourse Price: " + rs.getString(6));
            alert.showAndWait();
        }
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void enroll(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        String selectedCourse = allCourses.getSelectionModel().getSelectedItems().toString();
        String sql = "SELECT count(*) FROM `users` u, `user_enroll_course` ue, `course` c WHERE u.id = ue.user_id AND ue.course_id = c.id AND c.name = ? AND u.login = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, selectedCourse.replace("[", "").replace("]", "")); // jei paziuresite jis Jums kurso pavadinima tarp [] grazins, del to replace padarau
        statement.setString(2, currentUser);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            int counter = rs.getInt(1);
            if (counter > 0) alertMessage("Already enrolled");
            else {
                alertMessage("Successfully enrolled");
                String enrollSql = "INSERT INTO `user_enroll_course`(`user_id`, `course_id`) VALUES(?,?)";
                statement = connection.prepareStatement(enrollSql);
                statement.setInt(1, DbOperations.getUserIdByLogin(currentUser, connection));
                statement.setInt(2, DbOperations.getCourseIdByName(selectedCourse.replace("[", "").replace("]", ""), connection));
                statement.execute();
            }
        }
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void unenroll(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        connection = DbOperations.connectToDb();
        String selectedCourse = allCourses.getSelectionModel().getSelectedItems().toString();
        String sql = "SELECT count(*) FROM `users` u, `user_enroll_course` ue, `course` c WHERE u.id = ue.user_id AND ue.course_id = c.id AND c.name = ? AND u.login = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, selectedCourse.replace("[", "").replace("]", "")); // jei paziuresite jis Jums kurso pavadinima tarp [] grazins, del to replace padarau
        statement.setString(2, currentUser);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            int counter = rs.getInt(1);
            if (counter == 0) alertMessage("Already not enrolled");
            else {
                alertMessage("Successfully unenrolled");
                String unenrollSql = "DELETE FROM `user_enroll_course` WHERE course_id =?";
                statement = connection.prepareStatement(unenrollSql);
                statement.setInt(1, DbOperations.getCourseIdByName(selectedCourse.replace("[", "").replace("]", ""), connection));
                statement.execute();
            }
        }
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void loadCreatedCourses(Event event) throws SQLException {
        myCreatedCourses.getItems().clear();
        //get from db
        refresh();
    }

    private void refresh() throws SQLException {
        myCreatedCourses.getItems().clear();
        connection = DbOperations.connectToDb();
        String sql = "SELECT c.id, c.name, c.start_date, c.end_date, c.course_price FROM course AS c, users as U WHERE c.admin_id = u.id AND u.login = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, currentUser);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            TableParams tableParams = new TableParams();
            tableParams.setColId(rs.getInt(1));
            tableParams.setColName(rs.getString(2));
            tableParams.setColStart(rs.getString(3));
            tableParams.setColEnd(rs.getString(4));
            tableParams.setColPrice(rs.getDouble(5));
            data.add(tableParams);
        }
        myCreatedCourses.setItems(data);
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void addNewCourse(ActionEvent actionEvent) throws SQLException {
        if(addName.getText()=="" || addCoursePrice.getText()=="" || addStartDate.getValue()==null || addEndDate.getValue()==null) {
            alertMessage("Užpildykite visus laukus");
        } else {
            LocalDate ldStartDate = addStartDate.getValue();
            LocalDate ldEndDate = addStartDate.getValue();
            java.sql.Date sqlStartDate = java.sql.Date.valueOf(ldStartDate);
            java.sql.Date sqlEndDate = java.sql.Date.valueOf(ldEndDate);
            DbOperations.InsertDbRecord(addName.getText(), sqlStartDate, sqlEndDate, 1,
                    Double.parseDouble(addCoursePrice.getText().replaceAll(",", "")), courseIS);
            allCourses.getItems().add(addName.getText());
            addName.setText("");
            addStartDate.getEditor().setText("");
            addEndDate.getEditor().setText("");
            addCoursePrice.setText("");
            refresh();
        }

    }

    public Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public void loadMyCourses(Event event) throws SQLException {
        myCourses.getItems().clear();
        List<String> options = new ArrayList<>();
        connection = DbOperations.connectToDb();
        String getMyCoursessql = "SELECT c.name, c.start_date FROM users u, user_enroll_course ue, course c WHERE u.id = ue.user_id AND c.id = ue.course_id AND u.login = ?";
        statement = connection.prepareStatement(getMyCoursessql);
        statement.setString(1, currentUser);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            options.add(rs.getString(1) + " " + rs.getString(2));
        }
        myCourses.getItems().addAll(options);
    }

    public void loadAccounts(Event event) throws SQLException {
        accountsField.getItems().clear();
        List<String> options = new ArrayList<>();
        connection = DbOperations.connectToDb();
        String getAccountsSql = "SELECT * FROM users";
        statement = connection.prepareStatement(getAccountsSql);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            options.add(rs.getString(2) + " (" + rs.getString(3) + ")" + " - " + rs.getString(4));
        }
        accountsField.getItems().addAll(options);
    }

    public void loadFolders(Event event) {
    }

    public void updateAccount(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        String selectedAcc = accountsField.getSelectionModel().getSelectedItems().toString();
        String sql = "SELECT u.id, u.login, u.psw, u.email, u.phone_number FROM `users` AS u WHERE u.login = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, selectedAcc.split("\\(", 2)[0].replace("[", "").trim());
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            if(!updLogin.getText().equals("")) {
                DbOperations.updateUserRecord(rs.getInt(1), "login" , updLogin.getText());
                updLogin.clear();
            }
            if(!updPsw.getText().equals("")) {
                DbOperations.updateUserRecord(rs.getInt(1), "psw", updPsw.getText());
                updPsw.clear();
            }
            if(!updateEmail.getText().equals("")) {
                DbOperations.updateUserRecord(rs.getInt(1), "email", updateEmail.getText());
                updateEmail.clear();
            }
            if(!updateNr.getText().equals("")) {
                DbOperations.updateUserRecord(rs.getInt(1), "phone_number", updateNr.getText());
                updateNr.clear();
            }
        }
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void deleteAccount(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        String selectedAcc = accountsField.getSelectionModel().getSelectedItems().toString();
        String sql = "DELETE FROM `users` WHERE login = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, selectedAcc.split("\\(", 2)[0].replace("[", "").trim());
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void empIsCheckedAcc(ActionEvent actionEvent) {
        if(empAcc.isSelected()) {
            updateNr.setDisable(false);
        } else {
            updateNr.setDisable(true);
        }
    }

    public void createAccount(ActionEvent actionEvent) {
        connection = DbOperations.connectToDb();
        String selectedAcc = accountsField.getSelectionModel().getSelectedItems().toString();
        PreparedStatement preparedStatement = null;
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        try {
            if(empAcc.isSelected()) {
                preparedStatement = connection.prepareStatement(" INSERT INTO users values (default, ?, ?, ?, ?, ?, default, default, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, updLogin.getText());
                preparedStatement.setString(2, updPsw.getText());
                preparedStatement.setString(3, "");
                preparedStatement.setString(4, updateNr.getText());
                preparedStatement.setString(5, "");
                preparedStatement.setDate(6, date);
                preparedStatement.setDate(7, date);
                preparedStatement.setInt(8, DbOperations.getCourseIdByName(selectedAcc, connection));
                preparedStatement.setBoolean(9, empAcc.isSelected());
                preparedStatement.executeUpdate();

                ResultSet tableKeys = preparedStatement.getGeneratedKeys();
                tableKeys.next();
            } else {
                preparedStatement = connection.prepareStatement(" INSERT INTO users values (default, ?, ?, ?, default, ?, default, default, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, updLogin.getText());
                preparedStatement.setString(2, updPsw.getText());
                preparedStatement.setString(3, updateEmail.getText());
                preparedStatement.setString(4, "");
                preparedStatement.setDate(5, date);
                preparedStatement.setDate(6, date);
                preparedStatement.setInt(7, DbOperations.getCourseIdByName(selectedAcc, connection));
                preparedStatement.setBoolean(8, empAcc.isSelected());
                preparedStatement.executeUpdate();

                ResultSet tableKeys = preparedStatement.getGeneratedKeys();
                tableKeys.next();
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        updLogin.setText("");
        updPsw.setText("");
        updateNr.setText("");
        updateEmail.setText("");

        DbOperations.disconnectFromDb(connection, statement);
    }
}
