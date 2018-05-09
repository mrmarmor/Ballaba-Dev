package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Common.BallabaDialogBuilder;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.AddPropertyPhotoItemBinding;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
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

        return new ViewHolder(binder, binder.addPropEditPhotoImageView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(TAG, photos.size() + ":" + position);

        if (photos.size() > 0) {
            //TODO what if user photos are too large image (>40960px*4096px)??
            holder.binder.addPropEditPhotoImageView.setImageURI(photos.get(position));
        }

        initTags(holder.binder.addPropEditPhotoRoomsFlowLayout, attachments, position);
        initButtonRemovePhoto(holder, position);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "property photos: " + photos.size());
        return (photos == null || photos.size() == 0) ? 0 : photos.size();
    }

    private void initTags(final FlowLayout flowLayout, List<PropertyAttachmentAddonEntity> items, final int position){
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
                    if (state.equals(UiUtils.ChipsButtonStates.NOT_PRESSED) || !allChipsUnselected(flowLayout, (Button)v)){
                        UiUtils.instance(true, context).onChipsButtonClick((Button) v, state);
                        onClickListener.chipOnClick(attachment.id, position);
                    }
                }
            });
            //highlightSavedButtons(flowLayout, attachment);

            flowLayout.addView(chipsItem);
        }

    }

    private boolean allChipsUnselected(FlowLayout flowLayout, Button currentButton){
            for (int i = 0; i < flowLayout.getChildCount(); i++){
                Log.d(TAG, flowLayout.getChildAt(i)+":"+currentButton);
                if (!flowLayout.getChildAt(i).equals(currentButton))
                    if (flowLayout.getChildAt(i).getTag().equals(UiUtils.ChipsButtonStates.PRESSED))
                        return false;
            }

            return true;
    }

    private void initButtonRemovePhoto(final ViewHolder holder, final int position){
        holder.binder.addPropEditPhotoButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BallabaDialogBuilder areUSureDialog = new BallabaDialogBuilder(context);

                areUSureDialog.setButtons("הסרה", "ביטול", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removePhoto(holder, position);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                areUSureDialog.setContent("הסרת תמונה", "האם להסיר תמונה זו?", null).show();



            }
        });

    }

    private void removePhoto(final ViewHolder holder, final int position){
        final Uri photoToRemove = photos.get(position);
        photos.remove(position);
        notifyDataSetChanged();
        onClickListener.closeButtonOnClick(true);

        Snackbar snackbar = UiUtils.instance(true, context)
                .showSnackBar(holder.binder.getRoot(), "התמונה הוסרה")
                .setDuration(Snackbar.LENGTH_INDEFINITE)
                .setAction("ביטול", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        photos.add(position, photoToRemove);
                        notifyDataSetChanged();
                        onClickListener.closeButtonOnClick(false);
                    }
                });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private AddPropertyPhotoItemBinding binder;
        private ImageView imageView;

        ViewHolder(AddPropertyPhotoItemBinding binder, ImageView imageView) {
            super(binder.getRoot());
            this.binder = binder;
            this.imageView = imageView;
            //initTags(binder.addPropEditPhotoRoomsFlowLayout, attachments, 0);

        }
    }

    public interface AddPropPhotoRecyclerListener {
        void chipOnClick(String id, int position);
        void closeButtonOnClick(boolean isHide);
    }
}
