package com.smartcoins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.smartcoins.database.DatabaseHelper;
import com.smartcoins.models.Customer;
import com.smartcoins.models.Product;
import com.smartcoins.utils.CustomersAdapter;
import com.smartcoins.utils.Helpers;
import com.smartcoins.utils.NetworkCheck;
import com.smartcoins.utils.ProductAdapter;
import com.smartcoins.utils.RecyclerTouchListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class PaymentActivity extends AppCompatActivity {
    private ProductAdapter mAdapter;
    private ArrayList<Product> productlists = new ArrayList<>();
    private DatabaseHelper db;
    private Toolbar bar;
    private RecyclerView recyclerView;
    private List<Customer> pendinglist = new ArrayList<>();
    private ProgressDialog dialog;
    public TabItem syncd,unsycd;
    public TabLayout tabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        dialog = new ProgressDialog(this);
        db = new DatabaseHelper(this);
        tabs = findViewById(R.id.tablayout);

        syncd = findViewById(R.id.syncdtab);
        unsycd = findViewById(R.id.unsyncdtab);

        recyclerView = findViewById(R.id.recycler_product);
        productlists.addAll(db.getAllPayments());
        //Toast.makeText(this, productlists.toString(), LENGTH_LONG).show();

        bar = findViewById(R.id.toolbar);
        bar.setTitleTextColor(Color.WHITE);
        bar.setTitle("Payments");
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(pendinglist.size()>0) {
            pendinglist.clear();
        }
        pendinglist.addAll(db.getPendingCustomers());
        mAdapter = new ProductAdapter(this, productlists,2);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        tabs.getTabAt(0).setText("Un-synchronized "+String.valueOf(db.getAllPayments().size()));
        tabs.getTabAt(1).setText("Synchronized "+String.valueOf(db.getSyncedPayments().size()));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Helpers.show(getApplicationContext(),tab.getText().toString()+" Payments");
                if(tab.getText().toString().contains("Synchronized")){
                    productlists.clear();
                    productlists.addAll(db.getSyncedPayments());
                    mAdapter = new ProductAdapter(PaymentActivity.this, productlists,2);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PaymentActivity.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                }

                if(tab.getText().toString().contains("Un-synchronized")){
                    productlists.clear();
                    productlists.addAll(db.getAllPayments());
                    mAdapter = new ProductAdapter(PaymentActivity.this, productlists,2);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PaymentActivity.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                productlists.clear();

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Helpers.show(getApplicationContext(),tab.getText().toString()+" Payments");

                if(tab.getText().toString().equals("Synchronized")){
                    productlists.clear();
                    productlists.addAll(db.getSyncedPayments());
                    mAdapter = new ProductAdapter(PaymentActivity.this, productlists,2);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PaymentActivity.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                }

                if(tab.getText().toString().equals("Un-synchronized")){
                    productlists.clear();
                    productlists.addAll(db.getAllPayments());
                    mAdapter = new ProductAdapter(PaymentActivity.this, productlists,2);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PaymentActivity.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                }

            }
        });



        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Helpers.show(getApplicationContext(),String.valueOf(productlists.get(position).getSoc_id()));
                //prod_name = productlists.get(position).getName();

            }

            @Override
            public void onLongClick(View view, int position) {
                //showActionsDialog(position);
            }
        }));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_payment, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId()==R.id.action_sync){

            item.setVisible(false);
            if(NetworkCheck.isNetworkAvailable(getApplicationContext())){
                dialog.setMessage("Processing payment ..");
                dialog.setCancelable(false);
                dialog.show();

           if(pendinglist.size()==0) {

            if(productlists.size()>0){

                for(int i = 0; i<productlists.size(); i++){

                    payment(productlists.get(i));

                }



            }else{
                dialog.hide();
                item.setVisible(true);
                Helpers.show(PaymentActivity.this,"No Pending payments available");
            }
           }else{
               dialog.hide();
               item.setVisible(true);
               Helpers.show(PaymentActivity.this,"Please Synchronize the custmers first before running payments");
           }

            }else{
                dialog.hide();
                item.setVisible(true);
                Helpers.show(PaymentActivity.this,"No Internet");
            }


            //startActivity(new Intent(PaymentActivity.this, ProductActivity.class));


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }




    public void payment(final Product bill){

        Ion.with(getApplicationContext())
                .load("POST", "http://smartbusiness.smartcoins.space/api/index.php/invoices")
                .addHeader("Accept", "application/json")
                .addHeader("DOLAPIKEY", Helpers.getSharedPreferencesString(getApplicationContext(),Helpers.Keys.api,"QqHJWv38giuk2T2Y2Sp3ys46Pd89FPUg"))
                .setBodyParameter("socid", bill.getSoc_id())
                .setBodyParameter("date", "1592690400")
                .setBodyParameter("datem", "1592690400")
                .setBodyParameter("type", "0")
                .setBodyParameter("paye", "1")
                .setBodyParameter("module_source", "takepos")
                .setBodyParameter("pos_source", "2")
                .setBodyParameter("linked_objects", "[]")
                .setBodyParameter("date_lim_reglement", "1592690400")
                .setBodyParameter("products", "[]")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (result != null) {
                            final String invoiceid = result;
                            Toast.makeText(getApplicationContext(),"invoice id "+result,LENGTH_LONG).show();
                            //validate invoice
                            Ion.with(getApplicationContext())
                                    .load("POST", "http://smartbusiness.smartcoins.space/api/index.php/invoices/" + invoiceid + "/lines")
                                    .addHeader("Accept", "application/json")
                                    .addHeader("DOLAPIKEY", Helpers.getSharedPreferencesString(getApplicationContext(),Helpers.Keys.api,"QqHJWv38giuk2T2Y2Sp3ys46Pd89FPUg"))
                                    .setBodyParameter("localtax1_type", "0")
                                    .setBodyParameter("localtax2_type", "0")
                                    .setBodyParameter("rank", "1")
                                    .setBodyParameter("pa_ht", "0.00000000")
                                    .setBodyParameter("marque_tx", "100")
                                    .setBodyParameter("shed_percent", "0")
                                    .setBodyParameter("special_code", "0")
                                    .setBodyParameter("fk_code_ventilation", "0")
                                    .setBodyParameter("product_desc", "")
                                    .setBodyParameter("situation_percent", "100")
                                    .setBodyParameter("qty", "1")
                                    .setBodyParameter("subprice", bill.getPrice())
                                    .setBodyParameter("product_type", "0")
                                    .setBodyParameter("fk_product", "4")
                                    .setBodyParameter("array_options", "[]")
                                    .setBodyParameter("fk_product_type", "0")
                                    .setBodyParameter("ventilation_code", "0")
                                    .setBodyParameter("fk_accounting_account", "0")
                                    .asString()
                                    .setCallback(new FutureCallback<String>() {
                                        @Override
                                        public void onCompleted(Exception e, String result) {
                                            try {
                                                int n = Integer.parseInt(result);


                                                //Toast.makeText(ProductActivity.this,result,LENGTH_LONG).show();

                                                //perform payment
                                                Ion.with(getApplicationContext())
                                                        .load("POST", "http://smartbusiness.smartcoins.space/api/index.php/invoices/" + invoiceid + "/validate")
                                                        .addHeader("Accept", "application/json")
                                                        .addHeader("DOLAPIKEY", Helpers.getSharedPreferencesString(getApplicationContext(),Helpers.Keys.api,"QqHJWv38giuk2T2Y2Sp3ys46Pd89FPUg"))
                                                        .setBodyParameter("idwarehouse", "0")
                                                        .setBodyParameter("notrigger", "0")
                                                        .asString()
                                                        .setCallback(new FutureCallback<String>() {
                                                            @Override
                                                            public void onCompleted(Exception e, String result) {
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(result);
                                                                    String id = jsonObject.getString("socid");
                                                                    int n = Integer.parseInt(id);
                                                                    Toast.makeText(getApplicationContext(),"validation :"+id,LENGTH_LONG).show();
                                                                    //perform payment
                                                                    Ion.with(getApplicationContext())
                                                                            .load("POST", "http://smartbusiness.smartcoins.space/api/index.php/invoices/" + invoiceid + "/payments")
                                                                            .addHeader("Accept", "application/json")
                                                                            .addHeader("DOLAPIKEY", Helpers.getSharedPreferencesString(getApplicationContext(),Helpers.Keys.api,"QqHJWv38giuk2T2Y2Sp3ys46Pd89FPUg"))
                                                                            .setBodyParameter("datepaye", "1592690400")
                                                                            .setBodyParameter("paiementid", "4")
                                                                            .setBodyParameter("closepaidinvoices", "yes")
                                                                            .setBodyParameter("accountid", "2")
                                                                            .setBodyParameter("num_paiement", "ref1")
                                                                            .setBodyParameter("comment", "string")
                                                                            .setBodyParameter("chqemetteur", "CASH")
                                                                            .setBodyParameter("chqbank", "Cash_-1")
                                                                            .asString()
                                                                            .setCallback(new FutureCallback<String>() {
                                                                                @Override
                                                                                public void onCompleted(Exception e, String result) {
                                                                                    try {
                                                                                        int n = Integer.parseInt(result);
                                                                                        dialog.hide();
                                                                                        Toast.makeText(getApplicationContext(), "payed", LENGTH_LONG).show();
                                                                                        db.updatePaymentSync(String.valueOf(bill.getId()));


                                                                                    } catch(Exception p) {
                                                                                        dialog.hide();
                                                                                        Toast.makeText(getApplicationContext(), "Error payment " + p.getMessage(), LENGTH_LONG).show();
                                                                                    }
                                                                                }
                                                                            });


                                                                } catch(Exception v) {
                                                                    dialog.hide();
                                                                    Toast.makeText(getApplicationContext(), "Error validation  " + v.getMessage(), LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                            } catch(Exception r) {
                                                dialog.hide();
                                                Toast.makeText(getApplicationContext(), "Error lines:  " + r.getMessage(), LENGTH_LONG).show();
                                            }
                                        }
                                    });


                        } else {
                            dialog.hide();
                            Toast.makeText(getApplicationContext(), "Error  " + e.getMessage(), LENGTH_LONG).show();
                        }
                    }
                });

        }




}