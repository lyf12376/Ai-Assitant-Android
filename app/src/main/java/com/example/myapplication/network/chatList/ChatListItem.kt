package com.example.myapplication.network.chatList

data class ChatListItem (
    val auth: Boolean,
    val user_id: Int,
    val id: Int,
    val name: String,
    val message: String?,
    val model: String,
    val enable_web: Boolean,
    val shared: Boolean,
    val context: Int,
    val auto_use_coin: Boolean
)