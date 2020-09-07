package com.smartcoins.models;

/**
 * Created by ravi on 20/02/18.
 */

public class Category {
    public static final String TABLE_NAME = "categories";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ITEMID = "item_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FK = "fk_parent";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String name;
    private String item_id;
    private String parent;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_ITEMID + " TEXT,"
                    + COLUMN_FK + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Category() {
    }

    public Category(int id, String name,String item_id,String parent, String timestamp) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.item_id = item_id;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getParent() {
        return parent;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return name;
    }
}
