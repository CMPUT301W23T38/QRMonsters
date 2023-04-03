package com.example.qrmonsters;

/**
 * The textToAsciiNumbers class contains methods to generate ASCII art numbers.
 */
public class textToAsciiNumbers {

    public static String zero    = "00000\n0   0\n00000";
    public static String one     = "  1  \n  1  \n  1  ";
    public static String two     = "22222\n  2  \n22222";
    public static String three   = "33333\n  333\n33333";
    public static String four    = "   4 \n44444\n   4 ";
    public static String five    = "55555\n55555\n55555";
    public static String six     = "666  \n6666 \n66666";
    public static String seven   = "77777\n  7  \n7    ";
    public static String eight   = "88888\n8 8 8\n88888";
    public static String nine    = "99999\n 9999\n   99";
    /**
     * This method returns a String containing the ASCII art number generated based on the num parameter.
     *
     * @param num a Character representing the number to convert to ASCII art.
     * @return a String containing the ASCII art number.
     * @throws IllegalStateException if the number is not recognized.
     */
    public static String getAscii(Character num){

        String mainString;

        switch(num){

            case '0':
                mainString = zero;
                break;

            case '1':
                mainString = one;
                break;

            case '2':
                mainString = two;
                break;

            case '3':
                mainString = three;
                break;

            case '4':
                mainString = four;
                break;

            case '5':
                mainString = five;
                break;

            case '6':
                mainString = six;
                break;

            case '7':
                mainString = seven;
                break;

            case '8':
                mainString = eight;
                break;

            case '9':
                mainString = nine;
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + num);
        }

        return mainString;

    }




}
