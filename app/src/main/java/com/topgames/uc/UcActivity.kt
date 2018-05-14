package com.topgames.uc

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.sound.connection.Utils
import com.sound.connection.hldp.Callback
import com.sound.connection.hldp.RequestCallback
import com.sound.connection.hldp.SoundConnection
import kotlinx.android.synthetic.main.activity_uc.*
import java.lang.Exception

class UcActivity : AppCompatActivity() {


    private val tagName = this::class.java.simpleName
    private val soundConnection: SoundConnection = SoundConnection()

    private val tvReceivedDataView: TextView by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        findViewById<TextView>(R.id.receivedData)
    }

    private val etSendingData: EditText by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        findViewById<EditText>(R.id.sendingData)
    }

    private val tvSendingBinaryData: TextView by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        findViewById<TextView>(R.id.sendingBinaryData)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uc)

        tvSendingBinaryData.text = Utils.convertToBinary(etSendingData.text.toString()).run {
            var binary = StringBuilder()
            this.forEach { bit -> binary.append(bit) }
            binary.toString()
        }

        findViewById<View>(R.id.sendData).setOnClickListener {
            etSendingData.text.let {
                if (!TextUtils.isEmpty(it))
                    soundConnection.send(it.toString(), object : RequestCallback {
                        override fun onSuccess() {

                        }

                        override fun onError(errorCode: Int) {

                        }
                    })
            }
        }

        findViewById<View>(R.id.startReceiving).setOnClickListener {
            soundConnection.subscribe(object : Callback {
                override fun onReceive(data: String?) {
                    runOnUiThread {
                        data?.let {
                            if (tvReceivedDataView.text == "---") tvReceivedDataView.text = ""
                            tvReceivedDataView.text = tvReceivedDataView.text.toString() + it
                            Log.d(tagName, "onReceive - $it")
                        }
                    }
                }

                override fun onError(errorCode: Int, e: Exception?) {
                    Log.e(tagName, "onError in subscribe", e)
                }
            })
        }

        findViewById<View>(R.id.stopReceiving).setOnClickListener {
            soundConnection.release()
        }

        findViewById<View>(R.id.clear).setOnClickListener {
            tvReceivedDataView.text = "---"
        }

        etSendingData.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                tvSendingBinaryData.text = Utils.convertToBinary(etSendingData.text.toString()).run {
                    var binary = StringBuilder()
                    this.forEach { bit -> binary.append(bit) }
                    binary.toString()
                }
            }
        })
    }

    override fun onStop() {
        soundConnection.release()
        super.onStop()
    }
}