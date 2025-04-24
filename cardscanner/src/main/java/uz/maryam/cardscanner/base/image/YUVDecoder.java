package uz.maryam.cardscanner.base.image;

public class YUVDecoder {

    static {
        System.loadLibrary("yuv-decoder");
    }

    public static native void YUVtoRGBA(byte[] yuv, int width, int height, int[] out);

}
