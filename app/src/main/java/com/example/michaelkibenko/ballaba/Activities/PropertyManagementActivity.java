package com.example.michaelkibenko.ballaba.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.example.michaelkibenko.ballaba.Adapters.PropertyManagementAdapter;
import com.example.michaelkibenko.ballaba.Fragments.PropertyManagement.property_managment_adapters.PropertyManageInterestedAdapter;
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
    private boolean moreThanOneChecked, isSomeOneChecked;
    private int propertyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_management);

        // get Property Address - English... convert to hebrew is needed
        //propertyAddress = getIntent().getStringExtra("ADDRESS");
        propertyID = getIntent().getIntExtra("ID" , -1);
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

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PropertyManagementActivity.this);
        builder.setTitle("הסרה מהרשימה");
        builder.setMessage("האם ברצונך להסיר את המעוניינים");
        builder.setPositiveButton("הסרה", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<Integer> userDeletedIds = adapter.getPropertyManageInterestedFragment().getSelectedUsersID();
                adapter.getPropertyManageInterestedFragment().getAdapter().deleteSelectedItems(userDeletedIds);
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
        switch (position) {
            case 0:
                showCheckboxInToolbar(false);
                showEditToolbar(false);
                isChecked(false, false);
                changeToolbarText(null);
                break;
            case 1:
                showCheckboxInToolbar(false);
                showEditToolbar(true);
                isChecked(false, false);
                changeToolbarText(propertyAddress);
                break;
            case 2:
                showCheckboxInToolbar(true);
                showEditToolbar(false);
                changeToolbarText(null);
                isChecked(isSomeOneChecked, moreThanOneChecked);
                break;
            case 3:
                showCheckboxInToolbar(true);
                showEditToolbar(false);
                changeToolbarText(null);
                isChecked(false, false);
                selectAllCheckBox.setChecked(false);
                break;
        }
    }

    public void showCheckboxInToolbar(boolean show) {
        selectAllCheckBox.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showEditToolbar(boolean show) {
        editIB.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void isChecked(boolean checked, boolean moreThanOneChecked) {
        confirmBtn.setVisibility(checked && !moreThanOneChecked ? View.VISIBLE : View.GONE);
        deleteBtn.setVisibility(checked ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        boolean isCheck = ((CheckBox) v).isChecked();
        boolean isInterestedTab = tabLayout.getSelectedTabPosition() == 2;
        PropertyManageInterestedAdapter interestedAdapter = adapter.getPropertyManageInterestedFragment().getAdapter();
        moreThanOneChecked = interestedAdapter.isMoreThanOneChecked();
        isSomeOneChecked = !interestedAdapter.isAllUnChecked();
        isChecked(isCheck, !moreThanOneChecked);
        deleteBtn.setImageDrawable(getDrawable(isInterestedTab ? R.drawable.delete_white_24 : R.drawable.close_white_24));
        adapter.checkAllInterested(isCheck, isInterestedTab);
    }

    public int getPropertyID() {
        return propertyID;
    }
}