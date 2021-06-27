package com.yandex.todo.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yandex.todo.R
import com.yandex.todo.data.api.model.Task
import kotlinx.android.synthetic.main.item_task.view.*


class HomeAdapter(
    private val items: ArrayList<Task>,
    private val callback: OnTaskClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.apply {
            name.text = item.description
            item_layout.setOnClickListener {
                callback.onTaskClickListener(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(
            layoutInflater.inflate(R.layout.item_task, parent, false)
        )

    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position)
    }

    fun restoreItem(item: Task, position: Int) {
        items.add(position, item)
        // notify item added by position
        notifyItemInserted(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.name)
        var viewBackground: RelativeLayout = view.findViewById(R.id.view_background)
        var viewForeground: RelativeLayout = view.findViewById(R.id.view_foreground)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}