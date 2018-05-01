package com.sound.connection;

import android.util.Log;

import java.util.Arrays;

public class Utils {

    public static byte[] convertToBinary(CharSequence data) {
        byte[] byteDataArray = ((String) data).getBytes();
        Log.d("Utils", Arrays.toString(byteDataArray));
        return byteDataArray;
    }
}
