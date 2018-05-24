package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michaelkibenko.ballaba.Entities.MyPropertiesLandlord;
import com.example.michaelkibenko.ballaba.R;

import java.util.List;

public class MyPropertiesLandlordAdapter extends RecyclerView.Adapter<MyPropertiesLandlordAdapter.ViewHolder>{

    private Context context;
    private List<MyPropertiesLandlord> myPropertiesLandlords;


    public MyPropertiesLandlordAdapter(Context context , List<MyPropertiesLandlord> myPropertiesLandlords) {
        this.context = context;
        this.myPropertiesLandlords = myPropertiesLandlords;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_properties_landlord_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return myPropertiesLandlords.size();
    }
}
