package com.tozmart.tozisdk.api;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.tozmart.tozisdk.view.CameraView;

public interface ICameraView {

    void captureImage(final CameraView.ImageCallback callback);

    void setFacing(int facing);

    int getFacing();

    void toggleFacing();

    void openGalleryFromActivity(Activity activity, int requestCode);

    void openGalleryFromFragment(Fragment fragment, int requestCode);

    boolean lackRequiredSensors();

    void setOnSensorListener(CameraView.OnSensorListener listener);

}
