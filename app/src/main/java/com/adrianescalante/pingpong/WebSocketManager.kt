package com.adrianescalante.pingpong

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.lang.ref.WeakReference

object WebSocketManager {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private var isConnected = false

    private var currentActivity: WeakReference<AppCompatActivity>? = null

    fun setActiveActivity(activity: AppCompatActivity) {
        currentActivity = WeakReference(activity)
    }

    fun connect(ip: String) {
        val request = Request.Builder().url("ws://$ip:443").build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
                Log.i("WebSocket", "Conectado al servidor $ip")
                setActivityView(WaitingActivity::class.java)

            }

            override fun onMessage(ws: WebSocket, text: String) {
                handleMessage(text)
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Error de conexión", t)

                (currentActivity?.get() as? MainActivity)?.btnConnect?.text = "Connect"

                currentActivity?.get()?.runOnUiThread {
                    Toast.makeText(currentActivity?.get(), "Error de conexión", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun setActivityView(activityClass: Class<out AppCompatActivity>) {

        val current = currentActivity?.get()

        if (current != null && current::class.java != activityClass) {
            val intent = Intent(current, activityClass)
            current.startActivity(intent)
        }
    }

    private fun handleMessage(message: String) {
        try {
            val json = JSONObject(message)
            val type = json.optString(Cons.K_TYPE, "")
            //Toast.makeText(it, "Error: ${json.optString("msg")}", Toast.LENGTH_SHORT).show()

            when (type) {
                Cons.T_COUNTDOWN -> {

                    val js = JSONObject(json.optString(Cons.K_VALUE))
                    val player1 = js.optString("player1")
                    val player2 = js.optString("player2")
                    val msgCountdown = js.optString("msgCountDown")

                    (currentActivity?.get() as? CountdownActivity)?.initCountdown(player1,player2,msgCountdown)
                }

                Cons.T_SERVER_START_GAME -> {
                    //(currentActivity?.get() as? GameActivity)?.onServerStart(json)
                }

//                "Error" -> {
//                    currentActivity?.get()?.runOnUiThread {
//                        Toast.makeText(it, "Error: ${json.optString("msg")}", Toast.LENGTH_SHORT).show()
//                    }
//                }


            }

        } catch (e: Exception) {
            Log.e("WebSocket", "Error procesando mensaje", e)
        }
    }

    fun send(json: JSONObject) {
        webSocket?.send(json.toString())
    }
}
