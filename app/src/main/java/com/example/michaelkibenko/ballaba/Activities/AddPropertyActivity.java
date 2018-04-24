package com.example.michaelkibenko.ballaba.Activities;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Presenters.AddPropertyPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityAddPropertyBinding;

public class AddPropertyActivity extends AppCompatActivity {
    private final static String TAG = AddPropertyActivity.class.getSimpleName();

    //private AddPropertyPresenter presenter;
    private ActivityAddPropertyBinding binder;
    public BallabaUser user;

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

    //Here we adds in the left corner of the actionbar a counter of pages
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        TextView pagesCounterTv = new TextView(this);
        int pageNumber = binder.addPropertyViewPager.getCurrentItem();
        //pagesCounterTv.setText((pageNumber+1) + "/4");
        pagesCounterTv.setPadding(16, 0, 16, 0);
        menu.add(0, 1, 1, (pageNumber+1)+"/4").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        String label = getResources().getStringArray(R.array.addProperty_titles)[pageNumber];
        getSupportActionBar().setTitle(label);

        return true;
    }

    //Here we adds a back button at the right corner of the actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();//we can use binder.addPropertyViewPager.setCurrentItem(binder.addPropertyViewPager.getCurrentItem() - 1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}