package com.gnb.products.models;

import com.gnb.products.models.base.BaseModel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class ProductModel extends BaseModel {
    @ColumnInfo(name = "sku")
    public String Sku;

    @ColumnInfo(name = "number_of_transactions")
    public int NumberOfTransactions;

    public String getSku() {
        return Sku;
    }

    public void setSku(String sku) {
        Sku = sku;
    }

    public int getNumberOfTransactions() {
        return NumberOfTransactions;
    }

    public void setNumberOfTransactions(int numberOfTransactions) {
        NumberOfTransactions = numberOfTransactions;
    }
}
