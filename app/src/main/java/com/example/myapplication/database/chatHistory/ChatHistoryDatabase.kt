package com.example.myapplication.database.chatHistory

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ChatHistory::class], version = 1, exportSchema = false)
@TypeConverters(ChatHistoryConverters::class)
abstract class ChatHistoryDatabase : RoomDatabase() {
    abstract fun chatHistoryDao(): ChatHistoryDao

    companion object{
        @Volatile
        private var INSTANCE: ChatHistoryDatabase? = null
        fun getDatabase(context: Context): ChatHistoryDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChatHistoryDatabase::class.java,
                    "Game"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}