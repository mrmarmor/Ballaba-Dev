package com.example.michaelkibenko.ballaba.Activities.Scoring;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class ScoringConfirmIDActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_TAKE_PIC = 15135;
    private ProgressBar progressBar;
    private Bitmap photo;
    private JSONObject object;
    private byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoring_confirm_id_activity_layout);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();


        object = new JSONObject();


        progressBar = findViewById(R.id.scoring_confrim_id_progress_bar);
        findViewById(R.id.activity_confirm_id_back_btn).setOnClickListener(this);
        findViewById(R.id.confrim_id_take_picture_text_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent intent = new Intent(ScoringConfirmIDActivity.this , ScoringCameraActivity.class);
                //Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(intent, REQUEST_TAKE_PIC);

                Intent intent = new Intent(ScoringConfirmIDActivity.this , ScoringCameraActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.confirm_id_activity_continue_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                ConnectionsManager.getInstance(ScoringConfirmIDActivity.this).uploadScoringID(object, true, new BallabaResponseListener() {
                    @Override
                    public void resolve(BallabaBaseEntity entity) {
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(ScoringConfirmIDActivity.this, ScoringPicTakenActivity.class);
                        intent.putExtra("USER_IMAGE", byteArray);
                        startActivity(intent);
                    }

                    @Override
                    public void reject(BallabaBaseEntity entity) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ScoringConfirmIDActivity.this, "We couldn't manage to detect an id in this picture..\nplease try again", Toast.LENGTH_LONG).show();
                    }
                });
                /*Intent intent = new Intent(ScoringConfirmIDActivity.this, ScoringPicTakenActivity.class);
                intent.putExtra("USER_IMAGE", byteArray);
                startActivity(intent);*/
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
        if (resultCode == RESULT_OK && requestCode == REQUEST_TAKE_PIC) {

            photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            try {
                object.put("image", encoded);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //photo.recycle();
            UiUtils.instance(true, this).buttonChanger((Button) findViewById(R.id.confirm_id_activity_continue_btn), true);
        }
    }
}
