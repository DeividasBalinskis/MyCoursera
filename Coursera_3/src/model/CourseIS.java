package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class CourseIS implements Serializable {
  private ArrayList<Course> allCourses = new ArrayList<>();
  private ArrayList<Administrator> allAdmins = new ArrayList<>();
  private ArrayList<Student> allStudents = new ArrayList<>();
  private String name;
  private LocalDate dateCreated;
  private String version;

  public CourseIS(String name, LocalDate dateCreated, String version) {
    this.name = name;
    this.dateCreated = dateCreated;
    this.version = version;
  }

  public ArrayList<Course> getAllCourses() {
    return allCourses;
  }

  public void setAllCourses(ArrayList<Course> allCourses) {
    this.allCourses = allCourses;
  }

  public ArrayList<Administrator> getAllAdmins() {
    return allAdmins;
  }

  public void setAllAdmins(ArrayList<Administrator> allAdmins) {
    this.allAdmins = allAdmins;
  }

  public ArrayList<Student> getAllStudents() {
    return allStudents;
  }

  public void setAllStudents(ArrayList<Student> allStudents) {
    this.allStudents = allStudents;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(LocalDate dateCreated) {
    this.dateCreated = dateCreated;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return "This is "
        + name
        + " course system, currently there are "
        + allStudents.size()
        + " students and "
        + allAdmins.size()
        + "contributors ("
        + dateCreated
        + ")";
  }
}
