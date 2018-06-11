package com.project.carryon.carryon.GeneralClasses;



public class Path {
    private String pathID;
    private String carrierID;
    private double range;      //in km
    private Means means;
    private long departureDate;  //include data e ora di partenza programmata (timestamp)
    private long estimatedTime; //tempo stimato di percorrenza in *secondi*
    private String departureAddressID;
    private String arrivalAddressID;

    public Path(String carrierID, double range, Means means, long departureDate, long estimatedTime, String departureAddressID, String arrivalAddressID) {
        this.pathID = null;
        this.carrierID = carrierID;
        this.range = range;
        this.means = means;
        this.departureDate = departureDate;
        this.estimatedTime = estimatedTime;
        this.departureAddressID = departureAddressID;
        this.arrivalAddressID = arrivalAddressID;
    }
    public Path(){}

    public String getPathID() {
        return pathID;
    }

    public void setPathID(String pathID) {
        this.pathID = pathID;
    }

    public String getCarrierID() {
        return carrierID;
    }

    public void setCarrierID(String carrierID) {
        this.carrierID = carrierID;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public Means getMeans() {
        return means;
    }

    public void setMeans(Means means) {
        this.means = means;
    }

    public long getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(long departureDate) {
        this.departureDate = departureDate;
    }

    public long getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(long estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getDepartureAddressID() {
        return departureAddressID;
    }

    public void setDepartureAddressID(String departureAddressID) {
        this.departureAddressID = departureAddressID;
    }

    public String getArrivalAddressID() {
        return arrivalAddressID;
    }

    public void setArrivalAddressID(String arrivalAddressID) {
        this.arrivalAddressID = arrivalAddressID;
    }
}
