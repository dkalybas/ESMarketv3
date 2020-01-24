package com.example.esmarket.Model;

public class Refferals {

    private String UserPhone;
    private String refferalNumber;


    public Refferals(){

    }


    public Refferals(String userPhone, String refferalNumber) {
        UserPhone = userPhone;
        this.refferalNumber = refferalNumber;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getRefferalNumber() {
        return refferalNumber;
    }

    public void setRefferalNumber(String refferalNumber) {
        this.refferalNumber = refferalNumber;
    }
}
