package com.yandex.todo.data.model.task.mapper

import com.yandex.todo.data.model.task.Importance
import com.yandex.todo.data.model.task.Task
import com.yandex.todo.data.model.task.TaskDaoEntity
import com.yandex.todo.data.model.task.TaskNetworkEntity
import java.util.*

fun Task.mapToNetworkEntity() = TaskNetworkEntity(
    id = this.id,
    text = this.content,
    importance = this.importance.type,
    isDone = !this.isActive,
    deadline = this.deadline.time / 1000,
    createdAt = this.createdAt.time / 1000,
    updatedAt = this.updatedAt.time / 1000,
)

fun TaskNetworkEntity.mapFromNetworkEntity() = Task(
    id = this.id,
    content = this.text,
    importance = when (this.importance) {
        Importance.LOW.type -> Importance.LOW
        Importance.BASIC.type -> Importance.BASIC
        Importance.IMPORTANT.type -> Importance.IMPORTANT
        else -> Importance.BASIC
    },
    isActive = !this.isDone,
    deadline = Date(this.deadline * 1000),
    createdAt = Date(this.createdAt * 1000),
    updatedAt = Date(this.updatedAt * 1000),
)

fun Task.mapToDaoEntity() = TaskDaoEntity(
    id = this.id,
    content = this.content,
    importance = this.importance,
    isActive = this.isActive,
    deadline = this.deadline,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)

fun TaskDaoEntity.mapFromDaoEntity() = Task(
    id = this.id,
    content = this.content,
    importance = this.importance,
    isActive = this.isActive,
    deadline = this.deadline,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)

fun TaskNetworkEntity.mapToDaoEntity() = TaskDaoEntity(
    id = this.id,
    content = this.text,
    importance = when (this.importance) {
        Importance.LOW.type -> Importance.LOW
        Importance.BASIC.type -> Importance.BASIC
        Importance.IMPORTANT.type -> Importance.IMPORTANT
        else -> Importance.BASIC
    },
    isActive = !this.isDone,
    deadline = Date(this.deadline * 1000),
    createdAt = Date(this.createdAt * 1000),
    updatedAt = Date(this.updatedAt * 1000),
)

fun mapFromListOfNetworkEntity(list: List<TaskNetworkEntity>): List<Task> {
    return list.map { it.mapFromNetworkEntity() }
}

fun mapToListOfNetworkEntity(list: List<Task>): List<TaskNetworkEntity> {
    return list.map { it.mapToNetworkEntity() }
}

fun mapFromListOfDaoEntity(list: List<TaskDaoEntity>): List<Task> {
    return list.map { it.mapFromDaoEntity() }
}

fun mapToListOfDaoEntity(list: List<Task>): List<TaskDaoEntity> {
    return list.map { it.mapToDaoEntity() }
}