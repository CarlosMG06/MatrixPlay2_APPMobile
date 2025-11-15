package com.adrianescalante.pingpong

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.slider.Slider
import org.json.JSONObject


class GameActivity : AppCompatActivity() , ServerEventListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val display = findViewById<Display>(R.id.display)
        display.invalidate()

    }
    override fun onResume() {
        super.onResume()
        WebSocketManager.setListener(this)
    }

    override fun onPause() {
        super.onPause()
        WebSocketManager.setListener(null)
    }

    override fun onServerMessage(json: JSONObject) {
        runOnUiThread {
            val tipo = json.getString("type")
//            if (tipo == "countdown") {
//                val time = json.getInt("value")
//                // cambiar pantalla CountDownActivity
//                val intent = Intent(this, CountDownActivity::class.java)
//                intent.putExtra("tiempo", time)
//                startActivity(intent)
//            }
        }
    }





}