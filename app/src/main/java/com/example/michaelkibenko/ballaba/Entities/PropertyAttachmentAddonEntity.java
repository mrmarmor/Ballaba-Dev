package com.example.michaelkibenko.ballaba.Entities;

public class PropertyAttachmentAddonEntity extends BallabaBaseEntity {
    public String id;
    public String title;
    public String formattedTitle;

    public PropertyAttachmentAddonEntity(String id, String title, String formattedTitle) {
        this.id = id;
        this.title = title;
        this.formattedTitle = formattedTitle;
    }
}
