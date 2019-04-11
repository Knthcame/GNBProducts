package com.gnb.products.converters;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.gnb.products.models.RateModel;
import com.gnb.products.models.TransactionModel;
import com.gnb.products.services.DatabaseClient;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import androidx.annotation.RequiresApi;

public class CurrencyConverter {
    private static final String TAG = "CurrencyConverter";
    private final Context mContext;

    public CurrencyConverter(Context context){
        mContext = context;
    }

    public List<TransactionModel> convertToCurrency(List<TransactionModel> list, String currency){
        List<TransactionModel> result = new ArrayList<>();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            list.forEach((transaction) -> convertToCurrency(transaction, currency));
            result = list;
        }
        else{
            for (TransactionModel transaction : list) {
                result.add(convertToCurrency(transaction, currency));
            }
        }
        return result;
    }

    public TransactionModel convertToCurrency(TransactionModel transaction, String currency){
        String currentCurrency = transaction.getCurrency();
        double conversion = findConversion(currentCurrency, currency);
        transaction.setCurrency(currency);
        transaction.setAmount(transaction.getAmount() * conversion);
        return transaction;
    }

    public double convertToCurrency(double amount, String fromCurrency, String toCurrency){
        double conversion = findConversion(fromCurrency, toCurrency);
        return amount * conversion;
    }

    private double findConversion(String fromCurrency, String toCurrency) {
        double result = 0;
        if(fromCurrency.equals(toCurrency))
            return 1;

        List<RateModel> rates = DatabaseClient.getInstance(mContext).getRates();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            RateModel rateModel = getNougatSimpleConversion(fromCurrency, toCurrency, rates);
            if(rateModel == null){
                //Check inverse relationship
                rateModel = getNougatSimpleConversion(toCurrency, fromCurrency, rates);
                if(rateModel == null){
                    result = GetNougatComplexConversion(rates, fromCurrency, toCurrency);
                }
                else {
                    result = 1/rateModel.getRate();
                }
            }
            else {
                result = rateModel.Rate;
            }
        }else{
            Collection<RateModel> filteredRates = getSimpleConversion(fromCurrency, toCurrency, rates);
            if(filteredRates.isEmpty()) {
                //Check inverse conversion
                filteredRates = getSimpleConversion(toCurrency, fromCurrency, rates);
                if (filteredRates.isEmpty()) {
                    result = GetComplexConversion(rates, fromCurrency, toCurrency);
                }
            }else{
                result = Iterables.get(filteredRates, 0).getRate();
            }
        }

        return result;
    }

    private Collection<RateModel> getSimpleConversion(String fromCurrency, String toCurrency, List<RateModel> rates) {
        return Collections2.filter(rates, (rate) -> rate != null
                && rate.getFromCurrency().equals(fromCurrency)
                && rate.getToCurrency().equals(toCurrency));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private RateModel getNougatSimpleConversion(String fromCurrency, String toCurrency, List<RateModel> rates) {
        return rates.stream().filter((rate) -> rate.getFromCurrency().equals(fromCurrency) &&
                                                                      rate.getToCurrency().equals(toCurrency))
                        .findFirst().orElse(null);
    }

    private double GetComplexConversion(List<RateModel> rates, String fromCurrency, String toCurrency) {
        double result = 0;
        Collection<RateModel> fromRates =
                Collections2.filter(rates, rate -> rate.getFromCurrency().equals(fromCurrency));

        Collection<RateModel> toRates =
                Collections2.filter(rates, rate -> rate.getToCurrency().equals(toCurrency));

        for(RateModel currentRate : fromRates){
            Collection<RateModel> filteredRates =
                    Collections2.filter(toRates, rate -> rate.getFromCurrency().equals(currentRate.getToCurrency()));
            if(!filteredRates.isEmpty()){
                RateModel toRate = Iterables.get(filteredRates, 0);
                result = currentRate.getRate() * toRate.getRate();
            }
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private double GetNougatComplexConversion(List<RateModel> rates, String fromCurrency, String toCurrency) {
        double result = 0;
        List<RateModel> fromRates = rates.stream()
                .filter(rate -> rate.getFromCurrency().equals(fromCurrency))
                .collect(Collectors.toList());

        List<RateModel> toRates = rates.stream()
                .filter(rate -> rate.getToCurrency().equals(toCurrency))
                .collect(Collectors.toList());

        for(RateModel currentRate : fromRates){
            RateModel toRate = toRates.stream()
                    .filter(rate -> rate.getFromCurrency().equals(currentRate.getToCurrency()))
                    .findFirst().orElse(null);
            if(toRate != null){
                result = currentRate.getRate() * toRate.getRate();
                break;
            }
        }

        if(result == 0)
            result = GetIterativeConversion(rates, fromCurrency, toCurrency);

        return result;
    }

    //Could work with only this method, but is can take a longer than needed route that generates a less accurate conversion in some cases
    //Missing support for lower than api 24 (replicate with Collections2 as the other methods)
    @RequiresApi(api = Build.VERSION_CODES.N)
    private double GetIterativeConversion(List<RateModel> rates, String fromCurrency, String toCurrency) {
        int numberAttempts = 0;
        double result = 0;
        String filter = fromCurrency;
        double conversion = 1;
        do{
            String finalFilter = filter;
            List<RateModel> fromRates = rates.stream()
                    .filter(rate -> rate.getFromCurrency().equals(finalFilter))
                    .collect(Collectors.toList());

            Optional<RateModel> rate = fromRates.stream().
                    filter(x -> x.getToCurrency().equals(toCurrency))
                    .findFirst();
            if(rate.isPresent()){
                result = conversion * rate.get().getRate();
            }
            else {
                RateModel firstFromRates = fromRates.get(0);
                conversion = conversion * firstFromRates.getRate();
                filter = firstFromRates.getToCurrency();
            }
            numberAttempts++;
        }
        while (numberAttempts <20 && result == 0);

        Log.d(TAG, String.format("Looped %1%d times. Conversion: %2%s to %3$s x%4$f", numberAttempts, fromCurrency, toCurrency, result));
        return result;
    }
}
