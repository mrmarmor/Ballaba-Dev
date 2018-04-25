package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Presenters.AddPropertyPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropAssetBinding;

import java.util.HashMap;

public class AddPropAssetFrag extends Fragment {
    private static final String TAG = AddPropAssetFrag.class.getSimpleName();

    private Context context;
    private static ActivityAddPropertyBinding binderMain;
    private FragmentAddPropAssetBinding binderAsset;

    public AddPropAssetFrag() {}
    public static AddPropAssetFrag newInstance(ActivityAddPropertyBinding binding) {
        AddPropAssetFrag fragment = new AddPropAssetFrag();
        binderMain = binding;
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binderAsset = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_prop_asset, container, false);
        View view = binderAsset.getRoot();
        binderAsset.addPropertyAssetButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> data = storeDataOnFinish(new HashMap<String, String>());
                new AddPropertyPresenter((AppCompatActivity)context, binderMain).getDataFromFragment(data, 1);
            }
        });

        initEditTexts(BallabaSearchPropertiesManager.getInstance(context).getPropertyFull());

        return view;
    }

    private void initEditTexts(BallabaPropertyFull property){
        binderAsset.addPropCityEditText.setText(property.city);
        binderAsset.addPropAddressEditText.setText(property.formattedAddress);
        binderAsset.addPropAptNoEditText.setText(property.street_number);
        binderAsset.addPropFloorEditText.setText(property.floor);
        binderAsset.addPropMaxFloorEditText.setText(property.max_floor);
        binderAsset.addPropRoomsEditText.setText(property.roomsNumber);
        binderAsset.addPropToiletsEditText.setText(property.toilets);
        binderAsset.addPropBathroomsEditText.setText(property.bathrooms);
        binderAsset.addPropSizeEditText.setText(property.size);
    }

    private HashMap<String, String> storeDataOnFinish(HashMap<String, String> map){
        for (int i = 0; i < binderAsset.addPropertyLocationRoot.getChildCount(); i++){//root.getChildCount(); i++) {
            View v = binderAsset.addPropertyLocationRoot.getChildAt(i);
            if (v instanceof EditText) {
                map.put(v.getTag()+"", ((EditText)v).getText()+"");
            }

        }

        return map;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

}