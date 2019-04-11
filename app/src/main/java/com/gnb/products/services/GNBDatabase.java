package com.gnb.products.services;

import com.gnb.products.daos.ProductsDao;
import com.gnb.products.daos.RatesDao;
import com.gnb.products.daos.TransactionsDao;
import com.gnb.products.models.ProductModel;
import com.gnb.products.models.RateModel;
import com.gnb.products.models.TransactionModel;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {
            RateModel.class,
            TransactionModel.class,
            ProductModel.class
        },
        version = 1)
public abstract class GNBDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "GNB_DATABASE.db3";

    public abstract RatesDao ratesDao();

    public abstract TransactionsDao transactionsDao();

    public abstract ProductsDao productsDao();
}
