package com.example.sm_street_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
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

public class review_rating extends AppCompatActivity {
    RatingBar et1_rating;
    EditText et2_review;
    Button b1_send_rating;
    SharedPreferences sh;
    ArrayList<String> product_arr, product_id_arr;
    String url, rating_str, review_str , product_id_str, title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_rating);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        et1_rating = findViewById(R.id.ET1_Rating);
        et2_review = findViewById(R.id.ET2_Review);
        b1_send_rating = findViewById(R.id.B1_SendRating);

//        if(android.os.Build.VERSION.SDK_INT>9)
//        {
//            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }
//
//        url = "http://" + sh.getString("ip", "") + ":5000/view_product_name";
//        RequestQueue queue = Volley.newRequestQueue(review_rating.this);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                // Display the response string.
//                Log.d("+++++++++++++++++", response);
//                try {
//                    Toast.makeText(review_rating.this, "err" + response, Toast.LENGTH_SHORT).show();
//                    JSONArray ar = new JSONArray(response);
//
//                    product_arr = new ArrayList<>();
//                    product_id_arr = new ArrayList<>();
//
//
//                    for (int i=0; i<ar.length(); i++) {
//                        JSONObject jo = ar.getJSONObject(i);
//                        product_arr.add(jo.getString("Product_name"));
//                        product_id_arr.add(jo.getString("Product_id"));
//
//                    }
//
////                    ArrayAdapter<String> ad=new ArrayAdapter<>(review_rating.this,android.R.layout.simple_list_item_1,product_arr );
////                    sp1_product.setAdapter(ad);
////                    sp1_product.setOnItemSelectedListener(review_rating.this);
//
//
//                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(),"========="+e,Toast.LENGTH_LONG).show();
//                    Log.d("=========", e.toString());
//                }
//
//
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(review_rating.this, "err" + error, Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @NonNull
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("lid", sh.getString("lid", ""));
//                return params;
//            }
//        };
//        queue.add(stringRequest);

        b1_send_rating.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                rating_str = String.valueOf(et1_rating.getRating());
                review_str = et2_review.getText().toString();
//                product_str = sp1_product.toString();
                if (rating_str.equalsIgnoreCase("")) {
                    et1_rating.getNumStars();
                } else if (review_str.equalsIgnoreCase("")) {
                    et2_review.setError("Enter your product type");
                }
                else {


                    RequestQueue queue = Volley.newRequestQueue(review_rating.this);
                    String url ="http://"+sh.getString("ip", "")+":5000/shop_product_rating";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.
                            Log.d("+++++++++++++++++",response);
                            try {
                                JSONObject json=new JSONObject(response);
                                String res=json.getString("task");

                                if(res.equalsIgnoreCase("success"))
                                {
                                    Toast.makeText(getApplicationContext(),"successfully rated",Toast.LENGTH_LONG).show();
                                    Intent in=new Intent(getApplicationContext(),user_home.class)	;

                                    startActivity(in);

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"registration failed",Toast.LENGTH_LONG).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(getApplicationContext(),"Error"+error,Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams()
                        {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("Rating", rating_str);
                            params.put("Review", review_str);
                            params.put("lid", sh.getString("user_lid", ""));
                            params.put("shop_id", sh.getString("shop_id", ""));
                            return params;
                        }
                    };
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);




                }
            }
        });
    }

//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        product_id_str=product_id_arr.get(i);
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent ik = new Intent(getApplicationContext(), user_home.class);
        startActivity(ik);
    }

}
