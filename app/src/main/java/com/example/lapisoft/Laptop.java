package com.example.lapisoft;

public class Laptop {
    private String model;
    private String serialNumber;
    private String colour;
    private String screenSize;
    private String user;
    private String userStudentID;
    private String userPhone;
    private String userResidence;

    public Laptop(String model, String serialNumber, String colour, String screenSize, String user, String userStudentID, String userPhone, String userResidence) {
        this.model = model;
        this.serialNumber = serialNumber;
        this.colour = colour;
        this.screenSize = screenSize;
        this.user = user;
        this.userStudentID = userStudentID;
        this.userPhone = userPhone;
        this.userResidence = userResidence;
    }

    public Laptop(){

    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getscreenSize() {
        return screenSize;
    }

    public void setscreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

    public String getuser() {
        return user;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserResidence() {
        return userResidence;
    }

    public void setuser(String user) {
        this.user = user;
    }

    public String getuserStudentID() {
        return userStudentID;
    }

    public void setuserStudentID(String userStudentID) {
        this.userStudentID = userStudentID;
    }
}





