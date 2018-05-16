package com.example.michaelkibenko.ballaba.Activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;

/**
 * Created by michaelkibenko on 21/02/2018.
 */

public class EditViewportSubActivity extends BaseActivity {
    private static final String TAG = EditViewportSubActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_viewport);

    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(RESULT_OK);
    }*/
}