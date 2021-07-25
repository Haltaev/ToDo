package com.yandex.todo.ui.home

import androidx.lifecycle.*
import com.yandex.todo.R
import com.yandex.todo.data.api.repository.TasksRepository
import com.yandex.todo.data.model.task.BaseResult
import com.yandex.todo.data.model.task.Task
import com.yandex.todo.data.model.task.TaskDaoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val tasksRepository: TasksRepository,
) : ViewModel() {

    /**
     * [itemsMediatorLiveData] contains two types of list: only active tasks and all tasks,
     * it depends on [_currentFiltering]. Setting data and observers in fun [getTasks]
     */
    val itemsMediatorLiveData = MediatorLiveData<List<Task>>()

    private var _currentFiltering = MutableLiveData<Boolean>().apply { value = true }
    val currentFiltering: LiveData<Boolean> = _currentFiltering

    private var _removedTask = MutableLiveData<Task?>()
    val removedTask: LiveData<Task?> = _removedTask

    private var _downloadState = MutableLiveData<Int>()
    val downloadState: LiveData<Int> = _downloadState

    fun changeTaskState(position: Int) {
        viewModelScope.launch {
            val task = itemsMediatorLiveData.value?.get(position)
            task?.let {
                if (task.isActive) {
                    tasksRepository.completeTask(task.id)
                } else {
                    tasksRepository.activateTask(task.id)
                }
            }
        }
    }

    fun changeFilterState() {
        _currentFiltering.value?.let {
            _currentFiltering.postValue(!it)
        }
    }

    /**
     * [TaskDaoEntity.hasRecentChanges]
     *
     * if(hasRecentChanges == true) {
     * task status is changing in current session or
     * after the last change of the variable [_currentFiltering]
     * }
     *
     * this field need to prevent disappearing items from list
     * when their status ([Task.isActive]) is changing
     *
     * [saveLastChanges]
     * set false to hasRecentChanges for all Tasks
     */

    fun saveLastChanges() {
        coroutineScope.launch {
            tasksRepository.updateRecentChangesStatus()
        }
    }

    fun downloadTasks() {
        viewModelScope.launch {
            when (val resp = tasksRepository.downloadTasks()) {
                is BaseResult.Error -> {
                    when (resp) {
                        BaseResult.Error.OtherErrors -> _downloadState.postValue(R.string.error_others)
                        BaseResult.Error.UnknownHost -> _downloadState.postValue(R.string.error_unknown_host)
                    }
                }
                is BaseResult.Success -> _downloadState.postValue(0)
            }
        }
    }

    /**
     * [markAsDelete]
     * task mark as deleted in DB
     * Saving [Task] in [_removedTask] need for restore Task call fun [cancelDelete]
     *
     * [_removedTask] for showing snackbar
     */

    fun markAsDelete(position: Int) {
        viewModelScope.launch {
            val task = itemsMediatorLiveData.value?.get(position)
            task?.let {
                _removedTask.postValue(task)
                tasksRepository.markAsDeleted(task.id)
            }
        }
    }

    fun cancelDelete() {
        viewModelScope.launch {
            _removedTask.value?.id?.let {
                tasksRepository.unMarkAsDeleted(it)
            }
            _removedTask.postValue(null)
        }
    }

    /**
     * duplicate check value of [_currentFiltering]
     */

    fun getTasks() {
        viewModelScope.launch {
            if (_currentFiltering.value == true) {
                itemsMediatorLiveData.addSource(tasksRepository.getActiveTasks()) {
                    if (_currentFiltering.value == true)
                        itemsMediatorLiveData.postValue(it)
                }
            } else {
                itemsMediatorLiveData.addSource(tasksRepository.getAllTasks()) {
                    if (_currentFiltering.value == false)
                        itemsMediatorLiveData.postValue(it)
                }
            }
        }
    }
}