package com.sound.connection.hldp;

import com.sound.connection.Utils;
import com.sound.connection.stp.ISoundTransferProtocol;
import com.sound.connection.stp.SoundTransferProtocol;

public class SoundConnection implements ISoundConnection {


    private ISoundTransferProtocol mSoundTransferProtocol;


    public SoundConnection() {
        init();
    }

    private void init() {
        mSoundTransferProtocol = new SoundTransferProtocol();
    }

    @Override
    public void subscribe(Callback callback) {
        mSoundTransferProtocol.subscribe(callback);
    }

    @Override
    public void send(CharSequence data, RequestCallback callback) {
        byte[] binaryData = Utils.convertToBinary(data);
        mSoundTransferProtocol.send(binaryData, callback);
    }

    @Override
    public void release() {
        mSoundTransferProtocol.release();
    }
}
