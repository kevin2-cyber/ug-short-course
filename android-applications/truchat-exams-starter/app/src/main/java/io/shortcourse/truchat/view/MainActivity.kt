package io.shortcourse.truchat.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.toast
import io.shortcourse.truchat.R
import io.shortcourse.truchat.core.view.RootActivity
import io.shortcourse.truchat.databinding.ActivityMainBinding
import io.shortcourse.truchat.model.Room
import io.shortcourse.truchat.util.InputValidator
import kotlinx.coroutines.launch

class MainActivity : RootActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)

        dao.getRooms().observe(this@MainActivity, Observer {
            debugLog(it)
            if (it != null && it.isNotEmpty()) {
                val rooms = mutableListOf<String>()
                for (room in it) {
                    rooms.add(room.name)
                }

                val adapter: ArrayAdapter<String> = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, rooms)
                binding.roomName.setAdapter(adapter).also {
                    binding.roomName.showDropDown()
                }
            }
        })
    }

    fun joinRoom(v: View?) {
        if (InputValidator.validate(binding.roomName)) {
            ioScope.launch {
                val room = Room(name = binding.roomName.text.toString())
                val existingRoom = dao.getRoom(room.name)

                if (existingRoom == null) {
                    Snackbar.make(binding.container, "Room does not exist. Add new?", Snackbar.LENGTH_LONG)
                            .setAction("Create room") {
                                dao.addRoom(room)

                                uiScope.launch {
                                    toast("Settings up...")
                                    binding.room = room
                                }
                            }.show()
                } else {
                    binding.room = room
                    startActivity(Intent(this@MainActivity, UsersActivity::class.java).apply {
                        putExtra(UsersActivity.EXTRA_ROOM, binding.room)
                    })
                    finishAfterTransition()
                }

            }
        } else toast("Please enter a room name first")
    }
}
