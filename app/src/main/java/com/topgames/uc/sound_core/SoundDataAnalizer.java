package com.topgames.uc.sound_core;

import android.util.Log;

public class SoundDataAnalizer {

    private final String tagName = this.getClass().getSimpleName();
    private final int sampleRate;

    public SoundDataAnalizer(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    private int maxValue = 0;

    public void analizeRecorded(short[] data) {

    }
}
