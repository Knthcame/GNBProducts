package com.gnb.products.models;

import com.gnb.products.models.base.BaseModel;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class RateModel extends BaseModel {

    @SerializedName("from")
    @ColumnInfo(name = "from")
    public String FromCurrency;

    @SerializedName("to")
    @ColumnInfo(name = "to")
    public String ToCurrency;

    @SerializedName("rate")
    @ColumnInfo(name = "rate")
    public double Rate;

    public double getRate() {
        return Rate;
    }

    public String getFromCurrency() {
        return FromCurrency;
    }

    public String getToCurrency() {
        return ToCurrency;
    }

    public void setRate(int rate) {
        Rate = rate;
    }

    public void setFromCurrency(String fromCurrency) {
        FromCurrency = fromCurrency;
    }

    public void setToCurrency(String toCurrency) {
        ToCurrency = toCurrency;
    }

    @NonNull
    @Override
    public String toString() {
        return "From: " + FromCurrency + " to " + ToCurrency + ": x" + Rate;
    }
}
