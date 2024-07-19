package com.example.myapplication.network.chat

import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val conversation: Int,
    val quota: Double,
    val keyword: String,
    val message: String,
    val end: Boolean,
    val plan: Boolean
)
