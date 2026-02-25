package com.example.perfectnotes.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.perfectnotes.databinding.ItemNoteGridBinding
import com.example.perfectnotes.databinding.ItemNoteListBinding
import com.example.perfectnotes.domain.model.Note
import com.example.perfectnotes.presentation.viewmodel.MainViewModel

class NoteAdapter(
    private val viewMode: MainViewModel.ViewMode,
    private val onItemClick: (Note) -> Unit,
    private val onPinClick: (Note) -> Unit,
    private val onArchiveClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit
) : ListAdapter<Note, RecyclerView.ViewHolder>(NoteDiffCallback()) {
    
    override fun getItemViewType(position: Int): Int {
        return when (viewMode) {
            MainViewModel.ViewMode.GRID -> VIEW_TYPE_GRID
            MainViewModel.ViewMode.LIST -> VIEW_TYPE_LIST
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_GRID -> {
                val binding = ItemNoteGridBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                GridViewHolder(binding, onItemClick, onPinClick, onArchiveClick, onDeleteClick)
            }
            VIEW_TYPE_LIST -> {
                val binding = ItemNoteListBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ListViewHolder(binding, onItemClick, onPinClick, onArchiveClick, onDeleteClick)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }
    
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val note = getItem(position)
        when (holder) {
            is GridViewHolder -> holder.bind(note)
            is ListViewHolder -> holder.bind(note)
        }
    }
    
    class GridViewHolder(
        private val binding: ItemNoteGridBinding,
        private val onItemClick: (Note) -> Unit,
        private val onPinClick: (Note) -> Unit,
        private val onArchiveClick: (Note) -> Unit,
        private val onDeleteClick: (Note) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(note: Note) {
            binding.apply {
                tvTitle.text = note.title
                tvContent.text = note.previewContent
                tvDate.text = note.formattedDate
                tvCategory.text = note.category
                
                ivPin.visibility = if (note.isPinned) android.view.View.VISIBLE else android.view.View.GONE
                
                root.setCardBackgroundColor(note.color)
                
                root.setOnClickListener { onItemClick(note) }
                root.setOnLongClickListener {
                    showContextMenu(note)
                    true
                }
            }
        }
        
        private fun showContextMenu(note: Note) {
            android.widget.PopupMenu(binding.root.context, binding.root).apply {
                menu.add(0, 1, 0, if (note.isPinned) "Lepas Pin" else "Pin")
                menu.add(0, 2, 1, if (note.isArchived) "Aktifkan" else "Arsipkan")
                menu.add(0, 3, 2, "Hapus")
                
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        1 -> onPinClick(note)
                        2 -> onArchiveClick(note)
                        3 -> onDeleteClick(note)
                    }
                    true
                }
                show()
            }
        }
    }
    
    class ListViewHolder(
        private val binding: ItemNoteListBinding,
        private val onItemClick: (Note) -> Unit,
        private val onPinClick: (Note) -> Unit,
        private val onArchiveClick: (Note) -> Unit,
        private val onDeleteClick: (Note) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(note: Note) {
            binding.apply {
                tvTitle.text = note.title
                tvContent.text = note.previewContent
                tvDate.text = note.formattedDate
                tvCategory.text = note.category
                
                ivPin.visibility = if (note.isPinned) android.view.View.VISIBLE else android.view.View.GONE
                
                root.setCardBackgroundColor(note.color)
                
                root.setOnClickListener { onItemClick(note) }
                root.setOnLongClickListener {
                    showContextMenu(note)
                    true
                }
            }
        }
        
        private fun showContextMenu(note: Note) {
            android.widget.PopupMenu(binding.root.context, binding.root).apply {
                menu.add(0, 1, 0, if (note.isPinned) "Lepas Pin" else "Pin")
                menu.add(0, 2, 1, if (note.isArchived) "Aktifkan" else "Arsipkan")
                menu.add(0, 3, 2, "Hapus")
                
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        1 -> onPinClick(note)
                        2 -> onArchiveClick(note)
                        3 -> onDeleteClick(note)
                    }
                    true
                }
                show()
            }
        }
    }
    
    class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
    
    companion object {
        private const val VIEW_TYPE_GRID = 1
        private const val VIEW_TYPE_LIST = 2
    }
}