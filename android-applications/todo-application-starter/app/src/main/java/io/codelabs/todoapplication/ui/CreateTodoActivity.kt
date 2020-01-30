package io.codelabs.todoapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.codelabs.todoapplication.R

class CreateTodoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_todo)
    }
}
