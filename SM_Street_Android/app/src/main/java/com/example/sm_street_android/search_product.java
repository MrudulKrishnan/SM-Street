package com.example.sm_street_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class search_product extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner sp_product_category;
    ListView l1_products;
    SharedPreferences sh;
    String url, category_id_str;
    ArrayList<String> category_arr, category_id_arr, product_arr, details_arr, price_arr, image_arr,
            product_id_arr, product_stock_arr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        sp_product_category = findViewById(R.id.SP1_Category);
        l1_products = findViewById(R.id.L1_RenralProducts);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        url = "http://" + sh.getString("ip", "") + ":5000/view_category_user";
        RequestQueue queue = Volley.newRequestQueue(search_product.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
//                    Toast.makeText(search_product.this, "err" + response, Toast.LENGTH_SHORT).show();
                    JSONArray ar = new JSONArray(response);

                    category_arr = new ArrayList<>();
                    category_id_arr = new ArrayList<>();


                    for (int i=0; i<ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        category_arr.add(jo.getString("category"));
                        category_id_arr.add(jo.getString("category_id"));

                    }

                    ArrayAdapter<String> ad=new ArrayAdapter<String>(search_product.this,android.R.layout.simple_list_item_1,category_arr );
                    sp_product_category.setAdapter(ad);
                    sp_product_category.setOnItemSelectedListener(search_product.this);


                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(),"========="+e,Toast.LENGTH_LONG).show();
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(search_product.this, "err" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lid", sh.getString("user_lid", ""));
                return params;
            }
        };
        queue.add(stringRequest);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category_id_str = category_id_arr.get(position);


        url = "http://" + sh.getString("ip", "") + ":5000/view_products_user";
        RequestQueue queue = Volley.newRequestQueue(search_product.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
//                    Toast.makeText(search_product.this, "err" + response, Toast.LENGTH_SHORT).show();
                    JSONArray ar = new JSONArray(response);

                    product_arr = new ArrayList<>();
                    details_arr = new ArrayList<>();
                    price_arr = new ArrayList<>();
                    image_arr = new ArrayList<>();
                    product_id_arr = new ArrayList<>();
                    product_stock_arr = new ArrayList<>();


                    for (int i=0; i<ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        product_arr.add(jo.getString("product"));
                        details_arr.add(jo.getString("details"));
                        price_arr.add(jo.getString("price"));
                        image_arr.add(jo.getString("image"));
                        product_id_arr.add(jo.getString("product_id"));
                        product_stock_arr.add(jo.getString("stocks"));

                    }

                    l1_products.setAdapter(new custom_search_product(search_product.this,
                            product_arr,details_arr, price_arr, image_arr, product_id_arr, product_stock_arr));


                } catch (Exception e) {
//                    Toast.makeText(search_product.this, "err", Toast.LENGTH_SHORT).show();

                    Toast.makeText(getApplicationContext(),"========="+e,Toast.LENGTH_LONG).show();
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(search_product.this, "err" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("category_id", category_id_str);
                params.put("shop_id", sh.getString("shop_id", ""));

                return params;
            }
        };
        queue.add(stringRequest);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}