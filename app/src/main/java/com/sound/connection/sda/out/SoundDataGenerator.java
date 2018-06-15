package com.sound.connection.sda.out;

public class SoundDataGenerator {

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
     * @param zeroValueFrequency   - frequency of zero symbol
     * @param oneValueFrequency    - frequency of one symbol
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
     * Generate sound data array by input byte data.
     *
     * @param data               - data for generation in sound waves;
     * @param zeroValueFrequency - frequency of zero symbol;
     * @param oneValueFrequency  - frequency of one symbol;
     * @param symbolDuration     - the smallest data signal duration in milliseconds;
     * @param pauseDuration      - pause duration in milliseconds.
     * @return output signal array.
     */
    public short[] generateByDataArray(final byte[] data,
                                       final int zeroValueFrequency,
                                       final int oneValueFrequency,
                                       final int symbolDuration,
                                       final int pauseDuration) {
        int soundDataArraySize =
                calculateSoundDataArrayWithPauseSize(data.length, symbolDuration, pauseDuration);
        short[] sound = new short[soundDataArraySize];
        int offset = 0;
        for (byte value : data) {
            short[] generatedByValue;
            if (value == 0) {
                generatedByValue = generateSignalWithPauseInternal(
                        zeroValueFrequency, mSampleRate, symbolDuration, pauseDuration);
            } else {
                generatedByValue = generateSignalWithPauseInternal(
                        oneValueFrequency, mSampleRate, symbolDuration, pauseDuration);
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
            sound[i] = Math.sin(2.0 * Math.PI * i / ((double) sampleRate / frequency));
            buffer[i] = (short) (sound[i] * Short.MAX_VALUE);
        }
        return buffer;
    }

    /**
     * Generate one signal with some frequency and duration.
     *
     * @param frequency      - signal frequency;
     * @param sampleRate     - signal sample rate;
     * @param symbolDuration - signal duration in milliseconds;
     * @param pauseDuration  - pause duration in milliseconds;
     * @return output signal array.
     */
    private short[] generateSignalWithPauseInternal(final int frequency,
                                                    final int sampleRate,
                                                    final int symbolDuration,
                                                    final int pauseDuration) {
        // Sine wave
        int dataSize = sampleRate * symbolDuration / 1000;
        int pauseSize = sampleRate * pauseDuration / 1000;
        double[] sound = new double[dataSize + pauseSize];
        short[] buffer = new short[dataSize + pauseSize];
        for (int i = 0; i < sound.length; i++) {
            if (i <= dataSize) {
                sound[i] = Math.sin(2.0 * Math.PI * i / ((double) sampleRate / frequency));
                buffer[i] = (short) (sound[i] * Short.MAX_VALUE);
            } else {
                sound[i] = Math.sin(2.0 * Math.PI * i / ((double) sampleRate / 1));
                buffer[i] = (short) (sound[i] * Short.MAX_VALUE);
            }
        }
        return buffer;
    }

    /**
     * Calculate size future sound data.
     *
     * @param byteDataSize   - discrete sounds count;
     * @param symbolDuration - duration of one discrete sound;
     * @return future sound data size.
     */
    private int calculateSoundDataArraySize(final int byteDataSize,
                                            final int symbolDuration) {
        int oneDiscreteSignal = mSampleRate * symbolDuration / 1000 /*one second*/;
        return byteDataSize * oneDiscreteSignal;
    }

    private int calculateSoundDataArrayWithPauseSize(final int byteDataSize,
                                                     final int symbolDuration,
                                                     final int pauseDuration) {
        int signalWithPauseDuration = mSampleRate * (symbolDuration + pauseDuration) / 1000 /*one second*/;
        return byteDataSize * signalWithPauseDuration;
    }
}