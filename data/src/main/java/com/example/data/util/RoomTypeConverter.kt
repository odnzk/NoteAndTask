package com.example.data.util

import androidx.room.TypeConverter
import java.util.*

class RoomTypeConverter {

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromTimestamp(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

}
