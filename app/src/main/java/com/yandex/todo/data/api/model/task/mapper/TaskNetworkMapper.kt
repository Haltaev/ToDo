package com.yandex.todo.data.api.model.task.mapper

import com.yandex.todo.data.api.model.task.Task
import com.yandex.todo.data.api.model.task.TaskModel
import java.util.*

class TaskNetworkMapper : TaskMapper<Task, TaskModel> {
    override fun Task.mapFromTask(): TaskModel {
        return TaskModel(
            id = this.id,
            text = this.text,
            importance = this.importance,
            done = this.isDone,
            deadline = this.deadline.time,
            createdAt = this.createdAt.time,
            updatedAt = this.updatedAt.time,
        )
    }

    override fun TaskModel.mapToTask(): Task {
        return Task(
            id = this.id,
            text = this.text,
            importance = this.importance,
            isDone = this.done,
            deadline = Date(this.deadline),
            createdAt = Date(this.createdAt),
            updatedAt = Date(this.updatedAt),
        )
    }

    fun List<TaskModel>.mapToListTasks(): List<Task> {
        return this.map { it.mapToTask() }
    }

    fun List<Task>.mapFromListTasks(): List<TaskModel> {
        return this.map { it.mapFromTask() }
    }
}