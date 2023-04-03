package com.example.qrmonsters;
/**

 The textToAsciiLetters class contains methods to generate ASCII art letters.
 */
public class textToAsciiLetters {

    public String A1 = "  *  ";
    public String A2 = " *** ";
    public String A3 = "*   *";

    public String B1 = "*****";
    public String B2 = "**** ";
    public String B3 = "*****";

    public String C1 = " ****";
    public String C2 = "*    ";
    public String C3 = " ****";

    public String D1 = "**** ";
    public String D2 = "*   *";
    public String D3 = "**** ";

    public String E1 = "*****";
    public String E2 = "***  ";
    public String E3 = "*****";

    public String F1 = "*****";
    public String F2 = "***  ";
    public String F3 = "**";
    /**

     This method returns a String containing the ASCII art letter generated based on the base and component parameters.

     @param base a Character representing the base letter.

     @param component a Character representing the component of the letter to replace the asterisk symbol.

     @return a String containing the ASCII art letter.

     @throws IllegalStateException if the base letter is not recognized.
     */

    public static String getAscii(Character base, Character component){

        String B = "*****\n**** \n*****";
        String C = " ****\n*    \n ****";
        String D = "**** \n*   *\n**** ";
        String F = "*****\n***  \n**   ";
        String mainString;

        switch(base){

            case 'B':
                mainString = B;
                break;

            case 'C':
                mainString = C;
                break;

            case 'D':
                mainString = D;
                break;

            case 'F':
                mainString = F;
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + base);
        }

        String replaceString = mainString.replace('*',component);


        return replaceString;

    }


}
