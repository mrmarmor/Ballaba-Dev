package com.example.michaelkibenko.ballaba.Fragments.AddProperty;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michaelkibenko.ballaba.Activities.AddPropertyActivityNew;
import com.example.michaelkibenko.ballaba.R;

public class OnBoardingFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;

    public static final String STATE = "STATE";

    public @interface STATE_TYPES {
        int AGREEMENT = 1;
        int PHOTO = 2;
    }

    private @OnBoardingFragment.STATE_TYPES
    int currentType = STATE_TYPES.AGREEMENT;
    private int currentLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AddPropertyActivityNew) context).showToolbar(false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            currentType = bundle.getInt(STATE);
        }
        changeState(currentType);

        view = inflater.inflate(currentLayout, container, false);

        view.findViewById(R.id.fragment_agreement_on_boarding_next_button).setOnClickListener(this);
        view.findViewById(R.id.fragment_agreement_on_boarding_back_image_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }

    private void changeState(int type) {
        switch (type) {
            case STATE_TYPES.AGREEMENT:
                currentLayout = R.layout.activity_agreement_on_boarding;
                break;
            case STATE_TYPES.PHOTO:
                currentLayout = R.layout.activity_photo_on_boarding;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_agreement_on_boarding_next_button:
                switch (currentType) {
                    case STATE_TYPES.AGREEMENT:
                        ((AddPropertyActivityNew) context).changeFragment(new AddPropLandlordFrag(), true);
                        break;
                    case STATE_TYPES.PHOTO:

                        break;
                }
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
