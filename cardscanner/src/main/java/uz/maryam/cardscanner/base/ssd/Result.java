package uz.maryam.cardscanner.base.ssd;

import java.util.ArrayList;

public class Result {

    public ArrayList<Float> floatArrayList;
    public ArrayList<Integer> pickedLabels;
    public ArrayList<float[]> pickedBoxes;

    public Result() {
        this.floatArrayList = new ArrayList<Float>();
        this.pickedLabels = new ArrayList<Integer>();
        this.pickedBoxes = new ArrayList<float[]>();
    }
}
