package com.debdas.vishwajeet.rishav.ffs.foodforsurvival;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.debdas.vishwajeet.rishav.ffs.foodforsurvival.foodforsurvival.R;


public class WebSignUp extends AppCompatActivity {

    WebView simpleWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_sign_up);
        simpleWebView = findViewById(R.id.webView1);

// set web view client
        simpleWebView.setWebViewClient(new MyWebViewClient());

// string url which you have to load into a web view
        String url = "https://goo.gl/forms/MIp3gH5xJEjHsEkr2";
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.loadUrl(url); // load the url on the web view
    }

    // custom web view client class who extends WebViewClient
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url); // load the url
            return true;
        }
    }
}

