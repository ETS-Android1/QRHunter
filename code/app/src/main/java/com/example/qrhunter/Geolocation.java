package com.example.qrhunter;

import java.util.ArrayList;

public class Geolocation {
    Float latitude;
    Float longitude;
    ArrayList<Float> coordinate = new ArrayList<Float>();

    public Geolocation(Float latitude, Float longitude) {
        coordinate.add(latitude);
        coordinate.add(longitude);
    }
}
