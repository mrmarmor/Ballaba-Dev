package com.example.michaelkibenko.ballaba.Adapters;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.example.michaelkibenko.ballaba.Presenters.PropertyItemPresenter;
import com.example.michaelkibenko.ballaba.Presenters.TestingPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.PropertyItemSingleInARowBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyItemDuplicatedBinding;

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
    private GridLayoutManager recyclerViewLayoutManager;
    private LinearLayout parent_layout;
    private PropertyItemSingleInARowBinding bindSingle;
    private PropertyItemDuplicatedBinding bindDuplicate;
    //private String phoneNumber, shareCardMessage;

    public PropertiesRecyclerAdapter(Context mContext, RecyclerView mRecyclerView
            , GridLayoutManager manager, List<BallabaProperty> properties, BallabaUser user) {
        this.mContext = mContext;
        this.mRecyclerView = mRecyclerView;
        this.recyclerViewLayoutManager = manager;
        this.properties = properties;
        this.user = user;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(mContext);
        bindSingle = DataBindingUtil.inflate(mInflater, R.layout.property_item_single_in_a_row
                , parent, false);
        bindSingle.setPresenter(new PropertyItemPresenter(mContext));

        this.parent = parent;


        ////mInflater = LayoutInflater.from(mContext);
        ////firstRootView = mInflater.inflate(R.layout.property_item_single_in_a_row, parent, false);
        //parent_layout = (LinearLayout)view.findViewById(R.id.single_Property_parent);

        return new ViewHolder(bindSingle.getRoot());//firstRootView);
    }

    @Override
    public void onBindViewHolder(final PropertiesRecyclerAdapter.ViewHolder holder, final int position) {
        if (user != null && properties.size() > 0 && position < properties.size()) {
            Log.d(TAG, properties.size()+":"+position);
            BallabaProperty property = properties.get(position);

            //arrangeLayoutManagerToDisplaySingleAndDuplicateItems(recyclerViewLayoutManager);

            //TODO another way to set items count in a row. not working - displays 1 item.
            /*if (position == 0) {
                recyclerViewLayoutManager.setSpanCount(1);
                //recyclerViewLayoutManager = new StaggeredGridLayoutManager(1 ,StaggeredGridLayoutManager.VERTICAL);
            } else {
                recyclerViewLayoutManager.setSpanCount(2);
            }*/

            Glide.with(mContext).load(property.bitmap()).into(bindSingle.propertyItemImageView);//holder.propertyImageView);
            bindSingle.propertyItemAddressTextView.setText(property.address());
            bindSingle.propertyItemPriceTextView.setText(property.price());

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

    public void setImageView(Bitmap bitmap){
        //holder.propertyImageView.setImageBitmap(bitmap);
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

    private void arrangeLayoutManagerToDisplaySingleAndDuplicateItems(final GridLayoutManager layoutManager){
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Log.d(TAG, "span: "+position+":"+layoutManager.getSpanCount());
                if (position > 0) {
                    Log.d(TAG, "position is "+position + " returning 1");
                    return 1;
                }

                Log.d(TAG, "position is "+position + " returning 0");
                return 0;//TODO it should be 2. however it gets an "array out of bounds" exception.
            }
        });
    }

}
