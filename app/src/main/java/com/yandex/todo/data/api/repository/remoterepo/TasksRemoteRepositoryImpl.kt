package com.yandex.todo.data.api.repository.remoterepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yandex.todo.data.api.ApiService
import com.yandex.todo.data.api.model.task.Task
import javax.inject.Inject

class TasksRemoteRepositoryImpl @Inject constructor(
    private val api: ApiService,
): TasksRemoteRepository {
    private val tasksInternal = MutableLiveData<List<Task>>()
//    override val tasks: LiveData<List<Task>>
//        get() = tasksInternal

//    override suspend fun loadTasks(): LiveData<BaseResult> {



//        val lv = MutableLiveData<BaseResponse>()
//        try {
//            val response = api.getTasks()
//            if (response.isSuccessful && response.body() != null) {
//                tasksInternal.postValue(response.body())
//                lv.postValue(BaseResponse.Success)
//                return lv
//            }
//        } catch (e: UnknownHostException) {
//            lv.postValue(BaseResponse.Failure.UnknownHostException)
//        } catch (e: Exception) {
//            lv.postValue(BaseResponse.Failure.OtherError)
//            return lv
//        }
//        return lv
//    }
}