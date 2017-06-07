package com.example.shivam6731.hawkers;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class firstpage extends AppCompatActivity {
WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(firstpage.this,MainActivity.class));
            }
        },3500);

        webview=(WebView)findViewById(R.id.webview);
        webview.loadUrl("file:///android_asset/foody.gif");
    }
}
