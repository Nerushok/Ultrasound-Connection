package com.topgames.uc;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    // originally from http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
    // and modified by Steve Pomeroy <steve@staticfree.info>
//    private final int duration = 3; // seconds
//    private final int sampleRate = 44100;
//    private final int numSamples = duration * sampleRate;
//    private final double sample[] = new double[numSamples];
//    private final double freqOfTone = 20000; // hz

//    private final byte generatedSnd[] = new byte[2 * numSamples];

    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                generateTone(1000, 4000).play();
                playSound(18000, 44100 * 2);
            }
        }, 1000);
    }

    private AudioTrack generateTone(double freqHz, int durationMs) {
        int count = (44100 * (durationMs / 1000)) & ~1;
        byte[] samples = new byte[count];
        int t = 0;
        for(int i = 0; i < count; i++){
            byte sample = (byte)(Math.sin(2 * Math.PI * i / (44100 / freqHz)) * 255);
            samples[i] = sample;
            if (t < 100) Log.d("Test", String.valueOf(samples[t]));
            t++;
        }
        int bufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);
        AudioTrack track = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT,
                bufferSize,
                AudioTrack.MODE_STREAM);
        track.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
        track.write(samples, 0, count);
        return track;
    }

    private void playSound(double frequency, int duration) {
        // AudioTrack definition
        int mBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        AudioTrack mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                mBufferSize, AudioTrack.MODE_STREAM);

        // Sine wave
        double[] mSound = new double[duration];
        short[] mBuffer = new short[duration];
        for (int i = 0; i < mSound.length; i++) {
            mSound[i] = Math.sin((2.0*Math.PI * i/(44100/frequency)));
            mBuffer[i] = (short) (mSound[i]*Short.MAX_VALUE);
        }

        mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
        mAudioTrack.play();

        mAudioTrack.write(mBuffer, 0, mSound.length);
        mAudioTrack.stop();
        mAudioTrack.release();

    }

//    private AudioTrack generateTone(double freqHz, int durationMs) {
//        int count = (int)(44100.0 * 2.0 * (durationMs / 1000.0)) & ~1;
//        short[] samples = new short[count];
//        int t = 0;
//        for(int i = 0; i < count; i += 2){
//            short sample = (short)(Math.sin(2 * Math.PI * i / (44100.0 / freqHz)) * 0x7FFF);
//            samples[i + 0] = sample;
//            samples[i + 1] = sample;
//            if (t < 100) Log.d("Test", String.valueOf(samples[t]));
//            t++;
//        }
//        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
//                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
//                count * (Short.SIZE / 8), AudioTrack.MODE_STATIC);
//        track.write(samples, 0, count);
//        return track;
//    }
}
