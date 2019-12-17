package com.tozmart.tozisdk.view.editprofileview;

import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.DraweeView;

public class DefaultOnDoubleTapListener implements GestureDetector.OnDoubleTapListener {

    protected Attacher mAttacher;

    public DefaultOnDoubleTapListener(Attacher attacher) {
        setPhotoDraweeViewAttacher(attacher);
    }

    @Override public boolean onSingleTapConfirmed(MotionEvent e) {

        if (mAttacher == null) {
            return false;
        }
        DraweeView<GenericDraweeHierarchy> draweeView = mAttacher.getDraweeView();
        if (draweeView == null) {
            return false;
        }

        if (mAttacher.getOnPhotoTapListener() != null) {
            final RectF displayRect = mAttacher.getDisplayRect();

            if (null != displayRect) {
                final float x = e.getX(), y = e.getY();
                if (displayRect.contains(x, y)) {
                    float xResult = (x - displayRect.left) / displayRect.width();
                    float yResult = (y - displayRect.top) / displayRect.height();
                    mAttacher.getOnPhotoTapListener().onPhotoTap(draweeView, xResult, yResult);
                    return true;
                }
            }
        }

        if (mAttacher.getOnViewTapListener() != null) {
            mAttacher.getOnViewTapListener().onViewTap(draweeView, e.getX(), e.getY());
            return true;
        }

        return false;
    }

    @Override public boolean onDoubleTap(MotionEvent event) {
        if (mAttacher == null) {
            return false;
        }

        try {
            float scale = mAttacher.getScale();
            float x = event.getX();
            float y = event.getY();

            // min, mid, max
            if (scale < Math.round(mAttacher.getMediumScale() * 10) / 10.f) {
                mAttacher.setScale(Math.round(mAttacher.getMediumScale() * 10) / 10.f, x, y, true);
            } else if (scale >= Math.round(mAttacher.getMediumScale() * 10) / 10.f && scale < Math.round(mAttacher.getMaximumScale())) {
                mAttacher.setScale(mAttacher.getMaximumScale(), x, y, true);
            } else {
                mAttacher.setScale(Math.round(mAttacher.getMinimumScale()), x, y, true);
            }
        } catch (Exception e) {
            // Can sometimes happen when getX() and getY() is called
        }
        return true;
    }

    @Override public boolean onDoubleTapEvent(MotionEvent event) {
        return false;
    }

    public void setPhotoDraweeViewAttacher(Attacher attacher) {
        mAttacher = attacher;
    }
}
