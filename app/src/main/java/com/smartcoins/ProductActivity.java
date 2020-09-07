package com.smartcoins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.smartcoins.database.DatabaseHelper;
import com.smartcoins.models.Customer;
import com.smartcoins.models.Product;
import com.smartcoins.utils.BillAdapter;
import com.smartcoins.utils.CustomersAdapter;
import com.smartcoins.utils.Helpers;
import com.smartcoins.utils.NetworkCheck;
import com.smartcoins.utils.ProductAdapter;
import com.smartcoins.utils.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class ProductActivity extends AppCompatActivity {

    private TextView noNotesView,market,fee,zone,plot,name,items,txdate;
    private  String prod_name,price,prodID,tosell;
    private ProductAdapter mAdapter;
    private List<Product> productlists = new ArrayList<>();
    private ArrayList<Product> bill = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private DatabaseHelper db;
    private Toolbar bar;
    private ProgressDialog dialog;
    private LinearLayout ll;
    private   FloatingActionButton fab;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        dialog = new ProgressDialog(this);
        ll = findViewById(R.id.ll);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        tosell = date;

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_product);
        noNotesView = findViewById(R.id.empty_notes_view);
        txdate = findViewById(R.id.date);
        items = findViewById(R.id.itemcount);
        items.setText("Added Items : 0");
        txdate.setText(tosell);
        fab = findViewById(R.id.fab);

        bar = findViewById(R.id.toolbar);
        bar.setTitleTextColor(Color.WHITE);
        bar.setTitle("Products");
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        market = findViewById(R.id.market);
        fee = findViewById(R.id.fee);
        zone = findViewById(R.id.zone);
        plot = findViewById(R.id.plot);
        name = findViewById(R.id.customer_name);

        name.setText(MainActivity._name);
        market.setText(MainActivity._market);
        fee.setText(MainActivity._fee);
        zone.setText(MainActivity._zone);
        plot.setText(MainActivity._plot);

       db = new DatabaseHelper(this);

       if (db.gettoSellToday(txdate.getText().toString()).size()== 0 && db.getTodaySoldProductss().size() > 0 ){
           db.reffreshsoldproductfortoday();
       }

        productlists.addAll(db.getAllProducts(CategoryActivity.Id));

        if (productlists.size()==0){
            recyclerView.setVisibility(View.GONE);
            txdate.setText("No products available on "+MainActivity._zone);
            txdate.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            txdate.setVisibility(View.GONE);
        }

        mAdapter = new ProductAdapter(this, productlists,1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                prod_name = productlists.get(position).getName();
                int id = productlists.get(position).getId();
                if(bill.size()>0){

                    for(int i =0; i<bill.size(); i++){
                        if(bill.get(i).getId() == id){
                            Helpers.show(ProductActivity.this,"Product "+prod_name+ " is already selected");
                            return;
                        }
                    }

                    Helpers.show(ProductActivity.this,"Process payment for "+bill.get(0).getName()+ " to continue!");
                    return;
                }

                bill.add(new Product(productlists.get(position).getId(),MainActivity._soc_id,prod_name,productlists.get(position).getProduct_id(),productlists.get(position).getPrice(),productlists.get(position).getTimestamp(),"0"));

                items.setText("Added Items : " +String.valueOf(bill.size()));
                fab.setVisibility(View.VISIBLE);
                Toast.makeText(ProductActivity.this,"Added "+prod_name+" to the bill list",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                //showActionsDialog(position);
            }
        }));


        if(bill.size()==0){
            fab.setVisibility(View.GONE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDialog(false, null, -1);
            }
        });

    }


    private void showNoteDialog(final boolean shouldUpdate, final Customer customer, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(ProductActivity.this);
        alertDialogBuilderUserInput.setView(view);

        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        ListView listView = view.findViewById(R.id.bills);
        BillAdapter adbProduct;
        //ArrayList<Product> myListItems  = new ArrayList<Product>();


        adbProduct = new BillAdapter(ProductActivity.this, 0, bill);
        listView.setAdapter(adbProduct);
        dialogTitle.setText(MainActivity._name + " Bill");

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "pay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            @SuppressLint("RestrictedApi")
                            public void onClick(DialogInterface dialogBox, int id) {
                                bill.clear();
                                fab.setVisibility(View.GONE);
                                dialogBox.cancel();
                            }
                });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (bill.size()==0) {
                    Toast.makeText(ProductActivity.this, "No bill to pay, click Item to add bil", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    payment(bill);
                    alertDialog.dismiss();
                }

            }
        });
    }


    public void payment(final ArrayList<Product> bill){

        if(!NetworkCheck.isNetworkAvailable(getApplicationContext()) || MainActivity._soc_id.equals("0")){
        if(MainActivity._soc_id.equals("0")){
            db.updatesoldproductfortoday(String.valueOf(bill.get(0).getId()),txdate.getText().toString());
            long  id = db.insertPayment(MainActivity._name,bill.get(0).getName(),bill.get(0).getPrice(),bill.get(0).getProduct_id(),MainActivity.ID,"0");

        }else{
            db.updatesoldproductfortoday(String.valueOf(bill.get(0).getId()),txdate.getText().toString());
            long  id = db.insertPayment(MainActivity._name,bill.get(0).getName(),bill.get(0).getPrice(),bill.get(0).getProduct_id(),MainActivity._soc_id,"0");
        }

            startActivity(new Intent(ProductActivity.this, PaymentActivity.class));
            finish();
        }else {

        db.updatesoldproductfortoday(String.valueOf(bill.get(0).getId()),txdate.getText().toString());
        dialog.setMessage("Processing payment ..");
        dialog.setCancelable(false);
        dialog.show();

        Ion.with(this)
                .load("POST", "http://smartbusiness.smartcoins.space/api/index.php/invoices")
                .addHeader("Accept", "application/json")
                .addHeader("DOLAPIKEY", Helpers.getSharedPreferencesString(getApplicationContext(),Helpers.Keys.api,"QqHJWv38giuk2T2Y2Sp3ys46Pd89FPUg"))
                .setBodyParameter("socid", MainActivity._soc_id)
                .setBodyParameter("date", "1592690400")
                .setBodyParameter("datem", "1592690400")
                .setBodyParameter("type", "0")
                .setBodyParameter("paye", "1")
                .setBodyParameter("module_source", "takepos")
                .setBodyParameter("pos_source", "1")
                .setBodyParameter("linked_objects", "[]")
                .setBodyParameter("date_lim_reglement", "1592690400")
                .setBodyParameter("products", "[]")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (result != null) {
                            final String invoiceid = result;
                            Toast.makeText(ProductActivity.this,"invoice id "+result,LENGTH_LONG).show();
                            //validate invoice
                            Ion.with(ProductActivity.this)
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
                                    .setBodyParameter("subprice", bill.get(0).getPrice())
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
                                                Ion.with(ProductActivity.this)
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
                                                                    Ion.with(ProductActivity.this)
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
                                                                                        Toast.makeText(ProductActivity.this, "payed", LENGTH_LONG).show();
                                                                                        startActivity(new Intent(ProductActivity.this, MainActivity.class));


                                                                                    } catch(Exception p) {
                                                                                        dialog.hide();
                                                                                        Toast.makeText(ProductActivity.this, "Error payment " + p.getMessage(), LENGTH_LONG).show();
                                                                                    }
                                                                                }
                                                                            });


                                                                } catch(Exception v) {
                                                                    dialog.hide();
                                                                    Toast.makeText(ProductActivity.this, "Error validation  " + v.getMessage(), LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                            } catch(Exception r) {
                                                dialog.hide();
                                                Toast.makeText(ProductActivity.this, "Error lines:  " + r.getMessage(), LENGTH_LONG).show();
                                            }
                                        }
                                    });


                        } else {
                            dialog.hide();
                            Toast.makeText(ProductActivity.this, "Error  " + e.getMessage(), LENGTH_LONG).show();
                        }
                    }
                });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setIconified(false);
        searchView.onActionViewExpanded();
        searchView.setQueryHint("search products");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {

                //Helpers.show(MainActivity.this,newText);
                if(text.isEmpty()){
                    productlists.clear();
                   productlists.addAll(db.getAllProducts(CategoryActivity.Id));
                    mAdapter = new ProductAdapter(ProductActivity.this, productlists,1);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else{
                    products.addAll(productlists);
                    productlists.clear();
                    text = text.toLowerCase();

                    for(Product item: products){
                        if(item.getName().toLowerCase().contains(text)){
                            productlists.clear();
                            productlists.add(item);
                            mAdapter = new ProductAdapter(ProductActivity.this, productlists,1);
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }

                    }

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {

                //Helpers.show(MainActivity.this,newText);
                if(text.isEmpty()){
                    productlists.clear();
                    productlists.addAll(db.getAllProducts(CategoryActivity.Id));
                    mAdapter = new ProductAdapter(ProductActivity.this, productlists,1);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else{
                    products.addAll(productlists);
                    productlists.clear();
                    text = text.toLowerCase();

                    for(Product item: products){
                        if(item.getName().toLowerCase().contains(text)){
                            productlists.clear();
                            productlists.add(item);
                            mAdapter = new ProductAdapter(ProductActivity.this, productlists,1);
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }

                    }

                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.details){
            if(item.getTitle()=="Show Details"){
                ll.setVisibility(View.VISIBLE);
                item.setTitle("Hide Details");
            }else {
                ll.setVisibility(View.GONE);
                item.setTitle("Show Details");
            }
        }
        if (item.getItemId()==R.id.payments){
                startActivity(new Intent(ProductActivity.this,PaymentActivity.class));
        }
        if (item.getItemId()==R.id.logout){
            db.cleardb();
            Helpers.putSharedPreferencesBoolean(getApplicationContext(), Helpers.Keys.loggedin,false);
            startActivity(new Intent(ProductActivity.this,Login.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}