package com.example.qrmonsters;

import android.location.Location;

public class QRCode {

    private String codeName;
    private String codeHash;
    private Integer codeScore;
    private Location codeLocation;

    public QRCode(String codeName, String codeHash, Integer codeScore, Location codeLocation) {
        this.codeName = codeName;
        this.codeHash = codeHash;
        this.codeScore = codeScore;
        this.codeLocation = codeLocation;
    }

    public QRCode(String codeName, String codeHash, Integer codeScore) {
        this.codeName = codeName;
        this.codeHash = codeHash;
        this.codeScore = codeScore;
        this.codeLocation = null;
    }

    public String getCodeName() {
        return codeName;
    }

    public String getCodeHash() {
        return codeHash;
    }

    public Integer getCodeScore() {
        return codeScore;
    }

    public Location getCodeLocation() {
        return codeLocation;
    }
}
