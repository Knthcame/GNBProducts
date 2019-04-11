package com.gnb.products.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.gnb.products.R;
import com.gnb.products.adapters.TransactionAdapter;
import com.gnb.products.constants.Currencies;
import com.gnb.products.constants.IntentKeys;
import com.gnb.products.models.ProductModel;
import com.gnb.products.presenters.ProductDetailPresenter;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AbsListView.OnScrollListener {

    private ProductDetailPresenter mPresenter;

    private TextView mTotalAmount;

    private Spinner mCurrencySpinner;

    private TextView mLoadedTransactions;

    private ListView mTransactionList;

    private TransactionAdapter mTransactionAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        int productId = getIntent().getIntExtra(IntentKeys.PRODUCT_ID, -1);
        if (productId == -1)
            finish();
        else {
            mPresenter = new ProductDetailPresenter(this, productId);
            InitializeView();
        }
    }

    private void InitializeView() {
        ProductModel product = mPresenter.getProductModel();
        setSupportActionBar(findViewById(R.id.my_toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.product_detail_title, product.getSku()));

        mCurrencySpinner = findViewById(R.id.currency_spinner);
        mTotalAmount = findViewById(R.id.total_amount);
        mLoadedTransactions = findViewById(R.id.loaded_transactions);
        mTransactionList = findViewById(R.id.transaction_list);

        setTotalAmount();

        mTransactionAdapter = new TransactionAdapter(this, mPresenter.getNextTransactions());
        mCurrencySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Currencies.SupportedCurrencies));
        setDefaultCurrency();

        setTransactionList();

        setLoadedTransactions();
    }

    private void setDefaultCurrency() {
        mCurrencySpinner.setSelection(Currencies.SupportedCurrencies.indexOf(mPresenter.getPreferedCurrency()));
        mCurrencySpinner.setOnItemSelectedListener(this);
    }

    private void setLoadedTransactions() {
        mLoadedTransactions.setText(getString(R.string.loaded_transactions, mTransactionList.getAdapter().getCount(), mPresenter.getProductTransactionsCount()));
    }

    private void setTransactionList() {
        mTransactionList.setAdapter(mTransactionAdapter);
        mTransactionList.setOnScrollListener(this);
    }

    private void setTotalAmount() {
        String totalAmount = getString(R.string.total_amount_title, mPresenter.getTotalAmount());
        mTotalAmount.setText(totalAmount);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mPresenter.onSpinnerItemSelected(Currencies.SupportedCurrencies.get(position));
        setTotalAmount();
        updateTransactionListCurrency();
    }

    private void updateTransactionListCurrency() {
        mTransactionAdapter.refreshTransactions(mPresenter.getCurrentTransactions());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //setDefaultCurrency();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int lastVisiblePosition = mTransactionList.getLastVisiblePosition();
        int listSize = mPresenter.getListSize();
        if(scrollState == SCROLL_STATE_IDLE && lastVisiblePosition == listSize - 1)
            mTransactionAdapter.loadMoreTransactions(mPresenter.getNextTransactions());
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //Not necessary
    }
}
