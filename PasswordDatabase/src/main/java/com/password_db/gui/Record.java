package com.password_db.gui;

public class Record {
    private String record[];

    public Record(){}

    public Record(String website, String username, String password){
        this.record = new String[]{website, username, password};
    }

    public String[] getRecord(){
        return record;
    }

    public String getApplication(){
        return record[0];
    }
}
