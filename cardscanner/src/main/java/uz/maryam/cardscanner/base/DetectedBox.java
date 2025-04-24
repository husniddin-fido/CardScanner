package uz.maryam.cardscanner.base;


import androidx.annotation.NonNull;

public class DetectedBox implements Comparable {
    private final float confidence;
    final CGRect rect;
    final int row;
    final int col;

    DetectedBox(int row, int col, float confidence, int numRows, int numCols,
                CGSize boxSize, CGSize cardSize, CGSize imageSize) {

        float w = boxSize.width * imageSize.width / cardSize.width;
        float h = boxSize.height * imageSize.height / cardSize.height;
        float x = (imageSize.width - w) / ((float) (numCols - 1)) * ((float) col);
        float y = (imageSize.height - h) / ((float) (numRows - 1)) * ((float) row);
        this.rect = new CGRect(x, y, w, h);
        this.row = row;
        this.col = col;
        this.confidence = confidence;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return Float.compare(this.confidence, ((DetectedBox) o).confidence);
    }

}
