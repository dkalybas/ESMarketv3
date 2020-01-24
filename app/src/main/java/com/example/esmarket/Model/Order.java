package com.example.esmarket.Model;

public class Order {


    private String UserPhone;
    private String ProductId;
    private String ProductName;
    private String Price;
    private String Quantity;
    private String Amount;
    private String Image;

    public Order(){

    }

    public Order(String userPhone, String productId, String productName, String price, String quantity, String amount, String image) {
        UserPhone = userPhone;
        ProductId = productId;
        ProductName = productName;
        Price = price;
        Quantity = quantity;
        Amount = amount;
        Image = image;
    }


    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
