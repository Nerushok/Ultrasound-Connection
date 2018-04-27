package com.sound.connection.stp;

public class SoundTransferProtocolOptions {

    private int mSampleRate;

    private int mRecordBufferSize;

    public SoundTransferProtocolOptions(int sampleRate, int recordBufferSize) {
        this.mSampleRate = sampleRate;
        this.mRecordBufferSize = recordBufferSize;
    }

    public int getSampleRate() {
        return mSampleRate;
    }

    public void setSampleRate(int sampleRate) {
        mSampleRate = sampleRate;
    }

    public int getRecordBufferSize() {
        return mRecordBufferSize;
    }

    public void setRecordBufferSize(int recordBufferSize) {
        mRecordBufferSize = recordBufferSize;
    }
}
