package com.guvenlinokta.app;

public class DisasterInfo {
    private String name;
    private int iconResId;

    public DisasterInfo(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }

    @Override
    public String toString() {
        return name;
    }
}
