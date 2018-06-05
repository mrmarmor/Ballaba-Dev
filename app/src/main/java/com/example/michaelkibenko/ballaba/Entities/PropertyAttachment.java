package com.example.michaelkibenko.ballaba.Entities;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

import com.example.michaelkibenko.ballaba.Common.BallabaConnectivityAnnouncer;
import com.example.michaelkibenko.ballaba.R;

import java.util.ArrayList;

/**
 * Created by User on 10/04/2018.
 */

public class PropertyAttachment {
    public String id, type, title;
    public @DrawableRes int icon;

    public PropertyAttachment(String id, String type, String title, int icon) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.icon = icon;
    }

    public enum Type /*implements Serializable*/ {
        /*TODO missing icon*/FURNISHED(1, R.string.attach_furnished, R.drawable.bed_blue_24),
        /*TODO missing icon*/ELECTRONICS(2, R.string.attach_electronics, R.drawable.bed_blue_24),
        PARKING(3, R.string.attach_parking, R.drawable.combined_shape),
        /*TODO missing icon*/RENOVATED(4, R.string.attach_renovated, R.drawable.bed_blue_24),
        /*TODO missing icon*/SUN_BOILER(5, R.string.attach_sunboiler, R.drawable.bed_blue_24),
        /*TODO missing icon*/ELEVATOR(6, R.string.attach_elevator, R.drawable.bed_blue_24),
        /*TODO missing icon*/GUARD(7, R.string.attach_guard, R.drawable.bed_blue_24),
        /*TODO missing icon*/BARS(8, R.string.attach_bars, R.drawable.bed_blue_24),
        /*TODO missing icon*/WAREHOUSE(9, R.string.attach_warehouse, R.drawable.bed_blue_24),
        /*TODO missing icon*/SUN_TERRACE(10, R.string.attach_sunterrace, R.drawable.bed_blue_24),
        /*TODO missing icon*/SERVICE_BALCONY(11, R.string.attach_service_balcony, R.drawable.bed_blue_24),
        /*TODO missing icon*/GARDEN(12, R.string.attach_garden, R.drawable.bed_blue_24),
        POOL(13, R.string.attach_pool, R.drawable.pool_blue_24),
        /*TODO missing icon*/ANIMALS(14, R.string.attach_animals, R.drawable.bed_blue_24),
        /*TODO missing icon*/FLOOR_HEAT(15, R.string.attach_floor_heat, R.drawable.bed_blue_24),
        /*TODO missing icon*/ALL_INCLUDED(16, R.string.attach_all_included, R.drawable.bed_blue_24),
        /*TODO missing icon*/GYM(17, R.string.attach_gym, R.drawable.bed_blue_24),
        /*TODO missing icon*/DISABLED_ACCESS(18, R.string.attach_disabled_access, R.drawable.bed_blue_24),
        /*TODO missing icon*/APS(19, R.string.attach_aps, R.drawable.bed_blue_24);
        //NO_SMOKING()

        int id, title, icon;
        Type(int resIntId, int resIntTitle, int resIntIcon) {
            this.id = resIntId;
            this.title = resIntTitle;
            this.icon = resIntIcon;
        }

        /*private static Type instance;
        public static Type getInstance() {
            return instance;
        }*/
        //private Type(){}

        public int getId() { return id; }
        public int getTitle() { return title; }
        public int getIcon() { return icon; }

        public static Type getTypeById(String id){
            for (Type type : Type.values()){
                if (type.id == Integer.parseInt(id))
                    return type;
            }

            return null;
        }
    }

}
