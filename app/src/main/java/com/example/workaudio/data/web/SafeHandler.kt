package com.example.workaudio.data.web

sealed class SafeHandler<out T> {
    data class Success<out T>(val value: T) : SafeHandler<T>()
    data class GenericError(val code: Int? = null, val error: String? = null) :
        SafeHandler<Nothing>()

    object NetworkError : SafeHandler<Nothing>()
}
