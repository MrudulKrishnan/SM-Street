package com.example.sm_street_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

class custom_view_rental extends BaseAdapter {
    private Context context;
    ArrayList<String> rental_name_arr, address_arr, user_phone_arr, user_email_arr, rental_id_arr;
    SharedPreferences sh;
    Button b1_view_product;
    String shop_id;
    public custom_view_rental(Context applicationContext, ArrayList<String> rental_name_arg,
                             ArrayList<String> address_arg, ArrayList<String> user_phone_arg,
                             ArrayList<String> user_email_arg, ArrayList<String> rental_id_arg)
    {
        // TODO Auto-generated constructor stub
        this.context = applicationContext;
        this.rental_name_arr = rental_name_arg;
        this.address_arr = address_arg;
        this.user_phone_arr = user_phone_arg;
        this.user_email_arr = user_email_arg;
        this.rental_id_arr = rental_id_arg;
        sh= PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return rental_name_arr.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public int getItemViewType(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if(convertview==null)
        {
            gridView=new View(context);
            gridView=inflator.inflate(R.layout.activity_custom_view_rental,null);

        }
        else
        {
            gridView=(View)convertview;

        }
        ///////////////////////
        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        /////////////////////////////////

        TextView tv1_worker_name = (TextView)gridView.findViewById(R.id.TV1_Notification);
        TextView tv2_worker_phone = (TextView)gridView.findViewById(R.id.TV2_Date);
        TextView tv3_date = (TextView)gridView.findViewById(R.id.TV3_Phone);
        TextView tv4_status = (TextView)gridView.findViewById(R.id.TV4_Email);
        b1_view_product =gridView.findViewById(R.id.B1_ViewProducts);

        b1_view_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor edp = sh.edit();
                edp.putString("rental_id", rental_id_arr.get(position));
                edp.commit();

                Intent i = new Intent(context, view_rental_book.class);
                context.startActivity(i);

            }
        });



        tv1_worker_name.setText(rental_name_arr.get(position));
        tv2_worker_phone.setText(address_arr.get(position));
        tv3_date.setText(user_phone_arr.get(position));
        tv4_status.setText(user_email_arr.get(position));

        tv1_worker_name.setTextColor(Color.BLACK);
        tv2_worker_phone.setTextColor(Color.BLACK);
        tv3_date.setTextColor(Color.BLACK);
        tv4_status.setTextColor(Color.BLACK);

        return gridView;

    }

}
