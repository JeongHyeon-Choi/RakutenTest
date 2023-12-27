package com.jhyun.rakuten.ui.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jhyun.rakuten.databinding.ItemNoteBinding

class NoteAdapter(
    private val viewModel: NoteListViewModel,
) : ListAdapter<NoteListData, NoteViewHolder>(object : DiffUtil.ItemCallback<NoteListData>() {
    override fun areItemsTheSame(oldItem: NoteListData, newItem: NoteListData): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: NoteListData, newItem: NoteListData): Boolean {
        return false
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
            binding.viewModel = viewModel
        }
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.binding.data = getItem(position)
        holder.binding.executePendingBindings()
    }

    override fun onViewRecycled(holder: NoteViewHolder) {
        super.onViewRecycled(holder)

    }

}

class NoteViewHolder(binding: ItemNoteBinding) : ViewHolder(binding.root) {
    val binding = binding
}