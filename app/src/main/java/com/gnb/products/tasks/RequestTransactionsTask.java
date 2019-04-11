package com.gnb.products.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.gnb.products.managers.TransactionsManager;
import com.gnb.products.models.TransactionModel;
import com.gnb.products.services.DatabaseClient;

import java.util.List;

public class RequestTransactionsTask extends AsyncTask<Void, Void, List<TransactionModel>> {
    private static final String TAG = "TransactionsRequest";
    protected final Context mContext;

    public RequestTransactionsTask(Context context){
        mContext = context;
    }

    @Override
    protected List<TransactionModel> doInBackground(Void... voids) {
        List<TransactionModel> transactions = TransactionsManager.getInstance().getTransactions();
        return transactions;
    }

    @Override
    protected void onPostExecute(List<TransactionModel> transactions) {
        super.onPostExecute(transactions);
        DatabaseClient.getInstance(mContext).insertTransactions(transactions);
    }
}
