package com.example.michaelkibenko.ballaba.Activities.Scoring;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;

import java.io.ByteArrayOutputStream;

public class ScoringSelfieActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_TAKE_PIC = 45243;
    private Bitmap photo;
    private byte[] byteArray;
    private String encoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoring_selfie_activity_layout);

        findViewById(R.id.activity_confirm_id_back_btn).setOnClickListener(this);
        Button takePicBtn = findViewById(R.id.confrim_id_take_picture_btn);
        UiUtils.instance(true , this).buttonChanger(takePicBtn , true);
        takePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                startActivityForResult(intent, REQUEST_TAKE_PIC);
                Intent intent = new Intent(ScoringSelfieActivity.this, SelfCamActivity.class);
                startActivityForResult(intent, REQUEST_TAKE_PIC);
            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PIC && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
            encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Intent intent = new Intent(this, ScoringImageComparionActivity.class);
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            //intent.putExtra("IMAGE" , byteArray);
            intent.putExtra("IMAGE" , encoded);
            startActivity(intent);
        }
    }
}
