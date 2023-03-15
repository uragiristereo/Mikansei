package com.uragiristereo.mejiboard.core.model.result

import retrofit2.Response

fun failedResponseFormatter(response: Response<*>): String {
    // TODO: Format response properly
    return "${response.code()} ${response.message()}: ${response.errorBody()}"
}
