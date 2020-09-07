package com.smartcoins.models;

/**
 * Created by ravi on 20/02/18.
 */

public class Product {
    public static final String TABLE_NAME = "product_details";
    public static final String TABLE_NAME_PAY = "payments";
    public static final String TABLE_NAME_PARENT = "product_parents";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SOC_ID = "soc_id";
    public static final String COLUMN_PROD_NAME = "product_name";
    public static final String COLUMN_CUST_NAME = "customer_name";
    public static final String COLUMN_FK = "fk_parent";
    public static final String COLUMN_PROD_ID = "product_id";
    public static final String COLUMN_TOSELL = "tosell";
    public static final String COLUMN_ROWID = "row_id";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_CUSTOMCODE = "customcode";
    public static final String COLUMN_SYNC = "sync"; //0-no, 1-yes
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String name;
    private String sync;
    private String soc_id;
    private String tosell;
    private String cust_name;
    private String customcode;
    private String price;
    private String product_id;
    private String timestamp;



    // Create table SQL query
    public static final String CREATE_TABLE_PRODUCT =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_PRICE + " TEXT,"
                    + COLUMN_PROD_ID + " TEXT,"
                    + COLUMN_CUSTOMCODE + " TEXT,"
                    + COLUMN_TOSELL + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public static final String CREATE_TABLE_PRODUCT_PARENT =
            "CREATE TABLE " + TABLE_NAME_PARENT + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_FK + " TEXT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_ROWID + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";


    public static final String CREATE_TABLE_PAY =
            "CREATE TABLE " + TABLE_NAME_PAY + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PROD_NAME + " TEXT,"
                    + COLUMN_CUST_NAME + " TEXT,"
                    + COLUMN_PROD_ID + " TEXT,"
                    + COLUMN_PRICE + " TEXT,"
                    + COLUMN_SOC_ID + " TEXT,"
                    + COLUMN_SYNC + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Product() {
    }


    public String getCustomcode() {
        return customcode;
    }

    public void setCustomcode(String customcode) {
        this.customcode = customcode;
    }

    public String getCust_name() {
        return cust_name;
    }

    public Product(int id, String soc_id, String name, String product_id, String price, String timestamp, String syc) {
        this.id = id;
        this.name = name;
        this.sync = syc;
        this.soc_id = soc_id;
        this.product_id = product_id;
        this.price = price;
        this.timestamp = timestamp;
    }

    public String getSoc_id() {
        return soc_id;
    }

    public void setSoc_id(String soc_id) {
        this.soc_id = soc_id;
    }

    public Product(int id, String name, String product_id, String price,String tosell, String timestamp) {
        this.id = id;
        this.name = name;
        this.product_id =product_id;
        this.price = price;
        this.tosell =tosell;
        this.timestamp = timestamp;
    }

    public String getTosell() {
        return tosell;
    }

    public void setTosell(String tosell) {
        this.tosell = tosell;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }
    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
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
