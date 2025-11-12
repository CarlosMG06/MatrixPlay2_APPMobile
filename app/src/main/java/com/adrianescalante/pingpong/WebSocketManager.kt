package com.adrianescalante.pingpong

import android.util.Log
import okhttp3.*

object WebSocketManager {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private var isConnected = false

    var onMessageReceived: ((String) -> Unit)? = null
    var onConnected: (() -> Unit)? = null
    var onError: ((String) -> Unit)? = null



    fun connect(ip: String, port: Int) {
        if (isConnected) return

        val request = Request.Builder()
            .url("wss://$ip:$port")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
                isConnected = true
                Log.d("WebSocket", "Conectado al servidor")
                onConnected?.invoke()
            }

            override fun onMessage(ws: WebSocket, text: String) {
                Log.d("WebSocket", "Mensaje: $text")
                onMessageReceived?.invoke(text)
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                isConnected = false
                Log.e("WebSocket", "Error: ${t.message}")
                onError?.invoke(t.message ?: "Error desconocido")
            }

            override fun onClosing(ws: WebSocket, code: Int, reason: String) {
                isConnected = false
                ws.close(1000, null)
            }
        })
    }

    fun sendMessage(msg: String) {
        webSocket?.send(msg)
    }

    fun close() {
        webSocket?.close(1000, "Cerrando")
        isConnected = false
    }
}