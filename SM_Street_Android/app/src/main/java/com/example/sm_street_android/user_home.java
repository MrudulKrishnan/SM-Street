package com.example.sm_street_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class user_home extends AppCompatActivity {

    Button view_shop, search_product, view_offers, recommendation, notification, review_rating,
            send_complaint, view_special_offers, view_rental_book, logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        view_shop = findViewById(R.id.B1_ViewShops);
        view_offers = findViewById(R.id.B3_ViewOffers);
        recommendation = findViewById(R.id.B4_Recommandation);
        notification = findViewById(R.id.B5_Notification);
        send_complaint = findViewById(R.id.B7_SendComplaint);
        view_special_offers = findViewById(R.id.B8_SpecialOffer);
        view_rental_book = findViewById(R.id.B9_ViewRentalBook);
        logout = findViewById(R.id.B10_logout);


        view_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), view_shop.class);
                startActivity(i);

            }
        });


        view_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), view_offers.class);
                startActivity(i);

            }
        });

        recommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), notifications.class);
                startActivity(i);

            }
        });


        send_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), sent_complaint_reply.class);
                startActivity(i);

            }
        });

        view_special_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), view_special_offers.class);
                startActivity(i);

            }
        });

        view_rental_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), view_rental.class);
                startActivity(i);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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