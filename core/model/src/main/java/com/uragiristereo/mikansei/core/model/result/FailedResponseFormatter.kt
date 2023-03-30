package com.uragiristereo.mikansei.core.model.result

fun failedResponseFormatter(
    responseCode: Int,
    errorBody: String?,
): String {
    // TODO: Format response properly

    val body = when {
        errorBody == null -> null
        responseCode == 401 -> "Invalid name/API key."
        else -> errorBody
    }

    return "$responseCode: $body"
}
