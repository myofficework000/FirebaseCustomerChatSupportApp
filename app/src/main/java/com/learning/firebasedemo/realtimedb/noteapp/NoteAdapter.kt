package com.learning.firebasedemo.realtimedb.noteapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learning.firebasedemo.databinding.ItemListNotesBinding

class NoteAdapter(private val notes: ArrayList<Notes>) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    internal lateinit var binding: ItemListNotesBinding

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemListNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Notes) {
            binding.apply {
                txtDate.text = item.date.replace("/", " ").substring(0, 6)
                txtTitle.text = item.title
                txtDesc.text = item.description
            }
        }
    }
}