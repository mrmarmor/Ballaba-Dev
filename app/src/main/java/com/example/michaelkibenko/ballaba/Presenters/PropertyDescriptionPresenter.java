package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.michaelkibenko.ballaba.Activities.BaseActivity;
import com.example.michaelkibenko.ballaba.Activities.PropertyDescriptionActivity;
import com.example.michaelkibenko.ballaba.Activities.PropertyGalleryActivity;
import com.example.michaelkibenko.ballaba.Activities.Scoring.ScoringWelcomeActivity;
import com.example.michaelkibenko.ballaba.Activities.StreetAndMapBoardActivity;
import com.example.michaelkibenko.ballaba.Activities.VirtualTourActivity;
import com.example.michaelkibenko.ballaba.Adapters.DescCommentAdapter;
import com.example.michaelkibenko.ballaba.Adapters.PropertyDescriptionOpenDoorDatesRecycerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachment;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Fragments.BallabaMapFragment;
import com.example.michaelkibenko.ballaba.Fragments.BallabaStreetViewFragment;
import com.example.michaelkibenko.ballaba.Holders.EndpointsHolder;
import com.example.michaelkibenko.ballaba.Holders.PropertyAttachmentsAddonsHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.BallabaUserManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
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
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by User on 08/04/2018.
 */

public class PropertyDescriptionPresenter implements View.OnClickListener/*, OnStreetViewPanoramaReadyCallback*/ {
    private final String TAG = PropertyDescriptionPresenter.class.getSimpleName();
    public static final String PROPERTY_IMAGE = "Prop_image", FRAGMENT_NAME = "fragment name", PROPERTY_LATLNG_EXTRA = "property latLng extra", PROPERTY_POSITION = "property position", PROPERTY_IS_SAVED = "property is saved";

    private FragmentActivity activity;
    private Resources res;
    private ActivityPropertyDescriptionBinding binder;
    private PropertyDescriptionImageBinding binderImage;
    private PropertyDescriptionPriceBinding binderPrice;
    private PropertyDescriptionAttachmentsBinding binderAttach;
    private PropertyDescriptionAttachmentsExtendedBinding binderAttachExt;
    private PropertyDescriptionPaymentsBinding binderPay;
    private PropertyDescriptionPaymentMethodsBinding binderPayMethod;
    private PropertyDescriptionCommentsBinding binderComment;
    private Intent propertyIntent;
    private BallabaPropertyFull propertyFull;
    //private BallabaMapFragment mapFragment;
    private LatLng propertyLatLng;
    private ImageView imageFrame;
    private boolean isFromSearch;


    public PropertyDescriptionPresenter(final Context context, ActivityPropertyDescriptionBinding binding) {
        this.activity = (FragmentActivity) context;
        this.binder = binding;


        propertyIntent = activity.getIntent();
        res = activity.getResources();

        isFromSearch = propertyIntent.getBooleanExtra("SHOW_CONTINUE_OPTION", true);
        binder.propertyDescriptionRootBottomContainer.setVisibility(isFromSearch ? View.VISIBLE : View.GONE);
        initProperty();
    }

    private void initProperty() {
        //TODO initBinders()
        binderImage = binder.propertyDescriptionImage;
        binderPrice = binder.propertyDescriptionPrice;
        binderAttach = binder.propertyDescriptionAttachments;
        binderAttachExt = binder.propertyDescriptionAttachmentsExtended;
        binderPay = binder.propertyDescriptionPayments;
        binderPayMethod = binder.propertyDescriptionPaymentMethods;
        binderComment = binder.propertyDescriptionComments;

        fetchDataFromServer(propertyIntent.getStringExtra(PropertyDescriptionActivity.PROPERTY));
        imageFrame = binder.propertyDescriptionImage.propertyDescriptionMainImage;
        //ImageView imageFrame = binderImage.propertyDescriptionMainImage;

        buttons_setOnClickListeners();
    }

    private void fetchDataFromServer(final String PROPERTY_ID) {
        ConnectionsManager.getInstance(activity).getPropertyById(PROPERTY_ID, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if (entity instanceof BallabaOkResponse) {
                    propertyFull = BallabaSearchPropertiesManager
                            .getInstance(activity).parsePropertiesFull(((BallabaOkResponse) entity).body);
                    BallabaSearchPropertiesManager.getInstance(activity).setPropertyFull(propertyFull);
                    if (propertyFull != null) {
                        displayDataOnScreen(propertyFull);
                        initPropertyMap();
                        initMeetings();
                        initPhoto();
                    }
                } else {
                    Log.d(TAG, "error: Response is not an instance of BallabaOkResponse");
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.d(TAG, "properties: error");
            }
        });
    }

    private void initPhoto() {
        if (propertyFull.photos.size() > 0) {
            Glide.with(activity).load(propertyFull.photos.get(0).get("photo_url")).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    imageFrame.setImageDrawable(res.getDrawable(R.mipmap.ic_launcher));
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    imageFrame.setImageDrawable(resource);
                    return true;
                }
            }).into(imageFrame);
        }
    }

    private void initMeetings() {
        ArrayList<HashMap<String, String>> dates = propertyFull.openDoorDates;
        if (dates.isEmpty()) {
            binder.propertyDescriptionOpenDoorDates.getRoot().setVisibility(View.GONE);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
            binder.propertyDescriptionOpenDoorDates.datesRecyclerView.setLayoutManager(linearLayoutManager);
            PropertyDescriptionOpenDoorDatesRecycerAdapter adapter = new PropertyDescriptionOpenDoorDatesRecycerAdapter(activity, dates, propertyFull);
            binder.propertyDescriptionOpenDoorDates.datesRecyclerView.setAdapter(adapter);
        }
    }

    private void displayDataOnScreen(BallabaPropertyFull propertyFull) {
        String price = StringUtils.getInstance(true).formattedNumberWithComma(propertyFull.price);
        String rooms = String.format("%s %s", propertyFull.roomsNumber, activity.getString(R.string.propertyItem_numberOfRooms));
        String size = String.format("%s %s", propertyFull.size, activity.getString(R.string.propertyItem_propertySize));
        String baths = String.format("%s %s", propertyFull.bathrooms, activity.getString(R.string.propertyItem_bathtub));
        String toilets = String.format("%s %s", propertyFull.toilets, activity.getString(R.string.propertyItem_toilets));

        binder.propertyDescriptionRootTextViewRentFee.setText(price);
        binderPrice.propertyDescriptionPricePriceTextView.setText(String.format("%s%s", "₪", price));
        binderPrice.propertyDescriptionPriceAddressTextView.setText(propertyFull.formattedAddress);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.US);
        SimpleDateFormat newSdf = new SimpleDateFormat("dd.MM.yy", Locale.US);
        Date date = null;
        try {
            date = sdf.parse(propertyFull.entry_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        binderPrice.propertyDescriptionPriceDateOfEntranceTextView.setText(newSdf.format(date));
        binderPrice.propertyDescriptionPriceRentalPeriodTextView.setText(propertyFull.rentPeriod);
        binderPrice.propertyDescriptionPriceFullDescriptionTextView.setText(propertyFull.description);

        binderAttach.propertyDescriptionAttachmentsNumberOfRoomsTextView.setText(rooms);
        binderAttach.propertyDescriptionAttachmentsSizeTextView.setText(size);
        binderAttach.propertyDescriptionAttachmentsBathroomsTextView.setText(baths);
        binderAttach.propertyDescriptionAttachmentsToiletsTextView.setText(toilets);
        //initAttachmentExtendedRecyclerView(propertyFull);

        if (propertyFull.landlords != null && propertyFull.landlords.size() > 0)
            displayLandlord(propertyFull.landlords.get(0));
        displayAttachmentsOnScreen(propertyFull.attachments);
        displayPaymentsOnScreen(propertyFull.payments);
        paymentMethodsStatesChanger(propertyFull.paymentMethods);
        initCommentsRecycler(binderComment.propertyDescriptionCommentsRecycler);
        setIsSavedButton();

        propertyLatLng = new LatLng(Double.parseDouble(propertyFull.lat), Double.parseDouble(propertyFull.lng));

    }

    private void displayLandlord(HashMap<String, String> landlord) {
        binderPrice.propertyDescriptionPriceLandlordNameTextView.setText(landlord.get("first_name") + " " + landlord.get("last_name"));
        binderPrice.propertyDescriptionPriceLandlordCityTextView.setText(landlord.get("city"));
        Glide.with(activity)
                .load(landlord.get("profile_image"))
                .into(binderPrice.propertyDescriptionPriceLandlordProfileImage);
    }

   /* private void initAttachmentExtendedRecyclerView(BallabaPropertyFull propertyFull){
        RecyclerView attachmentsRV = binderAttachExt.propertyDescriptionAttachmentsExtendedRecyclerView;
        AttachmentsRecyclerAdapter attachAdapter = new AttachmentsRecyclerAdapter(activity, binderAttach, binderAttachExt, propertyFull.attachments*//*propertyFull.attachments*//*);
        GridLayoutManager gLayoutManager = new GridLayoutManager(activity, 2, LinearLayoutManager.HORIZONTAL, false);
        attachmentsRV.setLayoutManager(gLayoutManager);
        attachmentsRV.setAdapter(attachAdapter);
    }*/

    private void displayAttachmentsOnScreen(ArrayList<String> propertyAttachments) {
        final UiUtils uiUtils = UiUtils.instance(true, activity);
        TextView chipFloor = uiUtils.generateCustomTextView(propertyFull.floor + " מתוך " + propertyFull.max_floor, R.drawable.building_blue_24);
        binderAttachExt.propertyDescriptionAttachmentsExtendedContainerRight.addView(chipFloor);

        if (propertyAttachments != null) {//TODO if we want to hide this section when it contains no attachment: && propertyAttachments.size() > 0) {
            for (int i = 0; i < propertyAttachments.size(); i++) {
                PropertyAttachment.Type propertyAttachment = PropertyAttachment.Type.getTypeById(
                        propertyAttachments.get(i));

                TextView tv = uiUtils.generateCustomTextView(activity.getString(propertyAttachment.getTitle()), R.drawable.building_blue_24);

               /* //TextView tv = new TextView(activity);
                tv.setText();
                tv.setCompoundDrawablesRelativeWithIntrinsicBounds(propertyAttachment.getIcon(), 0, 0, 0);
                tv.setTextAppearance(R.style.property_description_textViews);
                tv.setCompoundDrawablePadding(15);
                tv.setPaddingRelative(0, 16, 8, 16);*/

                /*GridLayout.LayoutParams param = new GridLayout.LayoutParams(
                        GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f),
                        GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f));
                tv.setLayoutParams(param);*/

                if (i % 2 == 0)
                    binderAttachExt.propertyDescriptionAttachmentsExtendedContainerLeft.addView(tv);
                    //binderAttachExt.propertyDescriptionAttachmentsExtendedContainer.addView(tv);
                else
                    binderAttachExt.propertyDescriptionAttachmentsExtendedContainerRight.addView(tv);
                //addView(binderAttachExt.propertyDescriptionAttachmentsExtendedContainer, tv, i);
            }
        } /*else {
            binderAttachExt.getRoot().setVisibility(View.GONE);
            binder.propertyDescriptionAttachmentsExtendedDivider.setVisibility(View.GONE);
        }*/
    }

    private void displayPaymentsOnScreen(ArrayList<HashMap<String, String>> propertyPayments) {
        if (propertyPayments != null && propertyPayments.size() > 0) {
            for (int i = 0; i < propertyPayments.size(); i++) {
                HashMap<String, String> item = propertyPayments.get(i);
                if (item.get("price") == null || item.get("price").equals("n,ull") || item.get("price").equals(""))
                    continue;
                TextView tv = getTextView(getFormattedTitleFromId(item.get("payment_type")),
                        res.getColor(R.color.black));
                binderPay.propertyDescriptionPaymentsContainerRight.addView(tv, i);

                String formattedPrice = item.get("price");
                tv = getTextView(String.format("%s%s", "₪", formattedPrice),
                        res.getColor(R.color.colorAccent));
                binderPay.propertyDescriptionPaymentsContainerLeft.addView(tv, i);

                //TODO TESTING
                /*tv = getTextView(propertyPayments.get(i).get("payment_type"),
                        activity.getResources().getColor(R.color.black, activity.getTheme()));
                binderPay.propertyDescriptionPaymentsContainerRight.addView(tv, i * 2 + 1);

                formattedPrice = propertyPayments.get(i).get("price");
                tv = getTextView(String.format("%s%s", "₪", formattedPrice),
                        activity.getResources().getColor(R.color.colorAccent, activity.getTheme()));
                binderPay.propertyDescriptionPaymentsContainerLeft.addView(tv, i * 2 + 1);*/
                //TODO END OF TESTING

            }
        } else {
            binderPay.getRoot().setVisibility(View.GONE);
            binder.propertyDescriptionPaymentsDivider.setVisibility(View.GONE);
        }
    }

    private TextView getTextView(final String text, final @ColorInt int color) {
        TextView tv = new TextView(activity);

        if (text != null) {
            tv.setText(text);
            tv.setTextAppearance(activity, R.style.propertyDescriptionPayments_textView);
            tv.setTextColor(color);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0 , 16 , 0 , 0);
            tv.setLayoutParams(params);
        }

        return tv;
    }

    private String getFormattedTitleFromId(String paymentId) {
        ArrayList<PropertyAttachmentAddonEntity> payments = PropertyAttachmentsAddonsHolder.getInstance().getPaymentTypes();
        for (PropertyAttachmentAddonEntity payment : payments)
            if (paymentId.equals(payment.id))
                return payment.formattedTitle;

        return null;
    }

    private final String ONLY_CHEQUE = "1", ONLY_TRANSFER = "2", BOTH = "3";

    private void paymentMethodsStatesChanger(ArrayList<HashMap<String, String>> payMethods) {
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

    private void initCommentsRecycler(RecyclerView recyclerView) {
        if (propertyFull.comments != null && propertyFull.comments.size() > 0) {
            DescCommentAdapter adapter = new DescCommentAdapter(activity, propertyFull.comments);
            LinearLayoutManager lManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(lManager);
            recyclerView.setAdapter(adapter);
        } else {
            binderComment.propertyDescriptionCommentsRoot.setVisibility(View.GONE);
        }
    }

    private void initPropertyMap() {
        //mapFragment = BallabaMapFragment.newInstance();

        Log.d(TAG, "activity is: " + activity);
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

    private void setIsSavedButton() {
        Drawable d = propertyFull.is_saved ? res.getDrawable(R.drawable.heart_blue_24, activity.getTheme())
                : res.getDrawable(R.drawable.heart_white_24, activity.getTheme());
        binderImage.propertyDescriptionMainImageIsSaved.setImageDrawable(d);

        binderImage.propertyDescriptionMainImageIsSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable d;
                if (propertyFull.is_saved) {
                    d = res.getDrawable(R.drawable.heart_white_24, activity.getTheme());
                    ConnectionsManager.getInstance(activity).removeFromFavoritesProperty(propertyFull.id);
                } else {
                    d = res.getDrawable(R.drawable.heart_blue_24, activity.getTheme());
                    ConnectionsManager.getInstance(activity).addToFavoritesProperty(propertyFull.id);
                }
                binderImage.propertyDescriptionMainImageIsSaved.setImageDrawable(d);
                propertyFull.is_saved = !propertyFull.is_saved;
                BallabaSearchPropertiesManager.getInstance(activity).setPropertyFull(propertyFull);
            }
        });
    }

    private void buttons_setOnClickListeners() {
        binderImage.propertyDescriptionMainImageIsSaved.setOnClickListener(this);
        binderImage.propertyDescriptionMainImageShare.setOnClickListener(this);
        binderImage.propertyDescriptionMainImageBack.setOnClickListener(this);
        binderImage.propertyDescriptionPriceToVirtualTourButton.setOnClickListener(this);
        binder.propertyDescriptionRootToStreetViewButton.setOnClickListener(this);
        binder.propertyDescriptionImage.goToGallery.setOnClickListener(this);
    }

    public void onClickContinue() {
        //scoring_status
        boolean isUserScored = BallabaUserManager.getInstance().getUser().getIs_scored();
        if (isUserScored)
            Toast.makeText(activity, "here we are applying a meeting to landlord", Toast.LENGTH_SHORT).show();
        else
            activity.startActivity(new Intent(activity, ScoringWelcomeActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goToGallery:
                Intent intent = new Intent(activity, PropertyGalleryActivity.class);
                intent.putExtra("SHOW_CONTINUE_OPTION" , isFromSearch);
                activity.startActivity(intent);
                break;

            case R.id.propertyDescription_mainImage_share:
                shareProperty(new Intent(android.content.Intent.ACTION_SEND));
                break;

            case R.id.propertyDescription_mainImage_back:
                activity.finish();
                break;

            case R.id.propertyDescription_root_toStreetView_button:
                if (propertyLatLng != null) {
                    intent = new Intent(activity, StreetAndMapBoardActivity.class);
                    intent.putExtra(PROPERTY_LATLNG_EXTRA, propertyLatLng.latitude + "," + propertyLatLng.longitude);
                    intent.putExtra(FRAGMENT_NAME, BallabaStreetViewFragment.TAG);
                    activity.startActivity(intent);
                } else {
                    ((BaseActivity) activity).getDefaultSnackBar(binder.propertyDescriptionRootToStreetViewButton, "מיקום הנכס אינו ידוע", false).show();
                }

                //TODO states
                //@Visibility.Mode int isStreetViewVisible = binderPrice.propertyDescriptionPriceStreetViewContainer.getVisibility();
                //binderPrice.propertyDescriptionPriceStreetViewContainer.setVisibility(
                //        isStreetViewVisible == View.GONE? View.VISIBLE:View.GONE);
                //Toast.makeText(activity, "street", Toast.LENGTH_SHORT).show();
                break;

            case R.id.propertyDescription_mapFragment_container:
                intent = new Intent(activity, StreetAndMapBoardActivity.class);
                intent.putExtra(PROPERTY_LATLNG_EXTRA, propertyLatLng.latitude + "," + propertyLatLng.longitude);
                intent.putExtra(FRAGMENT_NAME, BallabaMapFragment.TAG);
                activity.startActivity(intent);
                break;

            case R.id.propertyDescriptionPrice_toVirtualTour_button:
                intent = new Intent(activity, VirtualTourActivity.class);
                //intent.putExtra(PROPERTY_LATLNG_EXTRA, propertyLatLng.latitude+","+propertyLatLng.longitude);
                activity.startActivity(intent);
        }

    }

    private void shareProperty(Intent shareIntent) {
        shareIntent.setType("text/plain");
        String shareBody = "הי. רציתי לשתף אותך בנכס זה שראיתי באפליקציית Ballaba-It.";
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share property");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        activity.startActivity(Intent.createChooser(shareIntent, "שתף נכס"));
    }

}