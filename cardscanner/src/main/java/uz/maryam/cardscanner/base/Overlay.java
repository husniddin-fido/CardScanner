package uz.maryam.cardscanner.base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import uz.maryam.cardscanner.R;


class Overlay extends View {

    private final RectF oval = new RectF();
    private final Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    int cornerDp = 6;
    boolean drawCorners = true;
    private RectF rect;
    private int radius;

    public Overlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    protected int getBackgroundColorId() {
        return R.color.card_scan_camera_background;
    }

    protected int getCornerColorId() {
        return R.color.card_scan_corner_color;
    }

    public void setRect(RectF rect, int radius) {
        this.rect = rect;
        this.radius = radius;
        postInvalidate();
    }

    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (rect != null) {
            Paint paintAntiAlias = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintAntiAlias.setColor(getResources().getColor(getBackgroundColorId()));
            paintAntiAlias.setStyle(Paint.Style.FILL);
            canvas.drawPaint(paintAntiAlias);

            paintAntiAlias.setXfermode(xfermode);
            canvas.drawRoundRect(rect, radius, radius, paintAntiAlias);

            if (!drawCorners) {
                return;
            }

            Paint paint = new Paint();
            paint.setColor(getResources().getColor(getCornerColorId()));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(dpToPx(cornerDp));
            paint.setStrokeCap(Paint.Cap.ROUND);

            // top left
            int lineLength = dpToPx(40);
            float x = rect.left + dpToPx(1);
            float y = rect.top + dpToPx(1);
            oval.left = x;
            oval.top = y;
            oval.right = x + 2 * radius;
            oval.bottom = y + 2 * radius;
            canvas.drawArc(oval, 180, 90, false, paint);
            canvas.drawLine(oval.left, oval.bottom - radius, oval.left,
                    oval.bottom - radius + lineLength, paint);
            canvas.drawLine(oval.right - radius, oval.top,
                    oval.right - radius + lineLength, oval.top, paint);

            // top right
            x = rect.right - dpToPx(1) - 2 * radius;
            y = rect.top + dpToPx(1);
            oval.left = x;
            oval.top = y;
            oval.right = x + 2 * radius;
            oval.bottom = y + 2 * radius;
            canvas.drawArc(oval, 270, 90, false, paint);
            canvas.drawLine(oval.right, oval.bottom - radius, oval.right,
                    oval.bottom - radius + lineLength, paint);
            canvas.drawLine(oval.right - radius, oval.top,
                    oval.right - radius - lineLength, oval.top, paint);

            // bottom right
            x = rect.right - dpToPx(1) - 2 * radius;
            y = rect.bottom - dpToPx(1) - 2 * radius;
            oval.left = x;
            oval.top = y;
            oval.right = x + 2 * radius;
            oval.bottom = y + 2 * radius;
            canvas.drawArc(oval, 0, 90, false, paint);
            canvas.drawLine(oval.right, oval.bottom - radius, oval.right,
                    oval.bottom - radius - lineLength, paint);
            canvas.drawLine(oval.right - radius, oval.bottom,
                    oval.right - radius - lineLength, oval.bottom, paint);

            // bottom left
            x = rect.left + dpToPx(1);
            y = rect.bottom - dpToPx(1) - 2 * radius;
            oval.left = x;
            oval.top = y;
            oval.right = x + 2 * radius;
            oval.bottom = y + 2 * radius;
            canvas.drawArc(oval, 90, 90, false, paint);
            canvas.drawLine(oval.left, oval.bottom - radius, oval.left,
                    oval.bottom - radius - lineLength, paint);
            canvas.drawLine(oval.right - radius, oval.bottom,
                    oval.right - radius + lineLength, oval.bottom, paint);
        }

        drawBox(canvas);
    }

    private void drawBox(Canvas canvas) {
        if (rect != null) {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeCap(Paint.Cap.SQUARE);
            paint.setStrokeWidth(2);
            // top
            float x = rect.left + 55 + 2 * radius;
            float y = rect.top + 2;
            float x1 = rect.right - 55 - 2 * radius;
            float y2 = rect.top + 2;
            canvas.drawLine(x, y, x1, y2, paint);

            // bottom
            x = rect.left + 55 + 2 * radius;
            y = rect.bottom - 2;
            x1 = rect.right - 55 - 2 * radius;
            y2 = rect.bottom - 2;
            canvas.drawLine(x, y, x1, y2, paint);


            // left
            x = rect.left + 2;
            y = rect.top + 55 + 2 * radius;
            x1 = rect.left + 2;
            y2 = rect.bottom - 55 - 2 * radius;
            canvas.drawLine(x, y, x1, y2, paint);

            // right
            x = rect.right - 2;
            y = rect.top + 55 + 2 * radius;
            x1 = rect.right - 2;
            y2 = rect.bottom - 55 - 2 * radius;
            canvas.drawLine(x, y, x1, y2, paint);
        }
    }
}