package uz.maryam.cardscanner.base.interfaces;

import android.graphics.Bitmap;

import java.util.List;

import uz.maryam.cardscanner.base.ssd.DetectedSSDBox;


public interface OnObjectListener {
    void onPrediction(final Bitmap bitmap, List<DetectedSSDBox> boxes, int imageWidth, int imageHeight, final Bitmap fullScreenBitmap);

    void onObjectFatalError();
}
