package com.smartcoins;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.smartcoins.database.DatabaseHelper;
import com.smartcoins.models.Category;
import com.smartcoins.models.Customer;
import com.smartcoins.models.Product;
import com.smartcoins.models.User;
import com.smartcoins.utils.Helpers;
import com.smartcoins.utils.NetworkCheck;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission_group.CAMERA;
import static android.widget.Toast.LENGTH_LONG;

public class Login extends Activity {

    public Button login;
    public EditText userID,pin;
    private ProgressDialog dialog;
    private List<Category> categories;
    private List<Product> products;
    TextView logo;
    private List<Customer> customerslist;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        if(Helpers.getSharedPreferencesBoolean(getApplicationContext(), Helpers.Keys.loggedin,false)){
            startActivity(new Intent(Login.this, CategoryActivity.class));
            finish();
        }

        dialog = new ProgressDialog(this);

        categories = new ArrayList<>();
        customerslist = new ArrayList<>();
        products =  new ArrayList<>();


        db = new DatabaseHelper(this);


        categories.addAll(db.getCategories(0));
        customerslist.addAll(db.getAllNotes());
        products.addAll(db.getAllProducts());



        if (categories.isEmpty()&& customerslist.isEmpty() && products.isEmpty()){
            if (NetworkCheck.isNetworkAvailable(getApplicationContext())==false){
                Helpers.show(getApplicationContext(),"Please check your internet connection");

            }else{
                syncData();
            }
        }



        userID = findViewById(R.id.empid);
        pin = findViewById(R.id.password);
        login = findViewById(R.id.login);
        logo = findViewById(R.id.logo);
        logo.setVisibility(View.VISIBLE);
        login.setVisibility(View.GONE);
        userID.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                logo.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);

                userID.setVisibility(View.VISIBLE);
            }
        }, 5000);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkCheck.isNetworkAvailable(Login.this)){
                        Helpers.show(getApplicationContext(),"Please check your internet connection, You must sync the device to use it");
                        return;
                }else{

               if(!userID.getText().toString().isEmpty()){
                   userID.setVisibility(View.GONE);
                   pin.setVisibility(View.VISIBLE);
               }

                if(userID.getText().toString().isEmpty()){
                    userID.setError("Enter your phone number to login");
                }
                if(categories.size()==0 && customerslist.size()==0 && products.size()==0){
                    Helpers.show(getApplicationContext(),"Please check your internet connection, You must sync the device to use it");
                    return;
                }
                if(!userID.getText().toString().isEmpty() && !pin.getText().toString().isEmpty() ) {
                    try {
                        //pin.getText().toString()
                        User user = db.LoginUser(userID.getText().toString(), pin.getText().toString());

                        Helpers.putSharedPreferencesBoolean(getApplicationContext(), Helpers.Keys.loggedin, true);
                        Helpers.putSharedPreferencesString(getApplicationContext(), Helpers.Keys.phone, user.getPhone());
                        Helpers.putSharedPreferencesString(getApplicationContext(), Helpers.Keys.id, String.valueOf(user.getId()));
                        Helpers.putSharedPreferencesString(getApplicationContext(), Helpers.Keys.name, user.getName());
                        Helpers.putSharedPreferencesString(getApplicationContext(), Helpers.Keys.api, user.getApi());
                        Helpers.show(getApplicationContext(),user.getApi());
                        startActivity(new Intent(getApplicationContext(),CategoryActivity.class));
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(Login.this, "login Failed wrong phone or pin!", Toast.LENGTH_LONG).show();
                    }
                }

            }
            }
        });



    }

    public void syncData(){

        dialog.setMessage("Preparing App");
        dialog.setCancelable(false);
        dialog.show();

        Ion.with(this)
                .load("GET", "http://smartbusiness.smartcoins.space/api/index.php/users?sortfield=t.rowid&sortorder=ASC%27")
                .addHeader("Accept", "application/json")
                .addHeader("DOLAPIKEY", "QqHJWv38giuk2T2Y2Sp3ys46Pd89FPUg")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject("{ items:" + result + "}");
                                JSONArray jsonArray = jsonObject.getJSONArray("items");

//                                if(jsonArray.length() > products.size()) {
                                db.clearUsers();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject curr = jsonArray.getJSONObject(i);
                                  // Helpers.show(getApplicationContext(),"OPERATOR " + curr.getString("id"));
                                    db.insertUser(curr.getString("firstname")+" " + curr.getString("lastname"), curr.getString("login"), curr.getString("office_fax"), curr.getString("id"),curr.getString("town"));
                                }

//                                }else{
//                                    //do nothing
//                                }

                            } catch (Exception j_e) {

                                Toast.makeText(Login.this, result, LENGTH_LONG).show();
                            }


                        } else {

                            Toast.makeText(Login.this, "Error  " + e.getMessage(), LENGTH_LONG).show();
                        }
                    }
                });


        Ion.with(this)
                .load("GET","http://smartbusiness.smartcoins.space/api/index.php/products?sortfield=t.ref&sortorder=ASC&limit=100")
                .addHeader("Accept","application/json")
                .addHeader("DOLAPIKEY","QqHJWv38giuk2T2Y2Sp3ys46Pd89FPUg")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if(result != null){
                            try {
                                JSONObject jsonObject = new JSONObject("{ items:" +result+ "}");
                                JSONArray jsonArray = jsonObject.getJSONArray("items");

                                if(jsonArray.length() > products.size()) {
                                    db.clearProducts();
                                    products.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject curr = jsonArray.getJSONObject(i);
                                        db.insertProduct(curr.getString("id"),curr.getString("label"),curr.getString("price"),"0",curr.getString("customcode"));
                                    }

                                }else{
                                    //do nothing
                                }

                            }catch (Exception j_e){

                                Toast.makeText(Login.this,result,LENGTH_LONG).show();
                            }



                        }else{

                            Toast.makeText(Login.this,"Error  " + e.getMessage(),LENGTH_LONG).show();}
                    }});





        Ion.with(this)
                .load("GET","http://smartbusiness.smartcoins.space/api/index.php/categories?sortfield=t.rowid&sortorder=ASC&limit=100")
                .addHeader("Accept","application/json")
                .addHeader("DOLAPIKEY","QqHJWv38giuk2T2Y2Sp3ys46Pd89FPUg")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if(result != null){
                            try {
                                JSONObject jsonObject = new JSONObject("{ items:" +result+ "}");
                                JSONArray jsonArray = jsonObject.getJSONArray("items");

                                if(jsonArray.length() > categories.size()) {
                                    db.clearCategories();
                                    categories.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject curr = jsonArray.getJSONObject(i);
                                        db.insertCategory(curr.getString("id"),curr.getString("label"),curr.getString("fk_parent"));
                                    }

                                }else{
                                    //do nothing
                                }

                            }catch (Exception j_e){
                                //dialog.hide();
                                Toast.makeText(Login.this,result,LENGTH_LONG).show();
                            }



                        }else{
                            //dialog.hide();
                            Toast.makeText(Login.this,"Error  " + e.getMessage(),LENGTH_LONG).show();}
                    }});


        Ion.with(this)
                .load("http://smartbusiness.smartcoins.space/api/index.php/thirdparties?sortfield=t.rowid&sortorder=ASC&limit=100")
                .addHeader("Accept","application/json")
                .addHeader("DOLAPIKEY","QqHJWv38giuk2T2Y2Sp3ys46Pd89FPUg")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if(result != null){
                            try {
                                JSONObject jsonObject = new JSONObject("{ items:" +result+ "}");
                                JSONArray jsonArray = jsonObject.getJSONArray("items");

                                if(jsonArray.length() < customerslist.size()  || jsonArray.length() > customerslist.size() ) {
                                    db.clearCustomers();

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject curr = jsonArray.getJSONObject(i);
                                        createNote(curr.getString("name"),curr.getString("phone"),curr.getString("id"));
                                    }
                                    dialog.hide();

                                }else{
                                    //do nothing
                                }


                            }catch (Exception j_e){
                                dialog.hide();
                                Toast.makeText(Login.this,j_e.getMessage(),LENGTH_LONG).show();
                            }



                        }else{
                            dialog.hide();
                            Toast.makeText(Login.this,"Error  " + e.getMessage(),LENGTH_LONG).show();}
                    }});


    }




    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);

        return result == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestPermission() {

        Toast.makeText(this, "Please request permission.", Toast.LENGTH_LONG).show();
        ActivityCompat.requestPermissions(this, new String[]{INTERNET}, 200);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted)
                        Toast.makeText(Login.this, "Permission Granted, Now you can access internet.",Toast.LENGTH_LONG).show();
                    else {

                        Toast.makeText(Login.this, "Permission Denied, You cannot access internet.",Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(INTERNET)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{INTERNET},
                                                            200);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }

    private void createNote(String name,String number,String id) {
        // inserting note in db and getting
        // newly inserted note id
       db.insertCustomer(name,number,id,"0");

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}