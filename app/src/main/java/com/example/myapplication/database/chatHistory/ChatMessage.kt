package com.example.myapplication.database.chatHistory

import com.example.myapplication.network.eachChatRecord.Block
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val content: List<Block>,
    val role:String, // assistant,user
)

