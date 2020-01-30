package developer.quicknotes.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import developer.quicknotes.R
import developer.quicknotes.core.UserPreferences
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val prefs: UserPreferences by lazy { UserPreferences(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        save_username.setOnClickListener {
            // Get that text entered in the input layout
            val name = username.text.toString()

            if (name.isNotEmpty()) {
                // Navigate to the Notes Activity
                prefs.addUsername(name)
                startActivity(
                    Intent(
                        this@LoginActivity, NotesActivity::class.java
                    )
                )
                finish()
            } else {
                val text = "Please enter a username first..."
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
//                Snackbar.make(container,text,Snackbar.LENGTH_SHORT).show()
            }


        }
    }
}
