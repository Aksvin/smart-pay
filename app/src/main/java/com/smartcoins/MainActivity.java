package com.smartcoins;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.smartcoins.database.DatabaseHelper;
import com.smartcoins.models.Customer;
import com.smartcoins.models.Product;
import com.smartcoins.utils.CustomersAdapter;
import com.smartcoins.utils.Helpers;
import com.smartcoins.utils.NetworkCheck;
import com.smartcoins.utils.RecyclerTouchListener;
import com.smartcoins.utils.SharedPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {
    private CustomersAdapter mAdapter;
    private Toolbar bar;
    int LastID;
    private ProgressDialog dialog;
    private List<Customer> customerslist = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Customer> pendinglist = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    public static String _market,_fee,_zone,_plot,_name,_soc_id,ID;
    private RecyclerView recyclerView;
    private LinearLayout ll;
    private TextView noNotesView,market,fee,zone,plot;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noNotesView = findViewById(R.id.empty_notes_view);
        ll = findViewById(R.id.ll);

        bar = findViewById(R.id.toolbar);
        bar.setTitleTextColor(Color.WHITE);
        bar.setTitle("Customers");
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dialog = new ProgressDialog(this);

        market = findViewById(R.id.market);
        fee = findViewById(R.id.fee);
        zone = findViewById(R.id.zone);
        plot = findViewById(R.id.plot);

        _market =CategoryActivity.market;
        _fee  = CategoryActivity.fee;
        _plot = CategoryActivity.plot;
        _zone = CategoryActivity.zone;

        if (_zone==null){
            _zone =" ";
        }if(_plot==null){
            _plot= " ";
        }

        market.setText(_market);
        fee.setText(_fee);
        zone.setText(_zone);
        plot.setText(_plot);


        db = new DatabaseHelper(this);



////        Helpers.show(this,db.getLastCustomers().get(0).getSoc_id());
//        LastID = Integer.valueOf(db.getLastCustomers().get(0).getSoc_id())+1;
//        Helpers.show(this,String.valueOf(LastID));


// if(NetworkCheck.isNetworkAvailable(this) && pendinglist.size()==0 ) {
//
//     dialog.setMessage("Reload customers ..");
//     dialog.show();
//     customerslist.clear();
//     db.clearCustomers();
//     Ion.with(this)
//             .load("http://smartbusiness.smartcoins.space/api/index.php/thirdparties?sortfield=t.rowid&sortorder=ASC&limit=100")
//             .addHeader("Accept", "application/json")
//             .addHeader("DOLAPIKEY", "QqHJWv38giuk2T2Y2Sp3ys46Pd89FPUg")
//             .asString()
//             .setCallback(new FutureCallback<String>() {
//                 @Override
//                 public void onCompleted(Exception e, String result) {
//                     if (result != null) {
//                         try {
//                             JSONObject jsonObject = new JSONObject("{ items:" + result + "}");
//                             JSONArray jsonArray = jsonObject.getJSONArray("items");
//                             db.clearCustomers();
//                             if (jsonArray.length() < customerslist.size() || jsonArray.length() > customerslist.size()) {
//
//                                 for (int i = 0; i < jsonArray.length(); i++) {
//                                     JSONObject curr = jsonArray.getJSONObject(i);
//                                     createNote(curr.getString("name"), curr.getString("phone"), curr.getString("id"));
//                                 }
//
//                             } else {
//                                 //do nothing
//                             }
//                             customerslist.addAll(db.getAllNotes());
//
//                             dialog.hide();
//                         } catch (Exception j_e) {
//                             dialog.hide();
//                             Toast.makeText(MainActivity.this, j_e.getMessage(), LENGTH_LONG).show();
//                         }
//
//
//                     } else {
//                         dialog.hide();
//                         Toast.makeText(MainActivity.this, "Error  " + e.getMessage(), LENGTH_LONG).show();
//                     }
//                 }
//             });
//
//            }else{
//
//            }

        customerslist.addAll(db.getAllNotes());

        mAdapter = new CustomersAdapter(this, customerslist,0);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyNotes();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDialog(false, null, -1);
            }
        });




        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         **/
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Helpers.show(getApplicationContext(),String.valueOf(customerslist.get(position).getId()));
                //Helpers.show(getApplicationContext(),String.valueOf(customerslist.get(position).getSoc_id()));
                    _name = customerslist.get(position).getName();
                    _soc_id = String.valueOf(customerslist.get(position).getSoc_id());
                    ID = String.valueOf(customerslist.get(position).getId());
                    startActivity(new Intent(MainActivity.this,ProductActivity.class));
            }

            @Override
            public void onLongClick(View view, int position) {
                //showActionsDialog(position);
            }
        }));




    }

    /**
     * Inserting new note in db
     * and refreshing the list
     */
    private void createNote(String name,String number,String ids) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertCustomer(name,number,ids,"1");
         //get the newly inserted note from db
        Customer n = db.getCustomer(id);
        if (n != null) {
            // adding new note to array list at 0 position
            customerslist.add(0, n);
            // refreshing the list
            pendinglist.clear();
            pendinglist.addAll(db.getPendingCustomers());
            mAdapter.notifyDataSetChanged();
            toggleEmptyNotes();
        }
    }

    /**
     * Updating note in db and updating
     * item in the list by its position
     */
    private void updateNote(String note, int position) {
        Customer n = customerslist.get(position);
        // updating note text
        n.setName(note);

        // updating note in db
        db.updateNote(n);

        // refreshing the list
        customerslist.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
    }

    /**
     * Deleting note from SQLite and removing the
     * item from the list by its position
     */
    private void deleteNote(int position) {
        // deleting the note from db
        db.deleteNote(customerslist.get(position));

        // removing the note from the list
        customerslist.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, customerslist.get(position), position);
                } else {
                    deleteNote(position);
                }
            }
        });
        builder.show();
    }


    /**
     * Shows alert dialog with EditText options to enter / edit
     * a note.
     * when shouldUpdate=true, it automatically displays old note and changes the
     * button text to UPDATE
     */
    private void showNoteDialog(final boolean shouldUpdate, final Customer customer, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.customer_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputname = view.findViewById(R.id.customer);
        final EditText inputNumber = view.findViewById(R.id.customer_phone);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        if (shouldUpdate && customer != null) {
            inputname.setText(customer.getName());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputname.getText().toString()) || TextUtils.isEmpty(inputNumber.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter name and number of the customer!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && customer != null) {
                    // update note by it's id
                    updateNote(inputname.getText().toString(), position);
                } else {

                    createNote(inputname.getText().toString(),inputNumber.getText().toString(),"0");
                }
            }
        });
    }

    /**
     * Toggling list and empty notes view
     */
    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (db.getNotesCount() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
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
        searchView.setQueryHint("search customers");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {

                //Helpers.show(MainActivity.this,newText);
                if(text.isEmpty()){
                    customerslist.clear();
                    customerslist.addAll(db.getAllNotes());
                    mAdapter = new CustomersAdapter(MainActivity.this, customerslist,0);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else{
                    customers.addAll(customerslist);
                    customerslist.clear();
                    text = text.toLowerCase();

                    for(Customer item: customers){
                        if(item.getName().toLowerCase().contains(text)){
                            customerslist.clear();
                            customerslist.add(item);
                            mAdapter = new CustomersAdapter(MainActivity.this, customerslist,0);
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
                    customerslist.clear();
                    customerslist.addAll(db.getAllNotes());
                    mAdapter = new CustomersAdapter(MainActivity.this, customerslist,0);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else{
                    customers.addAll(customerslist);
                    customerslist.clear();
                    text = text.toLowerCase();

                    for(Customer item: customers){
                        if(item.getName().toLowerCase().contains(text)){
                            customerslist.clear();
                            customerslist.add(item);
                            mAdapter = new CustomersAdapter(MainActivity.this, customerslist,0);
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
        if (item.getItemId()==R.id.action_sync){


            if(NetworkCheck.isNetworkAvailable(getApplicationContext())){
                pendinglist.clear();
                pendinglist.addAll(db.getPendingCustomers());
                if(pendinglist.size()>0){

                    dialog.setMessage("Sync the customers..");
                    dialog.show();
                    for(int i = 0; i < pendinglist.size(); i++){

                        uploadCustomers(pendinglist.get(i),i);
                    }
                    //reloadCustomers();

                }else{
                    Helpers.show(MainActivity.this,"No new customer available");
                }}else{
                Helpers.show(MainActivity.this,"No Internet");
            }





        }
        if (item.getItemId()==R.id.payments){

            startActivity(new Intent(MainActivity.this,PaymentActivity.class));
        }
        if (item.getItemId()==R.id.logout){
            db.cleardb();
            Helpers.putSharedPreferencesBoolean(getApplicationContext(), Helpers.Keys.loggedin,false);
            startActivity(new Intent(MainActivity.this,Login.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public  void reloadCustomers(){

    dialog.setMessage("Reloading customers");
    dialog.show();
        Ion.with(this)
                .load("http://smartbusiness.smartcoins.space/api/index.php/thirdparties?sortfield=t.rowid&sortorder=ASC&limit=100")
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

                                if (jsonArray.length() >customerslist.size() || jsonArray.length() < customerslist.size()) {
                                    db.clearCustomers();
                                    customerslist.clear();

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject curr = jsonArray.getJSONObject(i);
                                        createNote(curr.getString("name"), curr.getString("phone"), curr.getString("id"));
                                    }

                                } else {
                                    //do nothing
                                }
                                customerslist.addAll(db.getAllNotes());

                                mAdapter = new CustomersAdapter(MainActivity.this, customerslist,0);
                                recyclerView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();

                                dialog.hide();
                            } catch (Exception j_e) {
                                //dialog.hide();
                                Toast.makeText(MainActivity.this, j_e.getMessage(), LENGTH_LONG).show();
                            }


                        } else {
                            //dialog.hide();
                            Toast.makeText(MainActivity.this, "Error  " + e.getMessage(), LENGTH_LONG).show();
                        }
                    }
                });
    }


   public void uploadCustomers(final Customer c, final int i){
       Ion.with(MainActivity.this)
               .load("POST", "http://smartbusiness.smartcoins.space/api/index.php/thirdparties")
               .addHeader("Accept", "application/json")
               .addHeader("DOLAPIKEY", "QqHJWv38giuk2T2Y2Sp3ys46Pd89FPUg")
               .setBodyParameter("entity", "1")
               .setBodyParameter("name", c.getName())
               .setBodyParameter("name_alias", "")
               .setBodyParameter("status", "1")
               .setBodyParameter("state_id", "ref1")
               .setBodyParameter("phone", c.getNumber())
               .setBodyParameter("socialnetworks", "[]")
               .setBodyParameter("tva_assuj", "1")
               .setBodyParameter("tva_intra", "")
               .setBodyParameter("localtax1_value", "0.000")
               .setBodyParameter("localtax2_value", "0.000")
               .setBodyParameter("typent_id", "0")
               .setBodyParameter("typent_code", "TE_UNKNOWN")
               .setBodyParameter("remise_supplier_percent", "0")
               .setBodyParameter("fk_prospectlevel", "")
               .setBodyParameter("date_modification", "1592708318")
               .setBodyParameter("user_modification", "5")
               .setBodyParameter("date_creation", "1592623211")
               .setBodyParameter( "user_creation", "1")
               .setBodyParameter( "client", "1")
               .setBodyParameter( "prospect", "0")
               .setBodyParameter("fournisseur", "0")
               .setBodyParameter("code_client", clientcode(i))
               .setBodyParameter("stcomm_id", "0")
               .setBodyParameter("status_prospect_label", "Never contacted")
               .setBodyParameter("ref", "1")
               .setBodyParameter("fk_incoterms", "0")
               .setBodyParameter( "fk_multicurrency", "0")
               .setBodyParameter( "multicurrency_code", "")
               .setBodyParameter( "id", "1")
               .setBodyParameter("country", "Malawi")
               .setBodyParameter("country_id", "144")
               .setBodyParameter( "country_code", "MW")
               .setBodyParameter("fk_account", "0")
               .asString()
               .setCallback(new FutureCallback<String>() {
                   @Override
                   public void onCompleted(Exception e, String result) {
                       if (result != null) {

                           //Toast.makeText(MainActivity.this, String.valueOf(i), LENGTH_LONG).show();
                //
                           dialog.hide();
                           try {
                               int n = Integer.parseInt(result);
                               Toast.makeText(MainActivity.this,"registered", LENGTH_LONG).show();
                               //Toast.makeText(MainActivity.this, result, LENGTH_LONG).show();
                               db.updateCustomerSocId(result, String.valueOf(c.getId()));
                               mAdapter.notifyDataSetChanged();

                               if(db.getAllPayments().size()>0){
                                   try {

                                       db.updatePaymentforCustomer(result, String.valueOf(c.getId()));

                                   }catch (Exception ex){
                                       Toast.makeText(MainActivity.this, ex.getMessage(), LENGTH_LONG).show();
                                   }

                               }

                           } catch (NumberFormatException Ne) {
                               Toast.makeText(MainActivity.this,"Failed to register customer " +c.getName(), LENGTH_LONG).show();
                           }

                       } else {
                           Toast.makeText(MainActivity.this, "Error uploading customer " + e.getMessage(), LENGTH_LONG).show();
                       }

                       reloadCustomers();
                   }
               });
   }


        public  String clientcode(int i){

            Calendar calendar = Calendar.getInstance();
            long now = calendar.getTimeInMillis();
            String value = String.valueOf(now);
//                LastID = LastID+1;
//               String value = "";
//            String code =  String.valueOf(LastID+i);
//               if(code.length() == 1){
//                   value = "SHOP-00"+(String.valueOf(LastID+i))+"-CD";
//               }
//               if(code.length() == 2){
//                   value = "SHOP-0"+(String.valueOf(LastID+i))+"-CD";
//               }
//               if(code.length() == 3){
//                    value = "SHOP-"+(String.valueOf(LastID+i))+"-CD";
//               }
               return value;
        }


}
