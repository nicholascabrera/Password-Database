package com.password_db.databases;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import java.awt.Cursor;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.password_db.gui.GUI;
import com.password_db.gui.Record;
import com.password_db.trie.Trie;
import com.password_db.cryptography.Password;
import com.password_db.exceptions.IncorrectUsernameException;

//this will allow me to use multithreading to avoid my UI from freezing during complicated tasks.
public class DatabaseTaskManager extends SwingWorker<Void, Void>{
    
    //input values
    private TaskManager choice;
    private Object[] parameters;
    private Database database;
    private GUI gui;
    private JPasswordField passField;
    private JButton loginButton;

    //get the return values from the database
    private boolean databaseReturnBoolean;
    private LogIn databaseLogInStatus;
    private ArrayList<Record> databaseReturnRecords;
    private ArrayList<String> allApplicationsThatBeginWith;

    public DatabaseTaskManager(Database database){
        this.database = database;
    }

    public DatabaseTaskManager(GUI gui, Database database){
        this.gui = gui;
        this.database = database;
    }

    public DatabaseTaskManager(GUI gui, Database database, JPasswordField passField, JButton loginButton){
        this.gui = gui;
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
     *     <li>PULL_PASSWORDS: [0] JTable Password Database Table, [1] JFrame Current Frame, [2] Application Trie</li>
     *     <li>PULL_PASSWORD: [0] JTable Password Database Table, [1] JFrame Current Frame, [2] String Application</li>
     *     <li>STORE_PASSWORD: [0] String ID, [1] String Website, [2] String Username, [3] String Password, [4] String Salt, [5] String Key, [6] JFrame Current Frame</li>
     *     <li>FIND_APPLICATIONS: [0] JTable Password Database Table, [1] JFrame Current Frame, [2] Trie Applications Trie, [3] String Search Term</li>
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

    public ArrayList<String> getApplications() {
        return this.allApplicationsThatBeginWith;
    }

    public boolean isPasswordStored(){
        return this.databaseReturnBoolean;
    }

    public LogIn getVerification(){
        return this.databaseLogInStatus;
    }

    public ArrayList<Record> getRecords(){
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
                            "Invalid Username", JOptionPane.QUESTION_MESSAGE);
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
            case PULL_PASSWORD:
                this.databaseReturnRecords = database.pullPasswords((String)parameters[2]);
                break;
            case STORE_PASSWORD:
                this.databaseReturnBoolean = database.storeGeneratedPassword((String)parameters[0], (String)parameters[1], (String)parameters[2], (String)parameters[3], (String)parameters[4], (String)parameters[5]);
                break;
            case FIND_APPLICATIONS:
                this.allApplicationsThatBeginWith = ((Trie)parameters[2]).findWordsBeginningWith((String)parameters[3]);
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
                    this.gui.setInstance("portal");
                    ((JFrame)parameters[2]).dispose();
                } else if (dbResult == LogIn.REGISTER) {
                    JOptionPane.showMessageDialog(((JFrame)parameters[2]), "Please input your desired password.",
                            "Registration Instructions", JOptionPane.QUESTION_MESSAGE);
                    this.passField.setText("");
                    this.database.setPassword(new Password());
                    this.passField.setEchoChar((char) 0);
                    this.loginButton.setText("Register");
                }

                break;

            case ADD_USER:
                if(this.isUserAdded()){
                    JOptionPane.showMessageDialog(null, "Registration Successful. Please Log In!", "Registration", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Registration Unsuccessful.", "Registration", JOptionPane.ERROR_MESSAGE);
                }

                this.passField.setEchoChar(((char)parameters[2]));
                this.loginButton.setText("Log In");
                break;

            case PULL_PASSWORDS:
                ArrayList<Record> records = this.getRecords();
                Trie applicationsTrie = (Trie)this.parameters[2];
                DefaultTableModel tableModel = (DefaultTableModel)((JTable)this.parameters[0]).getModel();

                for(int record = 0; record < records.size(); record++){
                    Object[] row = new Object[3];
                    for(int field = 0; field < 3; field ++){
                        row[field] = records.get(record).getRecord()[field];
                    }
                    
                    tableModel.addRow(row);
                }

                for (Record r : records){
                    applicationsTrie.insert(r.getApplication(), r);
                }

                ((JFrame)parameters[1]).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                break;

            case PULL_PASSWORD:
                records = this.getRecords();
                tableModel = (DefaultTableModel)((JTable)this.parameters[0]).getModel();

                for(int record = 0; record < records.size(); record++){

                    Object[] row = new Object[3];
                    
                    for(int field = 0; field < 3; field ++){
                        row[field] = records.get(record).getRecord()[field];
                    }
                    
                    tableModel.addRow(row);
                }

                ((JFrame)parameters[1]).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                break;

            case FIND_APPLICATIONS:
                ArrayList<String> applications = this.getApplications();
                tableModel = (DefaultTableModel)((JTable)this.parameters[0]).getModel();

                for(int application = 0; application < applications.size(); application++){

                    Object[] row = new Object[3];
                    Record r = (((Trie)parameters[2]).getRecordOfApplication(applications.get(application)));

                    for(int field = 0; field < 3; field ++){
                        row[field] = r.getRecord()[field];
                    }
                    
                    tableModel.addRow(row);
                }

                ((JFrame)parameters[1]).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                break;

            case STORE_PASSWORD:
                if(this.isPasswordStored()){
                    System.out.println("Data storage completed successfuly.");
                }

                ((JFrame)parameters[6]).setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                try {
                    this.gui.fillTable(((JFrame)parameters[6]));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            default:
                break;
        }
        
        choice = TaskManager.NO_CHOICE;
    }
}