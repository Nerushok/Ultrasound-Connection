package com.topgames.uc.sound_core.core;

import android.util.Log;

import ca.uol.aig.fftpack.RealDoubleFFT;

public class SoundDataAnalyzer {

    private final String tagName = this.getClass().getSimpleName();
    private final int sampleRate;
    private final RealDoubleFFT spectreAnalyzer = new RealDoubleFFT(256);

    public SoundDataAnalyzer(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    private int maxValue = 0;

    /**
     * Process data in 3 steps.
     * 1) Filter low frequency sound
     * 2) Get DTF.
     * 3) Analyze data for
     * @param data
     */
    public void analyzeRecorded(double[] data) {
        spectreAnalyzer.ft(data); // dtf
    }
}
