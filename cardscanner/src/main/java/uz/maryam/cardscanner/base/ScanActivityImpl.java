package uz.maryam.cardscanner.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import java.util.List;

import uz.maryam.cardscanner.R;
import uz.maryam.cardscanner.base.image.BitmapHelper;

public class ScanActivityImpl extends ScanBaseActivity {
    public static final String SCAN_CARD_TEXT = "scanCardText";
    public static final String SCAN_CARD_TORCH_ON = "torchOnIcon";
    public static final String SCAN_CARD_TORCH_OFF = "torchOffIcon";
    public static final String POSITION_CARD_TEXT = "positionCardText";
    public static final String RESULT_CARD_NUMBER = "cardNumber";
    public static final String RESULT_EXPIRY_MONTH = "expiryMonth";
    public static final String RESULT_EXPIRY_YEAR = "expiryYear";
    private static long startTimeMs = 0;
    private ImageView mDebugImageView;
    private boolean mInDebugMode = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bouncer_private_activity_scan_card);

        String scanCardText = getIntent().getStringExtra(SCAN_CARD_TEXT);
        if (!TextUtils.isEmpty(scanCardText)) {
            ((TextView) findViewById(R.id.scanCard)).setText(scanCardText);
        }

        String positionCardText = getIntent().getStringExtra(POSITION_CARD_TEXT);
        if (!TextUtils.isEmpty(positionCardText)) {
            ((TextView) findViewById(R.id.positionCard)).setText(positionCardText);
        }

        if (getIntent().getByteArrayExtra(SCAN_CARD_TORCH_ON) != null && getIntent().getByteArrayExtra(SCAN_CARD_TORCH_OFF) != null) {
            BitmapHelper.byteArrayToBitmap(getIntent().getByteArrayExtra(SCAN_CARD_TORCH_ON));
            BitmapHelper.byteArrayToBitmap(getIntent().getByteArrayExtra(SCAN_CARD_TORCH_OFF));

        }


        (findViewById(R.id.flashlightButton)).setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.is_unselect_flashlight));

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 110);
        } else {
            mIsPermissionCheckDone = true;
        }

        findViewById(R.id.closeButton).setOnClickListener(v -> onBackPressed());

        mDebugImageView = findViewById(R.id.debugImageView);
        mInDebugMode = getIntent().getBooleanExtra("debug", false);
        if (!mInDebugMode) {
            mDebugImageView.setVisibility(View.INVISIBLE);
        }
        setViewIds(R.id.flashlightButton, R.id.cardRectangle, R.id.shadedBackground, R.id.texture,
                R.id.cardNumber, R.id.expiry);
    }

    @Override
    protected void onCardScanned(String numberResult, String month, String year) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_CARD_NUMBER, numberResult);
        intent.putExtra(RESULT_EXPIRY_MONTH, month);
        intent.putExtra(RESULT_EXPIRY_YEAR, year);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onPrediction(final String number, final Expiry expiry, final Bitmap bitmap,
                             final List<DetectedBox> digitBoxes, final DetectedBox expiryBox,
                             final Bitmap bitmapForObjectDetection, final Bitmap fullScreenBitmap) {

        if (mInDebugMode) {
            mDebugImageView.setImageBitmap(ImageUtils.drawBoxesOnImage(bitmap, digitBoxes,
                    expiryBox));
            if (startTimeMs != 0) {
                startTimeMs = 0;
            }
        }

        super.onPrediction(number, expiry, bitmap, digitBoxes, expiryBox, bitmapForObjectDetection,
                fullScreenBitmap);
    }

}
