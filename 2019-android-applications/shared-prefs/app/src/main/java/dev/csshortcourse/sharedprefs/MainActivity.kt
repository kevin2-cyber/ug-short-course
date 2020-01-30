package dev.csshortcourse.sharedprefs

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val prefs by lazy { MySharedPrefs(this@MainActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        save_prefs.setOnClickListener { save() }
        retrieve_prefs.setOnClickListener { retrieve() }
        clear_prefs.setOnClickListener { clear() }

    }

    private fun save() {
        val item = my_prefs_item.text.toString()
        if (item.isEmpty()) {
            Toast.makeText(this, "Cannot save empty field", Toast.LENGTH_SHORT).show()
        } else {
            prefs.save(item)
            my_prefs_item.text?.clear()
        }
    }

    private fun retrieve() {
        my_prefs_value.text = prefs.retrieve()
    }

    private fun clear() = prefs.clear()
}
