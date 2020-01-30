package dev.csshortcourse.assignmenttwo.datasource

import dev.csshortcourse.assignmenttwo.model.Chat
import java.util.*

object FakeAPI {

    fun loadFakeResponse(sender: String, recipient: String): MutableList<Chat> {
        return mutableListOf<Chat>().apply {
            for (i in 0 until 5) {
                val chat =
                    Chat(UUID.randomUUID().toString(), recipient, sender, "Some cool response")
                this@apply.add(chat)
            }
        }
    }
}