package com.example.kot;

import java.util.Random;

public class Details {
    private int amount, sitNumber;
    private String username, destination, departure, busNum,code;

    //SET
    public void setAmount(int fare) {
        amount = fare;
    }
    public void setSitNumber(int sit) {
        sitNumber = sit;
    }
    public  void setUsername(String name) {
        username = name;
    }
    public void setDestination(String dest) {
        destination = dest;
    }
    public void setBusNum(String num) {
        busNum = num;
    }
    public void setDeparture(String depart){departure=depart;};
    public void setCode(String kode){code = kode;};
    //GET
    public String getAmount() {
        String fare = Integer.toString(amount);
        return fare;
    }
    public String getSitNumber() {
        return "Sit: " + sitNumber;
    }
    public String getUsername() {
        return username;
    }
    public String getDestination() {
        return destination;
    }
    public String getDeparture() {return  departure;};
    public String getBusNum() {
        return busNum;
    }
    public String getCode() {return code;}
    public String OutputConfirm() {
        return ("Dest: " + getDestination() + "\nBus: " + getBusNum() + "\n" + getSitNumber() + "\nKSH." + getAmount());
    }
    public String generateRandomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomCode = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            randomCode.append(randomChar);
        }

        return randomCode.toString();
    }
}
