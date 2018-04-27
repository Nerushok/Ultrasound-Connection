package com.sound.connection.hldp;

interface ISoundConnection {

    void subscribe(Callback callback);

    void release();
}
