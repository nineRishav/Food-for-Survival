package com.debdas.vishwajeet.rishav.ffs.foodforsurvival;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class Bill extends AppCompatActivity {

    StringRequest stringRequest;
    String item_name, seller_name, amount, quantity, image_url, order_no, buyer_name, price_per_item;
    ImageView itemImage;
    TextView jorderNo, jItemName, jSellerName, jprice, jquantity, jRoom, jPhone;
    RequestOptions options = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);


        Intent intent = getIntent();
        order_no = intent.getStringExtra("order_no").replace("$$", "").trim();
        item_name = intent.getStringExtra("item_name");
        seller_name = intent.getStringExtra("seller_name");
        amount = intent.getStringExtra("amount");
        quantity = intent.getStringExtra("quantity");
        image_url = intent.getStringExtra("item_url");
        price_per_item = intent.getStringExtra("price");

        jorderNo = findViewById(R.id.OrderNo);
        jItemName = findViewById(R.id.ItemName);
        jSellerName = findViewById(R.id.SellerName);
        jprice = findViewById(R.id.price);
        jquantity = findViewById(R.id.Quantity);
        jRoom = findViewById(R.id.room_no);
        jPhone = findViewById(R.id.phone);
        itemImage = findViewById(R.id.itemLogo);

        jorderNo.setText(String.format("Order No. : %s", order_no));
        jItemName.setText(item_name);
        jSellerName.setText(String.format("Seller : %s", seller_name));
        jquantity.setText(String.format("Qty : %s", quantity));
        jprice.setText(String.format("₹%s", amount));
        Glide.with(getApplicationContext()).load(image_url).apply(options).into(itemImage);
        setCaller();

    }

    private void setCaller() {

        stringRequest = new StringRequest(Request.Method.POST, "https://debdasbrk350.000webhostapp.com/food/call_seller.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String[] s = response.split("\n");
                //s[0]=Room Number  s[1]=Phone Number
                jRoom.setText(String.format("Room no. : %s", s[0]));
                jPhone.setText(String.format("Phone no.: %s", s[1]));
                SharedPreferences sharedPreferences = getSharedPreferences("MyPref", 0);
                buyer_name = sharedPreferences.getString("email", "");


                //send Message
                sendMessage(s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> prams = new HashMap<>();
                prams.put("name", seller_name.trim());
                return prams;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    // Sending message to Seller
    public void sendMessage(String[] s) {

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto: " + s[1]));
        intent.putExtra("sms_body", "Food for Survival\n\n" + "Order no : #" + order_no + "\nBuyer : " + buyer_name + "\n" +
                item_name + "(" + price_per_item + ") x" + quantity + " = " + " Rs. " + amount);
        startActivity(intent);
//
//        String SMS_SENT = "SMS_SENT";
//        String SMS_DELIVERED = "SMS_DELIVERED";
//
//        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SMS_SENT), 0);
//        PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SMS_DELIVERED), 0);
//
//        // For when the SMS has been sent
//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        Toast.makeText(context, "Generic failure cause", Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_NO_SERVICE:
//                        Toast.makeText(context, "Service is currently unavailable", Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_NULL_PDU:
//                        Toast.makeText(context, "No pdu provided", Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        Toast.makeText(context, "Radio was explicitly turned off", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        }, new IntentFilter(SMS_SENT));
//
//// For when the SMS has been delivered
//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        Toast.makeText(getBaseContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        }, new IntentFilter(SMS_DELIVERED));
//
//// Get the default instance of SmsManager
//        SmsManager smsManager = SmsManager.getDefault();
//        smsManager.sendTextMessage(s[1], null, "Food for Survival\n\n" + "Order no : #" + order_no + "\nBuyer : " + buyer_name + "\n" +
//                item_name + "(" + price_per_item + ") x" + quantity + " = " + " Rs. " + amount, sentPendingIntent, deliveredPendingIntent);

    }


    // Call seller when number  clicked
    public void callSeller(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + jPhone.getText().toString().replace("Phone no.: ", "").trim()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        startActivity(intent);


    }

}