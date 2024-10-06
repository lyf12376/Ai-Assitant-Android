package com.example.myapplication.page.recordPage

import android.util.Log
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Const.Token
import com.example.myapplication.database.chatHistory.ChatHistory
import com.example.myapplication.database.chatHistory.ChatHistoryDao
import com.example.myapplication.network.chatList.ChatListService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val chatHistoryDao: ChatHistoryDao,
) : ViewModel() {

    private val _conversationHistory = MutableStateFlow<List<ChatHistory>>(emptyList())
    val conversationHistory = _conversationHistory.asStateFlow()

    init {
        viewModelScope.launch {
            chatHistoryDao.getAllChatHistory()
                .flowOn(Dispatchers.IO)
                .collect { chatHistories ->
                    _conversationHistory.value = chatHistories
                    Log.d("RecordViewModel", "Collected chat histories: $chatHistories")
                }
        }
    }
}