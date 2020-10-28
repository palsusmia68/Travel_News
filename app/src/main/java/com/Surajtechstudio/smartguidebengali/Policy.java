package com.Surajtechstudio.smartguidebengali;

import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Policy extends AppCompatActivity {
    WebView wbv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        setContentView(R.layout.activity_policy);
        wbv=(WebView)findViewById(R.id.wbv);
        //wbv.getSettings().setLoadWithOverviewMode(true);
        //wbv.getSettings().setUseWideViewPort(true);
        wbv.getSettings().setJavaScriptEnabled(true);
        wbv.getSettings().setSupportMultipleWindows(true);
        wbv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //wbv.getSettings().setBuiltInZoomControls(true);
        wbv.setWebViewClient(new WebViewClient() {
                                 @Override
                                 public void onPageStarted(WebView view, String url, Bitmap favicon) {

                                     super.onPageStarted(view, url, favicon);
                                 }

                                 @Override
                                 public void onPageFinished(WebView view, String url) {
                                     super.onPageFinished(view, url);
                                 }

                                 public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                     return false;
                                 }

                             }
        );

        wbv.loadUrl("file:///android_asset/privacysmartguide.html");

    }
}
