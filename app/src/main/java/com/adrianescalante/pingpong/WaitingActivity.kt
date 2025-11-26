package com.adrianescalante.pingpong

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.internal.http2.Http2Reader
import org.json.JSONObject
import kotlin.concurrent.thread

class WaitingActivity : AppCompatActivity() , ServerEventListener{

    lateinit var txtWaiting : TextView
    lateinit var handler : Handler

    lateinit var animationRunnable : Runnable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_waiting)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handler = Handler(Looper.getMainLooper())

        txtWaiting = findViewById(R.id.txtWaiting)

        animationRunnable = object : Runnable {

            var dots = 0

            override fun run (){
                val text = "Waiting for Players" + ".".repeat(dots)
                txtWaiting.text = text
                dots = (dots+1)%4
                handler.postDelayed(this,800)

            }
        }
    }



    override fun onResume() {
        super.onResume()
        WebSocketManager.setListener(this)

        handler.post(animationRunnable)
        val jo = JSONObject()
            .put(Cons.K_TYPE,Cons.WAITING_COUNTDOWN)

        WebSocketManager.send(jo)


    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(animationRunnable)
        WebSocketManager.setListener(null)
    }

    override fun onServerMessage(json: JSONObject) {
        runOnUiThread {

            val type = json.optString(Cons.K_TYPE, "")
            when (type){
                Cons.INIT_COUNT_DOWN -> {
                    val intent = Intent(this, CountdownActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }



    fun setTxtWaiting(msg : String){
        txtWaiting.text = msg
    }

}