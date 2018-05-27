package com.example.michaelkibenko.ballaba.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.example.michaelkibenko.ballaba.Adapters.PropertyManagementAdapter;
import com.example.michaelkibenko.ballaba.R;

public class PropertyManagementActivity extends BaseActivity {
    private final static String TAG = PropertyManagementActivity.class.getSimpleName();

    private RtlViewPager propertyManagementViewPager;
    private TabLayout tabLayout;
    private CheckBox toolbarCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* binder = DataBindingUtil.setContentView(this, R.layout.activity_property_management);
        binder.setPresenter(new PropertyManagementPresenter(this, binder));*/
        setContentView(R.layout.activity_property_management);

        tabLayout = findViewById(R.id.propertyManagement_tabs_root);
        toolbarCheckbox = findViewById(R.id.propertyManagement_toolbar_checkbox);

        RtlViewPager propertyManagementViewPager = findViewById(R.id.propertyManagement_viewPager);

        PropertyManagementAdapter adapter = new PropertyManagementAdapter(this , propertyManagementViewPager , getSupportFragmentManager() , tabLayout);
        propertyManagementViewPager.setAdapter(adapter);


        findViewById(R.id.propertyManagement_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //getSupportActionBar().setElevation(0);
        //ViewCompat.setElevation(binder.propertyManagementTabsRoot, 8);
    }


    public void changeToolbarText(String txt){
        ((TextView)findViewById(R.id.propertyManagement_title_text_view)).setText(txt);
    }


    public void showInterestedToolbar(boolean show) {
        toolbarCheckbox.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}