package com.example.esmarket.Model;

public class User {

    private  String Name ;
    private  String Password;
    private String Phone ;
    private String Email;
    private String HomeAddress;
    private boolean Coupon;
    private boolean CouponExchanged;


    public User() {

    }


    public User(String name, String password,String homeAddress,String email ,boolean coupon,
                boolean couponExchanged) {
        Name = name;
        Password = password;
        HomeAddress=homeAddress;
        Email = email;
        Coupon=coupon;
        CouponExchanged=couponExchanged;

    }



    public User(String name, String password, String phone, String email) {
        Name = name;
        Password = password;
        Phone = phone;
        Email = email;
    }

    public boolean isCouponExchanged() {
        return CouponExchanged;
    }

    public void setCouponExchanged(boolean couponExchanged) {
        CouponExchanged = couponExchanged;
    }

    public boolean isCoupon() {
        return Coupon;
    }

    public void setCoupon(boolean coupon) {
        Coupon = coupon;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getHomeAddress() {
        return HomeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        HomeAddress = homeAddress;
    }
}
