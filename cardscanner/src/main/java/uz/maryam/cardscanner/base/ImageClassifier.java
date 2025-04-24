package uz.maryam.cardscanner.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;

abstract public class ImageClassifier {

    private static final String TAG = "CardScan";

    private static final int DIM_BATCH_SIZE = 1;

    private static final int DIM_PIXEL_SIZE = 3;

    private final int[] intValues = new int[getImageSizeX() * getImageSizeY()];

    private final Interpreter.Options tfliteOptions = new Interpreter.Options();

    protected Interpreter tflite;

    protected ByteBuffer imgData = null;

    private MappedByteBuffer tfliteModel;

    protected ImageClassifier(Context context) throws IOException {
        init(context);
    }

    protected ImageClassifier() {
        // don't do anything, but make sure that you call init later
    }

    protected void init(Context context) throws IOException {
        tfliteModel = loadModelFile(context);
        tflite = new Interpreter(tfliteModel, tfliteOptions);
        imgData =
                ByteBuffer.allocateDirect(
                        DIM_BATCH_SIZE
                                * getImageSizeX()
                                * getImageSizeY()
                                * DIM_PIXEL_SIZE
                                * getNumBytesPerChannel());
        imgData.order(ByteOrder.nativeOrder());
    }

    protected void classifyFrame(Bitmap bitmap) {
        if (tflite == null) {
            Log.e(TAG, "Image classifier has not been initialized; Skipped.");
        }
        convertBitmapToByteBuffer(bitmap);
        runInference();
    }

    private void recreateInterpreter() {
        if (tflite != null) {
            tflite.close();
            tflite = new Interpreter(tfliteModel, tfliteOptions);
        }
    }

    public void useNNAPI() {
        tfliteOptions.setUseNNAPI(true);
        recreateInterpreter();
    }

    public void setNumThreads(int numThreads) {
        tfliteOptions.setNumThreads(numThreads);
        recreateInterpreter();
    }

    abstract protected MappedByteBuffer loadModelFile(Context context) throws IOException;

    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (imgData == null) {
            return;
        }
        imgData.rewind();

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, getImageSizeX(), getImageSizeY(), false);
        resizedBitmap.getPixels(intValues, 0, resizedBitmap.getWidth(), 0, 0,
                resizedBitmap.getWidth(), resizedBitmap.getHeight());
        // Convert the image to floating point.
        int pixel = 0;
        for (int i = 0; i < getImageSizeX(); ++i) {
            for (int j = 0; j < getImageSizeY(); ++j) {
                final int val = intValues[pixel++];
                addPixelValue(val);
            }
        }
    }

    protected abstract int getImageSizeX();

    protected abstract int getImageSizeY();

    protected abstract int getNumBytesPerChannel();

    protected abstract void addPixelValue(int pixelValue);

    protected abstract void runInference();
}