package com.tozmart.tozisdk.entity;

/**
 * Created by tracy on 2018/2/25.
 */

public class ImageProc {
    private int Height;
    private int Width;
    private int[] HumanRect;
    private int[] FaceRect;
    private int Horizonal_INV;
    private int Verizonal_INV;

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public int getWidth() {
        return Width;
    }

    public void setWidth(int width) {
        Width = width;
    }

    public int[] getHumanRect() {
        return HumanRect;
    }

    public void setHumanRect(int[] humanRect) {
        HumanRect = humanRect;
    }

    public int[] getFaceRect() {
        return FaceRect;
    }

    public void setFaceRect(int[] faceRect) {
        FaceRect = faceRect;
    }

    public int getHorizonal_INV() {
        return Horizonal_INV;
    }

    public void setHorizonal_INV(int horizonal_INV) {
        Horizonal_INV = horizonal_INV;
    }

    public int getVerizonal_INV() {
        return Verizonal_INV;
    }

    public void setVerizonal_INV(int verizonal_INV) {
        Verizonal_INV = verizonal_INV;
    }
}
