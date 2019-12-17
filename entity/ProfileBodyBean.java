package com.tozmart.tozisdk.entity;

/**
 * Created by tracy on 2018/2/25.
 */

public class ProfileBodyBean {
    private Pixel[] CallPx;
    private PaintMovPx[] CMovPxIdx;
    private PaintColor[] CLooseIdx;
    private ProfilePaintLine[] CPaintLines;
    private ProfileSizeLine[] CSizeLines;
    private String CAddInfo;

    public Pixel[] getCallPx() {
        return CallPx;
    }

    public void setCallPx(Pixel[] callPx) {
        CallPx = callPx;
    }

    public PaintMovPx[] getCMovPxIdx() {
        return CMovPxIdx;
    }

    public void setCMovPxIdx(PaintMovPx[] CMovPxIdx) {
        this.CMovPxIdx = CMovPxIdx;
    }

    public PaintColor[] getCLooseIdx() {
        return CLooseIdx;
    }

    public void setCLooseIdx(PaintColor[] CLooseIdx) {
        this.CLooseIdx = CLooseIdx;
    }

    public ProfilePaintLine[] getCPaintLines() {
        return CPaintLines;
    }

    public void setCPaintLines(ProfilePaintLine[] CPaintLines) {
        this.CPaintLines = CPaintLines;
    }

    public String getCAddInfo() {
        return CAddInfo;
    }

    public void setCAddInfo(String CAddInfo) {
        this.CAddInfo = CAddInfo;
    }

    public ProfileSizeLine[] getCSizeLines() {
        return CSizeLines;
    }

    public void setCSizeLines(ProfileSizeLine[] CSizeLines) {
        this.CSizeLines = CSizeLines;
    }
}
