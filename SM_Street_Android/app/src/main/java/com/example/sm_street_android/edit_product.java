package com.example.sm_street_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class edit_product extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText e1_product_name, e2_type, e3_details, e4_price_per_day, e5_image;
    Button b1_upload, b2_add;
    Spinner sp1_category;
    String product_name_str, product_type_str, product_details_str, price_per_day_str,
            image_str, url, title;
    ImageView iv1_product;
    ArrayList<String> image_array_str, category_arr, category_id_arr;
    SharedPreferences sh;
    String PathHolder = "", category_id_str;
    byte[] filedt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        e1_product_name = findViewById(R.id.E1_ProductNameE);
        sp1_category = findViewById(R.id.SP1_EditCategory);
        e3_details = findViewById(R.id.E3_DetailsE);
        e4_price_per_day = findViewById(R.id.E4_PricePerDayE);
        e5_image = findViewById(R.id.E5_ImageE);
        iv1_product = findViewById(R.id.IV1_ProductE);
        b1_upload = findViewById(R.id.B1_imageE);
        b2_add = findViewById(R.id.B2_Edit);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());






//        ArrayAdapter<String> ad = new ArrayAdapter<>(edit_product.this, android.R.layout.simple_list_item_1, ar1);
//        s1.setAdapter(ad);


        url ="http://"+sh.getString("ip", "") + ":5000/edit_product_view";
        RequestQueue queue = Volley.newRequestQueue(edit_product.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try
                {
                    Toast.makeText(edit_product.this, "err" + response, Toast.LENGTH_SHORT).show();

                    JSONArray ar=new JSONArray(response);
                    {
                        JSONObject jo=ar.getJSONObject(0);
                        e1_product_name.setText(jo.getString("product"));
                        e3_details.setText(jo.getString("Details"));
                        e4_price_per_day.setText(jo.getString("Price_per_day"));
                        e5_image.setText(jo.getString("Image"));
                        image_array_str.add(jo.getString("Image"));
                        Toast.makeText(edit_product.this, "image_array_str"+image_array_str, Toast.LENGTH_SHORT).show();

//                        if(android.os.Build.VERSION.SDK_INT>9)
//                        {
//                            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
//                            StrictMode.setThreadPolicy(policy);
//                        }
//                        iv1_product.setImageDrawable(Drawable.createFromPath(getIntent().getStringExtra("Image")));
                        java.net.URL thumb_u;
                        try {
                            //thumb_u = new java.net.URL("http://192.168.43.57:5000/static/photo/flyer.jpg");
//                            thumb_u = new java.net.URL("http://"+sh.getString("ip","")+":5000"+product_image_str.get(position));
                            Toast.makeText(edit_product.this, "image_array_str"+image_array_str, Toast.LENGTH_SHORT).show();

                            thumb_u = new java.net.URL("http://"+sh.getString("ip","")+":5000/"+image_array_str);
                            Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
                            iv1_product.setImageDrawable(thumb_d);
                        }
                        catch (Exception e)
                        {
                            Log.d("errsssssssssssss",""+e);
                        }
                    }
                }
                catch (Exception e)
                {
                    Log.d("=========", e.toString());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(edit_product.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("p_id",getIntent().getStringExtra("p_id"));
                return params;
            }
        };
        queue.add(stringRequest);



        url = "http://" + sh.getString("ip", "") + ":5000/view_category_user";
        RequestQueue queue1 = Volley.newRequestQueue(edit_product.this);

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
                    Toast.makeText(edit_product.this, "err" + response, Toast.LENGTH_SHORT).show();
                    JSONArray ar = new JSONArray(response);

                    category_arr = new ArrayList<>();
                    category_id_arr = new ArrayList<>();


                    for (int i=0; i<ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        category_arr.add(jo.getString("category"));
                        category_id_arr.add(jo.getString("category_id"));

                    }

                    ArrayAdapter<String> ad=new ArrayAdapter<String>(edit_product.this,android.R.layout.simple_list_item_1,category_arr );
                    sp1_category.setAdapter(ad);
                    sp1_category.setOnItemSelectedListener(edit_product.this);


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"========="+e,Toast.LENGTH_LONG).show();
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(edit_product.this, "err" + error, Toast.LENGTH_SHORT).show();
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
        queue1.add(stringRequest1);


        b1_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
//            intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 7);
            }
        });

        b2_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product_name_str = e1_product_name.getText().toString();
                product_details_str = e3_details.getText().toString();
                price_per_day_str = e4_price_per_day.getText().toString();
                image_str = e5_image.getText().toString();

                if (product_name_str.equalsIgnoreCase("")) {
                    e1_product_name.setError("Enter your product name");
                } else if (product_details_str.equalsIgnoreCase("")) {
                    e3_details.setError("Enter product_details");
                } else if (price_per_day_str.equalsIgnoreCase("")) {
                    e4_price_per_day.setError("Enter type");
                } else if (image_str.equalsIgnoreCase("")) {
                    e5_image.setError("upload your product photo");

                }
                else
                {
                    uploadBitmap(title);
                }
            }
        });

    }
    ProgressDialog pd;
    private void uploadBitmap(final String title)
    {
        Toast.makeText(edit_product.this, "Report Sent Successfully%%%%%%%%%%%%%%%%", Toast.LENGTH_LONG).show();
        RequestQueue queue3 = Volley.newRequestQueue(edit_product.this);
        url = "http://" + sh.getString("ip","") + ":5000/edit_product";
        pd=new ProgressDialog(edit_product.this);
        pd.setMessage("Uploading....");
        pd.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>()
        {
            @Override
            public void onResponse(NetworkResponse response1) {
                pd.dismiss();
                String x=new String(response1.data);
                try {
                    JSONObject obj = new JSONObject(new String(response1.data));
                    if (obj.getString("task").equalsIgnoreCase("success"))
                    {
                        Toast.makeText(edit_product.this, "Successfully uploaded", Toast.LENGTH_LONG).show();
                        Intent i=new Intent(getApplicationContext(), manage_product.class);
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
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();

                params.put("product_name",product_name_str);
                params.put("category_id",category_id_str);
                params.put("product_details",product_details_str);
                params.put("price_per_day",price_per_day_str);
                params.put("p_id",getIntent().getStringExtra("p_id"));
//                params.put("lid",sh.getString("lid",""));
                return params;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(PathHolder , filedt ));
                return params;
            }
        };
        Volley.newRequestQueue(edit_product.this).add(volleyMultipartRequest);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 7:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Log.d("File Uri", "File Uri: " + uri.toString());
                    // Get the path
                    try {
                        PathHolder = FileUtils.getPathFromURI(edit_product.this, uri);
//                        PathHolder = data.getData().getPath();
//                        Toast.makeText(this, PathHolder, Toast.LENGTH_SHORT).show();

                        filedt = getbyteData(PathHolder);
                        Log.d("filedataaa", filedt + "");
//                        Toast.makeText(this, filedt+"", Toast.LENGTH_SHORT).show();
                        e5_image.setText(PathHolder);
                    }
                    catch (Exception e){
                        Toast.makeText(edit_product.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
    private byte[] getbyteData(String pathHolder) {
        Log.d("path", pathHolder);
        File fil = new File(pathHolder);
        int fln = (int) fil.length();
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(fil);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[fln];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }
            byteArray = bos.toByteArray();
            inputStream.close();
        } catch (Exception e) {
        }
        return byteArray;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category_id_str = category_id_arr.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent ik = new Intent(getApplicationContext(), manage_product.class);
        startActivity(ik);
    }
}