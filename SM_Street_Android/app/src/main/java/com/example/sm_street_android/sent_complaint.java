package com.example.sm_street_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class sent_complaint extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    EditText e1_complaint;
    Spinner sp1_shop;
    Button b1_send_complaint;
    String complaint_str, url, shop_id_str;
    SharedPreferences sh;
    ArrayList<String> shop_name_arr, shop_id_arr;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_complaint);
        e1_complaint = findViewById(R.id.E1_EnterComplaint);
        sp1_shop = findViewById(R.id.SP1_Shops);
        b1_send_complaint = findViewById(R.id.B1_SendComplaint);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        url = "http://" + sh.getString("ip", "") + ":5000/view_shop_name";
        RequestQueue queue = Volley.newRequestQueue(sent_complaint.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
                    Toast.makeText(sent_complaint.this, "err" + response, Toast.LENGTH_SHORT).show();
                    JSONArray ar = new JSONArray(response);

                    shop_name_arr = new ArrayList<>();
                    shop_id_arr = new ArrayList<>();


                    for (int i=0; i<ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        shop_name_arr.add(jo.getString("shop_name"));
                        shop_id_arr.add(jo.getString("shop_id"));

                    }

                    ArrayAdapter<String> ad=new ArrayAdapter<>(sent_complaint.this,android.R.layout.simple_list_item_1,shop_name_arr );
                    sp1_shop.setAdapter(ad);
                    sp1_shop.setOnItemSelectedListener(sent_complaint.this);


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"========="+e,Toast.LENGTH_LONG).show();
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(sent_complaint.this, "err" + error, Toast.LENGTH_SHORT).show();
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


        b1_send_complaint.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                Toast.makeText(sent_complaint.this, "complaint toast ", Toast.LENGTH_SHORT).show();

                complaint_str = e1_complaint.getText().toString();

                if (complaint_str.equalsIgnoreCase("")) {
                    e1_complaint.setError("Enter Your complaint");
                }
                else {
                    RequestQueue queue = Volley.newRequestQueue(sent_complaint.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/send_shop_complaint";

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
                                    Toast.makeText(sent_complaint.this, "Complaint sent successfully ", Toast.LENGTH_SHORT).show();
                                    Intent ik = new Intent(getApplicationContext(), user_home.class);
                                    startActivity(ik);
                                } else {
                                    Toast.makeText(sent_complaint.this, "please try again", Toast.LENGTH_SHORT).show();
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
                            params.put("complaint", complaint_str);
                            params.put("lid", sh.getString("user_lid", ""));
                            params.put("shop_id", shop_id_str);
                            return params;
                        }
                    };
                    queue.add(stringRequest);
                } }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        shop_id_str=shop_id_arr.get(i);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent ik = new Intent(getApplicationContext(), sent_complaint_reply.class);
        startActivity(ik);
    }

}