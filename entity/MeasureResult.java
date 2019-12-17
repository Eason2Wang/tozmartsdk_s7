package com.tozmart.tozisdk.entity;

/**
 * Created by tracy on 17/12/7.
 */

public class MeasureResult {
    private int Sidx;
    private String Name;
    private int IsShow;
    private int MType;
    private int Showidx;
    private float Measure;
    private int Eidx;

    public int getSidx() {
        return Sidx;
    }

    public void setSidx(int sidx) {
        Sidx = sidx;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getIsShow() {
        return IsShow;
    }

    public void setIsShow(int isShow) {
        IsShow = isShow;
    }

    public int getMType() {
        return MType;
    }

    public void setMType(int MType) {
        this.MType = MType;
    }

    public int getShowidx() {
        return Showidx;
    }

    public void setShowidx(int showidx) {
        Showidx = showidx;
    }

    public float getMeasure() {
        return Measure;
    }

    public void setMeasure(float measure) {
        Measure = measure;
    }

    public int getEidx() {
        return Eidx;
    }

    public void setEidx(int eidx) {
        Eidx = eidx;
    }
}
