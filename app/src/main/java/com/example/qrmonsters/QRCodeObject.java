package com.example.qrmonsters;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
/**

 The QRCodeObject class represents a QR code object that contains information such as its name, hash, score, and location.
 */
public class QRCodeObject implements Parcelable {
    // Private fields for storing QR code properties
    private String codeName;
    private String codeHash;
    private Integer codeScore;
    private Location codeLocation;

    private HashMap<String, String> comments;
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

        this.comments = new HashMap<>();
    }
    public QRCodeObject(String codeName, String codeHash, Integer codeScore, Location codeLocation, HashMap<String, String> comments) {
        this.codeName = codeName;
        this.codeHash = codeHash;
        this.codeScore = codeScore;
        this.codeLocation = codeLocation;
        this.comments = comments;
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

        this.comments = new HashMap<String, String>();
    }

    public QRCodeObject() {
        // Required empty constructor for Firebase Database
    }

    protected QRCodeObject(Parcel in) {
        codeName = in.readString();
        codeHash = in.readString();
        codeScore = in.readInt();
        codeLocation = in.readParcelable(Location.class.getClassLoader());
        comments = in.readHashMap(String.class.getClassLoader());
    }

    public static final Creator<QRCodeObject> CREATOR = new Creator<QRCodeObject>() {
        @Override
        public QRCodeObject createFromParcel(Parcel in) {
            return new QRCodeObject(in);
        }

        @Override
        public QRCodeObject[] newArray(int size) {
            return new QRCodeObject[size];
        }
    };

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


    /**
     * Returns the comments of the QR code.
     * @return the comments of the QR code
     */
    public HashMap<String, String> getComments() {
        return comments;
    }

    /**
     * Sets the comments of the QR code.
     * @param comments the comments of the QR code
     */
    public void setComments(HashMap<String, String> comments) {
        this.comments = comments;
    }

    /**
     * Adds a comment to the QR code.
     * @param comment the comment to add to the QR code
     */
    public void addComment(String user, String comment) {
        comments.put(user, comment);
    }

    /**
     * Removes a comment from the QR code.
     * @param comment the comment to remove from the QR code
     */
    public void removeComment(String comment) {
        comments.remove(comment);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(codeName);
        dest.writeString(codeHash);
        dest.writeInt(codeScore);
        dest.writeParcelable(codeLocation, flags);
        dest.writeMap(comments);
    }

    /**
     * Returns the string representation of the QR code.
     * @return the string representation of the QR code
     */
    @NonNull
    @Override
    public String toString() {
        return "QRCodeObject{" +
                "codeName='" + codeName + '\'' +
                ", codeHash='" + codeHash + '\'' +
                ", codeScore=" + codeScore +
                ", codeLocation=" + codeLocation +
                ", comments=" + comments +
                '}';
    }

}
