package com.guvenlinokta.app;

import java.util.List;

public class WeatherResponse {
    public Main main;
    public List<Weather> weather;
    public Wind wind;
    public String name;

    public static class Main {
        public float temp;
        public int humidity;
    }

    public static class Weather {
        public String description;
        public String icon;
    }

    public static class Wind {
        public float speed;
    }
}