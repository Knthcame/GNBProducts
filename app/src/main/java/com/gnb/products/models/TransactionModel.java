package com.gnb.products.models;

import com.gnb.products.models.base.BaseModel;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class TransactionModel extends BaseModel {

    @SerializedName("sku")
    @ColumnInfo(name = "sku")
    public String Sku;

    @SerializedName("amount")
    @ColumnInfo(name = "amount")
    public double Amount;

    @SerializedName("currency")
    @ColumnInfo(name = "currency")
    public String Currency;

    public String getSku() {
        return Sku;
    }

    public void setSku(String sku) {
        Sku = sku;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    @NonNull
    @Override
    public String toString() {
        return Sku + "(" + Amount+ "" + Currency +")";
    }
}
