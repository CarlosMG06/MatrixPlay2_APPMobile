package com.adrianescalante.pingpong

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import org.json.JSONObject

class Display @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val pixelTypeface: Typeface =
        ResourcesCompat.getFont(context, R.font.m6x11plus)!!

    private val paintText = Paint().apply {
        color = Color.WHITE
        textSize = 15f
        isAntiAlias = false
        typeface = pixelTypeface
    }
    private var p1Points =0
    private var p2Points = 0


    private var scaleX = 1f
    private var scaleY = 1f

    private var ballY = 0f
    private var ballX = 0f
    private var ballSize = 2f


    private var p1PossY = 0f
    private var p2PossY = 0f


    private var p1PossX = 0f
    private var p2PossX = 61f
    private var recWidth = 3f
    private var recHeight = 16f


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        scaleX = w / 64f
        scaleY = h / 64f

        recWidth*=scaleX
        recHeight*=scaleY

        ballX*=scaleX
        ballY*=scaleY
        ballSize*=scaleX

        paintText.textSize=15*scaleX;

        p1PossX*=scaleX
        p2PossX*=scaleX

    }

    fun drawPoints(canvas: Canvas){

        //p1 points
        canvas.drawText(
            p1Points.toString(),
            (width*0.25).toFloat(),
            (height*0.2).toFloat(),
            paintText
        )

        //p2 points
        canvas.drawText(
            p2Points.toString(),
            (width*0.65).toFloat(),
            (height*0.2).toFloat(),
            paintText
        )

    }
    fun drawRects(canvas : Canvas){
        paint.color = Color.parseColor("#50589C")

        canvas.drawRect(
            p1PossX,  // left
            p1PossY, // top
            p1PossX+recWidth,  // right
            p1PossY+recHeight, // bottom
            paint)
        canvas.drawRect(
            p2PossX,  // left
            p2PossY, // top
            p2PossX+recWidth,  // right
            p2PossY+recHeight, // bottom
            paint)

    }

    fun drawBall(canvas : Canvas){
        paint.color = Color.parseColor("#3C467B")
        canvas.drawRect(
            ballX,  // left
            ballY, // top
            ballX+ballSize,  // right
            ballY+ballSize, // bottom
            paint)
    }

    private val paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    fun drawWhiteLine(canvas:Canvas){
        paint.color = Color.WHITE

        canvas.drawLine(
            width*0.5f,
            0f,
            width*0.5f,
            height.toFloat(),
            paint
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        // Fons
        canvas.drawColor(Color.parseColor("#636CCB"))
        drawPoints(canvas)
        drawBall(canvas)
        drawRects(canvas)

    }

    fun setDatos(json : JSONObject){
        p1PossY = (json.optInt("p1PossY").toFloat())*scaleY
        p2PossY = (json.optInt("p2PossY").toFloat())*scaleY

        ballX = (json.optInt("ballX").toFloat())*scaleX
        ballY = (json.optInt("ballY").toFloat())*scaleY

        p1Points = json.optInt("p1Points")
        p2Points = json.optInt("p2Points")

        invalidate()
    }

}