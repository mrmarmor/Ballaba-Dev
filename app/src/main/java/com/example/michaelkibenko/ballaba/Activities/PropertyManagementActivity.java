package com.example.michaelkibenko.ballaba.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.example.michaelkibenko.ballaba.Adapters.PropertyManagementAdapter;
import com.example.michaelkibenko.ballaba.Fragments.PropertyManagement.property_managment_adapters.PropertyManageInterestedAdapter;
import com.example.michaelkibenko.ballaba.Fragments.PropertyManagement.property_managment_adapters.PropertyManageMeetingAdapter;
import com.example.michaelkibenko.ballaba.R;

import java.util.ArrayList;

public class PropertyManagementActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = PropertyManagementActivity.class.getSimpleName();

    private RtlViewPager propertyManagementViewPager;
    private TabLayout tabLayout;
    private CheckBox selectAllCheckBox;
    private ImageButton editIB, confirmBtn, deleteBtn;
    private PropertyManagementAdapter adapter;

    private String propertyAddress;
    private boolean moreThanOneChecked, isAllUnChecked, isInterestedTab;
    private int propertyID , tabPosition;

    private PropertyManageInterestedAdapter interestedAdapter;
    private PropertyManageMeetingAdapter meetingsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_management);

        // get Property Address - English... convert to hebrew is needed
        //propertyAddress = getIntent().getStringExtra("ADDRESS");
        propertyID = getIntent().getIntExtra("ID", -1);
        findViewsAndListeners();

        adapter = new PropertyManagementAdapter(this, propertyManagementViewPager, getSupportFragmentManager(), tabLayout);
        propertyManagementViewPager.setAdapter(adapter);

        setOnClickListeners();

        selectAllCheckBox.setOnClickListener(this);
    }

    private void setOnClickListeners() {

        findViewById(R.id.propertyManagement_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PropertyManagementActivity.this, "Edit Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userConfirmID = adapter.getPropertyManageInterestedFragment().getSelectedUserID();
                Toast.makeText(PropertyManagementActivity.this, "CONGRATULATION \nYou've made a CONTRACT with \n" + userConfirmID, Toast.LENGTH_SHORT).show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });

    }

    private void checkForCheckedStatus() {
        interestedAdapter = adapter.getPropertyManageInterestedFragment().getAdapter();
        meetingsAdapter = adapter.getPropertyManageMeetingsFragment().getAdapter();
        if (interestedAdapter != null) {
            moreThanOneChecked = interestedAdapter.isMoreThanOneChecked();
        }
        if (meetingsAdapter != null) {
            moreThanOneChecked = meetingsAdapter.isMoreThanOneChecked();
        }
        isAllUnChecked = !interestedAdapter.isAllUnChecked();
    }

    private void showDeleteDialog() {
        checkForCheckedStatus();
        AlertDialog.Builder builder = new AlertDialog.Builder(PropertyManagementActivity.this);
        builder.setTitle("הסרה מהרשימה");
        builder.setMessage("האם ברצונך להסיר את המעוניינים");
        builder.setPositiveButton("הסרה", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<Integer> userDeletedIds = adapter.getPropertyManageInterestedFragment().getSelectedUsersID();
                adapter.getPropertyManageInterestedFragment().getAdapter().deleteSelectedItems(userDeletedIds);

                toolbarImagesVisibility(false, true, !isAllUnChecked && !moreThanOneChecked, !moreThanOneChecked);
            }
        });
        builder.setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });


        dialog.show();
    }

    private void findViewsAndListeners() {
        tabLayout = findViewById(R.id.propertyManagement_tabs_root);
        selectAllCheckBox = findViewById(R.id.propertyManagement_toolbar_checkbox);
        editIB = findViewById(R.id.propertyManagement_edit_btn);
        confirmBtn = findViewById(R.id.propertyManagement_confirm_btn);
        deleteBtn = findViewById(R.id.propertyManagement_delete_or_cancel_btn);
        propertyManagementViewPager = findViewById(R.id.propertyManagement_viewPager);
    }

    public void changeToolbarText(@Nullable String txt) {
        ((TextView) findViewById(R.id.propertyManagement_title_text_view)).setText(txt != null ? txt : "הנכסים שלי");
    }

    public void onStateChange(int position) {
        isInterestedTab = position == 2;
        tabPosition = position;
        switch (position) {
            case 0:
                toolbarImagesVisibility(false, false, false, false);
                changeToolbarText(null);
                break;
            case 1:
                toolbarImagesVisibility(true, false, false, false);
                changeToolbarText(propertyAddress);
                break;
            case 2:
                checkForCheckedStatus();
                toolbarImagesVisibility(false, true, false, false);
                changeToolbarText(null);
                //isChecked(isAllUnChecked);
                break;
            case 3:
                checkForCheckedStatus();
                toolbarImagesVisibility(false, true, false, false);
                //showCheckboxInToolbar(true);
                changeToolbarText(null);

                //isChecked(false);
                selectAllCheckBox.setChecked(false);
                break;
        }
    }

    public void toolbarImagesVisibility(boolean showEdit, boolean showCheckBox, boolean showConfirm, boolean showDelete ) {
        boolean checkToolbarCheckBox = false;
        if (tabPosition == 2){
            isAllUnChecked = interestedAdapter.isAllUnChecked();
            if (!isAllUnChecked){
                showConfirm = interestedAdapter.checkHowMuchSelected() <= 1;
                showDelete = interestedAdapter.checkHowMuchSelected() >= 1;
                checkToolbarCheckBox = interestedAdapter.isAllChecked();
            }
        }else if (tabPosition  == 3){
            isAllUnChecked = meetingsAdapter.isAllUnChecked();
            if (!isAllUnChecked){
                showConfirm = meetingsAdapter.checkHowMuchSelected() <= 1;
                showDelete = meetingsAdapter.checkHowMuchSelected() >= 1;
                checkToolbarCheckBox = meetingsAdapter.isAllChecked();
            }
        }
        editIB.setVisibility(showEdit ? View.VISIBLE : View.GONE);
        selectAllCheckBox.setVisibility(showCheckBox ? View.VISIBLE : View.GONE);
        confirmBtn.setVisibility(showConfirm ? View.VISIBLE : View.GONE);
        deleteBtn.setVisibility(showDelete ? View.VISIBLE : View.GONE);
        deleteBtn.setImageDrawable(getDrawable(isInterestedTab ? R.drawable.delete_white_24 : R.drawable.close_white_24));
        selectAllCheckBox.setChecked(checkToolbarCheckBox);
    }

    @Override
    public void onClick(View v) {
        boolean isCheck = ((CheckBox) v).isChecked();
        moreThanOneChecked = interestedAdapter.isMoreThanOneChecked();
        isAllUnChecked = interestedAdapter.isAllUnChecked();
        adapter.checkAllInterested(isCheck, isInterestedTab);
        toolbarImagesVisibility(false , true , isCheck , isCheck);
    }

    public int getPropertyID() {
        Log.d(TAG, "property id: " + propertyID);
        return propertyID;
    }
}