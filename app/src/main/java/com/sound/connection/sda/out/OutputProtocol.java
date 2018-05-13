package com.sound.connection.sda.out;

import com.sound.connection.stp.SoundTransferProtocolOptions;

public class OutputProtocol implements IOutputProtocol {


    private SoundTransferProtocolOptions mOptions;
    private SoundDataGenerator mSoundDataGenerator;
    private SoundOscillator mSoundOscillator;


    public OutputProtocol(SoundTransferProtocolOptions options) {
        this.mOptions = options;
        init();
    }

    private void init() {
        mSoundDataGenerator = new SoundDataGenerator(mOptions.getSampleRate());
        mSoundOscillator = new SoundOscillator(mOptions.getSampleRate());
    }

    @Override
    public void release() {
        mSoundOscillator.release();
    }

    @Override
    public void sendData(byte[] data, OnGenerateSoundListener listener) {
        mSoundOscillator.generate(
                mSoundDataGenerator.generateByDataArray(data,
                        mOptions.getZeroFrequency(),
                        mOptions.getOneFrequency(),
                        mOptions.getSymbolDurationInMillis()));
    }
}
