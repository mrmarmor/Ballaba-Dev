package com.example.michaelkibenko.ballaba.Adapters;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.michaelkibenko.ballaba.Entities.BallabaProperty;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Presenters.PropertyItemPresenter;
import com.example.michaelkibenko.ballaba.Presenters.TestingPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.PropertyItemBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by User on 13/03/2018.
 */

public class PropertiesRecyclerAdapter extends RecyclerView.Adapter<PropertiesRecyclerAdapter.ViewHolder> {
    private final String TAG = PropertiesRecyclerAdapter.class.getSimpleName();

    private BallabaUser user;
    private List<BallabaProperty> properties = Collections.emptyList();
    final private Context mContext;
    private View firstRootView, anotherRootViews;
    private LayoutInflater mInflater;
    private ViewGroup parent;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager recyclerViewLayoutManager;
    private LinearLayout parent_layout;
    private PropertyItemBinding bind;

    public PropertiesRecyclerAdapter(Context mContext, RecyclerView mRecyclerView
            , LinearLayoutManager manager, List<BallabaProperty> properties, BallabaUser user) {
        this.mContext = mContext;
        this.mRecyclerView = mRecyclerView;
        this.recyclerViewLayoutManager = manager;
        this.properties = properties;
        this.user = user;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(mContext);
        bind = DataBindingUtil.inflate(mInflater, R.layout.property_item
                , parent, false);
        bind.setPresenter(new PropertyItemPresenter(mContext));

        this.parent = parent;

        ////mInflater = LayoutInflater.from(mContext);
        ////firstRootView = mInflater.inflate(R.layout.property_item_single_in_a_row, parent, false);
        //parent_layout = (LinearLayout)view.findViewById(R.id.single_Property_parent);

        return new ViewHolder(bind.getRoot());//firstRootView);
    }

    @Override
    public void onBindViewHolder(final PropertiesRecyclerAdapter.ViewHolder holder, final int position) {
        if (user != null && properties.size() > 0 && position < properties.size()) {
            Log.d(TAG, properties.size()+":"+position);
            BallabaProperty property = properties.get(position);

            RequestOptions options = new RequestOptions();
            options.centerCrop();
            Glide.with(mContext)
                    .load(property.bitmap())
                    .apply(options)
                    .into(bind.propertyItemImageView);//holder.propertyImageView);
            bind.propertyItemAddressTextView.setText(property.address());
            bind.propertyItemPriceTextView.setText(property.price());

            setFontForDevicesUnderApi26();

            //anotherRootViews = mInflater.inflate(R.layout.property_item_duplicated, parent, false);

            //TODO without binder and glide:
            //holder.propertyImageView.setImageBitmap(property.bitmap());
            ////holder.textViewAddress.setText(property.address());
            ////holder.textViewPrice.setText(property.price());
            //parent_layout = (LinearLayout) findViewById(R.id.single_message_parent);

        }
    }

    @Override
    public int getItemCount() {return properties == null? 0 : properties.size();}

    public void updateList(List<BallabaProperty> newList) {
        properties = newList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //private TextView textViewPrice, textViewAddress;
        //private ImageView propertyImageView;

        ViewHolder(View itemView) {
            super(itemView);

            //TODO without binder and glide:
            //propertyImageView = itemView.findViewById(R.id.propertyItem_imageView);
            //textViewAddress = itemView.findViewById(R.id.propertyItem_address_textView);
            //textViewPrice = itemView.findViewById(R.id.propertyItem_price_textView);
        }
    }

    private void setFontForDevicesUnderApi26(){
        final Typeface typefaceRegular = ResourcesCompat.getFont(mContext, R.font.rubik_regular);
        final Typeface typefaceBold = ResourcesCompat.getFont(mContext, R.font.rubik_bold);

        bind.propertyItemRoomsTextView.setTypeface(typefaceRegular);
        bind.propertyItemPropertySizeTextView.setTypeface(typefaceRegular);
        bind.propertyItemAddressTextView.setTypeface(typefaceRegular);
        bind.propertyItemPriceMonthlyTextView.setTypeface(typefaceRegular);
        bind.propertyItemPriceTextView.setTypeface(typefaceBold);
        bind.propertyItemPriceCurrencyTextView.setTypeface(typefaceBold);
    }

}
