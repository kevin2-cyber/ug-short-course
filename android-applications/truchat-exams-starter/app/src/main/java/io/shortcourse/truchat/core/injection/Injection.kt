package io.shortcourse.truchat.core.injection

import io.shortcourse.truchat.core.datasource.TruChatDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val roomModule = module {
    single { TruChatDatabase.get(androidContext()).dao() }
}