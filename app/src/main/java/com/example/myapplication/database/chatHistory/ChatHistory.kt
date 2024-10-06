package com.example.myapplication.database.chatHistory

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.json.Json

@Entity(tableName = "chat_history")
data class ChatHistory(
    @PrimaryKey
    val id:Int,
    val model:String,
    val content:List<ChatMessage>
)
//{
//    "id" : 0,
//    "message" : ""
//     "role" : 0,
//     "model" : ""
//}
