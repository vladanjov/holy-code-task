package com.vladan.holycodetask.core.common

sealed interface Resource<out T> {
    data object Loading : Resource<Nothing>
    data class Success<T>(val data: T) : Resource<T>
    data class Error<T>(val error: AppError, val data: T? = null) : Resource<T>
}
