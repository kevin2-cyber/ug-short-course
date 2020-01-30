package io.codelabs.ugcloudchat.view.adapter

import io.codelabs.ugcloudchat.model.WhatsappUser

interface OnChatItemClickListener {

    fun onChatClick(user: WhatsappUser, id: Long)
}