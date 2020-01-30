package developer.quicknotes.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import developer.quicknotes.R
import developer.quicknotes.core.UserPreferences
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val prefs: UserPreferences by lazy { UserPreferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Send the user to the login screen when the Get Started button is clicked
        // We will make use of intents in this case
        get_started_button.setOnClickListener {
            if (prefs.getUsername().isNullOrEmpty()) {
                startActivity(
                    Intent(
                        /*From*/ this@MainActivity,/*To*/ LoginActivity::class.java
                    )
                )
            } else {
                startActivity(
                    Intent(
                        /*From*/ this@MainActivity,/*To*/ NotesActivity::class.java
                    )
                )
            }
            finish()
        }

    }
}
