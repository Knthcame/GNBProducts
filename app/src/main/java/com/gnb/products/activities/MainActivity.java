package com.gnb.products.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gnb.products.R;
import com.gnb.products.listeners.OnDataRequestListener;
import com.gnb.products.presenters.MainActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MainActivityPresenter.View {
    private static final String TAG = "MainActivity";
    private MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.my_toolbar));

        presenter = new MainActivityPresenter(this);
        setLoadingAdapter();
        RequestData();
    }

    @Override
    public void setLoadingAdapter() {
        ListView list = findViewById(R.id.product_item_list);
        List<String> loading = new ArrayList<String>(){};
        loading.add(getString(R.string.loading));
        loading.add(null);
        list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, loading));
    }

    @Override
    public void RequestData() {
        ListView list = findViewById(R.id.product_item_list);
        presenter.RequestData(new OnDataRequestListener(this, list));
    }
}
