package com.gnb.products.daos;

import com.gnb.products.models.RateModel;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface RatesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRates(List<RateModel> rateModels);

    @Query("SELECT * FROM ratemodel")
    List<RateModel> getRates();

    @Query("DELETE FROM ratemodel")
    void deleteRates();
}
