package io.shortcourse.truchat.core.view

import io.codelabs.sdk.view.BaseActivity
import io.shortcourse.truchat.core.TruChatApplication
import io.shortcourse.truchat.core.datasource.TruChatDao
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

abstract class RootActivity : BaseActivity() {

    val dao: TruChatDao by inject { parametersOf(application as TruChatApplication) }

}