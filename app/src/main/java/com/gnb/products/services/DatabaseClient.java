package com.gnb.products.services;

import android.content.Context;
import android.util.Log;

import com.gnb.products.models.ProductModel;
import com.gnb.products.models.RateModel;
import com.gnb.products.models.TransactionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.room.Room;

public class DatabaseClient {
    private static final String TAG = "DatabaseClient";

    private static DatabaseClient INSTANCE;
    private final GNBDatabase database;

    private DatabaseClient(Context ctx){
        database = Room.databaseBuilder(ctx, GNBDatabase.class, GNBDatabase.DATABASE_NAME).build();
    }

    public static synchronized DatabaseClient getInstance(Context ctx){
        if(INSTANCE == null){
                INSTANCE = new DatabaseClient(ctx);
        }
        return INSTANCE;
    }

    public List<RateModel> getRates(){
        List<RateModel> result;
        try {
            Future<List<RateModel>> future = Executors.newSingleThreadExecutor()
                    .submit(() -> database.ratesDao().getRates());

            result = future.get();
        }
        catch (Exception e){
            Log.e(TAG, e.toString());
            result = new ArrayList<>();
        }

        Log.i(TAG, "Retrieved " + result.size() + " rates from db");
        return result;
    }

    public List<TransactionModel> getTransactions(){
        List<TransactionModel> result;
        try {
            Future<List<TransactionModel>> future = Executors.newSingleThreadExecutor()
                    .submit(() -> database.transactionsDao().getTransactions());

            result = future.get();
        }
        catch (Exception e){
            Log.e(TAG, e.toString());
            result = new ArrayList<>();
        }

        Log.i(TAG, "Retrieved " + result.size() + " transactions from db");
        return result;
    }

    public List<TransactionModel> getProductTransactions(String sku){
        List<TransactionModel> result;
        try {
            Future<List<TransactionModel>> future = Executors.newSingleThreadExecutor()
                    .submit(() -> database.transactionsDao().getProductTransactions(sku));

            result = future.get();
        }
        catch (Exception e){
            Log.e(TAG, e.toString());
            result = new ArrayList<>();
        }

        Log.i(TAG, "Retrieved " + result.size() + " transactions from db for product " + sku);
        return result;
    }

    public List<ProductModel> getProducts(){
        List<ProductModel> result;
        try{
            Future<List<ProductModel>> future = Executors.newSingleThreadExecutor()
                    .submit(() -> database.productsDao().getProducts());

            result = future.get();
        }
        catch (Exception e){
            Log.e(TAG, e.toString());
            result = new ArrayList<>();
        }

        Log.i(TAG, "Retrieved " + result.size() + " products from db");
        return result;
    }

    public ProductModel getProduct(int id){
        ProductModel result;
        try{
            Future<ProductModel> future = Executors.newSingleThreadExecutor()
                    .submit(() -> database.productsDao().getProduct(id));

            result = future.get();

            Log.i(TAG,"Retrieved product with id " + result.getId());
        }
        catch (Exception e){
            Log.e(TAG, e.toString());
            result = null;
        }
        return result;
    }

    public int getProductTransactionsCount(String sku){
        int result;
        try {
            Future<Integer> future = Executors.newSingleThreadExecutor()
                    .submit(() -> database.transactionsDao().getProductTransactionsCount(sku));
            result = future.get();
        }
        catch (Exception e){
            Log.e(TAG, e.toString());
            result = 0;
        }
        Log.i(TAG, "Product " + sku + " has " + result + " transactions");
        return result;
    }

    public Future insertTransactions(final List<TransactionModel> transactions){
        Future future = Executors.newSingleThreadExecutor()
                .submit(() -> database.transactionsDao().insertTransactions(transactions));
        Log.i(TAG, "Inserted " + transactions.size() + " transactions in database");
        return future;
    }

    public Future insertRates(final List<RateModel> rates){
        Future future = Executors.newSingleThreadExecutor()
                .submit(() -> database.ratesDao().insertRates(rates));
        Log.i(TAG, "Inserted " + rates.size() + " rates in database");
        return future;
    }

    public Future insertProducts(final List<ProductModel> products){
        Future future = Executors.newSingleThreadExecutor()
                .submit(() -> database.productsDao().insertProducts(products));
        Log.i(TAG, "Inserted " + products.size() + " products in database");
        return future;
    }

    public Future clearAllTables(){
        Future future = Executors.newSingleThreadExecutor()
                .submit(database::clearAllTables);
        Log.i(TAG, "Cleared database");
        return future;
    }
}
