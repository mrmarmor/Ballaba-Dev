package com.example.michaelkibenko.ballaba.Fragments.AddProperty;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;

public class AddPropEditPhotoFrag extends Fragment {
    private final String TAG = AddPropEditPhotoFrag.class.getSimpleName();

    private static ActivityAddPropertyBinding binderMain;

    public AddPropEditPhotoFrag() {}
    public static AddPropTakePhotoFrag newInstance(ActivityAddPropertyBinding binding) {
        AddPropTakePhotoFrag fragment = new AddPropTakePhotoFrag();
        binderMain = binding;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_prop_edit_photo, container, false);
        ((ImageView)v.findViewById(R.id.a)).setImageURI(Uri.parse(getActivity().getIntent().getStringExtra("byteArray")));
        return v;
    }

}
