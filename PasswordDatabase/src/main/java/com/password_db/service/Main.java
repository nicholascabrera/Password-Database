package com.password_db.service;

import com.password_db.cryptography.Password;
import com.password_db.cryptography.SecureObject;
import com.password_db.gui.GUI;

public class Main {
    /**
     * 1: Log in. If the log in information is incorrect, the database won't connect.
     * 2: Three Choices: (a) Print the Database, (b) Search by website, or (c) Generate a new password.
     *(a) 3:    i. Pull the encrypted keys and generated passwords from the databases, pairing them by ID.
     *         ii. Use the master password inputted by the user to decrypt the keys.
     *        iii. Use the decrypted keys to decrypt the generated passwords.
     *         iv. Print the generated passwords and their corresponding we.
     *          v. GOTO 2.
     *
     *(b) 4:    i. Convert the website to lowercase and hash it, then search up the hash as the ID.
     *         ii. If the ID exists, pull the corresponding encrypted key and encrypted password.
     *        iii. Using the master password, decrypt the key.
     *         iv. Using the decrypted key, decrypt the generated password.
     *          v. Print the generated password and corresponding website.
     *         vi. GOTO 2.
     * 
     * (c) 5:   i. Go through password prompts, generate password. 
     *         ii. Show generated password to user.
     *        iii. Create Secure object, generate key and cipher.
     *         iv. Encrypt the generated password with the cipher.
     *          v. Encrypt the ciphers key with the master password.
     *         vi. Input the website the password is for.
     *        vii. Convert to lowercase and hash the website and use that as the ID.
     *       viii. If the ID already exists then overwrite it.
     *         ix. Push the ID and Encrypted Key to the Keys Database.
     *          x. Push the ID and the Encrypted Generated Password to the Passwords Database.
     *         xi. Display Success.
     *        xii. GOTO 2.
     */
    public static void main(String[] args) throws Exception {
        // start up window and initialize for login requirement
        GUI window = new GUI();
        window.init();

        // wait until the login has been entered, which is known because the instance changes
        do{
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        } while (window.getInstance().equals("login"));

        // retrieve login information from the window
        String username = window.getUsername();
        Password masterPassword = window.getPassword();

        // easy way to check that login information was received
        System.out.printf("Username: %s\n", username);
        System.out.printf("Password: %s\n", masterPassword.getPassword());
        

        // check to see if the database contains the login information (this step is in the LogInEventHandler)
        // if it does, go on to the next step (2)

        // if the username matches but the password doesn't, it re-prompts

        // if neither match, an option pane pops up asking if you want to register

        // if you say yes, then your information is hashed and sent to the database

        // if you say no, then you are re-prompted

        SecureObject s = new SecureObject();
        s.init();
        
        // byte[] salt = s.generateSalt(128);
        // System.out.println("Salt: " + Base64.getEncoder().encodeToString(salt));
        // System.out.print(s.argonHash(password, salt));


        // Password generatedPassword = new Password();
        // generatedPassword.init();



        // Password password = new Password("password123");          //create a master password
        // System.out.println("Plain Key: "+password.getPassword());   //print the master password

        // String plainKey = s.getKey();
        // System.out.println("Plain text: "+plainKey);

        // // byte[] cipher = s.encrypt(plain);                        //encrypt the password
        
        // byte[] encryptedKey = s.encrypt(password);

        // String decryptedKey = s.decrypt(encryptedKey, password);

        // plain = s.decrypt(cipher);

        //Database dbconn = new Database();
    }
}
