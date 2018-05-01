package com.sound.connection.hldp;

public interface RequestCallback {

    void onSuccess();

    void onError(int errorCode);
}
