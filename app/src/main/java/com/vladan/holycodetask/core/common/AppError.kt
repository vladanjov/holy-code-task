package com.vladan.holycodetask.core.common

import java.io.IOException

sealed interface AppError {
    data object NetworkError : AppError
    data class HttpError(val code: Int, val message: String?) : AppError
    data class Unknown(val message: String?) : AppError
}

fun Throwable.toAppError(): AppError = when (this) {
    is IOException -> AppError.NetworkError
    is retrofit2.HttpException -> AppError.HttpError(code(), message())
    else -> AppError.Unknown(message)
}
