package com.example.qrmonsters;

import java.util.List;

public class Player {
    private String userId;
    private String username;
    private String email;
    private String phoneNumber;
    private List<String> qrCodes;

    public Player() {
        // Required empty constructor for Firebase Database
    }

    public Player(String userId, String username, String email, String phoneNumber) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<String> getQrCodes() {
        return qrCodes;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void addQRCode(String qrCodeName){

        this.qrCodes.add(qrCodeName);
    }

    public void removeQRCode(String qrCodeName){

        this.qrCodes.remove(qrCodeName);

    }

}
