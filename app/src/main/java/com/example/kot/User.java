package com.example.kot;

public class User {
    private String username, fullname, email, phone;

    public User(String username, String fullname, String email, String phone) {
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname(){ return fullname;}

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
