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

public class rental_booking extends AppCompatActivity {

    EditText e1_from_date, e2_to_date, e3_quantity;
    Button b1_rental_book;
    String url, from_date_str, to_date_str, quantity_str;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_booking);

        e1_from_date = findViewById(R.id.E1_FromDate);
        e2_to_date = findViewById(R.id.E2_ToDate);
        e3_quantity = findViewById(R.id.E3_Quantity);
        b1_rental_book = findViewById(R.id.B1_RentalBook);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        b1_rental_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                from_date_str = e1_from_date.getText().toString();
                to_date_str = e2_to_date.getText().toString();
                quantity_str = e3_quantity.getText().toString();

                if (from_date_str.equalsIgnoreCase("")) {
                    e1_from_date.setError("Enter Your Feedback");
                }
                else {
                    RequestQueue queue = Volley.newRequestQueue(rental_booking.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/book_rental";

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
                                    Toast.makeText(rental_booking.this, "Feedback sent successfully ", Toast.LENGTH_SHORT).show();
                                    Intent ik = new Intent(getApplicationContext(), user_home.class);
                                    startActivity(ik);
                                } else {
                                    Toast.makeText(rental_booking.this, "please try again", Toast.LENGTH_SHORT).show();
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
                            params.put("from_date", from_date_str);
                            params.put("to_date", to_date_str);
                            params.put("quantity", quantity_str);
                            params.put("lid", sh.getString("user_lid", ""));
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
        Intent ik = new Intent(getApplicationContext(), user_home.class);
        startActivity(ik);
    }

}