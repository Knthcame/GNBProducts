package com.gnb.products.listeners;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.gnb.products.activities.ProductDetailActivity;
import com.gnb.products.constants.IntentKeys;
import com.gnb.products.models.ProductModel;

import java.util.List;

public class OnProductClickListener implements AdapterView.OnItemClickListener {
    private static final String TAG = "ProductClickListener";
    private final Context mContext;
    private final List<ProductModel> productsList;

    public OnProductClickListener(Context context, List<ProductModel> products){
        mContext = context;
        productsList = products;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            ProductModel productModel = productsList.get(position);
            if(productModel != null) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra(IntentKeys.PRODUCT_ID, productModel.Id);
                mContext.startActivity(intent);
            }
        }
        catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }

}
