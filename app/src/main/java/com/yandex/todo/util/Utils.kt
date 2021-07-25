package com.yandex.todo.util

import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.yandex.todo.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * [observable]    Observable LiveData
 * [T]             LiveData content
 * [action]        lambda. Block of actions, which need to do. Contains [T]
 */

fun <T> Fragment.observe(observable: LiveData<T>, action: (T) -> Unit) {
    observable.observe(viewLifecycleOwner, {
        if (it != null) action.invoke(it)
    })
}

fun Date.toSimpleString(): String {
    val format = SimpleDateFormat("EEE,  dd MMM", Locale("ru"))
    return format.format(this)
}

fun TextView.setTextViewDrawableColor(color: Int) {
    for (drawable in this.compoundDrawables) {
        if (drawable != null) {
            drawable.colorFilter =
                PorterDuffColorFilter(
                    ContextCompat.getColor(this.context, color),
                    PorterDuff.Mode.SRC_IN
                )
        }
    }
}

fun TextView.changeContextState(isActive: Boolean) {
    if (isActive) {
        this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        this.setTextColor(ContextCompat.getColor(this.context, R.color.gray))
    } else {
        this.paintFlags = this.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        this.setTextColor(ContextCompat.getColor(this.context, R.color.main_text_color))
    }
}