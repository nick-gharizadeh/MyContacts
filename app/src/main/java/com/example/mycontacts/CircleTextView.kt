package com.example.mycontacts

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CircleTextView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint()
    private var circleColor = Color.BLACK
    private var text = "B"

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = resources.getDimension(R.dimen.default_size)
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = circleColor
        canvas?.drawCircle(width / 2f, height / 2f, width / 2f, paint)
        canvas?.drawText(text, width / 2f, height / 2f - textPaint.ascent() / 2f, textPaint)
    }

    private fun generateColor(char: Char): Int {

        return when (char) {
            'E', 'G', 'I', 'W' -> Color.BLUE
            'B', 'R', 'P'  -> Color.MAGENTA
            'C', 'Y', 'K', 'N' -> Color.GREEN
            'M', 'Z', 'L', 'X' -> Color.YELLOW
            'V', 'U', 'Q', 'A' -> Color.GRAY
            'F', 'O', 'S', 'T'-> Color.CYAN
            'D', 'H'  -> Color.RED
            else -> Color.DKGRAY
        }

    }

    fun setText(char: Char) {
        this.text = char.toString().uppercase()
        circleColor = generateColor(text[0])
        invalidate()
    }

}
