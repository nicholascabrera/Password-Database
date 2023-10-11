package com.password_db.handlers;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.password_db.cryptography.Password;
import com.password_db.databases.Database;
import com.password_db.exceptions.InputValidationException;
import com.password_db.gui.Window;

public class LogInEventHandler implements ActionListener {

    private Window window;
    private JFrame frame;
    private TextField userField, passField;
    private String username;
    private Password masterPassword;
    private Database userDatabase;

    public LogInEventHandler(Window window, Database userDatabase, JFrame frame, TextField userField, TextField passField){
        this.window = window;
        this.frame = frame;
        this.userField = userField;
        this.passField = passField;
        this.userDatabase = userDatabase;
    }

    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand() == "login"){

            //I need to perform Input Validation here.
            Pattern userPattern = Pattern.compile("^[A-Za-z0-9.]{3,20}$");
            Pattern passwordPattern = Pattern.compile("^[A-Za-z0-9!@#\\$%\\^&\\*\\(\\)~`:/,\\.\\?\"-=_\\+]{10,128}$");

            try{
                if(!userPattern.matcher(this.userField.getText()).matches() || !passwordPattern.matcher(this.passField.getText()).matches()){
                    throw new InputValidationException("Invalid input.");
                }

                this.username = this.userField.getText();
                this.masterPassword = new Password(this.passField.getText());
                if(userDatabase.verifyCredentials(this.username, this.masterPassword)){
                    this.userDatabase.setUsername(this.username);
                    this.userDatabase.setPassword(this.masterPassword);
                    this.window.getInstance("portal");
                    this.frame.dispose();
                }
            } catch (InputValidationException err){
                JOptionPane.showMessageDialog(this.frame, err.getMessage(), "Invalid Validation", 0);
            }
        }
    }
}
