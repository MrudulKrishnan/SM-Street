package com.example.sm_street_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;

import android.content.DialogInterface;
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

public class login extends AppCompatActivity {

    EditText username_var, password_var;
    Button login_butt, register_butt, b1_seller_register;
    String username_str, password_str, url;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username_var = findViewById(R.id.username);
        password_var = findViewById(R.id.password);
        login_butt = findViewById(R.id.login_b);
        register_butt = findViewById(R.id.register_b);
        b1_seller_register = findViewById(R.id.B2_SellerRegister);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        login_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_str = username_var.getText().toString();
                password_str = password_var.getText().toString();

                ///////////////////////////////////////

                if (username_str.equalsIgnoreCase("")){
                    username_var.setError("Enter username");
                }
                else if (password_str.equalsIgnoreCase("")){
                    password_var.setError("enter password");
                }
                else {

                    ///////////////////////////////////////
                    RequestQueue queue = Volley.newRequestQueue(login.this);
                    url = "http://"+sh.getString("ip","")+":5000/login_code";
//                    Toast.makeText(login.this,url , Toast.LENGTH_SHORT).show();
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

                                if (res.equalsIgnoreCase("user"))
                                {
                                    String lid = json.getString("id");     // getting login id
                                    SharedPreferences.Editor edp = sh.edit();
                                    edp.putString("user_lid", lid);
                                    edp.commit();
                                    Intent ik = new Intent(getApplicationContext(), user_home.class);
                                    startActivity(ik);
                                }
                                else if ( res.equalsIgnoreCase("seller"))
                                {

                                    String lid = json.getString("id");     // getting login id
                                    String type = json.getString("type");     // getting login id
                                    SharedPreferences.Editor edp = sh.edit();
                                    edp.putString("seller_lid", lid);
                                    edp.putString("t", type);
                                    edp.commit();
//                                    Toast.makeText(getApplicationContext(), "type=====" + type, Toast.LENGTH_LONG).show();


                                    Intent ik = new Intent(getApplicationContext(),LocationService.class);
                                    startService(ik);
                                    Intent i= new Intent(getApplicationContext(), seller_home.class);
                                    startActivity(i);
//
//                                    Toast.makeText(login.this, "location uppppp", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
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
                            params.put("username", username_str);
                            params.put("password", password_str);
                            return params;
                        }
                    };
                    queue.add(stringRequest);

                }

            }
        });
        register_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(getApplicationContext(), user_registration.class);
                startActivity(i2);
            }
        });


        b1_seller_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i2 = new Intent(getApplicationContext(), seller_registration.class);
                startActivity(i2);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder ald = new AlertDialog.Builder(login.this);
        ald.setTitle("Do you want to exit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent in = new Intent(Intent.ACTION_MAIN);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        in.addCategory(Intent.CATEGORY_HOME);
                        startActivity(in);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });

        AlertDialog al = ald.create();
        al.show();
        ;

    }
}