package com.example.michaelkibenko.ballaba.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PreviewProfileFragment extends DialogFragment {

    private TextView userNameTV, aboutTV, jobTV, familyStatusTV;
    private ImageView instagramIV, twitterIV, linkedinIV, facebookIV;
    private View view;
    private CircleImageView profileImage;
    private View separator;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.preview_profile_layout, container, false);

        view.findViewById(R.id.preview_dialog_container_main_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findView();


        BallabaUser user = BallabaUserManager.getInstance().getUser();
        if (user != null) {
            Glide.with(getActivity()).load(user.getProfile_image()).into(profileImage);
        }

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        return view;
    }

    private void findView() {

        separator = view.findViewById(R.id.preview_dialog_status_separator);
        profileImage = view.findViewById(R.id.preview_dialog_profile_circle_image_view);
        userNameTV = view.findViewById(R.id.preview_dialog_user_name_text_view);
        aboutTV = view.findViewById(R.id.preview_dialog_user_description_text_view);
        jobTV = view.findViewById(R.id.preview_dialog_job_title_text_view);
        familyStatusTV = view.findViewById(R.id.preview_dialog_family_status_text_view);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), v.getTag() + "", Toast.LENGTH_SHORT).show();
            }
        };

        instagramIV = view.findViewById(R.id.preview_dialog_instagram_image_view);
        instagramIV.setOnClickListener(onClickListener);
        twitterIV = view.findViewById(R.id.preview_dialog_twitter_image_view);
        twitterIV.setOnClickListener(onClickListener);
        linkedinIV = view.findViewById(R.id.preview_dialog_linkedin_image_view);
        linkedinIV.setOnClickListener(onClickListener);
        facebookIV = view.findViewById(R.id.preview_dialog_facebook_image_view);
        facebookIV.setOnClickListener(onClickListener);


    }
}
