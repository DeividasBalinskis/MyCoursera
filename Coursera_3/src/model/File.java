package model;

import java.io.Serializable;
import java.util.Date;

public class File implements Serializable {

  private int id;
  private String name;
  private Date dateAdded;
  private String linkToFile;

  public File(int id, String name, Date dateAdded, String linkToFile) {
    this.id = id;
    this.name = name;
    this.dateAdded = dateAdded;
    this.linkToFile = linkToFile;
  }

  public File(String name, Date dateAdded, String linkToFile) {
    this.name = name;
    this.dateAdded = dateAdded;
    this.linkToFile = linkToFile;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getDateAdded() {
    return dateAdded;
  }

  public void setDateAdded(Date dateAdded) {
    this.dateAdded = dateAdded;
  }

  public String getLinkToFile() {
    return linkToFile;
  }

  public void setLinkToFile(String linkToFile) {
    this.linkToFile = linkToFile;
  }

  @Override
  public String toString() {
    return "This is " + name + " file " + ",date added " + dateAdded;
  }
}
