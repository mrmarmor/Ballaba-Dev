package com.example.michaelkibenko.ballaba.Activities.Scoring;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Fragments.CameraFragment;
import com.example.michaelkibenko.ballaba.R;

public class ScoringCameraActivity extends AppCompatActivity {


    private CameraFragment cameraFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        setContentView(R.layout.scoring_camera_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        cameraFragment = new CameraFragment();
        getFragmentManager().beginTransaction().replace(R.id.scoring_camera_frame_layout, cameraFragment).commit();
        /*findViewById(R.id.activity_scoring_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CameraFragment.REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "you cant use camera without permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        cameraFragment.startBackgroundThread();
        if (textureView.isAvailable()){
            openCamera();
        }else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopBackgourndThread();
    }*/

}