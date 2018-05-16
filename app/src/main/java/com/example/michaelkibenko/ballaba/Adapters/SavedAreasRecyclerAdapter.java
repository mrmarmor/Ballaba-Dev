package com.example.michaelkibenko.ballaba.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Activities.EditViewportSubActivity;
import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Activities.SavedAreaActivity;
import com.example.michaelkibenko.ballaba.Common.BallabaDialogBuilder;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.PropertyDescriptionComment;
import com.example.michaelkibenko.ballaba.Entities.Viewport;
import com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment;
import com.example.michaelkibenko.ballaba.Holders.EndpointsHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Managers.ViewportsManager;
import com.example.michaelkibenko.ballaba.Presenters.SavedAreaPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivitySavedAreaBinding;
import com.example.michaelkibenko.ballaba.databinding.AddPropertyPhotoItemBinding;
import com.example.michaelkibenko.ballaba.databinding.SavedAreaItemBinding;

import java.util.ArrayList;
import java.util.List;

import static com.example.michaelkibenko.ballaba.Presenters.SavedAreaPresenter.REQ_CODE_EDIT_VIEWPORT;

/**
 * Created by User on 15/05/2018.
 */

public class SavedAreasRecyclerAdapter extends RecyclerView.Adapter implements View.OnClickListener{
    private Context context;
    private SavedAreaItemBinding binder;
    private ActivitySavedAreaBinding binderParent;
    private LayoutInflater mInflater;
    private ArrayList<Viewport> viewports;
    private int position;

    public SavedAreasRecyclerAdapter(Context context, ActivitySavedAreaBinding binding, ArrayList<Viewport> viewports) {
        this.context = context;
        this.binderParent = binding;
        this.viewports = viewports;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(context);

        binder = DataBindingUtil.inflate(
                mInflater, R.layout.saved_area_item, parent, false);

        return new SavedAreasRecyclerAdapter.ViewHolder(binder);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        this.position = position;

        binder.savedAreasItemTitle.setText(viewports.get(position).title);
        binder.savedAreasItemEdit.setOnClickListener(this);
        binder.savedAreasItemDelete.setOnClickListener(this);

        //binder.savedAreasItemImageView.setAdjustViewBounds(false);
        Bitmap bitmap = BitmapFactory.decodeByteArray(viewports.get(position).mapImage
                , 0, viewports.get(position).mapImage.length);

        binder.savedAreasItemImageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return viewports.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.savedAreas_item_edit: default:

                if (viewports != null && viewports.size() > position) {
                    Intent intent = new Intent(context, EditViewportSubActivity.class);
                    ((Activity) context).startActivityForResult(intent, REQ_CODE_EDIT_VIEWPORT);

                    BallabaMapFragment mapFragment = BallabaMapFragment.newInstance();
                    mapFragment.setLocation(viewports.get(position).bounds.getCenter());
                }
                //fragment.focusAt(viewports.get(position).bounds);

                /*((SavedAreaActivity)context).setActivityState(SavedAreaActivity.MAP_STATES.ON);

                Fragment mapFragment = BallabaMapFragment.newInstance();
                ((SavedAreaActivity)context).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.savedAreas_root, mapFragment)
                        .commit();*/
                break;

            case R.id.savedAreas_item_delete:
                showDeleteItemDialog();
        }
    }

    private void showDeleteItemDialog(){
        final BallabaDialogBuilder areUSureDialog = new BallabaDialogBuilder(context, R.layout.dialog_regular);
        areUSureDialog.setContentInView(context.getString(R.string.savedAreas_item_deleteDialog_title)
                , context.getString(R.string.savedAreas_item_deleteDialog_message), null);
        areUSureDialog.setButtonsInView(context.getString(R.string.savedAreas_item_deleteDialog_button_positive)
                                      , context.getString(R.string.savedAreas_item_deleteDialog_button_negative)
                                      , R.color.red_error_phone
                                      , new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ConnectionsManager.getInstance(context).removeViewport(viewports.get(position).id, new BallabaResponseListener() {
                    @Override
                    public void resolve(BallabaBaseEntity entity) {
                        deleteItem();
                    }

                    @Override
                    public void reject(BallabaBaseEntity entity) {
                        ((BaseActivity)context).getDefaultSnackBar(binderParent.getRoot()
                                     , context.getString(R.string.savedAreas_item_deleteDialog_response_failure), false).show();
                    }
                });

                areUSureDialog.create().dismiss();
            }

        }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        areUSureDialog.create().dismiss();
                    }
        });

        areUSureDialog.show();
    }

    private void deleteItem(){
        if (viewports.size() > 0) {
            viewports.remove(position);
            notifyItemRemoved(position);
            ((BaseActivity)context).getDefaultSnackBar(binderParent.getRoot()
                    , context.getString(R.string.savedAreas_item_deleteDialog_response_success), false).show();
        }

        if (viewports.size() == 0)
            binderParent.getPresenter().showPlaceHolder();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        private ViewHolder(SavedAreaItemBinding binding) {
            super(binding.getRoot());
        }
    }
}
