package com.example.michaelkibenko.ballaba.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.michaelkibenko.ballaba.Adapters.PropertiesRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.SavedPropertiesActivityBinding;

import java.util.ArrayList;

import static com.example.michaelkibenko.ballaba.Activities.SavedPropertiesActivity.SAVE_PROPERTIES_STATES.EMPTY_STATE;
import static com.example.michaelkibenko.ballaba.Activities.SavedPropertiesActivity.SAVE_PROPERTIES_STATES.ITEMS;
import static com.example.michaelkibenko.ballaba.Activities.SavedPropertiesActivity.SAVE_PROPERTIES_STATES.LOADING;

public class SavedPropertiesActivity extends BaseActivity {

    @IntDef({LOADING, EMPTY_STATE, ITEMS})
    @interface SAVE_PROPERTIES_STATES {int LOADING = 1; int EMPTY_STATE = 2; int ITEMS = 3;}
    private SavedPropertiesActivityBinding binding;
    @SAVE_PROPERTIES_STATES int currentState;
    private ArrayList<BallabaPropertyResult> items;
    private PropertiesRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.saved_properties_activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.savedPropertiesRV.setLayoutManager(linearLayoutManager);
        fetchSavedProperties();
        binding.goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.emptyStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void onScreenStateChanger(@SAVE_PROPERTIES_STATES int state){
        if(currentState != state){
            this.currentState = state;
            if(currentState == LOADING){
                binding.savedPropertiesRV.setVisibility(View.GONE);
                binding.emptyStateLayout.setVisibility(View.GONE);
                binding.savedPropertiesProgressBar.setVisibility(View.VISIBLE);
            }
            else if(currentState == EMPTY_STATE){
                binding.savedPropertiesRV.setVisibility(View.GONE);
                binding.emptyStateLayout.setVisibility(View.VISIBLE);
                binding.savedPropertiesProgressBar.setVisibility(View.GONE);
            }else if(items != null){
                binding.savedPropertiesRV.setVisibility(View.VISIBLE);
                binding.emptyStateLayout.setVisibility(View.GONE);
                binding.savedPropertiesProgressBar.setVisibility(View.GONE);
            }
        }
    }

    private void fetchSavedProperties(){
        onScreenStateChanger(LOADING);
        ConnectionsManager.getInstance(this).getSavedProperties(new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if(entity instanceof BallabaOkResponse){
                    items = BallabaSearchPropertiesManager.getInstance(SavedPropertiesActivity.this).parsePropertyResults(((BallabaOkResponse)entity).body);
                    if(items!= null && items.size() != 0) {
                        adapter = new PropertiesRecyclerAdapter(SavedPropertiesActivity.this, getSupportFragmentManager(), items, true);
                        binding.savedPropertiesRV.setAdapter(adapter);
                        onScreenStateChanger(ITEMS);
                    }else{
                        onScreenStateChanger(EMPTY_STATE);
                    }
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                onScreenStateChanger(EMPTY_STATE);
            }
        });
    }
}
