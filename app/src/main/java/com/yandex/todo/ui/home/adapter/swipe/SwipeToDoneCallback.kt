package com.yandex.todo.ui.home.adapter.swipe

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.yandex.todo.R

abstract class SwipeToDoneCallback(context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    private val doneIcon = ContextCompat.getDrawable(context, R.drawable.ic_check)
    private val intrinsicWidth = doneIcon?.intrinsicWidth
    private val intrinsicHeight = doneIcon?.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = context.resources.getColor(R.color.bg_green_background, null)
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {

        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(
                c,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        // Draw the red delete background
        background.color = backgroundColor
        background.setBounds(
            itemView.left + dX.toInt(),
            itemView.top,
            itemView.left,
            itemView.bottom
        )
        background.draw(c)

        // Calculate position of done icon
        val doneIconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
        val doneIconMargin = (itemHeight + intrinsicHeight) / 2
        val doneIconLeft = itemView.left + doneIconMargin - intrinsicWidth!!
        val doneIconRight = itemView.left + doneIconMargin
        val doneIconBottom = doneIconTop + intrinsicHeight

        // Draw the delete icon
        doneIcon?.setBounds(doneIconLeft, doneIconTop, doneIconRight, doneIconBottom)
        doneIcon?.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
}