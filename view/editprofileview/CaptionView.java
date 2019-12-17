package com.tozmart.tozisdk.view.editprofileview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Created by Chale on 2019/9/3 0003.
 * 描述：针对放大镜中节点的文字说明
 */
public class CaptionView extends android.support.v7.widget.AppCompatTextView {
    private Paint textPaint;
    private int movePanColor;
    private String pxName;
    private PointF selectPoint;
    private float offsetX, offsetY;
    // 放大镜的半径
    private static final int RADIUS = 260;

    public CaptionView(Context context) {
        this(context, null);
    }

    public CaptionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private String pxName1;
    private String pxName2;

    public CaptionView(Context context, int movePanColor, String pxName) {
        super(context);
        this.movePanColor = movePanColor;
        if (pxName.length() > 16) {
            this.pxName1 = pxName.substring(0, 16);
            this.pxName2 = pxName.substring(16);
        } else {
            this.pxName1 = pxName;
            this.pxName2 = "";
        }
        initPaint();
    }

    public void setAttributes(PointF selectPoint, float offsetX, float offsetY) {
        this.selectPoint = selectPoint;
        invalidate();
    }

    private void initPaint() {
        textPaint = new Paint();
        textPaint.setColor(movePanColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(6);
        textPaint.setTextSize(50);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureDimension(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int result = RADIUS * 2;
        if (mode == MeasureSpec.AT_MOST) {
            result = Math.min(RADIUS * 2, size);
        } else if (mode == MeasureSpec.EXACTLY) {
            result = size;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        if (TextUtils.isEmpty(pxName)) {
            pxName = "";
        }
        canvas.drawText(pxName1, selectPoint.x - 0.5f * RADIUS + offsetX, selectPoint.y - 0.4f * RADIUS + offsetY, textPaint);
        canvas.drawText(pxName2, selectPoint.x - 0.5f * RADIUS + offsetX, selectPoint.y - 0.2f * RADIUS + offsetY, textPaint);
    }
}
