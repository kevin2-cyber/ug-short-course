package shortcourse.legere

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_main.*
import shortcourse.legere.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Old way
        // findViewById<MaterialButton>(R.id.get_started)

        // Another way
        // get_started.setOnClickListener { }

        binding.getStarted.setOnClickListener {
            // Intent
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}