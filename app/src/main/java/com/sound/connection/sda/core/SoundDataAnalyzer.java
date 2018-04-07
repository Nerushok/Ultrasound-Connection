package com.sound.connection.sda.core;

import android.util.Log;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.Arrays;


public class SoundDataAnalyzer {

    private final String tagName = this.getClass().getSimpleName();
    private final int sampleRate;
    private final FastFourierTransformer fastFourierTransformer;

    public SoundDataAnalyzer(int sampleRate) {
        this.sampleRate = sampleRate;
        fastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD);
    }

    /**
     * Process data in 3 steps.
     * 1) Filter low frequency sound
     * 2) Get DTF.
     *
     * @param data
     */
    public void analyzeRecorded(double[] data) {
        Complex[] complexes = fastFourierTransformer.transform(data, TransformType.FORWARD);
        Complex[] array = Arrays.copyOfRange(complexes, complexes.length/2, complexes.length);

        double[] result = new double[array.length];
        int x = 0;

        double maxAmplitudeFrequency = 0;
        for (int i = result.length-1; i >= 0; i--) {
            double realValue = array[i].abs();
            result[i] = realValue;
            if (maxAmplitudeFrequency < realValue) {
                maxAmplitudeFrequency = realValue;
                x = i;
            }
        }

        Log.d(tagName, "x = " + String.valueOf(x));
        Log.d(tagName, "frequency " + String.valueOf((result.length - x) * (sampleRate / data.length)));
        Log.d(tagName, "==============");
    }

    private double getMaxValue(double[] data) {
        double max = 0;
        for (double i : data) {
            if (i > max) max = i;
        }
        return max;
    }
}
