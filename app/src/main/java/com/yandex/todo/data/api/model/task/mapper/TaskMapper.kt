package com.yandex.todo.data.api.model.task.mapper

interface TaskMapper <Task, TaskModel>{
    fun Task.mapFromTask(): TaskModel

    fun TaskModel.mapToTask() : Task
}