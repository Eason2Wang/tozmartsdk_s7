package com.tozmart.tozisdk.view.editprofileview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.tozmart.tozisdk.R;
import com.tozmart.tozisdk.sdk.OneMeasureSDKLite;

import static com.tozmart.tozisdk.view.EditOutlineView.LINE_WIDTH;

public class MoveRectObject implements Cloneable {

    private PointF mStartPosition = new PointF();
    private PointF mEndPosition = new PointF();
    private int positionIndex;// 是outline points的第几个
    private boolean isSelected;
    private int mRectWidth;
    private Drawable mIcon;
    private Rect mDestRect;
    private String lineType;

    private Paint paint = new Paint();

    public MoveRectObject() {
    }

    public MoveRectObject(int width, PointF startPosition, PointF endPosition, String lineType, Drawable rectDrawable) {
        init(width, startPosition, endPosition, lineType, rectDrawable);
    }

    private void init(int width, PointF startPosition, PointF endPosition, String lineType, Drawable rectDrawable) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND); // 让画的线圆滑
        paint.setStrokeCap(Paint.Cap.ROUND);

        if (rectDrawable != null) {
            mIcon = rectDrawable;
        } else {
            mIcon = OneMeasureSDKLite.getInstance().getApplicationContext().getResources().getDrawable(R.drawable.move_rect);
        }

        mRectWidth = width;
        mStartPosition = startPosition;
        mEndPosition = endPosition;
        this.lineType = lineType;

        if (lineType.equals("L")) {
            mStartPosition.x = mStartPosition.x + mRectWidth;
            mDestRect = new Rect((int)mStartPosition.x - mRectWidth, (int)mStartPosition.y - mRectWidth / 2,
                    (int)mStartPosition.x, (int)mStartPosition.y + mRectWidth / 2);
        } else if (lineType.equals("R")) {
            mStartPosition.x = mStartPosition.x - mRectWidth;
            mDestRect = new Rect((int)mStartPosition.x, (int)mStartPosition.y - mRectWidth / 2,
                    (int)mStartPosition.x + mRectWidth, (int)mStartPosition.y + mRectWidth / 2);
        }
        mIcon.setBounds(mDestRect);
    }

    public void drawRectLine(Canvas canvas) {
        if (mIcon != null) {
            if (isSelected) {
                paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(), android.R.color.black));
                paint.setStrokeWidth(LINE_WIDTH * 1.4f);//设置粗细
                paint.setAlpha(150);
                canvas.drawLine(mStartPosition.x,
                        mStartPosition.y,
                        mEndPosition.x,
                        mEndPosition.y,
                        paint);
                paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(), R.color.color_50E3C2));
                paint.setStrokeWidth(LINE_WIDTH);//设置粗细
                paint.setAlpha(150);
                canvas.drawLine(mStartPosition.x,
                        mStartPosition.y,
                        mEndPosition.x,
                        mEndPosition.y,
                        paint);
                paint.setAlpha(255);
                mIcon.draw(canvas);
            } else {
                paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(), android.R.color.black));
                paint.setStrokeWidth(LINE_WIDTH * 1.4f);//设置粗细
                paint.setAlpha(150);
                canvas.drawLine(mStartPosition.x,
                        mStartPosition.y,
                        mEndPosition.x,
                        mEndPosition.y,
                        paint);
                paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(), android.R.color.white));
                paint.setStrokeWidth(LINE_WIDTH);//设置粗细
                paint.setAlpha(150);
                canvas.drawLine(mStartPosition.x,
                        mStartPosition.y,
                        mEndPosition.x,
                        mEndPosition.y,
                        paint);
                mIcon.draw(canvas);
            }
        }
    }

    /**
     * 判断点是否在多边形内
     *
     * @param x
     * @param y
     * @return
     */
    public boolean contains(float x, float y) {
        if (x < mDestRect.right && x > mDestRect.left
                && y > mDestRect.top && y < mDestRect.bottom) {
            return true;
        }
        return false;
    }

    public PointF getmStartPosition() {
        return mStartPosition;
    }

    public void setStartPosition(PointF startPosition) {
        mStartPosition = startPosition;
        if (lineType.equals("L")) {
            mStartPosition.x = mStartPosition.x + mRectWidth;
            mDestRect = new Rect((int)mStartPosition.x - mRectWidth, (int)mStartPosition.y - mRectWidth / 2,
                    (int)mStartPosition.x, (int)mStartPosition.y + mRectWidth / 2);
        } else if (lineType.equals("R")) {
            mStartPosition.x = mStartPosition.x - mRectWidth;
            mDestRect = new Rect((int)mStartPosition.x, (int)mStartPosition.y - mRectWidth / 2,
                    (int)mStartPosition.x + mRectWidth, (int)mStartPosition.y + mRectWidth / 2);
        }
        mIcon.setBounds(mDestRect);
    }

    public PointF getmEndPosition() {
        return mEndPosition;
    }

    public void setEndPosition(PointF mEndPosition) {
        this.mEndPosition = mEndPosition;
    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getmRectWidth() {
        return mRectWidth;
    }

    public void setRectWidth(int mRectWidth) {
        this.mRectWidth = mRectWidth;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    @Override
    public MoveRectObject clone() {
        MoveRectObject clone = null;
        try{
            clone = (MoveRectObject) super.clone();

        }catch(CloneNotSupportedException e){
            throw new RuntimeException(e); // won't happen
        }

        return clone;
    }
}
