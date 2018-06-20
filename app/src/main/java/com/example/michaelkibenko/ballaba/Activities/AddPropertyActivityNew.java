package com.example.michaelkibenko.ballaba.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Adapters.AddPropertyPhotoRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyPhoto;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropEditPhotoFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropLandlordFrag;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropMeetingsFrag;
import com.example.michaelkibenko.ballaba.Holders.SharedPreferencesKeysHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Managers.SharedPreferencesManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;

import org.json.JSONObject;

import java.util.ArrayList;

public class AddPropertyActivityNew extends BaseActivity
        implements AddPropertyPhotoRecyclerAdapter.AddPropPhotoFinishListener, View.OnClickListener {

    private final static String TAG = AddPropertyActivityNew.class.getSimpleName();

    private ProgressDialog pd;
    public BallabaUser user;
    private JSONObject photosJson;
    private BallabaPropertyPhoto photo;
    private boolean isSelectedTagForPhoto = false;
    private RelativeLayout container;
    private TextView pageNumTV;
    private FragmentManager fm;
    private AddPropEditPhotoFrag addPropEditPhotoFrag;
    private ArrayList<String> tags = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);

        setContentView(R.layout.activity_add_property_new);
        user = BallabaUserManager.getInstance().getUser();

        findViewById(R.id.activity_add_property_back_btn).setOnClickListener(this);

        container = findViewById(R.id.activity_add_property_container);
        pageNumTV = findViewById(R.id.activity_add_property_page_number_text_view);
        changePageIndicatorText(1);
        fm = getSupportFragmentManager();

        fm.beginTransaction().replace(R.id.activity_add_property_frame_layout, new AddPropLandlordFrag()).commit();
    }

    //when user clicks finish button on actionbar, last photo that hasn't sent yet, is sent now to server
    public void uploadPhoto(final AddPropertyActivityNew activity, final ConnectionsManager conn, BallabaPropertyPhoto photo) {
        this.photo = photo;
        Bundle b = addPropEditPhotoFrag.getArguments();
        if (b != null && b.containsKey(AddPropEditPhotoFrag.FIRST_PHOTO)) {
            Log.d(TAG, "only first photo");
            //this.photo = new BallabaPropertyPhoto(b.getByteArray(AddPropEditPhotoFrag.FIRST_PHOTO));
            b.remove(AddPropEditPhotoFrag.FIRST_PHOTO);
        } else if (b != null && b.containsKey(AddPropEditPhotoFrag.LAST_PHOTO)) {
            Log.d(TAG, "last photo");
            //this.photo = (BallabaPropertyPhoto) b.getSerializable(AddPropEditPhotoFrag.LAST_PHOTO);
            b.remove(AddPropEditPhotoFrag.LAST_PHOTO);
        }

        if (this.photo == null) {
            //TODO receive photo from fragment when he clicked "end" with only first photo
            Log.d(TAG, "photo is null");
            return;
        }

        //if (photosJson == USER_HAS_NOT_SWITCHED_TAG_FOR_HIS_PHOTO || photo == null) {
        if (this.photo.getTags().isEmpty()) {
            UiUtils.instance(true, activity).showSnackBar(container, "לא נבחר חדר");
        } else {
            //if (photo.getId() == PHOTO_HAS_NOT_BEEN_SENT) {
            pd = activity.getDefaultProgressDialog(activity, "Uploading...");
            pd.show();

            /*conn.uploadProperty(photosJson, new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    pd.dismiss();
                    Log.d(TAG, "upload property photo: success");
                    photo.setId(((BallabaPropertyPhoto) entity).getId());
                    photo.setHasSent(true);
                    //AddPropertyPresenter.getInstance(activity, binder).setViewPagerItem(6);
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    pd.dismiss();
                    Log.e(TAG, "upload property photo: failure");
                }
            });*/
            String propertyID = SharedPreferencesManager.getInstance(this).getString(SharedPreferencesKeysHolder.PROPERTY_ID, null);
            conn.newUploadProperty(5, propertyID, photosJson, new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    pd.dismiss();
                    Log.d(TAG, "upload property photo: success");
                    AddPropertyActivityNew.this.photo.setId(((BallabaPropertyPhoto) entity).getId());
                    AddPropertyActivityNew.this.photo.setHasSent(true);
                    changeFragment(new AddPropMeetingsFrag(), true);
                    //AddPropertyPresenter.getInstance(activity, binder).setViewPagerItem(6);
                }

                @Override
                public void reject(BallabaBaseEntity entity) {
                    pd.dismiss();
                    Log.e(TAG, "upload property photo: failure");
                }
            });
            //}

        }
    }

    public void changeFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        if (addToBackStack) {
            transaction.addToBackStack(null).replace(R.id.activity_add_property_frame_layout, fragment).commit();
        } else {
            transaction.replace(R.id.activity_add_property_frame_layout, fragment).commit();
        }
    }

    public void changePageIndicatorText(int number) {
        if (number <= 4 && number > 0) {
            pageNumTV.setText(number + "/4");
        } else if (number == 0) {
            pageNumTV.setText("");
        } else {
            pageNumTV.setText("סיום");
        }
    }

    @Override
    public void onBackPressed() {
        int entryCount = fm.getBackStackEntryCount();
        changePageIndicatorText(entryCount == 0 ? 1 : entryCount);
        if (entryCount > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFinish(JSONObject jsonObject) {
        this.photosJson = jsonObject;
        isSelectedTagForPhoto = true;
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
    }

    public void setFinishEnable(AddPropEditPhotoFrag addPropEditPhotoFrag, boolean b , final BallabaPropertyPhoto photo) { //
        // pageNumTV.setVisibility(b ? View.VISIBLE : View.GONE);
        this.addPropEditPhotoFrag = addPropEditPhotoFrag;
        pageNumTV.setOnClickListener(b ? new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto(AddPropertyActivityNew.this, ConnectionsManager.getInstance(AddPropertyActivityNew.this) , photo);
            }
        } : null);
    }
}