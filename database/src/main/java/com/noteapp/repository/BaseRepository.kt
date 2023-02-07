package com.noteapp.repository

abstract class BaseRepository {

    protected suspend fun <T> doRequest(request: suspend () -> T): Result<T> {
        return try {
            val data = request()
            Result.success(data)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}
