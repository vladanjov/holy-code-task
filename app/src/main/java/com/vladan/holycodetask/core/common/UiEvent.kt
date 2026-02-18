package com.vladan.holycodetask.core.common

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
}

fun AppError.toUiEvent(): UiEvent? = when (this) {
    is AppError.HttpError -> UiEvent.ShowSnackbar("Server error: $code")
    is AppError.Unknown -> UiEvent.ShowSnackbar(message ?: "An unknown error occurred")
    else -> null
}
