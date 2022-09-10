package com.jetsada.firebasemvvmapplication.ui.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jetsada.firebasemvvmapplication.data.model.Task
import com.jetsada.firebasemvvmapplication.databinding.ItemTaskLayoutBinding

class TaskListingAdapter(
    val onDeleteClicked: ((Int, Task) -> Unit)? = null,
) : RecyclerView.Adapter<TaskListingAdapter.MyViewHolder>() {

    private var list: MutableList<Task> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemTaskLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item,position)
    }

    fun updateList(list: MutableList<Task>){
        this.list = list
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        list.removeAt(position)
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: ItemTaskLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Task,position: Int) {
            binding.title.setText(item.description)
            binding.delete.setOnClickListener {
                onDeleteClicked?.invoke(position,item)
            }
        }
    }
}