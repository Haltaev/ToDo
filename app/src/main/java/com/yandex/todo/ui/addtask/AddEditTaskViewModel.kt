package com.yandex.todo.ui.addtask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.todo.data.api.repository.TasksRepository
import com.yandex.todo.data.model.task.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditTaskViewModel @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val tasksRepository: TasksRepository,
) : ViewModel() {

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task> = _task

    fun getTask(taskId: String?) {
        viewModelScope.launch {
            taskId?.let {
                _task.postValue(tasksRepository.getTask(it))
            }
        }
    }

    fun deleteTask(taskId: String?) {
        coroutineScope.launch {
            taskId?.let {
                tasksRepository.markAsDeleted(taskId)
            }
        }
    }

    fun saveTask(task: Task) {
        coroutineScope.launch {
            if (_task.value == null) {
                tasksRepository.saveTask(task)
            } else {
                val newTask = task.copy(
                    id = _task.value!!.id,
                    createdAt = _task.value!!.createdAt,
                )
                tasksRepository.saveTask(newTask)
            }
        }
    }
}