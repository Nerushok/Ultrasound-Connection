package com.sound.connection.stp;

public class SoundTransferProtocolOptions {

    private int mSampleRate;
    private int mRecordBufferSize;
    private int mZeroFrequency;
    private int mOneFrequency;
    private int mAdditionalFrequency;

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

    public int getZeroFrequency() {
        return mZeroFrequency;
    }

    public void setZeroFrequency(int zeroFrequency) {
        mZeroFrequency = zeroFrequency;
    }

    public int getOneFrequency() {
        return mOneFrequency;
    }

    public void setOneFrequency(int oneFrequency) {
        mOneFrequency = oneFrequency;
    }

    public int getAdditionalFrequency() {
        return mAdditionalFrequency;
    }

    public void setAdditionalFrequency(int additionalFrequency) {
        mAdditionalFrequency = additionalFrequency;
    }
}
