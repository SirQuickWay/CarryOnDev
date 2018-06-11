package com.project.carryon.carryon.GeneralClasses;


/**
 * Created by matte on 15/04/2018.
 */

public class User {
    private String userID;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String phoneNumber;
    private String mainAddressID;

    public User(String name, String surname, String username, String email, String phoneNumber, String mainAddressID) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.mainAddressID = mainAddressID;
    }
    public User(){}

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMainAddressID() {
        return mainAddressID;
    }

    public void setMainAddressID(String mainAddressID) {
        this.mainAddressID = mainAddressID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
