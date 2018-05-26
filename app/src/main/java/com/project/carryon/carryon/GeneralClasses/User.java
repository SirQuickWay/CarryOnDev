package com.project.carryon.carryon.GeneralClasses;


/**
 * Created by matte on 15/04/2018.
 */

public class User {
    private String UID;
    private String name;
    private String surname;
    private String username;
    private String email;

    //ADD: password, phoneNumber, Address

    public User(String UID, String name, String surname, String username, String email) {
        this.UID = UID;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
    }

    public String getUID() {
        return UID;
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

//others fields

}
