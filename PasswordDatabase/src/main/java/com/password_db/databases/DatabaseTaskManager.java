package com.password_db.databases;

import javax.swing.SwingWorker;

import com.password_db.gui.Record;

import com.password_db.cryptography.Password;

//this will allow me to use multithreading to avoid my UI from freezing during complicated tasks.
public class DatabaseTaskManager extends SwingWorker<Void, Void>{

    public static final int VERIFY_USER = 0, ADD_USER = 1, PULL_PASSWORDS = 2, STORE_PASSWORD = 3, NO_CHOICE = 4;
    
    private int choice;
    private Object[] parameters;
    private Database database;

    //get the return values from the database
    private boolean databaseReturnBoolean;
    private int databaseReturnInteger;
    private Record[] databaseReturnRecords;


    public DatabaseTaskManager(Database database){
        this.database = database;
        this.choice = NO_CHOICE;
    }

    public void setChoice(int choice){
        this.choice = choice;
    }

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

    public int getVerification(){
        return this.databaseReturnInteger;
    }

    public Record[] getRecords(){
        return this.databaseReturnRecords;
    }

    @Override
    protected Void doInBackground() throws Exception {
        switch(choice){
            case VERIFY_USER:
                this.databaseReturnInteger = database.verifyCredentials((String)parameters[0], (Password)parameters[1]);
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

        choice = NO_CHOICE;
        return null;
    }
}