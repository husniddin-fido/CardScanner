package uz.maryam.cardscanner.base;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

class SSDDetect extends ImageClassifier {

    static final int NUM_OF_PRIORS = 2766;
    static final int NUM_OF_PRIORS_PER_ACTIVATION = 6;
    static final int NUM_OF_CLASSES = 13;
    static final int NUM_OF_COORDINATES = 4;
    static final int NUM_LOC = NUM_OF_COORDINATES * NUM_OF_PRIORS;
    static final int NUM_CLASS = NUM_OF_CLASSES * NUM_OF_PRIORS;
    static final float PROB_THRESHOLD = 0.3f;
    static final float IOU_THRESHOLD = 0.45f;
    static final float CENTER_VARIANCE = 0.1f;
    static final float SIZE_VARIANCE = 0.2f;
    static final int CANDIDATE_SIZE = 200;
    static final int TOP_K = 10;
    static final int[] featureMapSizes = {19, 10};
    private static final float IMAGE_MEAN = 127.5f;
    private static final float IMAGE_STD = 128.5f;
    private static final int CROP_SIZE = 300;
    private final Map<Integer, Object> outputMap = new HashMap<>();
    float[][] outputLocations;
    float[][] outputClasses;
    private final File modelFile;

    public SSDDetect(Context context, File modelFile) throws IOException {
        this.modelFile = modelFile;
        init(context);
        outputLocations = new float[1][NUM_LOC];
        outputClasses = new float[1][NUM_CLASS];

        outputMap.put(0, outputClasses);
        outputMap.put(1, outputLocations);
    }


    @Override
    protected MappedByteBuffer loadModelFile(Context context) throws IOException {
        FileInputStream inputStream = new FileInputStream(modelFile);
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = 0;
        long declaredLength = modelFile.length();
        MappedByteBuffer result = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset,
                declaredLength);
        inputStream.close();
        return result;
    }

    @Override
    protected int getImageSizeX() {
        return CROP_SIZE;
    }

    @Override
    protected int getImageSizeY() {
        return CROP_SIZE;
    }

    @Override
    protected int getNumBytesPerChannel() {
        return 4;
    }

    @Override
    protected void addPixelValue(int pixelValue) {
        imgData.putFloat((((pixelValue >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
        imgData.putFloat((((pixelValue >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
        imgData.putFloat(((pixelValue & 0xFF) - IMAGE_MEAN) / IMAGE_MEAN);
    }

    @Override
    protected void runInference() {
        Object[] inputArray = {imgData};
        tflite.runForMultipleInputsOutputs(inputArray, outputMap);
    }
}
