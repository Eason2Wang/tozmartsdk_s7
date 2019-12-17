package com.tozmart.tozisdk.view.editprofileview;

/**
 * Interface definition for callback to be invoked when attached ImageView drag changes
 *
 * @author wys
 */
public interface OnMoveListener {
    /**
     * Callback for when the touch up
     *
     * @param eventX
     * @param eventY
     */
    void onMove(float eventX, float eventY);
}
