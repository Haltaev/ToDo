package com.yandex.todo.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yandex.todo.data.api.model.task.Task
import com.yandex.todo.databinding.ItemTaskBinding
import com.yandex.todo.ui.home.HomeViewModel


class HomeAdapter(private val viewModel: HomeViewModel) :
    ListAdapter<Task, HomeAdapter.ViewHolder>(TaskDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    class ViewHolder private constructor(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: HomeViewModel, item: Task) {
            binding.apply {
                viewmodel = viewModel
                task = item
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTaskBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

//    fun removeItem(position: Int) {
//        items.removeAt(position)
//        notifyItemRemoved(position)
//    }
//
//    fun restoreItem(item: Task, position: Int) {
//        items.add(position, item)
//        notifyItemInserted(position)
//    }
//
//    fun setData(newList: ArrayList<Task>) {
//        items = newList
//    }
//
//    fun getData(): ArrayList<Task> = items
//
//    fun getItem(position: Int): Task = items[position]
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}
