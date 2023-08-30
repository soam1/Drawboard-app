package com.akashsoam.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs:AttributeSet): View(context, attrs) {

//drawing path
    private lateinit var drawPath:FingerPath
    //defines what to draw
    private lateinit var canvasPaint:Paint
    //defines how to draw
    private lateinit var drawPaint:Paint
    private var color = Color.BLACK
    private lateinit var canvas: Canvas
    private lateinit var canvasBitmap: Bitmap
    private var brushSize:Float = 0.toFloat()

    init {
        setUpDrawing()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(canvasBitmap)
    }

    //this function will be called by the system when the user is going to touch the screen
    override fun onTouchEvent(event: MotionEvent?): Boolean {


        val touchX = event?.x
        val touchY = event?.y
        when(event?.action){
            //this event will be fired when the user puts the finger on the screen
            MotionEvent.ACTION_DOWN->{
                drawPath.color = color
                drawPath.brushThickness = brushSize.toFloat()

                drawPath.reset()//setting path before we set initial point
                drawPath.moveTo(touchX!!, touchY!!)

            }
            //this event will be fired when the user starts to move its fingers; this will be fired continually until user picks up the finger from the canvas
            MotionEvent.ACTION_MOVE->{
                drawPath.lineTo(touchY!!, touchX!!)
            }
            //this will be fired when user picks up the finger from the canvas
            MotionEvent.ACTION_UP->{
                drawPath = FingerPath(color, brushSize)
            }

            else-> return false


        }
        invalidate()//refreshing the layout to reflect the drawing changes

        return true


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(canvasBitmap, 0f, 0f, drawPaint)
        if(!drawPath.isEmpty){
            drawPaint.strokeWidth = drawPath.brushThickness
            drawPaint.color = drawPath.color
            canvas?.drawPath(drawPath, drawPaint)//drawing path on campus
        }
    }

    private fun setUpDrawing() {
        drawPaint = Paint()
        drawPath = FingerPath(color, brushSize)
        drawPaint.color = color
        drawPaint.style = Paint.Style.STROKE
        drawPaint.strokeJoin = Paint.Join.ROUND
        drawPaint.strokeCap = Paint.Cap.ROUND
        brushSize = 20.toFloat()
    }

    internal inner class FingerPath(var color:Int, var brushThickness:Float): Path() {

    }
}