package com.debdas.vishwajeet.rishav.ffs.foodforsurvival;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class SellerMainActivity extends AppCompatActivity {
    private static final String URL_PRODUCTS = "https://debdasbrk350.000webhostapp.com/food/seller_account_item.php";
    RecyclerView recyclerView;
    StringRequest stringRequest;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutSeller);
        recyclerView = findViewById(R.id.recyclerViewSeller);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        getItems();

        FloatingActionButton fab = findViewById(R.id.floating_modify_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerMainActivity.this, SellerAddNewItem.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fabViewOrder = findViewById(R.id.floating_ViewOrder_button);
        fabViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToOrderHistory = new Intent(SellerMainActivity.this, ViewOrder.class);
                startActivity(intentToOrderHistory);
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                getItems();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_button_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_credit:
                About();
                return true;
            case R.id.action_contactUs:
                Contact();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Mess() {

    }

    private void Contact() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String[] recipients = {"foodforsurvival2019@gmail.com", "9rishav@gmail.com", "vishjeet4@gmail.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        intent.setType("text/html");
        startActivity(Intent.createChooser(intent, "Send mail"));

    }

    private void About() {
        Intent i = new Intent(SellerMainActivity.this, AboutUs.class);
        startActivity(i);
    }


    public void getItems() {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        stringRequest = new StringRequest(Request.Method.POST, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("[]")) {
                            Toast.makeText(SellerMainActivity.this, "No Item to Display", Toast.LENGTH_LONG).show();
                        }
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        ProductDetail[] productDetails = gson.fromJson(response, ProductDetail[].class);
                        recyclerView.setAdapter(new RecyclerAdapter(SellerMainActivity.this, productDetails));

                        progressBar.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SellerMainActivity.this, "Error in Retrieving the items", Toast.LENGTH_LONG).show();
                progressBar.dismiss();
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

