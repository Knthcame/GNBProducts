package com.gnb.products.managers;

import android.util.Log;

import com.gnb.products.constants.UrlConstants;
import com.gnb.products.models.TransactionModel;
import com.gnb.products.services.HttpService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TransactionsManager {
    private static final String TAG = "TransactionsManager";

    private static TransactionsManager INSTANCE;

    private TransactionsManager(){}

    public static synchronized TransactionsManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new TransactionsManager();
        }
        return INSTANCE;
    }

    public List<TransactionModel> getTransactions(){
        try {
            URL baseUrl = new URL(UrlConstants.BaseUrl);
            URL url = new URL(baseUrl, UrlConstants.TransactionsExtension);

            String response = HttpService.getInstance().Get(url);

            Type type = new TypeToken<List<TransactionModel>>(){}.getType();
            List<TransactionModel> transactionModels = new Gson().fromJson(response, type);
            Log.i(TAG, "Received " + transactionModels.size() + " transaction models");
            return transactionModels;
        }
        catch (Exception e){
            Log.e(TAG, e.toString());
            return new ArrayList<>();
        }
    }
}
