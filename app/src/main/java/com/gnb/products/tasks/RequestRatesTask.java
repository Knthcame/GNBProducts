package com.gnb.products.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.gnb.products.managers.RatesManager;
import com.gnb.products.models.RateModel;
import com.gnb.products.services.DatabaseClient;

import java.util.List;

public class RequestRatesTask extends AsyncTask<Void, Void, List<RateModel>>{
    private static final String TAG = "RatesRequest";
    protected Context mContext;

    public RequestRatesTask(Context context){
        mContext = context;
    }

    @Override
    protected List<RateModel> doInBackground(Void... contexts){
        List<RateModel> rates = RatesManager.getInstance().getRates();
        return rates;
    }

    @Override
    protected void onPostExecute(List<RateModel> rates) {
        super.onPostExecute(rates);
        DatabaseClient.getInstance(mContext).insertRates(rates);
    }
}
