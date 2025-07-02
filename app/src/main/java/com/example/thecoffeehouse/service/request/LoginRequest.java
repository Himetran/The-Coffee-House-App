package com.example.thecoffeehouse.service.request;

public class LoginRequest {

    private String phone;

    private String password;

    public LoginRequest() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
