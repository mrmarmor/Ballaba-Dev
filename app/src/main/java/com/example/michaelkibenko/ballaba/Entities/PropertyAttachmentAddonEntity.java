package com.example.michaelkibenko.ballaba.Entities;

import java.io.Serializable;

public class PropertyAttachmentAddonEntity extends BallabaBaseEntity implements Serializable{
    public String id;
    public String title;
    public String formattedTitle;

    public PropertyAttachmentAddonEntity(String id, String title, String formattedTitle) {
        this.id = id;
        this.title = title;
        this.formattedTitle = formattedTitle;
    }
}
