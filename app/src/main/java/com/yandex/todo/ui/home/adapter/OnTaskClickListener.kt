package com.yandex.todo.ui.home.adapter

import com.yandex.todo.data.api.model.task.Task

interface OnTaskClickListener {
    fun onTaskClickListener(task: Task)
    fun onCheckBoxChangeStateListener(position: Int, done: Boolean)
}