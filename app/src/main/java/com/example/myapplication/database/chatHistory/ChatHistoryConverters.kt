package com.example.myapplication.database.chatHistory

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class ChatHistoryConverters {
    @TypeConverter
    fun fromJson(value: String): List<ChatMessage> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun toJson(value: List<ChatMessage>): String {
        return Json.encodeToString(value)
    }
}
