//package com.tozmart.tozisdk.view.editprofileview;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.graphics.PointF;
//import android.graphics.Rect;
//import android.graphics.RectF;
//import android.support.annotation.Nullable;
//import android.support.v4.content.ContextCompat;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.View;
//
//import com.tozmart.tozisdk.R;
//import com.tozmart.tozisdk.entity.PaintColor;
//import com.tozmart.tozisdk.entity.Pixel;
//import com.tozmart.tozisdk.entity.ProfilePaintLine;
//import com.tozmart.tozisdk.sdk.OneMeasureSDKLite;
//import com.tozmart.tozisdk.utils.ImageUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.tozmart.tozisdk.view.EditOutlineView.LINE_WIDTH;
//
///**
// * Created by Chale on 2019/9/3 0003.
// * 描述：针对放大镜中节点的文字说明
// */
//public class NewCaptionView extends View {
//    private int movePanColor;
//    private PointF point;
//
//    private final Path mPath = new Path();
//    private final Matrix matrix = new Matrix();
//    private Paint paint;
//    // 放大镜的半径
//    private static final int RADIUS = 260;
//    // 放大倍数
//    private static final float FACT = 2;
//    private float factor;
//    private float cxOnBitmap, cyOnBitmap, cxOnView, cyOnView;
//    float uiOffsetX, uiOffsetY;
//    private float actionBarHeight;
//    private Paint textPaint;
//    private String pxName1;
//    private String pxName2;
//
//    public NewCaptionView(Context context, int movePanColor, String pxName, PointF point) {
//        super(context);
//        this.movePanColor = movePanColor;
//        if (pxName.length() > 16) {
//            this.pxName1 = pxName.substring(0, 16);
//            this.pxName2 = pxName.substring(16);
//        } else {
//            this.pxName1 = pxName;
//            this.pxName2 = "";
//        }
//        this.point = point;
//        initPaint();
//        actionBarHeight = 0;
//    }
//
//    private void initPaint() {
//        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setAntiAlias(true);
//        paint.setStrokeJoin(Paint.Join.ROUND); // 让画的线圆滑
//        paint.setStrokeCap(Paint.Cap.ROUND);
//
//        mPath.addCircle(RADIUS, RADIUS, RADIUS, Path.Direction.CW);
//
//        textPaint = new Paint();
//        textPaint.setColor(movePanColor);
//        textPaint.setStyle(Paint.Style.FILL);
//        textPaint.setStrokeWidth(6);
//        textPaint.setTextSize(50);
//    }
//
//    public void setAttributes(float xOnBitmap, float yOnBitmap,
//                              float imageScale,
//                              float uiOffsetX, float uiOffsetY) {
//        factor = FACT * imageScale;
//        matrix.setScale(factor, factor);
//        cxOnBitmap = xOnBitmap * factor;
//        cyOnBitmap = yOnBitmap * factor;
//        cxOnView = xOnBitmap * imageScale;
//        cyOnView = yOnBitmap * imageScale;
//        this.uiOffsetX = uiOffsetX;
//        this.uiOffsetY = uiOffsetY;
//
//        invalidate();
//    }
//
//    @Override
//    public void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
////        canvas.drawText(pxName1, cxOnView - 0.2f * RADIUS, cyOnView - 0.2f * RADIUS, textPaint);
////        canvas.drawText(pxName2, cxOnView - 0.2f * RADIUS, cyOnView - 0.008f * RADIUS, textPaint);
//        canvas.drawText(pxName1, point.x * 0.4f, point.y, textPaint);
//        canvas.drawText(pxName2, point.x * 0.4f, point.y * 1.08f, textPaint);
//        // 放大镜的位置
//        canvas.translate(cxOnView - RADIUS + uiOffsetX, cyOnView - 1.6f * RADIUS + uiOffsetY + actionBarHeight);
//        // 剪切
//        canvas.clipPath(mPath);
//        canvas.translate(RADIUS - cxOnBitmap, RADIUS - cyOnBitmap);
//
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(LINE_WIDTH);
//        paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(),
//                R.color.text_disable));
//        //画圆边线
//        canvas.drawCircle(cxOnBitmap, cyOnBitmap, RADIUS - LINE_WIDTH / 2, paint);
//        canvas.drawColor(Color.TRANSPARENT);
//        //        paint.setColor(movePanColor);
////        paint.setAlpha(255);
////        //画点击的节点
////        canvas.drawCircle(cxOnBitmap,
////                cyOnBitmap,
////                LINE_WIDTH,
////                paint);
//    }
//}
