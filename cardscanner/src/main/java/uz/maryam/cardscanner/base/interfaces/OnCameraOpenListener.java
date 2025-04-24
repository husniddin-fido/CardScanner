package uz.maryam.cardscanner.base.interfaces;

import android.hardware.Camera;

import androidx.annotation.Nullable;

public interface OnCameraOpenListener {
    void onCameraOpen(@Nullable Camera camera);
}
