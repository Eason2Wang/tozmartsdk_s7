package com.tozmart.tozisdk.view.editprofileview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import com.tozmart.tozisdk.R;
import com.tozmart.tozisdk.utils.ImageUtils;

public class FaceRectObject {

    private RectF faceRect;
    private int mIconRes;
    private Drawable mIcon;
    private Bitmap blurFace;
    private Rect srcRect;
    private RectF destRect;

    public FaceRectObject(RectF faceRect, String imageCacheUrl) {
        this.faceRect = faceRect;
        Bitmap bitmap = ImageUtils.getBitmap(imageCacheUrl);

        if (bitmap == null) {
            return;
        }
        blurFace = Bitmap.createBitmap(bitmap, (int)faceRect.left, (int)faceRect.top, (int)faceRect.width(), (int)faceRect.height());
        bitmap.recycle();
        blurFace = ImageUtils.fastBlur(blurFace, 1, 8);
        srcRect = new Rect(0, 0, blurFace.getWidth(), blurFace.getHeight());
    }

    public FaceRectObject(Context context, RectF faceRect, @DrawableRes int faceCover) {
        this.faceRect = faceRect;
        mIconRes = faceCover;
        mIcon = ContextCompat.getDrawable(context, faceCover);
    }

    public void drawRect(Canvas canvas) {
        if (mIcon != null) {
            mIcon.draw(canvas);
        } else if (blurFace != null && srcRect != null && destRect != null){
            canvas.drawBitmap(blurFace, srcRect, destRect, null);
        }
    }

    public void setAttributes(float scale, float offsetX, float offsetY){
        if (mIcon != null) {
            mIcon.setBounds((int) (faceRect.left * scale + offsetX), (int) (faceRect.top * scale + offsetY),
                    (int) (faceRect.right * scale + offsetX), (int) (faceRect.bottom * scale + offsetY));
        } else if(blurFace != null) {
            destRect = new RectF(faceRect.left * scale + offsetX, faceRect.top * scale + offsetY,
                    faceRect.right * scale + offsetX, faceRect.bottom * scale + offsetY);
        }
    }

    public RectF getFaceRect() {
        return faceRect;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public int getIconRes() {
        return mIconRes == 0 ? R.drawable.ic_cover_face : mIconRes;
    }

    public void recycle() {
        if (blurFace != null && !blurFace.isRecycled()) {
            blurFace.recycle();
        }
    }
}
