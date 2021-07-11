package com.yandex.todo.ui.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.yandex.todo.data.api.model.task.Task

class HomeDiffUtilCallback(
    private val oldList: List<Task>,
    private val newList: List<Task>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}