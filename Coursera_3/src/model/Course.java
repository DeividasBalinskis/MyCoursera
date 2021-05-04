package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Course implements Serializable {
  private int id;
  private String name;
  private Date startDate;
  private Date endDate;
  private Administrator administrator;
  private ArrayList<Student> enrolledUsers = new ArrayList();
  private ArrayList<Folder> folders = new ArrayList();
  private double coursePrice;

  public Course(
          String name,
          Date startDate,
          Date endDate,
          Administrator administrator,
          ArrayList<Student> enrolledUsers,
          ArrayList<Folder> folders,
          double coursePrice) {
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.administrator = administrator;
    this.enrolledUsers = enrolledUsers;
    this.folders = folders;
    this.coursePrice = coursePrice;
  }

  public Course(String name, Date startDate, Date endDate, double coursePrice) {
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.coursePrice = coursePrice;
  }

  public Course(String name, Date startDate, Date endDate, Administrator administrator, double coursePrice) {
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.administrator = administrator;
    this.coursePrice = coursePrice;
    this.enrolledUsers = new ArrayList<>();
  }

  public Course(String name, double coursePrice) {
    this.name = name;
    this.coursePrice = coursePrice;
  }

  public Course(int id, String name, Date startDate, Date endDate, double coursePrice) {
    this.id = id;
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.coursePrice = coursePrice;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public ArrayList<Folder> getFolders() {
    return folders;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Administrator getAdministrator() {
    return administrator;
  }

  public void setAdministrator(Administrator administrator) {
    this.administrator = administrator;
  }

  public ArrayList<Student> getEnrolledUsers() {
    return enrolledUsers;
  }

  public void setEnrolledUsers(ArrayList<Student> enrolledUsers) {
    this.enrolledUsers = enrolledUsers;
  }

  public ArrayList<Folder> getAllFolders() {
    return folders;
  }

  public void setFolders(ArrayList<Folder> folders) {
    this.folders = folders;
  }

  public double getCoursePrice() {
    return coursePrice;
  }

  public void setCoursePrice(double coursePrice) {
    this.coursePrice = coursePrice;
  }

  @Override
  public String toString() {
    return  name + "{" + startDate + "}";
  }
}
