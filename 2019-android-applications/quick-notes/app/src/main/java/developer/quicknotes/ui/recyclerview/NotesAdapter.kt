package developer.quicknotes.ui.recyclerview

import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.quicknotes.R
import developer.quicknotes.data.Note
import developer.quicknotes.ui.DetailsActivity
import kotlinx.android.synthetic.main.item_note.view.*

class NotesAdapter constructor(private val context: Context) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    private val dataset = mutableListOf<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_note,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        // Get each note from the list
        val note = dataset[position]

        // Bind the properties of the note
        holder.v.note_message.text = note.message.trim()
        holder.v.note_timestamp.text = DateUtils.getRelativeTimeSpanString(note.timestamp,System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS)

        holder.v.setOnClickListener {
            context.startActivity(Intent(context, DetailsActivity::class.java).apply {
                putExtra(DetailsActivity.EXTRA_NOTE_ID, note.id)
            })
        }
    }

    /**
     * Add data to the adapter
     */
    fun addNotesToAdapter(notes: MutableList<Note>) {
        this.dataset.clear()
        this.dataset.addAll(notes)
        notifyDataSetChanged()
    }

    /**
     * ViewHolder class
     */
    class NoteViewHolder constructor(val v: View) : RecyclerView.ViewHolder(v)
}