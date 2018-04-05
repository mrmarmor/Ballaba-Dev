package com.example.michaelkibenko.ballaba.Holders;

import android.util.Log;

import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
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


            for (int i = 0; i < furniture.length(); i++) {
                JSONObject currentObject = furniture.getJSONObject(i);
                String id = currentObject.getString("id");
                String title = currentObject.getString("title");
                PropertyAttachmentAddonEntity entity = new PropertyAttachmentAddonEntity(id, title, title);
                this.furniture.add(entity);
            }

            for (int i = 0; i < electronics.length(); i++) {
                JSONObject currentObject = electronics.getJSONObject(i);
                String id = currentObject.getString("id");
                String title = currentObject.getString("title");
                PropertyAttachmentAddonEntity entity = new PropertyAttachmentAddonEntity(id, title, title);
                this.electronics.add(entity);
            }

            for (int i = 0; i < attachments.length(); i++) {
                JSONObject currentObject = attachments.getJSONObject(i);
                String id = currentObject.getString("id");
                String title = currentObject.getString("title");
                PropertyAttachmentAddonEntity entity = new PropertyAttachmentAddonEntity(id, title, title);
                this.attachments.add(entity);
            }

            Log.e(TAG, "vfdvfd");
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }
}
