package com.sound.connection.sda.out;

import com.sound.connection.stp.SoundTransferProtocolOptions;

public class OutputProtocol implements IOutputProtocol {


    private SoundTransferProtocolOptions mOptions;
    private SoundDataGenerator mSoundDataGenerator;


    public OutputProtocol(SoundTransferProtocolOptions options) {
        this.mOptions = options;
        init();
    }

    private void init() {
        mSoundDataGenerator = new SoundDataGenerator(mOptions.getSampleRate());
    }

    @Override
    public void sendData(byte[] data, OnGenerateSoundListener listener) {

    }
}
