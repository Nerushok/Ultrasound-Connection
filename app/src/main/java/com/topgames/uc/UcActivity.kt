package com.topgames.uc

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.sound.connection.hldp.Callback
import com.sound.connection.hldp.RequestCallback
import com.sound.connection.hldp.SoundConnection
import java.lang.Exception

class UcActivity : AppCompatActivity() {


    private val tagName = this::class.java.simpleName
    private val soundConnection: SoundConnection = SoundConnection()

    private val tvReceivedDataView: TextView by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        findViewById<TextView>(R.id.receivedData)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uc)

        findViewById<View>(R.id.sendData).setOnClickListener {
            soundConnection.send("a", object : RequestCallback {
                override fun onSuccess() {

                }

                override fun onError(errorCode: Int) {

                }
            })
        }

        findViewById<View>(R.id.startReceiving).setOnClickListener {
            soundConnection.subscribe(object : Callback {
                override fun onReceive(data: String?) {
                    runOnUiThread {
                        data?.let {
                            tvReceivedDataView.text = it
                            Log.d(tagName, "onReceive - $it")
                        }
                    }
                }

                override fun onError(errorCode: Int, e: Exception?) {
                    Log.e(tagName, "onError in subscribe", e)
                }
            })
        }
    }
}