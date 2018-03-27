package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelkibenko on 27/03/2018.
 */

public class MapPropetiesreciclerAdapter extends RecyclerView.Adapter {

    private final Context context;
    private ArrayList<BallabaPropertyResult> items;
    private LayoutInflater mInflater;

    public MapPropetiesreciclerAdapter(Context context){
        this.context = context;
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(context);
        View v = mInflater.inflate(R.layout.map_property_reciclerview_item, parent, false);
        return new MapPropertyItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        
    }

    private class MapPropertyItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView photo;
        public ImageButton saved_icon;
        public TextView roomsNumber;
        public TextView metres;
        public TextView formattedAddress;
        public MapPropertyItemViewHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.map_property_image);
            saved_icon = (ImageButton) itemView.findViewById(R.id.map_property_saved_button);
            roomsNumber = (TextView) itemView.findViewById(R.id.map_property_roomsNum);
            metres = (TextView) itemView.findViewById(R.id.map_property_metresNum);
            formattedAddress = (TextView) itemView.findViewById(R.id.map_property_formattedAddress);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
