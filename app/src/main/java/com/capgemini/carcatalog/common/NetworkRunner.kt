package com.capgemini.carcatalog.common

class NetworkRunner {

    suspend fun <T> execute(
        request: suspend () -> T
    ): Result<T> = try {
        val response = request()
        Result.Success(response)
    } catch (e: Exception) {
        Result.Error(e)
    }

}