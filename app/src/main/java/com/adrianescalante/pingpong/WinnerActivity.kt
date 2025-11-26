package com.adrianescalante.pingpong

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONArray
import org.json.JSONObject

class WinnerActivity : AppCompatActivity() , ServerEventListener{

    lateinit var txtWinner : TextView

    lateinit var btnSalir : Button

    lateinit var btnReplay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_winner)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtWinner= findViewById(R.id.txtWinner)
        btnReplay=findViewById(R.id.btnReplay)
        btnSalir=findViewById(R.id.btnSalir)

        val nom = intent.getStringExtra("winner")
        txtWinner.text = "Winner: $nom"

        btnReplay.setOnClickListener {

            var jo = JSONObject()
                .put(Cons.K_TYPE,Cons.C_PLAY_AGAIN)
                .put(Cons.K_VALUE, Globals.clientName)

            WebSocketManager.send(jo)

            val intent = Intent(this, WaitingActivity::class.java)
            startActivity(intent)
        }

        btnSalir.setOnClickListener {
            var jo = JSONObject()
                .put(Cons.K_TYPE,Cons.C_EXIT)
                .put(Cons.K_VALUE, Globals.clientName)

            WebSocketManager.send(jo)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        WebSocketManager.setListener(this)
        val nom = intent.getStringExtra("winner")

        txtWinner.text = "Winner: $nom"

    }

    override fun onPause() {
        super.onPause()
        WebSocketManager.setListener(null)
    }

    override fun onServerMessage(json: JSONObject) {
        runOnUiThread {
            val type = json.getString(Cons.K_TYPE)
            when (type) {


            }
        }
    }
}