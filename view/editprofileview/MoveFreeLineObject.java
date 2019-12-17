package com.tozmart.tozisdk.view.editprofileview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;

import com.tozmart.tozisdk.R;
import com.tozmart.tozisdk.sdk.OneMeasureSDKLite;

import static com.tozmart.tozisdk.view.EditOutlineView.LINE_WIDTH;

public class MoveFreeLineObject implements Parcelable, Cloneable {

    private int panColor;

    private PointF mStartPosition = new PointF();
    private PointF mEndPosition = new PointF();
    private boolean isStartSelected;
    private boolean isEndSelected;
    private float radius;
    private int paintLineId;

    private Paint paint = new Paint();

    public MoveFreeLineObject() {
    }

    public MoveFreeLineObject(float radius, PointF startPosition, PointF endPosition, int panColor) {
        init(radius, startPosition, endPosition, panColor);
    }

    private void init(float radius, PointF startPosition, PointF endPosition, int panColor) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND); // 让画的线圆滑
        paint.setStrokeCap(Paint.Cap.ROUND);

        mStartPosition = startPosition;
        mEndPosition = endPosition;
        this.radius = radius;
        this.panColor = panColor;
    }

    public void drawLine(Canvas canvas) {
        if (isStartSelected) {
            paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(), R.color.outline_color));
            paint.setStrokeWidth(LINE_WIDTH);//设置粗细
            paint.setAlpha(255);
            canvas.drawLine(mStartPosition.x,
                    mStartPosition.y,
                    mEndPosition.x,
                    mEndPosition.y,
                    paint);
            paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(), android.R.color.white));
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(150);
            canvas.drawCircle(mStartPosition.x,
                    mStartPosition.y,
                    radius,
                    paint);
            canvas.drawCircle(mEndPosition.x,
                    mEndPosition.y,
                    radius / 2,
                    paint);
            paint.setColor(panColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(LINE_WIDTH);
            paint.setAlpha(255);
            canvas.drawCircle(mStartPosition.x,
                    mStartPosition.y,
                    radius,
                    paint);
            canvas.drawCircle(mEndPosition.x,
                    mEndPosition.y,
                    radius / 2,
                    paint);
        } else if (isEndSelected) {
            paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(), R.color.outline_color));
            paint.setStrokeWidth(LINE_WIDTH);//设置粗细
            paint.setAlpha(255);
            canvas.drawLine(mStartPosition.x,
                    mStartPosition.y,
                    mEndPosition.x,
                    mEndPosition.y,
                    paint);
            paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(), android.R.color.white));
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(150);
            canvas.drawCircle(mStartPosition.x,
                    mStartPosition.y,
                    radius / 2,
                    paint);
            canvas.drawCircle(mEndPosition.x,
                    mEndPosition.y,
                    radius,
                    paint);
            paint.setColor(panColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(LINE_WIDTH);
            paint.setAlpha(255);
            canvas.drawCircle(mStartPosition.x,
                    mStartPosition.y,
                    radius / 2,
                    paint);
            canvas.drawCircle(mEndPosition.x,
                    mEndPosition.y,
                    radius,
                    paint);
        } else {
            paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(), R.color.outline_color));
            paint.setStrokeWidth(LINE_WIDTH);//设置粗细
            paint.setAlpha(255);
            canvas.drawLine(mStartPosition.x,
                    mStartPosition.y,
                    mEndPosition.x,
                    mEndPosition.y,
                    paint);
            paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(), android.R.color.white));
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(150);
            canvas.drawCircle(mStartPosition.x,
                    mStartPosition.y,
                    radius / 2,
                    paint);
            canvas.drawCircle(mEndPosition.x,
                    mEndPosition.y,
                    radius / 2,
                    paint);
            paint.setColor(panColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(LINE_WIDTH);
            paint.setAlpha(255);
            canvas.drawCircle(mStartPosition.x,
                    mStartPosition.y,
                    radius / 2,
                    paint);
            canvas.drawCircle(mEndPosition.x,
                    mEndPosition.y,
                    radius / 2,
                    paint);
        }
        paint.setColor(panColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(255);
        canvas.drawCircle(mStartPosition.x,
                mStartPosition.y,
                LINE_WIDTH,
                paint);
        canvas.drawCircle(mEndPosition.x,
                mEndPosition.y,
                LINE_WIDTH,
                paint);
    }

    /**
     * 判断点是否在多边形内
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isTouchStart(float x, float y) {
        if (x < mStartPosition.x + radius && x > mStartPosition.x - radius
                && y < mStartPosition.y + radius && y > mStartPosition.y - radius) {
            return true;
        }
        return false;
    }

    /**
     * 判断点是否在多边形内
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isTouchEnd(float x, float y) {
        if (x < mEndPosition.x + radius && x > mEndPosition.x - radius
                && y < mEndPosition.y + radius && y > mEndPosition.y - radius) {
            return true;
        }
        return false;
    }

    public PointF getStartPosition() {
        return mStartPosition;
    }

    public void setStartPosition(PointF startPosition) {
        mStartPosition = startPosition;
    }

    public PointF getEndPosition() {
        return mEndPosition;
    }

    public void setEndPosition(PointF mEndPosition) {
        this.mEndPosition = mEndPosition;
    }

    public boolean isStartSelected() {
        return isStartSelected;
    }

    public void setStartSelected(boolean startSelected) {
        isStartSelected = startSelected;
    }

    public boolean isEndSelected() {
        return isEndSelected;
    }

    public void setEndSelected(boolean endSelected) {
        isEndSelected = endSelected;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getPaintLineId() {
        return paintLineId;
    }

    public void setPaintLineId(int paintLineId) {
        this.paintLineId = paintLineId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public MoveFreeLineObject clone() {
        MoveFreeLineObject clone = null;
        try{
            clone = (MoveFreeLineObject) super.clone();

        }catch(CloneNotSupportedException e){
            throw new RuntimeException(e); // won't happen
        }

        return clone;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mStartPosition, flags);
        dest.writeParcelable(this.mEndPosition, flags);
        dest.writeByte(this.isStartSelected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isEndSelected ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.radius);
        dest.writeInt(this.paintLineId);
    }

    protected MoveFreeLineObject(Parcel in) {
        this.mStartPosition = in.readParcelable(PointF.class.getClassLoader());
        this.mEndPosition = in.readParcelable(PointF.class.getClassLoader());
        this.isStartSelected = in.readByte() != 0;
        this.isEndSelected = in.readByte() != 0;
        this.radius = in.readFloat();
        this.paintLineId = in.readInt();
    }

    public static final Creator<MoveFreeLineObject> CREATOR = new Creator<MoveFreeLineObject>() {
        @Override
        public MoveFreeLineObject createFromParcel(Parcel source) {
            return new MoveFreeLineObject(source);
        }

        @Override
        public MoveFreeLineObject[] newArray(int size) {
            return new MoveFreeLineObject[size];
        }
    };
}
