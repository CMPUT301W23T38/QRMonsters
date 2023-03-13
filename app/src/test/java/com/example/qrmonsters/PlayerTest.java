package com.example.qrmonsters;



import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

public class PlayerTest {
    @Test
    public void testAddQRCode() {
        Player player = new Player("123", "johndoe", "johndoe@example.com", "1234567890");
        player.addQRCode("qr1");
        player.addQRCode("qr2");
        ArrayList<String> expectedQRList = new ArrayList<>();
        expectedQRList.add("qr1");
        expectedQRList.add("qr2");
        assertEquals(expectedQRList, player.getQrCodes());
    }

    @Test
    public void testRemoveQRCode() {
        Player player = new Player("123", "johndoe", "johndoe@example.com", "1234567890");
        player.addQRCode("qr1");
        player.addQRCode("qr2");
        player.removeQRCode("qr1");
        ArrayList<String> expectedQRList = new ArrayList<>();
        expectedQRList.add("qr2");
        assertEquals(expectedQRList, player.getQrCodes());
    }

    @Test
    public void testGetters() {
        Player player = new Player("123", "johndoe", "johndoe@example.com", "1234567890");
        assertEquals("123", player.getUserId());
        assertEquals("johndoe", player.getUsername());
        assertEquals("johndoe@example.com", player.getEmail());
        assertEquals("1234567890", player.getPhoneNumber());
        assertEquals(new ArrayList<>(), player.getQrCodes());
    }

    @Test
    public void testSetters() {
        Player player = new Player("123", "johndoe", "johndoe@example.com", "1234567890");
        player.setUserId("456");
        player.setUsername("janedoe");
        player.setEmail("janedoe@example.com");
        player.setPhoneNumber("9876543210");
        ArrayList<String> qrCodes = new ArrayList<>();
        qrCodes.add("qr1");
        qrCodes.add("qr2");
        assertEquals("456", player.getUserId());
        assertEquals("janedoe", player.getUsername());
        assertEquals("janedoe@example.com", player.getEmail());
        assertEquals("9876543210", player.getPhoneNumber());
    }
}


