package com.gnb.products.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gnb.products.R;
import com.gnb.products.models.ProductModel;
import com.gnb.products.viewholders.ProductViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProductAdapter extends ArrayAdapter<ProductModel>{
    private static final String TAG = "ProductAdapter";
    private Context mContext;

    public ProductAdapter(@NonNull Context context, List<ProductModel> products) {
        super(context, R.layout.product, products);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ProductModel product = getItem(position);

        ProductViewHolder holder;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        if(view == null){
            view = inflater.inflate(R.layout.product, parent, false);
            holder = new ProductViewHolder();
            holder.Sku = view.findViewById(R.id.sku);
            holder.NumberOfTransactions = view.findViewById(R.id.number_of_transactions);
            view.setTag(holder);
        }
        else{
            holder = (ProductViewHolder) view.getTag();
        }

        holder.Sku.setText(product.Sku);
        holder.NumberOfTransactions.setText(String.valueOf(product.NumberOfTransactions));

        return view;
    }
}
