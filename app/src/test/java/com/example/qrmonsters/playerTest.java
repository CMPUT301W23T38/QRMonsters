package com.example.qrmonsters;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class playerTest {

    private Player mockPLayer(){

        Player newPLayer = new Player
                ("fdafeadfad","testPlayer", "test@c.c"
                        , "43343113");

        return newPLayer;

    }

    @Test
    void testUserName(){

        Player test = mockPLayer();
        assertEquals("testPlayer", test.getUsername());

    }

    @Test
    void testUserID(){

        Player test = mockPLayer();
        assertEquals("fdafeadfad", test.getUserId());

    }

    @Test
    void testUserEmail(){

        Player test = mockPLayer();
        assertEquals("test@c.c", test.getEmail());

    }

    @Test
    void testUserPhone(){

        Player test = mockPLayer();
        assertEquals("43343113", test.getPhoneNumber());

    }

    void testUserQRList(){

        Player test = mockPLayer();
        test.addQRCode("new_qr");

        assertTrue(test.getQrCodes().contains("new_qr"));

    }

}
