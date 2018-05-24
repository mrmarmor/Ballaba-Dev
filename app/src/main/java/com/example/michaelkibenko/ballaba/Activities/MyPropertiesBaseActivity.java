package com.example.michaelkibenko.ballaba.Activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.example.michaelkibenko.ballaba.Fragments.MyPropertiesLandlordFragment;
import com.example.michaelkibenko.ballaba.Fragments.MyPropertiesTenantFragment;
import com.example.michaelkibenko.ballaba.R;

public class MyPropertiesBaseActivity extends BaseActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener {


    private TabLayout tabLayout;
    private Fragment[] fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_properties_base_activity);

        fragments = new Fragment[]{new MyPropertiesLandlordFragment() , new MyPropertiesTenantFragment()};

        findViewAndListeners();
        onTabSelected(tabLayout.getTabAt(0));

    }

    private void findViewAndListeners() {
        findViewById(R.id.my_properties_main_back_btn).setOnClickListener(this);
        tabLayout = findViewById(R.id.my_properties_tab_layout);
        tabLayout.addOnTabSelectedListener(this);
    }


    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        getFragmentManager().beginTransaction().replace(R.id.my_properties_frame_layout , fragments[position]).commit();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
