/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package io.codelabs.githubrepo.shared.repository

import io.codelabs.githubrepo.shared.data.Repo
import io.codelabs.githubrepo.shared.datasource.DataSource
import io.codelabs.githubrepo.shared.datasource.local.dao.RepoDao
import io.codelabs.githubrepo.shared.datasource.remote.GitHubService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for calls to remote or local datasource
 */
class GitHubRepository(private val gitHubService: GitHubService, private val dao: RepoDao) :
    DataSource {

    // Makes call to server for repos
    override suspend fun getAllRepos(refresh: Boolean): MutableList<Repo> =
        withContext(Dispatchers.IO) {
            if (refresh) gitHubService.getAllReposAsync().await().apply {
                // Add results to local data source
                dao.insertAll(this)
            } else dao.getAllRepos()
        }

    override suspend fun requestUserIdentity() = gitHubService.getUserAsync().await()
}