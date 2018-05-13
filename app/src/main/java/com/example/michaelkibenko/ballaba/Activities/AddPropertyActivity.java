package com.example.michaelkibenko.ballaba.Activities;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Adapters.AddPropertyPhotoRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.Presenters.AddPropertyPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;

import org.json.JSONObject;

public class AddPropertyActivity extends AppCompatActivity
        implements AddPropertyPhotoRecyclerAdapter.AddPropPhotoFinishListener {

    private final static String TAG = AddPropertyActivity.class.getSimpleName();

    //private AddPropertyPresenter presenter;
    private ActivityAddPropertyBinding binder;
    public BallabaUser user;
    private JSONObject photosJson;
    private boolean isSelectedTagForPhoto = false;
    //private AddPropFinishListener listener;

    /*public AddPropertyActivity(AddPropFinishListener listener){
        this.listener = listener;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_add_property);
        binder.setPresenter(new AddPropertyPresenter(this, binder));
        user = BallabaUserManager.getInstance().getUser();

        UiUtils.instance(true, this).hideSoftKeyboard(binder.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorPrimary, getTheme())));

    }

    //Here we add in the left corner of the actionbar a counter of pages
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        TextView pagesCounterTv = new TextView(this);
        int pageNumber = binder.addPropertyViewPager.getCurrentItem();
        pagesCounterTv.setPadding(16, 0, 16, 0);
        if (pageNumber < 4)//page counter should be displayed only in first 4 screens
            menu.add(0, 1, 1, (pageNumber+1)+"/4")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        else if (pageNumber == 5)
            setButtonFinishUploadProperty(menu);

        String label = getResources().getStringArray(R.array.addProperty_titles)[pageNumber];
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //when user clicks finish button on actionbar, last photo that hasn't sent yet, is sent now to server
    private void setButtonFinishUploadProperty(Menu menu){
        final ConnectionsManager conn = ConnectionsManager.getInstance(this);
        final AddPropertyActivity activity = AddPropertyActivity.this;

        menu.add(0, 1, 1, getString(R.string.addProperty_editPhoto_finish))
            .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (photosJson == null) {//user has not switched tag for his photo
                            UiUtils.instance(true, activity).showSnackBar(binder.getRoot(), "לא נבחר חדר");
                        } else {
                            conn.uploadProperty(photosJson, new BallabaResponseListener() {
                                @Override
                                public void resolve(BallabaBaseEntity entity) {
                                    Log.d(TAG, "upload property photo: success");
                                    AddPropertyPresenter.getInstance(activity, binder).onNextViewPagerItem(5);
                                }

                                @Override
                                public void reject(BallabaBaseEntity entity) {
                                    Log.e(TAG, "upload property photo: failure");
                                }
                            });
                        }

                        return false;
                    }
                })
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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