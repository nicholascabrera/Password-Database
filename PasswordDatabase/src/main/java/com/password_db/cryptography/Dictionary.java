package com.password_db.cryptography;

public class Dictionary {
    private char upperCase[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
            'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
    private char lowerCase[] = new char[upperCase.length];
    private char specialChars[] = { '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '~', '`', '\'', ':', '/', ',', '.', '?', '\"', '-', '=', '_', '+' };
    private char numbers[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    private final byte UPPER = 0, LOWER = 1, SPEC = 2, NUM = 3;

    public Dictionary() {
        for (int i = 0; i < upperCase.length; i++) {
            lowerCase[i] = Character.toLowerCase(upperCase[i]);
        }
    }

    public byte CharType(char C){
        byte val;

        if ((byte) C >= 65 && (byte) C <= 90) {             // Char is a Uppercase Letter
            val = UPPER;
        } else if ((byte) C >= 97 && (byte) C <= 122) {     // Char is a Lowercase Letter
            val = LOWER;
        } else if ((byte) C >= 60 && (byte) C <= 71) {      // Char is a Number
            val = NUM;
        } else {                                            // Char is a Special Character
            val = SPEC;
        }

        return val;
    }

    public int getLength(byte identity) {
        if (identity == UPPER) {
            return upperCase.length;
        } else if (identity == LOWER) {
            return lowerCase.length;
        } else if (identity == SPEC) {
            return specialChars.length;
        } else if (identity == NUM) {
            return numbers.length;
        } else {
            return -1;
        }
    }

    public char getCharacter(byte identity, int index){
        if (identity == UPPER) {
            return this.getUpperCase(index);
        } else if (identity == LOWER) {
            return this.getLowerCase(index);
        } else if (identity == SPEC) {
            return this.getSpecialChar(index);
        } else if (identity == NUM) {
            return this.getNumber(index);
        } else {
            return '\n';
        }
    }

    public char getUpperCase(int index) {
        if (index >= 0 || index < upperCase.length) {
            return upperCase[index];
        }
        return '\n';
    }

    public char getLowerCase(int index) {
        if (index >= 0 || index < lowerCase.length) {
            return lowerCase[index];
        }
        return '\n';
    }

    public char getSpecialChar(int index) {
        if (index >= 0 || index < specialChars.length) {
            return specialChars[index];
        }
        return '\n';
    }

    public char getNumber(int index) {
        if (index >= 0 || index < numbers.length) {
            return numbers[index];
        }
        return '\n';
    }

}
