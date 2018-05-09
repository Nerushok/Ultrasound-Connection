package com.sound.connection.stp;

import com.sound.connection.hldp.Callback;
import com.sound.connection.hldp.RequestCallback;
import com.sound.connection.sda.in.IInputProtocol;
import com.sound.connection.sda.in.InputProtocol;
import com.sound.connection.sda.in.OnDataDetectListener;
import com.sound.connection.sda.out.IOutputProtocol;
import com.sound.connection.sda.out.OutputProtocol;

public class SoundTransferProtocol implements ISoundTransferProtocol {


    private IInputProtocol mInputProtocol;
    private IOutputProtocol mOutputProtocol;

    private SoundTransferProtocolOptions mOptions;

    private Callback mDataReceivedCallback;


    public SoundTransferProtocol() {
        mOptions = getDefaultOptions();
        init();
    }

    public SoundTransferProtocol(SoundTransferProtocolOptions options) {
        mOptions = options;
        init();
    }

    private void init() {
        mInputProtocol = new InputProtocol(mOptions);
        mOutputProtocol = new OutputProtocol(mOptions);

        mInputProtocol.init();
    }


    @Override
    public void send(byte[] data, RequestCallback callback) {
        mOutputProtocol.sendData(data, null);
    }

    @Override
    public void subscribe(Callback callback) {
        mDataReceivedCallback = callback;
        mInputProtocol.subscribe(mOnDataDetectListener);
        mInputProtocol.start();
    }

    private void notifySubscribedListeners(boolean binary) {
        mDataReceivedCallback.onReceive(binary ? "1" : "0");
    }

    private final OnDataDetectListener mOnDataDetectListener = new OnDataDetectListener() {
        @Override
        public void onDataSymbolDetect(boolean binary) {
            notifySubscribedListeners(binary);
        }

        @Override
        public void onStartSymbolDetect() {

        }
    };

    public SoundTransferProtocolOptions getDefaultOptions() {
        return new SoundTransferProtocolOptions(
                44100,
                1024,
                17000,
                18000,
                19000,
                50);
    }
}
