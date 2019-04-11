package com.gnb.products.models.base;

import com.google.gson.annotations.SerializedName;

import androidx.room.PrimaryKey;

public class BaseModel {

    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    public int Id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
