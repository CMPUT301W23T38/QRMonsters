package com.example.qrmonsters;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class textToAsciiLettersTest {



        @Test
        public void testGetAscii() {
            // Test for letter B
            String expectedB = "*****\n*   *\n*****";
            assertEquals(expectedB, textToAsciiLetters.getAscii('B', ' '));
            assertEquals(expectedB.replace('*', '$'), textToAsciiLetters.getAscii('B', '$'));

            // Test for letter C
            String expectedC = " ****\n*    \n ****";
            assertEquals(expectedC, textToAsciiLetters.getAscii('C', ' '));
            assertEquals(expectedC.replace('*', '#'), textToAsciiLetters.getAscii('C', '#'));

            // Test for letter D
            String expectedD = "**** \n*   *\n**** ";
            assertEquals(expectedD, textToAsciiLetters.getAscii('D', ' '));
            assertEquals(expectedD.replace('*', '@'), textToAsciiLetters.getAscii('D', '@'));

            // Test for letter F
            String expectedF = "*****\n***  \n**   ";
            assertEquals(expectedF, textToAsciiLetters.getAscii('F', ' '));
            assertEquals(expectedF.replace('*', '%'), textToAsciiLetters.getAscii('F', '%'));

            // Test for unexpected value
            try {
                textToAsciiLetters.getAscii('G', ' ');
            } catch (IllegalStateException e) {
                assertEquals("Unexpected value: G", e.getMessage());
            }
        }

}
    

