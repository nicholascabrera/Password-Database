package com.password_db.handlers;

import java.awt.Cursor;
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
import com.password_db.databases.DatabaseTaskManager;
import com.password_db.databases.TaskManager;
import com.password_db.exceptions.InputValidationException;
import com.password_db.gui.GUI;

public class LogInEventHandler implements ActionListener, KeyListener {

    private GUI window;
    private JFrame frame;
    private Database userDatabase;
    private DatabaseTaskManager taskManager;

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
        this.loginButton = loginButton;
        this.userDatabase = userDatabase;
        this.defaultEcho = this.passField.getEchoChar();
    }

    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand() == "login"){
            if(this.passField.getEchoChar() == (char)0 && this.loginButton.getText().equals("Register")){
                try{
                    this.loginButton.setEnabled(false);
                    this.registerUser();
                } catch (InputValidationException err){
                    this.loginButton.setEnabled(true);
                    JOptionPane.showMessageDialog(this.frame, err.getMessage(), "Input Validation", JOptionPane.ERROR_MESSAGE);
                    userField.setText("");
                }
            } else {
                try{
                    this.loginButton.setEnabled(false);
                    this.verifyLogin();
                } catch (InputValidationException err) {
                    this.loginButton.setEnabled(true);
                    if (this.userField.getText().equals("")) {
                        int entry = JOptionPane.showConfirmDialog(this.frame,
                                err.getMessage() + "\nWould you like to register?", "No Username Input",
                                JOptionPane.QUESTION_MESSAGE);
                        if (entry == JOptionPane.YES_OPTION) {
                            JOptionPane.showMessageDialog(this.frame, "Please input your desired username and password.",
                                    "Registration Instructions", JOptionPane.QUESTION_MESSAGE);
                            this.passField.setEchoChar((char) 0);
                            this.loginButton.setText("Register");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this.frame, err.getMessage(), "Input Validation", JOptionPane.ERROR_MESSAGE);
                    }
                }
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
        if(e.getKeyChar() == '\n' && loginButton.isEnabled()){
            this.actionPerformed(new ActionEvent(e.getSource(), e.getID(), "login"));
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

    private void registerUser() throws InputValidationException {
        //I need to perform Input Validation here.
        Pattern userPattern = Pattern.compile("^[A-Za-z0-9.]{3,20}$");
        Pattern passwordPattern = Pattern.compile("^[A-Za-z0-9!@#\\$%\\^&\\*\\(\\)~`:/,\\.\\?\"-=_\\+]{10,128}$");

        if (!this.inputValidation(userPattern, this.userField.getText())
                || !this.inputValidation(passwordPattern, String.valueOf(this.passField.getPassword()))) {
            throw new InputValidationException("Invalid input.");
        }

        this.username = this.userField.getText();
        this.masterPassword = new Password(new String(this.passField.getPassword()));
        this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        // SwingWorkers are not reusable.
        this.taskManager = new DatabaseTaskManager(this.window, this.userDatabase, this.passField, this.loginButton);
        this.taskManager.setChoice(TaskManager.VERIFY_USER);
        this.taskManager.setParameters(new Object[] { this.username, this.masterPassword, this.defaultEcho });
        this.taskManager.execute();
    }

    private void verifyLogin() throws InputValidationException{
        //I need to perform Input Validation here.
        Pattern userPattern = Pattern.compile("^[A-Za-z0-9.]{3,20}$");
        Pattern passwordPattern = Pattern.compile("^[A-Za-z0-9!@#\\$%\\^&\\*\\(\\)~`:/,\\.\\?\"-=_\\+]{10,128}$");

        if (!this.inputValidation(userPattern, this.userField.getText())
                || !this.inputValidation(passwordPattern, String.valueOf(this.passField.getPassword()))) {
            throw new InputValidationException("Invalid input.");
        } else {
            this.username = this.userField.getText();
            this.masterPassword = new Password(new String(this.passField.getPassword()));
            this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // SwingWorkers are not reusable.
            this.taskManager = new DatabaseTaskManager(this.window, this.userDatabase, this.passField, this.loginButton);
            this.taskManager.setChoice(TaskManager.VERIFY_USER);
            this.taskManager.setParameters(new Object[] {this.username, this.masterPassword, this.frame});
            this.taskManager.execute();
        }   
    }
}
