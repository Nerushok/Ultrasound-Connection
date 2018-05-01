package com.sound.connection.sda.in;

import android.util.Log;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.Arrays;


public class SoundDataAnalyzer {


    private final String mTagName = this.getClass().getSimpleName();

    private final int mSampleRate;
    private final int mDataSize;

    private final static int NONE_SYMBOL = -1;
    private final static int ZERO_SYMBOL = 0;
    private final static int ONE_SYMBOL = 1;
    private final static int ADDITIONAL_SYMBOL = 2;

    private final int mHalfDeltaFrequencyDetect = 50;
    private final int mZeroFrequency;
    private final int mZeroFrequencyTopLimit;
    private final int mZeroFrequencyBottomLimit;
    private final int mOneFrequency;
    private final int mOneFrequencyTopLimit;
    private final int mOneFrequencyBottomLimit;
    private final int mAdditionalSymbolFrequency;
    private final int mAdditionalSymbolFrequencyTopLimit;
    private final int mAdditionalSymbolFrequencyBottomLimit;

    private final float mFfrFrequencyResolution;

    private final FastFourierTransformer mFastFourierTransformer;


    public SoundDataAnalyzer(int sampleRate,
                             int dataSize,
                             int zeroFrequency,
                             int oneFrequency,
                             int additionalSymbolFrequency) {
        this.mSampleRate = sampleRate;
        this.mDataSize = dataSize;
        this.mFfrFrequencyResolution = mSampleRate / mDataSize;

        this.mZeroFrequency = getFftPositionByFrequency(zeroFrequency);
        this.mOneFrequency = getFftPositionByFrequency(oneFrequency);
        this.mAdditionalSymbolFrequency = getFftPositionByFrequency(additionalSymbolFrequency);

        this.mZeroFrequencyTopLimit = getFftPositionByFrequency(zeroFrequency + mHalfDeltaFrequencyDetect);
        this.mZeroFrequencyBottomLimit = getFftPositionByFrequency(zeroFrequency - mHalfDeltaFrequencyDetect);
        this.mOneFrequencyTopLimit = getFftPositionByFrequency(oneFrequency + mHalfDeltaFrequencyDetect);
        this.mOneFrequencyBottomLimit = getFftPositionByFrequency(oneFrequency - mHalfDeltaFrequencyDetect);
        this.mAdditionalSymbolFrequencyTopLimit = getFftPositionByFrequency(additionalSymbolFrequency + mHalfDeltaFrequencyDetect);
        this.mAdditionalSymbolFrequencyBottomLimit = getFftPositionByFrequency(additionalSymbolFrequency - mHalfDeltaFrequencyDetect);

        mFastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD);
    }

    /**
     * Searching special symbol (0, 1 and StartSymbol) in double array sound data.
     * @param data - sound data.
     * @return - if analyzing got StartSymbol - 2, else if data symbol zero - 0,
     * else if data symbol 1 - 1, else -1.
     */
    public byte searchSpecialSymbol(double[] data) {
        Complex[] fftTransformedData = Arrays.copyOfRange(
                mFastFourierTransformer.transform(data, TransformType.FORWARD), mDataSize / 2, mDataSize);

        double maxZeroValue = getMaxValueInArray(fftTransformedData,
                mZeroFrequencyBottomLimit,
                mZeroFrequencyTopLimit);
        double maxOneValue = getMaxValueInArray(fftTransformedData,
                mOneFrequencyBottomLimit,
                mOneFrequencyTopLimit);
        double maxAdditionalValue = getMaxValueInArray(fftTransformedData,
                mAdditionalSymbolFrequencyBottomLimit,
                mAdditionalSymbolFrequencyTopLimit);

        byte detectedSymbol = compareSymbolValue(maxZeroValue, maxOneValue, maxAdditionalValue);

//        Complex[] complexes = mFastFourierTransformer.transform(data, TransformType.FORWARD);
//        Complex[] array = Arrays.copyOfRange(complexes, complexes.length / 2, complexes.length);
//        int x = 0;
//        double maxAmplitudeFrequency = 0;
//        for (int i = result.length - 1; i >= 0; i--) {
//            double realValue = array[i].abs();
//            result[i] = realValue;
//            if (maxAmplitudeFrequency < realValue) {
//                maxAmplitudeFrequency = realValue;
//                x = i;
//            }
//        }

//        Log.d(mTagName, "x = " + String.valueOf(x));
//        Log.d(mTagName, "frequency " + String.valueOf((result.length - x) * (mSampleRate / data.length)));
//        Log.d(mTagName, "==============");
        return detectedSymbol;
    }

    /**
     * Call this method only once for optimisation.
     * @param frequency - frequency for calculate
     * @return position in FFT array.
     */
    private int getFftPositionByFrequency(int frequency) {
        return Math.round(frequency / getFftFrequencyResolution());
    }

    private double[] mCompareDoubleArray = new double[3];

    /**
     * Compare max symbol value with some coefficient and return detected symbol (0, 1 and StartSymbol).
     * @param maxZeroValue - max zero value in FFT array;
     * @param maxOneValue - max one value in FFT array;
     * @param maxAdditionalValue - max additional symbol value in FFT array
     * @return - if analyzing got StartSymbol - 2, else if data symbol zero - 0,
     * else if data symbol 1 - 1, else -1.
     */
    private byte compareSymbolValue(double maxZeroValue, double maxOneValue, double maxAdditionalValue) {
        mCompareDoubleArray[0] = maxZeroValue;
        mCompareDoubleArray[1] = maxOneValue;
        mCompareDoubleArray[2] = maxAdditionalValue;

        int maxValueSymbol = -1;
        double maxValue = 0.0;
        double preMaxValue = 0.0;
        for (int i = 0; i < mCompareDoubleArray.length-1; i++) {
            if (mCompareDoubleArray[i] > maxValue) {
                preMaxValue = maxValue;
                maxValue = mCompareDoubleArray[i];
                maxValueSymbol = i;
            }
        }

        if (maxValue < preMaxValue * 3) {
            return NONE_SYMBOL;
        } else {
            if (maxValueSymbol == 0) return ZERO_SYMBOL;
            else if (maxValueSymbol == 1) return ONE_SYMBOL;
            else return ADDITIONAL_SYMBOL;
        }
    }

    private int getFftFrequencyResolution() {
        return mSampleRate / mDataSize;
    }

//    public void analyzeRecorded(double[] data) {
//        Complex[] complexes = mFastFourierTransformer.transform(data, TransformType.FORWARD);
//        Complex[] array = Arrays.copyOfRange(complexes, complexes.length / 2, complexes.length);
//
//        double[] result = new double[array.length];
//        int x = 0;
//
//        double maxAmplitudeFrequency = 0;
//        for (int i = result.length - 1; i >= 0; i--) {
//            double realValue = array[i].abs();
//            result[i] = realValue;
//            if (maxAmplitudeFrequency < realValue) {
//                maxAmplitudeFrequency = realValue;
//                x = i;
//            }
//        }
//
//        Log.d(mTagName, "x = " + String.valueOf(x));
//        Log.d(mTagName, "frequency " + String.valueOf((result.length - x) * (mSampleRate / data.length)));
//        Log.d(mTagName, "==============");
//    }

    private double getMaxValueInArray(Complex[] data, int from, int to) {
        double max = 0;
        for (int i = from; i < to; to++) {
            if (data[i].abs() > max) max = i;
        }
        return max;
    }
}
