package com.sound.connection.sda.in;

import com.sound.connection.stp.SoundTransferProtocolOptions;

public interface IInputProtocol {

    void setOptions(SoundTransferProtocolOptions options);

    void init();

    void subscribe(OnDataDetectListener onDataDetectListener);

    void release();
}
