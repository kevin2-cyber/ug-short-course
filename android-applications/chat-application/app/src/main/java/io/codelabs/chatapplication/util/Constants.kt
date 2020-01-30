package io.codelabs.chatapplication.util

import io.codelabs.chatapplication.BuildConfig

private const val RELEASE_KEY_SHA_1 = "66:6B:CC:AD:00:C7:AC:D4:5B:73:BB:17:64:3D:4A:0F:EF:4B:41:20"
private const val RELEASE_KEY_SHA_256 =
    "59:46:D1:36:44:12:DE:9E:C2:3B:0C:98:8A:CB:8B:E5:4B:2B:7F:CC:07:A6:77:3B:3A:3F:75:A2:82:3B:B6:F1"

private const val DEBUG_KEY_SHA_1 = "70:0D:12:15:53:4A:2C:AE:5C:B6:E1:B8:7E:27:D3:8E:59:FF:A6:CE"
private const val DEBUG_KEY_SHA_256 =
    "16:38:DA:45:F9:E5:46:B1:4B:D4:E5:4E:B9:2B:27:2F:0A:B0:1B:18:15:21:65:B4:57:10:B4:67:90:2C:76:D7"

val WEB_CLIENT_ID: String =
    /*if (!BuildConfig.DEBUG)*/ "686634584869-pq9kvgs0f27l3hl109ss79mn3921r8a7.apps.googleusercontent.com"/* else "686634584869-jb2rkld5k30gqt17s8pmae1td4ubl27v.apps.googleusercontent.com"*/

const val USER_REF = "chat-users"
const val USER_CHATS_REF = "chat-users/%s/chats"
const val USER_CHATS_DOC_REF = "chat-users/%s/chats/%s"
const val USER_MESSAGES_DOC_REF = "chat-users/%s/chats/%s/messages"
const val USER_DOC_REF = "chat-users/%s"



