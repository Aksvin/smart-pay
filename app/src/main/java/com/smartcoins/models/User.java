package com.smartcoins.models;

/**
 * Created by ravi on 20/02/18.
 */

public class User {
    public static final String TABLE_NAME = "users";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERID = "user_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_API = "api_key";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String name;
    private String api;
    private String phone;
    private String userid;
    private String password;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_USERID + " TEXT,"
                    + COLUMN_PHONE + " TEXT,"
                    + COLUMN_PASSWORD + " TEXT,"
                    + COLUMN_API + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public User() {
    }

    public User(int id, String name, String phone, String userid, String password,String api, String timestamp) {
        this.id = id;
        this.name = name;
        this.api = api;
        this.phone = phone;
        this.userid = userid;
        this.password = password;
        this.timestamp = timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserid() {
        return userid;
    }

    public String getPassword() {
        return password;
    }

    public String getTimestamp() {
        return timestamp;
    }


    @Override
    public String toString() {
        return name;
    }
}
