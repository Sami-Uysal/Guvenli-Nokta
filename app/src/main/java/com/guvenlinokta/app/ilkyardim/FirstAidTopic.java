package com.guvenlinokta.app.ilkyardim;

import java.io.Serializable;

public class FirstAidTopic implements Serializable {
    private String title;
    private String description;

    public FirstAidTopic(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}