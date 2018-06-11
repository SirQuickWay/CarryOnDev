package com.project.carryon.carryon.GeneralClasses;

public class Address {
    private String addressID;
    private String zipCode;
    private String country;
    private String city;
    private String street;
    private double longitude;
    private double latitude;

    public Address(String zipCode, String country, String city, String street, double longitude, double latitude) {
        this.addressID = null;
        this.zipCode = zipCode;
        this.country = country;
        this.city = city;
        this.street = street;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public  Address()
    {}

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
