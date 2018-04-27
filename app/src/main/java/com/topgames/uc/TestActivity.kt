package com.topgames.uc

import android.media.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatEditText
import android.util.Log
import android.view.View
import com.sound.connection.sda.`in`.SoundDataAnalyzer
import com.sound.connection.sda.out.SoundDataGenerator

class TestActivity : AppCompatActivity() {

    private val tagName = this::class.java.simpleName

    private lateinit var frequencyEditText: AppCompatEditText

    private val sampleRate = 44100
    private var generatedFrequency = 440
    private val recordBlockSize = 1024
    private val soundDataGenerator = SoundDataGenerator(sampleRate)
    private val soundDataAnalyzer = SoundDataAnalyzer(sampleRate)
    private lateinit var audioRecorder: AudioRecord
    private var recordingBufferSize: Int = 0
    private var paused = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        frequencyEditText = findViewById(R.id.generatedFrequency)
        frequencyEditText.setText(generatedFrequency.toString())
        findViewById<View>(R.id.generateSoundButton).setOnClickListener {
            //            generateSound()
            generatedFrequency = frequencyEditText.text.toString().toInt()
            recordSound()
        }
        initAudioRecorder()
    }

    private fun initAudioRecorder() {
//        recordingBufferSize = AudioRecord.getMinBufferSize(sampleRate,
//                AudioFormat.CHANNEL_IN_MONO,
//                AudioFormat.ENCODING_PCM_16BIT)
//        audioRecorder = AudioRecord(
//                MediaRecorder.AudioSource.DEFAULT,
//                sampleRate,
//                AudioFormat.CHANNEL_IN_MONO,
//                AudioFormat.ENCODING_PCM_16BIT,
//                recordingBufferSize)
        audioRecorder = AudioRecord(
                MediaRecorder.AudioSource.DEFAULT,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                recordBlockSize)
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
//        if (!paused) {
//            paused = true
//            return
//        } else {
//            paused = false
//        }
//        thread(start = true) {
//            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO)
//            var bufferReadResult: Int
//            val audioBuffer = ShortArray(recordingBufferSize)
//            val toTransform = DoubleArray(recordingBufferSize)
//            if (audioRecorder.state != AudioRecord.STATE_INITIALIZED) {
//                Log.e(tagName, "Audio Record can't initialize!")
//            }
//            audioRecorder.startRecording()
//            Log.d(tagName, "Start recording")
//
//            while (!paused) {
//                bufferReadResult = audioRecorder.read(audioBuffer, 0, recordingBufferSize)
//                // TODO analise
//                audioBuffer.forEachIndexed { i, value ->
//                        toTransform[i] = value.toDouble() / 32768.0
//                }
//                soundDataAnalyzer.analyzeRecorded(toTransform)
//            }
//            audioRecorder.stop()
//        }

//        if (!paused) {
//            paused = true
//            return
//        } else {
//            paused = false
//        }
//        thread(start = true) {
//            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO)
//            var bufferReadResult: Int
//            val audioBuffer = ShortArray(recordBlockSize)
//            val toTransform = DoubleArray(recordBlockSize)
//            if (audioRecorder.state != AudioRecord.STATE_INITIALIZED) {
//                Log.e(tagName, "Audio Record can't initialize!")
//            }
//            audioRecorder.startRecording()
//            Log.d(tagName, "Start recording")
//
//            while (!paused) {
//                bufferReadResult = audioRecorder.read(audioBuffer, 0, recordingBufferSize)
//                // TODO analise
//                audioBuffer.forEachIndexed { i, value ->
//                    toTransform[i] = value.toDouble() / 32768.0
//                }
//                soundDataAnalyzer.analyzeRecorded(toTransform)
//            }
//            audioRecorder.stop()
//        }

        val toTransform = DoubleArray(recordBlockSize)

        for (i in 0 until toTransform.size) {
            toTransform[i] = Math.sin(2 * Math.PI * i * generatedFrequency / sampleRate) / 32768.0
        }
        val time = System.currentTimeMillis()
        soundDataAnalyzer.analyzeRecorded(toTransform)
        Log.d(tagName, "Time test = ${System.currentTimeMillis() - time}")
    }

    override fun onDestroy() {
        paused = true
        audioRecorder.stop()
        audioRecorder.release()
        super.onDestroy()
    }
}