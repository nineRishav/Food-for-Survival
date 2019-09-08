package com.debdas.vishwajeet.rishav.ffs.foodforsurvival;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.debdas.vishwajeet.rishav.ffs.foodforsurvival.foodforsurvival.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class ViewOrderBuyer extends AppCompatActivity {
    private static final String URL_PRODUCTS = "https://debdasbrk350.000webhostapp.com/food/buyer_account_view_order.php";
    RecyclerView recyclerView;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_buyer);
        recyclerView = findViewById(R.id.ViewOrderRecyclerBuyer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        call();
    }

    private void call() {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("[]")) {
                            Toast.makeText(ViewOrderBuyer.this, "You've not Order anything yet!", Toast.LENGTH_SHORT).show();
                        }

                        GsonBuilder gsonBuilder1 = new GsonBuilder();
                        Gson gson1 = gsonBuilder1.create();
                        OrderDetail[] orderDetails = gson1.fromJson(response, OrderDetail[].class);
                        recyclerView.setAdapter(new RecyclerAdapterForOrderDetail(ViewOrderBuyer.this, orderDetails));
                        progressBar.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ViewOrderBuyer.this, "Error in Retrieving ORDER" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                params.put("email", pref.getString("email", null).trim());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
