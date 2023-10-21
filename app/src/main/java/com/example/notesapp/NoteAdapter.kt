package com.example.notesapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.text.SimpleDateFormat
import java.util.*


class NoteAdapter(options: FirestoreRecyclerOptions<Note>, val context : Context) :
    FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder>(options) {

    class NoteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var titleTV : TextView
        var contentTV : TextView
        var timestampTV : TextView
        var cardLL : LinearLayout

        init {
            titleTV = itemView.findViewById(R.id.note_title_text_view)
            contentTV = itemView.findViewById(R.id.note_content_text_view)
            timestampTV = itemView.findViewById(R.id.note_timestamp_text_view)
            cardLL= itemView.findViewById(R.id.cardLL)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.each_note,parent,false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int, note: Note) {
        holder.titleTV.text = note.title
        holder.contentTV.text = note.content
        holder.timestampTV.text = SimpleDateFormat("MM/dd/yyyy").format(note.timestamp.toDate())
        var color = BaseActivity().getRandomColor()
        holder.cardLL.setBackgroundColor(holder.itemView.resources.getColor(color,null))

        holder.itemView.setOnClickListener {
            val intent = Intent(context,NotesCreateActivity::class.java)
            intent.putExtra("title",note.title)
            intent.putExtra("content",note.content)
            val noteID = this.snapshots.getSnapshot(position).id
            intent.putExtra("noteID",noteID)
            context.startActivity(intent)
        }

    }
}