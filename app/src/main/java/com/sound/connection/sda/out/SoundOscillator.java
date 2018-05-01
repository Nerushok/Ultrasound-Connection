package com.sound.connection.sda.out;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class SoundOscillator {


    private final int mSampleRate;
    private AudioTrack mAudioTrack;


    public SoundOscillator(int sampleRate) {
        this.mSampleRate = sampleRate;
    }

    public void generate(short[] soundData) {

    }

    private AudioTrack createAudioTrack() {
        int bufferSize = AudioTrack.getMinBufferSize(mSampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                AudioFormat.CHANNEL_OUT_MONO,)
    }
}
