package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.michaelkibenko.ballaba.Adapters.AddPropertyPhotoRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyPhoto;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Holders.PropertyAttachmentsAddonsHolder;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Managers.SharedPreferencesManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropEditPhotoBinding;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropTakePhotoFrag.REQUEST_CODE_CAMERA;

public class AddPropEditPhotoFrag extends Fragment {
    private final String TAG = AddPropEditPhotoFrag.class.getSimpleName();
    public static String FIRST_PHOTO = "first_photo", ORIENTATIONS = "orientations", LAST_PHOTO = "last_photo";

    private Context context;
    private static ActivityAddPropertyBinding binderMain;
    private static FragmentAddPropEditPhotoBinding binderEditPhoto;
    private AddPropertyPhotoRecyclerAdapter adapter;
    private ArrayList<BallabaPropertyPhoto> photos = new ArrayList<>();
    private String[] orientations;
    private byte[] bytes;
    private String propertyID;
    //private JSONObject photosJson;
    //private ButtonUploadPhotoListener onClickListener;

    public AddPropEditPhotoFrag() {}
    public static AddPropEditPhotoFrag newInstance(ActivityAddPropertyBinding binding/*, String photo*/) {
        AddPropEditPhotoFrag fragment = new AddPropEditPhotoFrag();
        binderMain = binding;

        return fragment;
    }

    public AddPropEditPhotoFrag setMainBinder(ActivityAddPropertyBinding binder){
        this.binderMain = binder;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binderEditPhoto = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_prop_edit_photo, container, false);

        propertyID = SharedPreferencesManager.getInstance(context).getString(SharedPreferencesKeysHolder.PROPERTY_ID , null);
        getPhotoFromTakePhotoFragment();
        initRecyclerView(getActivity());
        initButtons();

        if (photos.isEmpty()){
            photosPlaceHolderChanger(true);
        }

        return binderEditPhoto.getRoot();
    }

    private void getPhotoFromTakePhotoFragment(){
        if (photos.isEmpty()) {//detect that i came here from TakePhotoFragment(=photos is empty), not from another fragment => what causes duplicating photos
            if (getArguments() != null && getArguments().containsKey(FIRST_PHOTO)) {
                //photos.add(new BallabaPropertyPhoto(Uri.parse(getArguments().get(FIRST_PHOTO) + "")));
                /*String s = null;
                try {
                    s = new String(getArguments().getByteArray(FIRST_PHOTO) , "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }*/
                photos.add(new BallabaPropertyPhoto(getArguments().getByteArray(FIRST_PHOTO)));
                this.orientations = getArguments().getStringArray(ORIENTATIONS);
            }
        }
    }

    /*public void setCameraResult(Context context, int requestCode, int resultCode, Intent imageIntent){
        this.context = context;
        onActivityResult(requestCode, resultCode, imageIntent);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageIntent) {
        super.onActivityResult(requestCode, resultCode, imageIntent);

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && imageIntent != null){
            Log.d(TAG, "onActivityResult: " + imageIntent.getExtras() + " " + imageIntent.getExtras().get("data"));

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Bitmap bitmap = (Bitmap) imageIntent.getExtras().get("data");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            bytes = byteArrayOutputStream.toByteArray();

            this.orientations = new String[]{MediaStore.Images.Media.ORIENTATION};

            binderEditPhoto.addPropPhotosRV.smoothScrollToPosition(photos.size() + 50);
            sendPhotoToServer(adapter.getData(context, new JSONObject()));
            photosPlaceHolderChanger(false);
        }
    }

    public void initRecyclerView(final Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);

        List<PropertyAttachmentAddonEntity> attachments = PropertyAttachmentsAddonsHolder.getInstance().getPhotoTags();

        adapter =  new AddPropertyPhotoRecyclerAdapter(this , context, photos, attachments, new AddPropertyPhotoRecyclerAdapter.AddPropPhotoRecyclerListener() {
            @Override
            public void onClickChip(String id, int position) {
                Button btn = binderEditPhoto.addPropPhotosButtonUpload;
                UiUtils.instance(true, context).buttonChanger(btn, true);
            }

            @Override
            public void onClickRemovePhoto(boolean isPlaceHolderDisplayed) {
                //TODO send request to server to delete photo id from server if id exist

                //when user removes all photos, "noPhoto" should be appear, and button should be active
                //when he returns the photo(isHide==false), "noPhoto" should be hide, and button should be inactive until he chooses a tag chip
                Log.d(TAG, "items left: " + adapter.getItemCount()+"");
                photosPlaceHolderChanger(isPlaceHolderDisplayed && adapter.getItemCount()==0);
            }
        });
        adapter.setImageOrientation(orientations);

        binderEditPhoto.addPropPhotosRV.setLayoutManager(linearLayoutManager);
        //binderEditPhoto.addPropPhotosRV.setHasFixedSize(true);
        binderEditPhoto.addPropPhotosRV.setAdapter(adapter);
    }

    private void initButtons(){
        binderEditPhoto.addPropPhotosButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CODE_CAMERA);
            }
        });
    }

   /* public void setPhotoJson(JSONObject photoJson){
        this.photosJson = photoJson;
    }*/

    private void sendPhotoToServer(final JSONObject photoJson){
        if (photoJson == null) return;

        /*ConnectionsManager.getInstance(context).uploadProperty(photoJson, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                //TODO update property updating date on SharedPrefs??
                SharedPreferencesManager.getInstance(context).putString(SharedPreferencesKeysHolder.PROPERTY_UPLOAD_STEP, "5");

                //set id to photo. id is received from server.
                BallabaPropertyPhoto sentPhoto = photos.get(photos.size() - 2);
                sentPhoto.setId(((BallabaPropertyPhoto)entity).getId());
                sentPhoto.setHasSent(true);
                getArguments().putSerializable(LAST_PHOTO, sentPhoto);
                sentPhoto.setBytes(bytes);
                photos.add(sentPhoto);
                adapter.notifyItemInserted(photos.size() - 1);
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                UiUtils.instance(true, context).showSnackBar(
                        binderEditPhoto.getRoot(), "השמירה נכשלה נסה שנית מאוחר יותר");

                //TODO draw x on photo
                drawXonPhoto();

                //TODO NEXT LINE IS ONLY FOR TESTING:
                //AddPropertyPresenter.getInstance((AppCompatActivity)context, binderMain).onNextViewPagerItem(5);
                //new AddPropertyPresenter((AppCompatActivity)context, binderMain).getDataFromFragment(2);
            }
        });*/
        ConnectionsManager.getInstance(context).newUploadProperty(5  , propertyID , photoJson, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                //TODO update property updating date on SharedPrefs??
                SharedPreferencesManager.getInstance(context).putString(SharedPreferencesKeysHolder.PROPERTY_UPLOAD_STEP, "5");

                //set id to photo. id is received from server.

                BallabaPropertyPhoto sentPhoto = photos.get(photos.size() - 1);
                sentPhoto.setId(((BallabaPropertyPhoto)entity).getId());
                sentPhoto.setHasSent(true);
                getArguments().putSerializable(LAST_PHOTO, sentPhoto);

                BallabaPropertyPhoto newPhoto = new BallabaPropertyPhoto(bytes);
                photos.add(newPhoto);

                adapter.notifyItemInserted(photos.size() - 1);

            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                UiUtils.instance(true, context).showSnackBar(
                        binderEditPhoto.getRoot(), "השמירה נכשלה נסה שנית מאוחר יותר");

                //TODO draw x on photo
                drawXonPhoto();

                //TODO NEXT LINE IS ONLY FOR TESTING:
                //AddPropertyPresenter.getInstance((AppCompatActivity)context, binderMain).onNextViewPagerItem(5);
                //new AddPropertyPresenter((AppCompatActivity)context, binderMain).getDataFromFragment(2);
            }
        });
    }

    private void drawXonPhoto(){

    }

    private void photosPlaceHolderChanger(boolean show){
        binderEditPhoto.addPropNoPhotosTitle.setVisibility(show? View.VISIBLE : View.GONE);
        binderEditPhoto.addPropNoPhotosDescription.setVisibility(show? View.VISIBLE : View.GONE);
        UiUtils.instance(true, context)
                .buttonChanger(binderEditPhoto.addPropPhotosButtonUpload, show);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

        /*try {
            onClickListener = (ButtonUploadPhotoListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement RegWizardCallback ");
        }*/
    }
/*
    public interface ButtonUploadPhotoListener {
        void onClick(JSONObject jsonObject);
    }*/
}