package com.bitespeed.backendTask.model;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Component
public class RequestContactInfoModel implements Serializable {


    private String email;


    private String phoneNumber;

    public RequestContactInfoModel(String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public RequestContactInfoModel(){};

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "RequestContactInfoModel{" +
                "email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
