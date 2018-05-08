package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.transition.Visibility;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Activities.PropertyDescriptionActivity;
import com.example.michaelkibenko.ballaba.Common.BallabaPropertyListener;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Fragments.PropertyImageFragment;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Presenters.PropertyDescriptionPresenter;
import com.example.michaelkibenko.ballaba.Presenters.PropertyItemPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.AddPropertyPhotoItemBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropEditPhotoBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyItemBinding;
import com.nex3z.flowlayout.FlowLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 07/05/2018.
 */

public class AddPropertyPhotoRecyclerAdapter extends RecyclerView.Adapter<AddPropertyPhotoRecyclerAdapter.ViewHolder> {
    private final String TAG = AddPropertyPhotoRecyclerAdapter.class.getSimpleName();

    final private Context context;
    private LayoutInflater mInflater;
    private List<Uri> photos = new ArrayList<>();
    private List<PropertyAttachmentAddonEntity> attachments = new ArrayList<>();
    private AddPropPhotoRecyclerListener onClickListener;

    public AddPropertyPhotoRecyclerAdapter(Context mContext, List<Uri> photos, List<PropertyAttachmentAddonEntity> attachments
            , AddPropPhotoRecyclerListener listener) {
        this.context = mContext;
        this.photos = photos;
        this.attachments = attachments;
        this.onClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(context);

        AddPropertyPhotoItemBinding binder = DataBindingUtil.inflate(
                mInflater, R.layout.add_property_photo_item, parent, false);

        initTags(binder.addPropEditPhotoRoomsFlowLayout, attachments, 0);

        return new ViewHolder(binder, binder.addPropEditPhotoImageView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(TAG, photos.size() + ":" + position);

        if (photos.size() > 0) {
            //TODO what if user photos too large image (>40960px*4096px)??
            holder.binder.addPropEditPhotoImageView.setImageURI(photos.get(position));
        }

        //initTags(holder.binder.addPropEditPhotoRoomsFlowLayout, attachments, position);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "property photos: " + photos.size());
        //TODO
        return 1;
        //return (photos == null || photos.size() == 0) ? 0 : photos.size();
    }

    private void initTags(FlowLayout flowLayout, List<PropertyAttachmentAddonEntity> items, final int position){
        for (final PropertyAttachmentAddonEntity attachment : items) {
            Button chipsItem = (Button)mInflater.inflate(R.layout.chip_regular, null);
            if(chipsItem.getTag() != null) {
                if (!chipsItem.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                    chipsItem.setTag(UiUtils.ChipsButtonStates.NOT_PRESSED);
                }
            }else{
                chipsItem.setTag(UiUtils.ChipsButtonStates.NOT_PRESSED);
            }

            chipsItem.setText(attachment.formattedTitle);
            chipsItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String state = (String) v.getTag();
                    UiUtils.instance(true, context).onChipsButtonClick((Button) v, state);
                    onClickListener.chipOnClick(attachment.id, state, position);
                }
            });
            //highlightSavedButtons(flowLayout, attachment);

            flowLayout.addView(chipsItem);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private AddPropertyPhotoItemBinding binder;
        //private ImageView imageView;

        ViewHolder(AddPropertyPhotoItemBinding binder, ImageView imageView) {
            super(binder.getRoot());
            this.binder = binder;
            //this.imageView = imageView;
            //initTags(binder.addPropEditPhotoRoomsFlowLayout, attachments, 0);

        }
    }

    public interface AddPropPhotoRecyclerListener {
        void chipOnClick(String id, String state, int position);
    }
}
