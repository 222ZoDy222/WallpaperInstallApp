package com.zdy.wallpaperinstallapp.utils.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.max
import kotlin.math.min

class DraggableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), View.OnTouchListener {

    private var offsetX = 0f
    private var offsetY = 0f
    private var lastTouchX = 0f
    private var lastTouchY = 0f

    private var maxOffsetX = 0f
    private var maxOffsetY = 0f

    init {
        setOnTouchListener(this)
        scaleType = ScaleType.CENTER_CROP
    }

    override fun onDraw(canvas: Canvas) {
        // Save current state of Canvas and move it by X & Y
        drawable?.let {
            updateMaxOffsets(it)
            canvas.save()
            canvas.translate(offsetX, offsetY)
            super.onDraw(canvas)
            canvas.restore()
        }
    }

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        drawable?.let {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Save start position of touch
                    lastTouchX = event.x
                    lastTouchY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    // calc the offset
                    val dx = event.x - lastTouchX
                    val dy = event.y - lastTouchY

                    // update the position ensure within bounds
                    offsetX = ensureWithinBoundsX(offsetX + dx)
                    offsetY = ensureWithinBoundsY(offsetY + dy)

                    // save the last touch position
                    lastTouchX = event.x
                    lastTouchY = event.y

                    // redraw View
                    invalidate()
                }
            }
        }
        return true
    }

    private fun updateMaxOffsets(drawable: Drawable) {
        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()
        val drawableWidth = drawable.intrinsicWidth.toFloat()
        val drawableHeight = drawable.intrinsicHeight.toFloat()

        if (drawableWidth / drawableHeight > viewWidth / viewHeight) {
            maxOffsetX = (viewWidth - drawableWidth * (viewHeight / drawableHeight)) / 2
            maxOffsetY = 0f
        } else {
            maxOffsetX = 0f
            maxOffsetY = (viewHeight - drawableHeight * (viewWidth / drawableWidth)) / 2
        }
    }

    private fun ensureWithinBoundsX(newX: Float): Float {
        return min(0f, max(maxOffsetX, newX))
    }

    private fun ensureWithinBoundsY(newY: Float): Float {
        return min(0f, max(maxOffsetY, newY))
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        offsetX = 0f
        offsetY = 0f
        drawable?.let { updateMaxOffsets(it) }
    }
}
