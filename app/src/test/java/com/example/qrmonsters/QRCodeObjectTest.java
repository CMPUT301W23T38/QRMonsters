package com.example.qrmonsters;


import android.location.Location;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

public class QRCodeObjectTest {

    @Test
    public void testGetCodeName() {
        QRCodeObject qrCodeObject = new QRCodeObject("Test QR Code", "1234567890", 100);
        assertEquals("Test QR Code", qrCodeObject.getCodeName());
    }

    @Test
    public void testGetCodeHash() {
        QRCodeObject qrCodeObject = new QRCodeObject("Test QR Code", "1234567890", 100);
        assertEquals("1234567890", qrCodeObject.getCodeHash());
    }

    @Test
    public void testGetCodeScore() {
        QRCodeObject qrCodeObject = new QRCodeObject("Test QR Code", "1234567890", 100);
        assertEquals(Integer.valueOf(100), qrCodeObject.getCodeScore());
    }

    @Test
    public void testGetComments() {
        QRCodeObject qrCodeObject = new QRCodeObject("Test QR Code", "1234567890", 100);
        HashMap<String, String> comments = new HashMap<>();
        comments.put("comment1", "comment1");
        qrCodeObject.setComments(comments);
        assertEquals(comments, qrCodeObject.getComments());
    }

    @Test
    public void testAddComment() {
        QRCodeObject qrCodeObject = new QRCodeObject("Test QR Code", "1234567890", 100);
        qrCodeObject.addComment("Test Comment");
        assertTrue(qrCodeObject.getComments().containsKey("Test Comment"));
    }

    @Test
    public void testRemoveComment() {
        QRCodeObject qrCodeObject = new QRCodeObject("Test QR Code", "1234567890", 100);
        qrCodeObject.addComment("Test Comment");
        qrCodeObject.removeComment("Test Comment");
        assertFalse(qrCodeObject.getComments().containsKey("Test Comment"));
    }

    @Test
    public void testToString() {
        QRCodeObject qrCodeObject = new QRCodeObject("Test QR Code", "1234567890", 100);
        String expectedString = "QRCodeObject{codeName='Test QR Code', codeHash='1234567890', codeScore=100, codeLocation=null, comments={}}";
        assertEquals(expectedString, qrCodeObject.toString());
    }
}

