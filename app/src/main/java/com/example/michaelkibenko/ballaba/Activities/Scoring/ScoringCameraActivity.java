package com.example.michaelkibenko.ballaba.Activities.Scoring;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Fragments.CameraFragment;
import com.example.michaelkibenko.ballaba.R;

public class ScoringCameraActivity extends BaseActivity {


    private CameraFragment cameraFragment = new CameraFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoring_camera_layout);

        //getFragmentManager().beginTransaction().replace(R.id.scoring_camera_frame_layout , cameraFragment).commit();
        /*findViewById(R.id.activity_scoring_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CameraFragment.REQUEST_CAMERA_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_DENIED){
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
