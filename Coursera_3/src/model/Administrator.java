package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Administrator extends User implements Serializable {

    private String phoneNumber;
    private ArrayList<Course> myCreatedCourses = new ArrayList<>();

    public Administrator(
            String login,
            String psw,
            String email,
            String phoneNumber,
            ArrayList<Course> myCreatedCourses) {
        super(login, psw, email);
        this.phoneNumber = phoneNumber;
        this.myCreatedCourses = myCreatedCourses;
    }

    public Administrator(String login, String psw, String email, String phoneNumber) {
        super(login, psw, email);
        this.phoneNumber = phoneNumber;
    }

    public Administrator(int id, String login, String psw, String email, String phoneNumber) {
        super(id, login, psw, email);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<Course> getMyCreatedCourses() {
        return myCreatedCourses;
    }

    public void setMyCreatedCourses(ArrayList<Course> myCreatedCourses) {
        this.myCreatedCourses = myCreatedCourses;
    }
}
