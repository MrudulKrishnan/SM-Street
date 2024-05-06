package com.example.sm_street_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class custom_view_offers extends BaseAdapter {
    private Context context;
    ArrayList<String> product;
    ArrayList<String> offer_details;
    ArrayList<String> product_img;
    ArrayList<String> start_date;
    ArrayList<String> end_date;
//    ArrayList<String> product_id_ArrayStr;
    String title, url;
    SharedPreferences sh;
    public custom_view_offers(Context applicationContext, ArrayList<String> product_arr,
                                  ArrayList<String> offer_details_arr, ArrayList<String> product_img_arr,
                                  ArrayList<String> start_date_arr, ArrayList<String> end_date_arr)
    {
        // TODO Auto-generated constructor stub
        this.context = applicationContext;
        this.product = product_arr;
        this.offer_details = offer_details_arr;
        this.product_img = product_img_arr;
        this.start_date = start_date_arr;
        this.end_date = end_date_arr;
        sh= PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return product.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public int getItemViewType(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if(convertview==null)
        {
            gridView=new View(context);
            gridView=inflator.inflate(R.layout.activity_custom_view_offers,null);

        }
        else
        {
            gridView=(View)convertview;

        }
        ///////////////////////
        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        /////////////////////////////////

        TextView tv1_product_name = (TextView)gridView.findViewById(R.id.TV1_ProductName);
        TextView tv2_product_type = (TextView)gridView.findViewById(R.id.TV2_Details);
        TextView tv3_product_details = (TextView)gridView.findViewById(R.id.TV3_Price);
        TextView tv4_product_price = (TextView)gridView.findViewById(R.id.TV4_EndDate);
        ImageView im_product_image = (ImageView) gridView.findViewById(R.id.Product_image);
//        Button B1_send_request = gridView.findViewById(R.id.B1_ProductRequest);

//        B1_send_request.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                RequestQueue queue = Volley.newRequestQueue(context);
//                url = "http://"+sh.getString("ip","")+":5000/product_request";
////                Toast.makeText(context,url , Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "Product Request sent successfully", Toast.LENGTH_SHORT).show();
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response)
//                    {
//                        // Display the response string.
//                        Log.d("+++++++++++++++++", response);
//                        try
//                        {
//                            JSONObject json = new JSONObject(response);
//                            String res = json.getString("task");
//
//                            if (res.equalsIgnoreCase("success"))
//                            {
//                                String lid = json.getString("id");     // getting login id
//                                SharedPreferences.Editor edp = sh.edit();
//                                edp.putString("lid", lid);
//                                edp.commit();
//                                Intent ik = new Intent(context, home.class);
//                                context.startActivity(ik);
//                            }
//                            else
//                            {
//                                Toast.makeText(context, "Invalid request", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        catch (JSONException e)
//                        {
//                            e.printStackTrace();
//                        }
//                    }
//                },new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error)
//                    {
//                        Toast.makeText(context, "Error" + error, Toast.LENGTH_LONG).show();
//                    }
//                }) {
//                    @Override
//                    protected Map<String, String> getParams()
//                    {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("lid", sh.getString("lid", ""));
//                        params.put("p_id",product_id_ArrayStr.get(position));
//                        return params;
//                    }
//                };
//                queue.add(stringRequest);
//
//            }
//        });
        java.net.URL thumb_u;
        try {

            //thumb_u = new java.net.URL("http://192.168.43.57:5000/static/photo/flyer.jpg");

            thumb_u = new java.net.URL("http://"+sh.getString("ip","")+":5000"+product_img.get(position));
            Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
            im_product_image.setImageDrawable(thumb_d);
        }
        catch (Exception e)
        {
            Log.d("errsssssssssssss",""+e);
        }

        tv1_product_name.setText(product.get(position));
        tv2_product_type.setText(offer_details.get(position));
        tv3_product_details.setText(start_date.get(position));
        tv4_product_price.setText(end_date.get(position));

        tv1_product_name.setTextColor(Color.BLACK);
        tv2_product_type.setTextColor(Color.BLACK);
        tv3_product_details.setTextColor(Color.BLACK);
        tv4_product_price.setTextColor(Color.BLACK);

        return gridView;

    }

}
