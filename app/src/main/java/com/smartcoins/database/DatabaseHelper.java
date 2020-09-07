package com.smartcoins.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.smartcoins.models.Category;
import com.smartcoins.models.Customer;
import com.smartcoins.models.Product;
import com.smartcoins.models.User;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name

    private static final String DATABASE_NAME = "smartCoins_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(User.CREATE_TABLE);
        db.execSQL(Customer.CREATE_TABLE);
        db.execSQL(Category.CREATE_TABLE);
        db.execSQL(Product.CREATE_TABLE_PRODUCT);
        db.execSQL(Product.CREATE_TABLE_PAY);
        db.execSQL(Product.CREATE_TABLE_PRODUCT_PARENT);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Customer.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Category.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Product.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Product.TABLE_NAME_PAY);
        db.execSQL("DROP TABLE IF EXISTS " + Product.TABLE_NAME_PARENT);
        // Create tables again
        onCreate(db);
    }



    public long insertUser(String name,String number,String passord,String userid,String api) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(User.COLUMN_NAME, name);
        values.put(User.COLUMN_PHONE, number);
        values.put(User.COLUMN_USERID, userid);
        values.put(User.COLUMN_API, api);
        values.put(User.COLUMN_PASSWORD, passord);
        // insert row
        long id = db.insert(User.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }


    public long insertCustomer(String name,String number,String soc_id,String isnew) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Customer.COLUMN_NAME, name);
        values.put(Customer.COLUMN_NUMBER, number);
        values.put(Customer.COLUMN_SOC_ID, soc_id);
        values.put(Customer.COLUMN_ISNEW, isnew);
        // insert row
        long id = db.insert(Customer.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public long insertPayment(String cust_name,String prod_name,String price,String row_id,String soc_id,String sync) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them

        values.put(Product.COLUMN_PROD_NAME, prod_name);
        values.put(Product.COLUMN_CUST_NAME, cust_name);
        values.put(Product.COLUMN_PROD_ID, row_id);
        values.put(Product.COLUMN_PRICE, price);
        values.put(Product.COLUMN_SOC_ID, soc_id);
        values.put(Product.COLUMN_SYNC, sync);
        // insert row
        long id = db.insert(Product.TABLE_NAME_PAY,null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }



    public long insertCategory(String item_id,String name,String parent) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Category.COLUMN_NAME, name);
        values.put(Category.COLUMN_ITEMID, item_id);
        values.put(Category.COLUMN_FK, parent);
        // insert row
        long id = db.insert(Category.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public long insertProduct(String item_id,String name,String price,String tosell,String code) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Product.COLUMN_NAME, name);
        values.put(Product.COLUMN_PROD_ID, item_id);
        values.put(Product.COLUMN_TOSELL, tosell);
        values.put(Product.COLUMN_CUSTOMCODE, code);
        values.put(Product.COLUMN_PRICE, price);
        // insert row
        long id = db.insert(Product.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public Customer getCustomer(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Customer.TABLE_NAME,
                new String[]{Customer.COLUMN_ID, Customer.COLUMN_NAME,Customer.COLUMN_NUMBER,Customer.COLUMN_SOC_ID,Customer.COLUMN_ISNEW, Customer.COLUMN_TIMESTAMP},
                Customer.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Customer customer = new Customer(
                cursor.getInt(cursor.getColumnIndex(Customer.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Customer.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(Customer.COLUMN_NUMBER)),
                cursor.getString(cursor.getColumnIndex(Customer.COLUMN_SOC_ID)),
                cursor.getString(cursor.getColumnIndex(Customer.COLUMN_ISNEW)),
                cursor.getString(cursor.getColumnIndex(Customer.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return customer;
    }


    public User LoginUser(String phone,String pin) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =db.query(User.TABLE_NAME,
                new String[]{User.COLUMN_ID,User.COLUMN_USERID,User.COLUMN_PHONE, User.COLUMN_NAME, User.COLUMN_PASSWORD,User.COLUMN_API,User.COLUMN_TIMESTAMP},
                User.COLUMN_PHONE + "=? AND password=?",
                new String[]{String.valueOf(phone),String.valueOf(pin)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        User user = new User(
                cursor.getInt(cursor.getColumnIndex(User.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(User.COLUMN_PHONE)),
                cursor.getString(cursor.getColumnIndex(User.COLUMN_USERID)),
                cursor.getString(cursor.getColumnIndex(User.COLUMN_PASSWORD)),
                cursor.getString(cursor.getColumnIndex(User.COLUMN_API)),
                cursor.getString(cursor.getColumnIndex(User.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return user;
    }

    public Category getCategory(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Category.TABLE_NAME,
                new String[]{Category.COLUMN_ID,Category.COLUMN_ITEMID, Category.COLUMN_NAME, Category.COLUMN_FK},
                Customer.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Category category = new Category(
                cursor.getInt(cursor.getColumnIndex(Category.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Category.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(Category.COLUMN_ITEMID)),
                cursor.getString(cursor.getColumnIndex(Category.COLUMN_FK)),
                cursor.getString(cursor.getColumnIndex(Category.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return category;
    }


    public List<Customer> getPendingCustomers(){
        List<Customer> customers = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Customer.TABLE_NAME +" WHERE soc_id = 0  ORDER BY " +
                Customer.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();
                customer.setId(cursor.getInt(cursor.getColumnIndex(Customer.COLUMN_ID)));
                customer.setName(cursor.getString(cursor.getColumnIndex(Customer.COLUMN_NAME)));
                customer.setNumber(cursor.getString(cursor.getColumnIndex(Customer.COLUMN_NUMBER)));
                customer.setSoc_id(cursor.getString(cursor.getColumnIndex(Customer.COLUMN_SOC_ID)));
                customer.setIsnew(cursor.getString(cursor.getColumnIndex(Customer.COLUMN_ISNEW)));
                customer.setTimestamp(cursor.getString(cursor.getColumnIndex(Customer.COLUMN_TIMESTAMP)));

                customers.add(customer);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return customers;
    }

    public List<Customer> getAllNotes() {
        List<Customer> customers = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Customer.TABLE_NAME + " ORDER BY " +
                Customer.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();
                customer.setId(cursor.getInt(cursor.getColumnIndex(Customer.COLUMN_ID)));
                customer.setName(cursor.getString(cursor.getColumnIndex(Customer.COLUMN_NAME)));
                customer.setNumber(cursor.getString(cursor.getColumnIndex(Customer.COLUMN_NUMBER)));
                customer.setSoc_id(cursor.getString(cursor.getColumnIndex(Customer.COLUMN_SOC_ID)));
                customer.setIsnew(cursor.getString(cursor.getColumnIndex(Customer.COLUMN_ISNEW)));
                customer.setTimestamp(cursor.getString(cursor.getColumnIndex(Customer.COLUMN_TIMESTAMP)));

                customers.add(customer);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return customers;
    }

    public List<Customer> getLastCustomers() {
        List<Customer> customers = new ArrayList<>();

        // Select All Query
     String selectQuery = "SELECT  * FROM " + Customer.TABLE_NAME + " WHERE soc_id !=  0 ORDER BY " +
                Customer.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();
                customer.setSoc_id(cursor.getString(cursor.getColumnIndex(Customer.COLUMN_SOC_ID)));

                customers.add(customer);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return customers;
    }




    public List<Product> getSyncedPayments() {
        List<Product> products = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Product.TABLE_NAME_PAY + " WHERE "+Product.COLUMN_SYNC+" = 1 "+" ORDER BY " +
                Product.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(Product.COLUMN_ID)));
                product.setProduct_id(cursor.getString(cursor.getColumnIndex(Product.COLUMN_PROD_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(Product.COLUMN_PROD_NAME)));
                product.setCust_name(cursor.getString(cursor.getColumnIndex(Product.COLUMN_CUST_NAME)));
                product.setPrice(cursor.getString(cursor.getColumnIndex(Product.COLUMN_PRICE)));
                product.setSync(cursor.getString(cursor.getColumnIndex(Product.COLUMN_SYNC)));
                product.setSoc_id(cursor.getString(cursor.getColumnIndex(Product.COLUMN_SOC_ID)));
                product.setTimestamp(cursor.getString(cursor.getColumnIndex(Product.COLUMN_TIMESTAMP)));

                products.add(product);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return products;
    }




    public List<Product> getAllPayments() {
        List<Product> products = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Product.TABLE_NAME_PAY + " WHERE "+Product.COLUMN_SYNC+" = 0 "+" ORDER BY " +
                Product.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(Product.COLUMN_ID)));
                product.setProduct_id(cursor.getString(cursor.getColumnIndex(Product.COLUMN_PROD_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(Product.COLUMN_PROD_NAME)));
                product.setCust_name(cursor.getString(cursor.getColumnIndex(Product.COLUMN_CUST_NAME)));
                product.setPrice(cursor.getString(cursor.getColumnIndex(Product.COLUMN_PRICE)));
                product.setSync(cursor.getString(cursor.getColumnIndex(Product.COLUMN_SYNC)));
                product.setSoc_id(cursor.getString(cursor.getColumnIndex(Product.COLUMN_SOC_ID)));
                product.setTimestamp(cursor.getString(cursor.getColumnIndex(Product.COLUMN_TIMESTAMP)));

                products.add(product);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();
        // return notes list
        return products;
    }


    public List<Product> getAllProducts(String s) {
        List<Product> products = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Product.TABLE_NAME +" WHERE CAST(price as integer) > 0 AND CAST(tosell as integer) = 0 AND "+Product.COLUMN_CUSTOMCODE+" = " + s + " ORDER BY " +
                Product.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(Product.COLUMN_ID)));
                product.setProduct_id(cursor.getString(cursor.getColumnIndex(Product.COLUMN_PROD_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(Product.COLUMN_NAME)));
                product.setPrice(cursor.getString(cursor.getColumnIndex(Product.COLUMN_PRICE)));
                product.setCustomcode(cursor.getString(cursor.getColumnIndex(Product.COLUMN_CUSTOMCODE)));
                product.setTosell(cursor.getString(cursor.getColumnIndex(Product.COLUMN_TOSELL)));
                product.setTimestamp(cursor.getString(cursor.getColumnIndex(Product.COLUMN_TIMESTAMP)));

                products.add(product);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return products;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Product.TABLE_NAME +" WHERE CAST(price as integer) > 0 AND CAST(tosell as integer) = 0 ORDER BY " +
                Product.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(Product.COLUMN_ID)));
                product.setProduct_id(cursor.getString(cursor.getColumnIndex(Product.COLUMN_PROD_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(Product.COLUMN_NAME)));
                product.setPrice(cursor.getString(cursor.getColumnIndex(Product.COLUMN_PRICE)));
                product.setCustomcode(cursor.getString(cursor.getColumnIndex(Product.COLUMN_CUSTOMCODE)));
                product.setTosell(cursor.getString(cursor.getColumnIndex(Product.COLUMN_TOSELL)));
                product.setTimestamp(cursor.getString(cursor.getColumnIndex(Product.COLUMN_TIMESTAMP)));

                products.add(product);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return products;
    }

    public List<Product> getTodaySoldProductss() {
        List<Product> products = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Product.TABLE_NAME +" WHERE CAST(price as integer) > 0 AND CAST(tosell as integer) != 0 ORDER BY " +
                Product.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(Product.COLUMN_ID)));
                product.setProduct_id(cursor.getString(cursor.getColumnIndex(Product.COLUMN_PROD_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(Product.COLUMN_NAME)));
                product.setPrice(cursor.getString(cursor.getColumnIndex(Product.COLUMN_PRICE)));
                product.setTosell(cursor.getString(cursor.getColumnIndex(Product.COLUMN_TOSELL)));
                product.setTimestamp(cursor.getString(cursor.getColumnIndex(Product.COLUMN_TIMESTAMP)));

                products.add(product);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return products;
    }

    public List<Product> gettoSellToday(String tosell) {
        List<Product> products = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Product.TABLE_NAME +" WHERE CAST(price as integer) > 0 AND tosell = "+tosell+" ORDER BY " +
                Product.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(Product.COLUMN_ID)));
                product.setProduct_id(cursor.getString(cursor.getColumnIndex(Product.COLUMN_PROD_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(Product.COLUMN_NAME)));
                product.setPrice(cursor.getString(cursor.getColumnIndex(Product.COLUMN_PRICE)));
                product.setTosell(cursor.getString(cursor.getColumnIndex(Product.COLUMN_TOSELL)));
                product.setTimestamp(cursor.getString(cursor.getColumnIndex(Product.COLUMN_TIMESTAMP)));

                products.add(product);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return products;
    }


    public List<Category> getCategories(long id) {
        List<Category> categories = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Category.TABLE_NAME + " WHERE " + Category.COLUMN_FK+" = " +id+ " ORDER BY " +
                Category.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Category customer = new Category();
                customer.setId(cursor.getInt(cursor.getColumnIndex(Category.COLUMN_ID)));
                customer.setName(cursor.getString(cursor.getColumnIndex(Category.COLUMN_NAME)));
                customer.setItem_id(cursor.getString(cursor.getColumnIndex(Category.COLUMN_ITEMID)));
                customer.setParent(cursor.getString(cursor.getColumnIndex(Category.COLUMN_FK)));
                customer.setTimestamp(cursor.getString(cursor.getColumnIndex(Customer.COLUMN_TIMESTAMP)));

                categories.add(customer);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return categories;
    }



    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + Customer.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateNote(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Customer.COLUMN_SOC_ID, customer.getSoc_id());

        // updating row
        return db.update(Customer.TABLE_NAME, values, Customer.COLUMN_ID + " = ?",
                new String[]{String.valueOf(customer.getId())});
    }


    public int syncPayment(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Product.COLUMN_SYNC, "1");

        // updating row
        return db.update(Product.TABLE_NAME_PAY, values, Product.COLUMN_PROD_ID + " = ?",
                new String[]{String.valueOf(product.getProduct_id())});
    }

    public void updatePaymentforCustomer(String newid,String oldid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+Product.TABLE_NAME_PAY +" SET " +Product.COLUMN_SOC_ID + " = " + newid +" WHERE "+ Product.COLUMN_SOC_ID + " = "+ oldid);
        db.close();
    }

    public void updateCustomerSocId(String socid,String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+Customer.TABLE_NAME +" SET " +Customer.COLUMN_SOC_ID + " = " + socid +" WHERE "+ Customer.COLUMN_ID + " = "+ id);
        db.close();
    }

    public void updatePaymentSync(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+Product.TABLE_NAME_PAY +" SET " +Product.COLUMN_SYNC + " = 1 WHERE "+ Product.COLUMN_ID + " = "+ id);
        db.close();
    }



    public int customerPaymentscount(String id) {

        String countQuery = "SELECT * FROM "+Product.TABLE_NAME_PAY +" WHERE " +Product.COLUMN_SOC_ID + " = "+ id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }



    public void clearCustomers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM customers WHERE 1");
        db.close();
    }

    public void updatesoldproductfortoday(String id,String tosell) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+Product.TABLE_NAME+" SET "+Product.COLUMN_TOSELL+" = "+tosell +" WHERE id = "+id);
        db.close();
    }



    public void reffreshsoldproductfortoday() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+Product.TABLE_NAME+" SET "+Product.COLUMN_TOSELL+" = "+"0"+" WHERE 1");
        db.close();
    }

    public void updateCustomers(String id,String soc_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE customers SET soc_id = "+ soc_id +" WHERE id = "+id);
        db.close();
    }

    public void clearCategories() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM categories WHERE 1");
        db.close();
    }

    public void cleardb() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM categories WHERE 1");
        db.execSQL("DELETE FROM product_details WHERE 1");
        db.execSQL("DELETE FROM payments WHERE 1");
        db.execSQL("DELETE FROM customers WHERE 1");
        db.close();
    }

    public void clearProducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM product_details WHERE 1");
        db.close();
    }

    public void clearUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+User.TABLE_NAME+" WHERE 1");
        db.close();
    }

    public void deleteNote(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Customer.TABLE_NAME, Customer.COLUMN_ID + " = ?",
                new String[]{String.valueOf(customer.getId())});
        db.close();
    }
}
