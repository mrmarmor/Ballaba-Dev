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
import com.bumptech.glide.request.RequestOptions;
import com.example.michaelkibenko.ballaba.Activities.TenantMyPorpertyDetailsActivity;
import com.example.michaelkibenko.ballaba.Entities.MyPropertiesLandlord;
import com.example.michaelkibenko.ballaba.R;

import java.util.List;

public class MyPropertiesTenantAdapter extends RecyclerView.Adapter<MyPropertiesTenantAdapter.ViewHolder>{

    private Context context;
    private List<MyPropertiesLandlord> myPropertiesLandlords;


    public MyPropertiesTenantAdapter(Context context , List<MyPropertiesLandlord> myPropertiesLandlords) {
        this.context = context;
        this.myPropertiesLandlords = myPropertiesLandlords;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView propertyIV;
        private TextView address , rooms , size , firstBullet , secondBullet;
        private LinearLayout textsContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            propertyIV = itemView.findViewById(R.id.my_properties_tenant_item_image_view);
            address = itemView.findViewById(R.id.my_properties_tenant_item_address_text);
            rooms = itemView.findViewById(R.id.my_properties_tenant_item_rooms_text);
            size = itemView.findViewById(R.id.my_properties_tenant_item_size_text);
            firstBullet = itemView.findViewById(R.id.my_properties_tenant_item_first_bullet);
            secondBullet = itemView.findViewById(R.id.my_properties_tenant_item_second_bullet);
            textsContainer = itemView.findViewById(R.id.my_properties_tenant_item_text_container);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_properties_tenant_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        final MyPropertiesLandlord myPropertiesLandlord = myPropertiesLandlords.get(position);

        String imageUrl = myPropertiesLandlord.getPhotos()[0];

        if (imageUrl != null){
            Uri uri = Uri.parse(imageUrl);
            Glide.with(context)
                 .load(uri)
                 .apply(new RequestOptions().placeholder(R.drawable.photo_home_grey_24).fitCenter())
                 .into(holder.propertyIV);
        }

        holder.address.setText(myPropertiesLandlord.getAddress() + "");
        holder.rooms.setText(myPropertiesLandlord.getRooms() + " חדרים");
        holder.size.setText(myPropertiesLandlord.getSize() + " מ''ר");

        holder.firstBullet.setText("\u2022");
        holder.secondBullet.setText("\u2022");

        View.OnClickListener myClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TenantMyPorpertyDetailsActivity.class);
                intent.putExtra("ID" , myPropertiesLandlord.getId());
                intent.putExtra("ADDRESS" , myPropertiesLandlord.getAddress());
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
