package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.AddPropertyActivityNew;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

public class AddPropTakePhotoFrag extends Fragment implements View.OnClickListener {
    private final String TAG = AddPropTakePhotoFrag.class.getSimpleName();
    public static final int REQUEST_CODE_CAMERA = 1;

    private ActivityAddPropertyBinding binderMain;
    private int WRITE_EXTERNAL_STORAGE_PERMISSION = 123;

    public AddPropTakePhotoFrag() {
    }

    public static AddPropTakePhotoFrag newInstance() {
        AddPropTakePhotoFrag fragment = new AddPropTakePhotoFrag();
        return fragment;
    }

    public AddPropTakePhotoFrag setMainBinder(ActivityAddPropertyBinding binder) {
        this.binderMain = binder;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_prop_take_photo, container, false);
        ((AddPropertyActivityNew) getActivity()).changePageIndicatorText(-1);

        UiUtils.instance(true, getActivity()).hideSoftKeyboard(v);
        v.findViewById(R.id.addProp_takePhoto_button_professionalPhotographer).setOnClickListener(this);
        v.findViewById(R.id.addProp_takePhoto_button_takePhoto).setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.addProp_takePhoto_button_professionalPhotographer) {
            Toast.makeText(getActivity(), "taking Professional Photographer", Toast.LENGTH_SHORT).show();
        } else {
            //AddPropertyPresenter.getInstance((AppCompatActivity) getActivity(), binderMain).onNextViewPagerItem(4);
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                //File write logic here
            }else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageIntent) {
        super.onActivityResult(requestCode, resultCode, imageIntent);

        if (resultCode == RESULT_OK && imageIntent != null && imageIntent.getExtras().get("data") != null) {
            if (requestCode == REQUEST_CODE_CAMERA) {

                String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};

                AddPropEditPhotoFrag editPhotoFrag = new AddPropEditPhotoFrag();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Bitmap bitmap = (Bitmap) imageIntent.getExtras().get("data");
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                byte[] bytes = byteArrayOutputStream.toByteArray();

                Bundle bundle = new Bundle();
                bundle.putByteArray(AddPropEditPhotoFrag.FIRST_PHOTO, bytes);
                bundle.putStringArray(AddPropEditPhotoFrag.ORIENTATIONS, orientationColumn);
                editPhotoFrag.setArguments(bundle);

                ((AddPropertyActivityNew) getActivity()).changeFragment(editPhotoFrag, true);
                /*((AddPropertyPagerAdapter)binderMain.addPropertyViewPager.getAdapter())
                        .setData(imageIntent.getData(), orientationColumn);
                AddPropertyPresenter.getInstance((AppCompatActivity)getActivity(), binderMain).setViewPagerItem(5);*/
                //AddPropEditPhotoFrag.newInstance(binderMain).addPhoto(
                //        getActivity(), imageIntent.getData(), orientationColumn);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                //TODO do not permitted state
            }
            return;
        }
    }
}
