package com.example.sm_street_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
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

public class view_special_offers extends AppCompatActivity implements SearchView.OnQueryTextListener{

    ListView l1_offer;
    SharedPreferences sh;
    String url;
    ArrayList<String> product_arr, offer_details_arr,product_img_arr, start_date_arr, end_date_arr;
    SearchView s1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_special_offers);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1_offer = findViewById(R.id.L1_SpecialOffer);
        s1 = findViewById(R.id.SV1_ViewSpecialOffer);
        s1.setOnQueryTextListener(view_special_offers.this);



        url = "http://" + sh.getString("ip", "") + ":5000/view_special_offers";
        RequestQueue queue = Volley.newRequestQueue(view_special_offers.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(view_special_offers.this, "eeeerr" + response, Toast.LENGTH_SHORT).show();

                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
//                    Toast.makeText(view_special_offers.this, "" + response, Toast.LENGTH_SHORT).show();
                    JSONArray ar = new JSONArray(response);

                    product_arr = new ArrayList<>();
                    offer_details_arr = new ArrayList<>();
                    start_date_arr = new ArrayList<>();
                    end_date_arr = new ArrayList<>();
                    product_img_arr = new ArrayList<>();

                    for (int i=0; i<ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        product_arr.add(jo.getString("product"));
                        offer_details_arr.add(jo.getString("details"));
                        start_date_arr.add(jo.getString("start_date"));
                        end_date_arr.add(jo.getString("end_date"));
                        product_img_arr.add(jo.getString("image"));
                    }
//                    Toast.makeText(view_special_offers.this, "err" + response, Toast.LENGTH_SHORT).show();


                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l1_offer.setAdapter(new custom_view_offers(view_special_offers.this,
                            product_arr, offer_details_arr, product_img_arr, start_date_arr, end_date_arr));
//                    l1_offer.setOnItemClickListener(view_shop.this);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"========="+e,Toast.LENGTH_LONG).show();
                    Log.d("=========", e.toString());
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(view_special_offers.this, "err" + error, Toast.LENGTH_SHORT).show();
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
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        url = "http://" + sh.getString("ip", "") + ":5000/search_view_special_offers";
        RequestQueue queue = Volley.newRequestQueue(view_special_offers.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(view_special_offers.this, "eeeerr" + response, Toast.LENGTH_SHORT).show();

                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
//                    Toast.makeText(view_special_offers.this, "" + response, Toast.LENGTH_SHORT).show();
                    JSONArray ar = new JSONArray(response);

                    product_arr = new ArrayList<>();
                    offer_details_arr = new ArrayList<>();
                    start_date_arr = new ArrayList<>();
                    end_date_arr = new ArrayList<>();
                    product_img_arr = new ArrayList<>();

                    for (int i=0; i<ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        product_arr.add(jo.getString("product"));
                        offer_details_arr.add(jo.getString("details"));
                        start_date_arr.add(jo.getString("start_date"));
                        end_date_arr.add(jo.getString("end_date"));
                        product_img_arr.add(jo.getString("image"));
                    }
//                    Toast.makeText(view_special_offers.this, "err" + response, Toast.LENGTH_SHORT).show();


                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l1_offer.setAdapter(new custom_view_offers(view_special_offers.this,
                            product_arr, offer_details_arr, product_img_arr, start_date_arr, end_date_arr));
//                    l1_offer.setOnItemClickListener(view_shop.this);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"========="+e,Toast.LENGTH_LONG).show();
                    Log.d("=========", e.toString());
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(view_special_offers.this, "err" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lid", sh.getString("user_lid", ""));
                params.put("search_product", s);
                return params;
            }
        };
        queue.add(stringRequest);


        return true;
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent ik = new Intent(getApplicationContext(), user_home.class);
        startActivity(ik);
    }

}