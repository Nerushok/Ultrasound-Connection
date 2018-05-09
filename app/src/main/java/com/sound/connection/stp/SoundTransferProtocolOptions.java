package com.sound.connection.stp;

public class SoundTransferProtocolOptions {


    private int mSampleRate;
    private int mRecordBufferSize;
    private int mZeroFrequency;
    private int mOneFrequency;
    private int mAdditionalFrequency;
    private int mSymbolDurationInMillis;


    public SoundTransferProtocolOptions(int sampleRate,
                                        int recordBufferSize,
                                        int zeroFrequency,
                                        int oneFrequency,
                                        int additionalFrequency,
                                        int symbolDurationInMillis) {
        this.mSampleRate = sampleRate;
        this.mRecordBufferSize = recordBufferSize;
        this.mZeroFrequency = zeroFrequency;
        this.mOneFrequency = oneFrequency;
        this.mAdditionalFrequency = additionalFrequency;
        this.mSymbolDurationInMillis = symbolDurationInMillis;
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

    public void setSymbolDurationInMillis(int durationInMillis) {
        mSymbolDurationInMillis = durationInMillis;
    }

    public int getSymbolDurationInMillis() {
        return mSymbolDurationInMillis;
    }
}
