package com.muhammedkalender.pocketpassword.Models;

public class PasswordModel {
    private String name;
    private String encryptedPassword;
    private String color;

    public PasswordModel(String name, String encryptedPassword, String color) {
        this.name = name;
        this.encryptedPassword = encryptedPassword;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public String getColor() {
        return color;
    }
}
