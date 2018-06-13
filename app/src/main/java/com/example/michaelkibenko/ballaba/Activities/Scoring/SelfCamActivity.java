package com.example.michaelkibenko.ballaba.Activities.Scoring;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Fragments.CameraFragment;
import com.example.michaelkibenko.ballaba.R;

public class SelfCamActivity extends BaseActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 347;
    public static final int SELFI = 10;
    private CameraFragment cameraFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selfi_cam_layout);
        cameraFragment = new CameraFragment();
        cameraFragment.setCurrentType(CameraFragment.CAMERA_TYPES.FRONT);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        }else {
            openCameraFragment();
        }
    }

    private void openCameraFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.camera_fragment_container, cameraFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                MY_PERMISSIONS_REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == SELFI) {
                cameraFragment = new CameraFragment();
                cameraFragment.setCurrentType(CameraFragment.CAMERA_TYPES.FRONT);
                openCameraFragment();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_CAMERA){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraFragment();
            } else {
                //TODO set not granted state
            }
            return;
        }
    }
}
