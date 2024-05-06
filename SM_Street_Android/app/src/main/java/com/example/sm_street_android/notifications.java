package com.example.sm_street_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;
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

public class notifications extends AppCompatActivity {

    ListView l1_notification;
    SharedPreferences sh;
    ArrayList<String> notification_arr, date_arr;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        l1_notification = findViewById(R.id.L1_notification);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        url = "http://" + sh.getString("ip", "") + ":5000/view_notification_user";
        RequestQueue queue = Volley.newRequestQueue(notifications.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(notifications.this, "eeeerr" + response, Toast.LENGTH_SHORT).show();

                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
//                    Toast.makeText(notifications.this, "" + response, Toast.LENGTH_SHORT).show();
                    JSONArray ar = new JSONArray(response);

                    notification_arr = new ArrayList<>();
                    date_arr = new ArrayList<>();

                    for (int i=0; i<ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        notification_arr.add(jo.getString("notification"));
                        date_arr.add(jo.getString("date"));
                    }
//                    Toast.makeText(notifications.this, "err" + response, Toast.LENGTH_SHORT).show();


                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l1_notification.setAdapter(new custom_notifications(notifications.this, notification_arr,
                            date_arr));
//                    l1_offer.setOnItemClickListener(view_shop.this);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"========="+e,Toast.LENGTH_LONG).show();
                    Log.d("=========", e.toString());
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(notifications.this, "err" + error, Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent ik = new Intent(getApplicationContext(), user_home.class);
        startActivity(ik);
    }

}