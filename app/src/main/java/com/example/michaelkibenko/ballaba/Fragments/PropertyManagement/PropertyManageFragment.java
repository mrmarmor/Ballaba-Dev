package com.example.michaelkibenko.ballaba.Fragments.PropertyManagement;


import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Activities.PropertyManagementActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachment;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Holders.PropertyAttachmentsAddonsHolder;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.BallabaSearchPropertiesManager;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;
import com.example.michaelkibenko.ballaba.databinding.FragmentPropertyManageBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionAttachmentsBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionAttachmentsExtendedBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionPaymentMethodsBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionPaymentsBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionPriceBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

//TODO THIS CLASS IS ALMOST THE SAME AS PropertyDescriptionPresenter CLASS. SO PLEASE MAKE PropertyDescriptionPresenter A FRAGMENT AND RECYCLE IT!
public class PropertyManageFragment extends Fragment {
    private final String TAG = PropertyManageFragment.class.getSimpleName();
    private static final String PROPERTY_ID = "ID";

    private Activity activity;
    private FragmentPropertyManageBinding binder;
    //private PropertyDescriptionImageBinding binderImage;
    private PropertyDescriptionPriceBinding binderPrice;
    private PropertyDescriptionAttachmentsBinding binderAttach;
    private PropertyDescriptionAttachmentsExtendedBinding binderAttachExt;
    private PropertyDescriptionPaymentsBinding binderPay;
    private PropertyDescriptionPaymentMethodsBinding binderPayMethod;
    //private PropertyDescriptionCommentsBinding binderComment;
    private BallabaPropertyFull propertyFull;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO make server request and get data + progress dialog
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_property_manage, container, false);
        activity = getActivity();
        //binder.propertyManagementPrice.propertyDescriptionPriceToVirtualTourButton.setVisibility(View.GONE);

        initContainers();
        fetchDataFromServer(((PropertyManagementActivity)getActivity()).getPropertyID()+""/*getArguments().getInt(PROPERTY_ID)+""*/);

        return binder.getRoot();
    }

    private void initContainers(){
        binderPrice = binder.propertyManagementPrice;
        binderAttach = binder.propertyManagementAttachments;
        binderAttachExt = binder.propertyManagementAttachmentsExtended;
        binderPay = binder.propertyManagementPayments;
        binderPayMethod = binder.propertyManagementPaymentMethods;
    }

    private void fetchDataFromServer(final String PROPERTY_ID){
        ConnectionsManager.getInstance(activity).getPropertyById(PROPERTY_ID, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                if(entity instanceof BallabaOkResponse){
                    propertyFull = BallabaSearchPropertiesManager.getInstance(activity)
                            .parsePropertiesFull(((BallabaOkResponse)entity).body);
                    BallabaSearchPropertiesManager.getInstance(activity).setPropertyFull(propertyFull);

                    //Log.d(TAG, "properties: " + propertyFull.formattedAddress+":"+propertyFull.street);

                    displayDataOnScreen(propertyFull);

                } else {
                    Log.d(TAG, "error: Response is not an instance of BallabaOkResponse");
                }
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.d(TAG, "properties: error" + entity.toString());
            }
        });
    }

    private void displayDataOnScreen(BallabaPropertyFull propertyFull){
        String price = StringUtils.getInstance(true).formattedNumberWithComma(propertyFull.price);
        String rooms = String.format("%s %s", propertyFull.roomsNumber, activity.getString(R.string.propertyItem_numberOfRooms));
        String size = String.format("%s %s", propertyFull.size, activity.getString(R.string.propertyItem_propertySize));
        String baths = String.format("%s %s", propertyFull.bathrooms, activity.getString(R.string.propertyItem_bathtub));
        String toilets = String.format("%s %s", propertyFull.toilets, activity.getString(R.string.propertyItem_toilets));

        //binder.propertyManagementRootTextViewRentFee.setText(price);
        if (!propertyFull.photos.isEmpty())
            Glide.with(activity).load(propertyFull.photos.get(0).get("photo_url")).into(binder.propertyManagementMainImage);

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
    }

    private void displayLandlord(HashMap<String, String> landlord){
        binderPrice.propertyDescriptionPriceLandlordNameTextView.setText(landlord.get("first_name")+" "+landlord.get("last_name"));
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

    private void displayAttachmentsOnScreen(ArrayList<String> propertyAttachments){
        final UiUtils uiUtils = UiUtils.instance(true, activity);
        TextView chipFloor = uiUtils.generateCustomTextView(propertyFull.floor + " מתוך " + propertyFull.max_floor, R.drawable.building_blue_24);
        binderAttachExt.propertyDescriptionAttachmentsExtendedContainerRight.addView(chipFloor);

        if (propertyAttachments != null){//TODO if we want to hide this section when it contains no attachment: && propertyAttachments.size() > 0) {
            for (int i = 0; i < propertyAttachments.size(); i++) {
                PropertyAttachment.Type propertyAttachment = PropertyAttachment.Type.getTypeById(propertyAttachments.get(i));

                TextView tv = uiUtils.generateCustomTextView(activity.getString(propertyAttachment.getTitle()), R.drawable.building_blue_24);

                if (i % 2 == 0)
                    binderAttachExt.propertyDescriptionAttachmentsExtendedContainerLeft.addView(tv);
                else
                    binderAttachExt.propertyDescriptionAttachmentsExtendedContainerRight.addView(tv);
            }
        } /*else {
            binderAttachExt.getRoot().setVisibility(View.GONE);
            binder.propertyDescriptionAttachmentsExtendedDivider.setVisibility(View.GONE);
        }*/
    }

    private void displayPaymentsOnScreen(ArrayList<HashMap<String, String>> propertyPayments){
        if (propertyPayments != null && propertyPayments.size() > 0) {
            for (int i = 0; i < propertyPayments.size(); i++) {
                HashMap<String, String> paymentItem = propertyPayments.get(i);
                TextView tv = getTextView(getFormattedTitleFromId(paymentItem.get("payment_type")),
                        activity.getResources().getColor(R.color.black));
                if (paymentItem.get("price") == null || paymentItem.get("price").equals("null") ||
                        paymentItem.get("price").equals("") ||
                        paymentItem.get("price").equals("n,ull")) continue;
                binderPay.propertyDescriptionPaymentsContainerRight.addView(tv);

                String formattedPrice = paymentItem.get("price");
                tv = getTextView(String.format("%s%s", "₪", formattedPrice),
                        activity.getResources().getColor(R.color.colorAccent));
                binderPay.propertyDescriptionPaymentsContainerLeft.addView(tv);
                        /*activity.getResources().getColor(R.color.colorAccent);
                binderPay.propertyDescriptionPaymentsContainerLeft.addView(tv);*/
            }
        } else {
            binderPay.getRoot().setVisibility(View.GONE);
            binder.propertyManagementPaymentsDivider.setVisibility(View.GONE);
        }
    }

    private TextView getTextView(final String text, final @ColorInt int color) {
        TextView tv = new TextView(activity);

        if (text != null) {
            tv.setText(text);
            tv.setTextAppearance(activity, R.style.propertyDescriptionPayments_textView);
            tv.setTextColor(color);
        }

        return tv;
    }

    private String getFormattedTitleFromId(String paymentId){
        ArrayList<PropertyAttachmentAddonEntity> payments = PropertyAttachmentsAddonsHolder.getInstance().getPaymentTypes();
        for (PropertyAttachmentAddonEntity payment : payments)
            if (paymentId.equals(payment.id))
                return payment.formattedTitle;

        return null;
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



}
