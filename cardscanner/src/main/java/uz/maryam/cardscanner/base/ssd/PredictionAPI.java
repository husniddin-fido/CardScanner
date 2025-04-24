package uz.maryam.cardscanner.base.ssd;

import java.util.ArrayList;

public class PredictionAPI {
    ArrayList<Float> pickedBoxProbs;
    ArrayList<Integer> pickedLabels;
    ArrayList<float[]> pickedBoxes;

    public Result predictionAPI(float[][] k_scores, float[][] k_boxes, float probThreshold, float iouThreshold,
                                int candidateSize, int topK) {

        pickedBoxProbs = new ArrayList<Float>();
        pickedLabels = new ArrayList<Integer>();
        pickedBoxes = new ArrayList<float[]>();

        ArrayList<Float> probs;
        ArrayList<float[]> subsetBoxes;
        ArrayList<Integer> indices;

        for (int classIndex = 1; classIndex < k_scores[0].length; classIndex++) {
            probs = new ArrayList<Float>();
            subsetBoxes = new ArrayList<float[]>();

            for (int rowIndex = 0; rowIndex < k_scores.length; rowIndex++) {
                if (k_scores[rowIndex][classIndex] > probThreshold) {
                    probs.add(k_scores[rowIndex][classIndex]);
                    subsetBoxes.add(k_boxes[rowIndex]);
                }
            }
            if (probs.isEmpty()) {
                continue;
            }
            indices = NMS.hardNMS(subsetBoxes, probs, iouThreshold, topK, candidateSize);

            for (int Index : indices) {
                pickedBoxProbs.add(probs.get(Index));
                pickedBoxes.add(subsetBoxes.get(Index));
                pickedLabels.add(classIndex);
            }
        }
        Result res = new Result();
        res.floatArrayList = pickedBoxProbs;
        res.pickedBoxes = pickedBoxes;
        res.pickedLabels = pickedLabels;

        return res;

    }

}