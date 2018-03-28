package com.example.michaelkibenko.ballaba.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.transition.Visibility;
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
import com.example.michaelkibenko.ballaba.Common.BallabaPropertyListener;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaProperty;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Managers.PropertiesManager;
import com.example.michaelkibenko.ballaba.Presenters.PropertyItemPresenter;
import com.example.michaelkibenko.ballaba.Presenters.TestingPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
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
    private BallabaSearchPropertiesManager propertiesManager;
    private LayoutInflater mInflater;
    private PropertyItemBinding binder;
    private BallabaPropertyListener listener;
    private Resources res;

    public PropertiesRecyclerAdapter(Context mContext, List<BallabaPropertyResult> properties) {
        this.mContext = mContext;
        this.properties = properties;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(mContext);
        binder = DataBindingUtil.inflate(mInflater, R.layout.property_item
                , parent, false);
        binder.setPresenter(new PropertyItemPresenter(mContext, binder));

        listener = PropertyItemPresenter.getInstance(mContext, binder);
        res = mContext.getResources();

        propertiesManager = BallabaSearchPropertiesManager.getInstance(mContext);
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
        if (property.photos.size() > 0)
            Glide.with(mContext)
                .load(property.photos.get(0))
                .apply(options)
                .into(binder.propertyItemImageView);

        Drawable d = property.isSaved? res.getDrawable(R.drawable.heart_blue_24, mContext.getTheme())
                :res.getDrawable(R.drawable.heart_white_24, mContext.getTheme());
        binder.propertyItemIsSavedPropertyImageView.setImageDrawable(d);

        @Visibility.Mode int visibility = property.isGuarantee? View.VISIBLE : View.GONE;
        binder.propertyItemGuaranteeImageView.setVisibility(visibility);

        binder.propertyItemAddressTextView.setText(property.formattedAddress);
        binder.propertyItemPriceTextView.setText(StringUtils.getInstance(true, null)
                .formattedNumberWithComma(property.price));
        binder.propertyItemRoomsTextView.setText(String.format("%s %s", property.roomsNumber, mContext.getString(R.string.propertyItem_numberOfRooms)));
        binder.propertyItemPropertySizeTextView.setText(String.format("%s %s", property.size, mContext.getString(R.string.propertyItem_propertySize)));

            //TODO without binder and glide:
            //holder.propertyImageView.setImageBitmap(property.bitmap());
            ////holder.textViewAddress.setText(property.address());
            ////holder.textViewPrice.setText(property.price());
            //parent_layout = (LinearLayout) findViewById(R.id.single_message_parent);

        //TODO get offset properties from server:
        //ConnectionsManager.getInstance(this).getConfigRequest
        if (position == (properties.size() - (/*OFFSET*/20 / 2))){
            lazyLoading(properties.size());
        }

    }

    @Override
    public int getItemCount() {return properties == null? 0 : properties.size();}

    public void updateList(ArrayList<BallabaPropertyResult> newList) {
        properties = newList;
        BallabaSearchPropertiesManager.getInstance(mContext).appendProperties(newList, false);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //private TextView textViewPrice, textViewAddress;
        //private ImageView propertyImageView;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            binder.propertyItemIsSavedPropertyImageView.setOnClickListener(this);
            binder.propertyItemGuaranteeImageView.setOnClickListener(this);

            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                setFontForDevicesUnderApi26();
            }

            //TODO without binder and glide:
            //propertyImageView = itemView.findViewById(R.id.propertyItem_imageView);
            //textViewAddress = itemView.findViewById(R.id.propertyItem_address_textView);
            //textViewPrice = itemView.findViewById(R.id.propertyItem_price_textView);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, getLayoutPosition()+":"+properties.get(getLayoutPosition()).isSaved+"");
            listener.BallabaPropertyOnClick(v, getLayoutPosition());


        }
    }

    private void lazyLoading(int offset){
        BallabaSearchPropertiesManager.getInstance(mContext).getPropertiesByLatLng(null, offset
                , BallabaSearchPropertiesManager.LAZY_LOADING_OFFSET_STATES.AFTER_20, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                ArrayList<BallabaPropertyResult> properties =
                        propertiesManager.parsePropertyResults(((BallabaOkResponse)entity).body);
                propertiesManager.appendProperties(properties, true);

                updateList(propertiesManager.getResults());
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.e(TAG, "error fetching offset properties");
            }
        });
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
