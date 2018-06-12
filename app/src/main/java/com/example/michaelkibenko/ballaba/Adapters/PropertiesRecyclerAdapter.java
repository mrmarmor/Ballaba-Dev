package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.transition.Visibility;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Activities.PropertyDescriptionActivity;
import com.example.michaelkibenko.ballaba.Activities.Scoring.ScoringWelcomeActivity;
import com.example.michaelkibenko.ballaba.Common.BallabaPropertyListener;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Fragments.PropertyImageFragment;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Presenters.PropertyDescriptionPresenter;
import com.example.michaelkibenko.ballaba.Presenters.PropertyItemPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.example.michaelkibenko.ballaba.Views.CustomViewPager;
import com.example.michaelkibenko.ballaba.databinding.PropertyItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 13/03/2018.
 */

public class PropertiesRecyclerAdapter extends RecyclerView.Adapter<PropertiesRecyclerAdapter.ViewHolder>{
    private final String TAG = PropertiesRecyclerAdapter.class.getSimpleName();
    public final static int REQ_CODE_SHOW_FULL_PROPERTY = 1;

    private List<BallabaPropertyResult> properties;
    final private Context mContext;
    private BallabaSearchPropertiesManager propertiesManager;
    private LayoutInflater mInflater;
    private BallabaPropertyListener listener;
    private Resources res;
    private FragmentManager fragmentManager;
    private int firstArraySize;
    private final boolean isSavedScreen;
    private int spaceBetweenIndicator;

    public PropertiesRecyclerAdapter(Context mContext, FragmentManager fm, List<BallabaPropertyResult> properties, boolean isSavedScreen) {
        this.mContext = mContext;
        this.properties = properties;
        this.fragmentManager = fm;
        this.firstArraySize = properties.size();
        this.isSavedScreen = isSavedScreen;
        spaceBetweenIndicator = (int)mContext.getResources().getDimension(R.dimen.property_description_space_between_indicators);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(mContext);

        PropertyItemBinding binder = DataBindingUtil.inflate(mInflater, R.layout.property_item
                , parent, false);
        binder.setPresenter(new PropertyItemPresenter(mContext, binder));

        listener = PropertyItemPresenter.getInstance(mContext, binder);
        res = mContext.getResources();

        propertiesManager = BallabaSearchPropertiesManager.getInstance(mContext);
        return new ViewHolder(binder);//firstRootView);
    }

    @Override
    public void onBindViewHolder(final PropertiesRecyclerAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, properties.size()+":"+position);
        final BallabaPropertyResult property = properties.get(position);
        final int NUM_OF_PHOTOS = 6;
        PropertiesPhotosPagerAdapter propertiesPhotosViewPagerAdapter = new PropertiesPhotosPagerAdapter(
                fragmentManager, generateImageFragments(property.photos, property.id, position));
        holder.binder.propertyItemViewPager.setId(position+propertiesPhotosViewPagerAdapter.hashCode());
        //holder.binder.propertyItemViewPager.setCurrentItem(NUM_OF_PHOTOS/2);
        //holder.binder.propertyItemViewPager.setOffscreenPageLimit(0);
        holder.binder.propertyItemViewPager.setAdapter(propertiesPhotosViewPagerAdapter);
        /*holder.binder.propertyItemViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                holder.binder.propertyItemViewPagerIndicators.onPageChange(NUM_OF_PHOTOS - position - 1);
            }
            @Override
            public void onPageSelected(int position) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });*/

        setIndicators(holder.binder.tabIndicators, holder.binder.propertyItemViewPager, NUM_OF_PHOTOS);

       /* holder.binder.propertyItemViewPagerIndicators.setNoOfPages(NUM_OF_PHOTOS);
        holder.binder.propertyItemViewPagerIndicators.setVisibleDotCounts(5);
        holder.binder.propertyItemViewPagerIndicators.onPageChange(NUM_OF_PHOTOS - 1);*/
        //holder.binder.propertyItemViewPagerIndicators.setStartPosX(0);
        //holder.binder.propertyItemViewPagerIndicators.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);


        Drawable d = property.isSaved? res.getDrawable(R.drawable.heart_blue_24, mContext.getTheme())
                :res.getDrawable(R.drawable.heart_white_24, mContext.getTheme());
        holder.binder.propertyItemIsSavedPropertyImageView.setImageDrawable(d);

        holder.binder.propertyItemIsSavedPropertyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable d;
                if (property.isSaved){
                    d = res.getDrawable(R.drawable.heart_white_24, mContext.getTheme());
                    ConnectionsManager.getInstance(mContext).removeFromFavoritesProperty(property.id);
                    ((BaseActivity)mContext).getDefaultSnackBar(v, "נכס הוסר ממועדפים", false).show();
                }else{
                    d = res.getDrawable(R.drawable.heart_blue_24, mContext.getTheme());
                    ConnectionsManager.getInstance(mContext).addToFavoritesProperty(property.id);
                    ((BaseActivity)mContext).getDefaultSnackBar(holder.binder.getRoot(), "נכס הוסף למועדפים", false).show();
                }
                holder.binder.propertyItemIsSavedPropertyImageView.setImageDrawable(d);
                property.isSaved = !property.isSaved;
            }
        });

        @Visibility.Mode int visibility = property.isGuarantee? View.VISIBLE : View.GONE;
        holder.binder.propertyItemGuaranteeImageView.setVisibility(visibility);

        holder.binder.propertyItemAddressTextView.setText(property.formattedAddress);
        holder.binder.propertyItemPriceTextView.setText(StringUtils.getInstance(true)
                .formattedNumberWithComma(property.price));
        holder.binder.propertyItemRoomsTextView.setText(String.format("%s %s", property.roomsNumber, mContext.getString(R.string.propertyItem_numberOfRooms)));
        holder.binder.propertyItemPropertySizeTextView.setText(String.format("%s %s", property.size, mContext.getString(R.string.propertyItem_propertySize)));

        //TODO without binder and glide:
        //holder.propertyImageView.setImageBitmap(property.bitmap());
        ////holder.textViewAddress.setText(property.address());
        ////holder.textViewPrice.setText(property.price());
        //parent_layout = (LinearLayout) findViewById(R.id.single_message_parent);

        //TODO get offset properties from server:
        //ConnectionsManager.getInstance(this).getConfigRequest
        if(!isSavedScreen) {
            if (position == (properties.size() - (/*OFFSET*/20 / 2))) {
                lazyLoading();
            }
        }

        holder.binder.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullProperty(property, position);
            }
        });
    }

    private void showFullProperty(final BallabaPropertyResult property, final int position){

        BallabaUser user = BallabaUserManager.getInstance().getUser();

        user.userCurrentPropertyObservedID = property.id;
        boolean isScored = user.getIs_scored();

        if (isScored) {
            ConnectionsManager.getInstance(mContext).getPropertyPermission(property.id, new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    Intent intent = new Intent(mContext, PropertyDescriptionActivity.class);

                    intent.putExtra(PropertyDescriptionPresenter.PROPERTY_POSITION, position);
                    intent.putExtra(PropertyDescriptionActivity.PROPERTY, property.id);
                    if (property.photos.size() > 0)
                        intent.putExtra(PropertyDescriptionPresenter.PROPERTY_IMAGE
                                , property.photos.get(property.photos.size()/2));

                    mContext.startActivity(intent);
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    if (((BallabaErrorResponse)entity).statusCode == 403){
                        // TODO: 07/06/2018 INSERT X and get guarantor
                    }else {
                        // TODO: 07/06/2018 ERROR FLOW
                    }
                }
            });

        }else {
            mContext.startActivity(new Intent(mContext , ScoringWelcomeActivity.class));
        }
    }

    private ArrayList<PropertyImageFragment> generateImageFragments(ArrayList<String> photos, String propertyId, int position){
        ArrayList<PropertyImageFragment> items= new ArrayList<>();
        for (String photo : photos) {
            items.add(PropertyImageFragment.newInstance(photo, propertyId, position));
        }
        return items;
    }



    @Override
    public int getItemCount() {return properties == null? 0 : properties.size();}

    public void updateList(ArrayList<BallabaPropertyResult> newList) {
        properties.clear();
        properties.addAll(newList);
        notifyDataSetChanged();
    }

    public void updateList(ArrayList<BallabaPropertyResult> newList, boolean isLazyLoading) {
        if(!isLazyLoading) {
            properties.clear();
        }
        properties.addAll(newList);
        if(!isLazyLoading) {
            notifyDataSetChanged();
        }else{
            notifyItemInserted(properties.size()-this.firstArraySize);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //private TextView textViewPrice, textViewAddress;
        //private ImageView propertyImageView;
        public PropertyItemBinding binder;

        ViewHolder(PropertyItemBinding binder) {
            super(binder.getRoot());
            this.binder = binder;

            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                setFontForDevicesUnderApi26(binder);
            }

            //TODO without binder and glide:
            //propertyImageView = itemView.findViewById(R.id.propertyItem_imageView);
            //textViewAddress = itemView.findViewById(R.id.propertyItem_address_textView);
            //textViewPrice = itemView.findViewById(R.id.propertyItem_price_textView);
        }
    }

    private void lazyLoading(){
        BallabaSearchPropertiesManager.getInstance(mContext).getLazyLoadingResults(false, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                ArrayList<BallabaPropertyResult> properties =
                        propertiesManager.parsePropertyResults(((BallabaOkResponse)entity).body);
                propertiesManager.appendProperties(properties, true);

                updateList(propertiesManager.getResults(), true);
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.e(TAG, "error fetching offset properties");
            }
        });
    }

    private void setIndicators(final TabLayout tabLayout, final RtlViewPager vPager, final int numOfPhotos) {
        tabLayout.setupWithViewPager(vPager, true);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)tabLayout.getLayoutParams();
        params.width = numOfPhotos * spaceBetweenIndicator/*space*/;
        tabLayout.setLayoutParams(params);
    }

    private void setFontForDevicesUnderApi26(PropertyItemBinding binder){
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
