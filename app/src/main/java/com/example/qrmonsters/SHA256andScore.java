package com.example.qrmonsters;

import static java.lang.Character.isDigit;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 The User class represents a user who has a host name and provides static methods for generating a
 SHA-256 hash of a string and calculating a score for a string.
 */
public class SHA256andScore {
    String host_name;
    /**
     * Returns the SHA-256 hash of a given string.
     * @param str the string to be hashed
     * @return the SHA-256 hash of the input string
     */
    public static String getSha256Str(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            encodeStr = byte2Hex(messageDigest.digest());

            // encodeStr is the AFTER HASHED value, which is what you want for QR code
            // usage for this, somethingHASHED = somethingBEFORE.getSha256Str();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }
    public static int getScore(String str) {
        int i = 1;
        int sum = 0;
        int count = 1;

        for(i = 1; i<str.length();i++) {
            if (str.charAt(i)==str.charAt(i-1)) {
                count ++;
            }else{
                if(count == 1) {
                    continue;
                }
                if(isDigit(str.charAt(i-1))) {
                    int x = str.charAt(i-1)-'0';
                    sum += Math.pow(x,(count-1));
                }else{
                    int x = str.charAt(i-1)-'a'+10;
                    sum += Math.pow(x,(count-1));
                }
                count = 1;

            }
        }
        if (count != 1) {
            if(isDigit(str.charAt(i-1))) {
                int x = str.charAt(i-1)-'0';
                sum += Math.pow(x,(count-1));
            }else{
                int x = str.charAt(i-1)-'a'+10;
                sum += Math.pow(x,(count-1));
            }
        }
        return sum;
    }
    /**
     * Converts an array of bytes to a hex string.
     * @param bytes the byte array to be converted
     * @return the hex string representation of the input byte array
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                stringBuilder.append("0");
            }
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }

    public static Boolean isVowel(char isV){

        switch (isV){

            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
            case 'A':
            case 'E':
            case 'I':
            case 'O':
            case 'U':
                return true;

            default:
                return false;

        }

    }

    public static String generateName (String hashCode){

        char[] hashC= hashCode.toCharArray();
        
        String theConstonants = "";
        String theVowels = "";
        String theNumbers = "";
        String finalName = "";

        for (char letter: hashC) {

            if (Character.isLetter(letter)){

                if(!isVowel(letter)){

                    theConstonants = theConstonants + letter;

                }

            }
            if (theConstonants.length() == 4){
                break;
            }
        }

        theConstonants = theConstonants.toUpperCase(Locale.ROOT);

        for (char letter: hashC) {
            if (Character.isLetter(letter)){
                if(isVowel(letter)){
                    theVowels = theVowels + letter;
                }
            }
            if (theVowels.length() == 4){
                break;
            }
        }

        theVowels = theVowels.toLowerCase(Locale.ROOT);

        for (char letter: hashC) {
            if (Character.isDigit(letter)){
                theNumbers = theNumbers + letter;
            }
            if (theNumbers.length() == 3){
                break;
            }
        }

        for (int i = 0; i < 4; i++) {
            finalName = finalName + theConstonants.charAt(i);
            finalName = finalName + theVowels.charAt(i);
        }

        finalName = finalName + theNumbers;

        return finalName;
    }
}
