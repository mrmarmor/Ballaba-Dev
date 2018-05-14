package com.example.michaelkibenko.ballaba.Activities.Scoring;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;

public class ScoringPicTakenActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_TAKE_PIC = 43252;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoring_pic_taken_activity_layout);


        UiUtils.instance(true , this).buttonChanger((Button) findViewById(R.id.confirm_pic_taken_activity_continue_btn), true);

        byte[] chartData = getIntent().getByteArrayExtra("USER_IMAGE");
        ImageView imgViewer = findViewById(R.id.confrim_pic_taken_title_image_view);
        Bitmap bm = BitmapFactory.decodeByteArray(chartData, 0, chartData.length);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        imgViewer.setMinimumHeight(dm.heightPixels);
        imgViewer.setMinimumWidth(dm.widthPixels);
        imgViewer.setImageBitmap(bm);

        findViewById(R.id.activity_pic_taken_back_btn).setOnClickListener(this);
        findViewById(R.id.confirm_pic_taken_activity_continue_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScoringPicTakenActivity.this , ScoringSelfieActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }

}
