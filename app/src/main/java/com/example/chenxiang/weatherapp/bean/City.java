package com.example.chenxiang.weatherapp.bean;

/**
 * Created by chenxiang on 2016/12/27.
 */

public class City {
    private String province;
    private String city;
    private String number;
    private String firstPY;
    private String allPY;
    private String allFirstPY;

    @Override
    public String toString() {
        return "City{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", number='" + number + '\'' +
                ", firstPY='" + firstPY + '\'' +
                ", allPY='" + allPY + '\'' +
                ", allFirstPY='" + allFirstPY + '\'' +
                '}';
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getNumber() {
        return number;
    }

    public String getFirstPY() {
        return firstPY;
    }

    public String getAllPY() {
        return allPY;
    }

    public String getAllFirstPY() {
        return allFirstPY;
    }

    public void setProvince(String province) {

        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setFirstPY(String firstPY) {
        this.firstPY = firstPY;
    }

    public void setAllPY(String allPY) {
        this.allPY = allPY;
    }

    public void setAllFirstPY(String allFirstPY) {
        this.allFirstPY = allFirstPY;
    }

    public City(String province, String city, String number, String firstPY, String allPY, String allFirstPY) {

        this.province = province;
        this.city = city;
        this.number = number;
        this.firstPY = firstPY;
        this.allPY = allPY;
        this.allFirstPY = allFirstPY;
    }
}
