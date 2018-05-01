package com.sound.connection.stp;

import com.sound.connection.hldp.RequestCallback;
import com.sound.connection.sda.in.IInputProtocol;
import com.sound.connection.sda.out.IOutputProtocol;

public class SoundTransferProtocol implements ISoundTransferProtocol {


    private IInputProtocol mInputProtocol;
    private IOutputProtocol mOutputProtocol;


    @Override
    public void send(byte[] data, RequestCallback callback) {
        mOutputProtocol
    }
}
