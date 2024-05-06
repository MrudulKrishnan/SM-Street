package com.example.sm_street_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class custom_view_rental_book extends BaseAdapter {
    private Context context;
    ArrayList<String> product;
    ArrayList<String> offer_details;
    ArrayList<String> product_img;
    ArrayList<String> price;
    ArrayList<String> product_id;
    ArrayList<String> product_stocks;
    String title, url;
    SharedPreferences sh;
    public custom_view_rental_book(Context applicationContext, ArrayList<String> product_arg,ArrayList<String> details_arg,ArrayList<String> price_arg,
                                 ArrayList<String> image_arg, ArrayList<String> product_id_arg, ArrayList<String>  product_stocks_arg)
    {
        // TODO Auto-generated constructor stub
        this.context = applicationContext;
        this.product = product_arg;
        this.offer_details = details_arg;
        this.product_img = image_arg;
        this.price = price_arg;
        this.product_id = product_id_arg;
        this.product_stocks = product_stocks_arg;
        sh= PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return product.size();
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
            gridView=inflator.inflate(R.layout.activity_custom_view_rental_book,null);

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

        TextView tv1_product_name = (TextView)gridView.findViewById(R.id.TV1_ProductName);
        TextView tv2_product_details = (TextView)gridView.findViewById(R.id.TV2_Details);
        TextView tv3_product_price = (TextView)gridView.findViewById(R.id.TV3_Price);
        ImageView im_product_image = (ImageView) gridView.findViewById(R.id.Product_image);
        java.net.URL thumb_u;
        try {

            //thumb_u = new java.net.URL("http://192.168.43.57:5000/static/photo/flyer.jpg");

            thumb_u = new java.net.URL("http://"+sh.getString("ip","")+":5000"+product_img.get(position));
            Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
            im_product_image.setImageDrawable(thumb_d);
        }
        catch (Exception e)
        {
            Log.d("errsssssssssssss",""+e);
        }

        Button b1_add_quantity = gridView.findViewById(R.id.B1_AddQuantity);
//        Button b2_rating = gridView.findViewById(R.id.B2_Rating);

        b1_add_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context , book.class);
                i.putExtra("pid",product_id.get(position));
                SharedPreferences.Editor edp = sh.edit();
                edp.putString("pid", product_id.get(position));
                edp.commit();

                i.putExtra("productname",product.get(position));
                i.putExtra("image",product_img.get(position));
                i.putExtra("price",price.get(position));
                i.putExtra("stocks",product_stocks.get(position));
                i.putExtra("d",offer_details.get(position));
//                i.putExtra("e",e.get(position));
//                i.putExtra("f",f.get(position));
//                i.putExtra("g",g.get(position));
//                i.putExtra("h",h.get(position));
                context.startActivity(i);

            }
        });

        tv1_product_name.setText(product.get(position));
        tv2_product_details.setText(offer_details.get(position));
        tv3_product_price.setText(price.get(position));

        tv1_product_name.setTextColor(Color.BLACK);
        tv2_product_details.setTextColor(Color.BLACK);
        tv3_product_price.setTextColor(Color.BLACK);

        return gridView;

    }

}
