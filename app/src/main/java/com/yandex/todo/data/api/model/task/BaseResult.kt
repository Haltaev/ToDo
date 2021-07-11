package com.yandex.todo.data.api.model.task

import com.yandex.todo.data.api.model.task.BaseResult.Success
sealed class BaseResult<out R> {

    data class Success<out T>(val data: T) : BaseResult<T>()
    data class Error(val exception: Exception) : BaseResult<Nothing>()
    object Loading : BaseResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}
val BaseResult<*>.succeeded
    get() = this is Success && data != null