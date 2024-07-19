package com.example.myapplication.network.uploadFile

import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadFileService {
    @Multipart
    @POST("upload")
    suspend fun uploadFile(
        @Header("token") token:String,
        @Part file: MultipartBody.Part
    ): UploadResponse
}