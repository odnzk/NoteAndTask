package com.noteapp.repository

import android.database.sqlite.SQLiteConstraintException
import com.example.domain.util.exceptions.NotUniqueFieldException
import com.example.domain.util.exceptions.UniqueConstraints

abstract class BaseRepository {
    protected suspend fun <T> doRequest(
        constraints: UniqueConstraints? = null,
        request: suspend () -> T
    ): Result<T> {
        return try {
            val data = request()
            Result.success(data)
        } catch (ex: SQLiteConstraintException) {
            constraints?.let { Result.failure(NotUniqueFieldException(it)) } ?: Result.failure(ex)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    protected suspend fun <T> doRequest(
        request: suspend () -> T
    ): Result<T> {
        return try {
            val data = request()
            Result.success(data)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}
