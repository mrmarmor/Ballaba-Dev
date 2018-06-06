package com.example.michaelkibenko.ballaba.Activities.Scoring;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropLandlordFrag;
import com.example.michaelkibenko.ballaba.R;

public class ScoringPersonalDetailsActivity extends BaseActivity implements View.OnClickListener {


    private AddPropLandlordFrag addPropLandlordFrag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoring_personal_details_activity);

        findViewById(R.id.scoring_personal_details_toolbar_back_image_view).setOnClickListener(this);

        addPropLandlordFrag = new AddPropLandlordFrag();
        Bundle bundle = new Bundle();
        bundle.putBoolean("IS_TENANT" , true);
        addPropLandlordFrag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.scoring_personal_details_frame_layout , addPropLandlordFrag).commit();
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
