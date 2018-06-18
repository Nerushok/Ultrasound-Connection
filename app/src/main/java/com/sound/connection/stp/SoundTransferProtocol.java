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

    private final byte STATE_RELEASED = 0;
    private final byte STATE_INITIATED = 3;
    private volatile byte mState = STATE_RELEASED;


    public SoundTransferProtocol() {
        mOptions = getDefaultOptions();
        init();
    }

    public SoundTransferProtocol(SoundTransferProtocolOptions options) {
        mOptions = options;
        init();
    }

    private void init() {
        if (mState != STATE_RELEASED) return;
        mInputProtocol = new InputProtocol(mOptions);
        mOutputProtocol = new OutputProtocol(mOptions);

        mInputProtocol.init();

        setState(STATE_INITIATED);
    }


    @Override
    public void send(byte[] data, RequestCallback callback) {
        checkWorkingState();
        mOutputProtocol.sendData(data, null);
    }

    @Override
    public void subscribe(Callback callback) {
        checkWorkingState();
        mDataReceivedCallback = callback;
        mInputProtocol.subscribe(mOnDataDetectListener);
        mInputProtocol.start();
    }

    private void notifySubscribedListeners(boolean binary) {
        mDataReceivedCallback.onReceive(binary ? "1" : "0");
    }

    private void checkWorkingState() {
        if (mState == STATE_RELEASED) {
            init();
        }
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

    @Override
    public void release() {
        if (mState == STATE_RELEASED) return;
        mInputProtocol.release();
        mOutputProtocol.release();
        setState(STATE_RELEASED);
    }

    /**
     * Set new working state.
     *
     * @param state - new state.
     */
    private void setState(final byte state) {
        this.mState = state;
    }

    /**
     * Get current working state.
     *
     * @return - current state.
     */
    public int getState() {
        return mState;
    }

    public SoundTransferProtocolOptions getDefaultOptions() {
        return new SoundTransferProtocolOptions(
                44100,
                256,
                18000,
                18700,
                19500,
                30,
                10);
    }
}
