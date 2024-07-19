package com.example.myapplication.network.chatList

import retrofit2.http.GET
import retrofit2.http.Header

interface ChatListService {
    @GET("api/conversation/list")
    suspend fun getChatList(@Header("Authorization") token:String):ChatListResponse
}