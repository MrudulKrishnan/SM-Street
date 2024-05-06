package com.example.sm_street_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class sent_complaint_reply extends AppCompatActivity implements SearchView.OnQueryTextListener{

    ListView l1;
    Button b1;
    SearchView sv1_search_complaints_worker;
    SharedPreferences sh;
    String url;
    ArrayList<String> Complaints, Reply, Date, Shop_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_complaint_reply);

        l1 = findViewById(R.id.list_complaint);
        b1 = findViewById(R.id.button43);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sv1_search_complaints_worker = findViewById(R.id.SV1_SearchComplaintsWorker);

        sv1_search_complaints_worker.setOnQueryTextListener(sent_complaint_reply.this);

        url = "http://" + sh.getString("ip", "") + ":5000/shop_complaints_reply";
        RequestQueue queue = Volley.newRequestQueue(sent_complaint_reply.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
//                    Toast.makeText(sent_complaint_reply.this, "==" + response, Toast.LENGTH_SHORT).show();
                    JSONArray ar = new JSONArray(response);
                    Shop_name = new ArrayList<>();
                    Complaints = new ArrayList<>();
                    Reply = new ArrayList<>();
                    Date = new ArrayList<>();

                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        Shop_name.add(jo.getString("Shop_name"));
                        Complaints.add(jo.getString("Complaint"));
                        Reply.add(jo.getString("Reply"));
                        Date.add(jo.getString("Date"));
                    }

//                     ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
//                    lv.setAdapter(ad);

                    l1.setAdapter(new custom_complaint_reply(sent_complaint_reply.this, Complaints, Date, Reply, Shop_name));
//                    l1.setOnItemClickListener(send_complaints_view_reply.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(sent_complaint_reply.this, "err" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lid", sh.getString("user_lid", ""));
                return params;
            }
        };
        queue.add(stringRequest);

///////////////////////////////////////////////////////////////////////////////////////////

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(getApplicationContext(), sent_complaint.class);
                startActivity(i1);
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {


        url = "http://" + sh.getString("ip", "") + ":5000/search_user_complaints_reply";
        RequestQueue queue = Volley.newRequestQueue(sent_complaint_reply.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
//                    Toast.makeText(sent_complaint_reply.this, "==" + response, Toast.LENGTH_SHORT).show();
                    JSONArray ar = new JSONArray(response);
                    Shop_name = new ArrayList<>();
                    Complaints = new ArrayList<>();
                    Reply = new ArrayList<>();
                    Date = new ArrayList<>();

                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        Shop_name.add(jo.getString("Shop_name"));
                        Complaints.add(jo.getString("Complaint"));
                        Reply.add(jo.getString("Reply"));
                        Date.add(jo.getString("Date"));
                    }

//                     ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
//                    lv.setAdapter(ad);

                    l1.setAdapter(new custom_complaint_reply(sent_complaint_reply.this, Complaints, Date, Reply, Shop_name));
//                    l1.setOnItemClickListener(send_complaints_view_reply.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(sent_complaint_reply.this, "err" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lid", sh.getString("user_lid", ""));
                params.put("shop_name",s);
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