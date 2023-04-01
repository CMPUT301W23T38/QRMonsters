package com.example.qrmonsters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class textToAsciiNumbersTest {

    @Test
    public void testGetAscii() {
        Assertions.assertEquals("00000\n0   0\n00000", textToAsciiNumbers.getAscii('0'));
        Assertions.assertEquals("  1  \n  1  \n  1  ", textToAsciiNumbers.getAscii('1'));
        Assertions.assertEquals("22222\n  2  \n22222", textToAsciiNumbers.getAscii('2'));
        Assertions.assertEquals("33333\n  333\n33333", textToAsciiNumbers.getAscii('3'));
        Assertions.assertEquals("   4 \n44444\n   4 ", textToAsciiNumbers.getAscii('4'));
        Assertions.assertEquals("55555\n55555\n55555", textToAsciiNumbers.getAscii('5'));
        Assertions.assertEquals("666  \n6666 \n66666", textToAsciiNumbers.getAscii('6'));
        Assertions.assertEquals("77777\n  7  \n7    ", textToAsciiNumbers.getAscii('7'));
        Assertions.assertEquals("88888\n8 8 8\n88888", textToAsciiNumbers.getAscii('8'));
        Assertions.assertEquals("99999\n 9999\n   99", textToAsciiNumbers.getAscii('9'));
    }

    @Test
    public void testGetAsciiInvalid() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            textToAsciiNumbers.getAscii('a');
        });
    }
}
