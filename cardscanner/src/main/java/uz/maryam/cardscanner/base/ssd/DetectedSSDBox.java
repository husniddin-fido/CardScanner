package uz.maryam.cardscanner.base.ssd;

import android.graphics.RectF;

public class DetectedSSDBox {
    public final float confidence;
    public final int label;
    public final RectF rect;
    final float XMin;
    final float YMin;
    final float XMax;
    final float YMax;
    final int imageWidth;
    final int imageHeight;


    public DetectedSSDBox(float XMin, float YMin, float XMax, float YMax, float confidence, int imageWidth, int imageHeight, int label) {
        this.XMin = XMin * imageWidth;
        this.XMax = XMax * imageWidth;
        this.YMin = YMin * imageHeight;
        this.YMax = YMax * imageHeight;
        this.confidence = confidence;
        this.label = label;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.rect = new RectF(this.XMin, this.YMin, this.XMax, this.YMax);
    }

}

