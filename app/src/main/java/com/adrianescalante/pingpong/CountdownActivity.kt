package com.adrianescalante.pingpong

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject

class CountdownActivity : AppCompatActivity() , ServerEventListener{

    lateinit var msgCountdown : TextView
    lateinit var player1 : TextView
    lateinit var player2 : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
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
            val type = json.getString(Cons.K_TYPE)
            when (type) {
                Cons.T_COUNTDOWN -> {

                    val js = JSONObject(json.optString(Cons.K_VALUE))
                    val player1 = js.optString("player1")
                    val player2 = js.optString("player2")
                    val msgCountdown = js.optString("msgCountDown")

                    initCountdown(player1,player2,msgCountdown)

                    //revisamos si somos el player 1 o 2
                    //y asignamos el nombre del rival
                    //solo entramos una ves
                    if(msgCountdown.equals("5")){



                        if(player1.equals(Globals.clientName)){
                            Globals.player=1

                            Globals.rivalName = player2
                        }else{
                            Globals.player=2

                            Globals.rivalName = player1
                        }

                    }

                    //
                    if(msgCountdown.equals("0")){
                        val intent = Intent(this, GameActivity::class.java)
                        startActivity(intent)
                    }
                }

            }
        }
    }
    fun initCountdown(player1 : String, player2 : String, msgCountdown :String){

        //Toast.makeText(this, "Cuenta atras $msgCountdown", Toast.LENGTH_SHORT).show()
        this.player1.text = player1
        this.player2.text = player2
        this.msgCountdown.text = msgCountdown

    }
}