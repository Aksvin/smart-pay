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

import androidx.recyclerview.widget.RecyclerView;

import com.smartcoins.R;
import com.smartcoins.models.Customer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.MyViewHolder> {

    private Context context;
    private  int type;
    private List<Customer> notesList;

    public CustomersAdapter(Context context, List<Customer> notesList, int type) {
        this.type= type;
        this.context = context;
        this.notesList = notesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView note,number,status;
        public ImageView img;

        public MyViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.view_dp);
            note = view.findViewById(R.id.customer);
            number = view.findViewById(R.id.customer_phone_price);
             status= view.findViewById(R.id.status);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Customer customer = notesList.get(position);
            if (type ==1){
                holder.img.setImageResource(R.drawable.ic_product);
            }
        holder.note.setText(customer.getName());
        holder.number.setText(customer.getNumber());
        if (!customer.getIsnew().equals("1")){
            holder.status.setVisibility(View.VISIBLE);
        }else{
            holder.status.setVisibility(View.GONE);
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
