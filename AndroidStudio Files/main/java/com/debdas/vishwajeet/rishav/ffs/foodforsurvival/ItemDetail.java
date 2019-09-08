package com.debdas.vishwajeet.rishav.ffs.foodforsurvival;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.debdas.vishwajeet.rishav.ffs.foodforsurvival.foodforsurvival.R;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public class ItemDetail extends AppCompatActivity {
    String seller_name;
    String price, qty;
    TextView ptv, sellertv, pricetv, amt;
    Spinner spinner;
    String itemname, selectedItem, image_url;
    ProgressBar progressBar;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Intent intent = getIntent();

        progressBar = findViewById(R.id.pbid);
        progressBar.setVisibility(View.GONE);

        itemname = intent.getStringExtra("item_name");
        image_url = intent.getStringExtra("item_url");
        seller_name = intent.getStringExtra("seller_name").replace("Seller:", "");
        price = intent.getStringExtra("price").trim();
        qty = intent.getStringExtra("qty").trim();


        ptv = findViewById(R.id.ptv);
        sellertv = findViewById(R.id.sellertv);
        pricetv = findViewById(R.id.pricetv);
        spinner = findViewById(R.id.sp);
        amt = findViewById(R.id.amounttv);

        String[] a = {"1", "2", "3", "4", "5", "6", "7", "8"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinnerlayout, a);
        spinner.setAdapter(arrayAdapter);


        ptv.setText(itemname);
        sellertv.setText(format("Seller: %s", seller_name));
        pricetv.setText(price);

        ImageView imageView = findViewById(R.id.img);

        Glide.with(ItemDetail.this).load(image_url).into(imageView);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("DefaultLocale")
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                int p = Integer.parseInt(price) * Integer.parseInt(selectedItem);
                amt.setText(format("%d", p));
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void buyProduct(View view) {
        progressBar.setVisibility(View.VISIBLE);
        if (Integer.parseInt(selectedItem) > Integer.parseInt(qty)) {
            Snackbar.make(view, "Only " + qty + " pcs are available", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            progressBar.setVisibility(View.GONE);

        } else {

            stringRequest = new StringRequest(Request.Method.POST, "https://debdasbrk350.000webhostapp.com/food/buyer_modify_item.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("finish")) {
                        Toast.makeText(getApplicationContext(), "Sorry 0 item Left", Toast.LENGTH_LONG).show();
                    } else if (response.contains("$$")) {
                        Intent intent = new Intent(getApplicationContext(), Bill.class);
                        intent.putExtra("order_no", response.replace("\n\t", ""));          //Problem
                        intent.putExtra("item_name", itemname);
                        intent.putExtra("seller_name", seller_name);
                        intent.putExtra("quantity", selectedItem);
                        intent.putExtra("amount", amt.getText());
                        intent.putExtra("item_url", image_url);
                        intent.putExtra("price", price);
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("seller_name", seller_name);
                        editor.commit();
                        progressBar.setVisibility(View.GONE);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Order Failed", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> prams = new HashMap<>();

                    SharedPreferences sharedPreferences = getSharedPreferences("MyPref", 0);
                    prams.put("item_name", itemname.trim());
                    int z = (Integer.parseInt(qty) - Integer.parseInt(selectedItem));
                    prams.put("seller_name", seller_name.trim());
                    prams.put("email", sharedPreferences.getString("email", null).trim());
                    prams.put("amount", amt.getText().toString().trim());
                    prams.put("qty", selectedItem.trim());

                    return prams;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);


        }
    }

}
