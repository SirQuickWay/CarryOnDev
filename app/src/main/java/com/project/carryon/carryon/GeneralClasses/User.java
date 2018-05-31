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
    private String password;
    private String phoneNumber;
    private String mainAddressID;

    public User(String name, String surname, String username, String email, String password, String phoneNumber, String mainAddressID) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.mainAddressID = mainAddressID;
    }


    public String getuserID() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
