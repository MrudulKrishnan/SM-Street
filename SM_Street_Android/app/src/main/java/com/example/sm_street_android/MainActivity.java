package com.example.sm_street_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText IP;
    Button s_button;
    String IPS;
    SharedPreferences sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IP = findViewById(R.id.ip);
        s_button = findViewById(R.id.ip_button);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        s_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IPS = IP.getText().toString();

                if (IPS.equalsIgnoreCase("")){
                    IP.setError("Enter your IP address");
                }
                else{
                    SharedPreferences.Editor ed = sh.edit();
                    ed.putString("ip",IPS);
                    ed.commit();

                    Intent i1 = new Intent(getApplicationContext(), login.class);
                    startActivity(i1);
                    Toast.makeText(MainActivity.this, IP+" : please login", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}