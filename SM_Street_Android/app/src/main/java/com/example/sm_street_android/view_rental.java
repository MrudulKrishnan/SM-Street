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

public class view_rental extends AppCompatActivity implements SearchView.OnQueryTextListener{

    SharedPreferences sh;
    ListView l1_view_shop;
    SearchView s1;
    ArrayList<String> rental_name_arr, address_arr, user_phone_arr,user_email_arr, rental_id_arr;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rental);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        l1_view_shop = findViewById(R.id.L1_Shop);
        s1 = findViewById(R.id.SV1_SearchShop);
        s1.setOnQueryTextListener(view_rental.this);


        url = "http://" + sh.getString("ip", "") + ":5000/view_rental";
        RequestQueue queue = Volley.newRequestQueue(view_rental.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(view_rental.this, "eeeerr" + response, Toast.LENGTH_SHORT).show();

                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
//                    Toast.makeText(view_rental.this, "" + response, Toast.LENGTH_SHORT).show();
                    JSONArray ar = new JSONArray(response);

                    rental_name_arr = new ArrayList<>();
                    address_arr = new ArrayList<>();
                    user_phone_arr = new ArrayList<>();
                    user_email_arr = new ArrayList<>();
                    rental_id_arr = new ArrayList<>();

                    for (int i=0; i<ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        rental_name_arr.add(jo.getString("rental"));
                        address_arr.add(jo.getString("place")+ "," + jo.getString("post")+ ","
                                +jo.getString("pin")+ "," + jo.getString("pin"));
                        user_phone_arr.add(jo.getString("phone"));
                        user_email_arr.add(jo.getString("email"));
                        rental_id_arr.add(jo.getString("rental_id"));
                    }
//                    Toast.makeText(view_rental.this, "err" + response, Toast.LENGTH_SHORT).show();


                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l1_view_shop.setAdapter(new custom_view_rental(view_rental.this,
                            rental_name_arr, address_arr, user_phone_arr, user_email_arr, rental_id_arr));
//                    l1_view_shop.setOnItemClickListener(view_shop.this);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"========="+e,Toast.LENGTH_LONG).show();
                    Log.d("=========", e.toString());
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(view_rental.this, "err" + error, Toast.LENGTH_SHORT).show();
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



        url = "http://" + sh.getString("ip", "") + ":5000/search_view_rental";
        RequestQueue queue = Volley.newRequestQueue(view_rental.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(view_rental.this, "eeeerr" + response, Toast.LENGTH_SHORT).show();

                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
//                    Toast.makeText(view_rental.this, "" + response, Toast.LENGTH_SHORT).show();
                    JSONArray ar = new JSONArray(response);

                    rental_name_arr = new ArrayList<>();
                    address_arr = new ArrayList<>();
                    user_phone_arr = new ArrayList<>();
                    user_email_arr = new ArrayList<>();
                    rental_id_arr = new ArrayList<>();

                    for (int i=0; i<ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        rental_name_arr.add(jo.getString("shop"));
                        address_arr.add(jo.getString("place")+ "," + jo.getString("post")+ ","
                                +jo.getString("pin")+ "," + jo.getString("pin"));
                        user_phone_arr.add(jo.getString("phone"));
                        user_email_arr.add(jo.getString("email"));
                        rental_id_arr.add(jo.getString("rental_id"));
                    }
//                    Toast.makeText(view_rental.this, "err" + response, Toast.LENGTH_SHORT).show();


                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l1_view_shop.setAdapter(new custom_view_rental(view_rental.this,
                            rental_name_arr, address_arr, user_phone_arr, user_email_arr, rental_id_arr));
//                    l1_view_shop.setOnItemClickListener(view_shop.this);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"========="+e,Toast.LENGTH_LONG).show();
                    Log.d("=========", e.toString());
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(view_rental.this, "err" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lid", sh.getString("user_lid", ""));
                params.put("search_rental",s);
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