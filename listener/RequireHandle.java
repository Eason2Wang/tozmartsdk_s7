package com.tozmart.tozisdk.listener;

/**
 * Created by Chale on 2019/11/12 0012.
 */
public class RequireHandle<T> {
    public static OnRequireRefreshListener refreshListener;

    public RequireHandle(OnRequireRefreshListener<T> refreshListener) {
        this.refreshListener = refreshListener;
    }

    public static OnRequireRefreshListener getOnlidelistener() {
        return refreshListener;
    }

    public void cancel() {
        refreshListener = null;
    }
}
