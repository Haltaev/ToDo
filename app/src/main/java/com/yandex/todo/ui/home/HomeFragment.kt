package com.yandex.todo.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yandex.todo.EventObserver
import com.yandex.todo.R
import com.yandex.todo.data.api.model.task.Importance
import com.yandex.todo.data.api.model.task.Task
import com.yandex.todo.databinding.FragmentHomeBinding
import com.yandex.todo.ui.home.adapter.HomeAdapter
import dagger.android.support.DaggerFragment
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class HomeFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentHomeBinding

    private lateinit var adapter: HomeAdapter

    private val viewModel by viewModels<HomeViewModel> { viewModelFactory }

    private val args: HomeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner
        setupNavigation()
        setupListAdapter()
        setToolbar()
        viewModel.loadTasks(true)
//        setAdapter()
//        binding.floatingActionButton.setOnClickListener {
//            (activity as MainActivity).openFragmentWithBackStack(addEditTaskFragment())
//        }
//        val task = TaskModel("0", "Задача №5", Importance.BASIC, false, 0, 0, 0)
//        val task2 = TaskModel("0", "Задача №5", Importance.BASIC, false, 0, 0, 0)
//        Log.e("test", (task == task).toString())
//        Log.e("test", (task == task2).toString())
    }

    private fun setupNavigation() {
        viewModel.openTaskEvent.observe(viewLifecycleOwner, EventObserver {
            openTaskDetails(it)
        })
        viewModel.newTaskEvent.observe(viewLifecycleOwner, EventObserver {
            navigateToAddNewTask()
        })
    }

    private fun navigateToAddNewTask() {
        val action = HomeFragmentDirections
            .actionHomeFragmentToAddEditTaskFragment(null)
        findNavController().navigate(action)
    }

    private fun openTaskDetails(taskId: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToAddEditTaskFragment(taskId)
        findNavController().navigate(action)
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewmodel
        if (viewModel != null) {
            adapter = HomeAdapter(viewModel)
            binding.homeList.adapter = adapter
        }
    }

//
    private fun setToolbar() {
        binding.animToolbar.apply {
            setTitle(R.string.my_tasks)
            setTitleTextColor(Color.BLACK)
            (activity as? AppCompatActivity)?.setSupportActionBar(this)
        }
    }
//
//    private fun setAdapter() {
//        binding.homeList.apply {
//            val mLayoutManager = LinearLayoutManager(requireContext())
//            layoutManager = mLayoutManager
//            itemAnimator = DefaultItemAnimator()
//            addItemDecoration(
//                DividerItemDecoration(
//                    requireContext(),
//                    DividerItemDecoration.VERTICAL,
//                )
//            )
//            adapter = this@HomeFragment.adapter
//            val swipeLestHandler = object : SwipeToDeleteCallback(requireContext()) {
//                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                    val id = this@HomeFragment.adapter.getItem(viewHolder.adapterPosition).id
////                    viewModel.removeTask(id)
//                }
//            }
//
//            val swipeRightHandler = object : SwipeToDoneCallback(requireContext()) {
//                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                    val id = this@HomeFragment.adapter.getItem(viewHolder.adapterPosition).id
//                    val isDone = this@HomeFragment.adapter.getItem(viewHolder.adapterPosition).done
////                    viewModel.setTaskDone(isDone.not(), id)
//                }
//            }
//
//            val itemTouch = ItemTouchHelper(swipeLestHandler)
//            val itemTouch2 = ItemTouchHelper(swipeRightHandler)
//            itemTouch2.attachToRecyclerView(this)
//            itemTouch.attachToRecyclerView(this)
//        }
//    }

//    fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
//        if (viewHolder is HomeAdapter.MyViewHolder) {
//            viewHolder.adapterPosition.let {
//                val id: String = array[it].id
//                val deletedItem: Task = array[it]
//                val deletedIndex = it
//                adapter.removeItem(it)
//                val snackbar = Snackbar
//                    .make(binding.root, "Визуальное удаление Задачи №$id", Snackbar.LENGTH_LONG)
//                snackbar.setAction("Отменить") {
//                    adapter.restoreItem(deletedItem, deletedIndex)
//                }
//                snackbar.setActionTextColor(resources.getColor(R.color.blue, null))
//                snackbar.show()
//            }
//        }
//    }

//    override fun onTaskClickListener(task: Task) {
//        val arguments = Bundle()
//        arguments.putSerializable(TaskModel.TASK, task)
//        (activity as MainActivity).openFragmentWithBackStack(AddTaskFragment(), arguments)
//    }

//    override fun onCheckBoxChangeStateListener(position: Int, done: Boolean) {
//        val oldList = adapter.getData()
//        val changedItem = oldList[position].copy(done = done)
//        val newList = arrayListOf<Task>()
//        newList.addAll(oldList)
//        newList[position] = changedItem
//        val homeDiffUtilCallback = HomeDiffUtilCallback(oldList, newList)
//        val homeDiffResult = DiffUtil.calculateDiff(homeDiffUtilCallback)
//        adapter.setData(newList)
//        homeDiffResult.dispatchUpdatesTo(adapter)
//    }
}