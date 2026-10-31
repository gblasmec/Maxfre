package com.gblas.ffmaxoverlay

import android.view.MotionEvent
import android.view.View
import android.view.WindowManager

class DragTouchListener(
    private val params: WindowManager.LayoutParams,
    private val windowManager: WindowManager
) : View.OnTouchListener {
    private var startX = 0
    private var startY = 0
    private var touchX = 0f
    private var touchY = 0f

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = params.x
                startY = params.y
                touchX = event.rawX
                touchY = event.rawY
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                params.x = startX + (event.rawX - touchX).toInt()
                params.y = startY + (event.rawY - touchY).toInt()
                windowManager.updateViewLayout(view, params)
                return true
            }
        }
        return false
    }
}
