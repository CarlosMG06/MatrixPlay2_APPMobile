package com.adrianescalante.pingpong

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CountdownActivity : AppCompatActivity() {

    var onScreen = false
    lateinit var msgCountdown : TextView
    lateinit var player1 : TextView
    lateinit var player2 : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_countdown)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        msgCountdown = findViewById(R.id.msgCountdown)
        player1 = findViewById(R.id.player1)
        player2 = findViewById(R.id.player2)



    }

    fun initCountdown(player1 : String, player2 : String, msgCountdown :String){




        WebSocketManager.setActivityView(this@CountdownActivity::class.java)

        this.player1.text = player1
        this.player2.text = player2
        this.msgCountdown.text = msgCountdown

    }
}