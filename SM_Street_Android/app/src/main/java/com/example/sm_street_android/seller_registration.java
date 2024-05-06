package com.example.sm_street_android;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class seller_registration extends AppCompatActivity {
    EditText firstname, lastname, age, place, post, pin, phone, email, username, password;
    RadioButton male, female;
    Button butt_signup;
    String firstname_str, lastname_str, age_str, gender_str, place_str, post_str, pin_str, phone_str,
            email_str, username_str, password_str, url, title;
    SharedPreferences sh;
    String PathHolder = "";
    byte[] filedt = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        firstname = findViewById(R.id.e1_firstname);
        lastname = findViewById(R.id.e2_lastname);
        age = findViewById(R.id.e3_age);
        place = findViewById(R.id.e5_place);
        post = findViewById(R.id.e6_post);
        pin = findViewById(R.id.e7_pin);
        phone = findViewById(R.id.e8_phone);
        email = findViewById(R.id.e9_mail);
        username = findViewById(R.id.e10_username);
        password = findViewById(R.id.e11_password);
//        proof = findViewById(R.id.e12_proof);
        male = findViewById(R.id.radioButt_male);
        female = findViewById(R.id.radioButt_female);

        butt_signup = findViewById(R.id.butt_signup);
//        butt_proof = findViewById(R.id.butt_proof);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


//        butt_proof.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("*/*");
////            intent.setType("application/pdf");
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                startActivityForResult(intent, 7);
//
//
//            }
//        });

        butt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstname_str = firstname.getText().toString();
                lastname_str = lastname.getText().toString();
                age_str = age.getText().toString();
                place_str = place.getText().toString();
                post_str = post.getText().toString();
                pin_str = pin.getText().toString();
                email_str = email.getText().toString();
                phone_str = phone.getText().toString();
                username_str = username.getText().toString();
                password_str = password.getText().toString();
//                proof_str = proof.getText().toString();
                gender_str = "";
                if (male.isChecked()) {
                    gender_str = male.getText().toString();
                } else {
                    gender_str = female.getText().toString();
                }
                if (firstname_str.equalsIgnoreCase("")) {
                    firstname.setError("Enter Your firstName");
                } else if (lastname_str.equalsIgnoreCase("")) {
                    lastname.setError("Enter Your lastname");
                } else if (place_str.equalsIgnoreCase("")) {
                    place.setError("Enter Your place");
                } else if (age_str.equalsIgnoreCase("")) {
                    age.setError("Enter Your place");
                } else if (post_str.equalsIgnoreCase("")) {
                    post.setError("Enter Your post");
                } else if (pin_str.equalsIgnoreCase("")) {
                    pin.setError("Enter Your pin");
                } else if (email_str.equalsIgnoreCase("")) {
                    email.setError("Enter Your email");
                } else if (phone_str.equalsIgnoreCase("")) {
                    phone.setError("Enter Your phone");
                }
//                else if (proof_str.equalsIgnoreCase("")) {
//                    proof.setError("please select your ID proof");
//                }
                else if (username_str.equalsIgnoreCase("")) {
                    username.setError("Enter Your username");
                } else if (password_str.equalsIgnoreCase("")) {
                    password.setError("Enter Your password");
                }
                else {
                    uploadBitmap(title);
                }
            }
        });

    }
    ProgressDialog pd;
    private void uploadBitmap(final String title)
    {
//        Toast.makeText(getApplicationContext(), "IIIIIIIIIIIIIIIIIIIII", Toast.LENGTH_LONG).show();
        RequestQueue queue = Volley.newRequestQueue(seller_registration.this);pd=new ProgressDialog(seller_registration.this);
        url = "http://" + sh.getString("ip","") + ":5000/seller_registration";
        pd.setMessage("Uploading....");
        pd.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response1) {
                        pd.dismiss();
                        String x=new String(response1.data);
                        try {
                            JSONObject obj = new JSONObject(new String(response1.data));
//                        Toast.makeText(Upload_agreement.this, "Report Sent Successfully", Toast.LENGTH_LONG).show();
                            if (obj.getString("task").equalsIgnoreCase("success")) {

                                Toast.makeText(seller_registration.this, "Successfully uploaded", Toast.LENGTH_LONG).show();
                                Intent i=new Intent(getApplicationContext(),login.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("First_name", firstname_str);
                params.put("Last_name", lastname_str);
                params.put("Age", age_str);
                params.put("Place", place_str);
                params.put("Post", post_str);
                params.put("Pin", pin_str);
                params.put("Phone", phone_str);
                params.put("Gender", gender_str);
                params.put("Email", email_str);
                params.put("Username", username_str);
                params.put("Password", password_str);
                return params;
            }

//            @Override
//            protected Map<String, DataPart> getByteData() {
//                Map<String, DataPart> params = new HashMap<>();
//                long imagename = System.currentTimeMillis();
//                params.put("proof", new DataPart(PathHolder , filedt ));
//                return params;
//            }
        };
        Volley.newRequestQueue(seller_registration.this).add(volleyMultipartRequest);
    }
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    @Override
//    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case 7:
//                if (resultCode == RESULT_OK) {
//                    Uri uri = data.getData();
//                    Log.d("File Uri", "File Uri: " + uri.toString());
//                    // Get the path
//                    try {
//                        PathHolder = FileUtils.getPathFromURI(registration.this, uri);
////                        PathHolder = data.getData().getPath();
////                        Toast.makeText(this, PathHolder, Toast.LENGTH_SHORT).show();
//
//                        filedt = getbyteData(PathHolder);
//                        Log.d("filedataaa", filedt + "");
////                        Toast.makeText(this, filedt+"", Toast.LENGTH_SHORT).show();
//                        proof.setText(PathHolder);
//                    }
//                    catch (Exception e){
//                        Toast.makeText(registration.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;
//        }
//    }
//    private byte[] getbyteData(String pathHolder) {
//        Log.d("path", pathHolder);
//        File fil = new File(pathHolder);
//        int fln = (int) fil.length();
//        byte[] byteArray = null;
//        try {
//            InputStream inputStream = new FileInputStream(fil);
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            byte[] b = new byte[fln];
//            int bytesRead = 0;
//
//            while ((bytesRead = inputStream.read(b)) != -1) {
//                bos.write(b, 0, bytesRead);
//            }
//            byteArray = bos.toByteArray();
//            inputStream.close();
//        } catch (Exception e) {
//        }
//        return byteArray;
//    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent ik = new Intent(getApplicationContext(), login.class);
        startActivity(ik);
    }

}