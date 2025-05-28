package com.guvenlinokta.app;

public class DisasterInfo {
    private String name;
    private String description;
    private int gifResourceId;
    public DisasterInfo(String name, String description, int gifResourceId) {
        this.name = name;
        this.description = description;
        this.gifResourceId = gifResourceId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getGifResourceId() {
        return gifResourceId;
    }

    @Override
    public String toString() { // Spinner'da görünmesi için
        return name;
    }
}