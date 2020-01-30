package io.codelabs.ugcloudchat.viewmodel.repository

/**
 * Creates a state for network calls to the database
 */
enum class DatabaseState { LOADING, LOADED, ERROR, UNKNOWN }