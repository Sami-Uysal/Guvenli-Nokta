package com.guvenlinokta.app;

public class EmergencyNumber {
    private String name;
    private String number;

    public EmergencyNumber(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}