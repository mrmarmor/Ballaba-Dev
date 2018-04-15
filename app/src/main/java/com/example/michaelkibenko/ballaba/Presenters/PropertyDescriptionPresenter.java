package com.example.michaelkibenko.ballaba.Presenters;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.support.annotation.ColorInt;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Activities.PropertyDescriptionActivity;
import com.example.michaelkibenko.ballaba.Adapters.AttachmentsRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Adapters.DescCommentAdapter;
import com.example.michaelkibenko.ballaba.Common.BallabaDialogBuilder;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyResult;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachment;
import com.example.michaelkibenko.ballaba.Entities.PropertyDescriptionComment;
import com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment;
import com.example.michaelkibenko.ballaba.Fragments.PropertyImageFragment;
import com.example.michaelkibenko.ballaba.Holders.EndpointsHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.DeviceUtils;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.ActivityPropertyDescriptionBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionAttachmentsBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionAttachmentsExtendedBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionCommentsBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionImageBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionPaymentMethodsBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionPaymentsBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionPriceBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import static java.sql.Types.NULL;

/**
 * Created by User on 08/04/2018.
 */

public class PropertyDescriptionPresenter implements OnMapReadyCallback{
    private final String TAG = PropertyDescriptionPresenter.class.getSimpleName();
    public static final String PROPERTY_IMAGE = "Prop_image";

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
    private BallabaMapFragment mapFragment;

    private String propertyLat, propertyLng;

    public PropertyDescriptionPresenter(Context context, ActivityPropertyDescriptionBinding binding){
        this.activity = (FragmentActivity)context;
        this.binder = binding;

        propertyIntent = activity.getIntent();

        initProperty();
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
        ImageView imageFrame = binderImage.propertyDescriptionMainImage;//findViewById(R.id.propertyDescription_mainImage);

        Glide.with(activity)
                .load(propertyIntent.getStringExtra(PROPERTY_IMAGE))
                .into(imageFrame);

        buttons_setOnClickListeners();
    }

    private void initMapFragment(LatLng latLng){
        mapFragment = BallabaMapFragment.newInstance();
        mapFragment.setLocation(latLng);
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(binder.propertyDescriptionMapFragmentContainer.getId(), mapFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setMapFullScreen(){
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(binder.propertyDescriptionMapFragmentFullContainer.getId(), mapFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void fetchDataFromServer(final String PROPERTY_ID){
        ConnectionsManager.getInstance(activity).getPropertyById(PROPERTY_ID, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if(entity instanceof BallabaOkResponse){
                    BallabaPropertyFull propertyFull = BallabaSearchPropertiesManager
                            .getInstance(activity).parsePropertiesFull(((BallabaOkResponse)entity).body);

                    Log.d(TAG, "properties: " + propertyFull.formattedAddress+":"+propertyFull.street);

                    displayDataOnScreen(propertyFull);

                    initMapFragment(new LatLng(Double.parseDouble(propertyLat), Double.parseDouble(propertyLng)));

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

        binderPrice.propertyDescriptionPricePriceTextView.setText(String.format("%s %s", "₪", price));
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

        this.propertyLat = propertyFull.lat;
        this.propertyLng = propertyFull.lng;
        String latLngStr = propertyLat + "," + propertyLng;
        String url = String.format("%s%s%s|%s", EndpointsHolder.GOOGLE_MAP_API, latLngStr, EndpointsHolder.GOOGLE_MAP_API_SETTINGS, latLngStr);
//        Glide.with(activity)
//             .load(url)
//             .into(binderPayMethod.propertyDescriptionPaymentMethodsGoogleMapImageView);
        BallabaMapFragment.newInstance().setLocation(new LatLng(Double.parseDouble(propertyLat), Double.parseDouble(propertyLng)));

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

                tv = getTextView(propertyPayments.get(i).get("payment_type"),
                        activity.getResources().getColor(R.color.black, activity.getTheme()));
                binderPay.propertyDescriptionPaymentsContainerRight.addView(tv, i * 2 + 1);

                formattedPrice = propertyPayments.get(i).get("price");
                tv = getTextView(String.format("%s%s", formattedPrice, "₪"),
                        activity.getResources().getColor(R.color.colorAccent, activity.getTheme()));
                binderPay.propertyDescriptionPaymentsContainerLeft.addView(tv, i * 2 + 1);

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

    private void buttons_setOnClickListeners(){

        binderImage.propertyDescriptionMainImageToGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "gallery", Toast.LENGTH_SHORT).show();
            }
        });
        binderPrice.propertyDescriptionPriceToStreetViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "streetview", Toast.LENGTH_SHORT).show();

            }
        });
        binder.propertyDescriptionMapFragmentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiUtils.instance(true, activity).setFilterBarVisibility(UiUtils.ScreenStates.FULL);
//                final BallabaMapFragment dialog = BallabaMapFragment.newInstance();
//                dialog.setRetainInstance(true);
//                //dialog.getMapAsync(PropertyDescriptionPresenter.this);
//                dialog.setStyle(NULL, R.style.AppTheme);
//                dialog.show(activity.getSupportFragmentManager(), null);
//                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//                Log.d(TAG, dialog.getDialog()+"");
//
//                LayoutInflater li = LayoutInflater.from(activity);
//                View view = li.inflate(R.layout.fragment_map, null, true);
//                //BallabaDialogBuilder dialogBuilder = new BallabaDialogBuilder(activity);
//
//                MapView mMapView = (MapView) view.findViewById(R.id.mapView);
//                mMapView.getMapAsync(PropertyDescriptionPresenter.this);
//                //dialog.onAttach(activity);
//                //mMapView.onCreate(null);
//                //mMapView.onResume();
//                MapsInitializer.initialize(activity);
//
//                dialog.onCreateView(li, null, null);
            }
        });

    }

    //@BindingAdapter("bind:addView")
   /* private ViewGroup addView(ViewGroup container, TextView textView, final int INDEX) {
        container.addView(textView, INDEX);
        return container;
    }*/

    /*public TextView getDynamicTextViews(String attachmentType) {
        //TextView textView = new TextView(activity);
        //textView.setText(attachmentType);

        return new TextView(activity);
    }*/

    public void onClickContinue(){
        Toast.makeText(activity, "continue", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        LatLng propertyPos = new LatLng(Float.parseFloat(propertyLat), Float.parseFloat(propertyLng));
        Log.d(TAG, "map is ready. location is " + propertyPos);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(propertyPos).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }
}
