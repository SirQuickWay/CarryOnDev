package com.project.carryon.carryon.GeneralClasses;

import java.util.Date;

/**
 * Created by matte on 18/05/2018.
 */

public class Delivery {
    private String deliveryID;
    private String pacakgeDimension;
    private double packageWeight;
    private double price;
    private User sender;
    private User receiver;
    private User carrier;
    private Date pickUpDate;
    private Date deliveryDate;

    public Delivery(String deliveryID, String pacakgeDimension, double packageWeight, double price, User sender, User receiver, User carrier, Date pickUpDate, Date deliveryDate, int status) {
        this.deliveryID = deliveryID;
        this.pacakgeDimension = pacakgeDimension;
        this.packageWeight = packageWeight;
        this.price = price;
        this.sender = sender;
        this.receiver = receiver;
        this.carrier = carrier;
        this.pickUpDate = pickUpDate;
        this.deliveryDate = deliveryDate;
        this.status = status;
    }

    private int status;

    public Delivery(String deliveryID, String pacakgeDimension, double packageWeight, double price, User sender, User receiver, User carrier, Date pickUpDate, Date deliveryDate) {
        this.deliveryID = deliveryID;
        this.pacakgeDimension = pacakgeDimension;
        this.packageWeight = packageWeight;
        this.price = price;
        this.sender = sender;
        this.receiver = receiver;
        this.carrier = carrier;
        this.pickUpDate = pickUpDate;
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryID() {
        return deliveryID;
    }

    public String getPacakgeDimension() {
        return pacakgeDimension;
    }

    public double getPackageWeight() {
        return packageWeight;
    }

    public double getPrice() {
        return price;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public User getCarrier() {
        return carrier;
    }

    public Date getPickUpDate() {
        return pickUpDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public int getStatus() {
        return status;
    }
}
