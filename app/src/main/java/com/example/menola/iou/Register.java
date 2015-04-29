package com.example.menola.iou;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Peter on 28-04-2015.
 */

public class Register {

    private long id;
    private String name;
    private String description;
    private int value;
    private LatLng latLng;

    public long getId(){
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return name + description + value;
    }
}
