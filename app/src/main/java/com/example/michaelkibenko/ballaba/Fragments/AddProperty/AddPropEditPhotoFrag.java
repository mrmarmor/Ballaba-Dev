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
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Presenters.AddPropertyPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropAssetBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropEditPhotoBinding;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropTakePhotoFrag.REQUEST_CODE_CAMERA;

public class AddPropEditPhotoFrag extends Fragment {
    private final String TAG = AddPropEditPhotoFrag.class.getSimpleName();
    public static String PHOTO = "photo", ORIENTATIONS = "orientations";

    private Context context;
    private static ActivityAddPropertyBinding binderMain;
    private static FragmentAddPropEditPhotoBinding binderEditPhoto;
    private AddPropertyPhotoRecyclerAdapter adapter;
    private List<Uri> photos = new ArrayList<>();
    private String[] orientations;

    public AddPropEditPhotoFrag() {}
    public static AddPropEditPhotoFrag newInstance(ActivityAddPropertyBinding binding/*, String photo*/) {
        AddPropEditPhotoFrag fragment = new AddPropEditPhotoFrag();
        binderMain = binding;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binderEditPhoto = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_prop_edit_photo, container, false);

        if (getArguments() != null) {
            photos.add(Uri.parse(getArguments().get(PHOTO) + ""));
            this.orientations = getArguments().getStringArray(ORIENTATIONS);
        }

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
            String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
            photos.add(imageIntent.getData());
            this.orientations = orientationColumn;
            UiUtils.instance(true, context).buttonChanger(binderEditPhoto.addPropPhotosButtonUpload, false);

            adapter.notifyItemInserted(photos.size() - 1);
        }
    }

    public void initRecyclerView(final Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        List<PropertyAttachmentAddonEntity> attachments = new ArrayList<>();
        attachments.add(new PropertyAttachmentAddonEntity("4", "hall", "סלון"));
        attachments.add(new PropertyAttachmentAddonEntity("5", "toilets", "שירותים"));

        adapter =  new AddPropertyPhotoRecyclerAdapter(context, photos, attachments, new AddPropertyPhotoRecyclerAdapter.AddPropPhotoRecyclerListener() {
            @Override
            public void chipOnClick(String id, int position) {
                Button btn = binderEditPhoto.addPropPhotosButtonUpload;
                UiUtils.instance(true, context).buttonChanger(btn, true);
            }

            @Override
            public void closeButtonOnClick(boolean isHide) {
                binderEditPhoto.addPropNoPhotosTitle.setVisibility(isHide ? View.VISIBLE : View.GONE);
                binderEditPhoto.addPropNoPhotosDescription.setVisibility(isHide ? View.VISIBLE : View.GONE);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}