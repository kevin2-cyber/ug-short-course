package io.codelabs.ugcloudchat.viewmodel

import io.codelabs.ugcloudchat.model.LocalContact
import io.codelabs.ugcloudchat.model.WhatsappUser

class ContactUserMapper : UserMapper<WhatsappUser, LocalContact> {

    override fun map(input: LocalContact): WhatsappUser {
        return WhatsappUser(
            input.id,
            "",
            input.phone,
            input.displayName,
            input.photoUri,
            input.thumbNailUri
        )
    }
}