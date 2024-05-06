package com.example.sm_street_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class seller_home extends AppCompatActivity {

    Button b1_mange_product, b2_update_status, b3_logout;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b1_mange_product = findViewById(R.id.B1_ManageSellerProduct);
        b2_update_status = findViewById(R.id.B2_UpdateStatus);
        b3_logout = findViewById(R.id.B3_Logout);


        b1_mange_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), manage_product.class);
                startActivity(i);


            }
        });

        b2_update_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), update_status.class);
                startActivity(i);


            }
        });

        b3_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), login.class);
                startActivity(i);


            }
        });

    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent ik = new Intent(getApplicationContext(), login.class);
        startActivity(ik);
    }

}