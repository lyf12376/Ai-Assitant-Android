package com.example.myapplication.network.chatList

data class ChatListResponse(
    val data:List<ChatListItem>,
    val message:String,
    val status:Boolean
)
