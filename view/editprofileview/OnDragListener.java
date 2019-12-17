package com.tozmart.tozisdk.view.editprofileview;

/**
 * Interface definition for callback to be invoked when attached ImageView drag changes
 *
 * @author wys
 */
public interface OnDragListener {
    /**
     * Callback for when the drag changes
     *
     * @param offsetX
     * @param offsetY
     */
    void onDrag(float offsetX, float offsetY);
}
