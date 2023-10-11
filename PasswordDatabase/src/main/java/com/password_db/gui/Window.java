package com.password_db.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.password_db.cryptography.Password;
import com.password_db.databases.Database;
import com.password_db.handlers.LogInEventHandler;

public class Window {
    private int x_dimension;
    private int y_dimension;
    private int x_location;
    private int y_location;
    private LogInEventHandler loginHandler;
    private Database userDatabase;
    private String instance;

    public Window(){}

    public void init(){
        this.x_dimension = 600;
        this.y_dimension = 400;
        this.x_location = 375;
        this.y_location = 100;
        this.userDatabase = new Database("-1", new Password("-1"));
        this.instance = "login";
        this.getInstance(this.instance);
    }

    public void getInstance(String instance){
        switch(instance){
            case "login":
                this.instance = "login";
                this.loginInstance();
                break;
            case "portal":
                this.instance = "portal";
                this.portalInstance();
                break;
            case "database":
                this.instance = "database";
                this.databaseInstance();
                break;
            case "website":
                this.instance = "website";
                this.websiteInstance();
                break;
            case "generate":
                this.instance = "generate";
                this.generationInstance();
                break;
        }
    }

    public String getInstance(){
        return this.instance;
    }

    /**
     * This instance allows the user to login. No matter what the user inputs, they will be allowed access.
     */
    private void loginInstance(){
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame loginFrame = new JFrame("Password Generator - Log In");
        loginFrame.setPreferredSize(new Dimension(this.x_dimension, this.y_dimension));
        loginFrame.setLocation(this.x_location, this.y_location);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = loginFrame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel textPane = new JPanel();
        textPane.setLayout(new BoxLayout(textPane, BoxLayout.PAGE_AXIS));

        // idea on how to store usernames and passwords without the passwords being stored anywhere in plaintext:
        // username is stored in plain text
        // instead of storing the password with it, a string that has been encrypted using the password as a key is paired with it
        // if the user logs in with an existing username but the string is incorrectly decrypted, then the password is wrong.
        // i can find if the username exists with a hash map, username is hashed and used as the ID for constant search time
        // the string can be some random word from a dictionary, which has some random words and their hashes.
        // i can find if the string matches plain english by hashing the string and seeing if that hash exists in the dictionary.
        // when all is said and done, it should be a constant run-time login verification system.

        int preferedSize = 200;

        TextField username = new TextField(preferedSize);
        TextField password = new TextField(preferedSize);
        JButton loginButton = new JButton("Log In");

        loginHandler = new LogInEventHandler(this, this.userDatabase, loginFrame, username, password);

        username.setMinimumSize(new Dimension(preferedSize, 20));
        password.setMinimumSize(new Dimension(preferedSize, 20));
        loginButton.setMinimumSize(new Dimension(preferedSize, 20));

        username.setPreferredSize(new Dimension(preferedSize, 20));
        password.setPreferredSize(new Dimension(preferedSize, 20));
        loginButton.setPreferredSize(new Dimension(preferedSize, 20));

        username.setMaximumSize(new Dimension(preferedSize, 20));
        password.setMaximumSize(new Dimension(preferedSize, 20));
        loginButton.setMaximumSize(new Dimension(preferedSize, 20));

        username.addActionListener(this.loginHandler);
        password.addActionListener(this.loginHandler);

        loginButton.setActionCommand("login");
        loginButton.addActionListener(this.loginHandler);

        username.setVisible(true);
        password.setVisible(true);

        JPanel userPane = new JPanel();
        userPane.setLayout(new FlowLayout(FlowLayout.LEFT));

        userPane.setMinimumSize(new Dimension(preferedSize, 20));
        userPane.setPreferredSize(new Dimension(preferedSize, 20));
        userPane.setMaximumSize(new Dimension(preferedSize, 20));

        JPanel passPane = new JPanel();
        passPane.setLayout(new FlowLayout(FlowLayout.LEFT));

        passPane.setMinimumSize(new Dimension(preferedSize, 20));
        passPane.setPreferredSize(new Dimension(preferedSize, 20));
        passPane.setMaximumSize(new Dimension(preferedSize, 20));

        JLabel userLabel = new JLabel("Username");
        JLabel passLabel = new JLabel("Password");

        userPane.add(userLabel);
        passPane.add(passLabel);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.LEFT));

        buttonPane.setMinimumSize(new Dimension(preferedSize+14, 30));
        buttonPane.setPreferredSize(new Dimension(preferedSize+14, 30));
        buttonPane.setMaximumSize(new Dimension(preferedSize+14, 30));

        buttonPane.add(loginButton);

        textPane.add(Box.createGlue());                                         // adds extra space to account for window expansion
        textPane.add(userPane);
        textPane.add(username);                                                 // adds the username text field
        textPane.add(Box.createRigidArea(new Dimension(0,5)));     // adds an invisible space between the text fields
        textPane.add(passPane);
        textPane.add(password);                                                 // adds the password text field
        textPane.add(Box.createRigidArea(new Dimension(0,5)));     // adds an invisible space between the text fields
        textPane.add(buttonPane);
        textPane.add(Box.createGlue());                                         // adds extra space to account for window expansion
        textPane.setBorder(BorderFactory.createEmptyBorder(100,10,100,10));

        contentPane.add(textPane, BorderLayout.CENTER);
        loginFrame.pack();
        loginFrame.setVisible(true);
    }

    /**
     * This instance displays the three options:
     * (a) Print the Database, (b) Search by website, or (c) Generate a new password.
     * features:
     *  - text field for website
     *  - buttons for database print, database search, and generate password.
     *  - search and generate take input from website to use as their ID.
     *  - print disregards website input.
     *  - additional button to go back to main.
     */
    private void portalInstance(){
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame portalFrame = new JFrame("Password Generator - Portal");
        portalFrame.setPreferredSize(new Dimension(this.x_dimension, this.y_dimension));
        portalFrame.setLocation(this.x_location, this.y_location);
        portalFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = portalFrame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        portalFrame.pack();
        portalFrame.setVisible(true);
    }

    /**
     * This instance displays the database to the user.
     */
    private void databaseInstance(){

    }

    /**
     * This instance displays the searched website to the user.
     */
    private void websiteInstance(){

    }

    /**
     * This instance allows the user to generate and regenerate a password.
     * This instance also allows the user to store a generated password into the database.
     */
    private void generationInstance(){

    }

    public String getUsername(){
        return this.userDatabase.getUsername();
    }

    public Password getPassword(){
        return this.userDatabase.getPassword();
    }
}
