package uz.maryam.cardscanner.base.interfaces;

import android.graphics.Bitmap;

import java.util.List;

import uz.maryam.cardscanner.base.DetectedBox;
import uz.maryam.cardscanner.base.Expiry;

public interface OnScanListener {
    void onPrediction(final String number, final Expiry expiry, final Bitmap bitmap,
                      final List<DetectedBox> digitBoxes, final DetectedBox expiryBox,
                      final Bitmap objectDetectionBitmap, final Bitmap fullScreenBitmap);

    void onFatalError();
}
