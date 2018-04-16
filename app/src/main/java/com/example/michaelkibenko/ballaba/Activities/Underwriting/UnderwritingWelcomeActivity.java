package com.example.michaelkibenko.ballaba.Activities.Underwriting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.michaelkibenko.ballaba.Activities.StreetAndMapBoard;
import com.example.michaelkibenko.ballaba.R;

public class UnderwritingWelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_underwriting_welcome);

        /*findViewById(R.id.underwriting_ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        findViewById(R.id.underwriting_cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

    public void onClickOk(){
        startActivity(new Intent(UnderwritingWelcomeActivity.this, StreetAndMapBoard.class));
    }

    public void onClickCancel(){
        finish();
    }
}
