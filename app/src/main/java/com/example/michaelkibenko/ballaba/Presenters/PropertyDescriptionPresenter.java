package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.michaelkibenko.ballaba.Activities.PropertyDescriptionActivity;
import com.example.michaelkibenko.ballaba.Activities.PropertyGalleryActivity;
import com.example.michaelkibenko.ballaba.Activities.StreetAndMapBoard;
import com.example.michaelkibenko.ballaba.Activities.VirtualTourActivity;
import com.example.michaelkibenko.ballaba.Adapters.DescCommentAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachment;
import com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment;
import com.example.michaelkibenko.ballaba.Fragments.BallabaStreetViewFragment;
import com.example.michaelkibenko.ballaba.Holders.EndpointsHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityPropertyDescriptionBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionAttachmentsBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionAttachmentsExtendedBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionCommentsBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionImageBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionPaymentMethodsBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionPaymentsBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionPriceBinding;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Created by User on 08/04/2018.
 */

public class PropertyDescriptionPresenter implements View.OnClickListener/*, OnStreetViewPanoramaReadyCallback*/ {
    private final String TAG = PropertyDescriptionPresenter.class.getSimpleName();
    public static final String PROPERTY_IMAGE = "Prop_image", FRAGMENT_NAME = "fragment name"
        , PROPERTY_LATLNG_EXTRA = "property latLng extra";

    private FragmentActivity activity;
    private ActivityPropertyDescriptionBinding binder;
    private PropertyDescriptionImageBinding binderImage;
    private PropertyDescriptionPriceBinding binderPrice;
    private PropertyDescriptionAttachmentsBinding binderAttach;
    private PropertyDescriptionAttachmentsExtendedBinding binderAttachExt;
    private PropertyDescriptionPaymentsBinding binderPay;
    private PropertyDescriptionPaymentMethodsBinding binderPayMethod;
    private PropertyDescriptionCommentsBinding binderComment;
    //private PropertyDescriptionImageBinding binderImage;
    private Intent propertyIntent;
    private BallabaResponseListener listener;
    private BallabaPropertyFull propertyFull;
    private BallabaMapFragment mapFragment;
    private LatLng propertyLatLng;
    private StreetViewPanorama panorama;
    private BallabaResponseListener responseListener;

    public PropertyDescriptionPresenter(final Context context, ActivityPropertyDescriptionBinding binding){
        this.activity = (FragmentActivity)context;
        this.binder = binding;

        propertyIntent = activity.getIntent();

        initProperty();
        //getStreetViewPanoramaAsync(this);

    }

    private void initProperty(){
        //TODO initBinders()
        binderImage = binder.propertyDescriptionImage;
        binderPrice = binder.propertyDescriptionPrice;
        binderAttach = binder.propertyDescriptionAttachments;
        binderAttachExt = binder.propertyDescriptionAttachmentsExtended;
        binderPay = binder.propertyDescriptionPayments;
        binderPayMethod = binder.propertyDescriptionPaymentMethods;
        binderComment = binder.propertyDescriptionComments;

        fetchDataFromServer(propertyIntent.getStringExtra(PropertyDescriptionActivity.PROPERTY));
//        ImageView imageFrame = binder.propertyDescriptionImage.propertyDescriptionMainImage;//findViewById(R.id.propertyDescription_mainImage);
        ImageView imageFrame = binderImage.propertyDescriptionMainImage;
        binder.propertyDescriptionImage.goToGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickToGallery();
            }
        });

        Glide.with(activity)
                .load(propertyIntent.getStringExtra(PROPERTY_IMAGE))
                .into(imageFrame);

        buttons_setOnClickListeners();
    }

    private void fetchDataFromServer(final String PROPERTY_ID){
        ConnectionsManager.getInstance(activity).getPropertyById(PROPERTY_ID, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if(entity instanceof BallabaOkResponse){
                    propertyFull = BallabaSearchPropertiesManager
                            .getInstance(activity).parsePropertiesFull(((BallabaOkResponse)entity).body);

                    BallabaSearchPropertiesManager.getInstance(activity).setPropertyFull(propertyFull);

                    Log.d(TAG, "properties: " + propertyFull.formattedAddress+":"+propertyFull.street);

                    displayDataOnScreen(propertyFull);

                    initMapFragment();
                    //initStreetView();

                    //callback.resolve(entity);
                }else {
                    Log.d(TAG, "error: Response is not an instance of BallabaOkResponse");
                    //callback.reject(new BallabaErrorResponse(500, null));
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.d(TAG, "properties: error" );

                //callback.reject(entity);
            }
        });
    }

    private void displayDataOnScreen(BallabaPropertyFull propertyFull){
        String price = StringUtils.getInstance(true, activity).formattedNumberWithComma(propertyFull.price);
        String rooms = String.format("%s %s", propertyFull.roomsNumber, activity.getString(R.string.propertyItem_numberOfRooms));
        String size = String.format("%s %s", propertyFull.size, activity.getString(R.string.propertyItem_propertySize));
        String baths = String.format("%s %s", propertyFull.bathrooms, activity.getString(R.string.propertyItem_bathtub));
        String toilets = String.format("%s %s", propertyFull.toilets, activity.getString(R.string.propertyItem_toilets));

        binderPrice.propertyDescriptionPricePriceTextView.setText(String.format("%s%s", "₪", price));
        binderPrice.propertyDescriptionPriceAddressTextView.setText(propertyFull.formattedAddress);
        binderPrice.propertyDescriptionPriceDateOfEntranceTextView.setText(propertyFull.entry_date);
        binderPrice.propertyDescriptionPriceRentalPeriodTextView.setText(propertyFull.rentPeriod);
        binderPrice.propertyDescriptionPriceFullDescriptionTextView.setText(propertyFull.description);

        //TODO displayLandlord()
        if (propertyFull.landlords != null){
            binderPrice.propertyDescriptionPriceLandlordNameTextView.setText(propertyFull.landlords.get(0).get("first_name")+" "+propertyFull.landlords.get(0).get("last_name"));
            binderPrice.propertyDescriptionPriceLandlordCityTextView.setText(propertyFull.landlords.get(0).get("city"));
            Glide.with(activity)
                    .load(propertyFull.landlords.get(0).get("profile_image"))
                    .into(binderPrice.propertyDescriptionPriceLandlordProfileImage);
        }

        binderAttach.propertyDescriptionAttachmentsNumberOfRoomsTextView.setText(rooms);
        binderAttach.propertyDescriptionAttachmentsSizeTextView.setText(size);
        binderAttach.propertyDescriptionAttachmentsBathroomsTextView.setText(baths);
        binderAttach.propertyDescriptionAttachmentsToiletsTextView.setText(toilets);
        //initAttachmentExtendedRecyclerView(propertyFull);
        displayAttachmentsOnScreen(propertyFull.attachments);
        displayPaymentsOnScreen(propertyFull.payments);
        paymentMethodsStatesChanger(propertyFull.paymentMethods);

        propertyLatLng = new LatLng(Double.parseDouble(propertyFull.lat), Double.parseDouble(propertyFull.lng));

                //TODO initRecycler()
        DescCommentAdapter adapter = new DescCommentAdapter(activity, propertyFull.comments);
        LinearLayoutManager lManager = new LinearLayoutManager(activity);
        binderComment.propertyDescriptionCommentsRecycler.setLayoutManager(lManager);
        binderComment.propertyDescriptionCommentsRecycler.setAdapter(adapter);
    }

   /* private void initAttachmentExtendedRecyclerView(BallabaPropertyFull propertyFull){
        RecyclerView attachmentsRV = binderAttachExt.propertyDescriptionAttachmentsExtendedRecyclerView;
        AttachmentsRecyclerAdapter attachAdapter = new AttachmentsRecyclerAdapter(activity, binderAttach, binderAttachExt, propertyFull.attachments*//*propertyFull.attachments*//*);
        GridLayoutManager gLayoutManager = new GridLayoutManager(activity, 2, LinearLayoutManager.HORIZONTAL, false);
        attachmentsRV.setLayoutManager(gLayoutManager);
        attachmentsRV.setAdapter(attachAdapter);
    }*/

    private void displayAttachmentsOnScreen(ArrayList<String> propertyAttachments){
        if (propertyAttachments != null) {
            for (int i = 0; i < propertyAttachments.size(); i++) {
                PropertyAttachment.Type propertyAttachment = PropertyAttachment.Type.getTypeById(
                        propertyAttachments.get(i));

                TextView tv = new TextView(activity);
                tv.setText(propertyAttachment.getTitle());
                tv.setCompoundDrawablesRelativeWithIntrinsicBounds(propertyAttachment.getIcon(), 0, 0, 0);
                tv.setTextAppearance(R.style.property_description_textViews);
                tv.setPaddingRelative(16, 16, 8, 16);

                GridLayout.LayoutParams param = new GridLayout.LayoutParams(
                        GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f),
                        GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f));
                tv.setLayoutParams(param);

            /*    if (i > propertyAttachments.size() / 2) {
                    tv.setX(-binderAttach.propertyDescriptionAttachmentsSizeTextView.getX()/2);
                } else {
                    tv.setPaddingRelative(16, 20, 0, 0);
                }*/

                binderAttachExt.propertyDescriptionAttachmentsExtendedContainer.addView(tv);
                //addView(binderAttachExt.propertyDescriptionAttachmentsExtendedContainer, tv, i);
            }
        }
    }

    private void displayPaymentsOnScreen(ArrayList<HashMap<String, String>> propertyPayments){
        if (propertyPayments != null) {
            for (int i = 0; i < propertyPayments.size(); i++) {
                TextView tv = getTextView(propertyPayments.get(i).get("payment_type"),
                        activity.getResources().getColor(R.color.black, activity.getTheme()));
                binderPay.propertyDescriptionPaymentsContainerRight.addView(tv, i * 2);

                String formattedPrice = propertyPayments.get(i).get("price");
                tv = getTextView(String.format("%s%s", "₪", formattedPrice),
                        activity.getResources().getColor(R.color.colorAccent, activity.getTheme()));
                binderPay.propertyDescriptionPaymentsContainerLeft.addView(tv, i * 2);

                //TODO TESTING
                /*tv = getTextView(propertyPayments.get(i).get("payment_type"),
                        activity.getResources().getColor(R.color.black, activity.getTheme()));
                binderPay.propertyDescriptionPaymentsContainerRight.addView(tv, i * 2 + 1);

                formattedPrice = propertyPayments.get(i).get("price");
                tv = getTextView(String.format("%s%s", "₪", formattedPrice),
                        activity.getResources().getColor(R.color.colorAccent, activity.getTheme()));
                binderPay.propertyDescriptionPaymentsContainerLeft.addView(tv, i * 2 + 1);*/
                //TODO END OF TESTING

                /*TextView tv = new TextView(activity);
                String formattedPrice = propertyPayments.get(i).get("price");
                tv.setText(String.format("%s%s", formattedPrice, "₪"));
                tv.setTextAppearance(R.style.propertyDescriptionPayments_textView);
                //tv.setWidth(R.dimen.propertyDescription_payments_textView_width);
                //tv.setHeight(R.dimen.propertyDescription_payments_textView_height);
                tv.setPaddingRelative(93, 16, 8, 16);
                tv.setTextColor(activity.getResources().getColor(R.color.black, activity.getTheme()));

                GridLayout.LayoutParams param = new GridLayout.LayoutParams(
                        GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f),
                        GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f));
                tv.setLayoutParams(param);
                binderPay.propertyDescriptionPaymentsContainer.addView(tv, 1);*/
            }
        }
    }

    private TextView getTextView(final String text, final @ColorInt int color) {
        TextView tv = new TextView(activity);
        tv.setText(text);
        tv.setTextAppearance(R.style.propertyDescriptionPayments_textView);
        tv.setTextColor(color);
        //ViewGroup.LayoutParams tvParams = tv.getLayoutParams();
        //tvParams.width = 77;
        //tvParams.height = 35;
        tv.setLayoutParams(new ViewGroup.LayoutParams(77, 35));
        //tv.setWidth(R.dimen.propertyDescription_payments_textView_width);
        //tv.setHeight(R.dimen.propertyDescription_payments_textView_height);
        //tv.setPaddingRelative((index%2 == 0? 16 : 0), 16, 0, 16);

        /*GridLayout.LayoutParams param = new GridLayout.LayoutParams(
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f),
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f));
        tv.setLayoutParams(param);*/

        return tv;
    }

    private final String ONLY_CHEQUE = "1", ONLY_TRANSFER = "2", BOTH = "3";
    private void paymentMethodsStatesChanger(ArrayList<HashMap<String, String>> payMethods){
        if (payMethods != null && payMethods.size() > 0) {
            switch (payMethods.get(0).get("payment_method")) {
                case ONLY_CHEQUE:
                    binderPayMethod.propertyDescriptionPaymentMethodsOrTextView.setVisibility(View.GONE);
                    binderPayMethod.propertyDescriptionPaymentMethodsTransferTextView.setVisibility(View.GONE);
                    break;

                case ONLY_TRANSFER:
                    binderPayMethod.propertyDescriptionPaymentMethodsOrTextView.setVisibility(View.GONE);
                    binderPayMethod.propertyDescriptionPaymentMethodsChequeTextView.setVisibility(View.GONE);
                    break;

                //case BOTH:

            }
        }
    }

    private void initMapFragment(){
        mapFragment = BallabaMapFragment.newInstance();

        String latLngStr = propertyLatLng.latitude + "," + propertyLatLng.longitude;
        String url = String.format("%s%s%s|%s", EndpointsHolder.GOOGLE_MAP_API, latLngStr, EndpointsHolder.GOOGLE_MAP_API_SETTINGS, latLngStr);
        Glide.with(activity).load(url).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                binder.propertyDescriptionMapFragmentContainer.setBackground(resource);
            }
        });

        binder.propertyDescriptionMapFragmentContainer.setOnClickListener(this);
    }

    /*@Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        if (latLng != null) {
            panorama = streetViewPanorama;
            panorama.setPosition(latLng);
            panorama.setStreetNamesEnabled(true);

            panorama.setOnStreetViewPanoramaChangeListener(new StreetViewPanorama.OnStreetViewPanoramaChangeListener() {
                @Override
                public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {
                    if (streetViewPanoramaLocation != null && streetViewPanoramaLocation.links != null) {
                        if (responseListener != null) {
                            responseListener.resolve(new BallabaOkResponse());
                        }
                    } else {
                        //TODO configure it
                        Toast.makeText(getActivity(), "No Street View Available For This Location", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }*/

    private void buttons_setOnClickListeners(){
        binderPrice.propertyDescriptionPriceToVirtualTourButton.setOnClickListener(this);
        binder.propertyDescriptionRootToStreetViewButton.setOnClickListener(this);
    }

    public void onClickContinue(){
        Toast.makeText(activity, "continue", Toast.LENGTH_SHORT).show();
    }

    public void onClickToGallery() {
        Intent intent = new Intent(activity, PropertyGalleryActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(activity, StreetAndMapBoard.class);
        intent.putExtra(PROPERTY_LATLNG_EXTRA, propertyLatLng.latitude+","+propertyLatLng.longitude);

        switch (v.getId()){
            case R.id.propertyDescription_root_toStreetView_button:
                intent.putExtra(FRAGMENT_NAME, BallabaStreetViewFragment.TAG);

                //TODO states
                //@Visibility.Mode int isStreetViewVisible = binderPrice.propertyDescriptionPriceStreetViewContainer.getVisibility();
                //binderPrice.propertyDescriptionPriceStreetViewContainer.setVisibility(
                //        isStreetViewVisible == View.GONE? View.VISIBLE:View.GONE);
                //Toast.makeText(activity, "street", Toast.LENGTH_SHORT).show();
                break;

            case R.id.propertyDescription_mapFragment_container:
                intent.putExtra(FRAGMENT_NAME, BallabaMapFragment.TAG);
                break;

            case R.id.propertyDescriptionPrice_toVirtualTour_button:
                Intent toVirtualTour = new Intent(activity, VirtualTourActivity.class);
                activity.startActivity(toVirtualTour);
        }

        activity.startActivity(intent);
    }

}
