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
import com.example.michaelkibenko.ballaba.databinding.FragmentAddPropPaymentsBinding;

public class AddPropPaymentsFrag extends Fragment {
    private static ActivityAddPropertyBinding binderMain;
    private FragmentAddPropPaymentsBinding binderPay;

    public AddPropPaymentsFrag() {}
    public static AddPropPaymentsFrag newInstance(ActivityAddPropertyBinding binding) {
        AddPropPaymentsFrag fragment = new AddPropPaymentsFrag();
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
        binderPay = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_prop_payments, container, false);
        View view = binderPay.getRoot();

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
