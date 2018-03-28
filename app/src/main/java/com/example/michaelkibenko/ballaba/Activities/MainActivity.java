package com.example.michaelkibenko.ballaba.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Common.BallabaDialogBuilder;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaProperty;
import com.example.michaelkibenko.ballaba.Fragments.PropertiesRecyclerFragment;
import com.example.michaelkibenko.ballaba.Managers.BallabaLocationManager;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.PropertiesManager;
import com.example.michaelkibenko.ballaba.Presenters.MainPresenter;
import com.example.michaelkibenko.ballaba.Presenters.SelectCityPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityMainLayoutBinding;
import com.google.android.gms.maps.model.LatLng;

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

        //initPropertiesRecyclerFragment();
    }

    @Override
    public void onBackPressed() {
        if (binder.mainActivityViewPager.getCurrentItem() == 0) {
            BallabaDialogBuilder areUSureDialog = new BallabaDialogBuilder(this);

            areUSureDialog.setButtons("Exit", "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.super.onBackPressed();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            areUSureDialog.setIcon(R.drawable.check_blue_24);
            areUSureDialog.setContent("Exit", "Are You sure you want to exit?", null).show();

        } else {
            presenter.onClickToGoogleMap();
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
        //TODO add result to board
        if (requestCode == MainPresenter.REQ_CODE_SELECT_CITY) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> cities = data.getStringArrayListExtra(SelectCityPresenter.SELECTED_CITIES_KEY);

                for (String city : cities) {
                    LatLng cityLatLng = BallabaLocationManager.getInstance(this).locationGeoCoder(city);
                    if (cityLatLng != null) {
                        String cityLatLngStr = cityLatLng.latitude + "," + cityLatLng.longitude;
                        Log.d(TAG, cityLatLngStr + "\n");
                    }
                }

                presenter.SearchBarStateUIChanger(cities.get(0), MainPresenter.SearchState.FILTERED);

                /*BallabaSearchPropertiesManager.getInstance(this).getPropertiesByLatLng(cityLatLngStr
                        , new BallabaResponseListener() {
                            @Override
                            public void resolve(BallabaBaseEntity entity) {
                                Log.d(TAG, "entity: "+entity);
                            }

                            @Override
                            public void reject(BallabaBaseEntity entity) {
                                Log.e(TAG, "entity: "+entity);
                            }
                        }, true);

                Toast.makeText(this, data.getStringExtra(SelectCityPresenter.SELECTED_CITY_KEY), Toast.LENGTH_LONG).show();
                */
            }
        }
    }

}
