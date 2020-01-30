/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package io.codelabs.githubrepo.shared.datasource

import io.codelabs.githubrepo.shared.data.Owner
import io.codelabs.githubrepo.shared.data.Repo

interface DataSource {
    // Required
    // Returns a list of repositories from GitHub
    suspend fun getAllRepos(refresh: Boolean): MutableList<Repo>

    // todo: This is an optional call
    suspend fun requestUserIdentity(): Owner
}