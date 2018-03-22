package com.example.michaelkibenko.ballaba.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Entities.BallabaProperty;
import com.example.michaelkibenko.ballaba.Fragments.PropertiesRecyclerFragment;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.PropertiesManager;
import com.example.michaelkibenko.ballaba.Presenters.MainPresenter;
import com.example.michaelkibenko.ballaba.Presenters.SelectCityPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelkibenko on 12/03/2018.
 */

public class MainActivity extends BaseActivity implements
        PropertiesRecyclerFragment.OnFragmentInteractionListener{

    private final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainLayoutBinding binder;
    private MainPresenter presenter;
    //private List<BallabaProperty> properties = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_main_layout);

        //properties = PropertiesManager.getInstance(this).getProperties();
        presenter = new MainPresenter(this, binder, getSupportFragmentManager());
        binder.setPresenter(presenter);

        //TODO ask permission for GPS
        //initPropertiesRecyclerFragment();
    }

    @Override
    public void onBackPressed() {
        if (binder.mainActivityViewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            binder.mainActivityViewPager.setCurrentItem(binder.mainActivityViewPager.getCurrentItem() - 1, false);
        }
    }

    private void initPropertiesRecyclerFragment(){
        /*PropertiesRecyclerFragment autocompleteFragment = (PropertiesRecyclerFragment)
                getSupportFragmentManager().findFragmentById(R.id.search_autoCompleteTV_fragment);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.search_autoCompleteTV_fragment, autocompleteFragment)
                .commit();*/
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(TAG, "data from fragment: "+uri.getEncodedUserInfo());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainPresenter.REQ_CODE_SELECT_CITY) {
            if (resultCode == RESULT_OK && data != null) {
                presenter.SearchBarStateUIChanger(data.getStringExtra(
                        SelectCityPresenter.SELECTED_CITY_KEY), MainPresenter.SearchState.FILTERED);
                Toast.makeText(this, data.getStringExtra(SelectCityPresenter.SELECTED_CITY_KEY), Toast.LENGTH_LONG).show();
            }
        }
    }

}
