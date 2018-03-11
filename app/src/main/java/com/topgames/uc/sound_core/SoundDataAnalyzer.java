package com.topgames.uc.sound_core;

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

    public void analyzeRecorded(double[] data) {
        spectreAnalyzer.ft(data);
    }
}
