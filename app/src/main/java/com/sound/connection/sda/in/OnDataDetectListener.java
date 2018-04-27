package com.sound.connection.sda.in;

public interface OnDataDetectListener {

    /**
     * Detected start packet symbol.
     */
    void onStartSymbolDetect();

    /**
     * Detected data symbol.
     * @param binary - data symbol. false - 0, true - 1.
     */
    void onDataSymbolDetect(boolean binary);
}
