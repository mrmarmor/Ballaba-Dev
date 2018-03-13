package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;

import com.example.michaelkibenko.ballaba.Adapters.SearchViewPagerAdapter;
import com.example.michaelkibenko.ballaba.databinding.SearchActivityLayoutBinding;

/**
 * Created by michaelkibenko on 12/03/2018.
 */

public class SearchPresenter extends BasePresenter {

    private final SearchActivityLayoutBinding binder;
    private MapPresenter mapPresenter;
    private Context context;
    private FragmentManager fm;
    private PagerAdapter pagerAdapter;

    public SearchPresenter(Context context, SearchActivityLayoutBinding binder, FragmentManager fm){
        this.binder = binder;
        this.context = context;
        this.fm = fm;

        initViewPager();

        //TODO as default the recyclerView will be shown. for now, map will be
        //mapPresenter = new MapPresenter(this.context, this.binder);
        //mapPresenter.openMapFragment();
    }

    private void initViewPager(){
        pagerAdapter = new SearchViewPagerAdapter(context, binder, fm);
        binder.searchViewPager.setAdapter(pagerAdapter);
    }

    public void onClickToGoogleMap(){
        binder.searchViewPager.setCurrentItem(binder.searchViewPager.getCurrentItem() - 1);
    }
}
