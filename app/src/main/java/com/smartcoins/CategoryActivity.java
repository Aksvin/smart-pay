package com.smartcoins;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.smartcoins.database.DatabaseHelper;
import com.smartcoins.models.Category;
import com.smartcoins.models.Customer;
import com.smartcoins.utils.Helpers;
import com.smartcoins.utils.NetworkCheck;
import com.smartcoins.utils.SharedPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class CategoryActivity extends AppCompatActivity {
    public Spinner markets,fees,zones,plots;
    public Button customers;
    public static String market,fee,zone,plot,Id;
    public  TextView name,phone;
    private Toolbar bar;
    private List<Category> categories,zonelist,plotlist,products;


    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categories = new ArrayList<>();
        zonelist = new ArrayList<>();
        plotlist = new ArrayList<>();
        products =  new ArrayList<>();

        bar = findViewById(R.id.toolbar);
        bar.setTitleTextColor(Color.WHITE);
        bar.setTitle("Categories");
        setSupportActionBar(bar);

        markets =findViewById(R.id.sp_markets);
        fees = findViewById(R.id.sp_fee_type);
        zones = findViewById(R.id.sp_zones);
        plots = findViewById(R.id.sp_plots);


        name = findViewById(R.id.op_name);
        phone = findViewById(R.id.phone);

        name.setText(Helpers.getSharedPreferencesString(getApplicationContext(), Helpers.Keys.name,""));
        phone.setText(Helpers.getSharedPreferencesString(getApplicationContext(), Helpers.Keys.phone,""));

        customers = findViewById(R.id.btn_view_customers);
        db = new DatabaseHelper(this);




        categories.addAll(db.getCategories(0));
        if (categories.size() > 0) {
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(CategoryActivity.this, android.R.layout.simple_spinner_item, categories.toArray());
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fees.setAdapter(spinnerArrayAdapter);



        fees.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category cat = (Category)parent.getSelectedItem();
                fee = cat.getName();
                zonelist.clear();
                zonelist.addAll(db.getCategories(Long.valueOf(cat.getItem_id())));

                    ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(CategoryActivity.this, android.R.layout.simple_spinner_item, zonelist.toArray());
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    zones.setAdapter(spinnerArrayAdapter);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        zones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category cat = (Category)parent.getSelectedItem();
                zone = cat.getName();
                Id = String.valueOf(cat.getItem_id());
                plotlist.clear();
                plotlist.addAll(db.getCategories(Long.valueOf(cat.getItem_id())));

                    ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(CategoryActivity.this, android.R.layout.simple_spinner_item, plotlist.toArray());

                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    plots.setAdapter(spinnerArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        }

        customers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(plots.getSelectedItem()==null){
                    plot ="";
                }else {
                    plot = plots.getSelectedItem().toString();
                }

                market = markets.getSelectedItem().toString();
                startActivity(new Intent(CategoryActivity.this,MainActivity.class));

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.payments){
            startActivity(new Intent(getApplicationContext(),PaymentActivity.class));

        }

        if (item.getItemId()==R.id.logout){
            db.cleardb();
            Helpers.putSharedPreferencesBoolean(getApplicationContext(), Helpers.Keys.loggedin,false);
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}