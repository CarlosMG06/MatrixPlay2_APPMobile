package com.adrianescalante.pingpong

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.lang.ref.WeakReference

object WebSocketManager {

    private var listener: ServerEventListener? = null

    fun setListener(l: ServerEventListener?) {
        listener = l
    }





    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private var isConnected = false

    private var currentActivity: WeakReference<AppCompatActivity>? = null


    fun onMessageReceived(msg: String) {
        val json = JSONObject(msg)
        listener?.onServerMessage(json)
    }
    fun setActiveActivity(activity: AppCompatActivity) {
        currentActivity = WeakReference(activity)
    }

    fun connect(ip: String) {
        val request = Request.Builder().url("ws://$ip:3000").build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            //al iniciar sesion guarda el log y cambia la pantalla
            override fun onOpen(ws: WebSocket, response: Response) {
                Log.i("WebSocket", "Conectado al servidor $ip")

            }


            override fun onMessage(ws: WebSocket, text: String) {
                onMessageReceived(text)
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Error de conexión", t)

                (currentActivity?.get() as? MainActivity)?.btnConnect?.text = "Connect"

                currentActivity?.get()?.runOnUiThread {
                    Toast.makeText(currentActivity?.get(), "Error de conexión", Toast.LENGTH_LONG).show()
                }
                //setActivityView(GameActivity::class.java)
            }
        })
    }
    fun msg(type : String) : JSONObject{
        return JSONObject().put(Cons.K_TYPE,type)
    }
    fun send(json: JSONObject) {
        webSocket?.send(json.toString())
    }
}
