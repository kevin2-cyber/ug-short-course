/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package io.codelabs.githubrepo.shared.util

/**
 * Constants
 */
object Constants {
    const val MAX_CACHE_SIZE = 10.times(1024).times(1024).toLong()
    const val DB_VER = 1
    const val APP_PREFS = "gitrepo_prefs"
    const val APP_DB = "gitrepo.db"
    const val CACHE_DIR = "gitrepo.cache"
}