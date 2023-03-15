package com.uragiristereo.mejiboard.core.model.result

sealed interface Result<T> {
    data class Success<T>(val data: T) : Result<T>

    data class Failed<T>(val message: String) : Result<T>

    data class Error<T>(val t: Throwable) : Result<T>
}
