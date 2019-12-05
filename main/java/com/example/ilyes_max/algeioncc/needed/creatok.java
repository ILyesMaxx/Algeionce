package com.example.ilyes_max.algeioncc.needed;

import com.facebook.stetho.json.annotation.JsonProperty;
import com.squareup.moshi.Json;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class creatok {
    @Json(name = "data")
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
