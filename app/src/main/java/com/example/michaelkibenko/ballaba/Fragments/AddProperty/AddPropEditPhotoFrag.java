package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.michaelkibenko.ballaba.Adapters.AddPropertyPhotoRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Presenters.AddPropertyPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropAssetBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropEditPhotoBinding;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropTakePhotoFrag.REQUEST_CODE_CAMERA;

public class AddPropEditPhotoFrag extends Fragment {
    private final String TAG = AddPropEditPhotoFrag.class.getSimpleName();

    private Context context;
    private static ActivityAddPropertyBinding binderMain;
    private static FragmentAddPropEditPhotoBinding binderEditPhoto;
    private AddPropertyPhotoRecyclerAdapter adapter;
    private List<Uri> photos = new ArrayList<>();

    public AddPropEditPhotoFrag() {}
    public static AddPropEditPhotoFrag newInstance(ActivityAddPropertyBinding binding) {
        AddPropEditPhotoFrag fragment = new AddPropEditPhotoFrag();
        binderMain = binding;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binderEditPhoto = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_prop_edit_photo, container, false);
        binderEditPhoto.addPropPhotosButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CODE_CAMERA);
            }
        });

        initRecyclerView(getActivity());

        return binderEditPhoto.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageIntent) {
        super.onActivityResult(requestCode, resultCode, imageIntent);

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && imageIntent != null){
            addPhoto(getActivity(), imageIntent.getData());
        }
    }

    public void initRecyclerView(Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        List<PropertyAttachmentAddonEntity> attachments = new ArrayList<>();
        attachments.add(new PropertyAttachmentAddonEntity("4", "hall", "סלון"));

        adapter =  new AddPropertyPhotoRecyclerAdapter(context, photos, attachments);
        binderEditPhoto.addPropPhotosRV.setLayoutManager(linearLayoutManager);
        //binderEditPhoto.addPropPhotosRV.setHasFixedSize(true);
        binderEditPhoto.addPropPhotosRV.setAdapter(adapter);
    }

    public void addPhoto(Context context, Uri photo) {
        photos.add(photo);

        if (adapter == null){
            initRecyclerView(context);
            adapter.updateList(photos);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}