package uz.maryam.cardscanner.base;

import android.content.Context;

import java.io.IOException;
import java.nio.MappedByteBuffer;

import uz.maryam.cardscanner.R;

class ResourceModelFactory extends ModelFactory {
    @Override
    public MappedByteBuffer loadFindFourFile(Context context) throws IOException {
        return loadModelFromResource(context, R.raw.findfour);
    }

    @Override
    public MappedByteBuffer loadRecognizeDigitsFile(Context context) throws IOException {
        return loadModelFromResource(context, R.raw.fourrecognize);
    }
}
