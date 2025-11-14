package com.adrianescalante.pingpong

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class Display @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Fons
        canvas.drawColor(Color.WHITE)

        // pintem un quadrat del 25% al 75% de l'alçada i amplada
        canvas.drawRect(
            width * 0.25f,  // left
            height * 0.75f, // top
            width * 0.75f,  // right
            height * 0.25f, // bottom
            paint)

    }
}