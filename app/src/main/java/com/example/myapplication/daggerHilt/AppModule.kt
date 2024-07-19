package com.example.myapplication.daggerHilt

import android.content.Context
import com.coder.vincent.sharp_retrofit.call_adapter.flow.FlowCallAdapterFactory
import com.example.myapplication.network.chatList.ChatListService
import com.example.myapplication.network.eachChatRecord.ChatRecordService
import com.example.myapplication.network.uploadFile.UploadFileService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(3000L, TimeUnit.MILLISECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(interceptor = loggingInterceptor)
            .build()
    }

    /**
     * 在目录中使用Hilt提供多个相对类型的实例对象，通过"@Named"注解进行区分
     * 也可以通过"@Qualifier"限定符定义新的注解进行区分*/

    @Singleton
    @Provides
    @Named("upload")
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://blob.datapipe.top")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(FlowCallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    @Named("chat")
    fun provideRetrofitChat(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://c.datapipe.top")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(FlowCallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideUploadFileService(@Named("upload") retrofit: Retrofit): UploadFileService {
        return retrofit.create(UploadFileService::class.java)
    }

    @Singleton
    @Provides
    fun provideChatListService(@Named("chat") retrofit: Retrofit):ChatListService{
        return retrofit.create(ChatListService::class.java)
    }

    @Singleton
    @Provides
    fun provideChatRecordService(@Named("chat") retrofit: Retrofit):ChatRecordService{
        return retrofit.create(ChatRecordService::class.java)
    }
}