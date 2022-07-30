package com.jetsada.firebasemvvmapplication.ui.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jetsada.firebasemvvmapplication.databinding.ItemNoteListLayoutBinding
import com.jetsada.firebasemvvmapplication.data.model.Note
import com.jetsada.firebasemvvmapplication.util.addChip
import com.jetsada.firebasemvvmapplication.util.hide
import java.text.SimpleDateFormat

class NoteListAdapter(
    val onItemClicked: (Int, Note) -> Unit,
    val onEditClicked: (Int, Note) -> Unit,
    val onDeleteClicked: (Int,Note) -> Unit
) : RecyclerView.Adapter<NoteListAdapter.MyViewHolder>() {

    val sdf = SimpleDateFormat("dd MMM yyyy")
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
            binding.title.setText(item.title)
            binding.date.setText(sdf.format(item.date))
            binding.tags.apply {
                if (item.tags.isNullOrEmpty()){
                    hide()
                }else {
                    removeAllViews()
                    if (item.tags.size > 2) {
                        item.tags.subList(0, 2).forEach { tag -> addChip(tag) }
                        addChip("+${item.tags.size - 2}")
                    } else {
                        item.tags.forEach { tag -> addChip(tag) }
                    }
                }
            }
            binding.desc.apply {
                if (item.description.length > 120){
                    text = "${item.description.substring(0,120)}..."
                }else{
                    text = item.description
                }
            }
            binding.itemLayout.setOnClickListener { onItemClicked.invoke(adapterPosition,item) }
        }
    }
}