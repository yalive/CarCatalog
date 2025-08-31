package com.capgemini.carcatalog.common

import javax.inject.Inject

class NetworkRunner @Inject constructor() {

    suspend fun <T> execute(
        request: suspend () -> T
    ): Result<T> = try {
        val response = request()
        Result.Success(response)
    } catch (e: Exception) {
        Result.Error(e)
    }

}