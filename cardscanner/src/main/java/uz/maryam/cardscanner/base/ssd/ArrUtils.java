package uz.maryam.cardscanner.base.ssd;

public class ArrUtils {

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public float[][] reshape(float[][] nums, int r, int c) {
        int totalElements = nums.length * nums[0].length;
        if (totalElements != r * c || totalElements % r != 0) {
            return nums;
        }
        final float[][] result = new float[r][c];
        int newR = 0;
        int newC = 0;
        for (float[] num : nums) {
            for (float v : num) {
                result[newR][newC] = v;
                newC++;
                if (newC == c) {
                    newC = 0;
                    newR++;
                }
            }
        }
        return result;
    }

    public float[][] rearrangeArray(float[][] locations, int[] featureMapSizes, int noOfPriors, int locationsPerPrior) {
        int totalLocationsForAllLayers = 0;
        for (int size : featureMapSizes) {
            totalLocationsForAllLayers = totalLocationsForAllLayers + size * size * noOfPriors * locationsPerPrior;
        }

        float[][] rearranged = new float[1][totalLocationsForAllLayers];
        int offset = 0;
        for (int steps : featureMapSizes) {
            int totalNumberOfLocationsForThisLayer = steps * steps * noOfPriors * locationsPerPrior;
            int stepsForLoop = steps - 1;
            int j;
            int i = 0;
            int step = 0;

            while (i < totalNumberOfLocationsForThisLayer) {
                while (step < steps) {
                    j = step;
                    while (j < totalNumberOfLocationsForThisLayer - stepsForLoop + step) {
                        rearranged[0][offset + i] = locations[0][offset + j];
                        i++;
                        j = j + steps;
                    }
                    step++;
                }
                offset = offset + totalNumberOfLocationsForThisLayer;
            }
        }
        return rearranged;
    }

    public float[][] convertLocationsToBoxes(float[][] locations, float[][] priors, float centerVariance, float sizeVariance) {
        float[][] boxes = new float[locations.length][locations[0].length];
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < 2; j++) {
                boxes[i][j] = locations[i][j] * centerVariance * priors[i][j + 2] + priors[i][j];
                boxes[i][j + 2] = (float) (Math.exp(locations[i][j + 2] * sizeVariance) * priors[i][j + 2]);
            }
        }
        return boxes;
    }

    public float[][] centerFormToCornerForm(float[][] locations) {
        float[][] boxes = new float[locations.length][locations[0].length];
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < 2; j++) {
                boxes[i][j] = locations[i][j] - locations[i][j + 2] / 2;
                boxes[i][j + 2] = locations[i][j] + locations[i][j + 2] / 2;
            }
        }
        return boxes;
    }

    public float[][] softmax2D(float[][] scores) {
        float[][] normalizedScores = new float[scores.length][scores[0].length];
        float rowSum;
        for (int i = 0; i < scores.length; i++) {
            rowSum = 0.0f;
            for (int j = 0; j < scores[0].length; j++) {
                rowSum = (float) (rowSum + Math.exp(scores[i][j]));
            }
            for (int j = 0; j < scores[0].length; j++) {
                normalizedScores[i][j] = (float) (Math.exp(scores[i][j]) / rowSum);
            }
        }
        return normalizedScores;
    }
}