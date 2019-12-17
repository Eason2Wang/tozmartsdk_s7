package com.tozmart.tozisdk.view.editprofileview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.ScrollerCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.DraweeView;
import com.tozmart.tozisdk.utils.Rx2Timer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class Attacher implements IAttacher, View.OnTouchListener, OnScaleDragGestureListener {

    private static final int EDGE_NONE = -1;
    private static final int EDGE_LEFT = 0, EDGE_TOP = 0;
    private static final int EDGE_RIGHT = 1, EDGE_BOTTOM = 1;
    private static final int EDGE_BOTH = 2;

    @IntDef({ HORIZONTAL, VERTICAL }) @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private int mOrientation = HORIZONTAL;

    private final float[] mMatrixValues = new float[9];
    private final RectF mDisplayRect = new RectF();
    private final Interpolator mZoomInterpolator = new AccelerateDecelerateInterpolator();

    private float mMinScale = DEFAULT_MIN_SCALE;
    private float mMidScale = DEFAULT_MID_SCALE;
    private float mMaxScale = DEFAULT_MAX_SCALE;
    private long mZoomDuration = ZOOM_DURATION;

    private ScaleDragDetector mScaleDragDetector;
    private GestureDetectorCompat mGestureDetector;

    private boolean mBlockParentIntercept = false;
    private boolean mAllowParentInterceptOnEdge = true;
    private int mScrollEdgeX = EDGE_BOTH;
    private int mScrollEdgeY = EDGE_BOTH;

    private final Matrix mMatrix = new Matrix();
    private int mImageInfoHeight = -1, mImageInfoWidth = -1;
//    private FlingRunnable mCurrentFlingRunnable;
    private WeakReference<DraweeView<GenericDraweeHierarchy>> mDraweeView;

    private OnPhotoTapListener mPhotoTapListener;
    private OnViewTapListener mViewTapListener;
    private View.OnLongClickListener mLongClickListener;
    private OnScaleChangeListener mScaleChangeListener;
    private OnScaleEndListener onScaleEndListener;
    private OnDragListener mDragListener;
    private OnMoveListener mMoveListener;
    private OnTouchUpListener onTouchUpListener;
    private OnTouchDownListener onTouchDownListener;

    private boolean flingAnimate = true;
    private boolean disnableScaleDrag = false;

    public Attacher(DraweeView<GenericDraweeHierarchy> draweeView) {
        mDraweeView = new WeakReference<>(draweeView);
        draweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        draweeView.setOnTouchListener(this);
        mScaleDragDetector = new ScaleDragDetector(draweeView.getContext(), this);
        mGestureDetector = new GestureDetectorCompat(draweeView.getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override public void onLongPress(MotionEvent e) {
                        super.onLongPress(e);
//                        if (null != mLongClickListener) {
//                            mLongClickListener.onLongClick(getDraweeView());
//                        }
                    }
                });
//        mGestureDetector.setOnDoubleTapListener(new DefaultOnDoubleTapListener(this));
    }

//    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener newOnDoubleTapListener) {
//        if (newOnDoubleTapListener != null) {
//            this.mGestureDetector.setOnDoubleTapListener(newOnDoubleTapListener);
//        } else {
//            this.mGestureDetector.setOnDoubleTapListener(new DefaultOnDoubleTapListener(this));
//        }
//    }

    @Nullable
    public DraweeView<GenericDraweeHierarchy> getDraweeView() {
        return mDraweeView.get();
    }

    @Override public float getMinimumScale() {
        return mMinScale;
    }

    @Override public float getMediumScale() {
        return mMidScale;
    }

    @Override public float getMaximumScale() {
        return mMaxScale;
    }

    @Override public void setMaximumScale(float maximumScale) {
        checkZoomLevels(mMinScale, mMidScale, maximumScale);
        mMaxScale = maximumScale;
    }

    @Override public void setMediumScale(float mediumScale) {
        checkZoomLevels(mMinScale, mediumScale, mMaxScale);
        mMidScale = mediumScale;
    }

    @Override public void setMinimumScale(float minimumScale) {
        checkZoomLevels(minimumScale, mMidScale, mMaxScale);
        mMinScale = minimumScale;
    }

    @Override public float getScale() {
        return (float) Math.sqrt(
                (float) Math.pow(getMatrixValue(mMatrix, Matrix.MSCALE_X), 2) + (float) Math.pow(
                        getMatrixValue(mMatrix, Matrix.MSKEW_Y), 2));
    }

    @Override public void setScale(float scale) {
        setScale(scale, false);
    }

    @Override public void setScale(float scale, boolean animate) {
        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();
        if (draweeView != null) {
            setScale(scale, (draweeView.getRight()) / 2, (draweeView.getBottom()) / 2, animate);
        }
    }

    @Override public void setScale(float scale, float focalX, float focalY, boolean animate) {
        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();

        if (animate) {
            if (draweeView == null || scale < mMinScale || scale > mMaxScale) {
                return;
            }
            draweeView.post(new AnimatedZoomRunnable(getScale(), scale, focalX, focalY));
        } else {
            onScale(scale, focalX, focalY);
        }
    }

    public void setTranslate(float transX, float transY, boolean animate) {
        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();

        if (draweeView == null) {
            return;
        }
        if (animate) {
            draweeView.post(new AnimatedTranslateRunnable(transX, transY));
        } else {
//            mMatrix.postTranslate(transX, transY);
//            checkMatrixAndInvalidate();
            onDrag(transX, transY);
        }
    }

    @Override public void setFlingAnimation(boolean animate){
        flingAnimate = animate;
    }

    @Override public void setOrientation(@OrientationMode int orientation) {
        mOrientation = orientation;
    }

    @Override public void setZoomTransitionDuration(long duration) {
        duration = duration < 0 ? ZOOM_DURATION : duration;
        mZoomDuration = duration;
    }

    @Override public void setAllowParentInterceptOnEdge(boolean allow) {
        mAllowParentInterceptOnEdge = allow;
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener newOnDoubleTapListener) {
        if (newOnDoubleTapListener != null) {
            this.mGestureDetector.setOnDoubleTapListener(newOnDoubleTapListener);
        } else {
            this.mGestureDetector.setOnDoubleTapListener(new DefaultOnDoubleTapListener(this));
        }
    }

    @Override public void setOnScaleChangeListener(OnScaleChangeListener listener) {
        mScaleChangeListener = listener;
    }

    @Override public void setOnScaleEndListener(OnScaleEndListener listener) {
        onScaleEndListener = listener;
    }

    @Override public void setOnDragListener(OnDragListener listener) {
        mDragListener = listener;
    }

    @Override public void setOnMoveListener(OnMoveListener listener) {
        mMoveListener = listener;
    }

    @Override public void setOnTouchUpListener(OnTouchUpListener listener) {
        onTouchUpListener = listener;
    }

    @Override public void setOnTouchDownListener(OnTouchDownListener listener) {
        onTouchDownListener = listener;
    }

    @Override public void setOnLongClickListener(View.OnLongClickListener listener) {
        mLongClickListener = listener;
    }

    @Override public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        mPhotoTapListener = listener;
    }

    @Override public void setOnViewTapListener(OnViewTapListener listener) {
        mViewTapListener = listener;
    }

    @Override public OnPhotoTapListener getOnPhotoTapListener() {
        return mPhotoTapListener;
    }

    @Override public OnViewTapListener getOnViewTapListener() {
        return mViewTapListener;
    }

    @Override public void update(int imageInfoWidth, int imageInfoHeight) {
        mImageInfoWidth = imageInfoWidth;
        mImageInfoHeight = imageInfoHeight;
        updateBaseMatrix();
    }

    private static void checkZoomLevels(float minZoom, float midZoom, float maxZoom) {
        if (minZoom >= midZoom) {
            throw new IllegalArgumentException("MinZoom has to be less than MidZoom");
        } else if (midZoom >= maxZoom) {
            throw new IllegalArgumentException("MidZoom has to be less than MaxZoom");
        }
    }

    private int getViewWidth() {

        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();

        if (draweeView != null) {

            return draweeView.getWidth()
                    - draweeView.getPaddingLeft()
                    - draweeView.getPaddingRight();
        }

        return 0;
    }

    private int getViewHeight() {
        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();
        if (draweeView != null) {
            return draweeView.getHeight()
                    - draweeView.getPaddingTop()
                    - draweeView.getPaddingBottom();
        }
        return 0;
    }

    private float getMatrixValue(Matrix matrix, int whichValue) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }

    public Matrix getDrawMatrix() {
        return mMatrix;
    }

    public RectF getDisplayRect() {
        checkMatrixBounds();
        return getDisplayRect(getDrawMatrix());
    }

    public boolean checkMatrixAndInvalidate() {

        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();

        if (draweeView == null) {
            return false;
        }

        if (checkMatrixBounds()) {
            draweeView.invalidate();
            return true;
        }
        return false;
    }

    public float[] checkDragInvalide() {

        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();

        if (draweeView == null) {
            return null;
        }

        float[] delta = checkDragable();
        if (delta != null) {
            draweeView.invalidate();
            return delta;
        }
        return null;
    }

    public boolean checkMatrixBounds() {
        RectF rect = getDisplayRect(getDrawMatrix());
        if (rect == null) {
            return false;
        }

        float height = rect.height();
        float width = rect.width();
        float deltaX = 0.0F;
        float deltaY = 0.0F;
        int viewHeight = getViewHeight();

        if (height <= (float) viewHeight) {
            deltaY = (viewHeight - height) / 2 - rect.top;
            mScrollEdgeY = EDGE_BOTH;
        } else if (rect.top > 0.0F) {
            deltaY = -rect.top;
            mScrollEdgeY = EDGE_TOP;
        } else if (rect.bottom < (float) viewHeight) {
            deltaY = viewHeight - rect.bottom;
            mScrollEdgeY = EDGE_BOTTOM;
        } else {
            mScrollEdgeY = EDGE_NONE;
        }
        int viewWidth = getViewWidth();
        if (width <= viewWidth) {
            deltaX = (viewWidth - width) / 2 - rect.left;
            mScrollEdgeX = EDGE_BOTH;
        } else if (rect.left > 0) {
            deltaX = -rect.left;
            mScrollEdgeX = EDGE_LEFT;
        } else if (rect.right < viewWidth) {
            deltaX = viewWidth - rect.right;
            mScrollEdgeX = EDGE_RIGHT;
        } else {
            mScrollEdgeX = EDGE_NONE;
        }

        mMatrix.postTranslate(deltaX, deltaY);
        return true;
    }

    public float[] checkDragable() {
        RectF rect = getDisplayRect(getDrawMatrix());
        if (rect == null) {
            return null;
        }

        float height = rect.height();
        float width = rect.width();
        float deltaX = 0.0F;
        float deltaY = 0.0F;
        int viewHeight = getViewHeight();

        if (height <= (float) viewHeight) {
            deltaY = (viewHeight - height) / 2 - rect.top;
            mScrollEdgeY = EDGE_BOTH;
        } else if (rect.top > 0.0F) {
            deltaY = -rect.top;
            mScrollEdgeY = EDGE_TOP;
        } else if (rect.bottom < (float) viewHeight) {
            deltaY = viewHeight - rect.bottom;
            mScrollEdgeY = EDGE_BOTTOM;
        } else {
            mScrollEdgeY = EDGE_NONE;
        }
        int viewWidth = getViewWidth();
        if (width <= viewWidth) {
            deltaX = (viewWidth - width) / 2 - rect.left;
            mScrollEdgeX = EDGE_BOTH;
        } else if (rect.left > 0) {
            deltaX = -rect.left;
            mScrollEdgeX = EDGE_LEFT;
        } else if (rect.right < viewWidth) {
            deltaX = viewWidth - rect.right;
            mScrollEdgeX = EDGE_RIGHT;
        } else {
            mScrollEdgeX = EDGE_NONE;
        }

        mMatrix.postTranslate(deltaX, deltaY);
        return new float[]{deltaX, deltaY};
    }

    private RectF getDisplayRect(Matrix matrix) {
        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();
        if (draweeView == null || (mImageInfoWidth == -1 && mImageInfoHeight == -1)) {
            return null;
        }
        mDisplayRect.set(0.0F, 0.0F, mImageInfoWidth, mImageInfoHeight);
        draweeView.getHierarchy().getActualImageBounds(mDisplayRect);
        matrix.mapRect(mDisplayRect);
        return mDisplayRect;
    }

    private void updateBaseMatrix() {
        if (mImageInfoWidth == -1 && mImageInfoHeight == -1) {
            return;
        }
        resetMatrix();
    }

    private void resetMatrix() {
        mMatrix.reset();
        checkMatrixBounds();
        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();
        if (draweeView != null) {
            draweeView.invalidate();
        }
    }

    private void checkMinScale() {
        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();
        if (draweeView == null) {
            return;
        }

        if (getScale() < mMinScale) {
            RectF rect = getDisplayRect();
            if (null != rect) {
                draweeView.post(new AnimatedZoomRunnable(getScale(), mMinScale, rect.centerX(),
                        rect.centerY()));
            }
        }
    }

    @Override public void onScale(float scaleFactor, float focusX, float focusY) {
        if (getScale() < mMaxScale || scaleFactor < 1.0F) {

            if (mScaleChangeListener != null) {
                mScaleChangeListener.onScaleChange(scaleFactor, focusX, focusY);
            }

            mMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
            checkMatrixAndInvalidate();
        }
    }

    @Override public void onScaleEnd() {
        checkMinScale();
    }

    @Override public void onDrag(float dx, float dy) {

        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();

        if (draweeView != null && !mScaleDragDetector.isScaling()) {
            mMatrix.postTranslate(dx, dy);
            float[] trans = checkDragInvalide();
            if (trans != null){
                if (mDragListener != null && getScale() > 1.f) {// 只有在图片被放大后才会触发onDrag事件
                    mDragListener.onDrag(dx + trans[0], dy + trans[1]);
                }
            }

            ViewParent parent = draweeView.getParent();
            if (parent == null) {
                return;
            }
            if (mAllowParentInterceptOnEdge
                    && !mScaleDragDetector.isScaling()
                    && !mBlockParentIntercept) {
                if (mOrientation == HORIZONTAL && (mScrollEdgeX == EDGE_BOTH || (mScrollEdgeX
                        == EDGE_LEFT && dx >= 1f) || (mScrollEdgeX == EDGE_RIGHT && dx <= -1f))) {
                    parent.requestDisallowInterceptTouchEvent(false);
                } else if (mOrientation == VERTICAL && (mScrollEdgeY == EDGE_BOTH || (mScrollEdgeY
                        == EDGE_TOP && dy >= 1f) || (mScrollEdgeY == EDGE_BOTTOM && dy <= -1f))) {
                    parent.requestDisallowInterceptTouchEvent(false);
                }
            } else {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
    }

//    @Override public void onFling(float startX, float startY, float velocityX, float velocityY) {
//        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();
//        if (draweeView == null) {
//            return;
//        }
//
//        if (flingAnimate) {
//            mCurrentFlingRunnable = new FlingRunnable(draweeView.getContext());
//            mCurrentFlingRunnable.fling(getViewWidth(), getViewHeight(), (int) velocityX,
//                    (int) velocityY);
//            draweeView.post(mCurrentFlingRunnable);
//        }
//    }

    public void disableScaleDrag(boolean disnableScaleDrag){
        this.disnableScaleDrag = disnableScaleDrag;
    }

    private float lastX, lastY, thisX, thisY;
    @Override public boolean onTouch(View v, MotionEvent event) {
        thisX = (int) event.getX();
        thisY = (int) event.getY();
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                ViewParent parent = v.getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
//                cancelFling();
                if (onTouchDownListener != null) {
                    onTouchDownListener.onTouchDown(event.getX(), event.getY());
                }

                //保存按下时的坐标和时间
                lastX = thisX;
                lastY = thisY;
                startCountDown();
            }
                break;
            case MotionEvent.ACTION_UP:
                if (onTouchUpListener != null) {
                    onTouchUpListener.onTouchUp(event.getX(), event.getY());
                }
                ViewParent parent = v.getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);
                }
                stopCountDown();
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                parent = v.getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);
                }
                stopCountDown();
            }
            break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                stopCountDown();
            }
            break;
            case MotionEvent.ACTION_MOVE:
                float offsetX = Math.abs(thisX - lastX);
                float offsetY = Math.abs(thisY - lastY);
                if (offsetX > 20 || offsetY > 20) {
                    stopCountDown();
                }
                if (mMoveListener != null) {
                    mMoveListener.onMove(event.getX(), event.getY());
                }
                break;
        }

        boolean wasScaling = mScaleDragDetector.isScaling();
        boolean wasDragging = mScaleDragDetector.isDragging();

        boolean handled = true;
        if (!disnableScaleDrag) {
            handled = mScaleDragDetector.onTouchEvent(event);
        }

        boolean noScale = !wasScaling && !mScaleDragDetector.isScaling();
        boolean noDrag = !wasDragging && !mScaleDragDetector.isDragging();
        mBlockParentIntercept = noScale && noDrag;

        if (mGestureDetector.onTouchEvent(event)) {
            handled = true;
        }

        return handled;
    }

    //订阅倒计时事件
    Rx2Timer timerCountDown;
    private void startCountDown() {
        timerCountDown = Rx2Timer.builder()
                .initialDelay(0)
                .period(1)
                .take(200)
                .unit(TimeUnit.MILLISECONDS)
                .onEmit(new Consumer<Long>() {
                    @Override
                    public void accept(Long count) throws Exception {
                        long i = count;
                    }
                })
                .onError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        timerCountDown.stop();
                    }
                })
                .onComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (null != mLongClickListener) {
                            mLongClickListener.onLongClick(getDraweeView());
                        }
                        timerCountDown.stop();
                    }
                })
                .build();
        timerCountDown.start();
    }

    private void stopCountDown() {
        if (timerCountDown != null) {
            timerCountDown.stop();
            timerCountDown = null;
        }
    }

    private class AnimatedZoomRunnable implements Runnable {
        private final float mFocalX, mFocalY;
        private final long mStartTime;
        private final float mZoomStart, mZoomEnd;

        public AnimatedZoomRunnable(final float currentZoom, final float targetZoom,
                final float focalX, final float focalY) {
            mFocalX = focalX;
            mFocalY = focalY;
            mStartTime = System.currentTimeMillis();
            mZoomStart = currentZoom;
            mZoomEnd = targetZoom;
        }

        @Override public void run() {

            DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();
            if (draweeView == null) {
                return;
            }

            float t = interpolate();
            float scale = mZoomStart + t * (mZoomEnd - mZoomStart);
            float deltaScale = scale / getScale();

            onScale(deltaScale, mFocalX, mFocalY);

            if (t < 1f) {
                postOnAnimation(draweeView, this);
            } else {
                if (onScaleEndListener != null) {
                    onScaleEndListener.onScaleEnd();
                }
            }
        }

        private float interpolate() {
            float t = 1f * (System.currentTimeMillis() - mStartTime) / mZoomDuration;
            t = Math.min(1f, t);
            t = mZoomInterpolator.getInterpolation(t);
            return t;
        }
    }

    private class AnimatedTranslateRunnable implements Runnable {
        private final float mTransX, mTransY;
        private final long mStartTime;

        public AnimatedTranslateRunnable(final float transX, final float transY) {
            mTransX = transX;
            mTransY = transY;
            mStartTime = System.currentTimeMillis();
        }

        @Override public void run() {

            DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();
            if (draweeView == null) {
                return;
            }

            float t = interpolate();

            onDrag(mTransX, mTransY);

            if (t < 1f) {
                postOnAnimation(draweeView, this);
            }
        }

        private float interpolate() {
            float t = 1f * (System.currentTimeMillis() - mStartTime) / mZoomDuration;
            t = Math.min(1f, t);
            t = mZoomInterpolator.getInterpolation(t);
            return t;
        }
    }

    private class FlingRunnable implements Runnable {

        private final ScrollerCompat mScroller;
        private int mCurrentX, mCurrentY;

        public FlingRunnable(Context context) {
            mScroller = ScrollerCompat.create(context);
        }

        public void cancelFling() {
            mScroller.abortAnimation();
        }

        public void fling(int viewWidth, int viewHeight, int velocityX, int velocityY) {
            final RectF rect = getDisplayRect();
            if (null == rect) {
                return;
            }

            final int startX = Math.round(-rect.left);
            final int minX, maxX, minY, maxY;

            if (viewWidth < rect.width()) {
                minX = 0;
                maxX = Math.round(rect.width() - viewWidth);
            } else {
                minX = maxX = startX;
            }

            final int startY = Math.round(-rect.top);
            if (viewHeight < rect.height()) {
                minY = 0;
                maxY = Math.round(rect.height() - viewHeight);
            } else {
                minY = maxY = startY;
            }

            mCurrentX = startX;
            mCurrentY = startY;

            if (startX != maxX || startY != maxY) {
                mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0);
            }
        }

        @Override public void run() {
            if (mScroller.isFinished()) {
                return;
            }

            DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();

            if (draweeView != null && mScroller.computeScrollOffset()) {
                final int newX = mScroller.getCurrX();
                final int newY = mScroller.getCurrY();
                mMatrix.postTranslate(mCurrentX - newX, mCurrentY - newY);
                draweeView.invalidate();
                mCurrentX = newX;
                mCurrentY = newY;
                postOnAnimation(draweeView, this);
            }
        }
    }

//    private void cancelFling() {
//        if (mCurrentFlingRunnable != null) {
//            mCurrentFlingRunnable.cancelFling();
//            mCurrentFlingRunnable = null;
//        }
//    }

    private void postOnAnimation(View view, Runnable runnable) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.postOnAnimation(runnable);
        } else {
            view.postDelayed(runnable, 16L);
        }
    }

    protected void onDetachedFromWindow() {
//        cancelFling();
    }
}
