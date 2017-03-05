package com.example.bcr6.assignment1.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by bcr6 on 3/2/17.
 * Database table mapping to class
 */

@DatabaseTable(tableName = "friend")
public class Friend {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    String firstName;
    @DatabaseField
    private String lastName;
    @DatabaseField
    private String mobileNumber;
    @DatabaseField
    private String emailAddress;
    @DatabaseField
    private String address;
    @DatabaseField
    private String imagePath;

    public Friend(String firstName, String lastName, String mobileNumber, String emailAddress, String address, String imagePath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.emailAddress = emailAddress;
        this.address = address;
        this.imagePath = imagePath;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
