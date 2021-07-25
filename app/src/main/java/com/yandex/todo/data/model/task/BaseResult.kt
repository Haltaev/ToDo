package com.yandex.todo.data.model.task

import com.yandex.todo.data.model.task.BaseResult.Success

sealed class BaseResult<out R> {

    data class Success<out T>(val data: T) : BaseResult<T>()
    sealed class Error : BaseResult<Nothing>() {
        object UnknownHost : Error()
        object OtherErrors : Error()
    }
}

val BaseResult<*>.succeeded
    get() = this is Success && data != null