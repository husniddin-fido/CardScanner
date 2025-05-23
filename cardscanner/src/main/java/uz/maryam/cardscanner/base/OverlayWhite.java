package uz.maryam.cardscanner.base;

import android.content.Context;
import android.util.AttributeSet;
import uz.maryam.cardscanner.R;

public class OverlayWhite extends Overlay {

    int backgroundColorId = R.color.card_scan_overlay_colored_background;
    int cornerColorId = R.color.card_scan_overlay_colored_corner_color;

    public OverlayWhite(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        cornerDp = 3;
    }

    public void setColorIds(int backgroundColorId, int cornerColorId) {
        this.backgroundColorId = backgroundColorId;
        this.cornerColorId = cornerColorId;
        postInvalidate();
    }

    @Override
    protected int getBackgroundColorId() {
        return backgroundColorId;
    }

    @Override
    protected int getCornerColorId() {
        return cornerColorId;
    }
}
