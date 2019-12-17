package com.tozmart.tozisdk.view;

import android.app.Service;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Vibrator;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.tozmart.tozisdk.R;
import com.tozmart.tozisdk.api.OnEditListener;
import com.tozmart.tozisdk.entity.PaintColor;
import com.tozmart.tozisdk.entity.PaintMovPx;
import com.tozmart.tozisdk.entity.Pixel;
import com.tozmart.tozisdk.entity.Profile2ModelData;
import com.tozmart.tozisdk.entity.ProfilePaintLine;
import com.tozmart.tozisdk.sdk.OneMeasureSDKLite;
import com.tozmart.tozisdk.utils.FileUtil;
import com.tozmart.tozisdk.utils.ImageUtils;
import com.tozmart.tozisdk.utils.SDKUtil;
import com.tozmart.tozisdk.utils.ScreenUtils;
import com.tozmart.tozisdk.view.editprofileview.CaptionView;
import com.tozmart.tozisdk.view.editprofileview.CurrentMode;
import com.tozmart.tozisdk.view.editprofileview.FaceRectObject;
import com.tozmart.tozisdk.view.editprofileview.MagnifierView;
import com.tozmart.tozisdk.view.editprofileview.MoveFreeLineObject;
import com.tozmart.tozisdk.view.editprofileview.MovePanObject;
import com.tozmart.tozisdk.view.editprofileview.MoveRectObject;
import com.tozmart.tozisdk.view.editprofileview.OnImageLoadListener;
import com.tozmart.tozisdk.view.editprofileview.OnMoveListener;
import com.tozmart.tozisdk.view.editprofileview.OnScaleChangeListener;
import com.tozmart.tozisdk.view.editprofileview.OnScaleEndListener;
import com.tozmart.tozisdk.view.editprofileview.OnTouchDownListener;
import com.tozmart.tozisdk.view.editprofileview.OnTouchUpListener;
import com.tozmart.tozisdk.view.editprofileview.PhotoDraweeView;
import com.tozmart.tozisdk.view.editprofileview.Step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import static com.tozmart.tozisdk.constant.PhotoType.FRONT;

public class EditOutlineView extends PhotoDraweeView {

    protected int outlineGoodColor;
    protected int outlineBadColor;
    protected int movePanColor;
    protected Drawable moveRectSrc;
    protected int faceCoverRes;

    private Vibrator mVibrator;//声明一个振动器对象

    public boolean isChangedAnything = false;// 是否对该页面进行了任何修改

    public static final int LINE_WIDTH = 5;

    private Stack<Step> undoStack = new Stack<>();

    private Uri frontImageUri;
    private Uri sideImageUri;
    MagnifierView magnifierView;// 放大镜
    CaptionView captionView;// 选择节点文本说明
    //    NewCaptionView newCaptionView;// 选择节点文本说明
    private CurrentMode currentMode = CurrentMode.NONE;

    private List<PaintMovPx> moveIndex = new ArrayList<>();
    private List<PaintMovPx> moveIndexSelected;
    private List<ProfilePaintLine> paintLinesSelected;
    private ArrayList<MovePanObject> movePanObjects = new ArrayList<>();
    private ArrayList<MoveRectObject> moveRectObjects = new ArrayList<>();
    private ArrayList<MoveFreeLineObject> moveFreeLineObjects = new ArrayList<>();

    // 创建画笔
    private Paint p;
    private Context mContext;
    private ArrayList<PointF> mOriPoints = new ArrayList<>();//原始的点
    private ArrayList<PointF> mDrawPoints = new ArrayList<>();//不断重画的点
    private ArrayList<PointF> mScaleDrawPoints = new ArrayList<>();//不断重画的画布上点
    private ArrayList<PointF> mSavePoints = new ArrayList<>();//edit后的点
    private ArrayList<ProfilePaintLine> oriPaintLines = new ArrayList<>();
    private ArrayList<ProfilePaintLine> drawPaintLines = new ArrayList<>();
    private ArrayList<ProfilePaintLine> scalePaintLines = new ArrayList<>();
    private ArrayList<ProfilePaintLine> savePaintLines = new ArrayList<>();
    private List<PaintColor> loosePoints;// 不准确的线下标
    private float oriImageHeight;
    private float canPointMoveDis;// 限制编辑点的移动范围
    private float circleRadius = 20;
    private float scaleCircleRadius;
    private float rectSize;
    private MovePanObject selectedMovePanObject;
    private MoveRectObject selectedMoveRectObject;
    private MoveFreeLineObject selectedFreeLineObject;
    private ProfilePaintLine selectPaintLine;
    private String imageCacheUrl;

    private float editTouchX;//编辑模式下用户触摸的位置
    private float editTouchY;

    private boolean isDrawOutline = true;

    private int screenWidth;

    /**
     * 遮挡脸
     */
    private FaceRectObject faceRectObject;

    private int flag;
    private Profile2ModelData profile2ModelData;
    private boolean coverFace;
    private SparseArray<PaintMovPx> moveIndexMap;
    //选中的点的文字说明
    private String pxName = "";
    //选中的点的坐标
    private PointF selectPoint;

    public EditOutlineView(Context context) {
        this(context, null);
    }

    public EditOutlineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditOutlineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.EditOutlineView);
            outlineGoodColor = a.getColor(R.styleable.EditOutlineView_outlineGoodColor, ContextCompat.getColor(context, R.color.outline_color));
            outlineBadColor = a.getColor(R.styleable.EditOutlineView_outlineBadColor, ContextCompat.getColor(context, R.color.outline_error_color));
            movePanColor = a.getColor(R.styleable.EditOutlineView_movePanColor, ContextCompat.getColor(context, R.color.color_50E3C2));
            moveRectSrc = a.getDrawable(R.styleable.EditOutlineView_moveRectSrc);
            faceCoverRes = a.getResourceId(R.styleable.EditOutlineView_faceCoverRes, R.drawable.ic_cover_face);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
        init(context);
    }

    protected void init(final Context context) {
        mContext = context;

        mVibrator = (Vibrator) OneMeasureSDKLite.getInstance().getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

        /**
         * 方法 说明 drawRect 绘制矩形 drawCircle 绘制圆形 drawOval 绘制椭圆 drawPath 绘制任意多边形
         * drawLine 绘制直线 drawPoint 绘制点
         */
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);
        p.setStrokeJoin(Paint.Join.ROUND); // 让画的线圆滑
        p.setStrokeCap(Paint.Cap.ROUND);

        screenWidth = ScreenUtils.getScreenWidth();

        setOnDragListener(new com.tozmart.tozisdk.view.editprofileview.OnDragListener() {
            @Override
            public void onDrag(float offsetX, float offsetY) {
                setFaceRect();
                isDrawOutline = false;
            }
        });
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (getAttacher().getDisplayRect() != null) {
                    if (currentMode == CurrentMode.NONE) {
                        // 先判断是否选中rect
                        for (MoveRectObject object : moveRectObjects) {
                            if (object.contains(editTouchX, editTouchY)) {
                                selectedMoveRectObject = object;
                                break;
                            }
                        }
                        if (selectedMoveRectObject != null) {
                            SDKUtil.vibrate(mVibrator, 50);
                            selectedMoveRectObject.setSelected(true);
                            for (ProfilePaintLine paintLine : savePaintLines) {
                                if (paintLine.getLocation() == selectedMoveRectObject.getPositionIndex()) {
                                    selectPaintLine = paintLine.copy();
                                    break;
                                }
                            }
                            currentMode = CurrentMode.EDIT_RECT_MODE;
                            if (onEditListener != null) {
                                onEditListener.onEdit(currentMode, undoStack.size() != 1);
                            }
                            getAttacher().disableScaleDrag(true);
                            invalidate();
                            return false;
                        }
                        // 再判断是否选中freeline
                        for (MoveFreeLineObject object : moveFreeLineObjects) {
                            if (object.isTouchStart(editTouchX, editTouchY)) {
                                selectedFreeLineObject = object;
                                selectedFreeLineObject.setStartSelected(true);
                                selectedFreeLineObject.setEndSelected(false);
                            } else if (object.isTouchEnd(editTouchX, editTouchY)) {
                                selectedFreeLineObject = object;
                                selectedFreeLineObject.setStartSelected(false);
                                selectedFreeLineObject.setEndSelected(true);
                            }
                        }

                        // 再判断是否选中pan
                        for (MovePanObject object : movePanObjects) {
                            if (object.contains(editTouchX, editTouchY) && !object.isSticked()) {
                                selectedMovePanObject = object;
                                pxName = moveIndexMap.get(object.getPositionIndex()).getPxName();
                                selectPoint = object.getPosition();
                                break;
                            }
                        }

                        if (selectedFreeLineObject != null) {
                            SDKUtil.vibrate(mVibrator, 50);
                            for (ProfilePaintLine paintLine : savePaintLines) {
                                if (paintLine.getPaintLineId() == selectedFreeLineObject.getPaintLineId()) {
                                    selectPaintLine = paintLine.copy();
                                    break;
                                }
                            }
                            currentMode = CurrentMode.EDIT_FREE_LINE_MODE;
                            if (onEditListener != null) {
                                onEditListener.onEdit(currentMode, undoStack.size() != 1);
                            }
                            getAttacher().disableScaleDrag(true);

                            float imageScale = getAttacher().getDisplayRect().height() / oriImageHeight;
                            magnifierView = new MagnifierView(mContext, imageCacheUrl, outlineGoodColor, outlineBadColor, movePanColor);

                            captionView = new CaptionView(context, movePanColor, pxName);
                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                            ((RelativeLayout) getParent()).addView(magnifierView, params);
                            ((RelativeLayout) getParent()).addView(captionView, params);
                            if (selectedFreeLineObject.isStartSelected()) {
                                magnifierView.setAttributes((selectedFreeLineObject.getStartPosition().x - getAttacher().getDisplayRect().left) / imageScale,
                                        (selectedFreeLineObject.getStartPosition().y - getAttacher().getDisplayRect().top) / imageScale,
                                        imageScale,
                                        getAttacher().getDisplayRect().left,
                                        getAttacher().getDisplayRect().top,
                                        0,
                                        0,
                                        mDrawPoints,
                                        loosePoints,
                                        drawPaintLines,
                                        faceRectObject);
                            } else {
                                magnifierView.setAttributes((selectedFreeLineObject.getEndPosition().x - getAttacher().getDisplayRect().left) / imageScale,
                                        (selectedFreeLineObject.getEndPosition().y - getAttacher().getDisplayRect().top) / imageScale,
                                        imageScale,
                                        getAttacher().getDisplayRect().left,
                                        getAttacher().getDisplayRect().top,
                                        0,
                                        0,
                                        mDrawPoints,
                                        loosePoints,
                                        drawPaintLines,
                                        faceRectObject);
                            }
                            captionView.setAttributes(selectPoint, (selectedFreeLineObject.getEndPosition().x - getAttacher().getDisplayRect().left) / imageScale,
                                    (selectedFreeLineObject.getEndPosition().y - getAttacher().getDisplayRect().top) / imageScale);
                            invalidate();
                            return false;
                        }

                        if (selectedMovePanObject != null) {
                            SDKUtil.vibrate(mVibrator, 50);
                            selectedMovePanObject.setSelected(true);
                            currentMode = CurrentMode.EDIT_LINE_MODE;
                            if (onEditListener != null) {
                                onEditListener.onEdit(currentMode, undoStack.size() != 1);
                            }
                            getAttacher().disableScaleDrag(true);

                            float imageScale = getAttacher().getDisplayRect().height() / oriImageHeight;
                            magnifierView = new MagnifierView(mContext, imageCacheUrl, outlineGoodColor, outlineBadColor, movePanColor);

                            captionView = new CaptionView(context, movePanColor, pxName);
                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                            ((RelativeLayout) getParent()).addView(magnifierView, params);
                            ((RelativeLayout) getParent()).addView(captionView, params);

                            magnifierView.setAttributes(mDrawPoints.get(selectedMovePanObject.getPositionIndex()).x,
                                    mDrawPoints.get(selectedMovePanObject.getPositionIndex()).y,
                                    imageScale,
                                    getAttacher().getDisplayRect().left,
                                    getAttacher().getDisplayRect().top,
                                    0,
                                    0,
                                    mDrawPoints,
                                    loosePoints,
                                    drawPaintLines,
                                    faceRectObject);
                            captionView.setAttributes(selectPoint, mDrawPoints.get(selectedMovePanObject.getPositionIndex()).x,
                                    mDrawPoints.get(selectedMovePanObject.getPositionIndex()).y);
                            invalidate();
                        }
                    }
                }
                return false;
            }
        });
        setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
        });
        setOnMoveListener(new OnMoveListener() {
            @Override
            public void onMove(float eventX, float eventY) {
                if (getAttacher().getDisplayRect() != null) {
                    setFaceRect();
                    if (currentMode == CurrentMode.EDIT_RECT_MODE) {
                        isChangedAnything = true;
                        float imageScale = getAttacher().getDisplayRect().height() / oriImageHeight;
                        resetDrawRect((eventY - editTouchY) / imageScale);
                        invalidate();
                    } else if (captionView != null && magnifierView != null && currentMode == CurrentMode.EDIT_FREE_LINE_MODE) {
                        isChangedAnything = true;
                        float imageScale = getAttacher().getDisplayRect().height() / oriImageHeight;
                        List<ProfilePaintLine> lines =
                                resetPaintLines((eventX - editTouchX) / imageScale, (eventY - editTouchY) / imageScale);
                        if (selectedFreeLineObject.isStartSelected()) {
                            selectedFreeLineObject.setStartPosition(
                                    new PointF(lines.get(1).getStartPt().getX(),
                                            lines.get(1).getStartPt().getY()));
                            magnifierView.setAttributes(lines.get(0).getStartPt().getX(),
                                    lines.get(0).getStartPt().getY(),
                                    imageScale,
                                    getAttacher().getDisplayRect().left,
                                    getAttacher().getDisplayRect().top,
                                    0,
                                    0,
                                    mDrawPoints,
                                    loosePoints,
                                    drawPaintLines,
                                    faceRectObject);

//                            captionView.setAttributes(lines.get(0).getStartPt().getX(),
//                                    lines.get(0).getStartPt().getY(),
//                                    imageScale,
//                                    getAttacher().getDisplayRect().left,
//                                    getAttacher().getDisplayRect().top);
                        } else if (selectedFreeLineObject.isEndSelected()) {
                            selectedFreeLineObject.setEndPosition(
                                    new PointF(lines.get(1).getEndPt().getX(),
                                            lines.get(1).getEndPt().getY()));
                            magnifierView.setAttributes(lines.get(0).getEndPt().getX(),
                                    lines.get(0).getEndPt().getY(),
                                    imageScale,
                                    getAttacher().getDisplayRect().left,
                                    getAttacher().getDisplayRect().top,
                                    0,
                                    0,
                                    mDrawPoints,
                                    loosePoints,
                                    drawPaintLines,
                                    faceRectObject);
//                            captionView.setAttributes(lines.get(0).getEndPt().getX(),
//                                    lines.get(0).getEndPt().getY(),
//                                    imageScale,
//                                    getAttacher().getDisplayRect().left,
//                                    getAttacher().getDisplayRect().top);
                        }
                        invalidate();
                    } else if (captionView != null && magnifierView != null && currentMode == CurrentMode.EDIT_LINE_MODE) {
                        isChangedAnything = true;
                        float imageScale = getAttacher().getDisplayRect().height() / oriImageHeight;
                        resetDrawPoints((eventX - editTouchX) / imageScale, (eventY - editTouchY) / imageScale);
                        magnifierView.setAttributes(mDrawPoints.get(selectedMovePanObject.getPositionIndex()).x,
                                mDrawPoints.get(selectedMovePanObject.getPositionIndex()).y,
                                imageScale,
                                getAttacher().getDisplayRect().left,
                                getAttacher().getDisplayRect().top,
                                0,
                                0,
                                mDrawPoints,
                                loosePoints,
                                drawPaintLines,
                                faceRectObject);
//                        captionView.setAttributes(mDrawPoints.get(selectedMovePanObject.getPositionIndex()).x,
//                                mDrawPoints.get(selectedMovePanObject.getPositionIndex()).y,
//                                imageScale,
//                                getAttacher().getDisplayRect().left,
//                                getAttacher().getDisplayRect().top);
                        selectedMovePanObject.setPosition(mScaleDrawPoints.get(selectedMovePanObject.getPositionIndex()));
                        invalidate();
                    }
                }
            }
        });
        setOnScaleChangeListener(new OnScaleChangeListener() {
            @Override
            public void onScaleChange(float scaleFactor, float focusX, float focusY) {
                setFaceRect();
                isDrawOutline = false;
            }
        });
        setOnTouchDownListener(new OnTouchDownListener() {
            @Override
            public void onTouchDown(float eventX, float eventY) {
                editTouchX = eventX;
                editTouchY = eventY;
            }
        });
        setOnTouchUpListener(new OnTouchUpListener() {
            @Override
            public void onTouchUp(float eventX, float eventY) {
                setFaceRect();
                calcuScaleDrawPoints();
                calcuScalePans();
                getAttacher().disableScaleDrag(false);
                if (currentMode == CurrentMode.EDIT_LINE_MODE) {
                    if (magnifierView != null && captionView != null) {
                        isChangedAnything = true;
                        ((RelativeLayout) getParent()).removeView(magnifierView);
                        ((RelativeLayout) getParent()).removeView(captionView);
                        magnifierView = null;
                        captionView = null;
                        selectedMovePanObject = null;

                        currentMode = CurrentMode.NONE;
                        resetMoveState();

                        saveEditPointsTmp();
                        undoStack.push(new Step(mDrawPoints, movePanObjects, drawPaintLines, moveRectObjects, moveFreeLineObjects, currentMode));
                        if (onEditListener != null) {
                            onEditListener.onEdit(currentMode, undoStack.size() != 1);
                        }
                    }
                } else if (currentMode == CurrentMode.EDIT_RECT_MODE) {
                    isChangedAnything = true;
                    selectedMoveRectObject = null;

                    currentMode = CurrentMode.NONE;
                    resetMoveState();

                    saveEditPaintLineTmp();
                    undoStack.push(new Step(mDrawPoints, movePanObjects, drawPaintLines, moveRectObjects, moveFreeLineObjects, currentMode));
                    if (onEditListener != null) {
                        onEditListener.onEdit(currentMode, undoStack.size() != 1);
                    }
                } else if (currentMode == CurrentMode.EDIT_FREE_LINE_MODE) {
                    if (magnifierView != null && captionView != null) {
                        isChangedAnything = true;
                        ((RelativeLayout) getParent()).removeView(magnifierView);
                        ((RelativeLayout) getParent()).removeView(captionView);
                        magnifierView = null;
                        captionView = null;
                        selectedFreeLineObject = null;

                        currentMode = CurrentMode.NONE;
                        resetMoveState();

                        saveEditPaintLineTmp();
                        undoStack.push(new Step(mDrawPoints, movePanObjects, drawPaintLines, moveRectObjects, moveFreeLineObjects, currentMode));
                        if (onEditListener != null) {
                            onEditListener.onEdit(currentMode, undoStack.size() != 1);
                        }
                    }
                }
            }
        });
        setOnScaleEndListener(new OnScaleEndListener() {
            @Override
            public void onScaleEnd() {
                setFaceRect();
                calcuScaleDrawPoints();
                calcuScalePans();
            }
        });
        setOnImageLoadListener(new OnImageLoadListener() {
            @Override
            public void onImageLoad() {
                setFaceRect();
                calcuScaleDrawPoints();
                initMoveObjects();
                calcuScalePans();
                if (undoStack.size() == 0) {
                    undoStack.push(new Step(mDrawPoints, movePanObjects, drawPaintLines, moveRectObjects, moveFreeLineObjects, currentMode));
                }
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (getAttacher().getDisplayRect() != null && mDrawPoints.size() != 0 && mDrawPoints.size() == mScaleDrawPoints.size()) {
            //face
            if (faceRectObject != null) {
                faceRectObject.drawRect(canvas);
            }
            if (isDrawOutline) {
                // 画轮廓线
                p.setStrokeWidth(LINE_WIDTH);//设置粗细
                p.setColor(outlineGoodColor);
                int l = mScaleDrawPoints.size();
                for (int i = 0; i < l; i++) {
                    canvas.drawLine(mScaleDrawPoints.get(i).x,
                            mScaleDrawPoints.get(i).y,
                            mScaleDrawPoints.get(i + 1 == l ? 0 : i + 1).x,
                            mScaleDrawPoints.get(i + 1 == l ? 0 : i + 1).y,
                            p);
                }
                p.setColor(outlineBadColor);
                if (loosePoints != null) {
                    for (PaintColor paintColor : loosePoints) {
                        for (int i = paintColor.getLoosePartStart(); i < paintColor.getLoosePartEnd(); i++) {
                            canvas.drawLine(mScaleDrawPoints.get(i).x,
                                    mScaleDrawPoints.get(i).y,
                                    mScaleDrawPoints.get(i + 1 == l ? 0 : i + 1).x,
                                    mScaleDrawPoints.get(i + 1 == l ? 0 : i + 1).y,
                                    p);
                        }
                    }
                }

                for (MovePanObject object : movePanObjects) {
                    object.drawPan(canvas);
                }
                for (MoveRectObject object : moveRectObjects) {
                    object.drawRectLine(canvas);
                }
                for (MoveFreeLineObject object : moveFreeLineObjects) {
                    object.drawLine(canvas);
                }
            }
        }
    }

    /**
     * 计算放大之后的各个点集
     */
    private void calcuScaleDrawPoints() {
        if (getAttacher().getDisplayRect() != null && mDrawPoints.size() != 0) {
            isDrawOutline = true;
            float imageScale = getAttacher().getDisplayRect().height() / oriImageHeight;
            scaleCircleRadius = circleRadius * imageScale;

            int l = mDrawPoints.size();
            mScaleDrawPoints.clear();
            for (int i = 0; i < l; i++) {
                mScaleDrawPoints.add(new PointF((mDrawPoints.get(i).x) * imageScale + getAttacher().getDisplayRect().left,
                        (mDrawPoints.get(i).y) * imageScale + getAttacher().getDisplayRect().top));
            }

            int index = 0;
            int size = drawPaintLines.size();
            for (ProfilePaintLine paintLine : drawPaintLines) {
                ProfilePaintLine shuaiYinProfilePaintLine = paintLine.copy();

                Pixel paintPointStart = new Pixel();
                paintPointStart.setX(paintLine.getStartPt().getX() * imageScale + getAttacher().getDisplayRect().left);
                paintPointStart.setY(paintLine.getStartPt().getY() * imageScale + getAttacher().getDisplayRect().top);
                shuaiYinProfilePaintLine.setStartPt(paintPointStart);

                Pixel paintPointEnd = new Pixel();
                paintPointEnd.setX(paintLine.getEndPt().getX() * imageScale + getAttacher().getDisplayRect().left);
                paintPointEnd.setY(paintLine.getEndPt().getY() * imageScale + getAttacher().getDisplayRect().top);
                shuaiYinProfilePaintLine.setEndPt(paintPointEnd);

                scalePaintLines.set(index, shuaiYinProfilePaintLine);
                index++;
            }

            invalidate();
        }
    }

    private void calcuScalePans() {
        if (getAttacher().getDisplayRect() != null && mDrawPoints.size() != 0) {
            float imageScale = getAttacher().getDisplayRect().height() / oriImageHeight;

            for (MovePanObject object : movePanObjects) {
                object.setPosition(mScaleDrawPoints.get(object.getPositionIndex()));
                object.setRadius(circleRadius * imageScale);
            }
            setMoveRectObjects();
            setFreeLineObjects();
            invalidate();
        }
    }

    public void initialize(int flag,
                           Bitmap image,
                           Profile2ModelData profile2ModelData,
                           boolean coverFace) {
        oriImageHeight = image.getHeight();
        this.flag = flag;
        this.profile2ModelData = profile2ModelData;
        this.coverFace = coverFace;
        mOriPoints.clear();
        mDrawPoints.clear();
        mSavePoints.clear();
        ArrayList<PointF> points = new ArrayList<PointF>();
        ArrayList<ProfilePaintLine> paintLines = new ArrayList<>();
        this.loosePoints = new ArrayList<>();
        this.moveIndex = new ArrayList<>();
        if (flag == FRONT) {
            int drawPoints_front_size = profile2ModelData.getFrontAllPoints().size();
            if (drawPoints_front_size != 0) {
                for (int i = 0; i < drawPoints_front_size; i++) {
                    points.add(new PointF(profile2ModelData.getFrontAllPoints().get(i).x,
                            profile2ModelData.getFrontAllPoints().get(i).y));
                }
            }
            paintLines.addAll(Arrays.asList(profile2ModelData.getFrontPaintLines()));
            if (profile2ModelData.getfLooseIdx() != null) {
                this.loosePoints.addAll(Arrays.asList(profile2ModelData.getfLooseIdx()));
            }
            this.moveIndex.addAll(Arrays.asList(profile2ModelData.getFrontMoveIndex()));
            if (profile2ModelData.getFrontMoveIndexSelected() != null)
                this.moveIndexSelected = Arrays.asList(profile2ModelData.getFrontMoveIndexSelected());
            if (profile2ModelData.getFrontPaintLinesSelected() != null)
                this.paintLinesSelected = Arrays.asList(profile2ModelData.getFrontPaintLinesSelected());
            if (coverFace) {
                if (faceCoverRes == 0) {
                    faceCoverRes = R.drawable.ic_cover_face;
                }
                faceRectObject = new FaceRectObject(mContext, profile2ModelData.getFrontFaceRect(), faceCoverRes);
            }
        } else {
            int drawPoints_side_size = profile2ModelData.getSideAllPoints().size();
            if (drawPoints_side_size != 0) {
                for (int i = 0; i < drawPoints_side_size; i++) {
                    points.add(new PointF(profile2ModelData.getSideAllPoints().get(i).x,
                            profile2ModelData.getSideAllPoints().get(i).y));
                }
            }
            paintLines.addAll(Arrays.asList(profile2ModelData.getSidePaintLines()));
            if (profile2ModelData.getsLooseIdx() != null) {
                this.loosePoints.addAll(Arrays.asList(profile2ModelData.getsLooseIdx()));
            }
            this.moveIndex.addAll(Arrays.asList(profile2ModelData.getSideMoveIndex()));
            if (profile2ModelData.getSideMoveIndexSelected() != null)
                this.moveIndexSelected = Arrays.asList(profile2ModelData.getSideMoveIndexSelected());
            if (profile2ModelData.getSidePaintLinesSelected() != null)
                this.paintLinesSelected = Arrays.asList(profile2ModelData.getSidePaintLinesSelected());
            if (coverFace) {
                if (faceCoverRes == 0) {
                    faceCoverRes = R.drawable.ic_cover_face;
                }
                faceRectObject = new FaceRectObject(mContext, profile2ModelData.getSideFaceRect(), faceCoverRes);
            }
        }
        mOriPoints.addAll(points);
        mDrawPoints.addAll(points);
        mSavePoints.addAll(points);
        this.drawPaintLines.clear();
        this.drawPaintLines.addAll(paintLines);
        this.scalePaintLines.clear();
        this.scalePaintLines.addAll(paintLines);
        this.oriPaintLines.clear();
        this.savePaintLines.clear();
        for (ProfilePaintLine paintLine : paintLines) {
            this.oriPaintLines.add(paintLine.copy());
            this.savePaintLines.add(paintLine.copy());
        }

        circleRadius = oriImageHeight * 0.02f;
        canPointMoveDis = oriImageHeight * 0.05f;
        rectSize = oriImageHeight * 0.04f;

        imageCacheUrl = FileUtil.getAppExternalCachePath() + flag + "editcache.jpg";
        ImageUtils.save(image, imageCacheUrl, Bitmap.CompressFormat.JPEG);
        if (flag == FRONT) {
            frontImageUri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_FILE_SCHEME)
                    .path(imageCacheUrl)
                    .build();
            setPhotoUri(frontImageUri);
        } else {
            sideImageUri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_FILE_SCHEME)
                    .path(imageCacheUrl)
                    .build();
            setPhotoUri(sideImageUri);
        }

        //特殊点的信息数组
        int size = moveIndex.size();
        moveIndexMap = new SparseArray();
        for (int i = 0; i < size; i++) {
            PaintMovPx paintMovPx = moveIndex.get(i);
            int pxIndex = paintMovPx.getPxIndex();
            if (pxIndex == -1) {
                continue;
            }
            moveIndexMap.put(pxIndex, paintMovPx);
        }
    }

    private void initMoveObjects() {
        movePanObjects.clear();
        for (PaintMovPx index : moveIndex) {
            boolean isVisible = false;
            if (moveIndexSelected != null) {
                for (PaintMovPx selected : moveIndexSelected) {
                    if (selected.getPxName().equals(index.getPxName())) {
                        isVisible = true;
                        break;
                    }
                }
            } else {
                isVisible = true;
            }
            // 非关键点非显示点，则不需加入
            if (index.getPxProfess() == 0 || isVisible)
                addPanObject(index.getPxIndex(), false, isVisible);
        }
        setMoveRectObjects();
        setFreeLineObjects();
    }

    private void setFreeLineObjects() {
        moveFreeLineObjects.clear();
        for (ProfilePaintLine paintLine : scalePaintLines) {
            if (paintLinesSelected != null) {
                for (ProfilePaintLine selected : paintLinesSelected) {
                    if (selected.getLineName().equals(paintLine.getLineName())) {
                        addFreeLineObject(paintLine);
                        break;
                    }
                }
            } else {
                addFreeLineObject(paintLine);
            }
        }
    }

    private void setMoveRectObjects() {
        moveRectObjects.clear();
        for (ProfilePaintLine paintLine : scalePaintLines) {
            if (paintLinesSelected != null) {
                for (ProfilePaintLine selected : paintLinesSelected) {
                    if (selected.getLineName().equals(paintLine.getLineName())) {
                        addRectObject(paintLine.getLocation(), paintLine.getLineType());
                        break;
                    }
                }
            } else {
                addRectObject(paintLine.getLocation(), paintLine.getLineType());
            }
        }
    }

    private void setFaceRect() {
        if (getAttacher().getDisplayRect() != null && mDrawPoints.size() != 0) {
            float imageScale = getAttacher().getDisplayRect().height() / oriImageHeight;
            if (faceRectObject != null) {
                faceRectObject.setAttributes(imageScale, getAttacher().getDisplayRect().left, getAttacher().getDisplayRect().top);
            }
        }
    }

    public void addPanObject(int positionIndex, boolean isSticked, boolean isVisible) {
        if (getAttacher().getDisplayRect() != null) {
            float imageScale = getAttacher().getDisplayRect().height() / oriImageHeight;
            scaleCircleRadius = circleRadius * imageScale;
            MovePanObject movePanObject = new MovePanObject(scaleCircleRadius, mScaleDrawPoints.get(positionIndex), movePanColor);
            movePanObject.setPositionIndex(positionIndex);
            movePanObject.setSticked(isSticked);
            movePanObject.setVisible(isVisible);
            movePanObjects.add(movePanObject);

            invalidate();
        }
    }

    private void addRectObject(int positionIndex, String lineType) {
        if (getAttacher().getDisplayRect() != null) {
            float imageScale = getAttacher().getDisplayRect().height() / oriImageHeight;
            float scaleRectSize = rectSize * imageScale;
            if (lineType.equals("L")) {
                MoveRectObject moveRectObject = new MoveRectObject((int) scaleRectSize,
                        new PointF(0, mScaleDrawPoints.get(positionIndex).y),
                        mScaleDrawPoints.get(positionIndex), lineType, moveRectSrc);
                moveRectObject.setPositionIndex(positionIndex);
                moveRectObjects.add(moveRectObject);
            } else if (lineType.equals("R")) {
                MoveRectObject moveRectObject = new MoveRectObject((int) scaleRectSize,
                        new PointF(screenWidth, mScaleDrawPoints.get(positionIndex).y),
                        mScaleDrawPoints.get(positionIndex), lineType, moveRectSrc);
                moveRectObject.setPositionIndex(positionIndex);
                moveRectObjects.add(moveRectObject);
            }

            invalidate();
        }
    }

    private void addFreeLineObject(ProfilePaintLine paintLine) {
        if (getAttacher().getDisplayRect() != null) {
            if (paintLine.getLineType().equals("free")) {
                float imageScale = getAttacher().getDisplayRect().height() / oriImageHeight;
                scaleCircleRadius = circleRadius * imageScale;
                MoveFreeLineObject moveFreeLineObject = new MoveFreeLineObject(scaleCircleRadius,
                        new PointF(paintLine.getStartPt().getX(), paintLine.getStartPt().getY()),
                        new PointF(paintLine.getEndPt().getX(), paintLine.getEndPt().getY()), movePanColor);
                moveFreeLineObject.setPaintLineId(paintLine.getPaintLineId());
                moveFreeLineObjects.add(moveFreeLineObject);
            }
        }
    }

    public void resetDrawRect(float transY) {
        if (getAttacher().getDisplayRect() != null) {
            float imageScale = getAttacher().getDisplayRect().height() / oriImageHeight;
            ProfilePaintLine shuaiYinProfilePaintLine = selectPaintLine.copy();
            //限制可移动的范围
            float upY = Math.max(mOriPoints.get(shuaiYinProfilePaintLine.getDnRange()).y,
                    mOriPoints.get(shuaiYinProfilePaintLine.getUpRange()).y);
            float downY = Math.min(mOriPoints.get(shuaiYinProfilePaintLine.getDnRange()).y,
                    mOriPoints.get(shuaiYinProfilePaintLine.getUpRange()).y);
            if (shuaiYinProfilePaintLine.getStartPt().getY() + transY > upY) {
                transY = upY - shuaiYinProfilePaintLine.getStartPt().getY();
            } else if (shuaiYinProfilePaintLine.getStartPt().getY() + transY < downY) {
                transY = downY - shuaiYinProfilePaintLine.getStartPt().getY();
            }
            Log.e("paintTrans", String.valueOf(transY));
            PointF pointF = new PointF(shuaiYinProfilePaintLine.getStartPt().getX(),
                    shuaiYinProfilePaintLine.getStartPt().getY() + transY);
            ArrayList<Object> nearObjs = setLinePOnOutline(mSavePoints, pointF,
                    shuaiYinProfilePaintLine.getDnRange(), shuaiYinProfilePaintLine.getUpRange());
            pointF = (PointF) nearObjs.get(1);
            Pixel paintPointStart = new Pixel();
            paintPointStart.setX(pointF.x);
            paintPointStart.setY(pointF.y);
            shuaiYinProfilePaintLine.setStartPt(paintPointStart);

            Pixel paintPointEnd = new Pixel();
            paintPointEnd.setX(pointF.x);
            paintPointEnd.setY(pointF.y);
            shuaiYinProfilePaintLine.setEndPt(paintPointEnd);
            shuaiYinProfilePaintLine.setLocation((Integer) nearObjs.get(0));
            drawPaintLines.set(getPaintLineIndex(savePaintLines, selectPaintLine), shuaiYinProfilePaintLine);
            Log.e("paintIndex", String.valueOf(nearObjs.get(0)));
            selectedMoveRectObject.setRectWidth((int) (rectSize * imageScale));
            selectedMoveRectObject.setPositionIndex((Integer) nearObjs.get(0));
            if (selectedMoveRectObject.getLineType().equals("L")) {
                selectedMoveRectObject.setStartPosition(
                        new PointF(0, mScaleDrawPoints.get(selectedMoveRectObject.getPositionIndex()).y));
            } else if (selectedMoveRectObject.getLineType().equals("R")) {
                selectedMoveRectObject.setStartPosition(
                        new PointF(screenWidth, mScaleDrawPoints.get(selectedMoveRectObject.getPositionIndex()).y));
            }
            selectedMoveRectObject.setEndPosition(mScaleDrawPoints.get(selectedMoveRectObject.getPositionIndex()));

            ProfilePaintLine shuaiYinProfilePaintLineScale = selectPaintLine.copy();
            Pixel paintPointStartScale = new Pixel();
            paintPointStartScale.setX(pointF.x * imageScale + getAttacher().getDisplayRect().left);
            paintPointStartScale.setY(pointF.y * imageScale + getAttacher().getDisplayRect().top);
            shuaiYinProfilePaintLineScale.setStartPt(paintPointStartScale);

            Pixel paintPointEndScale = new Pixel();
            paintPointEndScale.setX(pointF.x * imageScale + getAttacher().getDisplayRect().left);
            paintPointEndScale.setY(pointF.y * imageScale + getAttacher().getDisplayRect().top);
            shuaiYinProfilePaintLineScale.setEndPt(paintPointEndScale);
            scalePaintLines.set(getPaintLineIndex(savePaintLines, selectPaintLine), shuaiYinProfilePaintLineScale);
        }
    }

    public void resetDrawPoints(float transX, float transY) {
        if (getAttacher().getDisplayRect() != null) {
            float x, y;
            float imageScale = getAttacher().getDisplayRect().height() / oriImageHeight;
            int panIndex = selectedMovePanObject.getPositionIndex();
            int pointsSize = mSavePoints.size();
            int[] limits = findNearestLimitIndex(panIndex);
            // 已触发的移动点的上下边界点下标
            int mSelectedPointsDownIndex = Math.min(limits[0], limits[1]);
            int mSelectedPointsUpIndex = Math.max(limits[0], limits[1]);

            //限制可移动的范围
            if (mSavePoints.get(panIndex).x + transX
                    > mOriPoints.get(panIndex).x + canPointMoveDis) {
                transX = mOriPoints.get(panIndex).x + canPointMoveDis
                        - mSavePoints.get(panIndex).x;
            } else if (mSavePoints.get(panIndex).x + transX
                    < mOriPoints.get(panIndex).x - canPointMoveDis) {
                transX = mOriPoints.get(panIndex).x - canPointMoveDis
                        - mSavePoints.get(panIndex).x;
            }
            if (mSavePoints.get(panIndex).y + transY
                    > mOriPoints.get(panIndex).y + canPointMoveDis) {
                transY = mOriPoints.get(panIndex).y + canPointMoveDis
                        - mSavePoints.get(panIndex).y;
            } else if (mSavePoints.get(panIndex).y + transY
                    < mOriPoints.get(panIndex).y - canPointMoveDis) {
                transY = mOriPoints.get(panIndex).y - canPointMoveDis
                        - mSavePoints.get(panIndex).y;
            }

            if (panIndex == 0) {
                for (int i = 0; i <= mSelectedPointsDownIndex; i++) {
                    if (i == panIndex) {
                        x = mSavePoints.get(i).x + transX;
                        y = mSavePoints.get(i).y + transY;
                        PointF moveNewPoint = new PointF(x, y);
                        mDrawPoints.set(i, moveNewPoint);
                    } else {
                        float changeX = transX / ((mSelectedPointsDownIndex - panIndex)) *
                                (((mSelectedPointsDownIndex - panIndex)) - (i - panIndex));
                        float changeY = transY / ((mSelectedPointsDownIndex - panIndex)) *
                                (((mSelectedPointsDownIndex - panIndex)) - (i - panIndex));
                        x = mSavePoints.get(i).x + changeX;
                        y = mSavePoints.get(i).y + changeY;
                        PointF moveNewPoint = new PointF(x, y);
                        mDrawPoints.set(i, moveNewPoint);
                    }
                    mScaleDrawPoints.set(i, new PointF((mDrawPoints.get(i).x) * imageScale + getAttacher().getDisplayRect().left,
                            (mDrawPoints.get(i).y) * imageScale + getAttacher().getDisplayRect().top));
                }
                for (int i = mSelectedPointsUpIndex; i < pointsSize; i++) {
                    float changeX = transX / ((pointsSize - 1 - mSelectedPointsUpIndex)) *
                            (((pointsSize - 1 - mSelectedPointsUpIndex)) - (pointsSize - 1 - i));
                    float changeY = transY / ((pointsSize - 1 - mSelectedPointsUpIndex)) *
                            (((pointsSize - 1 - mSelectedPointsUpIndex)) - (pointsSize - 1 - i));
                    x = mSavePoints.get(i).x + changeX;
                    y = mSavePoints.get(i).y + changeY;
                    PointF moveNewPoint = new PointF(x, y);
                    mDrawPoints.set(i, moveNewPoint);
                    mScaleDrawPoints.set(i, new PointF((mDrawPoints.get(i).x) * imageScale + getAttacher().getDisplayRect().left,
                            (mDrawPoints.get(i).y) * imageScale + getAttacher().getDisplayRect().top));
                }
            } else {
                for (int i = mSelectedPointsDownIndex; i <= mSelectedPointsUpIndex; i++) {
                    if (i < panIndex) {
                        float changeX = transX / ((panIndex - mSelectedPointsDownIndex)) *
                                (((panIndex - mSelectedPointsDownIndex)) - (panIndex - i));
                        float changeY = transY / ((panIndex - mSelectedPointsDownIndex)) *
                                (((panIndex - mSelectedPointsDownIndex)) - (panIndex - i));
                        x = mSavePoints.get(i).x + changeX;
                        y = mSavePoints.get(i).y + changeY;
                        PointF moveNewPoint = new PointF(x, y);
                        mDrawPoints.set(i, moveNewPoint);
                    } else if (i == panIndex) {
                        x = mSavePoints.get(i).x + transX;
                        y = mSavePoints.get(i).y + transY;
                        PointF moveNewPoint = new PointF(x, y);
                        mDrawPoints.set(i, moveNewPoint);
                    } else {
                        float changeX = transX / ((mSelectedPointsUpIndex - panIndex)) *
                                (((mSelectedPointsUpIndex - panIndex)) - (i - panIndex));
                        float changeY = transY / ((mSelectedPointsUpIndex - panIndex)) *
                                (((mSelectedPointsUpIndex - panIndex)) - (i - panIndex));
                        x = mSavePoints.get(i).x + changeX;
                        y = mSavePoints.get(i).y + changeY;
                        PointF moveNewPoint = new PointF(x, y);
                        mDrawPoints.set(i, moveNewPoint);
                    }
                    mScaleDrawPoints.set(i, new PointF((mDrawPoints.get(i).x) * imageScale + getAttacher().getDisplayRect().left,
                            (mDrawPoints.get(i).y) * imageScale + getAttacher().getDisplayRect().top));
                }
            }

            for (ProfilePaintLine paintLine : drawPaintLines) {
                if (!paintLine.getLineType().equals("free")) {
                    ProfilePaintLine shuaiYinProfilePaintLine = paintLine.copy();
                    Pixel paintPointStart = new Pixel();
                    paintPointStart.setX(mDrawPoints.get(paintLine.getLocation()).x * imageScale + getAttacher().getDisplayRect().left);
                    paintPointStart.setY(mDrawPoints.get(paintLine.getLocation()).y * imageScale + getAttacher().getDisplayRect().top);
                    shuaiYinProfilePaintLine.setStartPt(paintPointStart);

                    Pixel paintPointEnd = new Pixel();
                    paintPointEnd.setX(mDrawPoints.get(paintLine.getLocation()).x * imageScale + getAttacher().getDisplayRect().left);
                    paintPointEnd.setY(mDrawPoints.get(paintLine.getLocation()).y * imageScale + getAttacher().getDisplayRect().top);
                    shuaiYinProfilePaintLine.setEndPt(paintPointEnd);
                    scalePaintLines.set(getPaintLineIndex(drawPaintLines, paintLine), shuaiYinProfilePaintLine);

                    shuaiYinProfilePaintLine = paintLine.copy();
                    paintPointStart = new Pixel();
                    paintPointStart.setX(mDrawPoints.get(paintLine.getLocation()).x);
                    paintPointStart.setY(mDrawPoints.get(paintLine.getLocation()).y);
                    shuaiYinProfilePaintLine.setStartPt(paintPointStart);

                    paintPointEnd = new Pixel();
                    paintPointEnd.setX(mDrawPoints.get(paintLine.getLocation()).x);
                    paintPointEnd.setY(mDrawPoints.get(paintLine.getLocation()).y);
                    shuaiYinProfilePaintLine.setEndPt(paintPointEnd);
                    drawPaintLines.set(getPaintLineIndex(drawPaintLines, paintLine), shuaiYinProfilePaintLine);
                }
            }
            setMoveRectObjects();
        }
    }

    public List<ProfilePaintLine> resetPaintLines(float transX, float transY) {
        if (getAttacher().getDisplayRect() != null) {
            List<ProfilePaintLine> lines = new ArrayList<>();
            float imageScale = getAttacher().getDisplayRect().height() / oriImageHeight;
            Pixel selectPt = new Pixel();
            if (selectedFreeLineObject.isStartSelected()) {
                selectPt = selectPaintLine.getStartPt();
            } else if (selectedFreeLineObject.isEndSelected()) {
                selectPt = selectPaintLine.getEndPt();
            }
            Pixel oriPt = new Pixel();
            for (ProfilePaintLine paintLine : oriPaintLines) {
                if (paintLine.getPaintLineId() == selectedFreeLineObject.getPaintLineId()) {
                    if (selectedFreeLineObject.isStartSelected()) {
                        oriPt = paintLine.getStartPt();
                    } else if (selectedFreeLineObject.isEndSelected()) {
                        oriPt = paintLine.getEndPt();
                    }
                    break;
                }
            }
            //限制可移动的范围
            if (selectPt.getX() + transX
                    > oriPt.getX() + canPointMoveDis) {
                transX = oriPt.getX() + canPointMoveDis
                        - selectPt.getX();
            } else if (selectPt.getX() + transX
                    < oriPt.getX() - canPointMoveDis) {
                transX = oriPt.getX() - canPointMoveDis
                        - selectPt.getX();
            }
            if (selectPt.getY() + transY
                    > oriPt.getY() + canPointMoveDis) {
                transY = oriPt.getY() + canPointMoveDis
                        - selectPt.getY();
            } else if (selectPt.getY() + transY
                    < oriPt.getY() - canPointMoveDis) {
                transY = oriPt.getY() - canPointMoveDis
                        - selectPt.getY();
            }

            ProfilePaintLine selectPaintLineCopy = selectPaintLine.copy();
            if (selectedFreeLineObject.isStartSelected()) {
                selectPaintLineCopy.setStartPt(new Pixel(selectPt.getX() + transX, selectPt.getY() + transY));
            } else if (selectedFreeLineObject.isEndSelected()) {
                selectPaintLineCopy.setEndPt(new Pixel(selectPt.getX() + transX, selectPt.getY() + transY));
            }
            lines.add(selectPaintLineCopy);
            for (ProfilePaintLine paintLine : drawPaintLines) {
                if (paintLine.getPaintLineId() == selectPaintLineCopy.getPaintLineId()) {
                    paintLine.setStartPt(selectPaintLineCopy.getStartPt());
                    paintLine.setEndPt(selectPaintLineCopy.getEndPt());
                    break;
                }
            }
            for (ProfilePaintLine paintLine : scalePaintLines) {
                if (paintLine.getPaintLineId() == selectPaintLineCopy.getPaintLineId()) {
                    paintLine.setStartPt(new Pixel(selectPaintLineCopy.getStartPt().getX() * imageScale + getAttacher().getDisplayRect().left,
                            selectPaintLineCopy.getStartPt().getY() * imageScale + getAttacher().getDisplayRect().top));
                    paintLine.setEndPt(new Pixel(selectPaintLineCopy.getEndPt().getX() * imageScale + getAttacher().getDisplayRect().left,
                            selectPaintLineCopy.getEndPt().getY() * imageScale + getAttacher().getDisplayRect().top));
                    lines.add(paintLine);
                    break;
                }
            }
            return lines;
        }
        return null;
    }

    private int getPaintLineIndex(ArrayList<ProfilePaintLine> paintLines,
                                  ProfilePaintLine paintLine) {
        for (ProfilePaintLine pl : paintLines) {
            if (pl.getPaintLineId() == paintLine.getPaintLineId()) {
                return paintLines.indexOf(pl);
            }
        }
        return -1;
    }

    public void saveEditPointsTmp() {
        mSavePoints.clear();
        mSavePoints.addAll(mDrawPoints);
    }

    public void saveEditPaintLineTmp() {
        savePaintLines.clear();
        for (ProfilePaintLine paintLine : drawPaintLines) {
            savePaintLines.add(paintLine.copy());
        }
    }

    public ArrayList<MovePanObject> getMovePanObjects() {
        return movePanObjects;
    }

    public void clearAllExtraMoveObjects() {
        movePanObjects.clear();
        invalidate();
    }

    public ArrayList<PointF> getScalePoints() {
        return mScaleDrawPoints;
    }

    public ArrayList<PointF> getDrawPoints() {
        return mDrawPoints;
    }

    private ArrayList<Object> setLinePOnOutline(ArrayList<PointF> points, PointF p, int downIndex, int upIndex) {
        ArrayList<Object> params = new ArrayList<>();
        if (p.x != 0) {
            int index = 0;
            float minDis = 65535;
            for (int i = downIndex; i <= upIndex; i++) {
                if (Math.abs(points.get(i).y - p.y) < minDis) {
                    minDis = Math.abs(points.get(i).y - p.y);
                    index = i;
                }
            }
            params.add(index);
            params.add(points.get(index));
        }
        return params;
    }

    private int[] findNearestLimitIndex(int positionIndex) {
        int[] limits = new int[2];
        ArrayList<Integer> moveObjectIndexList = new ArrayList<>();
        for (MovePanObject object : movePanObjects) {
            if (object.getPositionIndex() != positionIndex) {
                moveObjectIndexList.add(object.getPositionIndex());
            }
        }
        moveObjectIndexList.add(0);
        moveObjectIndexList.add(mDrawPoints.size() - 1);
        Collections.sort(moveObjectIndexList, new sortIndex());
        int l = moveObjectIndexList.size();
        if (positionIndex == 0) {
            limits[0] = moveObjectIndexList.get(1);
            limits[1] = moveObjectIndexList.get(l - 2);
        } else {
            for (int i = 0; i < l - 1; i++) {
                if (positionIndex > moveObjectIndexList.get(i)
                        && positionIndex < moveObjectIndexList.get(i + 1)) {
                    limits[0] = moveObjectIndexList.get(i);
                    limits[1] = moveObjectIndexList.get(i + 1);
                }
            }
        }
        return limits;
    }

    /**
     * 设置除当前选中的pan之外select全为false
     */
    private void resetMoveState() {
        for (MovePanObject object : movePanObjects) {
            object.setSelected(false);
        }
        for (MoveRectObject object : moveRectObjects) {
            object.setSelected(false);
        }
        for (MoveFreeLineObject object : moveFreeLineObjects) {
            object.setStartSelected(false);
            object.setEndSelected(false);
        }
        invalidate();
    }

    /**
     * 撤销
     */
    public void undo() {
        if (undoStack.size() != 1) {
            undoStack.pop();
            Step step = undoStack.peek();
            mDrawPoints.clear();
            mDrawPoints.addAll(step.getDrawPoints());
            saveEditPointsTmp();
            movePanObjects.clear();
            movePanObjects.addAll(step.getMovePans());
            selectedMovePanObject = getSelectPan();
            drawPaintLines.clear();
            drawPaintLines.addAll(step.getDrawPaintLines());
            moveRectObjects.clear();
            moveRectObjects.addAll(step.getMoveRectObjects());
            moveFreeLineObjects.clear();
            moveFreeLineObjects.addAll(step.getMoveFreeLineObjects());
            saveEditPaintLineTmp();
            currentMode = step.getCurrentmode();
            calcuScaleDrawPoints();
            calcuScalePans();
            invalidate();
            if (onEditListener != null) {
                onEditListener.onEdit(currentMode, undoStack.size() != 1);
            }
        }
    }

    public Stack<Step> getUndoStack() {
        return undoStack;
    }

    public void setUndoStack(Stack<Step> undoStack) {
        this.undoStack = undoStack;
    }

    public MovePanObject getSelectPan() {
        for (MovePanObject object : movePanObjects) {
            if (object.isSelected()) {
                return object;
            }
        }
        return null;
    }

    @Override
    public void onDetachedFromWindow() {
        if (magnifierView != null) {
            magnifierView.recycleBitmap();
        }
        if (mVibrator != null) {
            mVibrator.cancel();
        }
        if (faceRectObject != null) {
            faceRectObject.recycle();
        }
        /**
         * 清除图片缓存
         */
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        if (frontImageUri != null) {
            imagePipeline.evictFromCache(frontImageUri);
        }
        if (sideImageUri != null) {
            imagePipeline.evictFromCache(sideImageUri);
        }
        super.onDetachedFromWindow();
    }

    private class sortIndex implements Comparator<Integer> {
        /**
         * @param lhs
         * @param rhs
         * @return an integer < 0 if lhs is less than rhs, 0 if they are
         * equal, and > 0 if lhs is greater than rhs,比较数据大小时
         */
        @Override
        public int compare(Integer lhs, Integer rhs) {
            // 从小到大排序
            if (lhs > rhs) {
                return 1;
            }
            return -1;
        }
    }

    private OnEditListener onEditListener;

    public void setOnEditListener(OnEditListener onEditListener) {
        this.onEditListener = onEditListener;
    }

    public void setOutlineGoodColor(@ColorInt int outlineGoodColor) {
        this.outlineGoodColor = outlineGoodColor;
    }

    public void setOutlineBadColor(@ColorInt int outlineBadColor) {
        this.outlineBadColor = outlineBadColor;
    }

    public void setMovePanColor(@ColorInt int movePanColor) {
        this.movePanColor = movePanColor;
    }

    public void setMoveRectSrc(Drawable moveRectSrc) {
        this.moveRectSrc = moveRectSrc;
    }

    public void setFaceCoverSrc(@DrawableRes int faceCoverRes) {
        this.faceCoverRes = faceCoverRes;
        if (flag == FRONT) {
            if (coverFace) {
                if (faceCoverRes == 0) {
                    faceCoverRes = R.drawable.ic_cover_face;
                }
                faceRectObject = new FaceRectObject(mContext, profile2ModelData.getFrontFaceRect(), faceCoverRes);
            }
        } else {
            if (coverFace) {
                if (faceCoverRes == 0) {
                    faceCoverRes = R.drawable.ic_cover_face;
                }
                faceRectObject = new FaceRectObject(mContext, profile2ModelData.getSideFaceRect(), faceCoverRes);
            }
        }
    }

    public Profile2ModelData getSavedProfile2ModelData() {
        if (flag == FRONT) {
            profile2ModelData.setFrontAllPoints(mSavePoints);
            ProfilePaintLine[] paintLines = new ProfilePaintLine[savePaintLines.size()];
            savePaintLines.toArray(paintLines);
            profile2ModelData.setFrontPaintLines(paintLines);
        } else {
            profile2ModelData.setSideAllPoints(mSavePoints);
            ProfilePaintLine[] paintLines = new ProfilePaintLine[savePaintLines.size()];
            savePaintLines.toArray(paintLines);
            profile2ModelData.setSidePaintLines(paintLines);
        }
        return profile2ModelData;
    }
}
