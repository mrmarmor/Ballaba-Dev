package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropAddonsBinding;
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropAssetBinding;

public class AddPropAddonsFrag extends Fragment {
    private static ActivityAddPropertyBinding binderMain;
    private FragmentAddPropAddonsBinding binderAddons;

    public AddPropAddonsFrag() {}
    public static AddPropAddonsFrag newInstance(ActivityAddPropertyBinding binding) {
        AddPropAddonsFrag fragment = new AddPropAddonsFrag();
        binderMain = binding;
        /* Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binderAddons = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_prop_addons, container, false);
        View view = binderAddons.getRoot();

        //initButtons(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

}
