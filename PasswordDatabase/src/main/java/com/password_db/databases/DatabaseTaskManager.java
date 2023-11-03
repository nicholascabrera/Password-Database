package com.password_db.databases;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingWorker;

import java.awt.Cursor;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.password_db.gui.GUI;
import com.password_db.gui.Record;

import com.password_db.cryptography.Password;
import com.password_db.exceptions.IncorrectUsernameException;

//this will allow me to use multithreading to avoid my UI from freezing during complicated tasks.
public class DatabaseTaskManager extends SwingWorker<Void, Void>{
    
    //input values
    private TaskManager choice;
    private Object[] parameters;
    private Database database;
    private GUI window;
    private JPasswordField passField;
    private JButton loginButton;

    //get the return values from the database
    private boolean databaseReturnBoolean;
    private LogIn databaseLogInStatus;
    private Record[] databaseReturnRecords;

    public DatabaseTaskManager(Database database){
        this.database = database;
    }

    public DatabaseTaskManager(GUI window, Database database, JPasswordField passField, JButton loginButton){
        this.window = window;
        this.database = database;
        this.passField = passField;
        this.loginButton = loginButton;
        this.choice = TaskManager.NO_CHOICE;
    }

    public void setChoice(TaskManager choice){
        this.choice = choice;
    }

    /**
     * Depending on the choice, the parameters must be different.
     * <html>
     *   <ul>
     *     <li>VERIFY_USER: [0] String Username, [1] Password Master Password, [2] JFrame Current Frame</li>
     *     <li>ADD_USER: [0] String Username, [1] Password Master Password, [2] Character Default Echo Character</li>
     *     <li>PULL_PASSWORDS</li>
     *     <li>STORE_PASSWORD</li>
     *     <li>NO_CHOICE</li>
     *   </ul>
     * </html>
     * @param parameters
     */
    public void setParameters(Object[] parameters){
        this.parameters = new Object[parameters.length];

        for(int i = 0; i < parameters.length; i++){
            this.parameters[i] = parameters[i];
        }
    }

    public boolean isUserAdded(){
        return this.databaseReturnBoolean;
    }

    public boolean isPasswordStored(){
        return this.databaseReturnBoolean;
    }

    public LogIn getVerification(){
        return this.databaseLogInStatus;
    }

    public Record[] getRecords(){
        return this.databaseReturnRecords;
    }

    @Override
    protected Void doInBackground() throws Exception {
        switch(choice){
            case VERIFY_USER:
                try {
                    this.databaseLogInStatus = database.verifyCredentials((String) parameters[0], (Password) parameters[1]);
                } catch (IncorrectUsernameException exception) {
                    int entry = JOptionPane.showConfirmDialog(null,
                            exception.getMessage() + "\nWould you like to register with this username?",
                            "Invalid Username", JOptionPane.YES_NO_OPTION);
                    if (entry == JOptionPane.YES_OPTION) {
                        this.databaseLogInStatus = LogIn.REGISTER;
                    }
                }
                break;
            case ADD_USER:
                this.databaseReturnBoolean = database.addUser((String)parameters[0], (Password)parameters[1]);
                break;
            case PULL_PASSWORDS:
                this.databaseReturnRecords = database.pullAllPasswords();
                break;
            case STORE_PASSWORD:
                this.databaseReturnBoolean = database.storeGeneratedPassword((String)parameters[0], (String)parameters[1], (String)parameters[2], (String)parameters[3], (String)parameters[4], (String)parameters[5]);
                break;
            default:
                break;
        }

        return null;
    }

    @Override
    public void done(){
        switch(choice){
            case VERIFY_USER:
                ((JFrame)parameters[2]).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                LogIn dbResult = this.getVerification();

                if (dbResult == LogIn.LOGIN_GOOD) {
                    this.database.setUsername((String)parameters[0]);
                    this.database.setPassword((Password)parameters[1]);
                    this.window.setInstance("portal");
                    ((JFrame)parameters[2]).dispose();
                } else if (dbResult == LogIn.REGISTER) {
                    JOptionPane.showMessageDialog(((JFrame)parameters[2]), "Please input your desired password.",
                            "Registration Instructions", JOptionPane.OK_OPTION);
                    this.passField.setText("");
                    this.database.setPassword(new Password());
                    this.passField.setEchoChar((char) 0);
                    this.loginButton.setText("Register");
                }
                break;

            case ADD_USER:
                if(this.isUserAdded()){
                    JOptionPane.showMessageDialog(null, "Registration Successful. Please Log In!", "Registration", JOptionPane.OK_OPTION);
                } else {
                    JOptionPane.showMessageDialog(null, "Registration Unsuccessful.", "Registration", JOptionPane.OK_OPTION);
                }

                this.passField.setEchoChar(((char)parameters[2]));
                this.loginButton.setText("Log In");
                break;

            case PULL_PASSWORDS:
                break;

            case STORE_PASSWORD:
                break;
            default:
                break;
        }
        
        choice = TaskManager.NO_CHOICE;
    }
}