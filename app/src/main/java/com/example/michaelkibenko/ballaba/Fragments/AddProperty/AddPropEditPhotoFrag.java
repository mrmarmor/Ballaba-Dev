package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropAssetBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropEditPhotoBinding;

public class AddPropEditPhotoFrag extends Fragment {
    private final String TAG = AddPropEditPhotoFrag.class.getSimpleName();

    private Context context;
    private static ActivityAddPropertyBinding binderMain;
    private static FragmentAddPropEditPhotoBinding binderEditPhoto;
    private Uri image;

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

        View v = binderEditPhoto.getRoot();
        return v;
    }

    public void setImage(Uri image){
         binderEditPhoto.a.setImageURI(image);

    }

}