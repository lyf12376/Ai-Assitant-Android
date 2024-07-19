package com.example.myapplication.network.chat

import kotlinx.serialization.*

@Serializable
data class ChatMessage(
    val token: String,
    val id: Int,
    val type: String,
    val message: String,
    val web: Boolean,
    val model: String,
    val context: Int,
    val auto_use_coin: Boolean,
    val ignore_context: Boolean,
    val max_tokens: Int,
    val temperature: Double,
    val top_p: Double,
    val top_k: Int,
    val presence_penalty: Double,
    val frequency_penalty: Double,
    val repetition_penalty: Double
)
