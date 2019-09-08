package com.debdas.vishwajeet.rishav.ffs.foodforsurvival;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.debdas.vishwajeet.rishav.ffs.foodforsurvival.foodforsurvival.R;

import java.util.HashMap;
import java.util.Map;

public class SellerAddNewItem extends AppCompatActivity {
    final String item_add_table_URL = "https://debdasbrk350.000webhostapp.com/food/seller_insert_table.php";
    EditText jPrice, jQuantity;
    Spinner jItemName;
    Button jmodifyButton;
    ArrayAdapter<String> arrayAdapter;
    String price, quantity;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_item);
        jItemName = findViewById(R.id.ItemListSpinner);

        jPrice = findViewById(R.id.input_Item_Price);
        jQuantity = findViewById(R.id.input_Item_Quantity);
        jmodifyButton = findViewById(R.id.modify_Button);
        retrieveItem();
    }

    private void retrieveItem() {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        final String item_retrieve_URL = "https://debdasbrk350.000webhostapp.com/food/fooditem_retrieve.php";
        RequestQueue requestQueue = Volley.newRequestQueue(SellerAddNewItem.this);
        StringRequest stringRequest = new StringRequest(item_retrieve_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String[] A = response.split("\n");
                        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.layoutlist, A);
                        jItemName.setAdapter(arrayAdapter);
                        progressBar.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Failed" + error.toString(), Toast.LENGTH_LONG).show();
                progressBar.dismiss();
            }
        });
        requestQueue.add(stringRequest);
    }

    public boolean validate() {
        boolean valid = true;

        quantity = jQuantity.getText().toString();
        price = jPrice.getText().toString();

        if (quantity.isEmpty() || quantity.equals("0")) {
            jQuantity.setError("Enter Quantity Greater than 0");
            valid = false;
        } else {
            jQuantity.setError(null);
        }
        if (!quantity.isEmpty()) {
            if (Integer.parseInt(quantity) > 99) {
                jQuantity.setError("Enter quantity less than 100");
                valid = false;
            } else {
                jQuantity.setError(null);
            }
        }

        if (price.isEmpty() || price.equals("0")) {
            jPrice.setError("Don't Sell for FREE ");
            valid = false;
        } else {
            jPrice.setError(null);
        }

        if (!price.isEmpty()) {

            if (Integer.parseInt(price) > 500) {
                jPrice.setError("Contact Administrator");
                valid = false;
            } else {
                jPrice.setError(null);
            }

        }

        return valid;

    }

    public void updateItemsOnValidate(final View view) {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        RequestQueue requestQueue = Volley.newRequestQueue(SellerAddNewItem.this);

        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(
                view.getWindowToken(), 0);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, item_add_table_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("present")) {

                            Snackbar snackbar = Snackbar.make(view, "Item Already Exist", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        } else {

                            Snackbar snackbar = Snackbar.make(view, response, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                        progressBar.dismiss();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server Down" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Intent intent = getIntent();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                params.put("item_name", jItemName.getSelectedItem().toString().trim());
                params.put("quantity", jQuantity.getText().toString().trim());
                params.put("price", jPrice.getText().toString().trim());
                params.put("email", pref.getString("email", null).trim());
                return params;

            }
        };
        requestQueue.add(stringRequest);
    }


    public void updateItems(View view) {
        if (validate()) {
            updateItemsOnValidate(view);
        }

    }
}