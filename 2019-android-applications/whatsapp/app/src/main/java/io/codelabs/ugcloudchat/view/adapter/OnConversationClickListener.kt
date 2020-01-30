package io.codelabs.ugcloudchat.view.adapter

import io.codelabs.ugcloudchat.model.Chat

interface OnConversationClickListener {
    fun onClick(chat: Chat, position: Int)
}