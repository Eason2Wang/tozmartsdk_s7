//package com.tozmart.tozisdk.view.editprofileview;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.support.v4.content.ContextCompat;
//import android.view.Gravity;
//
//import com.tozmart.tozisdk.R;
//import com.tozmart.tozisdk.sdk.OneMeasureSDKLite;
//
//import static com.tozmart.tozisdk.view.EditOutlineView.LINE_WIDTH;
//
///**
// * Created by Chale on 2019/9/4 0004.
// */
//public class CustomTextView extends android.support.v7.widget.AppCompatTextView {
//
//    // 放大镜的半径
//    private static final int RADIUS = 260;
//    // 放大倍数
//    private static final float FACT = 2;
//    private float factor;
//    private float cxOnBitmap, cyOnBitmap, cxOnView, cyOnView;
//    float uiOffsetX, uiOffsetY;
//    private final Path mPath = new Path();
//    private final Matrix matrix = new Matrix();
//    private Paint paint;
//    private String pxName;
//
//    public CustomTextView(Context context, int moveTextColor, String pxName) {
//        super(context);
//        this.pxName = pxName;
//        setTextColor(moveTextColor);
//        initPaint();
//    }
//
//    private void initPaint() {
//        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setAntiAlias(true);
//        paint.setStrokeJoin(Paint.Join.ROUND); // 让画的线圆滑
//        paint.setStrokeCap(Paint.Cap.ROUND);
//
//        mPath.addCircle(RADIUS, RADIUS, RADIUS, Path.Direction.CW);
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
//        // 放大镜的位置
//        canvas.translate(cxOnView - RADIUS + uiOffsetX, cyOnView - 1.6f * RADIUS + uiOffsetY);
//        // 剪切
//        setText(pxName);
//        setGravity(Gravity.CENTER);
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
//    }
//}
