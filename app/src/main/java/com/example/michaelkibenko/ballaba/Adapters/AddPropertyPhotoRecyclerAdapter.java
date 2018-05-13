package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.AddPropertyActivity;
import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Common.BallabaDialogBuilder;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropEditPhotoFrag;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Managers.SharedPreferencesManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
import com.example.michaelkibenko.ballaba.databinding.AddPropertyPhotoItemBinding;
import com.nex3z.flowlayout.FlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 07/05/2018.
 */

public class AddPropertyPhotoRecyclerAdapter extends RecyclerView.Adapter<AddPropertyPhotoRecyclerAdapter.ViewHolder> {
    private final String TAG = AddPropertyPhotoRecyclerAdapter.class.getSimpleName();

    private Context context;
    private LayoutInflater mInflater;
    private ViewHolder holder;
    private List<Uri> photos = new ArrayList<>();
    private List<PropertyAttachmentAddonEntity> attachments = new ArrayList<>();
    private String[] orientations;
    private AddPropPhotoRecyclerListener onClickListener;

    public AddPropertyPhotoRecyclerAdapter(){}
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
        Log.d(TAG, photos.size() + ":" + position+":"+orientations[0]);
        this.holder = holder;

        if (photos.size() > 0) {
            //TODO what if user photos are too large image (>40960px*4096px)??
            Bitmap bitmap = adjustPhotoOrientation(photos.get(position));
            if (bitmap != null) {
                holder.binder.addPropEditPhotoImageView.setImageBitmap(bitmap);
                holder.binder.addPropEditPhotoImageView.setTag(position);
            }
        }

        setFadeAnimation(holder.binder.addPropEditPhotoImageView);
        initTags(holder.binder.addPropEditPhotoRoomsFlowLayout, attachments, position);
        initButtonRemovePhoto(holder, position);
    }

    private final int FADE_DURATION = 1000;
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0f, 1f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "property photos: " + photos.size()+" attachs: "+attachments.size());
        return (photos == null || photos.size() == 0) ? 0 : photos.size();
    }

    private void initTags(final FlowLayout flowLayout, List<PropertyAttachmentAddonEntity> items, final int position){
        Log.d(TAG, flowLayout.getChildCount()+"");
        flowLayout.removeAllViewsInLayout();
        flowLayout.invalidate();
        Log.d(TAG, flowLayout.getChildCount()+"");

        for (final PropertyAttachmentAddonEntity attachment : items) {
            Button chipsItem = (Button)mInflater.inflate(R.layout.chip_regular, null);
            if (chipsItem.getTag() != null) {
                if (!chipsItem.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                    chipsItem.setTag(UiUtils.ChipsButtonStates.NOT_PRESSED);
                }
            } else {
                chipsItem.setTag(UiUtils.ChipsButtonStates.NOT_PRESSED);
            }

            chipsItem.setText(attachment.formattedTitle);
            chipsItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String state = (String) v.getTag();
                    if (state.equals(UiUtils.ChipsButtonStates.NOT_PRESSED) || !allChipsUnselected(flowLayout, (Button)v)){
                        UiUtils.instance(true, context).onChipsButtonClick((Button) v, state);
                        if (allChipsUnselected(flowLayout, (Button)v)) {//set button "upload photo" active only if chip of current photo selected
                            onClickListener.onClickChip(attachment.id, position);
                            if (photos.size() > 0) { // "finish" button is active only if there is at least 1 photo + room tag(=chip) selected
                                ((AddPropertyActivity)context).invalidateOptionsMenu();
                                //JSONObject jsonObject = getData(new JSONObject());
                                //AddPropEditPhotoFrag.newInstance(null).setPhotoJson(jsonObject);

                            }
                        }
                    }
                }
            });

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

                areUSureDialog.setButtons(context.getString(R.string.addProperty_editPhoto_removePhoto_snackBar_remove), context.getString(R.string.addProperty_editPhoto_removePhoto_undo), new DialogInterface.OnClickListener() {
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
                areUSureDialog.setContent(context.getString(R.string.addProperty_editPhoto_removePhoto_snackBar_title), context.getString(R.string.addProperty_editPhoto_removePhoto_snackBar_message), null).show();
            }
        });

    }

    private void removePhoto(final ViewHolder holder, final int position){
        final int SNACK_BAR_DURATION = 5000;//ms
        final Uri photoToRemove = photos.get(position);
        photos.remove(position);
        notifyDataSetChanged();
        onClickListener.onClickRemovePhoto(true);

        UiUtils.instance(true, context)
               .showSnackBar(holder.binder.getRoot(), context.getString(R.string.addProperty_editPhoto_removePhoto_message))
               .setDuration(SNACK_BAR_DURATION)
               .setAction(context.getString(R.string.addProperty_editPhoto_removePhoto_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       photos.add(/*position, */photoToRemove);//TODO photo returned to end of list. if we want to return it to its original position, set the index here
                       notifyDataSetChanged();
                       onClickListener.onClickRemovePhoto(false);
                   }
                });
    }

    //portrait/landscape
    private Bitmap adjustPhotoOrientation(Uri photo){
        Cursor cur = context.getContentResolver().query(photo, orientations, null, null, null);
        int orientation = -1;
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientations[0]));
        }
        cur.close();

        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);

        try {
            Bitmap bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photo);
            Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getData(JSONObject jsonObject){
        try {
            JSONObject innerObject = new JSONObject();
            JSONArray innerArrayTags = new JSONArray();
            String propertyId = SharedPreferencesManager.getInstance(context).getString(SharedPreferencesKeysHolder.PROPERTY_ID, "502");//"-1");

            jsonObject.put("step", "photos");
            jsonObject.put("property_id", Integer.parseInt(propertyId));

            //for (Uri photo : photos) {
            //ArrayList<String> tags = new ArrayList<>();
                FlowLayout tagsParent = holder.binder.addPropEditPhotoRoomsFlowLayout;
                for (int i = 0; i < tagsParent.getChildCount(); i++){
                    if (tagsParent.getChildAt(i).getTag().equals(UiUtils.ChipsButtonStates.PRESSED))
                        innerArrayTags.put(((Button)tagsParent.getChildAt(i)).getText().toString());
                        //tags.add(((Button)tagsParent.getChildAt(i)).getText().toString());
                }
            innerObject.put("tags", innerArrayTags);
            innerObject.put("image", photos.get(photos.size() - 1).toString());
            jsonObject.put("data", innerObject);
            //}
            return jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setImageOrientation(String[] orientations){
        this.orientations = orientations;
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
        void onClickChip(String id, int position);
        void onClickRemovePhoto(boolean isHide);
    }

}


