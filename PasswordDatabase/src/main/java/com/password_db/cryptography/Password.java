package com.password_db.cryptography;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.password_db.databases.Database;
import com.password_db.exceptions.InputValidationException;

public class Password {

    private byte length = -1;
    private boolean upperCase, lowerCase, specialChars, numbers, regenerate;
    private String password123 = "";

    /**
     * General constructor for the password object.
     */
    public Password() {
        this.password123 = "";
        this.evaluate();
    }

    /**
     * Constructor for the password object.
     * 
     * @param length
     */
    public Password(byte length) {
        this.length = length;
        this.evaluate();
    }

    /**
     * Constructor for the password object.
     * 
     * @param length
     * @param upperCase
     * @param lowerCase
     * @param specialChars
     * @param numbers
     */
    public Password(byte length, boolean upperCase, boolean lowerCase, boolean specialChars, boolean numbers) {
        this.length = length;
        this.upperCase = upperCase;
        this.lowerCase = lowerCase;
        this.specialChars = specialChars;
        this.numbers = numbers;
        this.evaluate();
    }

    public Password(String p){
        this.password123 = p;
        this.evaluate();
    }

    public void init(Database database) throws Exception{

        String username = this.getUsername();
        if (username.equals("")){
            return;
        }
        String website = this.getWebsite();
        if(website.equals("")){
            return;
        }
        
        do{
            this.password123 = "";
            this.evaluate();
            this.generatePassword();
        } while (regenerate);

        byte entry = (byte) JOptionPane.showConfirmDialog(null, "Do you want to store your password?", "Store?", 0);
        if (entry == JOptionPane.YES_OPTION) {  // if the user wants to store their password, it must be encrypted.

            // prepare to encrypt the password.
            SecureObject s = new SecureObject();
            s.init();
            byte[] salt = SecureObject.generateSalt(128);

            // encrypt the password. It is ready to store.
            byte[] encryptedPassword = s.encrypt(password123);

            // prepare to encrypt the key.
            Password masterPassword = database.getPassword();
            String key = s.getKey();

            // encrypt the key. It is ready to store.
            byte[] encryptedKey = s.encrypt(Base64.getDecoder().decode(key), masterPassword);

            // concatenate the website username, website, and master username, then hash it to use as the ID.
            String identifier = ((username.concat(website)).concat(database.getUsername())).concat(masterPassword.getPassword());
            byte[] hashSalt = Base64.getDecoder().decode("ABCDEFGHIJKLMNOP");
            String id = s.argonHash(identifier, hashSalt);     // ID has been created. It is ready to store.

            // perform storing operations.
            if(database.storeGeneratedPassword(id, website, username, 
                Base64.getEncoder().encodeToString(encryptedPassword), 
                Base64.getEncoder().encodeToString(salt), 
                Base64.getEncoder().encodeToString(encryptedKey)))
            {
                System.out.println("Data storage completed successfuly.");
            }
        }
    }

    private String getWebsite() {
        String website = "\n";
        do{
            String input = JOptionPane.showInputDialog(null, "What Website/Application is this for?");
            Pattern inputPattern = Pattern.compile("^[A-Za-z0-9]{0,20}$");

            if(input != null){
                try{
                    if (!inputPattern.matcher(input).matches()) { //checks to see if its a valid website name
                        throw new InputValidationException("Invalid input. Only letters and numbers allowed.");
                    } else {
                        website = input;
                    }
                } catch (InputValidationException e){
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Input Validation", 0);
                }
            } else {
                website = "";
            }

        } while (website.equals("\n"));

        return website;
    }

    private String getUsername() {
        String username = "\n";

        do{
            String input = JOptionPane.showInputDialog(null, "Enter your username:");
            Pattern inputPattern = Pattern.compile("^[A-Za-z0-9.]{3,20}$");

            if(input != null){
                try{
                    if (!inputPattern.matcher(input).matches()) { //checks to see if its a valid username
                        throw new InputValidationException("Invalid input. Only letters and numbers allowed, length must be greater than 3 but less than 20.");
                    } else {
                        username = input;
                    }
                } catch (InputValidationException e){
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Input Validation", 0);
                }
            } else {
                username = "";
            }

        } while (username.equals("\n"));

        return username;
    }

    private void generatePassword() {
        if(this.setup()){
            this.insert();
            byte passwordStrength = (byte) this.strengthTest();
            String outputMSG = "";

            if (passwordStrength > -1){
                outputMSG = outputMSG.concat("Congrats on your new password below!\n" + this.password123 +"\n");
                if (passwordStrength == 6) {
                    outputMSG = outputMSG.concat("This is an excellent password!");
                } else if (passwordStrength >= 4) {
                    outputMSG = outputMSG.concat("This is a good password, but you can still do better.");
                } else if (passwordStrength >= 3) {
                    outputMSG = outputMSG.concat("This is not a suitable password, so try making it better.");
                } else {
                    outputMSG = outputMSG.concat("This is an insecure password! Definitely find a new one.");
                }
            } else {
                System.out.println("Bad password strength call.");
            }

            outputMSG = outputMSG.concat("\nGenerate new password?");
            
            byte entry = (byte) JOptionPane.showConfirmDialog(null, outputMSG, "Password Generated!", 0);
            this.regenerate = (entry == JOptionPane.YES_OPTION) ? true : false;
        }
    }

    private boolean setup() {
        boolean generate = false;
        final int SHOW_AGAIN = 5;
        final int EMPTY = 0;

        do {
            //I need to perform input verification here.
            String input = JOptionPane.showInputDialog(
                null, "Enter your preferred password length.", "Password Length");

            if(input != null){
                Pattern numPattern = Pattern.compile("^[0-9]?[0-9]?[0-9]$");    //three digit number from 0-999

                try {
                    if (!numPattern.matcher(input).matches()) { //just checks to see if its a number
                        throw new InputValidationException("Invalid input. Only number between 6 - 127 allowed.");
                    } else {
                        generate = true;
                        int entry = Integer.parseInt(input);
                        if (entry > 127) {                      //brings upper limit from 999 to 127
                            JOptionPane.showMessageDialog(null, "Password too long! Try again with less than 128!");
                            this.length = SHOW_AGAIN;
                        } else if (entry < 6) {                 //brings lower limit from 0 to 6
                            JOptionPane.showMessageDialog(null, "Password too short! Try again with more than 5!");
                            this.length = SHOW_AGAIN;
                        } else {
                            this.length = (byte) entry;         //if the number is within the range 6-127, then we allow it to become length.
                        }
                    }
                } catch (InputValidationException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Invalid Validation", 0);
                    this.length = SHOW_AGAIN;    //sets the length to 5 in preparation to show the password prompt again.
                }
            } else {
                evaluate();     //resets the length variable, which should be 0 here. if it isn't, something else is horribly wrong.
            }
        } while (this.length == SHOW_AGAIN);

        if(generate){
            byte entry = (byte) JOptionPane.showConfirmDialog(
                null, "Will your password use upper case letters?","Password Specifications", 0);
            if (entry == JOptionPane.YES_OPTION) {
                this.upperCase = true;
            } else {
                this.upperCase = false;
            }

            entry = (byte) JOptionPane.showConfirmDialog(
                null, "Will your password use lower case letters?","Password Specifications", 0);
            if (entry == JOptionPane.YES_OPTION) {
                this.lowerCase = true;
            } else {
                this.lowerCase = false;
            }

            entry = (byte) JOptionPane.showConfirmDialog(
                null, "Will your password use special characters?","Password Specifications", 0);
            if (entry == JOptionPane.YES_OPTION) {
                this.specialChars = true;
            } else {
                this.specialChars = false;
            }

            entry = (byte) JOptionPane.showConfirmDialog(null, "Will your password use numbers?", "Password Specifications", 0);
            if (entry == JOptionPane.YES_OPTION) {
                this.numbers = true;
            } else {
                this.numbers = false;
            }
        }

        try{
            //if they're all false or the length 
            if(!(this.upperCase || this.lowerCase || this.specialChars || this.numbers) && (this.length != SHOW_AGAIN && this.length != EMPTY)){
                throw new InputValidationException("Invalid Input, at least one field must be chosen.");
            }
        } catch (InputValidationException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Invalid Validation", 0);
            generate = false;
            evaluate();
        }
        

        return generate;
    }

    /**
     * 
     * @param charCase
     * @param randomNumber
     * @return
     */
    private String getRandomLetter(byte charCase, byte randomNumber){
        Dictionary abc = new Dictionary();
        /**
         * from the inside out:
         * ((float)randomNumber / 128.0)        randomNumber is a supplied random number. It is a 2's complement byte number, but for it to be familiar I constrain it
         *                                      to be a value from -1.0 to 1.0 instead of -128.0 to 128.0 while converting to a float to retain the source of randomness.
         *                                      ***CLARIFICATION*** the decimal range above is non-inclusive, ie -1.0 and 1.0 are not possible to generate.
         * (abc.getLength(charCase)*(...))      (...) denotes the above lines. Here I multiply the 2's complement number by the number of characters in the selected list.
         *                                      Assuming upper case is the selected case (charCase == 0), then we multiply 26 by our range to stretch the range from -1.0
         *                                      to 1.0 into -26.0 to 26.0.
         * Math.abs(byte(...))                  I convert the float range of -26.0 to 26.0 into the integer number range -25 to 25 via truncation, while applying an
         *                                      absolute value function to constrain the range further to 0 to 25.
         * abc.getCharacter(charCase, ...)      I supply to the Dictionary object the identity of the character(charCase) as well as the index of the letter I want, which
         *                                      has been randomly generated above.
         * Character.toString(...)              Convert the returned character into a string, which is now ready for concatenation.
         */
        return Character.toString(abc.getCharacter(charCase, (Math.abs((byte)((abc.getLength(charCase)-1)*((float)randomNumber / 128.0))))));
    }

    private void insert() {
        Dictionary abc = new Dictionary();
        for (int i = 0; i < this.length; i++) {
            SecureRandom sr = new SecureRandom();                                           //random number generator, but secure
            byte[] randoms = new byte[2];                                                   //byte array of size 2, since i only need 2 random numbers (case and letter)
            sr.nextBytes(randoms);                                                          //store 2 random numbers into the byte array 'randoms'

            /**
             * from the inside out:
             * ((float)randoms[0] / 128.0)          randoms[0] is my first random number. It is a 2's complement byte number, but for it to be familiar I constrain it
             *                                      to be a value from -1.0 to 1.0 instead of -128.0 to 128.0 while converting to a float to retain the source of randomness.
             *                                      ***CLARIFICATION*** the decimal range above is non-inclusive, ie -1.0 and 1.0 are not possible to generate.
             * (4*(...))                            (...) denotes the above lines. Here I multiply the 2's complement number by four to stretch the range from -1.0 to 1.0
             *                                      into -4.0 to 4.0.
             * Math.abs(byte(...))                  I convert the float range of -4.0 to 4.0 into the integer number range -3 to 3 via truncation, while applying an absolute
             *                                      value function to constrain the range further to 0 to 3.
             * (byte)(...)                          I convert the integer number range to a byte number range.
             */
            byte caseNum = (byte)Math.abs((byte)(4*((float)randoms[0] / 128.0)));

            switch (caseNum) {
                case 0: // Uppercase letter
                    if (this.upperCase){        //if the user wants to use uppercase letters, insert.
                        if (abc.getLength(caseNum) != -1) {     //if the caseNum isn't 0-3, then something is wrong.
                            this.password123 = this.password123.concat(this.getRandomLetter(caseNum, randoms[1]));  //concatenate a random letter generated above.
                        } else {
                            System.out.printf("Bad upper case insertion at index %d.\nCase Number: %d", i, caseNum);    //if no work, say so.
                        }
                    } else {
                        i--;                    //if the user doesn't want uppercase letters, go back one and do it again.
                    }
                    break;
                case 1: // Lowercase letter
                    if (this.lowerCase) {
                        if (abc.getLength(caseNum) != -1) {
                            this.password123 = this.password123.concat(this.getRandomLetter(caseNum, randoms[1]));
                        } else {
                            System.out.printf("Bad lower case insertion at index %d.\nCase Number: %d", i, caseNum);
                        }
                    } else {
                        i--;
                    }
                    break;
                case 2: // Special character
                    if (this.specialChars){
                        if (abc.getLength(caseNum) != -1) {
                            this.password123 = this.password123.concat(this.getRandomLetter(caseNum, randoms[1]));
                        } else {
                            System.out.printf("Bad special character insertion at index %d.\nCase Number: %d", i, caseNum);
                        }
                    } else {
                        i--;
                    }
                    break;
                case 3: // Number
                    if (this.numbers){
                        if (abc.getLength(caseNum) != -1) {
                            this.password123 = this.password123.concat(this.getRandomLetter(caseNum, randoms[1]));
                        } else {
                            System.out.printf("Bad number insertion at index %d.\nCase Number: %d", i, caseNum);
                        }
                    } else {
                        i--;
                    }
                    break;
                default:
                    i--;
                    break;
            }
        }
    }

    private void evaluate(){
        Dictionary abc = new Dictionary();
        String s = this.password123;
        byte type;
        this.length = (byte)s.length();

        for (int i = 0; i < this.length; i++) {
            char c = s.charAt(i);
            type = abc.CharType(c);

            if (type == 0){
                this.upperCase = true;
            }
            if (type == 1){
                this.lowerCase = true;
            }
            if (type == 2){
                this.numbers = true;
            }
            if (type == 3){
                this.specialChars = true;
            }
        }
    }

    private int strengthTest(){
        Dictionary abc = new Dictionary();
        String s = this.password123;
        boolean usedUpper = false, usedLower = false, usedNum = false, usedSpec = false;
        byte type, score = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            type = abc.CharType(c);

            if (type == 0){
                usedUpper = true;
            }
            if (type == 1){
                usedLower = true;
            }
            if (type == 2){
                usedNum = true;
            }
            if (type == 3){
                usedSpec = true;
            }
        }

        if (usedUpper) score++;
        if (usedLower) score++;
        if (usedNum) score++;
        if (usedSpec) score++;

        if (s.length() >= 8) score++;
        if (s.length() >= 16) score++;

        return score;
    }

    public String getPassword(){
        return this.password123;
    }
}