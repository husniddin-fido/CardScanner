#include <jni.h>

JNIEXPORT void JNICALL
Java_uz_maryam_cardscanner_base_image_YUVDecoder_YUVtoRGBA(JNIEnv *env, jobject obj,
                                                           jbyteArray yuv420sp,
                                                           jint width, jint height,
                                                           jintArray rgbOut) {
    int sz;
    int i;
    int j;
    int Y;
    int Cr = 0;
    int Cb = 0;
    int pixPtr = 0;
    int jDiv2 = 0;
    int R = 0;
    int G = 0;
    int B = 0;
    int cOff;
    int w = width;
    int h = height;
    sz = w * h;

    jint *rgbData = (jint *) ((*env)->GetPrimitiveArrayCritical(env, rgbOut, 0));
    jbyte *yuv = (jbyte *) (*env)->GetPrimitiveArrayCritical(env, yuv420sp, 0);

    for (j = 0; j < h; j++) {
        pixPtr = j * w;
        jDiv2 = j >> 1;
        for (i = 0; i < w; i++) {
            Y = yuv[pixPtr];
            if (Y < 0) Y += 255;
            if ((i & 0x1) != 1) {
                cOff = sz + jDiv2 * w + (i >> 1) * 2;
                Cb = yuv[cOff];
                if (Cb < 0) Cb += 127; else Cb -= 128;
                Cr = yuv[cOff + 1];
                if (Cr < 0) Cr += 127; else Cr -= 128;
            }

            Y = Y + (Y >> 3) + (Y >> 5) + (Y >> 7);
            R = Y + (Cr << 1) + (Cr >> 6);
            if (R < 0) R = 0; else if (R > 255) R = 255;
            G = Y - Cb + (Cb >> 3) + (Cb >> 4) - (Cr >> 1) + (Cr >> 3);
            if (G < 0) G = 0; else if (G > 255) G = 255;
            B = Y + Cb + (Cb >> 1) + (Cb >> 4) + (Cb >> 5);
            if (B < 0) B = 0; else if (B > 255) B = 255;
            rgbData[pixPtr++] = 0xff000000 + (R << 16) + (G << 8) + B;
        }
    }

    (*env)->ReleasePrimitiveArrayCritical(env, rgbOut, rgbData, 0);
    (*env)->ReleasePrimitiveArrayCritical(env, yuv420sp, yuv, 0);
}
