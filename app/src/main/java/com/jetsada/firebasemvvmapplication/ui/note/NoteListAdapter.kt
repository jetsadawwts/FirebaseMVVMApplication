package com.jetsada.firebasemvvmapplication.ui.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jetsada.firebasemvvmapplication.databinding.ItemNoteListLayoutBinding
import com.jetsada.firebasemvvmapplication.model.Note


class NoteListAdapter(
    val onItemClicked: (Int, Note) -> Unit,
    val onEditClicked: (Int, Note) -> Unit,
    val onDeleteClicked: (Int,Note) -> Unit
) : RecyclerView.Adapter<NoteListAdapter.MyViewHolder>() {

    private var list: MutableList<Note> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemNoteListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    fun updateList(list: MutableList<Note>){
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

    inner class MyViewHolder(val binding: ItemNoteListLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Note){
            binding.noteIdValue.text = item.id
            binding.msg.text = item.text
            binding.edit.setOnClickListener { onEditClicked.invoke(adapterPosition, item) }
            binding.delete.setOnClickListener { onDeleteClicked.invoke(adapterPosition, item) }
            binding.itemLayout.setOnClickListener { onItemClicked.invoke(adapterPosition, item) }
        }
    }
}