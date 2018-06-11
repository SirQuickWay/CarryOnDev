package com.project.carryon.carryon.GeneralClasses;

import java.util.Date;

/**
 * Created by matte on 18/05/2018.
 */

public class Delivery {
    private String deliveryID;
    private String deliveryPathID;
    private String senderID;
    private String receiverID;
    private String carrierID;
    private String creatorID;
    private int status;   //0 prima di QRcode1; 1 dopo QRcode1 (pacco preso); 2 dopo QRcode2 (pacco consegnato)
    private Boolean user2Confirmed;
    private Boolean carrierConfirmed;
    private String packageContent;
    private double packageDim1;
    private double packageDim2;
    private double packageDim3;
    private double packageWeight;
    private double price;
    private long creationDate;        //Momento in cui tutti hanno accettato
    private long pickUpDate;         //Date Vere in cui le cose succedono. Data Delivery programmata si ricava dal Path
    private long receivedDate;
    private String pickedUpQRCode;
    private String deliveredQRCode;
    //QR codes da aggiungere.
    //l'indirizzo si ricava dagli users.

    public Delivery( String deliveryPathID, String senderID, String receiverID, String carrierID, String creatorID, String packageContent, double packageDim1, double packageDim2, double packageDim3, double packageWeight, double price, long creationDate) {
        this.deliveryID = null; //da inserire una volta creato dal database
        this.deliveryPathID = deliveryPathID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.carrierID = carrierID;
        this.creatorID = creatorID;
        this.status = 0;
        this.user2Confirmed = false;
        this.carrierConfirmed = false;
        this.packageContent = packageContent;
        this.packageDim1 = packageDim1;
        this.packageDim2 = packageDim2;
        this.packageDim3 = packageDim3;
        this.packageWeight = packageWeight;
        this.price = price;
        this.creationDate = creationDate;
        this.pickUpDate = 0;
        this.receivedDate = 0;
    }
    public Delivery(){}

    public String getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(String deliveryID) {
        this.deliveryID = deliveryID;
    }

    public String getDeliveryPathID() {
        return deliveryPathID;
    }

    public void setDeliveryPathID(String deliveryPathID) {
        this.deliveryPathID = deliveryPathID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getCarrierID() {
        return carrierID;
    }

    public void setCarrierID(String carrierID) {
        this.carrierID = carrierID;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Boolean getUser2Confirmed() {
        return user2Confirmed;
    }

    public void setUser2Confirmed(Boolean user2Confirmed) {
        this.user2Confirmed = user2Confirmed;
    }

    public Boolean getCarrierConfirmed() {
        return carrierConfirmed;
    }

    public void setCarrierConfirmed(Boolean carrierConfirmed) {
        this.carrierConfirmed = carrierConfirmed;
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

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public long getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(long pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public long getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(long receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getPickedUpQRCode() {
        return pickedUpQRCode;
    }

    public String getDeliveredQRCode() {
        return deliveredQRCode;
    }

    public void setPickedUpQRCode(String pickedUpQRCode) {
        this.pickedUpQRCode = pickedUpQRCode;
    }

    public void setDeliveredQRCode(String deliveredUpQRCode) {
        this.deliveredQRCode = deliveredUpQRCode;
    }
}