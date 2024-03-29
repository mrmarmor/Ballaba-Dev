package com.example.michaelkibenko.ballaba.Activities;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Adapters.AddPropertyPhotoRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyPhoto;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Fragments.AddProperty.AddPropEditPhotoFrag;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Presenters.AddPropertyPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;

import org.json.JSONObject;

public class AddPropertyActivity extends BaseActivityWithActionBar
        implements AddPropertyPhotoRecyclerAdapter.AddPropPhotoFinishListener {

    private final static String TAG = AddPropertyActivity.class.getSimpleName();

    private ActivityAddPropertyBinding binder;
    private ProgressDialog pd;
    public BallabaUser user;
    private JSONObject photosJson;
    private BallabaPropertyPhoto photo;
    private final AddPropertyActivity activity = this;
    private boolean isSelectedTagForPhoto = false;
    //private AddPropFinishListener listener;

    /*public AddPropertyActivity(AddPropFinishListener listener){
        this.listener = listener;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_add_property);
        setContentView(R.layout.activity_add_property);
        //binder.setPresenter(new AddPropertyPresenter(this, binder));
        user = BallabaUserManager.getInstance().getUser();

        //UiUtils.instance(true, this).hideSoftKeyboard(binder.getRoot());
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setBackgroundDrawable(
        //        new ColorDrawable(getResources().getColor(R.color.colorPrimary, getTheme())));

    }

    //Here we add in the left corner of the actionbar a counter of pages
    private final int PROPERTY_DATA = 0, PROPERTY_TAKE_PHOTO = 4
            , PROPERTY_EDIT_PHOTO = 5, PROPERTY_MEETINGS = 6;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        TextView pagesCounterTv = new TextView(this);
        pagesCounterTv.setPadding(16, 0, 16, 0);

        final int PAGE_NUMBER = binder.addPropertyViewPager.getCurrentItem();
        if (PAGE_NUMBER < 4) {//page counter should be displayed only in first 4 screens
            menu.add(0, PROPERTY_DATA, 1, (PAGE_NUMBER + 1) + "/4")
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else if (PAGE_NUMBER == 4) {
            menu.add(0, PROPERTY_TAKE_PHOTO, 1, "דילוג")
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else if (PAGE_NUMBER == 5) {
            menu.add(0, PROPERTY_EDIT_PHOTO, 1, getString(R.string.addProperty_editPhoto_finish))
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else if (PAGE_NUMBER == 6) {
            menu.add(0, PROPERTY_MEETINGS, 1, getString(R.string.addProperty_editPhoto_finish))
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        String label = getResources().getStringArray(R.array.addProperty_titles)[PAGE_NUMBER];
        getSupportActionBar().setTitle(label);

        return true;
    }

    //Here we add a back button at the right corner of the actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                int pageNumber = binder.addPropertyViewPager.getCurrentItem();
                if (pageNumber == 0) {
                    super.onBackPressed();
                } else {
                    binder.addPropertyViewPager.setCurrentItem(pageNumber - 1, false);
                    invalidateOptionsMenu();
                }
                return true;

            case PROPERTY_TAKE_PHOTO:
                AddPropertyPresenter.getInstance(activity, binder).setViewPagerItem(5);
                return true;

            case PROPERTY_EDIT_PHOTO:
                uploadPhoto(activity, ConnectionsManager.getInstance(activity));
                AddPropertyPresenter.getInstance(activity, binder).setViewPagerItem(6);
                return true;

            case PROPERTY_MEETINGS:
                Toast.makeText(activity, "finish", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //when user clicks finish button on actionbar, last photo that hasn't sent yet, is sent now to server
    private void uploadPhoto(final AddPropertyActivity activity, final ConnectionsManager conn){
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

        if (photo == null){
            //TODO receive photo from fragment when he clicked "end" with only first photo
            Log.d(TAG, "photo is null");
            return;
        }

        //if (photosJson == USER_HAS_NOT_SWITCHED_TAG_FOR_HIS_PHOTO || photo == null) {
        if (photo.getTags().isEmpty()) {
            UiUtils.instance(true, activity).showSnackBar(binder.getRoot(), "לא נבחר חדר");
        } else {
            //if (photo.getId() == PHOTO_HAS_NOT_BEEN_SENT) {
            pd = activity.getDefaultProgressDialog(activity, "Uploading...");
            pd.show();

            conn.uploadProperty(photosJson, new BallabaResponseListener() {
                @Override
                public void resolve(BallabaBaseEntity entity) {
                    pd.dismiss();
                    Log.d(TAG, "upload property photo: success");
                    photo.setId(((BallabaPropertyPhoto)entity).getId());
                    photo.setHasSent(true);
                    AddPropertyPresenter.getInstance(activity, binder).setViewPagerItem(6);
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

    @Override
    public void onFinish(JSONObject jsonObject) {
        this.photosJson = jsonObject;
        isSelectedTagForPhoto = true;
    }

  /*  public interface AddPropFinishListener {
        void onFinish();
    }*/
}