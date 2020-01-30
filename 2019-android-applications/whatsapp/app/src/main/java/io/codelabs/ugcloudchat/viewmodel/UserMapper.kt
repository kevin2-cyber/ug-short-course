package io.codelabs.ugcloudchat.viewmodel

/**
 * Custom mapper class to map any class[IN] to [OUT]
 */
interface UserMapper<OUT, IN> {

    fun map(input: IN): OUT
}