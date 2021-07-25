package com.yandex.todo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.yandex.todo.App
import com.yandex.todo.R
import com.yandex.todo.data.model.task.Task
import com.yandex.todo.databinding.FragmentHomeBinding
import com.yandex.todo.ui.home.adapter.HomeAdapter
import com.yandex.todo.ui.home.adapter.swipe.SwipeToDeleteCallback
import com.yandex.todo.ui.home.adapter.swipe.SwipeToDoneCallback
import com.yandex.todo.util.observe
import java.util.*
import javax.inject.Inject

class HomeFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<HomeViewModel> { viewModelFactory }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).getComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListAdapter()

        setFilterButtonOptions()

        binding.floatingActionButton.setOnClickListener { openTaskDetails() }

        binding.animToolbar.setOnClickListener { binding.mainRecyclerView.smoothScrollToPosition(0) }

        binding.swipeToRefreshLayout.setOnRefreshListener {
            binding.swipeToRefreshLayout.isRefreshing = true
            viewModel.downloadTasks()
        }

        observe(viewModel.itemsMediatorLiveData) {
            if (it.isNotEmpty()) {
                adapter.currentDate = Date()
                adapter.submitList(it)
            }
        }

        observe(viewModel.removedTask) {
            if (it != null) showSnackBarForCancel()
        }

        observe(viewModel.downloadState) {
            binding.swipeToRefreshLayout.isRefreshing = false
            if (it != 0) {
                Toast.makeText(
                    requireContext(),
                    requireContext().resources.getString(it),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupListAdapter() {
        val onTaskClickListener: (Task) -> Unit = { openTaskDetails(it.id) }
        val onStateChangeListener: (Int) -> Unit = { viewModel.changeTaskState(it) }

        adapter = HomeAdapter(onTaskClickListener, onStateChangeListener)
        binding.mainRecyclerView.adapter = adapter
        setSwipeHandlers()
    }

    private fun setFilterButtonOptions() {
        binding.filterButton.setOnClickListener {
            viewModel.saveLastChanges()
            viewModel.changeFilterState()
        }
        observe(viewModel.currentFiltering) {
            binding.filterButton.isActivated = it
            viewModel.getTasks()
        }
    }

    private fun openTaskDetails(taskId: String? = null) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToAddEditTaskFragment(taskId)
        )
    }

    private fun setSwipeHandlers() {
        val swipeLeftHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.markAsDelete(viewHolder.adapterPosition)
            }
        }

        val swipeRightHandler = object : SwipeToDoneCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.changeTaskState(viewHolder.adapterPosition)
            }
        }
        val itemTouch = ItemTouchHelper(swipeLeftHandler)
        val itemTouch2 = ItemTouchHelper(swipeRightHandler)
        itemTouch2.attachToRecyclerView(binding.mainRecyclerView)
        itemTouch.attachToRecyclerView(binding.mainRecyclerView)
    }

    private fun showSnackBarForCancel() {
        Snackbar
            .make(binding.root, getString(R.string.task_deleted), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.cancel)) {
                viewModel.cancelDelete()
            }
            .setActionTextColor(requireContext().resources.getColor(R.color.blue, null))
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.saveLastChanges()
        _binding = null
    }
}