package com.example.michaelkibenko.ballaba.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.R;

public class TenantMyPorpertyDetailsActivity extends BaseActivity{

    private TextView priceTV , addressTV , entryDateTV , exitDateTV;
    private LinearLayout myContractLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_property_description_activity);

        priceTV = findViewById(R.id.my_properties_tenant_price);
        addressTV = findViewById(R.id.my_properties_tenant_address);
        entryDateTV = findViewById(R.id.my_properties_tenant_entry_date_static);
        exitDateTV = findViewById(R.id.my_properties_tenant_exit_date_static);

        myContractLayout = findViewById(R.id.my_properties_tenant_contract_container);



    }
}
