package com.debdas.vishwajeet.rishav.ffs.foodforsurvival;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.debdas.vishwajeet.rishav.ffs.foodforsurvival.foodforsurvival.R;

import java.util.HashMap;
import java.util.Map;

public class SellerList extends AppCompatActivity {

    ProgressBar progressBar;
    RequestOptions options = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_list);

        progressBar = findViewById(R.id.pbsl);
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        String s = intent.getStringExtra("item_name");
        String item_url = intent.getStringExtra("item_url");

        ImageView imageView = findViewById(R.id.iv_url);
        Glide.with(SellerList.this).load(item_url).apply(options).into(imageView);

        fetchSellerList(s, item_url);
    }

    private void fetchSellerList(final String itemname, final String item_url) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://debdasbrk350.000webhostapp.com/food/seller_retrieve_table.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ListView listView = findViewById(R.id.seller_lv);
                String[] s = response.split("\n");
                for (int i = 0; i < s.length; i++) {
                    String[] s1 = s[i].split("\t");
                    s[i] = "Seller: " + s1[0] + "\nQuantity: " + s1[1] + "\n ₹ " + s1[2];

                }
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.layoutlist, s);
                listView.setAdapter(arrayAdapter);

                progressBar.setVisibility(View.GONE);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(SellerList.this, ItemDetail.class);
                        intent.putExtra("item_name", itemname);
                        intent.putExtra("item_url", item_url);
                        String[] sn = arrayAdapter.getItem(position).split("\n");

                        intent.putExtra("seller_name", sn[0]);
                        intent.putExtra("price", sn[2].replace("₹", ""));
                        intent.putExtra("qty", sn[1].replace("Quantity:", ""));
                        startActivity(intent);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> prams = new HashMap<>();
                prams.put("item_name", itemname.trim());
                return prams;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}