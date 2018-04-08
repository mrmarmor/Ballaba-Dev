package com.example.michaelkibenko.ballaba.Holders;

import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PropertyAttachmentsAddonsHolder {
    public static final String TAG = PropertyAttachmentsAddonsHolder.class.getSimpleName();

    private static PropertyAttachmentsAddonsHolder instance;

    private ArrayList<PropertyAttachmentAddonEntity> furniture;
    private ArrayList<PropertyAttachmentAddonEntity> electronics;
    private ArrayList<PropertyAttachmentAddonEntity> attachments;

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

    public void parseAttachmentsAddonsResponse(String response){
        try {
            this.furniture.clear();
            this.attachments.clear();
            this.electronics.clear();

            JSONObject jsonObject = new JSONObject(response);
            JSONArray furniture = jsonObject.getJSONArray("furniture");
            JSONArray electronics = jsonObject.getJSONArray("electronics");
            JSONArray attachments = jsonObject.getJSONArray("attachments");

            //TODO delete it
            JSONObject not_furnished = new JSONObject();
            not_furnished.put("id" , "123455");
            not_furnished.put("title", "not_furnished");
            attachments.put(not_furnished);

            JSONObject no_electronics = new JSONObject();
            no_electronics.put("id" , "12355");
            no_electronics.put("title", "no_electronics");
            attachments.put(no_electronics);



            for (int i = 0; i < furniture.length(); i++) {
                JSONObject currentObject = furniture.getJSONObject(i);
                String id = currentObject.getString("id");
                String title = currentObject.getString("title");
                PropertyAttachmentAddonEntity entity = new PropertyAttachmentAddonEntity(id, title, getFormattedFurnitureTitle(title));
                this.furniture.add(entity);
            }

            for (int i = 0; i < electronics.length(); i++) {
                JSONObject currentObject = electronics.getJSONObject(i);
                String id = currentObject.getString("id");
                String title = currentObject.getString("title");
                PropertyAttachmentAddonEntity entity = new PropertyAttachmentAddonEntity(id, title, getFormattedElectronicsTitle(title));
                this.electronics.add(entity);
            }

            for (int i = 0; i < attachments.length(); i++) {
                JSONObject currentObject = attachments.getJSONObject(i);
                String id = currentObject.getString("id");
                String title = currentObject.getString("title");
                PropertyAttachmentAddonEntity entity = new PropertyAttachmentAddonEntity(id, title, getFormattedAttachmentsTitle(title));
                this.attachments.add(entity);
            }

        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    private String getFormattedFurnitureTitle(String title){
        return title;
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
                return "שומר בבנין";
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
                return "אפשר בע״ח";
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

    private String getFormattedElectronicsTitle(String title){
        return title;
    }
}