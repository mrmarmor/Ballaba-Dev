package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Activities.PropertyDescriptionActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.R;
import java.util.ArrayList;

/**
 * Created by michaelkibenko on 27/03/2018.
 */

public class MapPropertiesRecyclerAdapter extends RecyclerView.Adapter<MapPropertiesRecyclerAdapter.MapPropertyItemViewHolder> {

    private final Context context;
    private ArrayList<BallabaPropertyResult> items;
    private LayoutInflater mInflater;

    public MapPropertiesRecyclerAdapter(Context context){
        this.context = context;
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public MapPropertyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(context);
        View v = mInflater.inflate(R.layout.map_property_reciclerview_item, parent, false);
        return new MapPropertyItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MapPropertyItemViewHolder holder, int position) {
        final BallabaPropertyResult propertyResult = items.get(position);
        try {
            Glide.with(context).load(propertyResult.photos.get(0)).into(holder.photo);
        }catch(IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
        if(propertyResult.isSaved){
            holder.saved_icon.setBackground(context.getResources().getDrawable(R.drawable.heart_blue_24,context.getTheme()));
        }else{
            holder.saved_icon.setBackground(context.getResources().getDrawable(R.drawable.heart_white_24,context.getTheme()));
        }
        holder.roomsNumber.setText(propertyResult.roomsNumber);
        holder.metres.setText(propertyResult.size);
        holder.formattedAddress.setText(propertyResult.formattedAddress);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PropertyDescriptionActivity.class);
                intent.putExtra(PropertyDescriptionActivity.PROPERTY_ID, propertyResult.id);
                context.startActivity(intent);
            }
        });
    }

    public class MapPropertyItemViewHolder extends RecyclerView.ViewHolder{
        public ConstraintLayout parent;
        public ImageView photo;
        public ImageButton saved_icon;
        public TextView roomsNumber;
        public TextView metres;
        public TextView formattedAddress;
        public MapPropertyItemViewHolder(View itemView) {
            super(itemView);
            parent = (ConstraintLayout) itemView;
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

    public void updateItems(ArrayList<BallabaPropertyResult> arr){
        items.clear();
        items.addAll(arr);
    }
}
