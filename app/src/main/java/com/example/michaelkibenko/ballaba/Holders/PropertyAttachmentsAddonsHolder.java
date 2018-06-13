package com.example.michaelkibenko.ballaba.Holders;

import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PropertyAttachmentsAddonsHolder {
    public static final String TAG = PropertyAttachmentsAddonsHolder.class.getSimpleName();
    private final String FURNITURE = "furniture", ELECTRONICS = "electronics", ATTACHMENTS = "attachments"
            , PAYMENT_TYPES = "payment_types", PAYMENT_METHODS = "payment_methods"
            , PROPERTY_PHOTO_TAGS = "property_photo_tags";

    private static PropertyAttachmentsAddonsHolder instance;

    private ArrayList<PropertyAttachmentAddonEntity> furniture, electronics, attachments
            , paymentTypes, paymentMethods, photoTags;

    private StringUtils stringUtils;
    public static PropertyAttachmentsAddonsHolder getInstance() {
        if(instance == null){
            instance = new PropertyAttachmentsAddonsHolder();
        }
        return instance;
    }

    private PropertyAttachmentsAddonsHolder(){
        furniture = new ArrayList<>();
        electronics = new ArrayList<>();
        attachments = new ArrayList<>();
        paymentTypes = new ArrayList<>();
        paymentMethods = new ArrayList<>();
        photoTags = new ArrayList<>();
        stringUtils = StringUtils.getInstance(true);
    }

    public ArrayList<PropertyAttachmentAddonEntity> getFurniture() {
        return furniture;
    }

    public ArrayList<PropertyAttachmentAddonEntity> getElectronics() {
        return electronics;
    }

    public ArrayList<PropertyAttachmentAddonEntity> getAttachments() {
        return attachments;
    }

    public ArrayList<PropertyAttachmentAddonEntity> getPaymentTypes() {
        return paymentTypes;
    }

    public ArrayList<PropertyAttachmentAddonEntity> getPaymentMethods() {
        return paymentMethods;
    }

    public ArrayList<PropertyAttachmentAddonEntity> getPhotoTags() {
        return photoTags;
    }

    public void parseAttachmentsAddonsResponse(String response){
        try {
            this.furniture.clear();
            this.attachments.clear();
            this.electronics.clear();
            this.paymentTypes.clear();
            this.paymentMethods.clear();
            this.photoTags.clear();

            JSONObject jsonObject = new JSONObject(response);
            JSONArray furniture = jsonObject.getJSONArray(FURNITURE);
            JSONArray electronics = jsonObject.getJSONArray(ELECTRONICS);
            JSONArray attachments = jsonObject.getJSONArray(ATTACHMENTS);
            JSONArray paymentTypes = jsonObject.getJSONArray(PAYMENT_TYPES);
            JSONArray paymentMethods = jsonObject.getJSONArray(PAYMENT_METHODS);
            JSONArray photoTags = jsonObject.getJSONArray(PROPERTY_PHOTO_TAGS);

            addAttachments(this.furniture, furniture, FURNITURE);
            addAttachments(this.electronics, electronics, ELECTRONICS);
            addAttachments(this.attachments, attachments, ATTACHMENTS);
            addAttachments(this.paymentTypes, paymentTypes, PAYMENT_TYPES);
            addAttachments(this.paymentMethods, paymentMethods, PAYMENT_METHODS);
            addTags(this.photoTags, photoTags, PROPERTY_PHOTO_TAGS);

        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    private void addTags(ArrayList<PropertyAttachmentAddonEntity> entities, JSONArray attachment
            , final String ATTACHMENT_TYPE) throws JSONException {
        for (int i = 0; i < attachment.length(); i++) {
            JSONObject currentObject = attachment.getJSONObject(i);
            String id = currentObject.getString("id");
            String title = stringUtils
                    .formattedHebrew(currentObject.getString("tag"));//TODO receiving tag in hebrew from server. consider replace it with english version
            String formattedTitle = title;//getFormattedTitle(ATTACHMENT_TYPE, title);
            entities.add(new PropertyAttachmentAddonEntity(id, title, formattedTitle));
        }
    }

    private void addAttachments(ArrayList<PropertyAttachmentAddonEntity> entities, JSONArray attachment
            , final String ATTACHMENT_TYPE) throws JSONException {
        for (int i = 0; i < attachment.length(); i++) {
            JSONObject currentObject = attachment.getJSONObject(i);
            String id = currentObject.getString("id");
            String title;
            String formatted;
            if (ATTACHMENT_TYPE.equals("attachments")){
                title = currentObject.getString( "type");
                formatted = getFormattedAttachmentsTitle(currentObject.getString("type"));
            }else {
                title = currentObject.getString( "name");
                formatted = stringUtils.formattedHebrew(currentObject.getString("name_he"));
            }

            //String formattedTitle = getFormattedTitle(ATTACHMENT_TYPE, title);
            entities.add(new PropertyAttachmentAddonEntity(id, title, formatted));
        }
    }

    public PropertyAttachmentAddonEntity getAttachmentById(String id){
        for (PropertyAttachmentAddonEntity entity : getAttachments()) {
            if(entity.id.equals(id)){
                return entity;
            }
        }
        return null;
    }

    public String getFormattedTitleById(ArrayList<PropertyAttachmentAddonEntity> entities, String id){
        for (PropertyAttachmentAddonEntity entity : entities) {
            if(entity.id.equals(id)){
                return entity.formattedTitle;
            }
        }
        return null;
    }

    private String getFormattedAttachmentsTitle(String title){
        switch (title){
            case "furnished" :
                return "מרוהטת";
            case "not_furnished":
                return "לא מרוהטת";
            case "electronics" :
                return "מוצרי חשמל";
            case "no_electronics":
                return "ללא מוצרי חשמל";
            case "parking":
                return "חנייה בבניין";
            case "renovated":
                return "משופצת";
            case "sunboiler":
                return "דוד שמש";
            case "elevator":
                return "מעלית";
            case "guard":
                return "שומר בניין";
            case "bars":
                return "סורגים";
            case "warehouse":
                return "מחסן";
            case "sunterrace":
                return "מרפסת שמש";
            case "service_balcony":
                return "מרפסת שירות";
            case "garden":
                return "גינה";
            case "pool":
                return "בריכה";
            case "animals":
                return "אפשר חיות מחמד";
            case "floor_heat":
                return "חימום רצפתי";
            case "all_included":
                return "כולל הכל";
            case "gym":
                return "חדר כושר";
            case "disabled_access":
                return "גישה לנכים";
            case "aps":
                return "ממ״ד";

            default:return title;
        }
    }

    private String getFormattedTitle(final String ATTACHMENT_TYPE, final String TITLE){
        /*switch (ATTACHMENT_TYPE) {
            case FURNITURE: default:
                return getFormattedFurnitureTitle(TITLE);
            case ELECTRONICS:
                return getFormattedElectronicsTitle(TITLE);
            case ATTACHMENTS:
                return getFormattedAttachmentsTitle(TITLE);
            case PAYMENT_TYPES:
                return getFormattedPaymentTypesTitle(TITLE);
            case PAYMENT_METHODS:
                return getFormattedPaymentMethodsTitle(TITLE);
        }*/
        return TITLE;
    }

    private String getFormattedFurnitureTitle(String title) {
        switch (title) {
            case "sofa":
                return "ספה";
            case "closet":
                return "ארון";
            case "bed":
                return "מטה";
            case "mattress":
                return "מיזרן";
            case "chair":
                return "כסא";
            case "table":
                return "שולחן";
            case "dining area":
                return "פינת אוכל";
            case "couch":
                return "כורסא";
            case "living room":
                return "סלון";

            default:
                return title;
        }
    }

    private String getFormattedElectronicsTitle(String title) {
        switch (title) {
            case "AC":
                return "מזגן";
            case "refrigerator":
                return "מקרר";
            case "tv":
                return "טלויזיה";
            case "oven":
                return "תנור";
            case "washing machine":
                return "מכונת כביסה";
            case "dryer":
                return "מייבש כביסה";
            case "dishwasher":
                return "מדיח כלים";
            case "microwave":
                return "מיקרוגל";
            case "entertainment center":
                return "מרכז בידור";

            default:
                return title;
        }
    }

    private String getFormattedPaymentTypesTitle(String title) {
        switch (title) {
            /*case "all_included":
                return "הכל כלול";*/
            case "arnona"://TODO
                return "ארנונה";
            case "house_committee":
                return "ועד בית";
            case "managment_fee"://TODO
                return "דמי ניהול";
            case "electricity":
                return "חשמל";
            case "water":
                return "מים";
            case "gas":
                return "גז";
            case "internet":
                return "אינטרנט";
            case "cables":
                return "כבלים";
            case "parking":
                return "חניה";

            default:
                return title;
        }
    }

    private String getFormattedPaymentMethodsTitle(String title) {
        switch (title) {
            case "checks":
                return "צ'קים";
            case "wiretransfer":
                return "העברה בנקאית";
            default:
                return title;
        }
    }

    private void addInPosition(int position, JSONObject jsonObj, JSONArray jsonArr) throws JSONException{
        for (int i = jsonArr.length(); i > position; i--){
            jsonArr.put(i, jsonArr.get(i-1));
        }
        jsonArr.put(position, jsonObj);
    }

}
