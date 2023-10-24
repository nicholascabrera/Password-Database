package com.password_db.gui;

public class Record {
    private String website, username, password;

    public Record(){}

    public Record(String website, String username, String password){
        this.website = website;
        this.username = username;
        this.password = password;
        this.formatRecord();
    }

    public String formatRecord(){
        return String.format("|%20s|%20s|%40s|\n", this.website, this.username, this.password);
    }

    public static Record[] append(Record[] records, Record newRecord){
        Record newRecords[] = new Record[records.length+1];
        for(int i = 0; i < records.length; i++){
            newRecords[0] = records[0];
        }

        newRecords[newRecords.length-1] = newRecord;
        return newRecords;
    }
}
