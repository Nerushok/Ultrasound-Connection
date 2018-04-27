package com.sound.connection.sda.in;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class SoundRecorder {

    private final String mTagName = this.getClass().getSimpleName();

    private final byte STATE_RELEASED = 0;
    private final byte STATE_RECORDING = 1;
    private final byte STATE_STOPPED = 2;
    private final byte STATE_INITIATED = 3;
    private volatile byte mState;

    private Thread mRecordingThread;

    private AudioRecord mAudioRecorder;
    private final int mSampleRate;
    private final int mRecordBufferSize;
    private final short[] mAudioBufferArray;

    private final OnSoundDataWriteListener mOnSoundDataWroteListener;


    public SoundRecorder(final int sampleRate,
                         final int recordBufferSize,
                         final OnSoundDataWriteListener onSoundDataWroteListener) {
        this.mSampleRate = sampleRate;
        this.mRecordBufferSize = recordBufferSize;
        this.mOnSoundDataWroteListener = onSoundDataWroteListener;
        mAudioBufferArray = new short[recordBufferSize];
        init();
    }

    /**
     * Init recording controller instance.
     */
    private void init() {
        if (mState != STATE_RELEASED) return;
        Log.d(mTagName, "init recorder");
        mRecordingThread = new Thread(mAudioRecordRunnable, "AudioRecordingThread");

        mAudioRecorder = new AudioRecord(
                MediaRecorder.AudioSource.DEFAULT,
                mSampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                mRecordBufferSize);

        setState(STATE_INITIATED);
    }

    /**
     * Start recording.
     */
    void startRecording() {
        if (mState == STATE_RELEASED) return;
        setState(STATE_RECORDING);
        mRecordingThread.start();
    }

    /**
     * Stop recording.
     */
    void stopRecording() {
        if (mState != STATE_RECORDING) return;
        mAudioRecorder.stop();
        setState(STATE_STOPPED);
    }

    /**
     * Runnable for recording audio data from {@link AudioRecord} on not main thread.
     */
    private final Runnable mAudioRecordRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(mTagName, "start recording");
            mAudioRecorder.startRecording();
            while (mState == STATE_RECORDING) {
                mAudioRecorder.read(mAudioBufferArray, 0, mRecordBufferSize);
                notifyListeners();
            }
        }
    };

    /**
     * Notify all subscribed listeners.
     */
    private void notifyListeners() {
        if (mOnSoundDataWroteListener != null) {
            mOnSoundDataWroteListener.onWrite(mAudioBufferArray);
        }
    }

    /**
     * Release recording controller instance.
     */
    void release() {
        if (mState == STATE_RELEASED) return;
        Log.d(mTagName, "release recorder");
        stopRecording();
        mAudioRecorder.release();

        setState(STATE_RELEASED);
    }

    /**
     * Set new recording controller state.
     * @param state - new state
     */
    private void setState(final byte state) {
        this.mState = state;
    }

    /**
     * Get current recording controller state.
     * @return - current state.
     */
    public int getState() {
        return mState;
    }
}
