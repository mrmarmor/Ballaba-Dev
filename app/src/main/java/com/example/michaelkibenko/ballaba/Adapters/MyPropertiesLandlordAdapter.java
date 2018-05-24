package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Activities.PropertyManagementActivity;
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

        private ImageView propertyIV;
        private TextView address , rooms , size , firstBullet , secondBullet;
        private LinearLayout textsContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            propertyIV = itemView.findViewById(R.id.my_properties_landlord_item_image_view);
            address = itemView.findViewById(R.id.my_properties_landlord_item_address_text);
            rooms = itemView.findViewById(R.id.my_properties_landlord_item_rooms_text);
            size = itemView.findViewById(R.id.my_properties_landlord_item_size_text);
            firstBullet = itemView.findViewById(R.id.my_properties_landlord_item_first_bullet);
            secondBullet = itemView.findViewById(R.id.my_properties_landlord_item_second_bullet);
            textsContainer = itemView.findViewById(R.id.my_properties_landlord_item_text_container);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_properties_landlord_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        final MyPropertiesLandlord myPropertiesLandlord = myPropertiesLandlords.get(position);

        Uri uri = Uri.parse(myPropertiesLandlord.getPhotos()[0].toString());
        Glide.with(context).load(uri).into(holder.propertyIV);

        holder.address.setText(myPropertiesLandlord.getAddress() + "");
        holder.rooms.setText(myPropertiesLandlord.getRooms() + " חדרים");
        holder.size.setText(myPropertiesLandlord.getSize() + " מ''ר");

        holder.firstBullet.setText("\u2022");
        holder.secondBullet.setText("\u2022");

        View.OnClickListener myClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PropertyManagementActivity.class);
                intent.putExtra("ID" , myPropertiesLandlord.getId());
                context.startActivity(intent);
            }
        };

        holder.textsContainer.setOnClickListener(myClick);
        holder.propertyIV.setOnClickListener(myClick);

    }

    @Override
    public int getItemCount() {
        return myPropertiesLandlords.size();
    }
}
