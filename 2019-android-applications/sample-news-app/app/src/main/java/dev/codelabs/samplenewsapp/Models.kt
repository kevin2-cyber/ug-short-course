package dev.codelabs.samplenewsapp

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Models response from server
 */
@Parcelize
data class ApiResponse(
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("totalResults")
    @Expose
    val results: Int,
    @SerializedName("articles")
    @Expose
    val articles: MutableList<NewsArticle>
) : Parcelable

/**
 * News Article data model
 */
@Parcelize
data class NewsArticle(
    @SerializedName("author")
    @Expose
    val author: String?,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("description")
    @Expose
    val desc: String,
    @SerializedName("url")
    @Expose
    val url: String,
    @SerializedName("urlToImage")
    @Expose
    val image: String?,
    @SerializedName("content")
    @Expose
    val content: String
) : Parcelable