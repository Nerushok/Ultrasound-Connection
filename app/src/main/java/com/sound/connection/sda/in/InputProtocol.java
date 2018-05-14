package com.sound.connection.sda.in;

import android.util.Log;

import com.sound.connection.stp.SoundTransferProtocolOptions;

public class InputProtocol implements IInputProtocol {


    private final String mTagName = this.getClass().getSimpleName();

    private SoundRecorder mSoundRecorder;
    private SoundDataAnalyzer mSoundDataAnalyzer;
    private SoundTransferProtocolOptions mOptions;
    private OnDataDetectListener mOnDataDetectListener;

    private final byte EMPTY_SYMBOL = -1;
    private final byte ZERO_SYMBOL = 0;
    private final byte ONE_SYMBOL = 1;
    private final byte ADDITIONAL_SYMBOL = 2;

    private int mBufferDataSize;
    private int mSampleRate;
    private double[] mBufferedData;
    private final byte mQuantumSize = 8;
    private int mFftArraySize;
    private byte[] mAnalyzingSymbols = {EMPTY_SYMBOL, EMPTY_SYMBOL, EMPTY_SYMBOL, EMPTY_SYMBOL,
            EMPTY_SYMBOL, EMPTY_SYMBOL, EMPTY_SYMBOL, EMPTY_SYMBOL};

    private final byte STATE_RELEASED = 0;
    private final byte STATE_LISTENING = 1;
    private final byte STATE_PAUSED = 2;
    private final byte STATE_INITIATED = 3;
    private volatile byte mState;


    public InputProtocol(SoundTransferProtocolOptions options) {
        setOptions(options);
        init();
    }

    @Override
    public void init() {
        if (mState != STATE_RELEASED) return;
        mSampleRate = mOptions.getSampleRate();
//        mBufferDataSize = mOptions.getRecordBufferSize();
        mBufferDataSize = 256;
        mFftArraySize = mSampleRate * mOptions.getSymbolDurationInMillis() / 1000;

        mSoundRecorder = new SoundRecorder(
                mSampleRate,
                4096,
                mOnSoundDataWriteListener);
        mSoundDataAnalyzer = new SoundDataAnalyzer(mSampleRate, mBufferDataSize,
                mOptions.getZeroFrequency(), mOptions.getOneFrequency(), mOptions.getAdditionalFrequency());
        mBufferedData = new double[mBufferDataSize];

        setState(STATE_INITIATED);
    }

    @Override
    public void start() {
        if (mState == STATE_RELEASED || mState == STATE_LISTENING) return;
        setState(STATE_LISTENING);
        mSoundRecorder.startRecording();
    }

    @Override
    public void pause() {
        if (mState == STATE_RELEASED || mState == STATE_PAUSED) return;
        setState(STATE_PAUSED);
        mSoundRecorder.stopRecording();
    }

    private final OnSoundDataWriteListener mOnSoundDataWriteListener = new OnSoundDataWriteListener() {
        @Override
        public void onWrite(short[] data) {
            if (mState != STATE_LISTENING) return;
            onNewDataReceived(data);
        }
    };

    private void onNewDataReceived(short[] receivedData) {

//        int dataSize = 4096;
//        double[] sound = new double[dataSize];
//        short[] buffer = new short[dataSize];
//        for (int i = 0; i < sound.length; i++) {
//            sound[i] = Math.sin(2.0 * Math.PI * i / ((double) mSampleRate / 17000));
//            buffer[i] = (short) (sound[i] * Short.MAX_VALUE);
//        }

        short[] data = receivedData;

        int reducedDataSize = mFftArraySize / mQuantumSize;
        short[] reducedData = new short[mFftArraySize];
        int innerIndex = 0;

        for (int i = 0; i < data.length; i++) {
            reducedData[innerIndex] = data[i];
            innerIndex++;
            if (innerIndex == reducedDataSize) {
                innerIndex = 0;

                fillBufferDataArray(reducedData);
                byte foundedSymbol = mSoundDataAnalyzer.searchSpecialSymbol(mBufferedData);
//                if (foundedSymbol >= 0 && foundedSymbol <= 1) {
//                    notifyOnDataDetectListenersByDataSymbol(foundedSymbol != 0);
//                } else if (foundedSymbol == 2) {
//                    notifyOnDataDetectListenersByStartSymbol();
//                }
                putToAnalyzingSymbol(foundedSymbol);

                byte detectedSymbol = checkPresenceOfCharacter();
                if (detectedSymbol != EMPTY_SYMBOL) {
                    clearAnalyzingArray();
                    if (foundedSymbol >= 0 && foundedSymbol <= 1) {
                        notifyOnDataDetectListenersByDataSymbol(foundedSymbol != 0);
                    } else if (foundedSymbol == 2) {
                        notifyOnDataDetectListenersByStartSymbol();
                    }
                }
            }
        }
    }

    private void putToAnalyzingSymbol(byte newSymbol) {
        System.arraycopy(mAnalyzingSymbols, 1, mAnalyzingSymbols, 0, mQuantumSize-1);
        mAnalyzingSymbols[mAnalyzingSymbols.length - 1] = newSymbol;
    }

    private byte mZeroSymbolCount = 0;
    private byte mOneSymbolCount = 0;
    private byte mAdditionalSymbolCount = 0;

    private byte checkPresenceOfCharacter() {
        mZeroSymbolCount = 0;
        mOneSymbolCount = 0;
        mAdditionalSymbolCount = 0;
        for (int i = 0; i < mAnalyzingSymbols.length; i++) {
            byte quantum = mAnalyzingSymbols[i];
            if (quantum == ZERO_SYMBOL) mZeroSymbolCount++;
            else if (quantum == ONE_SYMBOL) mOneSymbolCount++;
            else if (quantum == ADDITIONAL_SYMBOL) mAdditionalSymbolCount++;
        }
        if (mZeroSymbolCount > 6) return ZERO_SYMBOL;
        else if (mOneSymbolCount > 6) return ONE_SYMBOL;
        else if (mAdditionalSymbolCount > 6) return ADDITIONAL_SYMBOL;
        else return EMPTY_SYMBOL;
    }

    private void clearAnalyzingArray() {
        for (int i = 0; i < mAnalyzingSymbols.length; i++) {
            mAnalyzingSymbols[i] = EMPTY_SYMBOL;
        }
    }

    private void notifyOnDataDetectListenersByStartSymbol() {
        if (mOnDataDetectListener == null) return;
        mOnDataDetectListener.onStartSymbolDetect();
    }

    private void notifyOnDataDetectListenersByDataSymbol(boolean symbol) {
        if (mOnDataDetectListener == null) return;
        mOnDataDetectListener.onDataSymbolDetect(symbol);
    }

    @Override
    public void subscribe(OnDataDetectListener onDataDetectListener) {
        this.mOnDataDetectListener = onDataDetectListener;
    }

    @Override
    public void release() {
        mSoundRecorder.release();
    }

    @Override
    public void setOptions(SoundTransferProtocolOptions options) {
        if (options != null) {
            mOptions = options;
        }
    }

    /**
     * Set new input working state.
     *
     * @param state - new state.
     */
    private void setState(final byte state) {
        this.mState = state;
    }

    /**
     * Get current input working state.
     *
     * @return - current state.
     */
    public int getState() {
        return mState;
    }

    private int i1 = 0;

    private void fillBufferDataArray(short[] newData) {
        for (i1 = 0; i1 < mBufferDataSize; i1++) {
            mBufferedData[i1] = (double) newData[i1] / 32768.0;
        }
    }
}
