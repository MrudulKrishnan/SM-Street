package com.example.sm_street_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class update_status extends AppCompatActivity {

    EditText e1_update_status;
    Button b1_update;
    String status_str, url;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status);

        e1_update_status = findViewById(R.id.E1_UpdateStatus);
        b1_update = findViewById(R.id.B1_update);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        b1_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                status_str = e1_update_status.getText().toString();

                if (status_str.equalsIgnoreCase("")) {
                    e1_update_status.setError("Enter Your firstName");
                }
                else {
                    RequestQueue queue = Volley.newRequestQueue(update_status.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/update_seller_status";

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.
                            Log.d("+++++++++++++++++", response);
                            try {
                                JSONObject json = new JSONObject(response);
                                String res = json.getString("task");

                                if (res.equalsIgnoreCase("success")) {
                                    Toast.makeText(update_status.this, "Update successfully ", Toast.LENGTH_SHORT).show();
                                    Intent ik = new Intent(getApplicationContext(), seller_home.class);
                                    startActivity(ik);
                                } else {
                                    Toast.makeText(update_status.this, "please try again", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {


                            Map<String, String> params = new HashMap<String, String>();
                            params.put("seller_status", status_str);
                            params.put("lid", sh.getString("seller_lid", ""));
                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }

            }
        });

    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent ik = new Intent(getApplicationContext(), seller_home.class);
        startActivity(ik);
    }

}