package com.smartcoins.utils;

/**
 * Created by ravi on 20/02/18.
 */

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartcoins.R;
import com.smartcoins.models.Customer;
import com.smartcoins.models.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context context;
    private  int type;
    private List<Product> notesList;

    public ProductAdapter(Context context, List<Product> notesList, int type) {
        this.type= type;
        this.context = context;
        this.notesList = notesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView note,prod_name,price,prod_price_tx,prod_price_amount,status,date;
        public ImageView img;

        public MyViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.view_dp);
            prod_price_tx = view.findViewById(R.id.txt_price_phone);

            prod_price_amount = view.findViewById(R.id.customer_phone_price);
            status = view.findViewById(R.id.status);
            note = view.findViewById(R.id.customer);
            date = view.findViewById(R.id.date);
            prod_name = view.findViewById(R.id.prod_name);
            price = view.findViewById(R.id.prod_price);

        }
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (type==2){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_row, parent, false);
            return new MyViewHolder(itemView);
        }else{
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.note_list_row, parent, false);
            return new MyViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = notesList.get(position);

        if (type ==1){
            holder.note.setText(product.getName());
            holder.img.setImageResource(R.drawable.ic_product);
            holder.prod_price_tx.setText("Price");
            double price = Math.round(Double.valueOf(product.getPrice())*100.0)/100.0;
            holder.prod_price_amount.setText(String.valueOf(price)+"/=");
            holder.status.setVisibility(View.GONE);

        }if(type==2){
            holder.prod_name.setText(product.getName());
            holder.note.setText(product.getCust_name());
            holder.date.setText(product.getTimestamp());
            double price = Math.round(Double.valueOf(product.getPrice())*100.0)/100.0;
            holder.price.setText("Price : "+String.valueOf(price) +"/=");




        }
    }





    @Override
    public int getItemCount() {
        return notesList.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}
