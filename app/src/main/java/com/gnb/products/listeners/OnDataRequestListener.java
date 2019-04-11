package com.gnb.products.listeners;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.gnb.products.adapters.ProductAdapter;
import com.gnb.products.listeners.contracts.OnEventListener;
import com.gnb.products.models.ProductModel;
import com.gnb.products.services.DatabaseClient;

import java.util.List;

public class OnDataRequestListener implements OnEventListener {
    private static final String TAG = "DataRequestListener";
    private final Context mContext;
    private ListView listView;

    public OnDataRequestListener(Context context, ListView list){
        listView = list;
        mContext = context;
    }

    @Override
    public void onSuccess() {
        List<ProductModel> products = DatabaseClient.getInstance(mContext).getProducts();
        listView.setAdapter(new ProductAdapter(mContext, products));
        listView.setOnItemClickListener(new OnProductClickListener(mContext, products));
    }

    @Override
    public void onFailure(Exception e) {
        Log.e(TAG, e.toString());
    }
}
