package com.example.qrmonsters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 * The Player class represents a player of the QR Monsters game.
 */

public class Player {
    private String userId;
    private String username;
    private String email;
    private String phoneNumber;
    private ArrayList<String> qrCodes;
    private int TotalScore;
    private int highestIndividualScore;
    private int numQRCodesScanned;

    private ArrayList<Integer> qrScores = new ArrayList<>();
    //private List<String> qrCodes = new List<String>;
    /**
     * Default constructor required for Firebase Database.
     */
    public Player() {
        // Required empty constructor for Firebase Database
    }
    public Player(String userId, String username, String email, String phoneNumber, ArrayList
                  qrCodes) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.qrCodes = qrCodes;

    }

    /**
     * Constructs a new Player object.
     *
     * @param userId the user ID of the player
     * @param username the username of the player
     * @param email the email address of the player
     * @param phoneNumber the phone number of the player
     */
    public Player(String userId, String username, String email, String phoneNumber) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.qrCodes = new ArrayList<>();
        this.qrScores = new ArrayList<>();
    }
    /**
     * Returns the user ID of the player.
     *
     * @return the user ID of the player
     */
    public String getUserId() {
        return userId;
    }
    /**
     * Returns the username of the player.
     *
     * @return the username of the player
     */
    public String getUsername() {
        return username;
    }
    /**
     * Returns the email address of the player.
     *
     * @return the email address of the player
     */
    public String getEmail() {
        return email;
    }
    /**
     * Returns the phone number of the player.
     *
     * @return the phone number of the player
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    /**
     * Returns the list of QR codes owned by the player.
     *
     * @return the list of QR codes owned by the player
     */
    public ArrayList<String> getQrCodes() {
        return qrCodes;
    }
    /**
     * Sets the user ID of the player.
     *
     * @param userId the user ID of the player
     */

    public void setUserId(String userId) {
        this.userId = userId;
    }
    /**
     * Sets the username of the player.
     *
     * @param username the username of the player
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * Sets the email address of the player.
     *
     * @param email the email address of the player
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Sets the phone number of the player.
     *
     * @param phoneNumber the phone number of the player
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    /**
     * Adds a new QR code to the player's list of owned QR codes.
     *
     * @param qrCodeName the name of the QR code to add
     */
    public void addQRCode(String qrCodeName){

        this.qrCodes.add(qrCodeName);
    }
    /**
     * Removes a QR code from the player's list of owned QR codes.
     *
     * @param qrCodeName the name of the QR code to remove
     */
    public void removeQRCode(String qrCodeName){

        this.qrCodes.remove(qrCodeName);

    }

    public int getTotalScore() {
        return TotalScore;
    }
    
    public void setTotalScore(int TotalScore) {
        this.TotalScore = TotalScore;
    }

    public int getHighestIndividualScore() {
        return highestIndividualScore;
    }

    public void setHighestIndividualScore(int highestIndividualScore) {
        this.highestIndividualScore = highestIndividualScore;
    }

    public int getNumQRCodesScanned() {
        return numQRCodesScanned;
    }

    public void setNumQRCodesScanned(int numQRCodesScanned) {
        this.numQRCodesScanned = numQRCodesScanned;
    }

    public List<Integer> getQrScores() {
        return qrScores;
    }

    public void setQrScores(List<Integer> qrScores) {
        this.qrScores = (ArrayList<Integer>) qrScores;
    }

    public void addQRCode(String qrCodeName, int qrCodeScore) {
        this.qrCodes.add(qrCodeName);
        this.qrScores.add(qrCodeScore);
    }

}
