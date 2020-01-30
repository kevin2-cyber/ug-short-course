package dev.csshortcourse.ugnews.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.csshortcourse.ugnews.databinding.ActivityMainBinding
import dev.csshortcourse.ugnews.util.moveTo

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigate to news page
        binding.startReading.setOnClickListener { moveTo(NewsActivity::class.java, true) }
    }
}
