package com.example.michaelkibenko.ballaba.Fragments.AddProperty;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Adapters.AddPropertyPagerAdapter;
import com.example.michaelkibenko.ballaba.Presenters.AddPropertyPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class AddPropTakePhotoFrag extends Fragment implements View.OnClickListener{
    private final String TAG = AddPropTakePhotoFrag.class.getSimpleName();
    public static final int REQUEST_CODE_CAMERA = 1;

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
        if (v.getId() == R.id.addProp_takePhoto_button_professionalPhotographer) {
            Toast.makeText(getActivity(), "taking Professional Photographer", Toast.LENGTH_SHORT).show();
        } else {
            //AddPropertyPresenter.getInstance((AppCompatActivity) getActivity(), binderMain).onNextViewPagerItem(4);
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CODE_CAMERA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageIntent) {
        super.onActivityResult(requestCode, resultCode, imageIntent);

        //AddPropEditPhotoFrag.newInstance(binderMain).setCameraResult(
        //        getActivity(), requestCode, resultCode, imageIntent);
       // AddPropEditPhotoFrag.newInstance(binderMain)
        //        .onActivityResult(requestCode, resultCode, imageIntent);
        if (resultCode == RESULT_OK && imageIntent != null && imageIntent.getData() != null){
            if (requestCode == REQUEST_CODE_CAMERA) {
                String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};

                ((AddPropertyPagerAdapter)binderMain.addPropertyViewPager.getAdapter())
                        .setData(imageIntent.getData(), orientationColumn);
                AddPropertyPresenter.getInstance((AppCompatActivity)getActivity(), binderMain).onNextViewPagerItem(4);
                //AddPropEditPhotoFrag.newInstance(binderMain).addPhoto(
                //        getActivity(), imageIntent.getData(), orientationColumn);
            }
        }
    }

}
