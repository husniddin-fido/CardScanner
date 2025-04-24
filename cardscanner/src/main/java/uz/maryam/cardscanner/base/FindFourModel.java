package uz.maryam.cardscanner.base;

import android.content.Context;

import java.io.IOException;
import java.nio.MappedByteBuffer;

/**
 * This classifier works with the float MobileNet model.
 */
class FindFourModel extends ImageClassifier {

    final int rows = 34;
    final int cols = 51;
    final CGSize boxSize = new CGSize(80, 36);
    final CGSize cardSize = new CGSize(480, 302);
    private final int classes = 3;
    private final int digitClass = 1;
    private final int expiryClass = 2;

    private final float[][][][] labelProbArray;

    FindFourModel(Context context) throws IOException {
        super(context);
        labelProbArray = new float[1][rows][cols][classes];
    }

    boolean hasDigits(int row, int col) {
        return this.digitConfidence(row, col) >= 0.5;
    }

    boolean hasExpiry(int row, int col) {
        return this.expiryConfidence(row, col) >= 0.5;
    }

    float digitConfidence(int row, int col) {
        return labelProbArray[0][row][col][digitClass];
    }

    float expiryConfidence(int row, int col) {
        return labelProbArray[0][row][col][expiryClass];
    }

    @Override
    protected MappedByteBuffer loadModelFile(Context context) throws IOException {
        return ModelFactory.getSharedInstance().loadFindFourFile(context);
    }

    @Override
    protected int getImageSizeX() {
        return 480;
    }

    @Override
    protected int getImageSizeY() {
        return 302;
    }

    @Override
    protected int getNumBytesPerChannel() {
        return 4; // Float.SIZE / Byte.SIZE;
    }

    @Override
    protected void addPixelValue(int pixelValue) {
        imgData.putFloat(((pixelValue >> 16) & 0xFF) / 255.f);
        imgData.putFloat(((pixelValue >> 8) & 0xFF) / 255.f);
        imgData.putFloat((pixelValue & 0xFF) / 255.f);
    }

    @Override
    protected void runInference() {
        tflite.run(imgData, labelProbArray);
    }
}
