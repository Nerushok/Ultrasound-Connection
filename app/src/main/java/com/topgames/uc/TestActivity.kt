package com.topgames.uc

import android.media.*
import android.os.Bundle
import android.os.Process
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.topgames.uc.sound_core.core.SoundDataAnalyzer
import com.topgames.uc.sound_core.core.SoundDataGenerator
import kotlin.concurrent.thread

class TestActivity : AppCompatActivity() {

    private val tagName = this::class.java.simpleName

    private val sampleRate = 44100
    private val recordBlockSize = 256
    private val soundDataGenerator = SoundDataGenerator(sampleRate)
    private val soundDataAnalyzer = SoundDataAnalyzer(sampleRate)
    private lateinit var audioRecorder: AudioRecord
    private var recordingBufferSize: Int = 0
    private var paused = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.generateSoundButton).setOnClickListener {
//            generateSound()
            recordSound()
        }
        initAudioRecorder()
    }

    private fun initAudioRecorder() {
        recordingBufferSize = AudioRecord.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT)
        audioRecorder = AudioRecord(
                MediaRecorder.AudioSource.DEFAULT,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                recordingBufferSize)
    }

    private fun generateSound() {
        // AudioTrack definition
        val mBufferSize = AudioTrack.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT)

        val mAudioTrack = AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                mBufferSize, AudioTrack.MODE_STREAM)

        // Sine wave
        val soundData = soundDataGenerator.generateByDataArray(byteArrayOf(0, 1, 0, 1, 0, 1), 1000, 2000, 500)
        mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume())
        mAudioTrack.play()

        mAudioTrack.write(soundData, 0, soundData.size)
        mAudioTrack.stop()
        mAudioTrack.release()
    }

    private fun recordSound() {
        thread(start = true) {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO)
            var bufferReadResult: Int
            val audioBuffer = shortArrayOf((recordingBufferSize.toShort() / 2).toShort())
            val toTransform = DoubleArray(recordingBufferSize)
            if (audioRecorder.state != AudioRecord.STATE_INITIALIZED) {
                Log.e(tagName, "Audio Record can't initialize!")
            }
            audioRecorder.startRecording()
            Log.d(tagName, "Start recording")

            while (!paused) {
                bufferReadResult = audioRecorder.read(audioBuffer, 0, audioBuffer.size)
                // TODO analise
                audioBuffer.forEachIndexed { i, value ->
                    if (i < recordBlockSize && i < bufferReadResult) {
                        toTransform[i] = value / 32768.0
                    }
                }
                soundDataAnalyzer.analyzeRecorded(toTransform)
            }
            audioRecorder.stop()
            audioRecorder.release()
        }
    }

    override fun onDestroy() {
        paused = true
        audioRecorder.stop()
        audioRecorder.release()
        super.onDestroy()
    }
}