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

public class MovePanObject implements Parcelable, Cloneable {

    private int panColor;

    private PointF mPosition = new PointF();
    private int positionIndex;// 是outline points的第几个
    private boolean isSelected;
    private float mRadius;
    private boolean isSticked;
    private boolean isSpecial;
    private boolean isVisible;

    private Paint paint = new Paint();

    public MovePanObject() {
    }

    public MovePanObject(float radius, PointF position, int panColor) {
        init(radius, position, panColor);
    }

    private void init(float radius, PointF position, int panColor) {
        this.panColor = panColor;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND); // 让画的线圆滑
        paint.setStrokeCap(Paint.Cap.ROUND);

        mRadius = radius;
        mPosition = position;
    }

    public void drawPan(Canvas canvas) {
        if (!isVisible)
            return;
        if (isSelected) {
            paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(), android.R.color.white));
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(150);
            canvas.drawCircle(mPosition.x,
                    mPosition.y,
                    mRadius,
                    paint);
            paint.setColor(panColor);
            paint.setAlpha(255);
            canvas.drawCircle(mPosition.x,
                    mPosition.y,
                    LINE_WIDTH,
                    paint);
            paint.setColor(panColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(LINE_WIDTH);
            canvas.drawCircle(mPosition.x,
                    mPosition.y,
                    mRadius,
                    paint);
        } else if (isSticked) {
            paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(), android.R.color.white));
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(150);
            canvas.drawCircle(mPosition.x,
                    mPosition.y,
                    mRadius / 4,
                    paint);
            paint.setColor(panColor);
            paint.setAlpha(255);
            canvas.drawCircle(mPosition.x,
                    mPosition.y,
                    LINE_WIDTH,
                    paint);
            paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(), R.color.text_disable));
            paint.setAlpha(150);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(LINE_WIDTH);
            canvas.drawCircle(mPosition.x,
                    mPosition.y,
                    mRadius / 4,
                    paint);
        }else if (isSpecial) {
            paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(), android.R.color.white));
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(150);
            canvas.drawCircle(mPosition.x,
                    mPosition.y,
                    mRadius,
                    paint);
            paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(), R.color.google_blue));
            paint.setAlpha(150);
            canvas.drawCircle(mPosition.x,
                    mPosition.y,
                    LINE_WIDTH,
                    paint);
            paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(), R.color.google_blue));
            paint.setAlpha(150);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(LINE_WIDTH);
            canvas.drawCircle(mPosition.x,
                    mPosition.y,
                    mRadius,
                    paint);
        } else {
            paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(), android.R.color.white));
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(150);
            canvas.drawCircle(mPosition.x,
                    mPosition.y,
                    mRadius / 2,
                    paint);
            paint.setColor(panColor);
            paint.setAlpha(255);
            canvas.drawCircle(mPosition.x,
                    mPosition.y,
                    LINE_WIDTH,
                    paint);
            paint.setColor(panColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(LINE_WIDTH);
            canvas.drawCircle(mPosition.x,
                    mPosition.y,
                    mRadius / 2,
                    paint);
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
        if (x < mPosition.x + mRadius && x > mPosition.x - mRadius
                && y < mPosition.y + mRadius && y > mPosition.y - mRadius) {
            return true;
        }
        return false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public PointF getPosition() {
        return mPosition;
    }

    public void setPosition(PointF mPosition) {
        this.mPosition = mPosition;
    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        this.mRadius = radius;
    }

    public boolean isSticked() {
        return isSticked;
    }

    public void setSticked(boolean sticked) {
        isSticked = sticked;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    @Override
    public MovePanObject clone() {
        MovePanObject clone = null;
        try{
            clone = (MovePanObject) super.clone();

        }catch(CloneNotSupportedException e){
            throw new RuntimeException(e); // won't happen
        }

        return clone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mPosition, flags);
        dest.writeInt(this.positionIndex);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.mRadius);
        dest.writeByte(this.isSticked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSpecial ? (byte) 1 : (byte) 0);
    }

    protected MovePanObject(Parcel in) {
        this.mPosition = in.readParcelable(PointF.class.getClassLoader());
        this.positionIndex = in.readInt();
        this.isSelected = in.readByte() != 0;
        this.mRadius = in.readFloat();
        this.isSticked = in.readByte() != 0;
        this.isSpecial = in.readByte() != 0;
    }

    public static final Creator<MovePanObject> CREATOR = new Creator<MovePanObject>() {
        @Override
        public MovePanObject createFromParcel(Parcel source) {
            return new MovePanObject(source);
        }

        @Override
        public MovePanObject[] newArray(int size) {
            return new MovePanObject[size];
        }
    };
}
