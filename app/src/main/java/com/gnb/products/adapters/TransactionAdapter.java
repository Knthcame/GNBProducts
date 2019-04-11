package com.gnb.products.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gnb.products.R;
import com.gnb.products.models.TransactionModel;
import com.gnb.products.viewholders.TransactionViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TransactionAdapter extends ArrayAdapter<TransactionModel> {
    private static final String TAG = "TransactionAdapter";
    private Context mContext;
    private List<TransactionModel> mTransactions;

    public TransactionAdapter(@NonNull Context context, List<TransactionModel> transactionModels) {
        super(context, R.layout.product, transactionModels);
        mContext = context;
        mTransactions = transactionModels;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        TransactionModel transaction = getItem(position);

        TransactionViewHolder holder;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        if(view == null){
            view = inflater.inflate(R.layout.transaction, parent, false);
            holder = new TransactionViewHolder();
            holder.Amount = view.findViewById(R.id.transaction_amount);
            holder.Currency = view.findViewById(R.id.transaction_currency);
            view.setTag(holder);
        }
        else{
            holder = (TransactionViewHolder) view.getTag();
        }

        String amount = String.format("%.2f", transaction.getAmount());

        holder.Amount.setText(amount);
        holder.Currency.setText(String.valueOf(transaction.getCurrency()));

        return view;
    }

    public void loadMoreTransactions(List<TransactionModel> transactionModels){
        mTransactions.addAll(transactionModels);
        notifyDataSetChanged();
    }
}
