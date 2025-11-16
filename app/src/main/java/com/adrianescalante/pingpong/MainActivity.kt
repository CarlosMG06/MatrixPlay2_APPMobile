package com.adrianescalante.pingpong

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adrianescalante.pingpong.WebSocketManager.msg
import com.adrianescalante.pingpong.WebSocketManager.send
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

class MainActivity : AppCompatActivity() , ServerEventListener {
    lateinit var btnConnect: Button
    lateinit var msgStatusName: TextView

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

        msgStatusName = findViewById(R.id.msgStatusName)
        btnConnect = findViewById(R.id.btnConnect)
        val nom = findViewById<EditText>(R.id.nom)
        val ip = findViewById<EditText>(R.id.ip)


        btnConnect.setOnClickListener {


            val txtNom = nom.text.toString()
            val txtIp = ip.text.toString()

            if (txtIp.isNotEmpty() && txtNom.isNotEmpty()) {

                Toast.makeText(this, "Conectando con $txtIp", Toast.LENGTH_SHORT).show()
                btnConnect.text = "Connecting ..."

                Globals.clientName = txtNom

                WebSocketManager.connect(txtIp)


            } else {
                Toast.makeText(this, "No espacios vacios", Toast.LENGTH_SHORT).show()
            }


        }

    }

    fun setMsgStatusName(msg: String) {
        msgStatusName.text = msg
        msgStatusName.visibility = View.VISIBLE
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
                Cons.K_GET_NAME -> {
                    val js = msg(Cons.CHECK_NAME)
                        .put(Cons.K_VALUE, Globals.clientName)


                    send(js)
                }


                Cons.CHECK_NAME_STATUS -> {

                    val statusNombre = json.optString(Cons.K_VALUE)

                    //si esta disponible entramos a esperar
                    if (statusNombre.equals(Cons.K_NAME_AVALIBLE)) {

                        val intent = Intent(this, WaitingActivity::class.java)
                        startActivity(intent)

                    }

                    if (statusNombre.equals(Cons.K_NAME_USED)) {

                        setMsgStatusName("El nombre de usuario esta en uso.")
                    }

                }
            }
        }
    }
}