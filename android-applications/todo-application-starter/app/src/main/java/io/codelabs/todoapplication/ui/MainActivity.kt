package io.codelabs.todoapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.codelabs.todoapplication.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }

    fun addNewTodo(v: View?) {
        // Create an explicit intent to the CreateActivity class
        // using Activity#startActivity() method
        val intent = Intent(this, CreateTodoActivity::class.java)
        startActivity(intent)
    }


}
