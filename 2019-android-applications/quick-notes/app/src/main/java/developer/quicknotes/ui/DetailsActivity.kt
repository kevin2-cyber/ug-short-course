package developer.quicknotes.ui

import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import developer.quicknotes.R
import developer.quicknotes.core.NoteDAO
import developer.quicknotes.core.NoteDatabase
import developer.quicknotes.data.Note
import developer.quicknotes.extensions.print
import developer.quicknotes.extensions.toast
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailsActivity : AppCompatActivity() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)
    private val dao: NoteDAO by lazy { NoteDatabase.getDatabaseInstance(this).dao() }
    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Get the note's id from here
        if (intent.hasExtra(EXTRA_NOTE_ID)) {
            val noteId = intent.getIntExtra(EXTRA_NOTE_ID, 0)
            setupNote(noteId)
        }

        // Attach menu to toolbar
        setSupportActionBar(toolbar)

        // For the arrow on the toolbar
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupNote(noteId: Int) {
        dao.readSingleNote(noteId).observe(this, Observer { note ->
            note.print()
            this@DetailsActivity.note = note

            if (note != null) {
                // Bind properties
                note_message.text = note.message.trim()
                note_timestamp.text = DateUtils.getRelativeTimeSpanString(
                    note.timestamp, System.currentTimeMillis(),
                    DateUtils.SECOND_IN_MILLIS
                )
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Delete note
            R.id.menu_delete_note -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Confirm deletion").setMessage("Do you wish to delete this note?")
                    .setPositiveButton("Yes") { dialogInterface, i ->
                        dialogInterface.dismiss()
                        ioScope.launch {
                            dao.deleteNote(note)
                        }
                        toast("Note has been deleted successfully")
                        finishAfterTransition()
                    }.setNegativeButton("No") { dialogInterface, i ->
                        dialogInterface.dismiss()
                    }
                    .create().show()
            }

            //TODO: Edit note
            R.id.menu_edit_note -> {
                toast("Edit note")
            }

            R.id.menu_share_note -> {
                // Send note details to another application
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, note?.message)
                    type = "text/plain"
                }
                startActivity(sendIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        const val EXTRA_NOTE_ID = "EXTRA_NOTE_ID"
    }
}
