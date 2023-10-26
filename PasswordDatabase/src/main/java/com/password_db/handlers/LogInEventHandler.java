package com.password_db.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.password_db.cryptography.Password;
import com.password_db.databases.Database;
import com.password_db.exceptions.InputValidationException;
import com.password_db.gui.GUI;

public class LogInEventHandler implements ActionListener, KeyListener {

    private GUI window;
    private JFrame frame;
    private Database userDatabase;

    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;

    private String username;
    private Password masterPassword;

    private char defaultEcho;

    public LogInEventHandler(GUI window, Database userDatabase, JFrame frame, JTextField userField, JPasswordField passField, JButton loginButton){
        this.window = window;
        this.frame = frame;
        this.userField = userField;
        this.passField = passField;
        this.userDatabase = userDatabase;
        this.loginButton = loginButton;

        this.defaultEcho = this.passField.getEchoChar();
    }

    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand() == "login"){
            if(this.passField.getEchoChar() == (char)0 && this.loginButton.getText().equals("Register")){
                this.registerUser();
            } else {
                this.verifyLogin();
            }
        }
    }

    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            this.frame.dispose();
            System.exit(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar() == '\n'){
            if(this.passField.getEchoChar() == (char)0  && this.loginButton.getText().equals("Register")){
                this.registerUser();
            } else {
                this.verifyLogin();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private boolean inputValidation(Pattern pattern, String input){
        if(pattern.matcher(input).matches()){
            return true;
        }

        return false;
    }

    private void registerUser(){
        //I need to perform Input Validation here.
        Pattern userPattern = Pattern.compile("^[A-Za-z0-9.]{3,20}$");
        Pattern passwordPattern = Pattern.compile("^[A-Za-z0-9!@#\\$%\\^&\\*\\(\\)~`:/,\\.\\?\"-=_\\+]{10,128}$");

        try {
            if (!this.inputValidation(userPattern, this.userField.getText()) || !this.inputValidation(passwordPattern, String.valueOf(this.passField.getPassword()))) {
                throw new InputValidationException("Invalid input.");
            }

            this.username = this.userField.getText();
            this.masterPassword = new Password(new String(this.passField.getPassword()));

            if(this.userDatabase.addUser(this.username, this.masterPassword)){
                JOptionPane.showMessageDialog(this.frame, "Registration Successful. Please Log In!", "Registration", JOptionPane.OK_OPTION);

            } else {
                JOptionPane.showMessageDialog(this.frame, "Registration Unsuccessful.", "Registration", JOptionPane.OK_OPTION);
            }
        } catch (InputValidationException e){
            JOptionPane.showMessageDialog(this.frame, e.getMessage(), "Input Validation", JOptionPane.OK_OPTION);
            userField.setText("");
        }

        this.passField.setEchoChar(this.defaultEcho);
        this.loginButton.setText("Log In");
    }

    private void verifyLogin(){
        //I need to perform Input Validation here.
        Pattern userPattern = Pattern.compile("^[A-Za-z0-9.]{3,20}$");
        Pattern passwordPattern = Pattern.compile("^[A-Za-z0-9!@#\\$%\\^&\\*\\(\\)~`:/,\\.\\?\"-=_\\+]{10,128}$");

        try {
            if (!this.inputValidation(userPattern, this.userField.getText()) || !this.inputValidation(passwordPattern, String.valueOf(this.passField.getPassword()))) {
                throw new InputValidationException("Invalid input.");
            }

            this.username = this.userField.getText();
            this.masterPassword = new Password(new String(this.passField.getPassword()));
            int dbResult = userDatabase.verifyCredentials(this.username, this.masterPassword);
            if (dbResult == Database.LOGIN_GOOD) {
                this.userDatabase.setUsername(this.username);
                this.userDatabase.setPassword(this.masterPassword);
                this.window.setInstance("portal");
                this.frame.dispose();
            } else if (dbResult == Database.REGISTER) {
                JOptionPane.showMessageDialog(this.frame, "Please input your desired password.",
                            "Registration Instructions", JOptionPane.OK_OPTION);
                this.passField.setText("");
                this.masterPassword = new Password();
                this.passField.setEchoChar((char) 0);
                this.loginButton.setText("Register");
            }
        } catch (InputValidationException err) {
            if (this.userField.getText().equals("")) {
                int entry = JOptionPane.showConfirmDialog(this.frame,
                        err.getMessage() + "\nWould you like to register?", "No Username Input",
                        JOptionPane.YES_NO_OPTION);
                if (entry == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this.frame, "Please input your desired username and password.",
                            "Registration Instructions", JOptionPane.OK_OPTION);
                    this.passField.setEchoChar((char) 0);
                    this.loginButton.setText("Register");
                }
            } else {
                JOptionPane.showMessageDialog(this.frame, err.getMessage(), "Input Validation", JOptionPane.OK_OPTION);
            }
        }
    }
}
