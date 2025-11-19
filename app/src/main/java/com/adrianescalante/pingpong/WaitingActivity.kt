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

class WaitingActivity : AppCompatActivity() , ServerEventListener{

    lateinit var msgNom : TextView

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

        msgNom = findViewById<TextView>(R.id.msgNom)


        // WebSocketManager.sendMessage("{\"msg\":\"msg \"}")
    }

    override fun onResume() {
        super.onResume()
        WebSocketManager.setListener(this)

        val jo = JSONObject()
            .put(Cons.K_TYPE,Cons.WAITING_COUNTDOWN)

        WebSocketManager.send(jo)


    }

    override fun onPause() {
        super.onPause()
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



    public fun setNom(msg : String){
        msgNom.text = msg
    }

}