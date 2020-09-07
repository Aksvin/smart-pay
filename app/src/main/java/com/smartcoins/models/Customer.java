package com.smartcoins.models;

/**
 * Created by ravi on 20/02/18.
 */

public class Customer {
    public static final String TABLE_NAME = "customers";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_SOC_ID = "soc_id";
    public static final String COLUMN_ISNEW = "isnew";

    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String name,number,isnew,soc_id;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_NUMBER + " TEXT,"
                    + COLUMN_SOC_ID + " TEXT,"
                    + COLUMN_ISNEW + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Customer() {
    }

    public Customer(int id, String name,String number,String soc_id,String isnew, String timestamp) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.soc_id = soc_id;
        this.isnew = isnew;
        this.timestamp = timestamp;
    }


    public String getSoc_id() {
        return soc_id;
    }

    public void setSoc_id(String soc_id) {
        this.soc_id = soc_id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setIsnew(String isnew) {
        this.isnew = isnew;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public String getNumber() {
        return number;
    }

    public String getIsnew() {
        return isnew;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
