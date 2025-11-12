package com.adrianescalante.pingpong

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MainActivity : AppCompatActivity() {
    lateinit var btnConnect : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnConnect = findViewById<Button>(R.id.btnConnect)
        val nom = findViewById<EditText>(R.id.nom)
        val ip = findViewById<EditText>(R.id.ip)

        WebSocketManager.setActiveActivity(this)

        btnConnect.setOnClickListener {



            val txtNom = nom.text.toString()
            val txtIp = ip.text.toString()

            if (txtIp.isNotEmpty() && txtNom.isNotEmpty()) {

                Toast.makeText(this, "Conectando con $txtIp", Toast.LENGTH_SHORT).show()
                btnConnect.text = "Connecting ..."


                WebSocketManager.connect(txtIp)



            } else {
                Toast.makeText(this, "No espacios vacios", Toast.LENGTH_SHORT).show()
            }



        }

    }
    fun setWaitingScreen(){
        val intent = Intent(this@MainActivity, WaitingActivity::class.java)
        startActivity(intent)
    }


}