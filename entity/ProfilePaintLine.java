package com.tozmart.tozisdk.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tracy on 18/1/5.
 */

public class ProfilePaintLine implements Parcelable {
    private String LineName;// 所画线的名字
    private String ImgDir;// 所画线的在的图片，f表示前， s 表示侧面， b 表示背面
    private String LineType;// 所画线的类型  L 表示起点在左边， R表示起点在右边， free表示两端点是free的状态
    private Pixel StartPt;// 所画线起点坐标
    private Pixel EndPt;// 所画线终点坐标
    private int Location;
    private int DnRange;// 所画线在身上轮廓的 下标限制，不能超过全身点索引的下标， free 型没有限制
    private int UpRange;// 所画线在身上轮廓的 上标限制，不能超过全身点索引的下标， free 型没有限制
    private int paintLineId;

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public String getImgDir() {
        return ImgDir;
    }

    public void setImgDir(String imgDir) {
        ImgDir = imgDir;
    }

    public String getLineType() {
        return LineType;
    }

    public void setLineType(String lineType) {
        LineType = lineType;
    }

    public Pixel getStartPt() {
        return StartPt;
    }

    public void setStartPt(Pixel startPt) {
        StartPt = startPt;
    }

    public Pixel getEndPt() {
        return EndPt;
    }

    public void setEndPt(Pixel endPt) {
        EndPt = endPt;
    }

    public int getDnRange() {
        return DnRange;
    }

    public void setDnRange(int dnRange) {
        DnRange = dnRange;
    }

    public int getUpRange() {
        return UpRange;
    }

    public void setUpRange(int upRange) {
        UpRange = upRange;
    }

    public int getLocation() {
        return Location;
    }

    public void setLocation(int location) {
        Location = location;
    }

    public int getPaintLineId() {
        return paintLineId;
    }

    public void setPaintLineId(int paintLineId) {
        this.paintLineId = paintLineId;
    }

    public ProfilePaintLine copy(){
        ProfilePaintLine profilePaintLine = new ProfilePaintLine();
        profilePaintLine.setLineName(LineName);
        profilePaintLine.setEndPt(EndPt);
        profilePaintLine.setStartPt(StartPt);
        profilePaintLine.setImgDir(ImgDir);
        profilePaintLine.setLineType(LineType);
        profilePaintLine.setLocation(Location);
        profilePaintLine.setDnRange(DnRange);
        profilePaintLine.setUpRange(UpRange);
        profilePaintLine.setPaintLineId(paintLineId);
        return profilePaintLine;
    }

    public ProfilePaintLine() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.LineName);
        dest.writeString(this.ImgDir);
        dest.writeString(this.LineType);
        dest.writeParcelable(this.StartPt, flags);
        dest.writeParcelable(this.EndPt, flags);
        dest.writeInt(this.Location);
        dest.writeInt(this.DnRange);
        dest.writeInt(this.UpRange);
        dest.writeInt(this.paintLineId);
    }

    protected ProfilePaintLine(Parcel in) {
        this.LineName = in.readString();
        this.ImgDir = in.readString();
        this.LineType = in.readString();
        this.StartPt = in.readParcelable(Pixel.class.getClassLoader());
        this.EndPt = in.readParcelable(Pixel.class.getClassLoader());
        this.Location = in.readInt();
        this.DnRange = in.readInt();
        this.UpRange = in.readInt();
        this.paintLineId = in.readInt();
    }

    public static final Creator<ProfilePaintLine> CREATOR = new Creator<ProfilePaintLine>() {
        @Override
        public ProfilePaintLine createFromParcel(Parcel source) {
            return new ProfilePaintLine(source);
        }

        @Override
        public ProfilePaintLine[] newArray(int size) {
            return new ProfilePaintLine[size];
        }
    };
}
