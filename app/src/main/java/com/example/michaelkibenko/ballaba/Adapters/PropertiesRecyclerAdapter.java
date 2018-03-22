package com.example.michaelkibenko.ballaba.Adapters;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
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
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Presenters.PropertyItemPresenter;
import com.example.michaelkibenko.ballaba.Presenters.TestingPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.PropertyItemBinding;

import java.text.Format;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by User on 13/03/2018.
 */

public class PropertiesRecyclerAdapter extends RecyclerView.Adapter<PropertiesRecyclerAdapter.ViewHolder> {
    private final String TAG = PropertiesRecyclerAdapter.class.getSimpleName();

    private List<BallabaPropertyResult> properties;
    final private Context mContext;
    private LayoutInflater mInflater;
    private PropertyItemBinding binder;

    public PropertiesRecyclerAdapter(Context mContext, List<BallabaPropertyResult> properties) {
        this.mContext = mContext;
        this.properties = properties;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(mContext);
        binder = DataBindingUtil.inflate(mInflater, R.layout.property_item
                , parent, false);
        binder.setPresenter(new PropertyItemPresenter(mContext));

        ////mInflater = LayoutInflater.from(mContext);
        ////firstRootView = mInflater.inflate(R.layout.property_item_single_in_a_row, parent, false);
        //parent_layout = (LinearLayout)view.findViewById(R.id.single_Property_parent);

        return new ViewHolder(binder.getRoot());//firstRootView);
    }

    @Override
    public void onBindViewHolder(final PropertiesRecyclerAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, properties.size()+":"+position);
        BallabaPropertyResult property = properties.get(position);

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(mContext)//TODO next line is only for testing!!!
                .load(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.dummy_property))//property.photos.get(0))
                .apply(options)
                .into(binder.propertyItemImageView);
        binder.propertyItemAddressTextView.setText(property.formattedAddress);
        binder.propertyItemPriceTextView.setText(property.price);
        binder.propertyItemRoomsTextView.setText(String.format("%s %s"
                , property.roomsNumber, mContext.getString(R.string.propertyItem_numberOfRooms)));
        binder.propertyItemPropertySizeTextView.setText(String.format("%s %s"
                , property.size, mContext.getString(R.string.propertyItem_propertySize)));

            //TODO without binder and glide:
            //holder.propertyImageView.setImageBitmap(property.bitmap());
            ////holder.textViewAddress.setText(property.address());
            ////holder.textViewPrice.setText(property.price());
            //parent_layout = (LinearLayout) findViewById(R.id.single_message_parent);
    }

    @Override
    public int getItemCount() {return properties == null? 0 : properties.size();}

    public void updateList(List<BallabaPropertyResult> newList) {
        properties = newList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //private TextView textViewPrice, textViewAddress;
        //private ImageView propertyImageView;

        ViewHolder(View itemView) {
            super(itemView);

            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                setFontForDevicesUnderApi26();
            }

            //TODO without binder and glide:
            //propertyImageView = itemView.findViewById(R.id.propertyItem_imageView);
            //textViewAddress = itemView.findViewById(R.id.propertyItem_address_textView);
            //textViewPrice = itemView.findViewById(R.id.propertyItem_price_textView);
        }
    }

    private void setFontForDevicesUnderApi26(){
        final Typeface typefaceRegular = ResourcesCompat.getFont(mContext, R.font.rubik_regular);
        final Typeface typefaceBold = ResourcesCompat.getFont(mContext, R.font.rubik_bold);

        binder.propertyItemRoomsTextView.setTypeface(typefaceRegular);
        binder.propertyItemPropertySizeTextView.setTypeface(typefaceRegular);
        binder.propertyItemAddressTextView.setTypeface(typefaceRegular);
        binder.propertyItemPriceMonthlyTextView.setTypeface(typefaceRegular);
        binder.propertyItemPriceTextView.setTypeface(typefaceBold);
        binder.propertyItemPriceCurrencyTextView.setTypeface(typefaceBold);
    }

}
