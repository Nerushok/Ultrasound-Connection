package com.sound.connection.sda.out;

public class SoundDataGenerator implements IOutputProtocol {

    /**
     * Generate sound sample rate.
     */
    private int mSampleRate;

    public SoundDataGenerator(int sampleRate) {
        this.mSampleRate = sampleRate;
    }

    /**
     * Generate sound data array by input byte data.
     *
     * @param data                 - data for generation in sound waves;
     * @param singleSignalDuration - the smallest data signal duration in milliseconds;
     * @return output signal array.
     */
    public short[] generateByDataArray(final byte[] data,
                                       final int zeroValueFrequency,
                                       final int oneValueFrequency,
                                       final int singleSignalDuration) {
        int soundDataArraySize = calculateSoundDataArraySize(data.length, singleSignalDuration);
        short[] sound = new short[soundDataArraySize];
        int offset = 0;
        for (byte value : data) {
            short[] generatedByValue;
            if (value == 0) {
                generatedByValue = generateSignalInternal(
                        zeroValueFrequency, mSampleRate, singleSignalDuration);
            } else {
                generatedByValue = generateSignalInternal(
                        oneValueFrequency, mSampleRate, singleSignalDuration);
            }
            System.arraycopy(generatedByValue, 0, sound, offset, generatedByValue.length);
            offset += generatedByValue.length;
        }
        return sound;
    }

    /**
     * Generate one signal with some frequency and duration.
     *
     * @param frequency  - signal frequency;
     * @param sampleRate - signal sample rate;
     * @param duration   - signal duration in milliseconds;
     * @return output signal array.
     */
    private short[] generateSignalInternal(final int frequency,
                                           final int sampleRate,
                                           final int duration) {
        // Sine wave
        int dataSize = sampleRate * duration / 1000;
        double[] sound = new double[dataSize];
        short[] buffer = new short[dataSize];
        for (int i = 0; i < sound.length; i++) {
            sound[i] = Math.sin((2.0 * Math.PI * i / (sampleRate / frequency)));
            buffer[i] = (short) (sound[i] * Short.MAX_VALUE);
        }
        return buffer;
    }

    /**
     * Calculate size future sound data.
     *
     * @param byteDataSize         - discrete sounds count;
     * @param singleSignalDuration - duration of one discrete sound;
     * @return future sound data size.
     */
    private int calculateSoundDataArraySize(final int byteDataSize,
                                            final int singleSignalDuration) {
        int oneDiscreteSignal = mSampleRate * singleSignalDuration / /*one second*/ 1000;
        return byteDataSize * oneDiscreteSignal;
    }
}