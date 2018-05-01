package com.sound.connection.hldp;

interface ISoundConnection {

    void subscribe(Callback callback);

    void send(CharSequence data, RequestCallback callback);

    void release();
}
