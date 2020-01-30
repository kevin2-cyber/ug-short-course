package developer.quicknotes.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import developer.quicknotes.R
import developer.quicknotes.core.NoteDAO
import developer.quicknotes.core.NoteDatabase
import developer.quicknotes.core.UserPreferences
import developer.quicknotes.ui.recyclerview.NotesAdapter
import kotlinx.android.synthetic.main.activity_notes.*

class NotesActivity : AppCompatActivity() {
    //    private lateinit var dao: NoteDAO
    private val dao: NoteDAO by lazy { NoteDatabase.getDatabaseInstance(this).dao() }
    private val prefs: UserPreferences by lazy { UserPreferences(this) }

    // Setup recyclerview
    private val adapter: NotesAdapter by lazy { NotesAdapter(this) }
    private val lm: RecyclerView.LayoutManager by lazy { LinearLayoutManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        add_new_note_button.setOnClickListener {
            startActivity(Intent(this@NotesActivity, AddNoteActivity::class.java))
        }

        // Change the text of the header username
        header_username.text = prefs.getUsername()

        // Setup recyclerview
        notes_list.apply {
            layoutManager = lm
            setHasFixedSize(true)
            adapter = this@NotesActivity.adapter
            addItemDecoration(
                DividerItemDecoration(
                    this@NotesActivity,
                    LinearLayoutManager.VERTICAL
                )
            )
            itemAnimator = DefaultItemAnimator()
        }


//        dao = NoteDatabase.getDatabaseInstance(this).dao()

        // Make live observation from here
        dao.readNotes().observe(this@NotesActivity, Observer { notes ->
            adapter.addNotesToAdapter(notes)
        })
    }

}
