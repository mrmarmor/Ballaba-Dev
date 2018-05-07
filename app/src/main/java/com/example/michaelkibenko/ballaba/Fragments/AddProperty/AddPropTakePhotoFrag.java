package com.example.michaelkibenko.ballaba.Fragments.AddProperty;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Presenters.AddPropertyPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;

import static android.app.Activity.RESULT_OK;

public class AddPropTakePhotoFrag extends Fragment implements View.OnClickListener{
    private final String TAG = AddPropTakePhotoFrag.class.getSimpleName();
    private final int REQUEST_CODE_CAMERA = 1;

    private static ActivityAddPropertyBinding binderMain;

    public AddPropTakePhotoFrag() {}
    public static AddPropTakePhotoFrag newInstance(ActivityAddPropertyBinding binding) {
        AddPropTakePhotoFrag fragment = new AddPropTakePhotoFrag();
        binderMain = binding;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_prop_take_photo, container, false);

        v.findViewById(R.id.addProp_takePhoto_button_professionalPhotographer).setOnClickListener(this);
        v.findViewById(R.id.addProp_takePhoto_button_takePhoto).setOnClickListener(this);

        return v;
    }

    public void onClick(View v){
        if (v.getId() == R.id.addProp_takePhoto_button_professionalPhotographer)
            Toast.makeText(getActivity(), "taking Professional Photographer", Toast.LENGTH_SHORT).show();
        else
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CODE_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageIntent) {
        super.onActivityResult(requestCode, resultCode, imageIntent);

        if (resultCode == RESULT_OK && imageIntent != null){
            Uri image = imageIntent.getData();

            if (requestCode == REQUEST_CODE_CAMERA){
                //Intent intent = new Intent(getActivity(), AddPropEditPhotoFrag.class);
                //intent.putExtra("byteArray", image.toString());

                AddPropEditPhotoFrag.newInstance(binderMain).setImage(image);
                AddPropertyPresenter.getInstance((AppCompatActivity)getActivity(), binderMain).onNextViewPagerItem(4);

            }
        }
    }
}
