package com.tozmart.tozisdk.entity;

/**
 * Created by Wayne on 2018/2/25.
 */

public class ProfileConfigBodyBean {
    private PaintMovPx[] mptsSelected;
    private ProfilePaintLine[] linesSelected;

    public PaintMovPx[] getMptsSelected() {
        return mptsSelected;
    }

    public void setMptsSelected(PaintMovPx[] mptsSelected) {
        this.mptsSelected = mptsSelected;
    }

    public ProfilePaintLine[] getLinesSelected() {
        return linesSelected;
    }

    public void setLinesSelected(ProfilePaintLine[] linesSelected) {
        this.linesSelected = linesSelected;
    }
}
