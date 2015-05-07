package com.example.menola.iou;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Peter on 28-04-2015.
 */

public class Register {

    private long id;
    private Integer user_id;
    private String description;
    private float value;
    private LatLng latLng;

    public long getId(){
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getuser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
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
        return user_id + description + value;
    }
}
