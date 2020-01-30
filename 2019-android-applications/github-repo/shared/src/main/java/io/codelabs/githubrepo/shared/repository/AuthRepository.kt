/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package io.codelabs.githubrepo.shared.repository

import com.google.gson.annotations.SerializedName
import io.codelabs.githubrepo.shared.BuildConfig
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query
import java.util.*

interface AuthRepository {
    companion object {
        const val AUTH_URL = "https://github.com/"
    }

    @GET("/login/oauth/access_token")
    @Headers("Accept: application/json")
    fun getAccessTokenAsync(
        @Query("code") code: String,
        @Query("redirect_uri") redirectUri: String = "gitrepo://callback",
        @Query("state") state: String = UUID.randomUUID().toString(),
        @Query("client_id") clientId: String = BuildConfig.API_KEY,
        @Query("client_secret") clientSecret: String = BuildConfig.API_SECRET
    ): Deferred<AccessToken>
}

data class AccessToken(
    @SerializedName("access_token") val token: String,
    val scope: String,
    @SerializedName("token_type") val tokenType: String
)