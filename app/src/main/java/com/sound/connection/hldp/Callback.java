package com.sound.connection.hldp;

public interface Callback {

    void onReceive(String data);

    void onError(int errorCode, Exception e);
}
