package com.sound.connection.stp;

import com.sound.connection.hldp.Callback;
import com.sound.connection.hldp.RequestCallback;

public interface ISoundTransferProtocol {

    void send(byte[] data, RequestCallback callback);

    void subscribe(Callback callback);

    void release();
}
