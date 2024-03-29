package com.example.michaelkibenko.ballaba.Activities.Scoring;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Managers.OCRHelper;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ScoringImageComparionActivity extends BaseActivity {

    private ProgressBar progressBar;
    private TextView textView;
    private RelativeLayout relativeLayout;
    private ImageView imageView;
    private Button tryAgainBtn;
//    private byte[] bytes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoring_image_comparion_layout);

        progressBar = findViewById(R.id.scoring_image_comparison_activity_progress_bar);
        textView = findViewById(R.id.scoring_image_comparison_activity_text_view);
        relativeLayout = findViewById(R.id.scoring_image_comparison_result_layout);
        imageView = findViewById(R.id.scoring_image_comparison_result_image_view);
        tryAgainBtn = findViewById(R.id.scoring_image_comparison_activity_try_again_btn);
        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(SelfCamActivity.SELFI);
                finish();
            }
        });
        UiUtils.instance(true, this).buttonChanger(tryAgainBtn, true);

//        File file = new File(Environment.getExternalStorageDirectory() + "/pic.jpg");
//        int size = (int) file.length();
//        bytes = new byte[size];
//        try {
//            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
//            buf.read(bytes, 0, bytes.length);
//            buf.close();
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        uploadSelfieForComaprison();

    }

    private void uploadSelfieForComaprison() {
//        JSONObject object = new JSONObject();
//        String encoded = Base64.encodeToString(bytes, Base64.DEFAULT);
//        try {
//            object.put("image", encoded);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        JSONObject object = OCRHelper.getInstance().getSelfiePhoto();
        ConnectionsManager.getInstance(this).uploadScoringID(object, false, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                Log.d("WOW", "resolve: " + entity.toString());
                //startActivity(new Intent());
                setResult(true);
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.d("WOW", "resolve: " + entity.toString());
                setResult(false);
            }
        });
    }

    private void setResult(final boolean good) {

        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!good) {
                    relativeLayout.setBackground(getResources().getDrawable(R.drawable.red_circular_border));
                    imageView.setImageResource(R.drawable.close_red_36);
                    tryAgainBtn.setVisibility(View.VISIBLE);
                }
                textView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
                if (good){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(ScoringImageComparionActivity.this, ScoringPersonalActivity.class));
                            finish();
                        }
                    } , 3000);
                }

            }
        }, 1500);
    }
}
