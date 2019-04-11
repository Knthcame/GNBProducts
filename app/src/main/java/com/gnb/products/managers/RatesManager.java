package com.gnb.products.managers;

import android.util.Log;

import com.gnb.products.constants.UrlConstants;
import com.gnb.products.models.RateModel;
import com.gnb.products.services.HttpService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RatesManager {
    private static final String TAG = "RatesManager";

    private static RatesManager INSTANCE;

    private RatesManager(){}

    public static synchronized RatesManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new RatesManager();
        }
        return INSTANCE;
    }

    public List<RateModel> getRates(){
        try {
            URL baseUrl = new URL(UrlConstants.BaseUrl);
            URL url = new URL(baseUrl, UrlConstants.RatesExtension);

            String response = HttpService.getInstance().Get(url);

            Type type = new TypeToken<List<RateModel>>(){}.getType();
            List<RateModel> rateModels = new Gson().fromJson(response, type);
            Log.i(TAG, "Received " + rateModels.size() + " rate models");
            return rateModels;
        }
        catch (Exception e){
            Log.e(TAG, e.toString());
            return new ArrayList<>();
        }
    }
}
