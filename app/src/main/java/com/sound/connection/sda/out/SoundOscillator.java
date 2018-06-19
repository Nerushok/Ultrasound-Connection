package com.sound.connection.sda.out;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SoundOscillator {


    private final String mTagName = this.getClass().getSimpleName();

    private AudioTrack mAudioTrack;

    private final int mSampleRate;

    private Executor mGenerateSoundExecutor;

    private volatile boolean mInitiated = false;


    public SoundOscillator(int sampleRate) {
        this.mSampleRate = sampleRate;
    }


    public void generate(final short[] soundData) {
        if (!mInitiated) init();
        mGenerateSoundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(mTagName, "generate - start");
                mAudioTrack.play();
                mAudioTrack.write(soundData, 0, soundData.length);
                mAudioTrack.stop();
                Log.d(mTagName, "generate - end");
            }
        });
    }

    public void release() {
        if (!mInitiated) return;
        mAudioTrack.stop();
        mAudioTrack.release();
        mInitiated = false;
    }

    private void init() {
        mInitiated = true;
        mGenerateSoundExecutor = Executors.newFixedThreadPool(3);

        int bufferSize = AudioTrack.getMinBufferSize(mSampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, mSampleRate,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                bufferSize, AudioTrack.MODE_STREAM);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAudioTrack.setVolume(AudioTrack.getMaxVolume());
        } else {
            mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
        }
    }
}
