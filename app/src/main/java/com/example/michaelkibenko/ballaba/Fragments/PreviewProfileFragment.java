package com.example.michaelkibenko.ballaba.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class PreviewProfileFragment extends DialogFragment {

    private TextView userNameTV, aboutTV, professionTV, maritalStatusTV;
    private ImageView instagramIV, twitterIV, linkedinIV, facebookIV, emptyStateIV;
    private RelativeLayout maritalStatusAndWorkContainer;
    private LinearLayout socialContainer, maritalStatusContainer, professionContainer;
    private View view;
    private CircleImageView profileImage;
    private View separator;
    private Context context;
    private String personalMaritalStatus;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        RelativeLayout root = new RelativeLayout(context);
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final Dialog dialog = new Dialog(context);
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

        Bundle bundle = getArguments();
        if (bundle != null) {
            String userID = bundle.getString("USER_ID", null);
            getUserDetails(userID);
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
        professionTV = view.findViewById(R.id.preview_dialog_job_title_text_view);
        maritalStatusTV = view.findViewById(R.id.preview_dialog_family_status_text_view);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, v.getTag() + "", Toast.LENGTH_SHORT).show();
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

        emptyStateIV = view.findViewById(R.id.preview_dialog_user_no_about_image_view);
        maritalStatusAndWorkContainer = view.findViewById(R.id.preview_dialog_user_marital_and_work_container);
        socialContainer = view.findViewById(R.id.preview_dialog_links_container);
        professionContainer = view.findViewById(R.id.preview_dialog_job_container);
        maritalStatusContainer = view.findViewById(R.id.preview_dialog_family_status_container);
    }

    private void getUserDetails(String id) {
        ConnectionsManager.getInstance(context).getUserProfile(id, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                Log.d("getUserDetails", "resolve: " + ((BallabaOkResponse) entity).body);
                parseUserDetails(((BallabaOkResponse) entity).body);
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.d("getUserDetails", "reject: " + entity);
                emptyStateIV.setVisibility(View.VISIBLE);
            }
        });
    }

    private void parseUserDetails(String response) {
        try {
            JSONObject object = new JSONObject(response);
            String userImage = object.getString("profile_image");
            Glide.with(context).load(userImage).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    profileImage.setImageDrawable(context.getResources().getDrawable(R.drawable.profile_grey_100));
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    profileImage.setImageDrawable(resource);
                    return true;
                }
            }).into(profileImage);

            String firstName = object.getString("first_name");
            String lastName = object.getString("last_name");
            userNameTV.setText(StringUtils.getInstance(true).formattedHebrew(firstName) + " " + StringUtils.getInstance(true).formattedHebrew(lastName));

            String profession = object.getString("profession");
            if (!profession.equals("null")) {
                professionTV.setText(StringUtils.getInstance(true).formattedHebrew(profession));
                emptyStateIV.setVisibility(View.VISIBLE);
            } else {
                emptyStateIV.setVisibility(View.GONE);
            }

            String about = object.getString("about");
            if (!about.equals("null") && !about.equals("")) {
                emptyStateIV.setVisibility(View.GONE);
                aboutTV.setText(StringUtils.getInstance(true).formattedHebrew(about));
            } else {
                emptyStateIV.setVisibility(View.VISIBLE);
            }

            String no_of_kids = object.getString("no_of_kids");

            JSONObject social = object.getJSONObject("social");
            if (!social.equals("null")) {
                socialContainer.setVisibility(View.VISIBLE);
                String facebook_token = social.getString("facebook_token");
                facebookIV.setVisibility(facebook_token.equals("null") ? View.GONE : View.VISIBLE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 16);
                facebookIV.setLayoutParams(params);

                String twitter_token = social.getString("twitter_token");
                twitterIV.setVisibility(twitter_token.equals("null") ? View.GONE : View.VISIBLE);
                params.setMargins(0, 0, 32, 16);
                twitterIV.setLayoutParams(params);

                String linkedin_token = social.getString("linkedin_token");
                linkedinIV.setVisibility(linkedin_token.equals("null") ? View.GONE : View.VISIBLE);
                linkedinIV.setLayoutParams(params);

                String instagram_token = social.getString("instagram_token");
                instagramIV.setVisibility(instagram_token.equals("null") ? View.GONE : View.VISIBLE);
                instagramIV.setLayoutParams(params);

            } else {
                socialContainer.setVisibility(View.VISIBLE);
            }

            String maritalStatus = object.getString("marital_status");

            if (maritalStatus != "null") {
                personalMaritalStatus = convertUserDataToEnglish(maritalStatus);
                maritalStatusAndWorkContainer.setVisibility(View.VISIBLE);
                professionContainer.setVisibility(View.VISIBLE);
                maritalStatusTV.setVisibility(View.VISIBLE);
                maritalStatusContainer.setVisibility(View.VISIBLE);
                separator.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                maritalStatusTV.setLayoutParams(params);
                maritalStatusTV.setText(personalMaritalStatus + " +" + no_of_kids);
            } else {
                maritalStatusAndWorkContainer.setVisibility(View.GONE);
                separator.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String convertUserDataToEnglish(String familyStatus) {
        switch (familyStatus) {
            case "single":
                personalMaritalStatus = "רווק/ה";
                break;
            case "married":
                personalMaritalStatus = "נשוי/ה";
                break;
            case "divorced":
                personalMaritalStatus = "גרוש/ה";
                break;
            case "widowed":
                personalMaritalStatus = "אלמן/ה";
                break;
        }
        return personalMaritalStatus;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
