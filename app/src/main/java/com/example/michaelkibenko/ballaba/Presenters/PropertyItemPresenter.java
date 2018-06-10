package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Common.BallabaPropertyListener;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.PropertyItemBinding;

/**
 * Created by User on 19/03/2018.
 */

public class PropertyItemPresenter extends BasePresenter implements BallabaPropertyListener{
    private final static String TAG = PropertyItemPresenter.class.getSimpleName();
    private final @IdRes int WHOLE_PROPERTY = R.id.propertyItem_rootLayout,
                             SAVED_PROPERTY = R.id.propertyItem_isSavedProperty_ImageView,
                             GUARANTEE_PROPERTY = R.id.propertyItem_guarantee_ImageView;

    private static PropertyItemPresenter instance;
    private Context context;
    private PropertyItemBinding binder;
    public BallabaPropertyResult property;

    public PropertyItemPresenter(Context context, PropertyItemBinding binder/*, int position*/){
        this.context = context;
        this.binder = binder;
    }

    public static PropertyItemPresenter getInstance(Context context, PropertyItemBinding binder/*, int position*/) {
        if(instance == null){
            instance = new PropertyItemPresenter(context, binder/*, position*/);
        }
        return instance;
    }

    @Override
    public void BallabaPropertyOnClick(final View view, final int position) {
        property = BallabaSearchPropertiesManager.getInstance(context).getResults().get(position);

        if (view.getId() == WHOLE_PROPERTY) {
            Toast.makeText(context, "go to property details screen: " + position, Toast.LENGTH_SHORT).show();
        } else if (view.getId() == GUARANTEE_PROPERTY) {
            Toast.makeText(context, "property guarantee: " + property.isGuarantee, Toast.LENGTH_SHORT).show();
        } else if (view.getId() == SAVED_PROPERTY){
            changeIsSavedIconState((ImageView)view, property);
        }
    }

    private void changeIsSavedIconState(final ImageView view, final BallabaPropertyResult property){
        property.isSaved = !property.isSaved;
        @DrawableRes int d = property.isSaved? R.drawable.heart_blue_24 : R.drawable.heart_white_24;
        view.setImageResource(d);
    }

}
