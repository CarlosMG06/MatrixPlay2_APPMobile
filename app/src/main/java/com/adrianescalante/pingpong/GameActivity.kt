package com.adrianescalante.pingpong

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adrianescalante.pingpong.WebSocketManager.msg
import com.adrianescalante.pingpong.WebSocketManager.send
import com.google.android.material.slider.Slider
import org.json.JSONObject


class GameActivity : AppCompatActivity() , ServerEventListener{

    lateinit var sliderP1 : Slider
    lateinit var sliderP2 : Slider

    lateinit var display : Display

    lateinit var player1Name : TextView
    lateinit var player2Name : TextView

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

        display = findViewById<Display>(R.id.display)
        display.invalidate()

        sliderP1 = findViewById(R.id.sliderP1)
        sliderP2 = findViewById(R.id.sliderP2)

        player1Name = findViewById(R.id.player1Name)
        player2Name = findViewById(R.id.player2Name)

        if(Globals.player==1){
            sliderP2.isEnabled = false
        }else{
            sliderP1.isEnabled= false
        }



        sliderP1.addOnChangeListener { _,value,_ ->
            val jsonValue = JSONObject()
                .put(Cons.C_NAME, Globals.clientName)
                .put(Cons.C_INPUT,value.toInt())

            val json = JSONObject()
                .put(Cons.K_TYPE,Cons.C_MOVE)
                .put(Cons.K_VALUE,jsonValue)

            send(json)
        }


        sliderP2.addOnChangeListener { _,value,_ ->
            val jsonValue = JSONObject()
                .put(Cons.C_NAME, Globals.clientName)
                .put(Cons.C_INPUT,value.toInt())

            val json = JSONObject()
                .put(Cons.K_TYPE,Cons.C_MOVE)
                .put(Cons.K_VALUE,jsonValue)

            send(json)
        }



    }
    override fun onResume() {
        super.onResume()
        WebSocketManager.setListener(this)
        val jo = JSONObject()
            .put(Cons.K_TYPE,Cons.C_READY_STARTGAME)

        send(jo)

        if(Globals.player==1){
            player1Name.text= Globals.clientName
            player2Name.text= Globals.rivalName
        }else{
            player1Name.text= Globals.rivalName
            player2Name.text= Globals.clientName


        }
    }

    override fun onPause() {
        super.onPause()
        WebSocketManager.setListener(null)
    }

    override fun onServerMessage(json: JSONObject) {
        runOnUiThread {
            val type = json.optString(Cons.K_TYPE)
            when (type) {
                Cons.T_SERVER_DATA -> {
                    val jo = json.optJSONObject(Cons.T_SERVER_GAME_DATA)
                    display.setDatos(jo)

                }
            }
        }
    }





}