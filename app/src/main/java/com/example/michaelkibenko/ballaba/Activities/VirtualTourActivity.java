package com.example.michaelkibenko.ballaba.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;

import com.example.michaelkibenko.ballaba.R;

public class VirtualTourActivity extends BaseActivity {


    private static final String VIRTUAL_TOUR_ENDPOINT_KEY = "virtual tour endpoint key";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.virtual_tour_activity_layout);
        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        WebView example = (WebView)findViewById(R.id.exampleWebView);
        example.getSettings().setJavaScriptEnabled(true);
        example.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        example.loadUrl("https://my.matterport.com/show/?m=eN4agGUh4BK");
    }
}
