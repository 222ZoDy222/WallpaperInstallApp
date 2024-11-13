package com.zdy.wallpaperinstallapp.utils.customView

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.max
import kotlin.math.min

class DraggableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr), View.OnTouchListener {

    private var offsetX = 0f
    private var offsetY = 0f
    private var lastTouchX = 0f
    private var lastTouchY = 0f

    init {
        setOnTouchListener(this)
    }

    override fun onDraw(canvas: Canvas) {
        // Сохраняем текущее состояние Canvas и смещаем его по X и Y
        canvas.save()
        canvas.translate(offsetX, offsetY)
        super.onDraw(canvas)
        canvas.restore()
    }

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Запоминаем начальную позицию касания
                lastTouchX = event.x
                lastTouchY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                // Вычисляем смещение
                val dx = event.x - lastTouchX
                val dy = event.y - lastTouchY

                // Обновляем положение с учётом границ
                offsetX = ensureWithinBoundsX(offsetX + dx)
                offsetY = ensureWithinBoundsY(offsetY + dy)

                // Перезапоминаем последнюю точку касания
                lastTouchX = event.x
                lastTouchY = event.y

                // Перерисовываем View
                invalidate()
            }
        }
        return true
    }

    private fun ensureWithinBoundsX(newX: Float): Float {
        val maxOffsetX = (width - drawable.intrinsicWidth).toFloat()
        return min(0f, max(maxOffsetX, newX))
    }

    private fun ensureWithinBoundsY(newY: Float): Float {
        val maxOffsetY = (height - drawable.intrinsicHeight).toFloat()
        return min(0f, max(maxOffsetY, newY))
    }
}
