package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Entities.BallabaProperty;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
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
    private View firstRootView, anotherRootViews;
    private LayoutInflater mInflater;
    private ViewGroup parent;
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
        this.parent = parent;
        mInflater = LayoutInflater.from(mContext);
        firstRootView = mInflater.inflate(R.layout.property_item_single_in_a_row, parent, false);
        //parent_layout = (LinearLayout)view.findViewById(R.id.single_Property_parent);

        return new ViewHolder(firstRootView);
    }

    @Override
    public void onBindViewHolder(final PropertiesRecyclerAdapter.ViewHolder holder, final int position) {
        if (user != null && properties.size() > 0) {
            //holder.tvMessage.setText(chatMessages.get(position).getText());
            Log.d(TAG, properties.size()+":"+position);
            if (position > 1)
                anotherRootViews = mInflater.inflate(R.layout.property_item_duplicated, parent, false);

            BallabaProperty property = properties.get(position);

            Glide.with(mContext).load(property.bitmap()).into(holder.propertyImageView);
            //holder.propertyImageView.setImageBitmap(property.bitmap());
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
