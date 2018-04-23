package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Adapters.AddPropertyPagerAdapter;
import com.example.michaelkibenko.ballaba.Common.BallabaFragmentListener;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropAddonsFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropAssetFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropLandlordFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropPaymentsFrag;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
//import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropAddonsBinding;
//import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropAssetBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropLandlordBinding;
//import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropPaymentsBinding;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 22/04/2018.
 */

public class AddPropPresenter implements Button.OnClickListener, BallabaFragmentListener{
    private static String TAG = AddPropPresenter.class.getSimpleName();

    private AppCompatActivity activity;
    private ActivityAddPropertyBinding binder;
    private FragmentAddPropLandlordBinding binderLandlord;
    /*private FragmentAddPropAssetBinding binderAsset;
    private FragmentAddPropAddonsBinding binderAsset;
    private FragmentAddPropPaymentsBinding binderPay;*/
    private ViewGroup root;
    private BottomSheetDialog bottomSheetDialog;
    private AddPropertyPagerAdapter addPropertyPagerAdapter;
    private final Fragment[] FRAGMENTS = new Fragment[]{new AddPropLandlordFrag(), new AddPropAssetFrag()
            , new AddPropAddonsFrag(), new AddPropPaymentsFrag()};
    private HashMap<String, String> data = new HashMap<>();

    public AddPropPresenter(AppCompatActivity activity) {
        this.activity = activity;
        //this.root = binder.addPropertyRoot;
        binderLandlord = DataBindingUtil.inflate(activity.getLayoutInflater(), R.layout.fragment_add_prop_landlord,null , false);

        binder.addPropertyLandlordButtonNext.setOnClickListener(this);
        addPropertyPagerAdapter = new AddPropertyPagerAdapter(activity, FRAGMENTS, activity.getSupportFragmentManager());
    }

    /*public void onClickProfileImage(){
        View sheetView = activity.getLayoutInflater().inflate(R.layout.take_pic_switch, null);
        sheetView.findViewById(R.id.takePic_button_camera).setOnClickListener(this);
        sheetView.findViewById(R.id.takePic_button_gallery).setOnClickListener(this);

        bottomSheetDialog = new BottomSheetDialog(activity);
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.takePic_button_camera:
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activity.startActivityForResult(takePicture, AddPropertyActivity.REQUEST_CODE_CAMERA);
                bottomSheetDialog.dismiss();
                break;

            case R.id.takePic_button_gallery:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activity.startActivityForResult(pickPhoto , AddPropertyActivity.REQUEST_CODE_GALLERY);
                bottomSheetDialog.dismiss();
                break;*/

            case R.id.addProperty_landlord_button_next:
                Toast.makeText(activity, "next", Toast.LENGTH_SHORT).show();
                int position = binder.addPropertyViewPager.getCurrentItem();
                switch (position){
                    case 0:
                        data.putAll(((AddPropLandlordFrag)FRAGMENTS[0]).getFieldsData());
                        break;

                    case 1:
                        //data.putAll(((AddPropAssetFrag)FRAGMENTS[1]).getFieldsData());
                        break;

                    case 2:
                        //data.putAll(((AddPropAddonsFrag)FRAGMENTS[2]).getFieldsData());
                        break;

                    case 3: default:
                        //data.putAll(((AddPropPaymentsFrag)FRAGMENTS[3]).getFieldsData());
                }
                //FRAGMENTS[position].
                //storeDataOnFinish();
        }
    }

    @Override
    public void onFragmentInteraction(HashMap<String, String> map) {

    }

    /*private void storeDataOnFinish(){
        Intent intent = new Intent(activity, AddPropertyActivity.class);
        for (int i = 0; i < binder.addPropertyEditTextsRoot.getChildCount(); i++){//root.getChildCount(); i++) {
            View v = binder.addPropertyEditTextsRoot.getChildAt(i);
            if (v instanceof EditText) {
                intent.putExtra(v.getTag()+"", ((EditText)v).getText()+"");
            }

        }

        intent.putExtra(binder.addPropAboutYourselfEditText.getTag()+""
                , binder.addPropAboutYourselfEditText.getText()+"");

        Log.d(TAG, intent.getStringExtra("aboutYourself")+":"+intent.getStringExtra("firstName"));
    }*/

}
