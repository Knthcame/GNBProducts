package com.gnb.products.daos;

import com.gnb.products.models.TransactionModel;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface TransactionsDao {
    @Query("SELECT * FROM transactionmodel")
    List<TransactionModel> getTransactions();

    @Query("SELECT * FROM transactionmodel WHERE sku = :sku")
    List<TransactionModel> getProductTransactions(String sku);

    @Query("SELECT COUNT(id) FROM transactionmodel WHERE sku = :sku")
    int getProductTransactionsCount(String sku);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTransactions(List<TransactionModel> transactionModels);

    @Query("DELETE FROM transactionmodel")
    void deleteTransactions();
}
