package com.gnb.products.daos;

import com.gnb.products.models.ProductModel;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProducts(List<ProductModel> productModels);

    @Query("SELECT * FROM productmodel WHERE id = :id")
    ProductModel getProduct(int id);

    @Query("SELECT * FROM productmodel")
    List<ProductModel> getProducts();

    @Query("DELETE FROM productmodel")
    void deleteProducts();
}
