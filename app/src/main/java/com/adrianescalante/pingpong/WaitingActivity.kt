package com.adrianescalante.pingpong

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import kotlin.concurrent.thread

class WaitingActivity : AppCompatActivity() , ServerEventListener{

    lateinit var txtWaiting : TextView
    var animacion = true

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

        txtWaiting = findViewById(R.id.txtWaiting)

//        thread {
//            while (animacion) {
//                for (i in 0..3) {
//                    val text = "Waiting for Players" + ".".repeat(i)
//                    txtWaiting.text = text
//                    Thread.sleep(800)
//                }
//            }
//        }.start()
        // WebSocketManager.sendMessage("{\"msg\":\"msg \"}")
    }

    override fun onResume() {
        super.onResume()
        WebSocketManager.setListener(this)

        animacion=true
        val jo = JSONObject()
            .put(Cons.K_TYPE,Cons.WAITING_COUNTDOWN)

        WebSocketManager.send(jo)


    }

    override fun onPause() {
        super.onPause()
        animacion=false
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



    public fun setTxtWaiting(msg : String){
        txtWaiting.text = msg
    }

}