package com.sound.connection;

import android.util.Log;

import java.util.Arrays;

public class Utils {

    public static byte[] convertToBinary(CharSequence data) {
//        byte[] byteDataArray = ((String) data).getBytes();
//        Log.d("Utils", Arrays.toString(byteDataArray));
//        return byteDataArray;

//        new String()
        int j = 0;
        byte[] outData = new byte[data.length() * 8];
        StringBuilder binary = new StringBuilder();
        for (byte b : ((String) data).getBytes()) {
            int val = b;

            for (int i = 0; i < 8; i++) {
                byte value = (val & 128) == 0 ? (byte) 0 : (byte) 1;
                binary.append(value);
                outData[j] = value;
                j++;
                val <<= 1;
            }
            binary.append(' ');
        }
        System.out.println("'" + data + "' to binary: " + binary);
        return outData;
    }
}
