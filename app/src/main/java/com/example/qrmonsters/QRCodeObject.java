package com.example.qrmonsters;

import android.location.Location;
/**

 The QRCodeObject class represents a QR code object that contains information such as its name, hash, score, and location.
 */
public class QRCodeObject {
    // Private fields for storing QR code properties
    private String codeName;
    private String codeHash;
    private Integer codeScore;
    private Location codeLocation;
    /**

     Constructs a QRCodeObject with the given name, hash, score, and location.
     @param codeName the name of the QR code
     @param codeHash the hash of the QR code
     @param codeScore the score of the QR code
     @param codeLocation the location of the QR code
     */
    public QRCodeObject(String codeName, String codeHash, Integer codeScore, Location codeLocation) {
        this.codeName = codeName;
        this.codeHash = codeHash;
        this.codeScore = codeScore;
        this.codeLocation = codeLocation;
    }
    /**

     Constructs a QRCodeObject with the given name, hash, and score (without a loaction).
     @param codeName the name of the QR code
     @param codeHash the hash of the QR code
     @param codeScore the score of the QR code
     */
    public QRCodeObject(String codeName, String codeHash, Integer codeScore) {
        this.codeName = codeName;
        this.codeHash = codeHash;
        this.codeScore = codeScore;
        this.codeLocation = null;
    }
    /**

     Returns the name of the QR code.
     @return the name of the QR code
     */
    public String getCodeName() {
        return codeName;
    }
    /**

     Returns the hash of the QR code.
     @return the hash of the QR code
     */

    public String getCodeHash() {
        return codeHash;
    }
    /**
     Returns the score of the QR code.
     @return the score of the QR code
     */
    public Integer getCodeScore() {
        return codeScore;
    }
    /**
     Returns the location of the QR code.
     @return the location of the QR code
     */
    public Location getCodeLocation() {
        return codeLocation;
    }
}
