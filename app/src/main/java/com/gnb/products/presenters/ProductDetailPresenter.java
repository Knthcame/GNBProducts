package com.gnb.products.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.gnb.products.constants.Currencies;
import com.gnb.products.constants.PreferencesConstants;
import com.gnb.products.converters.CurrencyConverter;
import com.gnb.products.models.ProductModel;
import com.gnb.products.models.TransactionModel;
import com.gnb.products.services.DatabaseClient;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ProductDetailPresenter {
    private static final int LIST_INCREMENT = 10;
    private ProductModel mProduct;
    private Context mContext;
    private CurrencyConverter mCurrencyConverter;
    private SharedPreferences mPreferences;
    private double totalAmount;
    private int mListSize = 0;
    private String totalAmountCurrency;

    public ProductDetailPresenter(Context context, int productId){
        mContext = context;
        mProduct = DatabaseClient.getInstance(context).getProduct(productId);
        mCurrencyConverter = new CurrencyConverter(context);
        mPreferences = context.getSharedPreferences(PreferencesConstants.FILE_NAME, Context.MODE_PRIVATE);
    }

    public ProductModel getProductModel(){
        return mProduct;
    }

    public double getTotalAmount(){
        if(totalAmount == 0){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                AtomicReference<Double> amount = new AtomicReference<>((double) 0);
                getProductTransactions()
                        .stream()
                        .map(TransactionModel::getAmount)
                        .forEach(x -> amount.updateAndGet(v -> (double) (v + x)));
                totalAmount = amount.get();
            }
            else{
                double amount = 0;
                for (TransactionModel transaction : getProductTransactions()) {
                    amount += transaction.getAmount();
                }
                totalAmount = amount;
            }
            totalAmountCurrency = getPreferedCurrency();
        }
        return mCurrencyConverter.convertToCurrency(totalAmount, totalAmountCurrency, getPreferedCurrency());
    }

    private List<TransactionModel> getProductTransactions(){
        List<TransactionModel> transactions = DatabaseClient.getInstance(mContext)
                .getProductTransactions(mProduct.Sku);
        return transactions;
    }

    public List<TransactionModel> getNextTransactions(){
        List<TransactionModel> transactions = getProductTransactions().subList(mListSize, mListSize + LIST_INCREMENT);
        mListSize = mListSize + LIST_INCREMENT;
        return mCurrencyConverter.convertToCurrency(transactions, getPreferedCurrency());
    }

    public List<TransactionModel> getCurrentTransactions(){
        List<TransactionModel> transactions = getProductTransactions().subList(0, mListSize);
        return mCurrencyConverter.convertToCurrency(transactions, getPreferedCurrency());
    }

    public String getPreferedCurrency(){
        return mPreferences.getString(PreferencesConstants.PREFERED_CURRENCY, Currencies.EURO);
    }

    public void onSpinnerItemSelected(String toCurrency){
        mPreferences.edit().putString(PreferencesConstants.PREFERED_CURRENCY, toCurrency).apply();
    }

    public int getProductTransactionsCount(){
        return DatabaseClient.getInstance(mContext).getProductTransactionsCount(mProduct.getSku());
    }

    public int getListSize(){
        return mListSize;
    }
}
