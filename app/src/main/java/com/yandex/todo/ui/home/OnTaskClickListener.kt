package com.yandex.todo.ui.home

import com.yandex.todo.data.api.model.Task

interface OnTaskClickListener {
    fun onTaskClickListener(task: Task)
}