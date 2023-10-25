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

    public static Record[] append(Record[] records, Record newRecord){
        Record newRecords[] = new Record[records.length+1];
        for(int i = 0; i < records.length; i++){
            newRecords[0] = records[0];
        }

        newRecords[newRecords.length-1] = newRecord;
        return newRecords;
    }
}
