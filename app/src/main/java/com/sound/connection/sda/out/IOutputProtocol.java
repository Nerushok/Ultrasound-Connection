package com.sound.connection.sda.out;

public interface IOutputProtocol {

    void sendData(byte[] data, OnGenerateSoundListener listener);

    void release();
}
