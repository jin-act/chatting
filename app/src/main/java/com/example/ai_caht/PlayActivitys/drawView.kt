package com.example.ai_caht.PlayActivitys

import android.content.Context
import android.graphics.*
import android.opengl.ETC1.getWidth
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class drawView: View {
    var size = 5
    val paint = Paint()
    val path = Path()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = size.toFloat()
        canvas!!.drawPath(path, paint)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var x : Float? = 0.toFloat()
        var y : Float? = 0.toFloat()
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                x = event.x
                y = event.y
                path.moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                x = event.x
                y = event.y
                path.lineTo(x, y)
                invalidate()
            }
        }
        return true
    }

}