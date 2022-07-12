package com.example.ahar.Model;

public class DonateFoodItemList {
    private String image,restaurantName,foodDes,startTime,endTime,date,consumePeople,restaurantAddress,approxPrice,donateid,deliveryAddress,status;

    public DonateFoodItemList() {
    }

    public DonateFoodItemList(String image, String restaurantName, String foodDes, String startTime, String endTime, String date, String consumePeople, String restaurantAddress, String approxPrice,String donateid) {
        this.image = image;
        this.restaurantName = restaurantName;
        this.foodDes = foodDes;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.consumePeople = consumePeople;
        this.restaurantAddress = restaurantAddress;
        this.approxPrice = approxPrice;
        this.donateid = donateid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getFoodDes() {
        return foodDes;
    }

    public void setFoodDes(String foodDes) {
        this.foodDes = foodDes;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getConsumePeople() {
        return consumePeople;
    }

    public void setConsumePeople(String consumePeople) {
        this.consumePeople = consumePeople;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getApproxPrice() {
        return approxPrice;
    }

    public void setApproxPrice(String approxPrice) {
        this.approxPrice = approxPrice;
    }

    public String getDonateid() {
        return donateid;
    }

    public void setDonateid(String donateid) {
        this.donateid = donateid;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
