package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Entities.BallabaProperty;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Fragments.PropertiesRecyclerFragment;
import com.example.michaelkibenko.ballaba.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by User on 13/03/2018.
 */

public class PropertiesRecyclerAdapter extends RecyclerView.Adapter<PropertiesRecyclerAdapter.ViewHolder> {
    private final String TAG = PropertiesRecyclerAdapter.class.getSimpleName();

    private BallabaUser user;
    private List<BallabaProperty> properties = Collections.emptyList();
    private Context mContext;
    private RecyclerView mRecyclerView;
    private LinearLayout parent_layout;
    //private String phoneNumber, shareCardMessage;

    public PropertiesRecyclerAdapter(Context mContext, RecyclerView mRecyclerView, List<BallabaProperty> properties, BallabaUser user) {
        this.mContext = mContext;
        this.mRecyclerView = mRecyclerView;
        this.properties = properties;
        this.user = user;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.property_item, parent, false);

        //parent_layout = (LinearLayout)view.findViewById(R.id.single_Property_parent);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PropertiesRecyclerAdapter.ViewHolder holder, final int position) {
        if (user != null && properties.size() > 0) {
            //holder.tvMessage.setText(chatMessages.get(position).getText());
            Log.d(TAG, properties.size()+":"+position);
            BallabaProperty property = properties.get(position);

            holder.propertyImageView.setImageBitmap(property.bitmap());
            holder.textViewAddress.setText(property.address());
            holder.textViewPrice.setText(property.price());
            //parent_layout = (LinearLayout) findViewById(R.id.single_message_parent);

        }
    }

    @Override
    public int getItemCount() {return properties == null? 0 : properties.size();}

    public void updateList(List<BallabaProperty> newList) {
        properties = newList;
        notifyDataSetChanged();
    }

    public void setImageView(Bitmap bitmap){
        //holder.propertyImageView.setImageBitmap(bitmap);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewPrice, textViewAddress;
        private ImageView propertyImageView;

        ViewHolder(View itemView) {
            super(itemView);

            propertyImageView = itemView.findViewById(R.id.propertyItem_imageView);
            textViewAddress = itemView.findViewById(R.id.propertyItem_address_textView);
            textViewPrice = itemView.findViewById(R.id.propertyItem_price_textView);
        }
    }

}
