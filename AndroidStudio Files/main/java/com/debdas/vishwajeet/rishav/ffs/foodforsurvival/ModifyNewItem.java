package com.debdas.vishwajeet.rishav.ffs.foodforsurvival;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class ModifyNewItem extends AppCompatActivity {

    final String item_table_URL = "https://debdasbrk350.000webhostapp.com/food/seller_modify_seller_item.php";
    EditText jPrice, jQuantity;
    TextView jItemName;
    Button jmodifyButton;
    Intent intent;
    String itemName, quantity, price, sellerName;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_new_item);

        jItemName = findViewById(R.id.input_Item_Name);
        jPrice = findViewById(R.id.input_Item_Price);
        jQuantity = findViewById(R.id.input_Item_Quantity);
        jmodifyButton = findViewById(R.id.modify_Button);

        intent = getIntent();
        itemName = intent.getStringExtra("item_name");
        quantity = intent.getStringExtra("quantity");
        price = intent.getStringExtra("price");
        sellerName = intent.getStringExtra("seller_name");

        jItemName.setText(itemName);
        jPrice.setText(price);
        jQuantity.setText(quantity);

    }

    public void updateItemsOnValidate(View view) {

        //for hiding keyboard
        View view1 = this.getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        RequestQueue requestQueue = Volley.newRequestQueue(ModifyNewItem.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, item_table_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1"))          // Problem "1    \n\t" from response in android, php checked"
                        {
                            Toast.makeText(getApplicationContext(), "Modified Successfully", Toast.LENGTH_SHORT).show();
                            Intent mainPage = new Intent(getApplicationContext(), SellerMainActivity.class);
                            startActivity(mainPage);
                        } else {
                            Toast.makeText(getApplicationContext(), "Try Again " + response, Toast.LENGTH_SHORT).show();
                        }
                        progressBar.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("item_name", jItemName.getText().toString().trim());
                params.put("price", jPrice.getText().toString().trim());
                params.put("quantity", jQuantity.getText().toString().trim());
                params.put("seller_name", sellerName.trim());
                return params;

            }
        };
        requestQueue.add(stringRequest);
    }

    public boolean validate() {
        boolean valid = true;

        price = jPrice.getText().toString();

        if (price.isEmpty() || price.equals("0")) {
            jPrice.setError("Don't Sell for FREE ");
            valid = false;
        } else {
            jPrice.setError(null);
        }
        return valid;

    }

    public void updateItems(View view) {
        if (validate())
            updateItemsOnValidate(view);
    }
}