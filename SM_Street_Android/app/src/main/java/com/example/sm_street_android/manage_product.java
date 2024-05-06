package com.example.sm_street_android;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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

public class manage_product extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView l1_manage_product;
    Button b1_add_product;
    SharedPreferences sh;
    String url, product_id_str;
    ArrayList<String> product_name, product_type, product_details, price_per_day,
            image, product_id_ArrayStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);

        b1_add_product = findViewById(R.id.B1_AddProduct);

        b1_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2=new Intent(getApplicationContext(), add_product.class);
                startActivity(i2);

            }
        });
        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1_manage_product=findViewById(R.id.L1_ManageProduct);
        url = "http://" + sh.getString("ip", "") + ":5000/view_product";
        RequestQueue queue = Volley.newRequestQueue(manage_product.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
//                    Toast.makeText(manage_product.this, "err" + response, Toast.LENGTH_SHORT).show();
                    JSONArray ar = new JSONArray(response);

                    product_name = new ArrayList<>();
                    product_type = new ArrayList<>();
                    product_details = new ArrayList<>();
                    price_per_day = new ArrayList<>();
                    image = new ArrayList<>();
                    product_id_ArrayStr = new ArrayList<>();


                    for (int i=0; i<ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        product_name.add(jo.getString("Product"));
                        product_type.add(jo.getString("Type"));
                        product_details.add(jo.getString("Details"));
                        price_per_day.add(jo.getString("Price_per_day"));
                        image.add(jo.getString("Image"));
                        product_id_ArrayStr.add(jo.getString("product_id"));

                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l1_manage_product.setAdapter(new custom_manage_product(manage_product.this, product_name, product_type, product_details, price_per_day, image));
                    l1_manage_product.setOnItemClickListener(manage_product.this);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"========="+e,Toast.LENGTH_LONG).show();
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(manage_product.this, "err" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lid", sh.getString("seller_lid", ""));
                return params;
            }
        };
        queue.add(stringRequest);

    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        product_id_str=product_id_ArrayStr.get(i);

        AlertDialog.Builder ald=new AlertDialog.Builder(manage_product.this);
        ald.setTitle("title")
                .setPositiveButton(" Edit ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try
                        {
                            Intent i=new Intent(getApplicationContext(),edit_product.class);
                            i.putExtra("p_id",product_id_str);
                            startActivity(i);

//                            SharedPreferences.Editor ed=sh.edit();
//                            ed.putString("orginal",fname.get(pos));
//                            ed.commit();
//
//                            startDownload(fname.get(pos));

                        }
                        catch(Exception e)
                        {
                            Toast.makeText(getApplicationContext(),e+"",Toast.LENGTH_LONG).show();
                        }

                    }
                })
                .setNegativeButton(" Delete ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {

                        RequestQueue queue = Volley.newRequestQueue(manage_product.this);
                        url = "http://"+sh.getString("ip","")+":5000/delete_product";
//                        Toast.makeText(manage_product.this,url , Toast.LENGTH_SHORT).show();

                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response)
                            {
                                // Display the response string.
                                Log.d("+++++++++++++++++", response);
                                try
                                {
                                    JSONObject json = new JSONObject(response);
                                    String res = json.getString("task");

                                    if (res.equalsIgnoreCase("success"))
                                    {
//                                        String lid = json.getString("id");     // getting login id
//                                        SharedPreferences.Editor edp = sh.edit();
//                                        edp.putString("lid", lid);
//                                        edp.commit();
                                        Intent ik = new Intent(getApplicationContext(), manage_product.class);
                                        startActivity(ik);
                                    }
                                    else
                                    {
                                        Toast.makeText(manage_product.this, "success", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        },new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("p_id",product_id_ArrayStr.get(i));
//                                params.put("lid",sh.getString("lid",""));
//                                params.put("uname", username);
//                                params.put("pass", password);

                                return params;
                            }
                        };
//                        i.putExtra("imgid", fid.get(pos));
//                        startActivity(i);
                        queue.add(stringRequest);
                    }

                });
        AlertDialog al=ald.create();
        al.show();
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent ik = new Intent(getApplicationContext(), seller_home.class);
        startActivity(ik);
    }

}