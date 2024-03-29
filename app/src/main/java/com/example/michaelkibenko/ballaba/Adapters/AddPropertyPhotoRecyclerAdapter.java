package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.michaelkibenko.ballaba.Activities.AddPropertyActivityNew;
import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Common.BallabaDialogBuilder;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyPhoto;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropEditPhotoFrag;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.AddPropertyPhotoItemBinding;
import com.nex3z.flowlayout.FlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;

/**
 * Created by User on 07/05/2018.
 */

public class AddPropertyPhotoRecyclerAdapter extends RecyclerView.Adapter<AddPropertyPhotoRecyclerAdapter.ViewHolder> {
    private final String TAG = AddPropertyPhotoRecyclerAdapter.class.getSimpleName();
    private final AddPropEditPhotoFrag addPropEditPhotoFrag;

    private Context context;
    private LayoutInflater mInflater;
    private ViewHolder holder;
    private List<BallabaPropertyPhoto> photos;
    private List<PropertyAttachmentAddonEntity> attachments;
    private String[] orientations;
    private AddPropPhotoRecyclerListener onClickListener;
    private AddPropPhotoFinishListener onFinishListener;

    public AddPropertyPhotoRecyclerAdapter(AddPropEditPhotoFrag addPropEditPhotoFrag, Context mContext, List<BallabaPropertyPhoto> photos, List<PropertyAttachmentAddonEntity> attachments
            , AddPropPhotoRecyclerListener listener) {
        this.addPropEditPhotoFrag = addPropEditPhotoFrag;
        this.context = mContext;
        this.photos = photos;
        this.attachments = attachments;
        this.onClickListener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.onFinishListener = (AddPropPhotoFinishListener) recyclerView.getContext();
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
        Log.d(TAG, photos.size() + ":" + position + ":" + orientations[0]);
        this.holder = holder;
        if (photos.size() > 0) {
            //TODO what if user photos are too large image (>40960px*4096px)??
            //Uri photoUri = photos.get(position).getPhoto();
            byte[] arr = photos.get(position).getBytes();
            Bitmap photoUri = BitmapFactory.decodeByteArray(arr , 0 , arr.length);
            //Matrix matrix = adjustPhotoOrientation(photoUri);
            Glide.with(context).load(photoUri).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    Log.d(TAG, "onLoadFailed: ");
                    holder.binder.addPropEditPhotoImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.dummy_property));
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    Log.d(TAG, "onResourceReady: ");
                    holder.binder.addPropEditPhotoImageView.setImageDrawable(resource);
                    return true;
                }
            }).into(holder.binder.addPropEditPhotoImageView);
            //holder.binder.addPropEditPhotoImageView.setTag(position);
            /*Bitmap bitmap = createBitmapFromUri(photoUri, matrix);

            if (bitmap != null) {
                holder.binder.addPropEditPhotoImageView.setImageBitmap(bitmap);
                holder.binder.addPropEditPhotoImageView.setTag(position);
            }*/
        }

        setFadeAnimation(holder.binder.addPropEditPhotoImageView);
        initTags(holder.binder.addPropEditPhotoRoomsFlowLayout, attachments, position);
        initButtonRemovePhoto(holder, position);
    }

    private void setFadeAnimation(View view) {
        final int FADE_DURATION = 1000;
        AlphaAnimation anim = new AlphaAnimation(0f, 1f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "property photos: " + photos.size() + " attachs: " + attachments.size());
        return (photos == null || photos.size() == 0) ? 0 : photos.size();
    }

    private void initTags(final FlowLayout flowLayout, List<PropertyAttachmentAddonEntity> items, final int position) {
        flowLayout.removeAllViewsInLayout();
        flowLayout.invalidate();
        Log.d(TAG, "tags number: " + flowLayout.getChildCount());

        for (final PropertyAttachmentAddonEntity attachment : items) {
            Button chipsItem = (Button) mInflater.inflate(R.layout.chip_regular, null);
            chipsItem.setText(attachment.formattedTitle);

            chipsItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTagClick(flowLayout, (Button) v, attachment, position);

                    //ArrayList<String> tags =  photos.get(position).getTags();
                    //tags.add(attachment.title);
                    //photos.get(position).setTags(tags);

                    //updates json with the new tag state and send it to parent activity(AddPropertyActivity)
                    onFinishListener.onFinish(getData(context, new JSONObject()));
                }
            });

            if (chipsItem.getTag() != null) {
                if (!chipsItem.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                    chipsItem.setTag(UiUtils.ChipsButtonStates.NOT_PRESSED);
                }
            } else {
                chipsItem.setTag(UiUtils.ChipsButtonStates.NOT_PRESSED);
            }

            //this loop set chip/tag press state, according to last uploading time
            for (PropertyAttachmentAddonEntity photoTag : photos.get(position).getTags()) {
                if (photoTag.id.equals(attachment.id)) {
                    photos.get(position).setTags(new HashSet<PropertyAttachmentAddonEntity>());
                    onTagClick(flowLayout, chipsItem, attachment, position);
                }
            }

            flowLayout.addView(chipsItem);
        }
    }

    private void onTagClick(FlowLayout flowLayout, Button btn, PropertyAttachmentAddonEntity attachment, int position) {
        String state = (String) btn.getTag();
        if (state.equals(UiUtils.ChipsButtonStates.NOT_PRESSED) || !allChipsUnselected(flowLayout, btn)) {
            if (state.equals(UiUtils.ChipsButtonStates.NOT_PRESSED))
                photos.get(position).addTag(attachment);
            else
                photos.get(position).removeTag(attachment);//user selected a tag
            UiUtils.instance(true, context).onChipsButtonClick(btn, state);
            if (allChipsUnselected(flowLayout, btn)) {//set button "upload photo" active only if at least 1 tag of current photo selected
                onClickListener.onClickChip(attachment.id, position);
                if (photos.size() > 0) { // "finish" button is active only if there is at least 1 photo + room tag(=chip) selected
                    //((AddPropertyActivity)context).invalidateOptionsMenu();
                    ((AddPropertyActivityNew) context).setFinishEnable(addPropEditPhotoFrag, true , photos.get(position));

                    //JSONObject jsonObject = getData(new JSONObject());
                    //AddPropEditPhotoFrag.newInstance(null).setPhotoJson(jsonObject);
                }
            }
        }

        /*if (state.equals(UiUtils.ChipsButtonStates.NOT_PRESSED))
            photos.get(position).addTag(attachment);
        else
            photos.get(position).removeTag(attachment);*/

    }

    private boolean allChipsUnselected(FlowLayout flowLayout, Button currentButton) {
        for (int i = 0; i < flowLayout.getChildCount(); i++) {
            if (!flowLayout.getChildAt(i).equals(currentButton)
                    && flowLayout.getChildAt(i).getTag().equals(UiUtils.ChipsButtonStates.PRESSED))
                return false;
        }

        return true;
    }

    private void initButtonRemovePhoto(final ViewHolder holder, final int position) {
        holder.binder.addPropEditPhotoButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BallabaDialogBuilder areUSureDialog = new BallabaDialogBuilder(context);

                areUSureDialog.setButtons(context.getString(R.string.addProperty_editPhoto_removePhoto_snackBar_remove), context.getString(R.string.addProperty_editPhoto_removePhoto_undo), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removePhoto(position);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                areUSureDialog.setContent(context.getString(R.string.addProperty_editPhoto_removePhoto_snackBar_title), context.getString(R.string.addProperty_editPhoto_removePhoto_snackBar_message), null);
                areUSureDialog.show();
            }
        });

    }

    private void removePhoto(final int position) {
        showSnackbar(photos.get(position), position);

        photos.remove(position);
        notifyDataSetChanged();
        onClickListener.onClickRemovePhoto(true);
    }

    private boolean wasPhotoDeleted = true;

    private void showSnackbar(final BallabaPropertyPhoto photoToDelete, final int position) {
        final int SNACK_BAR_DURATION = 5000;//ms
        //final BallabaPropertyPhoto photo = photos.get(position);

        Snackbar snackbar = ((BaseActivity) context).getDefaultSnackBar
                (holder.binder.getRoot(), context.getString(R.string.addProperty_editPhoto_removePhoto_message), false)

                .setDuration(SNACK_BAR_DURATION)
                .setAction(context.getString(R.string.addProperty_editPhoto_removePhoto_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        returnPhoto(photoToDelete, photos.size() - 1);//TODO photo returned to end of list. if we want to return it to its original position, set the index here
                        wasPhotoDeleted = false;
                    }
                })
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        Log.d(TAG, "wasPhotoDeleted: " + wasPhotoDeleted);
                        if (wasPhotoDeleted && photoToDelete.getId() >= 0) {
                            BallabaPropertyFull propertyFull = BallabaSearchPropertiesManager
                                    .getInstance(context).getPropertyFull();
                            final String propertyId = propertyFull != null ? propertyFull.getInstance(context).id : null;
                            if (propertyId == null) return;
                            deletePhotoFromServer(propertyId, photoToDelete, position);
                        }
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                    }
                });

        snackbar.show();
    }

    private void returnPhoto(BallabaPropertyPhoto photoToReturn, int position) {
        photos.add(position, photoToReturn);
        HashSet<PropertyAttachmentAddonEntity> tags = photoToReturn.getTags();
        for (PropertyAttachmentAddonEntity tag : tags) {
            int positionOfTag = Integer.parseInt(tag.id) - 1;//position is actually the same as its id - 1
            Button btn = (Button) holder.binder.addPropEditPhotoRoomsFlowLayout.getChildAt(positionOfTag);
            btn.setTag(UiUtils.ChipsButtonStates.NOT_PRESSED);
            onTagClick(holder.binder.addPropEditPhotoRoomsFlowLayout, btn, tag, position);
        }

        notifyDataSetChanged();

        onClickListener.onClickRemovePhoto(false);
    }

    private void deletePhotoFromServer(final String propertyId, final BallabaPropertyPhoto photo, final int position) {
        ConnectionsManager.getInstance(context).deletePropertyPhoto(propertyId, photo.getId(), new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {

            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                ((BaseActivity) context).getDefaultSnackBar(holder.binder.getRoot(), "יש לנו שגיאה בשרת. התמונה לא נמחקה.", false);
                returnPhoto(photo, position);
                wasPhotoDeleted = false;
            }
        });

    }

    //portrait/landscape
    /*private Matrix adjustPhotoOrientation(Uri photo) {
        Cursor cur = context.getContentResolver().query(photo, orientations, null, null, null);
        int orientation = -1;
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientations[0]));
            cur.close();
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);

        return matrix;
    }*/

    /*private Bitmap createBitmapFromUri(Uri photo, Matrix matrix) {
        try {
            Bitmap bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photo);
            Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            return bitmap;
        } catch (IOException | OutOfMemoryError e) {
            e.printStackTrace();
            Log.e(TAG, "error: " + e.getMessage());
            return null;
        }
    }*/

    public JSONObject getData(Context context, JSONObject jsonObject) {
        //byte[] photo = UiUtils.instance(true, context).uriToBytes(photos.get(photos.size() - 1).getPhoto());

        byte[] photo = photos.get(photos.size() - 1).getBytes();
        try {
            JSONArray innerArrayTags = new JSONArray();

            FlowLayout tagsParent = holder.binder.addPropEditPhotoRoomsFlowLayout;
            for (int i = 0; i < tagsParent.getChildCount(); i++) {
                if (tagsParent.getChildAt(i).getTag().equals(UiUtils.ChipsButtonStates.PRESSED))
                    innerArrayTags.put(((Button) tagsParent.getChildAt(i)).getText().toString());
            }

            jsonObject.put("tags", innerArrayTags);
            jsonObject.put("image", Base64.encodeToString(photo, Base64.DEFAULT));

            //}
            return jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setImageOrientation(String[] orientations) {
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
        void onClickRemovePhoto(boolean isPlaceHolderDisplayed);
    }

    public interface AddPropPhotoFinishListener {
        void onFinish(JSONObject jsonObject);
    }

}


