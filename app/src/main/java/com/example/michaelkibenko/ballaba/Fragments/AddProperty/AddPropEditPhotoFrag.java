package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.transition.Visibility;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.michaelkibenko.ballaba.Adapters.AddPropertyPhotoRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyPhoto;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Holders.PropertyAttachmentsAddonsHolder;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Managers.SharedPreferencesManager;
import com.example.michaelkibenko.ballaba.Presenters.AddPropertyPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropAssetBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropEditPhotoBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropTakePhotoFrag.REQUEST_CODE_CAMERA;
import static java.sql.Types.NULL;

public class AddPropEditPhotoFrag extends Fragment {
    private final String TAG = AddPropEditPhotoFrag.class.getSimpleName();
    public static String PHOTO = "photo", ORIENTATIONS = "orientations";

    private Context context;
    private ActivityAddPropertyBinding binderMain;
    private static FragmentAddPropEditPhotoBinding binderEditPhoto;
    private AddPropertyPhotoRecyclerAdapter adapter;
    private ArrayList<BallabaPropertyPhoto> photos = new ArrayList<>();
    private String[] orientations;
    //private JSONObject photosJson;
    //private ButtonUploadPhotoListener onClickListener;

    public AddPropEditPhotoFrag() {}
    public static AddPropEditPhotoFrag newInstance() {
        AddPropEditPhotoFrag fragment = new AddPropEditPhotoFrag();
        return fragment;
    }

    public AddPropEditPhotoFrag setMainBinder(ActivityAddPropertyBinding binder){
        this.binderMain = binder;
        return this;
    }

    public void setData(String photo, String[] orientations){
        photos.add(new BallabaPropertyPhoto(Uri.parse(photo)));
        this.orientations = orientations;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binderEditPhoto = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_prop_edit_photo, container, false);

        initRecyclerView(getActivity());
        initButtons();

        return binderEditPhoto.getRoot();
    }

    /*public void setCameraResult(Context context, int requestCode, int resultCode, Intent imageIntent){
        this.context = context;
        onActivityResult(requestCode, resultCode, imageIntent);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageIntent) {
        super.onActivityResult(requestCode, resultCode, imageIntent);

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && imageIntent != null){
            photos.add(new BallabaPropertyPhoto(imageIntent.getData()));
            this.orientations = new String[]{MediaStore.Images.Media.ORIENTATION};
            UiUtils.instance(true, context).buttonChanger(binderEditPhoto.addPropPhotosButtonUpload, false);
            binderEditPhoto.addPropNoPhotosTitle.setVisibility(View.GONE);
            binderEditPhoto.addPropNoPhotosDescription.setVisibility(View.GONE);

            adapter.notifyItemInserted(photos.size() - 1);

            //if (!photos.isEmpty())// && photos.get(photos.size() - 1).getId() != NULL)
                sendPhotoToServer(adapter.getData(context, new JSONObject()));

        }
    }

    public void initRecyclerView(final Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        List<PropertyAttachmentAddonEntity> attachments = PropertyAttachmentsAddonsHolder.getInstance().getPhotoTags();

        adapter =  new AddPropertyPhotoRecyclerAdapter(context, photos, attachments, new AddPropertyPhotoRecyclerAdapter.AddPropPhotoRecyclerListener() {
            @Override
            public void onClickChip(String id, int position) {
                Button btn = binderEditPhoto.addPropPhotosButtonUpload;
                UiUtils.instance(true, context).buttonChanger(btn, true);
            }

            @Override
            public void onClickRemovePhoto(boolean isHide) {
                //TODO send request to server to delete photo id from server if id exist

                //when user removes all photos, "noPhoto" should be appear, and button should be active
                //when he returns the photo(isHide==false), "noPhoto" should be hide, and button should be inactive until he chooses a tag chip
                binderEditPhoto.addPropNoPhotosTitle.setVisibility(
                        (isHide && adapter.getItemCount() > 0) ? View.GONE : View.VISIBLE);
                binderEditPhoto.addPropNoPhotosDescription.setVisibility(
                        (isHide && adapter.getItemCount() > 0) ? View.GONE : View.VISIBLE);
                UiUtils.instance(true, context).buttonChanger(binderEditPhoto.addPropPhotosButtonUpload, isHide);
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
        if (photoJson == null)
            return;

        ConnectionsManager.getInstance(context).uploadProperty(photoJson, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                //TODO update property updating date on SharedPrefs??
                SharedPreferencesManager.getInstance(context).putString(SharedPreferencesKeysHolder.PROPERTY_UPLOAD_STEP, "5");
                //set id to photo. id is received from server.
                photos.get(photos.size() - 1).setId(((BallabaPropertyPhoto)entity).getId());
                getArguments().putSerializable("photos", photos);
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