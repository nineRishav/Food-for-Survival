package com.debdas.vishwajeet.rishav.ffs.foodforsurvival;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.debdas.vishwajeet.rishav.ffs.foodforsurvival.foodforsurvival.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    EditText password_editText, email_editText;
    String email, password;
    ProgressDialog progressBar;
    RadioButton radioButtonSelected;
    RadioGroup radioGroup;
    Button loginButton, signUpButton;
    SharedPreferences pref;

    public static boolean hasPermisssion(Context ctx, String... permissions) {
        if (ctx != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(ctx, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS};

        if (!hasPermisssion(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        email_editText = findViewById(R.id.email);
        password_editText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        radioGroup = findViewById(R.id.rg);
        radioButtonSelected = findViewById(R.id.buyer_rb);
        signUpButton = findViewById(R.id.signUpButton);
        radioButtonSelected.setChecked(true);
        String s="<font color='#EE0000'>Sign Up</font>";
        String sourceString = "Don't have an account";

        signUpButton.setText(Html.fromHtml(sourceString+" "+s));
        loginButton.setEnabled(true);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        password_editText.setText(pref.getString("password", null));
        email_editText.setText(pref.getString("email", null));

        isInternetOn();
    }

    public void nextLogin(View view) {
        progressBar = new ProgressDialog(view.getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("Authenticating...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();


        if (!validate()) {
            onLoginFailed();
            return;
        }

        int r = radioGroup.getCheckedRadioButtonId();
        radioButtonSelected = findViewById(r);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://debdasbrk350.000webhostapp.com/food/buyer_login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                switch (response) {
                    case "1": {
                        //Buyer

                        String recent_token = FirebaseInstanceId.getInstance().getToken();
                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        User user=new User(recent_token,email);
                        databaseReference.setValue(user);

                        Intent intent = new Intent(getApplicationContext(), BuyerMainActivity.class);
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.putString("type", "BUYER");
                        editor.commit();
                        progressBar.setProgress(100);
                        progressBar.dismiss();
                        loginButton.setEnabled(true);
                        startActivity(intent);
                        break;
                    }
                    case "2": {
                        //seller activity
                        Intent intent = new Intent(getApplicationContext(), SellerMainActivity.class);
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.putString("type", "SELLER");
                        editor.commit();
                        progressBar.setProgress(100);
                        progressBar.dismiss();
                        loginButton.setEnabled(true);
                        startActivity(intent);
                        break;
                    }
                    default:
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                        loginButton.setEnabled(true);
                        break;
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.toString().equals("com.android.volley.ClientError"))
                    Toast.makeText(getApplicationContext(), "Server Down, Try again after sometime", Toast.LENGTH_LONG).show();
                progressBar.dismiss();
                loginButton.setEnabled(true);
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> prams = new HashMap<>();
                prams.put("email", email.trim());
                prams.put("password", password.trim());
                prams.put("role", radioButtonSelected.getText().toString().trim());
                return prams;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void aboutsUsN(View view) {
        Intent go = new Intent(getApplicationContext(), AboutUs.class);
        startActivity(go);
    }

    public boolean validate() {
        loginButton.setEnabled(false);
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        boolean valid = true;

        email = email_editText.getText().toString();
        password = password_editText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_editText.setError("Invalid email address");
            progressBar.dismiss();
            valid = false;
        } else {
            email_editText.setError(null);
        }

        if (password.isEmpty() || password.length() < 3 || password.length() > 12) {
            password_editText.setError("Enter between 3 and 12 characters");
            progressBar.dismiss();
            valid = false;
        } else {
            password_editText.setError(null);
        }
        return valid;

    }

    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginFailed() {
        //Toast.makeText(getApplicationContext(), " Login failed", Toast.LENGTH_LONG).show();
        progressBar.dismiss();

        loginButton.setEnabled(true);
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            Toast.makeText(this, " No Internet Connection", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    public void WebView(View view) {
        Intent intent = new Intent(getApplicationContext(), GoogleSignIn.class);
        startActivity(intent);
    }
}