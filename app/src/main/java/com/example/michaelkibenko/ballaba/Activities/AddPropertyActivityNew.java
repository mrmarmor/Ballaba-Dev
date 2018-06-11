package com.example.michaelkibenko.ballaba.Activities;

import android.app.ProgressDialog;
import android.net.Uri;
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
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;

import org.json.JSONObject;

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
    private void uploadPhoto(final AddPropertyActivityNew activity, final ConnectionsManager conn) {
        Fragment editPhotoFragment = getSupportFragmentManager().getFragments().get(0);
        Bundle b = editPhotoFragment.getArguments();
        if (b != null && b.containsKey(AddPropEditPhotoFrag.FIRST_PHOTO)) {
            Log.d(TAG, "only first photo");
            photo = new BallabaPropertyPhoto(Uri.parse(b.getString(AddPropEditPhotoFrag.FIRST_PHOTO)));
            b.remove(AddPropEditPhotoFrag.FIRST_PHOTO);
        } else if (b != null && b.containsKey(AddPropEditPhotoFrag.LAST_PHOTO)) {
            Log.d(TAG, "last photo");
            photo = (BallabaPropertyPhoto) b.getSerializable(AddPropEditPhotoFrag.LAST_PHOTO);
            b.remove(AddPropEditPhotoFrag.LAST_PHOTO);
        }

        if (photo == null) {
            //TODO receive photo from fragment when he clicked "end" with only first photo
            Log.d(TAG, "photo is null");
            return;
        }

        //if (photosJson == USER_HAS_NOT_SWITCHED_TAG_FOR_HIS_PHOTO || photo == null) {
        if (photo.getTags().isEmpty()) {
            UiUtils.instance(true, activity).showSnackBar(container, "לא נבחר חדר");
        } else {
            //if (photo.getId() == PHOTO_HAS_NOT_BEEN_SENT) {
            pd = activity.getDefaultProgressDialog(activity, "Uploading...");
            pd.show();

            conn.uploadProperty(photosJson, new BallabaResponseListener() {
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
        pageNumTV.setText(number + "/4");
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

  /*  public interface AddPropFinishListener {
        void onFinish();
    }*/
}