package com.example.roomcoroutines.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.roomcoroutines.R
import com.example.roomcoroutines.databinding.RecyclerviewItemBinding
import com.example.roomcoroutines.db.Note

class NoteListAdapter(
    context: Context,
    private val myInterface: ClickInterface
) :
    RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>() {

    private var notes = emptyList<Note>()
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemBinding: RecyclerviewItemBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.recyclerview_item,
            parent,
            false
        )
        return NoteViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        // we are getting list note from observe method{Livedata}or data base
        val current = notes[position]
        holder.binding.myNote = current
        holder.noteId = current.id
    }

    fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        myInterface.onSwipe(notes[position])
    }

    inner class NoteViewHolder(
        val binding: RecyclerviewItemBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        var noteId = 0

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(p0: View?) =
            myInterface.onNoteClick(
                Note(noteId, binding.myNote!!.title, binding.myNote!!.description)
            )

    }

}

interface ClickInterface {
    fun onNoteClick(note: Note)
    fun onSwipe(note: Note)
}