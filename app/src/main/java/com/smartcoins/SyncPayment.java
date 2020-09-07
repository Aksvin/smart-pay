package com.smartcoins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.smartcoins.database.DatabaseHelper;
import com.smartcoins.models.Customer;
import com.smartcoins.models.Product;
import com.smartcoins.utils.Helpers;
import com.smartcoins.utils.NetworkCheck;
import com.smartcoins.utils.ProductAdapter;
import com.smartcoins.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class SyncPayment extends AppCompatActivity {
    private ProductAdapter mAdapter;
    private ArrayList<Product> productlists = new ArrayList<>();
    private DatabaseHelper db;
    private Toolbar bar;
    private RecyclerView recyclerView;
    private List<Customer> pendinglist = new ArrayList<>();
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_payment);

        dialog = new ProgressDialog(this);
        db = new DatabaseHelper(this);


        recyclerView = findViewById(R.id.recycler_product);
        productlists.addAll(db.getSyncedPayments());
        //Toast.makeText(this, productlists.toString(), LENGTH_LONG).show();

        bar = findViewById(R.id.toolbar);
        bar.setTitleTextColor(Color.WHITE);
        bar.setTitle("Synchronized");
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pendinglist.addAll(db.getPendingCustomers());
        mAdapter = new ProductAdapter(this, productlists,2);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Helpers.show(getApplicationContext(),String.valueOf(productlists.get(position).getSoc_id()));
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


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}