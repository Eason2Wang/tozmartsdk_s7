package com.tozmart.tozisdk.api;

public interface ProgressListener {

    void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish);
}
