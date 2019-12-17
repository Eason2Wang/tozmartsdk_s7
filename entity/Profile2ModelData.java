package com.tozmart.tozisdk.entity;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tracy on 17/12/7.
 */

public class Profile2ModelData implements Parcelable {
    private int id;// return from profile api
    private ProfilePaintLine[] frontPaintLines;
    private ProfilePaintLine[] sidePaintLines;

    private ProfilePaintLine[] frontPaintLinesSelected;
    private ProfilePaintLine[] sidePaintLinesSelected;

    private PaintColor[] fLooseIdx;
    private PaintColor[] sLooseIdx;

    private RectF frontFaceRect = new RectF();
    private RectF sideFaceRect = new RectF();

    private ArrayList<PointF> frontAllPoints = new ArrayList<>();
    private ArrayList<PointF> sideAllPoints = new ArrayList<>();

    private int frontSpecialMoveIndex;// 特殊的移动点下标
    private int sideSpecialMoveIndex;// 特殊的移动点下标
    private PaintMovPx[] frontMoveIndex;
    private PaintMovPx[] sideMoveIndex;
    private PaintMovPx[] frontMoveIndexSelected;
    private PaintMovPx[] sideMoveIndexSelected;
    private List<Integer> frontStickIndex = new ArrayList<>();
    private List<Integer> sideStickIndex = new ArrayList<>();

    // 服务器返回的图片
    private Bitmap frontProcessedBitmap;
    private Bitmap sideProcessedBitmap;

    private ImageProcessFeedback imageProcessFeedback;

    private String measureId;
    private String frontCAddInfo;
    private String sideCAddInfo;
    private ProfileSizeLine[] frontCSizeLines;
    private ProfileSizeLine[] sideCSizeLines;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PaintColor[] getfLooseIdx() {
        return fLooseIdx;
    }

    public void setfLooseIdx(PaintColor[] fLooseIdx) {
        this.fLooseIdx = fLooseIdx;
    }

    public PaintColor[] getsLooseIdx() {
        return sLooseIdx;
    }

    public void setsLooseIdx(PaintColor[] sLooseIdx) {
        this.sLooseIdx = sLooseIdx;
    }

    public ProfilePaintLine[] getFrontPaintLines() {
        return frontPaintLines;
    }

    public void setFrontPaintLines(ProfilePaintLine[] frontPaintLines) {
        this.frontPaintLines = frontPaintLines;
    }

    public ProfilePaintLine[] getSidePaintLines() {
        return sidePaintLines;
    }

    public void setSidePaintLines(ProfilePaintLine[] sidePaintLines) {
        this.sidePaintLines = sidePaintLines;
    }

    public RectF getFrontFaceRect() {
        return frontFaceRect;
    }

    public void setFrontFaceRect(RectF frontFaceRect) {
        this.frontFaceRect = frontFaceRect;
    }

    public RectF getSideFaceRect() {
        return sideFaceRect;
    }

    public void setSideFaceRect(RectF sideFaceRect) {
        this.sideFaceRect = sideFaceRect;
    }

    public ArrayList<PointF> getFrontAllPoints() {
        return frontAllPoints;
    }

    public void setFrontAllPoints(ArrayList<PointF> frontAllPoints) {
        this.frontAllPoints.clear();
        this.frontAllPoints.addAll(frontAllPoints);
    }

    public ArrayList<PointF> getSideAllPoints() {
        return sideAllPoints;
    }

    public void setSideAllPoints(ArrayList<PointF> sideAllPoints) {
        this.sideAllPoints.clear();
        this.sideAllPoints.addAll(sideAllPoints);
    }

    public int getFrontSpecialMoveIndex() {
        return frontSpecialMoveIndex;
    }

    public void setFrontSpecialMoveIndex(int frontSpecialMoveIndex) {
        this.frontSpecialMoveIndex = frontSpecialMoveIndex;
    }

    public int getSideSpecialMoveIndex() {
        return sideSpecialMoveIndex;
    }

    public void setSideSpecialMoveIndex(int sideSpecialMoveIndex) {
        this.sideSpecialMoveIndex = sideSpecialMoveIndex;
    }

    public PaintMovPx[] getFrontMoveIndex() {
        return frontMoveIndex;
    }

    public void setFrontMoveIndex(PaintMovPx[] frontMoveIndex) {
        this.frontMoveIndex = frontMoveIndex;
    }

    public PaintMovPx[] getSideMoveIndex() {
        return sideMoveIndex;
    }

    public void setSideMoveIndex(PaintMovPx[] sideMoveIndex) {
        this.sideMoveIndex = sideMoveIndex;
    }

    public List<Integer> getFrontStickIndex() {
        return frontStickIndex;
    }

    public void setFrontStickIndex(List<Integer> frontStickIndex) {
        this.frontStickIndex = frontStickIndex;
    }

    public List<Integer> getSideStickIndex() {
        return sideStickIndex;
    }

    public void setSideStickIndex(List<Integer> sideStickIndex) {
        this.sideStickIndex = sideStickIndex;
    }

    public Bitmap getFrontProcessedBitmap() {
        return frontProcessedBitmap;
    }

    public void setFrontProcessedBitmap(Bitmap frontProcessedBitmap) {
        this.frontProcessedBitmap = frontProcessedBitmap;
    }

    public Bitmap getSideProcessedBitmap() {
        return sideProcessedBitmap;
    }

    public void setSideProcessedBitmap(Bitmap sideProcessedBitmap) {
        this.sideProcessedBitmap = sideProcessedBitmap;
    }

    public ImageProcessFeedback getImageProcessFeedback() {
        return imageProcessFeedback;
    }

    public void setImageProcessFeedback(ImageProcessFeedback imageProcessFeedback) {
        this.imageProcessFeedback = imageProcessFeedback;
    }

    public String getMeasureId() {
        return measureId;
    }

    public void setMeasureId(String measureId) {
        this.measureId = measureId;
    }

    public String getFrontCAddInfo() {
        return frontCAddInfo;
    }

    public void setFrontCAddInfo(String frontCAddInfo) {
        this.frontCAddInfo = frontCAddInfo;
    }

    public String getSideCAddInfo() {
        return sideCAddInfo;
    }

    public void setSideCAddInfo(String sideCAddInfo) {
        this.sideCAddInfo = sideCAddInfo;
    }

    public ProfileSizeLine[] getFrontCSizeLines() {
        return frontCSizeLines;
    }

    public void setFrontCSizeLines(ProfileSizeLine[] frontCSizeLines) {
        this.frontCSizeLines = frontCSizeLines;
    }

    public ProfileSizeLine[] getSideCSizeLines() {
        return sideCSizeLines;
    }

    public void setSideCSizeLines(ProfileSizeLine[] sideCSizeLines) {
        this.sideCSizeLines = sideCSizeLines;
    }

    public ProfilePaintLine[] getFrontPaintLinesSelected() {
        return frontPaintLinesSelected;
    }

    public void setFrontPaintLinesSelected(ProfilePaintLine[] frontPaintLinesSelected) {
        this.frontPaintLinesSelected = frontPaintLinesSelected;
    }

    public ProfilePaintLine[] getSidePaintLinesSelected() {
        return sidePaintLinesSelected;
    }

    public void setSidePaintLinesSelected(ProfilePaintLine[] sidePaintLinesSelected) {
        this.sidePaintLinesSelected = sidePaintLinesSelected;
    }

    public PaintMovPx[] getFrontMoveIndexSelected() {
        return frontMoveIndexSelected;
    }

    public void setFrontMoveIndexSelected(PaintMovPx[] frontMoveIndexSelected) {
        this.frontMoveIndexSelected = frontMoveIndexSelected;
    }

    public PaintMovPx[] getSideMoveIndexSelected() {
        return sideMoveIndexSelected;
    }

    public void setSideMoveIndexSelected(PaintMovPx[] sideMoveIndexSelected) {
        this.sideMoveIndexSelected = sideMoveIndexSelected;
    }

    public Profile2ModelData() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeTypedArray(this.frontPaintLines, flags);
        dest.writeTypedArray(this.sidePaintLines, flags);
        dest.writeTypedArray(this.fLooseIdx, flags);
        dest.writeTypedArray(this.sLooseIdx, flags);
        dest.writeParcelable(this.frontFaceRect, flags);
        dest.writeParcelable(this.sideFaceRect, flags);
        dest.writeTypedList(this.frontAllPoints);
        dest.writeTypedList(this.sideAllPoints);
        dest.writeInt(this.frontSpecialMoveIndex);
        dest.writeInt(this.sideSpecialMoveIndex);
        dest.writeTypedArray(this.frontMoveIndex, flags);
        dest.writeTypedArray(this.sideMoveIndex, flags);
        dest.writeList(this.frontStickIndex);
        dest.writeList(this.sideStickIndex);
        dest.writeParcelable(this.imageProcessFeedback, flags);
        dest.writeString(this.measureId);
        dest.writeString(this.frontCAddInfo);
        dest.writeString(this.sideCAddInfo);
        dest.writeTypedArray(this.frontCSizeLines, flags);
        dest.writeTypedArray(this.sideCSizeLines, flags);
        dest.writeTypedArray(this.frontPaintLinesSelected, flags);
        dest.writeTypedArray(this.sidePaintLinesSelected, flags);
        dest.writeTypedArray(this.frontMoveIndexSelected, flags);
        dest.writeTypedArray(this.sideMoveIndexSelected, flags);
    }

    protected Profile2ModelData(Parcel in) {
        this.id = in.readInt();
        this.frontPaintLines = in.createTypedArray(ProfilePaintLine.CREATOR);
        this.sidePaintLines = in.createTypedArray(ProfilePaintLine.CREATOR);
        this.fLooseIdx = in.createTypedArray(PaintColor.CREATOR);
        this.sLooseIdx = in.createTypedArray(PaintColor.CREATOR);
        this.frontFaceRect = in.readParcelable(RectF.class.getClassLoader());
        this.sideFaceRect = in.readParcelable(RectF.class.getClassLoader());
        this.frontAllPoints = in.createTypedArrayList(PointF.CREATOR);
        this.sideAllPoints = in.createTypedArrayList(PointF.CREATOR);
        this.frontSpecialMoveIndex = in.readInt();
        this.sideSpecialMoveIndex = in.readInt();
        this.frontMoveIndex = in.createTypedArray(PaintMovPx.CREATOR);
        this.sideMoveIndex = in.createTypedArray(PaintMovPx.CREATOR);
        this.frontStickIndex = new ArrayList<Integer>();
        in.readList(this.frontStickIndex, Integer.class.getClassLoader());
        this.sideStickIndex = new ArrayList<Integer>();
        in.readList(this.sideStickIndex, Integer.class.getClassLoader());
        this.imageProcessFeedback = in.readParcelable(ImageProcessFeedback.class.getClassLoader());
        this.measureId = in.readString();
        this.frontCAddInfo = in.readString();
        this.sideCAddInfo = in.readString();
        this.frontCSizeLines = in.createTypedArray(ProfileSizeLine.CREATOR);
        this.sideCSizeLines = in.createTypedArray(ProfileSizeLine.CREATOR);
        this.frontPaintLinesSelected = in.createTypedArray(ProfilePaintLine.CREATOR);
        this.sidePaintLinesSelected = in.createTypedArray(ProfilePaintLine.CREATOR);
        this.frontMoveIndexSelected = in.createTypedArray(PaintMovPx.CREATOR);
        this.sideMoveIndexSelected = in.createTypedArray(PaintMovPx.CREATOR);
    }

    public static final Creator<Profile2ModelData> CREATOR = new Creator<Profile2ModelData>() {
        @Override
        public Profile2ModelData createFromParcel(Parcel source) {
            return new Profile2ModelData(source);
        }

        @Override
        public Profile2ModelData[] newArray(int size) {
            return new Profile2ModelData[size];
        }
    };
}
