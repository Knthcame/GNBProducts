package com.gnb.products.presenters;

import android.content.Context;
import android.os.Build;

import com.gnb.products.listeners.OnDataRequestListener;
import com.gnb.products.models.ProductModel;
import com.gnb.products.models.RateModel;
import com.gnb.products.models.TransactionModel;
import com.gnb.products.services.DatabaseClient;
import com.gnb.products.tasks.RequestRatesTask;
import com.gnb.products.tasks.RequestTransactionsTask;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class MainActivityPresenter {
    private static final String TAG = "MainPresenter";
    private final Context mContext;
    private DatabaseClient database;

    public MainActivityPresenter(Context ctx) {
        mContext = ctx;
        database = DatabaseClient.getInstance(ctx);
    }

    public void RequestData(final OnDataRequestListener listener){
        database.clearAllTables();
        new RequestAndNotifyDataTask(mContext, listener).execute();
    }

    public interface View{
        void setLoadingAdapter();

        void RequestData();
    }

    static class RequestAndNotifyDataTask extends RequestRatesTask{
        private OnDataRequestListener mListener;

        public RequestAndNotifyDataTask(Context context, OnDataRequestListener listener){
            super(context);
            mListener = listener;
        }
        @Override
        protected void onPostExecute(List<RateModel> rates) {
            super.onPostExecute(rates);
            new RequestAndNotifyTransactionsTask(mContext, mListener).execute();
        }
    }

    private static class RequestAndNotifyTransactionsTask extends RequestTransactionsTask{
        private OnDataRequestListener mListener;

        public RequestAndNotifyTransactionsTask(Context context, OnDataRequestListener listener){
            super(context);
            mListener = listener;
        }

        @Override
        protected void onPostExecute(List<TransactionModel> transactions) {
            super.onPostExecute(transactions);
            onTransactionsReceived(mListener, transactions);
        }

        private void onTransactionsReceived(OnDataRequestListener listener, List<TransactionModel> transactions) {
            List<ProductModel> products = new ArrayList<>();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                List<String> skus = transactions.stream()
                        .map(TransactionModel::getSku)
                        .distinct()
                        .collect(Collectors.toList());

                for (String sku : skus) {
                    int number = (int) transactions.stream()
                            .filter(x -> x.Sku.equals(sku))
                            .count();

                    ProductModel product = new ProductModel();
                    product.setSku(sku);
                    product.setNumberOfTransactions(number);
                    products.add(product);
                }
            }
            else{
                while (transactions.size() > 0){
                    TransactionModel transaction = transactions.get(0);
                    ProductModel product = new ProductModel();
                    product.setSku(transaction.Sku);

                    Collection<TransactionModel> filteredList = Collections2.filter(transactions, input -> input != null && input.Sku.equals(transaction.Sku));
                    int number = filteredList.size();
                    product.setNumberOfTransactions(number);

                    products.add(product);

                    transactions.removeAll(filteredList);
                }
            }
            try {
                Future future = DatabaseClient.getInstance(mContext).insertProducts(products);
                future.get();
                listener.onSuccess();
            }
            catch (Exception e){
                listener.onFailure(e);
            }
        }
    }
}
