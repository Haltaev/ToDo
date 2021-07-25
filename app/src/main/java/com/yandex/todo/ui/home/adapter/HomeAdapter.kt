package com.yandex.todo.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yandex.todo.R
import com.yandex.todo.data.model.task.Importance
import com.yandex.todo.data.model.task.Task
import com.yandex.todo.databinding.ItemTaskBinding
import com.yandex.todo.util.changeContextState
import com.yandex.todo.util.toSimpleString
import java.util.*

class HomeAdapter(
    private val onTaskClickListener: (Task) -> Unit,
    private val onStateChangeListener: (Int) -> Unit,
    var currentDate: Date = Date(),
) : ListAdapter<Task, HomeAdapter.ViewHolder>(TaskDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item, currentDate)
    }

    class ViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Task, date: Date) {
            binding.contentTextView.text = item.content
            binding.checkbox.isChecked = !item.isActive
            binding.contentTextView.changeContextState(!item.isActive)
            when (item.importance) {
                Importance.BASIC -> {
                }
                Importance.LOW -> {
                    binding.contentTextView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_arrow_down,
                        0,
                        0,
                        0
                    )
                }
                Importance.IMPORTANT -> {
                    binding.contentTextView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_double_exclamation_point,
                        0,
                        0,
                        0
                    )
                }
            }
            if (item.deadline.time > 0L) {
                binding.contentTextView.maxLines = 2
                binding.deadlineTextView.text = item.deadline.toSimpleString()
            } else {
                binding.contentTextView.maxLines = 3
                binding.deadlineTextView.visibility = View.GONE
            }
            if (item.isActive) {
                binding.deadlineTextView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        if (item.deadline.after(date)) R.color.main_text_color
                        else R.color.red_delete
                    )
                )
            } else {
                binding.deadlineTextView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.gray
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(layoutInflater, parent, false)
        val holder = ViewHolder(binding)
        binding.checkbox.setOnClickListener {
            onStateChangeListener.invoke(holder.adapterPosition)
        }
        binding.itemLayout.setOnClickListener {
            onTaskClickListener.invoke(getItem(holder.adapterPosition))
        }
        return holder
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}
