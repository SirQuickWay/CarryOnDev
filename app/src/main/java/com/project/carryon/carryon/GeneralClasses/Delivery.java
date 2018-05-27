package com.project.carryon.carryon.GeneralClasses;

import java.util.Date;

/**
 * Created by matte on 18/05/2018.
 */

public class Delivery {
    private String deliveryID;
    //private Path deliveryPath;
    private User sender;
    private User receiver;
    private User carrier;
    private User creator;
    private int status;
    private Boolean deliveryComplete;
    private String packageContent;
    private double packageDim1;
    private double packageDim2;
    private double packageDim3;
    private double packageWeight;
    private double price;
    private Date creationDate;        //Momento in cui tutti hanno accettato
    private Date pickUpDate;         //Date Vere in cui le cose succedono. Data Delivery programmata si ricava dal Path
    private Date receivedDate;
    //QR codes da aggiungere.
    //l'indirizzo si ricava dagli users.

    public Delivery(String deliveryID, User creator, int status, Boolean deliveryComplete, String packageContent, double packageDim1, double packageDim2, double packageDim3, double packageWeight) {
        this.deliveryID = deliveryID;
        this.creator = creator;
        this.status = status;
        this.deliveryComplete = deliveryComplete;
        this.packageContent = packageContent;
        this.packageDim1 = packageDim1;
        this.packageDim2 = packageDim2;
        this.packageDim3 = packageDim3;
        this.packageWeight = packageWeight;
    }

    public String getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(String deliveryID) {
        this.deliveryID = deliveryID;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getCarrier() {
        return carrier;
    }

    public void setCarrier(User carrier) {
        this.carrier = carrier;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Boolean getDeliveryComplete() {
        return deliveryComplete;
    }

    public void setDeliveryComplete(Boolean deliveryComplete) {
        this.deliveryComplete = deliveryComplete;
    }

    public String getPackageContent() {
        return packageContent;
    }

    public void setPackageContent(String packageContent) {
        this.packageContent = packageContent;
    }

    public double getPackageDim1() {
        return packageDim1;
    }

    public void setPackageDim1(double packageDim1) {
        this.packageDim1 = packageDim1;
    }

    public double getPackageDim2() {
        return packageDim2;
    }

    public void setPackageDim2(double packageDim2) {
        this.packageDim2 = packageDim2;
    }

    public double getPackageDim3() {
        return packageDim3;
    }

    public void setPackageDim3(double packageDim3) {
        this.packageDim3 = packageDim3;
    }

    public double getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(double packageWeight) {
        this.packageWeight = packageWeight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(Date pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }
}