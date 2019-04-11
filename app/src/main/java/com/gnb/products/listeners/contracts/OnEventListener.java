package com.gnb.products.listeners.contracts;

public interface OnEventListener {
    void onSuccess();

    void onFailure(Exception e);
}
