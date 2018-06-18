package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageIntent) {
        super.onActivityResult(requestCode, resultCode, imageIntent);

        if (resultCode == RESULT_OK && imageIntent != null && imageIntent.getExtras().get("data") != null) {
            if (requestCode == REQUEST_CODE_CAMERA) {

                String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};

                AddPropEditPhotoFrag editPhotoFrag = new AddPropEditPhotoFrag();

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                Bitmap bitmap = (Bitmap) imageIntent.getExtras().get("data");
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "Title", null);

                Bundle bundle = new Bundle();
                bundle.putString(AddPropEditPhotoFrag.FIRST_PHOTO, Uri.parse(path).toString());
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

}
