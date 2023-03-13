package com.example.qrmonsters;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import android.location.Location;

import java.util.Optional;

public class qrTest {

    private QRCodeObject mockQR(){

        Location newLoc = new Location("");

        QRCodeObject newQR = new QRCodeObject("restika",
                "4e001a69624c883f3a3d00064eef5d5102b2a4823cf6f8682857de07a6f9e16b",
                22, newLoc);

        return newQR;

    }

    @Test
    void testUserName(){

        QRCodeObject test = mockQR();
        assertEquals("restika", test.getCodeName());

    }

    @Test
    void testUserHash(){

        QRCodeObject test = mockQR();
        assertEquals("4e001a69624c883f3a3d00064eef5d5102b2a4823cf6f8682857de07a6f9e16b"
                , test.getCodeHash());

    }

    @Test
    void testUserScore(){

        QRCodeObject test = mockQR();
        assertEquals(Optional.of(22), Optional.of(test.getCodeScore()));

    }

}
