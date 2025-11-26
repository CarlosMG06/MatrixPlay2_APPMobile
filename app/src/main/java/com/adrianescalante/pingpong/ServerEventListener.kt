package com.adrianescalante.pingpong

import org.json.JSONObject

interface ServerEventListener {
    fun onServerMessage(json: JSONObject)
}