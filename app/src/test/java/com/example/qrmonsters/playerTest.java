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
    void testUser(){

        Player test = mockPLayer();
        assertEquals("testPlayer", test.getUsername());

    }

}
