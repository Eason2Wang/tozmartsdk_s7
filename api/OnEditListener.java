package com.tozmart.tozisdk.api;

import com.tozmart.tozisdk.view.editprofileview.CurrentMode;

public interface OnEditListener {

    void onEdit(CurrentMode currentMode, boolean undoBtnVisible);

}
