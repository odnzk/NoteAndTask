package com.noteapp.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Update

interface BaseDao<T> {
    @Insert(onConflict = REPLACE)
    suspend fun insert(item: T): Long

    @Update
    suspend fun update(item: T): Long

    @Delete
    suspend fun delete(item: T)
}
