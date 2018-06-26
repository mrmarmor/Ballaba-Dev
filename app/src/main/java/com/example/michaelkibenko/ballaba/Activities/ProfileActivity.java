package com.example.michaelkibenko.ballaba.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityProfileBinding;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.instagram.instagramapi.activities.InstagramAuthActivity;
import com.instagram.instagramapi.engine.InstagramEngine;
import com.instagram.instagramapi.engine.InstagramKitConstants;
import com.instagram.instagramapi.exceptions.InstagramException;
import com.instagram.instagramapi.interfaces.InstagramLoginCallbackListener;
import com.instagram.instagramapi.objects.IGSession;
import com.instagram.instagramapi.utils.InstagramKitLoginScope;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropLandlordFrag.REQUEST_CODE_CAMERA;
import static com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropLandlordFrag.REQUEST_CODE_GALLERY;

public class ProfileActivity extends BaseActivityWithActionBar implements View.OnClickListener, TextWatcher {
    private final int REQUEST_CODE_CREDIT_CARD = 3;
    private final String TAG = ProfileActivity.class.getSimpleName();

    private ActivityProfileBinding binder;
    private CallbackManager faceBookCallbackManager;
    private View sheetView;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        BallabaUser user = BallabaUserManager.getInstance().getUser();
        if (user != null) {
            initViews(user);
            initSocials(user.getSocial());
        }

//        getPackageHash();

        //if we want edit mode not as default:
        /*binder.profileActivityFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binder.profileActivityImageAvatar.setEnabled(true);
                binder.profileActivityDetailsFullName.setEnabled(true);
                binder.profileActivityDetailsDateOfBirth.setEnabled(true);
                binder.profileActivityDetailsProfession.setEnabled(true);
                binder.profileActivityStatusSpinner.setEnabled(true);
                binder.profileActivityChildrenSpinner.setEnabled(true);
                binder.profileActivityDetailsAddress.setEnabled(true);
                binder.profileActivityDetailsEmail.setEnabled(true);
                binder.profileActivityDetailsAbout.setEnabled(true);
                binder.profileActivityDetailsCreditCard.setEnabled(true);
            }
        });*/
    }

    private void initViews(final BallabaUser user) {
            Glide.with(this).load(user.getProfile_image()).into(binder.profileActivityImageAvatar);
            binder.profileActivityDetailsFirstName.setText(user.getFirst_name());
            binder.profileActivityDetailsLastName.setText(user.getLast_name());
            binder.profileActivityDetailsPhone.setText(user.getPhone());
            binder.profileActivityDetailsDateOfBirth.setText(user.getBirth_date());
            binder.profileActivityDetailsId.setText(user.getId_number());
            binder.profileActivityDetailsProfession.setText(user.getProfession());
            binder.profileActivityStatusSpinner.setPrompt(user.getMarital_status());//TODO testing
            binder.profileActivityChildrenSpinner.setPrompt(user.getNo_of_kids());//TODO testing
            binder.profileActivityDetailsCity.setText(user.getCity());
            binder.profileActivityDetailsAddress.setText(user.getAddress());
            binder.profileActivityDetailsStreetNo.setText(user.getStreet_number());
            binder.profileActivityDetailsAptNo.setText(user.getApt_no());
            binder.profileActivityDetailsAddress.setText(user.getAddress());
            binder.profileActivityDetailsEmail.setText(user.getEmail());
            binder.profileActivityDetailsAbout.setText(user.getAbout());
            binder.profileActivityDetailsCreditCard.setText(user.getLast_4_digits() + " - **** - **** - ****");
            binder.profileActivityDetailsAboutCounter.setText(user.getAbout().length() + " / 120");

        binder.profileActivityDetailsAbout.addTextChangedListener(this);
        binder.profileActivityButtonNext.setOnClickListener(this);
        //binder.profileActivitySocialFacebookIV.setOnClickListener(this);
        UiUtils.instance(true, this).initAutoCompleteCity(binder.profileActivityDetailsCity);
        UiUtils.instance(true, this).initAutoCompleteAddressInCity(binder.profileActivityDetailsAddress, binder.profileActivityDetailsCity);
    }

    private void initSocials(final HashMap<String, String> social) {
        String facebookToken = social.get("facebook_token");
        String twitterToken = social.get("twitter_token");
        String linkenidToken = social.get("linkedin_token");
        String instagramToken = social.get("instagram_token");

        if (facebookToken != null)
            initFaceBook();

        if (twitterToken != null)
            initTwitter();

        if (linkenidToken != null)
            initLinkedIn();

        if (instagramToken != null)
            initInstagram();
    }

    private void initFaceBook() {
        binder.profileActivitySocialFacebookIV.setVisibility(View.VISIBLE);

        faceBookCallbackManager = CallbackManager.Factory.create();
        binder.profileActivitySocialFacebookIV.setReadPermissions("email");
        binder.profileActivitySocialFacebookIV.registerCallback(faceBookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, loginResult.toString());
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "error");

            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, error.toString());
            }
        });
    }

    private void initLinkedIn() {
        binder.profileActivitySocialLinkedinImageView.setVisibility(View.VISIBLE);

        binder.profileActivitySocialLinkedinImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LISessionManager.getInstance(getApplicationContext()).init(ProfileActivity.this, buildLinkedInScope()//pass the build scope here
                        , new AuthListener() {
                            @Override
                            public void onAuthSuccess() {
                                // Authentication was successful. You can now do
                                // other calls with the SDK.
//                                Toast.makeText(ProfileActivity.this, "Successfully authenticated with LinkedIn.", Toast.LENGTH_SHORT).show();
//                                LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken();
                                fetchBasicLinkedInProfileData();
                            }

                            @Override
                            public void onAuthError(LIAuthError error) {
                                Log.e(TAG, error.toString());
                                // Handle authentication errors
//                                Log.e(TAG, "Auth Error :" + error.toString());
//                                Toast.makeText(ProfileActivity.this, "Failed to authenticate with LinkedIn. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }, true);
                //if TRUE then it will show dialog if
                // any device has no LinkedIn app installed to download app else won't show anythin
            }
        });
    }

    private void fetchBasicLinkedInProfileData() {

        //In URL pass whatever data from user you want for more values check below link
        //LINK : https://developer.linkedin.com/docs/fields/basic-profile
        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,headline,public-profile-url,picture-url,email-address,picture-urls::(original))";

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!
                Log.d(TAG, "API Res : " + apiResponse.getResponseDataAsString() + "\n" + apiResponse.getResponseDataAsJson().toString());
                Toast.makeText(ProfileActivity.this, "Successfully fetched LinkedIn profile data.", Toast.LENGTH_SHORT).show();

                //update UI on successful data fetched
//                updateUI(apiResponse);
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!
                Log.e(TAG, "Fetch profile Error   :" + liApiError.getLocalizedMessage());
                Toast.makeText(ProfileActivity.this, "Failed to fetch basic profile data. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void doLinkedInLogout(View view) {
        LISessionManager.getInstance(this).clearSession();
    }

    private void getPackageHash() {
        try {

            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.michaelkibenko.ballaba",//give your package name here
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.e(TAG, "Hash  : " + Base64.encodeToString(md.digest(), Base64.NO_WRAP));//Key hash is printing in Log
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, e.getMessage(), e);
        }

    }

    private Scope buildLinkedInScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE, Scope.R_EMAILADDRESS);
    }

    private void initTwitter() {
        binder.profileActivitySocialTwitterIV.setVisibility(View.VISIBLE);

        binder.profileActivitySocialTwitterIV.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.e(TAG, result.toString());
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e(TAG, exception.toString());
            }
        });
    }

    private void initInstagram() {
        binder.profileActivitySocialInstagramIV.setVisibility(View.VISIBLE);

        String[] scopes = {InstagramKitLoginScope.BASIC};
        binder.profileActivitySocialInstagramIV.setScopes(scopes);
        binder.profileActivitySocialInstagramIV.setInstagramLoginCallback(new InstagramLoginCallbackListener() {
            @Override
            public void onSuccess(IGSession result) {
                Log.e(TAG, result.toString());
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "On Cancel");
            }

            @Override
            public void onError(InstagramException error) {
                Log.e(TAG, "error");
            }
        });
    }

    public void onClickProfileImage(View view) {
        //if (binder.profileActivityDetailsFullName.isEnabled()) {//=edit mode
        sheetView = getLayoutInflater().inflate(R.layout.take_pic_switch, null);
        sheetView.findViewById(R.id.takePic_button_camera).setOnClickListener(this);
        sheetView.findViewById(R.id.takePic_button_gallery).setOnClickListener(this);

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
        //}
    }

    public void onClickToCreditCard(View view) {
        startActivityForResult(new Intent(this, CreditCardActivity.class), REQUEST_CODE_CREDIT_CARD);
    }

    private boolean isEmailValid(String email) {
        boolean isValid = !TextUtils.isEmpty(email) && (Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isValid) {
            binder.profileActivityDetailsEmail.setError("כתובת דואר אלקטרוני שגויה");
            ((ScrollView) binder.getRoot()).smoothScrollTo(0, binder.profileActivityDetailsEmail.getTop() - 30);
            binder.profileActivityDetailsEmail.requestFocus();
        }

        return isValid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.takePic_button_camera:
                takePicture(Manifest.permission.CAMERA, REQUEST_CODE_CAMERA, new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
                break;

            case R.id.takePic_button_gallery:
                takePicture(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_CODE_GALLERY
                        , new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
                break;

            case R.id.profileActivity_social_instagramIV:
                Toast.makeText(this, "to instagram personal page", Toast.LENGTH_SHORT).show();
                break;

            case R.id.profileActivity_social_twitter_IV:
                Toast.makeText(this, "to twitter personal page", Toast.LENGTH_SHORT).show();
                break;

            case R.id.profileActivity_social_linkedin_image_view:
                Toast.makeText(this, "to linkenin personal page", Toast.LENGTH_SHORT).show();
                break;

            case R.id.profileActivity_button_next:
                if (isEmailValid(binder.profileActivityDetailsEmail.getText().toString()))
                    onFinish(ConnectionsManager.newInstance(this), getDataFromEditTexts(new JSONObject()));
                break;
        }
    }

    private void takePicture(String permissionCode, int requestCode, Intent intent) {
        if (ContextCompat.checkSelfPermission(this, permissionCode) == PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(new String[]{permissionCode}, requestCode);
        } else {
            startActivityForResult(intent, requestCode);
            bottomSheetDialog.dismiss();
        }
    }

    private void onFinish(ConnectionsManager conn, JSONObject jsonObject) {
        try {
            conn.uploadUser(jsonObject, new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    getDefaultSnackBar(binder.getRoot(), "פרטי משתמש נשמרו בהצלחה", false).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);
                            finish();
                        }
                    }).show();
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    getDefaultSnackBar(binder.getRoot(), ((BallabaErrorResponse) entity).message, false).show();
                }
            });
        } catch (JSONException e) {
            e.getStackTrace();
        }
    }

    private JSONObject getDataFromEditTexts(JSONObject jsonObject) {
        try {
            byte[] bytes = UiUtils.instance(true, this).drawableToUri(binder.profileActivityImageAvatar.getDrawable());
            jsonObject.put("profile_image", bytes);
            jsonObject.put(binder.profileActivityDetailsFirstName.getTag() + "", binder.profileActivityDetailsFirstName.getText());
            jsonObject.put(binder.profileActivityDetailsLastName.getTag() + "", binder.profileActivityDetailsLastName.getText());
            jsonObject.put(binder.profileActivityDetailsPhone.getTag() + "", binder.profileActivityDetailsPhone.getText());
            jsonObject.put(binder.profileActivityDetailsDateOfBirth.getTag() + "", binder.profileActivityDetailsDateOfBirth.getText());
            jsonObject.put(binder.profileActivityDetailsId.getTag() + "", binder.profileActivityDetailsId.getText());
            jsonObject.put(binder.profileActivityDetailsProfession.getTag() + "", binder.profileActivityDetailsProfession.getText());
            jsonObject.put(binder.profileActivityStatusSpinner.getTag() + "", binder.profileActivityStatusSpinner.getPrompt());
            jsonObject.put(binder.profileActivityChildrenSpinner.getTag() + "", binder.profileActivityChildrenSpinner.getPrompt());
            jsonObject.put(binder.profileActivityDetailsCity.getTag() + "", binder.profileActivityDetailsCity.getText());
            jsonObject.put(binder.profileActivityDetailsAddress.getTag() + "", binder.profileActivityDetailsAddress.getText());
            jsonObject.put(binder.profileActivityDetailsStreetNo.getTag() + "", binder.profileActivityDetailsStreetNo.getText());
            jsonObject.put(binder.profileActivityDetailsAptNo.getTag() + "", binder.profileActivityDetailsAptNo.getText());
            jsonObject.put(binder.profileActivityDetailsEmail.getTag() + "", binder.profileActivityDetailsEmail.getText());
            jsonObject.put(binder.profileActivityDetailsAbout.getTag() + "", binder.profileActivityDetailsAbout.getText());
            jsonObject.put(binder.profileActivityDetailsFirstName.getTag() + "", binder.profileActivityDetailsFirstName.getText());
            jsonObject.put(binder.profileActivityDetailsFirstName.getTag() + "", binder.profileActivityDetailsFirstName.getText());
            return jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        binder.profileActivitySocialTwitterIV.onActivityResult(requestCode, resultCode, intent);
        if (faceBookCallbackManager != null) {
            faceBookCallbackManager.onActivityResult(requestCode, resultCode, intent);
        }
        if (LISessionManager.getInstance(getApplicationContext()) != null) {
            LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, intent);
        }
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK && intent != null) {
            Uri selectedImage = intent.getData();

            switch (requestCode) {
                case REQUEST_CODE_CAMERA:
                case REQUEST_CODE_GALLERY:
                    binder.profileActivityImageAvatar.setImageURI(selectedImage);
                    break;

                case REQUEST_CODE_CREDIT_CARD:
                    binder.profileActivityDetailsCreditCard.setText(generateHiddenCreditCardNumber(intent));
                    break;
            }
        }
    }

    public String generateHiddenCreditCardNumber(Intent intent) {
        Bundle b = intent.getExtras();
        if (b != null && !b.isEmpty()) {
            int creditCardNumberLength = b.getInt(CreditCardActivity.CREDIT_CARD_NUMBER_LENGTH);
            String creditCardLast4Digits = b.getString(CreditCardActivity.CREDIT_CARD_NUMBER_LAST_4_DIGITS);
            //String hiddenNumber = "";
            StringBuilder hiddenNumber = new StringBuilder(20);
            for (int i = 0; i < creditCardNumberLength - 4; i++) {
                hiddenNumber.append("*");
                if ((i + 1) % 4 == 0)
                    hiddenNumber.append("-");
            }
            hiddenNumber.insert(hiddenNumber.length(), creditCardLast4Digits);

            return hiddenNumber.reverse().toString();
        }

        return "";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sheetView.findViewById(R.id.takePic_button_camera).performClick();
            }
        } else if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sheetView.findViewById(R.id.takePic_button_gallery).performClick();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        binder.profileActivityDetailsAboutCounter.setText(s.length() + " / 120");

    }


}
