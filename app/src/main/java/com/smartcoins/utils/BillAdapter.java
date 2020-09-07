package com.smartcoins.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.smartcoins.R;
import com.smartcoins.models.Product;

import java.util.ArrayList;

public class BillAdapter extends ArrayAdapter<Product> {
    private Activity activity;
    private ArrayList<Product> productlist;
    private static LayoutInflater inflater = null;

    public BillAdapter(Activity activity, int textViewResourceId, ArrayList<Product> productlist) {

        super(activity, textViewResourceId,productlist);
        try {
            this.activity = activity;
            this.productlist = productlist;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }

    }

    public int getCount() {
        return productlist.size();
    }


    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView product_name;
        public TextView product_price;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.item_product_bill, null);
                holder = new ViewHolder();

                holder.product_name = (TextView) vi.findViewById(R.id.item_name);
                holder.product_price = (TextView) vi.findViewById(R.id.item_price);


                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }


            holder.product_name.setText(productlist.get(position).getName());
            double price = Math.round(Double.valueOf(productlist.get(position).getPrice())*100.0)/100.0;

            holder.product_price.setText(String.valueOf(price));


        } catch (Exception e) {


        }
        return vi;
    }
}