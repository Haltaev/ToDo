package com.yandex.todo.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import com.yandex.todo.App
import com.yandex.todo.R
import com.yandex.todo.common.injectViewModel
import com.yandex.todo.data.api.model.Task
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


class HomeFragment : Fragment(), RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
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
        for (i in 0..20) {
            array.add(Task(i.toString(), "", "Задача №$i", ""))
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
        adapter = HomeAdapter(array, requireContext())
        scrollableview.adapter = adapter

        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
            RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        val itemTouchHelperToRightCallback: ItemTouchHelper.SimpleCallback =
            RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(scrollableview)
        ItemTouchHelper(itemTouchHelperToRightCallback).attachToRecyclerView(scrollableview)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is HomeAdapter.MyViewHolder) {
            if(direction == ItemTouchHelper.LEFT) {
                viewHolder.viewBackgroundGreen.visibility = View.GONE
                val id: String = array[viewHolder.adapterPosition].id
                val deletedItem: Task = array[viewHolder.adapterPosition]
                val deletedIndex = viewHolder.adapterPosition
                adapter?.removeItem(viewHolder.adapterPosition)
                val snackbar = Snackbar
                    .make(coordinator_layout, "$id removed from cart!", Snackbar.LENGTH_LONG)
                snackbar.setAction("UNDO") {
                    adapter?.restoreItem(deletedItem, deletedIndex)
                }
                snackbar.setActionTextColor(Color.BLUE)
                snackbar.show()
            } else {
                viewHolder.viewBackground.visibility = View.GONE
                val id: String = array[viewHolder.adapterPosition].id
                val deletedItem: Task = array[viewHolder.adapterPosition]
                val deletedIndex = viewHolder.adapterPosition
                adapter?.removeItem(viewHolder.adapterPosition)
                val snackbar = Snackbar
                    .make(coordinator_layout, "$id removed from cart!", Snackbar.LENGTH_LONG)
                snackbar.setAction("UNDO") {
                    adapter?.restoreItem(deletedItem, deletedIndex)
                }
                snackbar.setActionTextColor(Color.BLUE)
                snackbar.show()
            }
        }
    }
}