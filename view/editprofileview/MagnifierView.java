package com.tozmart.tozisdk.view.editprofileview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.tozmart.tozisdk.R;
import com.tozmart.tozisdk.entity.PaintColor;
import com.tozmart.tozisdk.entity.Pixel;
import com.tozmart.tozisdk.entity.ProfilePaintLine;
import com.tozmart.tozisdk.sdk.OneMeasureSDKLite;
import com.tozmart.tozisdk.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import static com.tozmart.tozisdk.view.EditOutlineView.LINE_WIDTH;

/**
 * 放大镜实现方式2
 *
 * @author chroya
 */

public class MagnifierView extends View {

    private int outlineGoodColor;
    private int outlineBadColor;
    private int movePanColor;

    private final Path mPath = new Path();
    private final Matrix matrix = new Matrix();
    private Paint paint;
    private Bitmap bitmap;
    // 放大镜的半径
    private static final int RADIUS = 260;
    // 放大倍数
    private static final float FACT = 2;
    private float factor;
    private float cxOnBitmap, cyOnBitmap, cxOnView, cyOnView;
    float uiOffsetX, uiOffsetY;
    ArrayList<PointF> drawPoints = new ArrayList<>();
    List<PaintColor> loosePoints;
    ArrayList<ProfilePaintLine> drawPaintLines = new ArrayList<>();
    private float actionBarHeight;

    private Bitmap faceIcon;
    private Context context;
    private Paint textPaint;

    public MagnifierView(Context context, String imageCacheUrl, int outlineGoodColor, int outlineBadColor, int movePanColor) {
        super(context);
        this.context = context;

        this.outlineGoodColor = outlineGoodColor;
        this.outlineBadColor = outlineBadColor;
        this.movePanColor = movePanColor;

        initPaint();

        actionBarHeight = 0;

        bitmap = ImageUtils.getBitmap(imageCacheUrl).copy(Bitmap.Config.RGB_565, true);
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND); // 让画的线圆滑
        paint.setStrokeCap(Paint.Cap.ROUND);

        mPath.addCircle(RADIUS, RADIUS, RADIUS, Direction.CW);

        textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(5);
        textPaint.setTextSize(200);
    }

    public void setAttributes(float xOnBitmap, float yOnBitmap,
                              float imageScale,
                              float uiOffsetX, float uiOffsetY,
                              float pointOffsetX, float pointOffsetY,
                              ArrayList<PointF> drawPoints,
                              List<PaintColor> loosePoints,
                              ArrayList<ProfilePaintLine> drawPaintLines,
                              FaceRectObject faceRectObject) {
        factor = FACT * imageScale;
        matrix.setScale(factor, factor);
        cxOnBitmap = xOnBitmap * factor;
        cyOnBitmap = yOnBitmap * factor;
        cxOnView = xOnBitmap * imageScale;
        cyOnView = yOnBitmap * imageScale;
        this.uiOffsetX = uiOffsetX;
        this.uiOffsetY = uiOffsetY;
        if (faceRectObject != null) {
            RectF faceRect = faceRectObject.getFaceRect();
            faceIcon = BitmapFactory.decodeResource(context.getResources(), faceRectObject.getIconRes());
            ImageUtils.overlayBitmap(faceIcon, bitmap, new Rect(0, 0, faceIcon.getWidth(), faceIcon.getHeight()), new RectF(faceRect.left, faceRect.top,
                    faceRect.right, faceRect.bottom));
        }

        this.drawPoints.clear();
        for (PointF point : drawPoints) {
            this.drawPoints.add(new PointF((point.x + pointOffsetX) * factor, (point.y + pointOffsetY) * factor));
        }
        this.loosePoints = loosePoints;

        this.drawPaintLines.clear();
        for (ProfilePaintLine paintLine : drawPaintLines) {
            if (paintLine.getLineType().equals("free")) {
                ProfilePaintLine paintLineCopy = paintLine.copy();
                Pixel paintPointStart = new Pixel();
                paintPointStart.setX((paintLine.getStartPt().getX() + pointOffsetX) * factor);
                paintPointStart.setY((paintLine.getStartPt().getY() + pointOffsetY) * factor);
                paintLineCopy.setStartPt(paintPointStart);

                Pixel paintPointEnd = new Pixel();
                paintPointEnd.setX((paintLine.getEndPt().getX() + pointOffsetX) * factor);
                paintPointEnd.setY((paintLine.getEndPt().getY() + pointOffsetY) * factor);
                paintLineCopy.setEndPt(paintPointEnd);
                this.drawPaintLines.add(paintLineCopy);
            }
        }

        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null && !bitmap.isRecycled()) {

            // 放大镜的位置
            canvas.translate(cxOnView - RADIUS + uiOffsetX, cyOnView - 1.6f * RADIUS + uiOffsetY + actionBarHeight);
            // 剪切
            canvas.clipPath(mPath);
            canvas.translate(RADIUS - cxOnBitmap, RADIUS - cyOnBitmap);
            // 画放大后的图
            canvas.drawBitmap(bitmap, matrix, null);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(LINE_WIDTH);
            paint.setColor(ContextCompat.getColor(OneMeasureSDKLite.getInstance().getApplicationContext(),
                    R.color.text_disable));
            //画圆边线
            canvas.drawCircle(cxOnBitmap, cyOnBitmap, RADIUS - LINE_WIDTH / 2, paint);

            paint.setColor(outlineGoodColor);
            int l = drawPoints.size();
            for (int i = 0; i < l; i++) {
                canvas.drawLine(drawPoints.get(i).x,
                        drawPoints.get(i).y,
                        drawPoints.get(i + 1 == l ? 0 : i + 1).x,
                        drawPoints.get(i + 1 == l ? 0 : i + 1).y,
                        paint);
            }

            for (ProfilePaintLine paintLine : drawPaintLines) {
                canvas.drawLine(paintLine.getStartPt().getX(),
                        paintLine.getStartPt().getY(),
                        paintLine.getEndPt().getX(),
                        paintLine.getEndPt().getY(),
                        paint);
            }

            paint.setColor(outlineBadColor);
            if (loosePoints != null) {
                for (PaintColor paintColor : loosePoints) {
                    for (int i = paintColor.getLoosePartStart(); i < paintColor.getLoosePartEnd(); i++) {
                        canvas.drawLine(drawPoints.get(i).x,
                                drawPoints.get(i).y,
                                drawPoints.get(i + 1 == l ? 0 : i + 1).x,
                                drawPoints.get(i + 1 == l ? 0 : i + 1).y,
                                paint);
                    }
                }
            }

            paint.setColor(movePanColor);
            paint.setAlpha(255);
            //画点击的节点
            canvas.drawCircle(cxOnBitmap,
                    cyOnBitmap,
                    LINE_WIDTH,
                    paint);
        }
    }

    public void recycleBitmap() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }
}
