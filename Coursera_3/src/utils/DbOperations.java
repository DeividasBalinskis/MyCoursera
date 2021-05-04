package utils;

import fxControl.TableParams;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DbOperations {

    private static Connection connection;
    private static PreparedStatement statement;

    public static Connection connectToDb() {
        String DB_URL = "jdbc:mysql://localhost/coursera";
        String USER = "root";
        String PASS = "";
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void disconnectFromDb(Connection connection, Statement statement) {
        try {
            if (connection != null && statement != null) {
                connection.close();
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //User
    public static void updateUserRecord(int id, String colName, String newValue) throws SQLException {
        if (!newValue.equals("")) {
            connection = DbOperations.connectToDb();
            String sql = "UPDATE `users` SET `" + colName + "`  = ? WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, newValue);
            statement.setInt(2, id);
            statement.executeUpdate();
            DbOperations.disconnectFromDb(connection, statement);
        }
    }

    public static void insertRecordAdmin(String login, String psw, String email, String phoneNum, int courseIs) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "INSERT INTO `users`(`login`, `psw`, `email`, `phone_number`, `course_is`) VALUES(?,?,?,?,?)";
        statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        statement.setString(2, psw);
        statement.setString(3, email);
        statement.setString(4, phoneNum);
        statement.setInt(5, courseIs);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void insertRecordStudent(String login, String psw, String email, int courseIs) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "INSERT INTO `users`(`login`, `psw`, `email`, `phone_number`, `course_is`) VALUES(?,?,?,?,?)";
        statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        statement.setString(2, psw);
        statement.setString(3, email);
        statement.setInt(4, courseIs);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void deleteUserRecord(String name) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "DELETE FROM `users` WHERE name = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void deleteUserRecord(int id) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "DELETE FROM `users` WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static ArrayList<Administrator> getAllAdminsFromDb(int courseIs) throws SQLException {
        ArrayList<Administrator> allAdmins = new ArrayList<>();
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM `users` AS c WHERE c.course_is = ? AND c.phone_number is not NULL";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, courseIs);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            allAdmins.add(new Administrator(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return allAdmins;
    }

    public static User getUserByName(String login) throws SQLException {
        User user = null;
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM users AS c WHERE c.login = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            user = new User(rs.getString(2), rs.getString(3), rs.getString(4));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return user;
    }

    public static int getUserIdByLogin(String login, Connection connection) throws SQLException {
        String sql = "SELECT id FROM users WHERE users.login = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        ResultSet rs = statement.executeQuery();
        int userId = 0;
        while (rs.next()){
            userId = rs.getInt(1);
        }
        return userId;
    }

    public static Administrator getAdmin(String login, String psw, int courseIs) throws SQLException {
        Administrator administrator = null;
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM users AS c WHERE c.login = ? AND c.psw = ? AND c.course_is = ? AND c.phone_number is not NULL";
        statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        statement.setString(2, psw);
        statement.setInt(3, courseIs);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            administrator = new Administrator(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return administrator;
    }


    //Course
    public static void updateDbRecord(int id, String colName, Double newValue) throws SQLException {
        if (newValue != 0) {
            connection = DbOperations.connectToDb();
            String sql = "UPDATE course SET `" + colName + "`  = ? WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setDouble(1, newValue);
            statement.setInt(2, id);
            statement.executeUpdate();
            DbOperations.disconnectFromDb(connection, statement);
        }
    }

    public static void updateDbRecord(int id, String colName, String newValue) throws SQLException {
        if (!newValue.equals("")) {
            connection = DbOperations.connectToDb();
            String sql = "UPDATE `course` SET `" + colName + "`  = ? WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, newValue);
            statement.setInt(2, id);
            statement.executeUpdate();
            DbOperations.disconnectFromDb(connection, statement);
        }
    }

    public static void updateDbRecord(int id, String colName, LocalDate newValue) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "UPDATE `course` SET `" + colName + "`  = ? WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setDate(1, Date.valueOf(newValue));
        statement.setInt(2, id);
        statement.executeUpdate();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void deleteDbRecord(int id) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "DELETE FROM `course` WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void deleteDbRecord(String name) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "DELETE FROM `course` WHERE name = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void insertRecordCourse(String name, LocalDate startDate, LocalDate endDate, int adminId, double price, int courseIs) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "INSERT INTO `course`(`name`, `start_date`, `end_date`, `admin_id`, `course_price`, `course_is`) VALUES(?,?,?,?,?,?)";
        statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setDate(2, Date.valueOf(startDate));
        statement.setDate(3, Date.valueOf(endDate));
        statement.setInt(4, adminId);
        statement.setDouble(5, price);
        statement.setInt(6, courseIs);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    //refractor??
    public static void InsertDbRecord(String name, Date start_date, Date end_date, int admin_id, double course_price, int course_is) throws SQLException {
        connection = DbOperations.connectToDb();
        PreparedStatement preparedStatement = connection.prepareStatement(" INSERT INTO course values (default, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, name);
        preparedStatement.setDate(2, start_date);
        preparedStatement.setDate(3, end_date);
        preparedStatement.setInt(4, admin_id);
        preparedStatement.setDouble(5, course_price);
        preparedStatement.setInt(6, course_is);
        preparedStatement.executeUpdate();

        ResultSet tableKeys = preparedStatement.getGeneratedKeys();
        tableKeys.next();

        DbOperations.disconnectFromDb(connection, statement);
    }

    public static ArrayList<Course> getAllCoursesFromDb(int courseIsId) throws SQLException {
        ArrayList<Course> allCourses = new ArrayList<>();
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM course AS c WHERE c.course_is = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, courseIsId);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            allCourses.add(new Course(rs.getInt(1), rs.getString(2), rs.getDate(3), rs.getDate(4), rs.getDouble(6)));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return allCourses;
    }

    public static Course getCourseByName(String name) throws SQLException {
        Course course = null;
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM course AS c WHERE c.name = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            course = new Course(rs.getInt(1), rs.getString(2), rs.getDate(3), rs.getDate(4), rs.getDouble(6));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return course;
    }

    public static int getCourseIdByName(String name, Connection connection) throws SQLException {
        String sql = "SELECT id FROM course WHERE course.name = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        ResultSet rs = statement.executeQuery();
        int courseId = 0;
        while (rs.next()){
            courseId = rs.getInt(1);
        }
        return courseId;
    }


    //Folder
    public static void insertRecordFolder(String name, LocalDate date_modified, int courseId) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "INSERT INTO `folder`(`name`, `date_modified`, `course_id`) VALUES(?,?,?)";
        statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setDate(2, Date.valueOf(date_modified));
        statement.setInt(3, courseId);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void deleteFolderRecord(int id) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "DELETE FROM `folder` WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void deleteFolderRecord(String name) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "DELETE FROM `folder` WHERE name = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static ArrayList<Folder> getAllFoldersFromDb(int courseId) throws SQLException {
        ArrayList<Folder> allFolders = new ArrayList<>();
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM folder AS c WHERE c.course_id = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, courseId);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            allFolders.add(new Folder(rs.getInt(1), rs.getString(2), rs.getDate(3)));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return allFolders;
    }

    public static Folder getFolderByName(String name) throws SQLException {
        Folder folder = null;
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM folder AS c WHERE c.name = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            folder = new Folder(rs.getInt(1), rs.getString(2), rs.getDate(3));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return folder;
    }

    public static int getFolderIdByName(String name, Connection connection) throws SQLException {
        String sql = "SELECT id FROM folder WHERE course.name = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        ResultSet rs = statement.executeQuery();
        int folderId = 0;
        while (rs.next()){
            folderId = rs.getInt(1);
        }
        return folderId;
    }


    //Files
    public static void insertRecordFile(String name, LocalDate dateAdded, String filePath, int folderId) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "INSERT INTO `folder_files`(`name`, `date_added`, `file_path`, `folder_id`) VALUES(?,?,?,?)";
        statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setDate(2, Date.valueOf(dateAdded));
        statement.setString(3, filePath);
        statement.setInt(4, folderId);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void deleteFileRecord(int id) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "DELETE FROM `folder_files` WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void deleteFileRecord(String name) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "DELETE FROM `folder_files` WHERE name = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static ArrayList<File> getAllFilesFromDb(int folderId) throws SQLException {
        ArrayList<File> allFiles = new ArrayList<>();
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM folder_files AS c WHERE c.course_id = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, folderId);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            allFiles.add(new File(rs.getInt(1), rs.getString(2), rs.getDate(3), rs.getString(4)));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return allFiles;
    }

    public static File getFileByName(String name) throws SQLException {
        File file = null;
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM folder_files AS c WHERE c.name = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            file = new File(rs.getInt(1), rs.getString(2), rs.getDate(3), rs.getString(4));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return file;
    }

    public static int getFileIdByName(String name, Connection connection) throws SQLException {
        String sql = "SELECT id FROM folder_files WHERE course.name = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        ResultSet rs = statement.executeQuery();
        int fileId = 0;
        while (rs.next()){
            fileId = rs.getInt(1);
        }
        return fileId;
    }


}
