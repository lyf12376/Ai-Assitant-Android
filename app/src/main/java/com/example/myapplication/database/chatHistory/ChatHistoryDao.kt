package com.example.myapplication.database.chatHistory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatHistoryDao {
    @Insert
    suspend fun insertChatHistory(chatHistory: ChatHistory)

    @Query("UPDATE CHAT_HISTORY SET content = :historyList WHERE id = :id")
    suspend fun updateChatHistory(id:Int,historyList:List<ChatMessage>)

    @Query("SELECT * FROM CHAT_HISTORY WHERE id = :id")
    suspend fun getChatHistoryById(id:Int):ChatHistory?

    @Query("SELECT * FROM CHAT_HISTORY")
    fun getAllChatHistory():Flow<List<ChatHistory>>

    @Query("DELETE FROM CHAT_HISTORY WHERE id = :id")
    suspend fun deleteChatHistoryById(id:Int):Int

}