package com.example.michaelkibenko.ballaba.Activities.Scoring;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        openCameraFragment();
        /*findViewById(R.id.activity_scoring_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });*/
    }

    private void openCameraFragment(){
        cameraFragment = new CameraFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.scoring_camera_frame_layout, cameraFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CameraFragment.REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "you cant use camera without permission", Toast.LENGTH_SHORT).show();
                finish();
            }else if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCameraFragment();
            }
        }
    }

}
