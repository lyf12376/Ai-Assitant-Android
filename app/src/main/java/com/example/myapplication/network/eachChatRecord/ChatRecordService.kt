package com.example.myapplication.network.eachChatRecord

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ChatRecordService {
    @GET("api/conversation/load")
    suspend fun getChatRecord(@Header("Authorization") token:String,@Query("id") id:Int): ChatRecord
}