package com.yandex.todo.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import com.yandex.todo.App
import com.yandex.todo.MainActivity
import com.yandex.todo.R
import com.yandex.todo.common.injectViewModel
import com.yandex.todo.data.api.model.Task
import com.yandex.todo.ui.addtask.AddTaskFragment
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


class HomeFragment : Fragment(), RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, OnTaskClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var adapter: HomeAdapter? = null
    private lateinit var viewModel: HomeViewModel
    private val array = arrayListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getComponent().inject(this)
        viewModel = injectViewModel(viewModelFactory)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false).apply { }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        array.clear()
        for (i in 0..10) {
            array.add(Task(i.toString(), "", "Задача №$i", ""))
        }
        for (i in 11..20) {
            array.add(Task(i.toString(), "$i июл. 2021", "Задача №$i", "Низкий"))
        }

        anim_toolbar.setTitle(R.string.my_tasks)
        anim_toolbar.setTitleTextColor(Color.BLACK)
        (activity as? AppCompatActivity)!!.setSupportActionBar(anim_toolbar)

        floating_action_button.setOnClickListener {
            (activity as MainActivity).openFragmentWithBackStack(AddTaskFragment())
        }

        val mLayoutManager = LinearLayoutManager(requireContext())
        scrollableview.layoutManager = mLayoutManager
        scrollableview.itemAnimator = DefaultItemAnimator()
        scrollableview.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        adapter = HomeAdapter(array, this)
        scrollableview.adapter = adapter

        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
            RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(scrollableview)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is HomeAdapter.MyViewHolder) {
            val id: String = array[viewHolder.adapterPosition].id
            val deletedItem: Task = array[viewHolder.adapterPosition]
            val deletedIndex = viewHolder.adapterPosition
            adapter?.removeItem(viewHolder.adapterPosition)
            val snackbar = Snackbar
                .make(coordinator_layout, "Визуальное удаление Задачи №$id", Snackbar.LENGTH_LONG)
            snackbar.setAction("Отменить") {
                adapter?.restoreItem(deletedItem, deletedIndex)
            }
            snackbar.setActionTextColor(resources.getColor(R.color.blue, null))
            snackbar.show()
        }
    }

    override fun onTaskClickListener(task: Task) {
        val arguments = Bundle()
        arguments.putSerializable(Task.TASK, task)
        (activity as MainActivity).openFragmentWithBackStack(AddTaskFragment(), arguments)
    }
}