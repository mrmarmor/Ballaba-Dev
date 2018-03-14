package com.example.michaelkibenko.ballaba.Managers;

import android.content.Context;

import com.example.michaelkibenko.ballaba.Entities.BallabaProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 14/03/2018.
 */

public class PropertiesManager {
    private static PropertiesManager instance;
    private Context context;
    private List<BallabaProperty> properties = new ArrayList<>();

    public static PropertiesManager getInstance(Context context) {
        if(instance == null){
            instance = new PropertiesManager(context);
        }
        return instance;
    }

    private PropertiesManager(Context context){
        this.context = context;
    }

    public List<BallabaProperty> getProperties() {
        return properties;
    }
    public void setProperties(List<BallabaProperty> properties){
        this.properties = properties;
    }

    // Properties management tools methods
    public void addProperties(List<BallabaProperty> properties){
        this.properties.addAll(properties);
    }
    public void addProperty(BallabaProperty property){
        properties.add(property);
    }
    public void removeProperty(BallabaProperty property){
        properties.remove(property);
    }
    public void removeProperty(int position){
        properties.remove(position);
    }
    public BallabaProperty getProperty(int position){
        return properties.get(position);
    }
    public BallabaProperty getPropertyById(String id){
        for (BallabaProperty property : properties)
            if (property.id().equals(id))
                return property;

        return null;
    }
}
