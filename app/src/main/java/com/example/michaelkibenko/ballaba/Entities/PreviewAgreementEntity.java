package com.example.michaelkibenko.ballaba.Entities;

public class PreviewAgreementEntity {

    private String title , content;
    private boolean isContentVisible;

    public PreviewAgreementEntity(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isContentVisible() {
        return isContentVisible;
    }

    public void setContentVisible(boolean contentVisible) {
        isContentVisible = contentVisible;
    }
}
