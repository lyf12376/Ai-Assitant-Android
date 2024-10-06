package com.example.myapplication.network.delete

import retrofit2.http.GET

interface DeleteRecordService {
    @GET("api/conversation/delete")
    suspend fun deleteRecord():DeleteRecordResponse
}